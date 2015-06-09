package com.jbl.browser.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
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
	

	@Override
	protected void onResume() {
		this.bindService(new Intent(this,WIFIService.class), this.serviceConnection, BIND_AUTO_CREATE);
		super.onResume();
	}
	
	@Override
	protected void onPause() {

		if(iWifiService!=null){
		     this.unbindService(this.serviceConnection);
		}
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	private IWifiService iWifiService;
	
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
		return iWifiService!=null?iWifiService.getWifiStatus():null;
	}


	@Override
	public void startConnection() {
		if(iWifiService!=null)
		iWifiService.startConnection();
	}


	@Override
	public void changeState(Class state) {
		if(iWifiService!=null)
		iWifiService.changeState(state);
		
	}


	@Override
	public void stopConnection() {
		if(iWifiService!=null)
			iWifiService.stopConnection();
		
	}


	@Override
	public long getOnlineTime() {
		// TODO Auto-generated method stub
		
		return iWifiService!=null?iWifiService.getOnlineTime():0;
	}
    
}
