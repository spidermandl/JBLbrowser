package com.jbl.browser.activity;

import com.actionbarsherlock.app.ActionBar;
import com.jbl.browser.R;

import android.os.Bundle;

/**
 * 子菜单activity
 * @author Desmond
 *
 */
public class SubMenuActivity extends BaseFragActivity {

	public static final String TAG="SubMenuActivity";
	@Override
	protected void onCreate(Bundle bundle) {
		setTheme(R.style.Theme_Sherlock_Light);
		setContentView(R.layout.activity_frame);
		super.onCreate(bundle);
		
		Class<?> fragmentClass= (Class<?>)(getIntent().getSerializableExtra(TAG));
		if(fragmentClass!=null){
			navigateTo(fragmentClass, null, true, TAG);
		}
	}
	
	
}
