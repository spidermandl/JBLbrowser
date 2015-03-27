package com.jbl.browser.fragment;

import java.util.ArrayList;
import android.content.Context;
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
import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.JBLApplication;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.adapter.SettingGridItemAdapter;
import com.jbl.browser.adapter.SettingPagerAdapter;
import com.jbl.browser.interfaces.SettingItemInterface;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.JBLPreference.BoolType;
import com.viewpager.indicator.LinePageIndicator;
import com.viewpager.indicator.PageIndicator;

/**
 * @author yyjoy-mac3 设置界面滑动分页控制类
 */

public class SettingPagerFragment extends SherlockFragment {
	private static final float APP_PAGE_SIZE = 8.0f;
	private SettingPagerAdapter adapter;
	private ViewPager viewPager;
	private ArrayList<SettingMenuType> list;
	private ArrayList<GridView> viewLists;
	public static final String TAG = "SettingPagerFragment";
	private PageIndicator mIndicator;
	private int PageCount;
	private View blank;
	private Context mContext;
	// 点击回调接口
	private SettingItemInterface settingInterface;
	
	/**
	 * 几种按钮类型设置类型
	 */
	public static enum SettingMenuType{
		ADD_BOOKMARK_ABLE(1), //添加书签 启用
		ADD_BOOKMARK_DISABLE(-1), //添加书签 不启用
		FAVORATE_HISTORY(2),//书签历史
		SETTING(3),//设置
		REFRESH(4),//刷新
		SHARE(5),//分享
		CACHE_PIC_ABLE(6),//图片缓存 启用
		CACHE_PIC_DISABLE(-6),//图片缓存 不启用
		DOWNLOAD(7),//下载管理
		QUIT(8),//退出
		PAGE_ROLL_ABLE(9),//翻页 启用
		PAGE_ROOL_DISABLE(-9),//翻页 不启用
		BROWSER_TRACK_ABLE(10),//浏览记录 启用
		BROWSER_TRACK_DISABLE(-10),//浏览记录 不启用
		FULL_SCREEN_ABLE(11),//全屏 启用
		FULL_SCREEN_DISABLE(-11),//全屏 不启用
		FEEDBACK(12),//反馈
		NIGHT_MODE_ABLE(13),//夜间模式 启用
		NIGHT_MODE_DISABLE(-13),//夜间模式 不启用
		PERSONAL(14);//个人中心
		// 定义私有变量
		private int nCode;
		
		private int drawable;
		private String name;

		private String[] resArrays= JBLApplication.getInstance().getResources().getStringArray(R.array.setting_content_item);
		// 构造函数，枚举类型只能为私有
		private SettingMenuType(int _nCode) {
			this.nCode = _nCode;
			switch (nCode) {
			case 1:
				drawable=R.drawable.menu_add_bookmark_selector;
				name= resArrays[0];
				break;
			case -1:
				drawable=R.drawable.menu_add_bookmark_disable;
				name= resArrays[0];
				break;
			case 2:
				drawable=R.drawable.menu_combine_selector;
				name= resArrays[1];
				break;
			case 3:
				drawable=R.drawable.menu_setting_selector;
				name= resArrays[2];
				break;
			case 4:
				drawable=R.drawable.menu_refresh_selector;
				name= resArrays[3];
				break;
			case 5:
				drawable=R.drawable.menu_share_selector;
				name= resArrays[4];
				break;
			case 6:
				drawable=R.drawable.no_pic_mode_selector;
				name= resArrays[5].substring(0,4);
				break;
			case -6:
				drawable=R.drawable.no_pic_mode_press;
				name= resArrays[5].substring(4);
				break;
			case 7:
				drawable=R.drawable.menu_download_selector;
				name= resArrays[6];
				break;
			case 8:
				drawable=R.drawable.menu_quit_selector;
				name= resArrays[7];
				break;
			case 9:
				drawable=R.drawable.menu_roll_webview_selector;
				name= resArrays[8].substring(0,4);
				break;
			case -9:
				drawable=R.drawable.menu_roll_webview_press;
				name= resArrays[8].substring(4);
				break;
			case 10:
				drawable=R.drawable.menu_wuhen_selector;
				name= resArrays[9].substring(0,4);
				break;
			case -10:
				drawable=R.drawable.menu_wuhen_pressed;
				name= resArrays[9].substring(4);
				break;
			case 11:
				drawable=R.drawable.menu_fullscreen_selector;
				name= resArrays[10].substring(0,4);
				break;
			case -11:
				drawable=R.drawable.menu_fullscreen_pressed;
				name= resArrays[10].substring(4);
				break;
			case 12:
				drawable=R.drawable.menu_feedback_selector;
				name= resArrays[11];
				break;
			case 13:
				drawable=R.drawable.menu_nightmode_selector;
				name= resArrays[12].substring(0,4);
				break;
			case -13:
				drawable=R.drawable.menu_nightmode_pressed;
				name= resArrays[12].substring(4);
				break;
			case 14:
				drawable=R.drawable.menu_personal_selector;
				name= resArrays[13];
				break;
			default:
				break;
			}
		}

