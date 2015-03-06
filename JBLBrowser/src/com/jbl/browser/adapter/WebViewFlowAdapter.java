package com.jbl.browser.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.jbl.browser.WebWindowManagement;
import com.jbl.browser.view.coverflow.FancyCoverFlowAdapter;

/**
 * 
 * @author yyjoy-mac3
 *
 */
public class WebViewFlowAdapter extends FancyCoverFlowAdapter {

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return WebWindowManagement.getInstance().getCount();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return WebWindowManagement.getInstance().getItem(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getCoverFlowItem(int position, View reusableView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		return WebWindowManagement.getInstance().getView(position);
	}

}
