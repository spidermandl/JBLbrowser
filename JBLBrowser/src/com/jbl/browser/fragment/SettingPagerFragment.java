package com.jbl.browser.fragment;

import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.adapter.SettingGridItemAdapter;
import com.jbl.browser.adapter.SettingPagerAdapter;
import com.jbl.browser.interfaces.SettingItemInterface;
import com.jbl.browser.utils.ImageInfo;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.JBLPreference.BoolType;
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
	private ArrayList<GridView> viewLists;
	public static final String TAG = "SettingPagerFragment";
	private PageIndicator mIndicator;
	private int PageCount;
	private View blank;
	private Context mContext;
	//点击回调接口
	private SettingItemInterface settingInterface;
	/*
	 * 菜单图标
	 */
	private int[] girdview_menu_image = {R.drawable.menu_add_bookmark_selector,R.drawable.menu_combine_selector,R.drawable.menu_setting_selector,
			R.drawable.menu_combine_selector,R.drawable.menu_share_selector,R.drawable.no_pic_mode_selector,R.drawable.menu_download_selector,
			R.drawable.menu_quit_selector,R.drawable.menu_roll_webview_selector,R.drawable.menu_wuhen_selector,R.drawable.menu_fullscreen_selector,
			R.drawable.menu_refresh_selector,R.drawable.menu_feedback_selector,R.drawable.menu_nightmode_selector};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater = getLayoutInflater(savedInstanceState);
		View view=inflater.inflate(R.layout.main_setting_panel, container, false);
		initPages();
		initViewAndAdapter();
		viewPager = (ViewPager)view. findViewById(R.id.setting_viewpager);
		mIndicator = (LinePageIndicator)view.findViewById(R.id.setting_indicator);
		adapter = new SettingPagerAdapter(getActivity(), viewLists);
		viewPager.setAdapter(adapter);
		mIndicator.setViewPager(viewPager);
		viewPager.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.menu_bar_appear));// 加载弹出菜单栏的动画效果
		blank=(View)view.findViewById(R.id.fill_pad);
		blank.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					//点击空白处 销毁fragment
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.remove(SettingPagerFragment.this);
					transaction.commitAllowingStateLoss();
					getFragmentManager().executePendingTransactions();
				} catch (Exception e) {
					
				}
				return true;
			}
		});
		view.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {

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
		for (int i = 0; i < resArrays.length; i++) {
			if (i == 5) {
				if (JBLPreference.getInstance(mContext).readInt(
						BoolType.PIC_CACHE.toString()) == JBLPreference.NO_PICTURE) {
					list.add(new ImageInfo(girdview_menu_image[i], resArrays[i]
							.substring(4)));
				} else {
					list.add(new ImageInfo(girdview_menu_image[i], resArrays[i]
							.substring(0, 4)));
				}
			} else if (i == 8) {
				if (JBLPreference.getInstance(mContext).readInt(
						BoolType.TURNNING.toString()) == JBLPreference.OPEN_TURNING_BUTTON) {
					list.add(new ImageInfo(girdview_menu_image[i], resArrays[i]
							.substring(4)));
				} else {
					list.add(new ImageInfo(girdview_menu_image[i], resArrays[i]
							.substring(0, 4)));
				}
			} else if (i == 9) {
				if (JBLPreference.getInstance(mContext).readInt(
						BoolType.HISTORY_CACHE.toString()) == JBLPreference.OPEN_HISTORY) {
					list.add(new ImageInfo(girdview_menu_image[i], resArrays[i]
							.substring(4)));
				} else {
					list.add(new ImageInfo(girdview_menu_image[i], resArrays[i]
							.substring(0, 4)));
				}
			} else if (i == 10) {
				if (JBLPreference.getInstance(mContext).readInt(
						BoolType.FULL_SCREEN.toString()) == JBLPreference.YES_FULL) {
					list.add(new ImageInfo(girdview_menu_image[i], resArrays[i]
							.substring(4)));
				} else {
					list.add(new ImageInfo(girdview_menu_image[i], resArrays[i]
							.substring(0, 4)));
				}
			} else if(i==13){
				  if(JBLPreference.getInstance(mContext).readInt(BoolType.BRIGHTNESS_TYPE.toString())==JBLPreference.NIGHT_MODEL){
					  list.add(new ImageInfo(girdview_menu_image[i], resArrays[i]
								.substring(4)));
				  }else {
						list.add(new ImageInfo(girdview_menu_image[i], resArrays[i]
								.substring(0, 4)));
					}
			}else{
				list.add(new ImageInfo(girdview_menu_image[i], resArrays[i]));
			}
		}
	}
	public void setInterface(SettingItemInterface i){
		settingInterface=i;
	}
	private void initViewAndAdapter() {
		 PageCount = (int) Math.ceil(list.size() / APP_PAGE_SIZE);
			viewLists = new ArrayList<GridView>();
			for (int i = 0; i < PageCount; i++) {
				GridView appPage = new GridView(getActivity());	
				final SettingGridItemAdapter adapter =new SettingGridItemAdapter(getActivity(), list, i);
				appPage.setAdapter(adapter);
				appPage.setNumColumns(4);
				viewLists.add(i, appPage);
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
							case 3://页面刷新
								if(settingInterface!=null)
									settingInterface.refresh();
								break;
							case 5://夜间模式
								if(settingInterface!=null)
									settingInterface.nightBright();
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
