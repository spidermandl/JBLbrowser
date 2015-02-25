package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.adapter.SettingGridItemAdapter;
import com.jbl.browser.adapter.SettingPagerAdapter;
import com.jbl.browser.interfaces.SettingItemInterface;
import com.jbl.browser.utils.ImageInfo;
import com.viewpager.indicator.LinePageIndicator;
import com.viewpager.indicator.PageIndicator;

/**
 * @author yyjoy-mac3
 * 设置界面滑动分页控制类
 */

public class SettingPagerFragment extends SherlockFragment{
	private static final float APP_PAGE_SIZE = 8.0f;
	private SettingPagerAdapter adapter;
	private ViewPager viewPager;
	private ArrayList<ImageInfo> list;
	private String[] resArrays;
	private Map<Integer, GridView> map;
	public static final String TAG = "SettingPagerFragment";
	private PageIndicator mIndicator;
	private int PageCount;
	private View mView;
	private RelativeLayout mRelativeLayout;
	//点击回调接口
	private SettingItemInterface settingInterface;
	/*
	 * caidantubiao
	 */
	private int[] girdview_menu_image = {R.drawable.menu_add_book_mark,R.drawable.menu_bookmark,R.drawable.menu_settings,
			R.drawable.menu_bookmark,R.drawable.menu_share,R.drawable.menu_nopic,R.drawable.menu_download,
			R.drawable.menu_exit,R.drawable.menu_scrollbutton,R.drawable.menu_nofoot,R.drawable.menu_fullscreen,
			R.drawable.menu_refresh,R.drawable.menu_feedback,R.drawable.menu_nightmode};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater = getLayoutInflater(savedInstanceState);
		View view=inflater.inflate(R.layout.main_setting_panel, container, false);
		initPages();
		initViewAndAdapter();
		viewPager = (ViewPager)view. findViewById(R.id.setting_viewpager);
		mIndicator = (LinePageIndicator)view.findViewById(R.id.setting_indicator);
		mRelativeLayout=(RelativeLayout)view.findViewById(R.id.panel_rl);
		adapter = new SettingPagerAdapter(getActivity(), map);
		viewPager.setAdapter(adapter);
		mIndicator.setViewPager(viewPager);
		viewPager.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.menu_bar_appear));// 加载弹出菜单栏的动画效果
		mView=(View)view.findViewById(R.id.fill_pad);
		mView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mView.setVisibility(View.GONE);
				mRelativeLayout.setVisibility(View.GONE);
				return true;
			}
		});
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
		viewPager.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.menu_bar_disappear));// 退出菜单栏时的动画效果
		super.onStop();
	}
	
	
	/**
	 * 将数据分页
	 * 
	 * @param list
	 */
	public void initPages() {
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		 list = new ArrayList<ImageInfo>();
	        resArrays=getResources().getStringArray(R.array.setting_content_item);	
	        for(int i=0;i<resArrays.length;i++){
	            list.add(new ImageInfo(girdview_menu_image[i] , resArrays[i]));
	           /* List<String> l = new ArrayList<String>();
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
						}*/
	        }
	}
	public void setInterface(SettingItemInterface i){
		settingInterface=i;
	}
	private void initViewAndAdapter() {
		 PageCount = (int) Math.ceil(list.size() / APP_PAGE_SIZE);
			map = new HashMap<Integer, GridView>();
			for (int i = 0; i < PageCount; i++) {
				GridView appPage = new GridView(getActivity());
				final SettingGridItemAdapter adapter =new SettingGridItemAdapter(getActivity(), list, i);
				appPage.setAdapter(adapter);
				appPage.setNumColumns(4);
				map.put(i, appPage);
				if (i == 0) {
					// 菜单监听事件
					appPage.setOnItemClickListener(new OnItemClickListener() {
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
					appPage.setOnItemClickListener(new OnItemClickListener(){

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
	
}
