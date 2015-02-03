package com.jbl.browser.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ActionMode;

import com.jbl.browser.R;
import com.jbl.browser.adapter.RecommendAdapter;
import com.jbl.browser.fragment.RecommendCollectFragment;
import com.jbl.browser.fragment.RecommendCustomFragment;
import com.jbl.browser.fragment.RecommendMainFragment;

@SuppressLint("NewApi")
public class RecommendMainActivity extends Activity{

	private static final String TAG="RecommendMainActivity";
	private static final String INSTANCESTATE_TAB = "tab";
	private static final int DEFAULT_OFFSCREEN_PAGES = 2;
	ViewPager mViewPager;
	RecommendAdapter mTabsAdapter;
	ActionMode mActionMode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend_main);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(DEFAULT_OFFSCREEN_PAGES);
		
		final ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE
				| ActionBar.DISPLAY_SHOW_HOME);

		
		mTabsAdapter = new RecommendAdapter(RecommendMainActivity.this, mViewPager);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.recommend), RecommendMainFragment.class,
				null);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.collect), RecommendCollectFragment.class,
				null);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.custom), RecommendCustomFragment.class,
				null);
		bar.setSelectedNavigationItem(PreferenceManager
				.getDefaultSharedPreferences(this).getInt(INSTANCESTATE_TAB, 0));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(this).edit();
		editor.putInt(INSTANCESTATE_TAB, getActionBar()
				.getSelectedNavigationIndex());
		editor.commit();
	}

	
	public void setActionMode(ActionMode actionMode) {
        mActionMode = actionMode;
    }

    public ActionMode getActionMode() {
        return mActionMode;
    }

    public Fragment getFragment(int tabIndex) {
        return mTabsAdapter.getItem(tabIndex);
    }
}
