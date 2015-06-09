package com.jbl.browser.activity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jbl.browser.JBLApplication;
import com.jbl.browser.db.UserInfoDao;
import com.jbl.browser.model.ErrorInfo;
import com.jbl.browser.tools.BusinessCallback;
import com.jbl.browser.tools.BusinessTool;
import com.jbl.browser.utils.StringUtils;
import com.jbl.browser.utils.UrlUtils;
import com.jbl.browser.wifi.AuthFailedState;
import com.jbl.browser.wifi.CMCCState;
import com.jbl.browser.wifi.ExceptionState;
import com.jbl.browser.wifi.HeartBeatState;
import com.jbl.browser.wifi.IState;
import com.jbl.browser.wifi.InitState;
import com.jbl.browser.wifi.MobileDataState;
import com.jbl.browser.wifi.NoCMCCState;
import com.jbl.browser.wifi.NormalWifiState;
import com.jbl.browser.wifi.OfflineState;
import com.jbl.browser.wifi.WifiMachine;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * wifi connect service
 * @author yyjoy-mac3
 *
 */
public class WIFIService extends Service{

	private List<ScanResult> wifiList;//范围内wifi列表
	private WifiManager wifiManager;
	private ConnectivityManager mConnectivityManager;
	private TelephonyManager mTelephonyManager;
	private List<String> passableHotsPot;//指定免费wifi列表
	//private WifiReceiver wifiReceiver;
	
	/**
	 * cmcc wifi免费认证会话id
	 */
	private String logonsessid;
	/**
	 * WIFI帐号编号
	 */
	private String wifi_id;
	/**
	 * 用户编号
	 */
	private String uid;
	/**
	 * WIFI帐号
	 */
	private String cmcc_account;
	/**
	 * WIFI密码
	 */
	private String cmcc_passwd;
	/**
	 * 上一次心跳时间
	 */
	private long lastHeartBeatTime;
	/**
	 * 开始心跳时间
	 */
	private long startHeartBeatTime;
	/**
	 * 免费wifi可用时长倒计
	 */
	private long countDownTime = 30*60*1000;
	
	/**
	 * 最大可上网时间
	 */
	private long maxTime=60*60*1000;
	/**
	 * 手机号
	 */
	private String phoneNumber;
	
	private WifiBinder mWifiBinder = new WifiBinder();
	
	/**
	 * 状态机管理器
	 */
	private WifiMachine stateMachine;
	
	public interface IWifiService{
		public IState getWifiStatus();//获取当前的状态
		public void startConnection();//开始进入wifi验证过程
		public void changeState(Class state);//外部干预状态改变，用于测试
		public void stopConnection();//断开cmcc连接
		public long getOnlineTime();//获取在线时间
	}
	
	/**
	 * 连接wifi service的通道
	 * @author Desmond
	 *
	 */
    public class WifiBinder extends Binder implements IWifiService{

		@Override
		public IState getWifiStatus() {
			// TODO Auto-generated method stub
			return stateMachine.getState();
		}

		@Override
		public void startConnection() {
			stateMachine.changeState(new InitState(WIFIService.this));
		}

