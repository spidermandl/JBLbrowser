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

import com.jbl.browser.model.ErrorInfo;
import com.jbl.browser.tools.BusinessCallback;
import com.jbl.browser.tools.BusinessTool;
import com.jbl.browser.utils.UrlUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * wifi connect service
 * @author yyjoy-mac3
 *
 */
public class WIFIService extends Service {

	private List<ScanResult> wifiList;
	private WifiManager wifiManager;
	private ConnectivityManager mConnectivityManager;
	private TelephonyManager mTelephonyManager;
	private List<String> passableHotsPot;
	//private WifiReceiver wifiReceiver;
	
	private WIFIStatus wStatus;
	private String logonsessid;
	
	private WifiBinder mWifiBinder = new WifiBinder();
	
	BusinessCallback callback=new BusinessCallback() {
		
		@Override
		public void fail(ErrorInfo e) {
			//isAuthing=false;
			Log.e("fail", "fail");
			wStatus=WIFIStatus.FAILED;
		}
		
		@Override
		public void error(ErrorInfo e) {
			//isAuthing=false;
			Log.e("fail", "fail");
			wStatus=WIFIStatus.FAILED;
		}
		
		@Override
		public void complete(Bundle values) {
			Log.e("complete", "complete");
			//isAuthing=false;
			wStatus=WIFIStatus.AUTHORITHED;
		}
	};
	
	public interface IWifiService{
		public WIFIStatus getWifiStatus();
		public void startConnection();
	}
	
    public class WifiBinder extends Binder implements IWifiService{

		@Override
		public WIFIStatus getWifiStatus() {
			// TODO Auto-generated method stub
			return wStatus;
		}

		@Override
		public void startConnection() {
			synchronized (this) {
				switchToSpecWifi();
			}
			
		}

    }
    
	/**
	 * 网络状态设置类型
	 */
	public static enum WIFIStatus{
		IDLE(0),//没有状态
		UNREACH(1), //没有cmcc
		CHECKED(2), //有cmcc但没有连上
		DATASTREAM(3),//数据流量
		CONNECTED(4), //cmcc连接上
		FAILED(5),//CMCC验证失败
	    AUTHORITHED(6);//CMCC验证通过
		// 定义私有变量
		private int nCode;

		// 构造函数，枚举类型只能为私有
		private WIFIStatus(int _nCode) {
			this.nCode = _nCode;
		}

		@Override
		public String toString() {
			return String.valueOf("wifi_status_type_"+this.nCode);
		}
	};
	boolean isAuthing=false;
	Handler mHanlder =new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(!isAuthing){
				isAuthing=true;
				Log.e("登录验证开始", "登录验证开始");
				
				BusinessTool.getInstance().getLogin(callback);
			}
		};
	};
	
	/**
	 * 检测wifi网路连接ssid
	 */
	Runnable wifiTestRun = new Runnable() {
		
		@Override
		public void run() {
			while(true){
				WifiInfo info = wifiManager.getConnectionInfo();
		        String wifiId = info != null ? info.getSSID() : null;
		        if(wifiId!=null&&wifiId.contains(UrlUtils.HOTPOT_NAME)){
		        	mHanlder.sendEmptyMessage(0);
		        	wStatus=WIFIStatus.CONNECTED;
//		        	wifiThread=null;
//		        	return;
		        }
		        
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	};
	
	Thread wifiThread=null;
	
	@Override
	public void onCreate() {
		IntentFilter mFilter = new IntentFilter();  
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);  
        mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		//this.registerReceiver(wifiReceiver, mFilter);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mConnectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mTelephonyManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		//wifiReceiver=new WifiReceiver();
		startSpecificWifi();
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
		BusinessTool.getInstance().eduLogout();
		super.onDestroy();
	}

	/**
	 * 检测edu wifi
	 */
	public void startSpecificWifi(){
		
		wifiList = wifiManager.getScanResults();
		if (wifiList == null || wifiList.size() == 0){
			wStatus = WIFIStatus.UNREACH;
			return;
		}
		onReceiveNewNetworks(wifiList);
	}
	
	/* 当搜索到新的wifi热点时判断该热点是否符合规格 */
	private void onReceiveNewNetworks(List<ScanResult> wifiList) {
		wStatus = WIFIStatus.UNREACH;
		passableHotsPot = new ArrayList<String>();
		for (ScanResult result : wifiList) {
			System.out.println(result.SSID);
			if ((result.SSID).contains(UrlUtils.HOTPOT_NAME)){
				wStatus = WIFIStatus.CHECKED;
		        WifiInfo info = wifiManager.getConnectionInfo();
		        String wifiId = info != null ? info.getSSID() : null;
		        if(wifiId!=null&&!wifiId.contains(UrlUtils.HOTPOT_NAME)){//判断wifi是否已经连接
				    passableHotsPot.add(result.SSID);
		        }else{
		        	wStatus = WIFIStatus.CONNECTED;
		        	//BusinessTool.getInstance().getLogin(callback);//直接验证
		        }
			}
		}

	}

	/**
	 * 连接到指定wifi（cmcc wifi）
	 */
	public void switchToSpecWifi() {
		if (wStatus!=WIFIStatus.CHECKED)
			return;
		WifiConfiguration wifiConfig = this.setWifiParams(passableHotsPot.get(0));
		int wcgID = wifiManager.addNetwork(wifiConfig);
		boolean flag = wifiManager.enableNetwork(wcgID, true);
		if(flag){
			if(wifiThread==null){
				wifiThread=new Thread(wifiTestRun);
				wifiThread.start();
			}
		}
	}

	/**
	 * 连接到2G／3G数据网络
	 */
	public void switchToDataRoam(){
		wifiManager.setWifiEnabled(false);//关闭wifi
		
        try {
        	Method mMethod=ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        	mMethod.invoke(mConnectivityManager, true);
        } catch (Exception e) {
        	
        }
            
    
	}
	/* 设置要连接的热点的参数 */
	private WifiConfiguration setWifiParams(String ssid) {
		WifiConfiguration config = new WifiConfiguration();     
        config.allowedAuthAlgorithms.clear();   
        config.allowedGroupCiphers.clear();   
        config.allowedKeyManagement.clear();   
        config.allowedPairwiseCiphers.clear();   
        config.allowedProtocols.clear();   
        config.SSID = "\"" + ssid + "\"";     
        
        //config.wepKeys[0] = "";   
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);   
        //config.wepTxKeyIndex = 0;
  
		return config;
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
