package com.jbl.browser.activity;


import com.actionbarsherlock.app.ActionBar;
import com.jbl.browser.R;
import com.jbl.browser.fragment.BookMarkFragment;
import com.jbl.browser.fragment.HistoryFragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;

/**
 * 
 * @author Desmond
 * 浏览器收藏 历史功能
 *
 */
public class HistoryFavourateActivity extends BaseSwapeActivity {

	public static final String TAG="HistoryFavourateActivity";
    
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
 		ab.setTitle(R.string.history_favourate_title);
		
		setContentView(R.layout.activity_favourate_history);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager)findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        
        /**
         * 初始设置各个fragment
         */
        mTabsAdapter.addTab(mTabHost.newTabSpec(BookMarkFragment.TAG).setIndicator(
        		getResources().getString(R.string.favourate_title)),
                BookMarkFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec(HistoryFragment.TAG).setIndicator(
        		getResources().getString(R.string.history_title)),
                HistoryFragment.class, null);
        //选择初使进入tab
        String tag=getIntent().getStringExtra("TAG");
        if (tag != null) {
            mTabHost.setCurrentTabByTag(tag);
        }
	}
	
	
}
