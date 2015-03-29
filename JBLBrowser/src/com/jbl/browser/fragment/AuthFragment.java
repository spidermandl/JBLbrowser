package com.jbl.browser.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.tools.BusinessTool;
import com.jbl.browser.utils.UrlUtils;
import com.jbl.browser.view.ProgressWebView;

/**
 *  用户验证界面
 * 
 * @author Desmond
 * 
 */
public class AuthFragment extends SherlockFragment {

	ProgressWebView mWebView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
		
	}
}
