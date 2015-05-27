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
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
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
	//private WifiReceiver wifiReceiver;
	
	private WIFIStatus wStatus;
	private String logonsessid;
	
	private WifiBinder mWifiBinder = new WifiBinder();
	
	BusinessCallback callback=new BusinessCallback() {
		
		@Override
		public void fail(ErrorInfo e) {
		}
		
		@Override
		public void error(ErrorInfo e) {
		}
		
		@Override
		public void complete(Bundle values) {
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
				connectToHotpot();
			}
			
		}

    }
    
	/**
	 * 网络状态设置类型
	 */
	public static enum WIFIStatus{
		UNREACH(0), //没有cmcc
		CHECKED(1), //有cmcc但没有连上
		CONNECTED(2), //cmcc连接上
		FAILED(3),//CMCC验证失败
	    AUTHORITHED(4);//CMCC验证通过
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
	
	Handler mHanlder =new Handler(){
		public void handleMessage(android.os.Message msg) {
			BusinessTool.getInstance().getLogin(callback);
		};
	};
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
	
//	/* 监听热点变化 */
//	private final class WifiReceiver extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();  
//            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)||
//            		action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)||
//            		action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) { 
//            	WifiInfo info = wifiManager.getConnectionInfo();
//		        String wifiId = info != null ? info.getSSID() : null;
//		        if(wifiId!=null&&wifiId.contains(UrlUtils.HOTPOT_NAME)){
//		        	BusinessTool.getInstance().getLogin(callback);
//		        }
//            }
//		}
//	}

	public void startSpecificWifi(){
		
		wifiList = wifiManager.getScanResults();
		if (wifiList == null || wifiList.size() == 0){
			wStatus = WIFIStatus.UNREACH;
			return;
		}
		onReceiveNewNetworks(wifiList);
	}
	
	/* 当搜索到新的wifi热点时判断该热点是否符合规格 */
	public void onReceiveNewNetworks(List<ScanResult> wifiList) {
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
		        	BusinessTool.getInstance().getLogin(callback);//直接验证
		        }
			}
		}

	}

	/* 连接到热点 */
	public void connectToHotpot() {
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
//		if(flag){
//			WifiInfo info=wifiManager.getConnectionInfo();
//			BusinessTool.getInstance().getLogin(new BusinessCallback() {
//				
//				@Override
//				public void fail(ErrorInfo e) {
//					isFirstLogin=false;
//				}
//				
//				@Override
//				public void error(ErrorInfo e) {
//					isFirstLogin=false;
//				}
//				
//				@Override
//				public void complete(Bundle values) {
//					isAuthed=true;
//					isFirstLogin=false;
//					
//				}
//			}, info.getBSSID(), int2ip(info.getIpAddress()));
//		}else{
//			isFirstLogin=false;
//		}
		System.out.println("connect success? " + flag);
	}

	/* 设置要连接的热点的参数 */
	public WifiConfiguration setWifiParams(String ssid) {
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
