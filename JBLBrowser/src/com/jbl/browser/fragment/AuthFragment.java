package com.jbl.browser.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.JBLApplication;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.activity.MainFragActivity;
import com.jbl.browser.bean.UserInfo;
import com.jbl.browser.db.UserInfoDao;
import com.jbl.browser.interfaces.LoadURLInterface;
import com.jbl.browser.tools.BusinessTool;
import com.jbl.browser.utils.UrlUtils;
import com.jbl.browser.view.ProgressWebView;

/**
 *  用户验证界面
 * 
 * @author Desmond
 * 
 */
public class AuthFragment extends SherlockFragment implements LoadURLInterface{

	ProgressWebView mWebView;
	public static ActionBar ab;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

//		this.getActivity().setTheme(R.style.Theme_Sherlock_Light);
//		/**
//		 * 设置actionbar样式
//		 */
//		ab = ((BaseFragActivity)this.getActivity()).getSupportActionBar();	
// 		ab.setDisplayHomeAsUpEnabled(true);		 	
// 		ab.setDisplayUseLogoEnabled(false);		 		
// 		ab.setDisplayShowHomeEnabled(false);		 		
// 		ab.setDisplayShowTitleEnabled(true);
// 		ab.setTitle(R.string.history_favourate_title);
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mWebView = new ProgressWebView(this.getActivity());
		init();
		return mWebView;
	}

	private void init() {

		mWebView.loadUrl(UrlUtils.URL_AUTH+BusinessTool.getDeviceID(getActivity()));
		mWebView.setDefaultSetting();
		mWebView.setInterface(this);
		
	}

	@Override
	public void startPage(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopPage(WebView view, String url) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void authSuccess() {
		UserInfo user=new UserInfo();
		user.setDeviceID(BusinessTool.getDeviceID(getActivity()));
		new UserInfoDao(getActivity()).userApproved(user);
		
		((BaseFragActivity)getActivity()).navigateTo(MainPageFragment.class, null, true, MainFragActivity.TAG);
	}
}
