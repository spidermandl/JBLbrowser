package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;


import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.adapter.SettingGridItemAdapter;
import com.jbl.browser.adapter.SettingPagerAdapter;
import com.jbl.browser.interfaces.SettingItemInterface;
import com.jbl.browser.utils.JBLPreference;
import com.viewpager.indicator.LinePageIndicator;
import com.viewpager.indicator.PageIndicator;

/**
 * @author yyjoy-mac3
 * 设置界面滑动分页控制类
 */

public class SettingPagerFragment extends SherlockFragment{
	public static final String TAG = "SettingPagerFragment";
	
	private ViewPager mViewPager; // 水平实现滑动效果
	private PagerAdapter mPageAdapter;	
	PageIndicator mIndicator;
	private static final int PAGE_SIZE = 8; // 每页显示的数据个数
	private static final int TEST_LIST_SIZE = 43; // 数据总长度
	int sTotalPages = 1;
	private int mCurrentPage;
	private List<List<String>> mPageList;
	private List<GridView> mGridViews;
	private Context mContext;
	private String[] resArrays;
	private List<View> mViewPages;
	private boolean flag=true;//true :开启无图  false:关闭无图
	/*
	 * caidantubiao
	 */
	private int[] girdview_menu_image = {R.drawable.menu_add_bookmark_selector,R.drawable.menu_combine_selector,R.drawable.menu_setting_selector,
			R.drawable.menu_nightmode_selector,R.drawable.menu_share_selector,R.drawable.no_pic_mode_selector,R.drawable.menu_download_selector,
			R.drawable.menu_quit_selector,R.drawable.ic_launcher,R.drawable.menu_wuhen_selector,R.drawable.menu_fullscreen_selector,
			R.drawable.menu_refresh_selector,R.drawable.menu_feedback_selector,R.drawable.ic_launcher};
		
	//点击回调接口
	private SettingItemInterface settingInterface;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initData();
		
		View view=inflater.inflate(R.layout.main_setting_panel, container, false);
		
		mViewPager = (ViewPager) view.findViewById(R.id.setting_viewpager);
		mIndicator = (LinePageIndicator)view.findViewById(R.id.setting_indicator);
		mPageAdapter = new SettingPagerAdapter(getPageViews());
		mViewPager.setAdapter(mPageAdapter);
		mIndicator.setViewPager(mViewPager);
		mViewPager.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.menu_bar_appear));// 加载弹出菜单栏的动画效果
		
		view.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});//最外层接管点击事件
		
		return view;
	}
	
	@Override
	public void onStop() {
		mViewPager.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.menu_bar_disappear));// 退出菜单栏时的动画效果
		super.onStop();
	}
	
	/**
	 * 初始化数据源
	 */
	void initData(){
		mContext=this.getActivity();
		resArrays= mContext.getResources().getStringArray(R.array.setting_content_item);		
		mPageList = new ArrayList<List<String>>();
		mGridViews = new ArrayList<GridView>();
		mViewPages = new ArrayList<View>();
		initPages(resArrays);
		initViewAndAdapter();
	}
	/**
	 * 将数据分页
	 * 
	 * @param list
	 */
	public void initPages(String[] list) {
		if (list.length % PAGE_SIZE == 0) {
			sTotalPages = list.length / PAGE_SIZE;
		} else {
			sTotalPages =list.length/ PAGE_SIZE + 1;
		}
		mCurrentPage = 0;
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < list.length; ++i) {
			if(i==5){
				if(JBLPreference.getInstance(mContext).readInt(JBLPreference.PIC_CACHE_TYPE)==0){
					l.add(list[i].substring(4));
				}else{
					l.add(list[i].substring(0,4));
				}
			}else if(i==8){
				   if(JBLPreference.getInstance(mContext).readInt(JBLPreference.TURNING_TYPE)==0){
					   l.add(list[i].substring(4));
				   }else{
					   l.add(list[i].substring(0,4));
				   }
			}else if(i==9){
					if(JBLPreference.getInstance(mContext).readInt(JBLPreference.HISTORY_CACHE_TYPE)==0){
						l.add(list[i].substring(4));
					}else{
						l.add(list[i].substring(0,4));
				    }
				 }else if(i==10){
					 if(JBLPreference.getInstance(mContext).readInt(JBLPreference.FULL_SCREEN_TYPE)==0){
							l.add(list[i].substring(4));
						}else{
							l.add(list[i].substring(0,4));
					    }
				}else{
						l.add(list[i]);
					}
			if ((i + 1) % PAGE_SIZE == 0) {
				mPageList.add(l);
				l = new ArrayList<String>();
			}
		}
		if (l.size() > 0) {
			mPageList.add(l);
		}
		
	}

	
	public void setInterface(SettingItemInterface i){
		settingInterface=i;
	}
	
	private void initViewAndAdapter() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < sTotalPages; ++i) {
			GridView lv = (GridView)inflater.inflate(R.layout.main_setting_gridview, null);
			mGridViews.add(lv);
			SettingGridItemAdapter adapter = new SettingGridItemAdapter(mContext,mPageList.get(i));
			lv.setAdapter(adapter);
			mViewPages.add(lv);
			if (i == 0) {
				// 菜单监听事件
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						switch (position) {
						case 0: // 添加书签	
							if(settingInterface!=null)
								settingInterface.addBookMark();
							break;
						case 1: // 跳转到书签界面
							if(settingInterface!=null)
								settingInterface.listBookMark();
							break;

						case 2://跳转到设置界面
							if(settingInterface!=null)
								settingInterface.browserSetting();
							break;
						case 3: // 跳转到历史记录界面
							if(settingInterface!=null)
								settingInterface.listHistory();
							break;
						case 4://分享
							if(settingInterface!=null)
								settingInterface.share();
							break;
						case 5:  //设置无图模式
							if(settingInterface!=null)
								settingInterface.fitlerPicLoading();
							break;
						case 6: //下载管理
							if(settingInterface!=null)
								settingInterface.manageDownload();
							break;
						case 7://退出系统
							if(settingInterface!=null)
								settingInterface.quit();

							break;
						default:
							break;
						}
						((BaseFragActivity)(SettingPagerFragment.this.getActivity())).removeFragment(SettingPagerFragment.this);
					}
				});
			}
			if(i==1){
				lv.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> parent,
							View view, int position, long id) {
						switch (position) {
						case 0://页面翻转
							if(settingInterface!=null)
								settingInterface.pageTurningSwitch();
							break;
						case 1://网页无痕浏览模式
							if(settingInterface!=null)
								settingInterface.withoutTrace();
							break;
						case 2://网页全屏浏览模式
							if(settingInterface!=null)
								settingInterface.fullScreen();
							break;
						case 5://页面刷新
							if(settingInterface!=null)
								settingInterface.refresh();
							break;
						default:
							break;
						}
						((BaseFragActivity)(SettingPagerFragment.this.getActivity())).removeFragment(SettingPagerFragment.this);
					}
					
				});
			}
		}
	}
	public List<View> getPageViews() {
		return mViewPages;
	}
	
}
