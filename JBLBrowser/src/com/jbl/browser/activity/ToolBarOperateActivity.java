package com.jbl.browser.activity;

import android.content.Intent;
import android.os.Bundle;

import com.jbl.browser.R;

public class ToolBarOperateActivity extends BaseFragActivity{
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		setTheme(R.style.Theme_Sherlock); //Used for theme switching in samples
        //setContentView(R.layout.activity_toolbar_operate);
		super.onCreate(arg0);
		Intent intent=getIntent();
		String toolName=intent.getStringExtra("toolName");
		
		
	}
}
