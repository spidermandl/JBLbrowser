package com.jbl.browser.activity;

import android.os.Bundle;

import com.jbl.browser.R;
import com.jbl.browser.fragment.MainPageFragment;

public class MainFragActivity extends BaseFragActivity {

	@Override
	protected void onCreate(Bundle arg0) {
        setTheme(R.style.Theme_Sherlock); //Used for theme switching in samples
        setContentView(R.layout.activity_frame);
		super.onCreate(arg0);
		navigateTo(MainPageFragment.class, null, true,MainPageFragment.TAG);
	}
}
