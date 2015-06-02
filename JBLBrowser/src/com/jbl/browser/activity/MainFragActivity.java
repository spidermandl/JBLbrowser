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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.actionbarsherlock.view.Window;
import com.jbl.browser.JBLApplication;
import com.jbl.browser.R;
import com.jbl.browser.activity.WIFIService.IWifiService;
import com.jbl.browser.activity.WIFIService.WIFIStatus;
import com.jbl.browser.db.UserInfoDao;
import com.jbl.browser.fragment.AuthFragment;
import com.jbl.browser.fragment.MainPageFragment;
import com.jbl.browser.tools.BusinessTool;
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
	
	private Context context;
//	private WifiManager wifiManager;
//	private List<ScanResult> wifiList;
//	private List<String> passableHotsPot;
	
	private ProgressDialog pd;
	private AlertDialog.Builder wifiWarning;
	
	private IWifiService iWifiService;
	private Handler statusHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			WIFIStatus status=(WIFIStatus)msg.obj;
			if(status!=null){
				switch (status) {
				case UNREACH:
					enter();
					break;
				case CHECKED:
					if(wifiWarning==null){
						wifiWarning=new AlertDialog.Builder(MainFragActivity.this)
					     .setTitle(R.string.cmcc_edu_warning)
					     .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						
						     @Override
						     public void onClick(DialogInterface dialog, int which) {
							
							     iWifiService.startConnection();
							     pd = new ProgressDialog(MainFragActivity.this);
			                     pd.setMessage("正在连接wifi...");
			                     pd.setCancelable(false);
			                     pd.show();
						     }
					      })
					     .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						
						     @Override
						     public void onClick(DialogInterface dialog, int which) {
							     enter();
							
						     }
					      });
						wifiWarning.show();
					}
					break;
				case CONNECTED:
					if(pd!=null&&pd.isShowing()){
						pd.setMessage("正在验证...");
						break;
					}
					pd = new ProgressDialog(MainFragActivity.this);
					pd.setMessage("正在验证...");
                    pd.setCancelable(false);
                    pd.show();
					break;
				case FAILED:
					if(pd!=null&&pd.isShowing()){
						pd.dismiss();
					}
					enter();
					break;
				case AUTHORITHED:
					System.out.println("success--------");
					if(pd!=null&&pd.isShowing()){
						pd.dismiss();
					}
					JBLPreference.getInstance(context).writeInt(JBLPreference.EDU_KEY,JBLPreference.IS_EDU);
					enter();
					break;
				default:
					break;
				}
			}
		};
	};
	
	/**
	 * 检测wifi扫描service的进度状态
	 */
	Runnable wifiTestRun=new Runnable() {
		
		@Override
		public void run() {
			while (true) {
				if(iWifiService!=null){
				     WIFIStatus status=iWifiService.getWifiStatus();
				     Message msg=new Message();
				     msg.obj=status;
				     statusHandler.sendMessage(msg);
				     if(status==WIFIStatus.AUTHORITHED||status==WIFIStatus.FAILED){
				    	 return;
				     }
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
	
	private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        	iWifiService = (IWifiService) service;
            
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        	iWifiService = null ;
        }

    };
    

	@Override
	protected void onCreate(Bundle arg0) {
		//setTheme(R.style.Theme_Sherlock); // Used for theme switching in samples
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_frame);
		JBLApplication.getInstance().addActivity(this);//添加到activity队列中
		context=getApplicationContext();
		init();
		super.onCreate(arg0);
		
		if(!JBLApplication.getInstance().isEntering()){//非第一次进入程序
			enter();
			return;
		}
		
		new Thread(wifiTestRun).start();//开启检测wifi验证服务
		
	}
	
	
	/**
	 * 初始化
	 */
	void init(){
		//开启wifi检测服务
		this.bindService(new Intent(this,WIFIService.class), this.serviceConnection, BIND_AUTO_CREATE);
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
		if(!new UserInfoDao(this).hasApproved(BusinessTool.getDeviceID(this))){
			navigateTo(AuthFragment.class,null,false,TAG);
		}else
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
		this.unbindService(this.serviceConnection);
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
