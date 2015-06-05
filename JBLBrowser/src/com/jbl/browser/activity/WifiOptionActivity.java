package com.jbl.browser.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.jbl.browser.JBLApplication;
import com.jbl.browser.R;



/**
 * wifi 连接以及状态显示activity
 * @author osondesmond
 *
 */
public class WifiOptionActivity extends BaseFragActivity {

	public static ActionBar ab;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setTheme(R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
		JBLApplication.getInstance().addActivity(this);//添加到activity队列中
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
}
