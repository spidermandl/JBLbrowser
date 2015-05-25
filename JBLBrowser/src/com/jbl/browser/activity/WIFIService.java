package com.jbl.browser.activity;

import java.io.IOException;
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
import android.content.BroadcastReceiver;
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
import android.os.IBinder;

/**
 * wifi connect service
 * @author yyjoy-mac3
 *
 */
@Deprecated
public class WIFIService extends Service {

	private List<ScanResult> wifiList;
	private WifiManager wifiManager;
	private List<String> passableHotsPot;
	private WifiReceiver wifiReceiver;
	private boolean isConnected=false;//指定wifi是否连接上
	private boolean isFirstLogin=true;//未做网络检测
	private boolean isAuthed=false;//wifi验证通过
	private String logonsessid;
	
	private WifiBinder mWifiBinder = new WifiBinder();

	public interface IWifiService{
		public boolean isWifiTested();
		public boolean isWifiAuthed();
	}
	
    public class WifiBinder extends Binder implements IWifiService{

		@Override
		public boolean isWifiTested() {
			// TODO Auto-generated method stub
			return isFirstLogin;
		}

		@Override
		public boolean isWifiAuthed() {
			// TODO Auto-generated method stub
			return isAuthed;
		}

    	
    }
    
	@Override
	public void onCreate() {
		IntentFilter mFilter = new IntentFilter();  
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);  
		this.registerReceiver(wifiReceiver, mFilter);
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
		unregisterReceiver(wifiReceiver);
		return super.onUnbind(intent);
	}
	
	/* 监听热点变化 */
	private final class WifiReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();  
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) { 
			     startSpecificWifi();
            }
		}
	}

	public void startSpecificWifi(){
		new Thread(){
			@Override
			public void run() {
				redirect02();
				super.run();
			}
		}.start();
		
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
					isFirstLogin=false;
				}
				
				@Override
				public void error(ErrorInfo e) {
					isFirstLogin=false;
				}
				
				@Override
				public void complete(Bundle values) {
					isAuthed=true;
					isFirstLogin=false;
					
				}
			}, info.getBSSID(), int2ip(info.getIpAddress()));
		}else{
			isFirstLogin=false;
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
	
	
	/**
	 * Http URL重定向
	 */
	private static void redirect02() {
		DefaultHttpClient httpclient = null;
		String url = "http://hotels.ctrip.com/hotel/hong-kong58";
		try {
			httpclient = new DefaultHttpClient();
//			httpclient.setRedirectStrategy(new RedirectStrategy() {
//
//				@Override
//				public HttpUriRequest getRedirect(HttpRequest arg0,
//						HttpResponse arg1, HttpContext arg2)
//						throws ProtocolException {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				@Override
//				public boolean isRedirected(HttpRequest arg0,
//						HttpResponse arg1, HttpContext arg2)
//						throws ProtocolException {
//					// TODO Auto-generated method stub
//					return false;
//				}	//设置重定向处理方式
//
//
//			});

			// 创建httpget.
			HttpGet httpget = new HttpGet(url);
			// 执行get请求.
			HttpResponse response = httpclient.execute(httpget);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// 打印响应内容长度
					System.out.println("Response content length: "
							+ entity.getContentLength());
					// 打印响应内容
					System.out.println("Response content: "
							+ EntityUtils.toString(entity));
				}
			} else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY
					|| statusCode == HttpStatus.SC_MOVED_PERMANENTLY) {
				
				System.out.println("当前页面发生重定向了---");
				
				Header[] headers = response.getHeaders("Location");
				if(headers!=null && headers.length>0){
					String redirectUrl = headers[0].getValue();
					System.out.println("重定向的URL:"+redirectUrl);
					
					redirectUrl = redirectUrl.replace(" ", "%20");
					get(redirectUrl);
				}
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpclient.getConnectionManager().shutdown();
		}
	}

	/**
	 * 发送 get请求
	 */
	private static void get(String url) {

		HttpClient httpclient = new DefaultHttpClient();

		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet(url);
			System.out.println("executing request " + httpget.getURI());
			// 执行get请求.
			HttpResponse response = httpclient.execute(httpget);
			
			// 获取响应状态
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode==HttpStatus.SC_OK){
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// 打印响应内容长度
					System.out.println("Response content length: "
							+ entity.getContentLength());
					// 打印响应内容
					System.out.println("Response content: "
							+ EntityUtils.toString(entity));
				}
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpclient.getConnectionManager().shutdown();
		}
	}
	
}
