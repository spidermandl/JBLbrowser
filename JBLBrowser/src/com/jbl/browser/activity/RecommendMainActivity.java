package com.jbl.browser.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jbl.browser.JBLApplication;
import com.jbl.browser.R;
import com.jbl.browser.fragment.BookMarkFragment;
import com.jbl.browser.fragment.HistoryFragment;
import com.jbl.browser.fragment.RecommendFragment;
import com.jbl.browser.interfaces.deleteHistory;

/**
 * 推荐页面主activity，默认显示recommendFragment
 * 
 * */
public class RecommendMainActivity extends BaseSwapeActivity {
	
	public static final String TAG="RecommendMainActivity";
	public deleteHistory clearHistory;            //清除记录接口
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
		//JBLApplication.getInstance().addActivity(this);//添加到activity队列中
		/**
		 * 设置actionbar样式
		 */
		final ActionBar ab = this.getSupportActionBar();	
 		ab.setDisplayHomeAsUpEnabled(true);		 	
 		ab.setDisplayUseLogoEnabled(false);		 		
 		ab.setDisplayShowHomeEnabled(false);		 		
 		ab.setDisplayShowTitleEnabled(true);
 		ab.setTitle(R.string.recommend_web_title);
 		
		setContentView(R.layout.activity_recommend_main);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager)findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        
        /**
         * 初始设置各个fragment
         */
        mTabsAdapter.addTab(mTabHost.newTabSpec(RecommendFragment.TAG).setIndicator(
        		getResources().getString(R.string.recommend_title)),
        		RecommendFragment.class, null);
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if(mTabHost.getCurrentTabTag().equals(HistoryFragment.TAG)){
			menu.add(0, 1, 0, "delete").setIcon(R.drawable.menu_delete)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			}
		return super.onCreateOptionsMenu(menu);
	}

	public void setInterface(deleteHistory i){
		this.clearHistory=i;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==1){
			//Toast.makeText(this,R.string.delete_bookmark_fail, 100).show();
			if(clearHistory!=null)
				clearHistory.clear();
		}
		return super.onOptionsItemSelected(item);
	}

}
