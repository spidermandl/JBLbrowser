package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jbl.browser.R;
import com.jbl.browser.adapter.SettingGridItemAdapter;
import com.jbl.browser.interfaces.SettingItemInterface;
import com.jbl.browser.utils.JBLPreference;

/*
 * 设置界面滑动分页控制类
 */

public class SettingPagerFragment {//extends SherlockDialogFragment{
	private static final String TAG = "SettingPagerFragment";
	
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
	//点击回调接口
	private SettingItemInterface settingInterface;
	
	public SettingPagerFragment(Context context) {
		mContext = context;
		resArrays= mContext.getResources().getStringArray(R.array.setting_content_item);		
		mPageList = new ArrayList<List<String>>();
		mGridViews = new ArrayList<GridView>();
		mViewPages = new ArrayList<View>();
		initPages(resArrays);
		initViewAndAdapter();

	}
//	public static SettingPagerFragment newInstance(String title) {  
//		SettingPagerFragment frag = new SettingPagerFragment();  
//        return frag;  
//    }
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		mContext = this.getActivity();
//		resArrays= mContext.getResources().getStringArray(R.array.setting_content_item);
//		mPageList = new ArrayList<List<String>>();
//		mGridViews = new ArrayList<GridView>();
//		mViewPages = new ArrayList<View>();
//		initPages(resArrays);
//		initViewAndAdapter();
//		setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);//android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
//		//getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//		super.onCreate(savedInstanceState);
//	}
//	
//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		return super.onCreateDialog(savedInstanceState);
//	}
//	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View v=new View(mContext);
//		v.setBackgroundColor(0x44CCCCCC);
//		return v;
//	}
	
	
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
						
					}
					
				});
			}
		}
	}
	public List<View> getPageViews() {
		return mViewPages;
	}
}
