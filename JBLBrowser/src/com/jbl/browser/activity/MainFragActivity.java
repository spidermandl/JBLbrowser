package com.jbl.browser.activity;

import android.content.Intent;
import android.os.Bundle;

import com.jbl.browser.R;
import com.jbl.browser.fragment.MainPageFragment;
import com.mozillaonline.providers.downloads.DownloadService;

public class MainFragActivity extends BaseFragActivity {

	@Override
	protected void onCreate(Bundle arg0) {
        setTheme(R.style.Theme_Sherlock); //Used for theme switching in samples
        setContentView(R.layout.activity_frame);
        startDownloadService();
		super.onCreate(arg0);
		navigateTo(MainPageFragment.class, null, true,MainPageFragment.TAG);
	}
	private void startDownloadService() {
		Intent intent = new Intent();
		intent.setClass(this, DownloadService.class);
		startService(intent);
	    }
}
