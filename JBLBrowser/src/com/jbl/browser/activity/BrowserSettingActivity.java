package com.jbl.browser.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.jbl.browser.JBLApplication;
import com.jbl.browser.R;
import com.jbl.browser.utils.BrightnessSettings;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.JBLPreference.BoolType;

/**
 * 浏览器设置界面
 * @author Desmond
 *
 */
public class BrowserSettingActivity extends BaseFragActivity {
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
 		 ab.setTitle(R.string.browser_setting_title);
		 setContentView(R.layout.activity_setting);
		 //判断是夜间模式需再设置下activity亮度
  		 if(JBLPreference.getInstance(BrowserSettingActivity.this).readInt(BoolType.BRIGHTNESS_TYPE.toString())==JBLPreference.NIGHT_MODEL){
  			 int brightness=JBLPreference.getInstance(BrowserSettingActivity.this).readInt(JBLPreference.NIGHT_BRIGHTNESS_VALUS);
  			 BrightnessSettings.setBrightness(BrowserSettingActivity.this,brightness);
  		 }
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
