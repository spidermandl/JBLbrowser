package com.jbl.browser.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.jbl.browser.R;

import android.os.Bundle;

/**
 * 浏览器设置界面
 * @author Desmond
 *
 */
public class BrowserSettingActivity extends BaseFragActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setTheme(R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
		/**
		 * 设置actionbar样式
		 */
		final ActionBar ab = this.getSupportActionBar();	
 		ab.setDisplayHomeAsUpEnabled(true);		 	
 		ab.setDisplayUseLogoEnabled(false);		 		
 		ab.setDisplayShowHomeEnabled(false);		 		
 		ab.setDisplayShowTitleEnabled(true);
 		ab.setTitle(R.string.browser_setting_title);
		
		setContentView(R.layout.activity_setting);
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