		@Override
		public String toString() {
			return String.valueOf("web_bool_type_"+this.nCode);
		}
		
		public int getDrawable(){
			return drawable;
		}
		public String getName(){
			return name;
		}
	};
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater = getLayoutInflater(savedInstanceState);
		View view = inflater.inflate(R.layout.main_setting_panel, container,false);
		initPages();
		initViewAndAdapter();
		viewPager = (ViewPager) view.findViewById(R.id.setting_viewpager);
		mIndicator = (LinePageIndicator) view.findViewById(R.id.setting_indicator);
		adapter = new SettingPagerAdapter(getActivity(), viewLists);
		viewPager.setAdapter(adapter);
		mIndicator.setViewPager(viewPager);
		viewPager.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.menu_bar_appear));// 加载弹出菜单栏的动画效果
		blank = (View) view.findViewById(R.id.fill_pad);
		blank.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					// 点击空白处 销毁fragment
					((BaseFragActivity) SettingPagerFragment.this.getActivity()).removeFragment(SettingPagerFragment.this);
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
		});// 最外层接管点击事件

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
		list = new ArrayList<SettingMenuType>();
		if (JBLPreference.getInstance(mContext).readInt(JBLPreference.HOST_URL_BOOLEAN) == JBLPreference.IS_HOST_URL) {
			list.add(SettingMenuType.ADD_BOOKMARK_DISABLE);
		} else {
			list.add(SettingMenuType.ADD_BOOKMARK_ABLE);
		}
		list.add(SettingMenuType.FAVORATE_HISTORY);
		list.add(SettingMenuType.SETTING);
		list.add(SettingMenuType.REFRESH);
		list.add(SettingMenuType.SHARE);
		if (JBLPreference.getInstance(mContext).readInt(BoolType.PIC_CACHE.toString()) == JBLPreference.NO_PICTURE) {
			list.add(SettingMenuType.CACHE_PIC_DISABLE);
		} else {
			list.add(SettingMenuType.CACHE_PIC_ABLE);
		}
		list.add(SettingMenuType.DOWNLOAD);
		list.add(SettingMenuType.QUIT);
		if (JBLPreference.getInstance(mContext).readInt(BoolType.TURNNING.toString()) == JBLPreference.OPEN_TURNING_BUTTON) {
			list.add(SettingMenuType.PAGE_ROOL_DISABLE);
		} else {
			list.add(SettingMenuType.PAGE_ROLL_ABLE);
		}
		if (JBLPreference.getInstance(mContext).readInt(BoolType.HISTORY_CACHE.toString()) == JBLPreference.OPEN_HISTORY) {
			list.add(SettingMenuType.BROWSER_TRACK_DISABLE);
		} else {
			list.add(SettingMenuType.BROWSER_TRACK_ABLE);
		}
		if (JBLPreference.getInstance(mContext).readInt(BoolType.FULL_SCREEN.toString()) == JBLPreference.YES_FULL) {
			list.add(SettingMenuType.FULL_SCREEN_DISABLE);
		} else {
			list.add(SettingMenuType.FULL_SCREEN_ABLE);
		}
		list.add(SettingMenuType.FEEDBACK);
		if (JBLPreference.getInstance(mContext).readInt(BoolType.BRIGHTNESS_TYPE.toString()) == JBLPreference.NIGHT_MODEL) {
			list.add(SettingMenuType.NIGHT_MODE_DISABLE);
		} else {
			list.add(SettingMenuType.NIGHT_MODE_ABLE);
		}
		list.add(SettingMenuType.PERSONAL);
		
	}

	public void setInterface(SettingItemInterface i) {
		settingInterface = i;
	}

	private void initViewAndAdapter() {
		PageCount = (int) Math.ceil(list.size() / APP_PAGE_SIZE);
		viewLists = new ArrayList<GridView>();
		for (int i = 0; i < PageCount; i++) {
			GridView appPage = new GridView(getActivity());
			appPage.setSelector(R.drawable.menu_selector);
			final SettingGridItemAdapter adapter = new SettingGridItemAdapter(getActivity(), list, i);
			appPage.setAdapter(adapter);
			appPage.setNumColumns(4);
			viewLists.add(i, appPage);

			// 菜单监听事件
			appPage.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					switch ((SettingMenuType) adapter.getItem(position)) {
					case ADD_BOOKMARK_ABLE:// 添加书签
						if (settingInterface != null)
							settingInterface.addBookMark();
						((BaseFragActivity) (SettingPagerFragment.this
								.getActivity()))
								.removeFragment(SettingPagerFragment.this);
						break;
					case FAVORATE_HISTORY:// 跳转到书签界面
						if (settingInterface != null)
							settingInterface.listBookMark();
						((BaseFragActivity) (SettingPagerFragment.this
								.getActivity()))
								.removeFragment(SettingPagerFragment.this);
						break;
					case SETTING:// 跳转到设置界面
						if (settingInterface != null)
							settingInterface.browserSetting();
						((BaseFragActivity) (SettingPagerFragment.this
								.getActivity()))
								.removeFragment(SettingPagerFragment.this);
						break;
					case REFRESH:// 刷新
						/*
						 * if(settingInterface!=null)
						 * settingInterface.listHistory();
						 */
						if (settingInterface != null)
							settingInterface.refresh();
						((BaseFragActivity) (SettingPagerFragment.this
								.getActivity()))
								.removeFragment(SettingPagerFragment.this);
						break;
					case SHARE:// 分享
						if (settingInterface != null)
							settingInterface.share();
						((BaseFragActivity) (SettingPagerFragment.this
								.getActivity()))
								.removeFragment(SettingPagerFragment.this);
						break;
					case CACHE_PIC_DISABLE:// 设置无图模式
					case CACHE_PIC_ABLE:
						if (settingInterface != null)
							settingInterface.fitlerPicLoading();
						((BaseFragActivity) (SettingPagerFragment.this
								.getActivity()))
								.removeFragment(SettingPagerFragment.this);
						break;
					case DOWNLOAD:// 下载管理
						if (settingInterface != null)
							settingInterface.manageDownload();
						((BaseFragActivity) (SettingPagerFragment.this
								.getActivity()))
								.removeFragment(SettingPagerFragment.this);
						break;
					case QUIT:// 退出系统
						if (settingInterface != null)
							settingInterface.quit();
						((BaseFragActivity) (SettingPagerFragment.this
								.getActivity()))
								.removeFragment(SettingPagerFragment.this);
						break;
					case PAGE_ROLL_ABLE:// 页面翻转
						if (JBLPreference.getInstance(mContext).readInt(
								JBLPreference.HOST_URL_BOOLEAN) == JBLPreference.ISNOT_HOST_URL) {
							if (settingInterface != null)
								settingInterface.pageTurningSwitch();
							((BaseFragActivity) (SettingPagerFragment.this
									.getActivity()))
									.removeFragment(SettingPagerFragment.this);
						}
						break;
					case BROWSER_TRACK_ABLE:// 网页无痕浏览模式
					case BROWSER_TRACK_DISABLE:
						if (settingInterface != null)
							settingInterface.withoutTrace();
						((BaseFragActivity) (SettingPagerFragment.this
								.getActivity()))
								.removeFragment(SettingPagerFragment.this);
						break;
					case FULL_SCREEN_ABLE:// 网页全屏浏览模式
					case FULL_SCREEN_DISABLE:
						if (settingInterface != null)
							settingInterface.fullScreen();
						((BaseFragActivity) (SettingPagerFragment.this
								.getActivity()))
								.removeFragment(SettingPagerFragment.this);
						break;
					case FEEDBACK:// 意见反馈
						break;
					case NIGHT_MODE_ABLE:// 夜间模式
					case NIGHT_MODE_DISABLE:
						if (settingInterface != null)
							settingInterface.nightBright();
						((BaseFragActivity) (SettingPagerFragment.this
								.getActivity()))
								.removeFragment(SettingPagerFragment.this);
						break;
					default:
						break;
					}

				}
			});
			
		}
	}

}