		@Override
		public void changeState(Class state) {
			try {
				stateMachine.changeState((IState)(state.getDeclaredConstructor(WIFIService.class).newInstance(WIFIService.this)));
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		public void stopConnection() {
			startHeartBeatSync("stop");
			
		}

		@Override
		public long getOnlineTime() {
			// TODO Auto-generated method stub
			return 0;
		}

    }
	
    /**
     * 状态检测机
     */
	Handler statusHandler = new Handler(){
		public void handleMessage(Message msg) {
			IState state=stateMachine.getState();
			if(state instanceof InitState){//初始状态
				NetworkInfo mobileData = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE),
				wifiData = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				
				if (!mobileData.isAvailable()) {// 没有数据网络
					stateMachine.setError("数据网络不可用");
				    stateMachine.changeState(new ExceptionState(WIFIService.this));
					return;
				}
				if (mobileData.isConnected() && !wifiData.isConnected()) {
					stateMachine.changeState(new MobileDataState(WIFIService.this));
					return;
				}
				stateMachine.runState();
			}else if(state instanceof MobileDataState){//数据网络状态
				stateMachine.changeState(new NormalWifiState(WIFIService.this));
			}else if(state instanceof NormalWifiState){//普通网络状态
				WifiInfo info = wifiManager.getConnectionInfo();
		        String wifiId = info != null ? info.getSSID() : null;
		        if(wifiId!=null&&wifiId.contains(UrlUtils.HOTPOT_NAME)){//cmcc 已经连接上
		        	stateMachine.changeState(new CMCCState(WIFIService.this));
					return;
		        }
		        
				NetworkInfo wifiData = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if(wifiData.isConnected()){//wifi已经连接上
					stateMachine.runState();
				}

				statusLooping(500);//循环状态
				
			}else if(state instanceof NoCMCCState){//no cmcc状态
				
			}else if(state instanceof CMCCState){//cmcc状态 未验证
				
			}else if(state instanceof AuthFailedState){//cmcc验证失败
				
			}else if(state instanceof HeartBeatState){//心跳状态
				stateMachine.runState();
			}
			else if(state instanceof OfflineState){//cmcc 下线
				
			}
		};
	};
	
	
	@Override
	public void onCreate() {
//		IntentFilter mFilter = new IntentFilter();  
//      mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);  
//      mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//      mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//		this.registerReceiver(wifiReceiver, mFilter);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mConnectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mTelephonyManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        stateMachine=new WifiMachine();
        stateMachine.setState(new InitState(this));//设置第一个状态
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mWifiBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		if (logonsessid != null) {
			WifiInfo info = wifiManager.getConnectionInfo();
			BusinessTool.getInstance().getLogout(new BusinessCallback() {

				@Override
				public void fail(ErrorInfo e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void error(ErrorInfo e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void complete(Bundle values) {
					// TODO Auto-generated method stub

				}
			}, info.getBSSID(), int2ip(info.getIpAddress()), logonsessid);
		}
		//unregisterReceiver(wifiReceiver);
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 开始连接cmcc wifi
	 */
	public void startCMCCWifi(){
		
		wifiList = wifiManager.getScanResults();
		if (wifiList == null || wifiList.size() == 0){
			stateMachine.changeState(new NoCMCCState(this));
			return;
		}
		
		passableHotsPot = new ArrayList<String>();
		for (ScanResult result : wifiList) {
			WifiInfo info = wifiManager.getConnectionInfo();
			String wifiId = info != null ? info.getSSID() : null;
			if (wifiId != null && !wifiId.contains(UrlUtils.HOTPOT_NAME)) {// 判断wifi是否已经连接
				passableHotsPot.add(result.SSID);
				connectCMCCWifi();
			} else {//已经连接
				stateMachine.changeState(new CMCCState(this));
			}
		}

	}
	
	/**
	 * 状态保持
	 * @param delay
	 */
	public void statusLooping(int delay){
		statusHandler.sendEmptyMessageDelayed(0, delay);
	}

	/**
	 * 连接2g／3g数据网络
	 */
	public void startMoblieData() {
		wifiManager.setWifiEnabled(false);//关闭wifi
		
        try {
        	Method mMethod=ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        	mMethod.invoke(mConnectivityManager, true);
        	statusLooping(0);
        } catch (Exception e) {
        	stateMachine.changeState(new NormalWifiState(this));
        }
		
	}  
	
	/**
	 * 打开wifi 连接普通wifi
	 */
	public void startWifiConnection(){
		wifiManager.setWifiEnabled(true);
	}
	
	/**
	 * 请求免费cmcc wifi登录账号
	 */
	public void requestAccount(){
		BusinessTool.getInstance().getWifiAccount(new BusinessCallback() {
			
			@Override
			public void fail(ErrorInfo e) {
			    stateMachine.setError("cmcc wifi 账户获取失败");
			    stateMachine.changeState(new ExceptionState(WIFIService.this));
				
			}
			
			@Override
			public void error(ErrorInfo e) {
				stateMachine.setError("cmcc wifi 账户获取失败");
			    stateMachine.changeState(new ExceptionState(WIFIService.this));
				
			}
			
			@Override
			public void complete(Bundle values) {
				
				try {
					/**
					 * 解析返回
					 */
					JSONObject json= new JSONObject(values.getString(StringUtils.DATA));
					json=json.getJSONObject("data");
					uid=json.getString("uid");
					cmcc_account=json.getString("wifiaccount");
					cmcc_passwd=json.getString("wifipass");
					countDownTime=json.getLong("times");
					phoneNumber=json.getString("mobile");
					statusLooping(0);
//					Message msg=new Message();
//					msg.obj = WifiAction.TOWIFIACCOUNT;
//					checkHandler.sendMessage(msg);
				} catch (JSONException e) {
					stateMachine.setError("cmcc wifi 账户获取失败");
				    stateMachine.changeState(new ExceptionState(WIFIService.this));
					e.printStackTrace();
				}
				
			}
		},new UserInfoDao(JBLApplication.getInstance().getApplicationContext()).getPhoneID(
				BusinessTool.getDeviceID(JBLApplication.getInstance().getApplicationContext()))
				,System.currentTimeMillis()/1000);
	}

	/**
	 * cmcc连接成功，发送cmcc wifi免费使用验证
	 */
	public void sendCMCCAuth(){

		BusinessTool.getInstance().getLogin(new BusinessCallback() {
			
			@Override
			public void fail(ErrorInfo e) {
				stateMachine.changeState(new AuthFailedState(WIFIService.this));
				
			}
			
			@Override
			public void error(ErrorInfo e) {
				stateMachine.changeState(new AuthFailedState(WIFIService.this));
			}
			
			@Override
			public void complete(Bundle values) {
				stateMachine.changeState(new HeartBeatState(WIFIService.this));
			}
		});
	}
	
	/**
	 * 开启发送心跳包请求
	 * @param type
	 */
	public void startHeartBeatSync(final String type){
		if(type.contains("start")){
			startHeartBeatTime=lastHeartBeatTime=System.currentTimeMillis();
			countDownTime=0;
		}
		BusinessTool.getInstance().sendHeartBeatSync(
				new BusinessCallback() {

					@Override
					public void fail(ErrorInfo e) {
						stateMachine.setError("心跳包请求错误");
						stateMachine.changeState(new OfflineState(WIFIService.this));

					}

					@Override
					public void error(ErrorInfo e) {
						stateMachine.setError("心跳包请求错误");
						stateMachine.changeState(new OfflineState(WIFIService.this));

					}

					@Override
					public void complete(Bundle values) {
						long currentTime = System.currentTimeMillis();
						if (currentTime - lastHeartBeatTime > 120000) {// 超时两分钟
							stateMachine.setError("心跳包超时");
							stateMachine.changeState(new OfflineState(WIFIService.this));
						} else {
							try {
								JSONObject json = new JSONObject(values.getString(StringUtils.DATA));
								json = json.getJSONObject("data");
								String type=json.getString("type");
								if(type.contains("stop")){//结束心跳包
									cmccLogout();//cmcc下线
									return;
								}
								lastHeartBeatTime = currentTime;//纪录此次心跳包时间
								countDownTime=currentTime-startHeartBeatTime;
								if(json.getLong("times")<=0){//时间到下线
									stateMachine.changeState(new OfflineState(WIFIService.this));
								}else{//继续心跳
									statusHandler.sendEmptyMessageDelayed(0, 60000);
								}
							} catch (JSONException e) {
								stateMachine.setError("心跳包请求错误");
								stateMachine.changeState(new OfflineState(WIFIService.this));
								e.printStackTrace();
							}
						}
					}
				},
				type,
				wifi_id,
				uid,
				new UserInfoDao(JBLApplication.getInstance().getApplicationContext()).getPhoneID(BusinessTool
								.getDeviceID(JBLApplication.getInstance().getApplicationContext())),
				System.currentTimeMillis(), countDownTime/1000);
		
	}

	/**
	 * cmcc免费wifi 下线
	 */
	private void cmccLogout(){
		BusinessTool.getInstance().eduLogout(new BusinessCallback() {
			
			@Override
			public void fail(ErrorInfo e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void error(ErrorInfo e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void complete(Bundle values) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/**
	 * 连接到指定wifi（cmcc wifi）
	 */
	private void connectCMCCWifi() {
		/**设置要连接的热点的参数 */
		WifiConfiguration config = new WifiConfiguration();     
        config.allowedAuthAlgorithms.clear();   
        config.allowedGroupCiphers.clear();   
        config.allowedKeyManagement.clear();   
        config.allowedPairwiseCiphers.clear();   
        config.allowedProtocols.clear();   
        config.SSID = "\"" + passableHotsPot.get(0) + "\"";     
        
        //config.wepKeys[0] = "";   
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);   
        //config.wepTxKeyIndex = 0;
        /**********************/
		int wcgID = wifiManager.addNetwork(config);
		boolean flag = wifiManager.enableNetwork(wcgID, true);
		if(flag){
			statusLooping(0);
		}
	}
	

    
	private String int2ip(int ipInt) {  
        StringBuilder sb = new StringBuilder();  
        sb.append(ipInt & 0xFF).append(".");  
        sb.append((ipInt >> 8) & 0xFF).append(".");  
        sb.append((ipInt >> 16) & 0xFF).append(".");  
        sb.append((ipInt >> 24) & 0xFF);  
        return sb.toString();  
    }


}
