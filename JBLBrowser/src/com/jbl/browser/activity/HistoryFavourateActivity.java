package com.jbl.browser.activity;


import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jbl.browser.R;
import com.jbl.browser.adapter.HistoryAdapter;
import com.jbl.browser.bean.History;
import com.jbl.browser.db.HistoryDao;
import com.jbl.browser.fragment.BookMarkFragment;
import com.jbl.browser.fragment.HistoryFragment;
import com.jbl.browser.utils.BrightnessSettings;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.JBLPreference.BoolType;

/**
 * 
 * @author Desmond
 * 浏览器收藏 历史功能
 *
 */
public class HistoryFavourateActivity extends BaseSwapeActivity {

	public static final String TAG="HistoryFavourateActivity";
	//全部删除与单条删除开关
	public static boolean mMenuFlag=true;
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

      //判断是夜间模式需再设置下activity亮度
      		if(JBLPreference.getInstance(HistoryFavourateActivity.this).readInt(BoolType.BRIGHTNESS_TYPE.toString())==JBLPreference.NIGHT_MODEL){
      			int brightness=JBLPreference.getInstance(HistoryFavourateActivity.this).readInt(JBLPreference.NIGHT_BRIGHTNESS_VALUS);
      			BrightnessSettings.setBrightness(HistoryFavourateActivity.this,brightness);
      		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 0, "delete").setIcon(R.drawable.menu_delete)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==1){
			//Toast.makeText(this,R.string.delete_bookmark_fail, 100).show();
			//当mMenuFlag为true（默认）时，删除全部历史记录
			if(mMenuFlag){
				AlertDialog.Builder builder=new Builder(this);
				//2所有builder设置一些参数
				builder.setTitle("清空记录");
				builder.setMessage("是否清空");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Boolean flag=new HistoryDao(getBaseContext()).clearHistory();	//清空记录	
						if(flag){
							RefreshListview();//更新历史界面
							Toast.makeText(HistoryFavourateActivity.this, "删除成功", 1000).show();
						}else{
							Toast.makeText(HistoryFavourateActivity.this, "删除失败", 1000).show();
						}
						
					}
				});
				builder.setNeutralButton("取消",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				
				builder.create().show();
			}
			//当mMenuFlag为false时，删除单条记录
			if(!mMenuFlag){
				AlertDialog.Builder builder=new Builder(this);
				//2所有builder设置一些参数
				builder.setTitle("删除记录");
				builder.setMessage("是否删除此记录");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						int mFlag=new HistoryDao(getBaseContext()).deleteHistoryById(HistoryFragment.deleteId);//单条删除
						if(mFlag!=0){
							RefreshListview();
							Toast.makeText(HistoryFavourateActivity.this, "删除成功", 1000).show();
						}else{
							Toast.makeText(HistoryFavourateActivity.this, "删除失败", 1000).show();
						}
					}
				});
				builder.setNeutralButton("取消",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				builder.create().show();
				mMenuFlag=true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	//更新历史界面
	public void RefreshListview(){
		HistoryFragment.list=new HistoryDao(HistoryFavourateActivity.this).queryAll();
		if(HistoryFragment.list.size()==0){
			HistoryFragment.listview.setVisibility(View.GONE);
			HistoryFragment.noHistory.setVisibility(View.VISIBLE);	
		}
		HistoryFragment.historyAdapter=new HistoryAdapter(HistoryFavourateActivity.this, HistoryFragment.list);
		HistoryFragment.historyAdapter.notifyDataSetChanged();
		HistoryFragment.listview.setAdapter(HistoryFragment.historyAdapter);
		
	}
}
