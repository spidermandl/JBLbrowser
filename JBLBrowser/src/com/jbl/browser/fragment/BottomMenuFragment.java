package com.jbl.browser.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.interfaces.ToolbarItemInterface;
import com.unionpay.upomp.bypay.activity.GetpassActivity;

/**
 * 主页面底部菜单栏
 * @author yyjoy-mac3
 *
 */
public class BottomMenuFragment extends SherlockFragment implements View.OnClickListener{
	public final static String TAG = "BottomMenuFragment";
	ToolbarItemInterface toolbarInterfaces;
	
	//定义操作栏控件
	private ImageView mBack; // 3.1  后退
	private ImageView mForward; // 3.2  前进
	private ImageView mHome; // 3.3  Home
	private ImageView mMultiWindows;// 3.4  切换多页模式
	private ImageView mMenu;// 3.5  选项菜单
	private ImageView mWifi;//3.6  
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bottom_main_toolbar, container, false);
		
		mBack = (ImageView) view.findViewById(R.id.toolbar_back); // 3.1
		mForward = (ImageView) view.findViewById(R.id.toolbar_forward); // 3.2
        mHome = (ImageView) view.findViewById(R.id.toolbar_home); // 3.3
        mMenu = (ImageView) view.findViewById(R.id.toolbar_menu); // 3.4
        mMultiWindows = (ImageView) view.findViewById(R.id.toolbar_multipage); // 3.5
        mWifi = (ImageView)view.findViewById(R.id.toolbar_wifi);//3.6
        
        mBack.setOnClickListener(this);
        mForward.setOnClickListener(this);
        mHome.setOnClickListener(this);
        mMenu.setOnClickListener(this);
        mMultiWindows.setOnClickListener(this);
        mWifi.setOnClickListener(this);
        
		return view;
	}
	
	public void setInterface(ToolbarItemInterface i){
		toolbarInterfaces=i;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.toolbar_back:
			if(toolbarInterfaces!=null)
				toolbarInterfaces.goBack();
			break;
		case R.id.toolbar_forward:
			if(toolbarInterfaces!=null)
				toolbarInterfaces.goForward();
			break;
		case R.id.toolbar_home:
			if(toolbarInterfaces!=null)
				toolbarInterfaces.goHome();
			break;
		case R.id.toolbar_menu:
			if(toolbarInterfaces!=null)
				toolbarInterfaces.goMenu();
			break;
		case R.id.toolbar_multipage:
			if(toolbarInterfaces!=null)
				toolbarInterfaces.goMultiWindow();
			break;
		case R.id.toolbar_wifi:
			if(toolbarInterfaces!=null)
				toolbarInterfaces.goWifi();
			break;
		default:
			break;
		}
		
	}
}
