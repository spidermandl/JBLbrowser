package com.jbl.browser.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.interfaces.MultiageToolbarItemInterface;
import com.jbl.browser.view.NewWindowTextView;
import com.jbl.browser.view.ScaleTextView;

public class MultipageBottomMenuFragment extends SherlockFragment implements View.OnClickListener{
	public final static String TAG = "MultipageBottomMenuFragment";
	MultiageToolbarItemInterface multiageToolbarItemInterface;
	
	//定义操作栏控件
	private NewWindowTextView mNewWindow; // 3.1  新建窗口
	private ScaleTextView mMultiWindows;// 3.4  多页模式
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.multipage_bottom_toolbar, container, false);
		mNewWindow=(NewWindowTextView)view.findViewById(R.id.new_window);
        mMultiWindows = (ScaleTextView) view.findViewById(R.id.toolbar_multipage); 
        mNewWindow.setOnClickListener(this);
        mMultiWindows.setOnClickListener(this);
		return view;
	}
	
	public void setInterface(MultiageToolbarItemInterface i){
		multiageToolbarItemInterface=i;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.new_window:
			if(multiageToolbarItemInterface!=null)
				multiageToolbarItemInterface.newWindow();;
			break;
		case R.id.toolbar_multipage:
			if(multiageToolbarItemInterface!=null)
				multiageToolbarItemInterface.multipageWindow();;
			break;
		default:
			break;
		}
		
	}
}