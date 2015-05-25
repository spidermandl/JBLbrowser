package com.jbl.browser.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;

import com.actionbarsherlock.view.Window;
import com.jbl.browser.JBLApplication;
import com.jbl.browser.R;
import com.jbl.browser.db.UserInfoDao;
import com.jbl.browser.fragment.AuthFragment;
import com.jbl.browser.fragment.MainPageFragment;
import com.jbl.browser.model.ErrorInfo;
import com.jbl.browser.tools.BusinessCallback;
import com.jbl.browser.tools.BusinessTool;
import com.jbl.browser.utils.UrlUtils;
import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.DownloadManager.Request;
import com.mozillaonline.providers.downloads.DownloadService;

/**
 * 首页activity，fragment集合
 * @author Desmond
 *
 */
public class MainFragActivity extends BaseFragActivity {
	public final static String TAG="MainFragActivity";

	//下载管理类
	private DownloadManager mDownloadManager;
	//下载模块接收receiver　
	private BroadcastReceiver mDownloadReceiver;
	
	
	private WifiManager wifiManager;
	private List<ScanResult> wifiList;
	private List<String> passableHotsPot;
	
	private ProgressDialog pd;
	
	
	BusinessCallback callback=new BusinessCallback() {
		
		@Override
		public void fail(ErrorInfo e) {
			pd.dismiss();
			enter();
		}
		
		@Override
		public void error(ErrorInfo e) {
			pd.dismiss();
			enter();
		}
		
		@Override
		public void complete(Bundle values) {
			pd.dismiss();
			enter();
		}
	};
	
	private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
           // countService = (ICountService) service;
            
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //countService = null ;
        }

    };
    

	@Override
	protected void onCreate(Bundle arg0) {
		//setTheme(R.style.Theme_Sherlock); // Used for theme switching in samples
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_frame);
		JBLApplication.getInstance().addActivity(this);//添加到activity队列中
		init();
		super.onCreate(arg0);
		
		if(passableHotsPot!=null&&passableHotsPot.size()>0){
			new AlertDialog.Builder(MainFragActivity.this)
			.setTitle(R.string.cmcc_edu_warning)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					WifiConfiguration wifiConfig = setWifiParams(passableHotsPot.get(0));
					int wcgID = wifiManager.addNetwork(wifiConfig);
					boolean flag = wifiManager.enableNetwork(wcgID, true);

			        WifiInfo info = wifiManager.getConnectionInfo();
			        String wifiId = info != null ? info.getSSID() : null;
			        //if(wifiId!=null&&!wifiId.contains(UrlUtils.HOTPOT_NAME)){//判断wifi是否已经连接

					if(flag||(wifiId!=null&&!wifiId.contains(UrlUtils.HOTPOT_NAME))){
						BusinessTool.getInstance().getLogin(callback);
						pd = new ProgressDialog(MainFragActivity.this);
	                    pd.setMessage("正在连接...");
	                    pd.setCancelable(false);
	                    pd.show();
					}else{
						enter();
					}
					
				}
			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					enter();
					
				}
			})
			.show();
		}
		else{
			enter();
		}

	}
	
	
	/**
	 * 初始化
	 */
	void init(){
		//开启wifi检测服务
		//this.bindService(new Intent(this,WIFIService.class), this.serviceConnection, BIND_AUTO_CREATE);
		startDownloadService();
		mDownloadManager = new DownloadManager(getContentResolver(),getPackageName());
		mDownloadReceiver = new BroadcastReceiver() {

		    @Override
		    public void onReceive(Context context, Intent intent) {
		    	showDownloadList();
		    }
		};
		
		//检测wifi连接名
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiList = wifiManager.getScanResults();
		onReceiveNewNetworks(wifiList);
		
	}
	
	void enter(){
		/**
		 * 判断是否注册过
		 */
		if(!new UserInfoDao(this).hasApproved(BusinessTool.getDeviceID(this))){
			navigateTo(AuthFragment.class,null,false,TAG);
		}else
		    navigateTo(MainPageFragment.class, null, true, TAG);
	}
	
	/* 当搜索到新的wifi热点时判断该热点是否符合规格 */
	private void onReceiveNewNetworks(List<ScanResult> wifiList) {
		if(wifiList == null || wifiList.size() == 0)
			return;
		passableHotsPot = new ArrayList<String>();
		for (ScanResult result : wifiList) {
			System.out.println(result.SSID);
			if ((result.SSID).contains(UrlUtils.HOTPOT_NAME)){
		        WifiInfo info = wifiManager.getConnectionInfo();
		        String wifiId = info != null ? info.getSSID() : null;
		        //if(wifiId!=null&&!wifiId.contains(UrlUtils.HOTPOT_NAME)){//判断wifi是否已经连接
				    passableHotsPot.add(result.SSID);
		        //}
			}
		}
	}
	/* 设置要连接的热点的参数 */
	private WifiConfiguration setWifiParams(String ssid) {
		WifiConfiguration apConfig = new WifiConfiguration();
		apConfig.SSID = "\"" + ssid + "\"";
        apConfig.wepKeys[0] = "";  
        apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
        apConfig.wepTxKeyIndex = 0;  
		return apConfig;
	}
	
	@Override
	protected void onStart() {
		//注册广播
		registerReceiver(mDownloadReceiver, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		unregisterReceiver(mDownloadReceiver);
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//this.unbindService(this.serviceConnection);
		JBLApplication.getInstance().quit();
	}
	/**
	 * 开启下载服务
	 */
	public void startDownloadService() {
		Intent intent = new Intent();
		intent.setClass(this, DownloadService.class);
		startService(intent);
	}
	
	// 跳转到下载管理界面
	public void showDownloadList() {
		Intent intent = new Intent();
		//intent.setClass(this, DownloadList.class);
		intent.setClass(this, DownloadManageActivity.class);
		startActivity(intent);
	}
	
	// 开始下载
	public void startDownload(String url) {
		Uri srcUri = Uri.parse(url);
		DownloadManager.Request request = new Request(srcUri);
		request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS, "/");
		request.setDescription("Just for test");
		mDownloadManager.enqueue(request);
	}
	

}
