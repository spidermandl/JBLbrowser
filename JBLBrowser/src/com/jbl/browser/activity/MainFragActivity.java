package com.jbl.browser.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.actionbarsherlock.view.Window;
import com.jbl.browser.JBLApplication;
import com.jbl.browser.R;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.fragment.MainPageFragment;
import com.jbl.browser.utils.JBLPreference;
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

	@Override
	protected void onCreate(Bundle arg0) {
		//setTheme(R.style.Theme_Sherlock); // Used for theme switching in samples
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_frame);
		init();
		super.onCreate(arg0);
		navigateTo(MainPageFragment.class, null, true, TAG);
	}
	
	
	/**
	 * 初始化
	 */
	void init(){
		startDownloadService();
		mDownloadManager = new DownloadManager(getContentResolver(),getPackageName());
		mDownloadReceiver = new BroadcastReceiver() {

		    @Override
		    public void onReceive(Context context, Intent intent) {
		    	showDownloadList();
		    }
		};
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
