package com.jbl.browser.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.interfaces.ToolbarItemInterface;
import com.jbl.browser.view.ScaleImageView;

/**
 * 主页面底部菜单栏
 * @author yyjoy-mac3
 *
 */
public class BottomMenuFragment extends SherlockFragment implements View.OnClickListener{

	ToolbarItemInterface toolbarInterfaces;
	
	//定义操作栏控件
	private ScaleImageView mBack; // 3.1  后退
	private ScaleImageView mForward; // 3.2  前进
	private ScaleImageView mHome; // 3.3  Home
	private ScaleImageView mMultiWindows;// 3.4  切换多页模式
	private ScaleImageView mMenu;// 3.5  选项菜单
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bottom_main_toolbar, container, false);
		
		mBack = (ScaleImageView) view.findViewById(R.id.toolbar_back); // 3.1
		mForward = (ScaleImageView) view.findViewById(R.id.toolbar_forward); // 3.2
        mHome = (ScaleImageView) view.findViewById(R.id.toolbar_home); // 3.3
        mMenu = (ScaleImageView) view.findViewById(R.id.toolbar_menu); // 3.4
        mMultiWindows = (ScaleImageView) view.findViewById(R.id.toolbar_multipage); // 3.5
        
        mBack.setOnClickListener(this);
        mForward.setOnClickListener(this);
        mHome.setOnClickListener(this);
        mMenu.setOnClickListener(this);
        mMultiWindows.setOnClickListener(this);
        
		return view;
	}
	
	public void setToolbarAction(ToolbarItemInterface i){
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
		default:
			break;
		}
		
	}
}
