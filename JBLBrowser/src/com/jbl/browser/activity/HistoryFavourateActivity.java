package com.jbl.browser.activity;

import android.annotation.SuppressLint;
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
import com.jbl.browser.interfaces.deleteHistory;
import com.jbl.browser.utils.BrightnessSettings;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.JBLPreference.BoolType;

/**
 * 
 * @author Desmond
 * 浏览器收藏 历史功能
 *
 */
@SuppressLint("NewApi")
public class HistoryFavourateActivity extends BaseSwapeActivity {

	public static final String TAG="HistoryFavourateActivity";
	//全部删除与单条删除开关
	public static boolean mMenuFlag=true;
	//actionbar 定义，在历史界面使用。
	public static ActionBar ab;
	public deleteHistory clearHistory;            //清除记录接口
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

      //判断是夜间模式需再设置下activity亮度
      		if(JBLPreference.getInstance(HistoryFavourateActivity.this).readInt(BoolType.BRIGHTNESS_TYPE.toString())==JBLPreference.NIGHT_MODEL){
      			int brightness=JBLPreference.getInstance(HistoryFavourateActivity.this).readInt(JBLPreference.NIGHT_BRIGHTNESS_VALUS);
      			BrightnessSettings.setBrightness(HistoryFavourateActivity.this,brightness);
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
