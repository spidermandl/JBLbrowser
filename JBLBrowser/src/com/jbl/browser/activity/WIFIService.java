package com.jbl.browser.activity;

import java.util.ArrayList;
import java.util.List;

import com.jbl.browser.model.ErrorInfo;
import com.jbl.browser.tools.BusinessCallback;
import com.jbl.browser.tools.BusinessTool;
import com.jbl.browser.utils.UrlUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * wifi connect service
 * @author yyjoy-mac3
 *
 */
public class WIFIService extends Service {

	private List<ScanResult> wifiList;
	private WifiManager wifiManager;
	private List<String> passableHotsPot;
	private WifiReceiver wifiReceiver;
	private boolean isConnected=false;
	private String logonsessid;
	
	@Override
	public void onCreate() {
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiReceiver=new WifiReceiver();
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
		return null;
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
		unregisterReceiver(wifiReceiver);
		return super.onUnbind(intent);
	}
	
	/* 监听热点变化 */
	private final class WifiReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			startSpecificWifi();
		}
	}

	public void startSpecificWifi(){
		wifiList = wifiManager.getScanResults();
		if (wifiList == null || wifiList.size() == 0 || isConnected)
			return;
		onReceiveNewNetworks(wifiList);
	}
	/* 当搜索到新的wifi热点时判断该热点是否符合规格 */
	public void onReceiveNewNetworks(List<ScanResult> wifiList) {
		passableHotsPot = new ArrayList<String>();
		for (ScanResult result : wifiList) {
			System.out.println(result.SSID);
			if ((result.SSID).contains(UrlUtils.HOTPOT_NAME)){
		        WifiInfo info = wifiManager.getConnectionInfo();
		        String wifiId = info != null ? info.getSSID() : null;
		        if(wifiId!=null&&!wifiId.contains(UrlUtils.HOTPOT_NAME)){//判断wifi是否已经连接
				    passableHotsPot.add(result.SSID);
		        }
			}
		}
		synchronized (this) {
			connectToHotpot();
		}
	}

	/* 连接到热点 */
	public void connectToHotpot() {
		if (passableHotsPot == null || passableHotsPot.size() == 0)
			return;
		WifiConfiguration wifiConfig = this.setWifiParams(passableHotsPot.get(0));
		int wcgID = wifiManager.addNetwork(wifiConfig);
		boolean flag = wifiManager.enableNetwork(wcgID, true);
		isConnected = flag;
		if(isConnected){
			WifiInfo info=wifiManager.getConnectionInfo();
			BusinessTool.getInstance().getLogin(new BusinessCallback() {
				
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
			}, info.getBSSID(), int2ip(info.getIpAddress()));
		}
		System.out.println("connect success? " + flag);
	}

	/* 设置要连接的热点的参数 */
	public WifiConfiguration setWifiParams(String ssid) {
		WifiConfiguration apConfig = new WifiConfiguration();
		apConfig.SSID = "\"" + ssid + "\"";
        apConfig.wepKeys[0] = "";  
        apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
        apConfig.wepTxKeyIndex = 0;  
            
		//apConfig.preSharedKey = "\"12122112\"";
//		apConfig.hiddenSSID = true;
//		apConfig.status = WifiConfiguration.Status.ENABLED;
//		apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//		apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//		apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//		apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//		apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//		apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		return apConfig;
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
