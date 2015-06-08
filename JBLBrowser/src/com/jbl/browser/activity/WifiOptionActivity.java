package com.jbl.browser.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.jbl.browser.JBLApplication;
import com.jbl.browser.R;
import com.jbl.browser.activity.WIFIService.IWifiService;
import com.jbl.browser.wifi.IState;



/**
 * wifi 连接以及状态显示activity
 * @author osondesmond
 *
 */
public class WifiOptionActivity extends BaseFragActivity implements IWifiService {

	public static ActionBar ab;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setTheme(R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
		/**
		 * 设置actionbar样式
		 */
		 ab = this.getSupportActionBar();	
 		 ab.setDisplayHomeAsUpEnabled(true);		 	
 		 ab.setDisplayUseLogoEnabled(false);		 		
 		 ab.setDisplayShowHomeEnabled(false);		 		
 		 ab.setDisplayShowTitleEnabled(true);
 		 ab.setTitle(R.string.wifi_option_title);
 		 
 		 init();
 		 
		 setContentView(R.layout.activity_wifi);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home://返回
			this.finish();
			break;
			
		default:
			break;
		}
		return false;
	}
	
	
	void init(){

		this.bindService(new Intent(this,WIFIService.class), this.serviceConnection, BIND_AUTO_CREATE);

	}
	

	@Override
	protected void onStop() {
		this.unbindService(this.serviceConnection);
		super.onStop();
	}
	
	private ProgressDialog pd;
	private AlertDialog.Builder wifiWarning;
	private IWifiService iWifiService;
//	private Handler statusHandler = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			WIFIStatus status=(WIFIStatus)msg.obj;
//			if(status!=null){
//				switch (status) {
//				case UNREACH:
//					
//					break;
//				case CHECKED:
//					if(wifiWarning==null){
//						wifiWarning=new AlertDialog.Builder(WifiOptionActivity.this)
//					     .setTitle(R.string.cmcc_edu_warning)
//					     .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//						
//						     @Override
//						     public void onClick(DialogInterface dialog, int which) {
//							
//							     iWifiService.startConnection();
//							     pd = new ProgressDialog(WifiOptionActivity.this);
//			                     pd.setMessage("正在连接wifi...");
//			                     pd.setCancelable(false);
//			                     pd.show();
//						     }
//					      })
//					     .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//						
//						     @Override
//						     public void onClick(DialogInterface dialog, int which) {
//							     
//							
//						     }
//					      });
//						wifiWarning.show();
//					}
//					break;
//				case CONNECTED:
//					if(pd!=null&&pd.isShowing()){
//						pd.setMessage("正在验证...");
//						break;
//					}
//					pd = new ProgressDialog(WifiOptionActivity.this);
//					pd.setMessage("正在验证...");
//                    pd.setCancelable(false);
//                    pd.show();
//					break;
//				case FAILED:
//					if(pd!=null&&pd.isShowing()){
//						pd.dismiss();
//					}
//					
//					break;
//				case AUTHORITHED:
//					System.out.println("success--------");
//					if(pd!=null&&pd.isShowing()){
//						pd.dismiss();
//					}
//					
//					break;
//				default:
//					break;
//				}
//			}
//		};
//	};
//	
//	/**
//	 * 检测wifi扫描service的进度状态
//	 */
//	Runnable wifiTestRun=new Runnable() {
//		
//		@Override
//		public void run() {
//			while (true) {
//				if(iWifiService!=null){
//				     WIFIStatus status=iWifiService.getWifiStatus();
//				     Message msg=new Message();
//				     msg.obj=status;
//				     statusHandler.sendMessage(msg);
//				     if(status==WIFIStatus.AUTHORITHED||status==WIFIStatus.FAILED){
//				    	 return;
//				     }
//				}
//				try {
//					Thread.sleep(1000L);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
//		}
//	};
	
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
	public IState getWifiStatus() {
		// TODO Auto-generated method stub
		return iWifiService.getWifiStatus();
	}


	@Override
	public void startConnection() {
		iWifiService.startConnection();
	}


	@Override
	public void changeState(Class state) {
		iWifiService.changeState(state);
		
	}


	@Override
	public void stopConnection() {
		iWifiService.stopConnection();
		
	}


	@Override
	public long getOnlineTime() {
		// TODO Auto-generated method stub
		return iWifiService.getOnlineTime();
	}
    
}
