package com.jbl.browser.activity;

import java.io.IOException;
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
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.StringUtils;
import com.jbl.browser.utils.UrlUtils;
import com.jbl.browser.wifi.AuthFailedState;
import com.jbl.browser.wifi.CMCCState;
import com.jbl.browser.wifi.ExceptionState;
import com.jbl.browser.wifi.FreeWifiState;
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
import android.text.format.Time;
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
	 * 手机号
	 */
	private String phoneNumber;

	/**
	 * 扫描2g／3g thread　
	 */
	Thread mobileDataThread=null;
	/**
	 * 普通wifi检测 thread
	 */
	Thread normalWifiThread=null;
	/**
	 * 连接cmcc thread
	 */
	Thread conCMCCThread=null;
	/**
	 * 发送心跳包loop线程
	 */
	Thread heartBeatLoopThread = null;
	
	private WifiBinder mWifiBinder = new WifiBinder();
	
	/**
	 * 状态机管理器
	 */
	private WifiMachine stateMachine;
	
	public interface IWifiService{
		public IState getWifiStatus();//获取当前的状态
		public void startConnection();//开始进入wifi验证过程
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
			stateMachine.runState();
			
		}

    }
	
	
	/**
	 * 执行动作类型
	 */
	public static enum WifiAction{
		TOMOBILEDATA(0),//连接数据网络
		TOWIFIACCOUNT(1),//请求wifi登录账号
		TONORMALWIFI(2),//连接任意wifi　
		TOCMCCWIFI(3),//连接cmcc wifi
		TOHEARTBEAT(4),//发心跳包
		TOOFFLINE(5);//下线
		// 定义私有变量
		private int nCode;

		// 构造函数，枚举类型只能为私有
		private WifiAction(int _nCode) {
			this.nCode = _nCode;
		}

		@Override
		public String toString() {
			return String.valueOf("wifi_status_type_"+this.nCode);
		}
		
	};
	
	/**
	 * 检测线程执行结果处理
	 */
	Handler checkHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch ((WifiAction)msg.obj) {
			case TOMOBILEDATA:
				stateMachine.changeState(new MobileDataState(WIFIService.this));
				mobileDataThread=null;
				break;
			case TOWIFIACCOUNT:
				stateMachine.changeState(new NormalWifiState(WIFIService.this));
				break;
			case TONORMALWIFI:
				startCMCCWifi();
				normalWifiThread=null;
				break;
			case TOCMCCWIFI:
				stateMachine.changeState(new CMCCState(WIFIService.this));
			    conCMCCThread=null;
				break;
			case TOHEARTBEAT:
				startHeartBeatSync("heartbeat");
				break;
			case TOOFFLINE:
				stateMachine.changeState(new OfflineState(WIFIService.this));
				heartBeatLoopThread=null;
				break;
			default:
				break;
			}
		};
	};
	/**
	 * 网络检测线程runnable
	 * 判断当前网路状态
	 */
	class CheckRunnable implements Runnable{

		WifiAction action;
		public CheckRunnable (WifiAction a){
			action = a;
		}
		@Override
		public void run() {
			while (true) {
				NetworkInfo mobileData = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE),
				wifiData = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				switch (action) {
				case TOMOBILEDATA:
					if(!mobileData.isAvailable()){//没有数据网络
						stateMachine.setError("数据网络不可用");
						return;
					}
					if (mobileData.isConnected() && !wifiData.isConnected()) {
						Message msg=new Message();
						msg.obj=WifiAction.TOMOBILEDATA;
						checkHandler.sendMessage(msg);
						return;
					}
					break;
				case TONORMALWIFI:
					if(wifiData.isConnected()){
						Message msg=new Message();
						msg.obj=WifiAction.TONORMALWIFI;
						checkHandler.sendMessage(msg);
						return;
					}
					break;
				case TOCMCCWIFI:
					WifiInfo info = wifiManager.getConnectionInfo();
			        String wifiId = info != null ? info.getSSID() : null;
			        if(wifiId!=null&&wifiId.contains(UrlUtils.HOTPOT_NAME)){
			        	Message msg=new Message();
						msg.obj=WifiAction.TOCMCCWIFI;
						checkHandler.sendMessage(msg);
						return;
			        }
					break;
				default:
					break;
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
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
	 * 
	 */
	public void openWifiSetting(){
		wifiManager.setWifiEnabled(true);
	}
	/**
	 * 连接2g／3g数据网络
	 */
	public void startMoblieData() {
		wifiManager.setWifiEnabled(false);//关闭wifi
		
        try {
        	Method mMethod=ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        	mMethod.invoke(mConnectivityManager, true);
        	if(mobileDataThread==null){
        	    mobileDataThread=new Thread(new CheckRunnable(WifiAction.TOMOBILEDATA));
        	    mobileDataThread.start();
        	}
        } catch (Exception e) {
        	stateMachine.changeState(new NormalWifiState(this));
        }
		
	}  
	
	/**
	 * 打开wifi 连接普通wifi
	 */
	public void startWifiConnection(){
		wifiManager.setWifiEnabled(true);
		if(normalWifiThread==null){
		    normalWifiThread=new Thread(new CheckRunnable(WifiAction.TONORMALWIFI));
		    normalWifiThread.start();
		}
	}
	
	/**
	 * 请求免费cmcc wifi登录账号
	 */
	public void requestAccount(){
		BusinessTool.getInstance().getWifiAccount(new BusinessCallback() {
			
			@Override
			public void fail(ErrorInfo e) {
			    stateMachine.setError("cmcc wifi 账户获取失败");
				
			}
			
			@Override
			public void error(ErrorInfo e) {
				stateMachine.setError("cmcc wifi 账户获取失败");
				
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
					cmcc_account=json.getString("account");
					cmcc_passwd=json.getString("pass");
					countDownTime=json.getLong("times");
					phoneNumber=json.getString("mobile");
					Message msg=new Message();
					msg.obj = WifiAction.TOWIFIACCOUNT;
					checkHandler.sendMessage(msg);
				} catch (JSONException e) {
					stateMachine.setError("cmcc wifi 账户获取失败");
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
				stateMachine.changeState(new FreeWifiState(WIFIService.this));
				
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
		if (heartBeatLoopThread == null) {
			heartBeatLoopThread = new Thread(new Runnable() {

				@Override
				public void run() {
					BusinessTool.getInstance().sendHeartBeatSync(
							new BusinessCallback() {

								@Override
								public void fail(ErrorInfo e) {
									stateMachine.setError("心跳包请求错误");
									Message msg = new Message();
									msg.obj = WifiAction.TOOFFLINE;
									checkHandler.sendMessage(msg);

								}

								@Override
								public void error(ErrorInfo e) {
									stateMachine.setError("心跳包请求错误");
									Message msg = new Message();
									msg.obj = WifiAction.TOOFFLINE;
									checkHandler.sendMessage(msg);

								}

								@Override
								public void complete(Bundle values) {
									long currentTime = System.currentTimeMillis();
									if (currentTime - lastHeartBeatTime > 120000) {// 超时两分钟
										stateMachine.setError("心跳包超时");
										Message msg = new Message();
										msg.obj = WifiAction.TOOFFLINE;
										checkHandler.sendMessage(msg);
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
												Message msg = new Message();
												msg.obj = WifiAction.TOOFFLINE;
												checkHandler.sendMessage(msg);
											}else{//继续心跳
												Message msg = new Message();
												msg.obj = WifiAction.TOHEARTBEAT;
												checkHandler.sendMessageDelayed(msg, 60000);
											}
										} catch (JSONException e) {
											stateMachine.setError("心跳包请求错误");
											Message msg = new Message();
											msg.obj = WifiAction.TOOFFLINE;
											checkHandler.sendMessage(msg);
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
			});
			heartBeatLoopThread.start();
		}
		
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
			if(conCMCCThread==null){
			    conCMCCThread=new Thread(new CheckRunnable(WifiAction.TOCMCCWIFI));
			    conCMCCThread.start();
			}
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
