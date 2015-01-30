package com.jbl.browser.activity;

import android.os.Bundle;

import com.jbl.browser.fragment.MainPageFragment;

public class MainFragActivity extends BaseFragActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		navigateTo(MainPageFragment.class, null, true,MainPageFragment.TAG);
	}
}
