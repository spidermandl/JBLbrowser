package com.jbl.browser.activity;

<<<<<<< HEAD
import android.app.AlertDialog;
import android.app.ProgressDialog;
=======
>>>>>>> 1b00643f31d70f3baddf7b245f7cc2fd5c9d3f17
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
<<<<<<< HEAD
import android.content.ServiceConnection;
import android.content.SharedPreferences;
=======
>>>>>>> 1b00643f31d70f3baddf7b245f7cc2fd5c9d3f17
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.actionbarsherlock.view.Window;
import com.jbl.browser.JBLApplication;
import com.jbl.browser.R;
<<<<<<< HEAD
import com.jbl.browser.activity.WIFIService.IWifiService;
import com.jbl.browser.activity.WIFIService.WIFIStatus;
import com.jbl.browser.fragment.MainPageFragment;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.UrlUtils;
=======
import com.jbl.browser.fragment.MainPageFragment;
>>>>>>> 1b00643f31d70f3baddf7b245f7cc2fd5c9d3f17
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
	
//	private WifiManager wifiManager;
//	private List<ScanResult> wifiList;
//	private List<String> passableHotsPot;
	
	@Override
	protected void onCreate(Bundle arg0) {
		//setTheme(R.style.Theme_Sherlock); // Used for theme switching in samples
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_frame);
		JBLApplication.getInstance().addActivity(this);//添加到activity队列中
		init();
<<<<<<< HEAD
		super.onCreate(arg0);
		SharedPreferences sp = getSharedPreferences(
				UrlUtils.SP_SaveUserInfo, Context.MODE_APPEND);
		sp.edit().putBoolean(UrlUtils.SP_SaveUserInfo_Second, true)
				.commit();
		if(!JBLApplication.getInstance().isEntering()){//非第一次进入程序
			enter();
			return;
		}
=======
>>>>>>> 1b00643f31d70f3baddf7b245f7cc2fd5c9d3f17
		
		enter();
//		if(!JBLApplication.getInstance().isEntering()){//非第一次进入程序
//		enter();
//		return;
//	}
		
		super.onCreate(arg0);
	}
	
	
	/**
	 * 初始化
	 */
	void init(){
		//开启wifi检测服务
		this.startService(new Intent(this,WIFIService.class));
		startDownloadService();
		mDownloadManager = new DownloadManager(getContentResolver(),getPackageName());
		mDownloadReceiver = new BroadcastReceiver() {

		    @Override
		    public void onReceive(Context context, Intent intent) {
		    	showDownloadList();
		    }
		};
		
		//检测wifi连接名
//		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//		wifiList = wifiManager.getScanResults();
//		onReceiveNewNetworks(wifiList);
		
	}
	
	void enter(){
		/**
		 * 判断是否注册过
		 */
//		if(!new UserInfoDao(this).hasApproved(BusinessTool.getDeviceID(this))){
//			navigateTo(AuthFragment.class,null,false,TAG);
//		}else
		navigateTo(MainPageFragment.class, null, true, TAG);
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
