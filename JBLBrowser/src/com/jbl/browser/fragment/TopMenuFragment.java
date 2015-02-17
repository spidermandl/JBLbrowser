package com.jbl.browser.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.interfaces.TopActionbarInterface;
import com.jbl.browser.view.ScaleImageView;
import com.jbl.browser.view.TopEdittextView;
import com.jbl.browser.view.TopImageView;

/**
 * 顶部ActionBar
 * */

public class TopMenuFragment extends SherlockFragment implements View.OnClickListener{

	TopActionbarInterface topActionbarInterfaces;
	
	//定义顶部ActionBar控件
	private TopImageView mSearch; //1.1搜索图标
	private TopEdittextView mInputAdress; //1.2输入网址搜索
	private TopImageView mCode; //1.3二维码搜索
	private TopImageView mLand; //1.4登录注册
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.top_menu_actionbar, container, false);
		mSearch=(TopImageView)view.findViewById(R.id.topMenu_mSearch);//1.1
		mInputAdress=(TopEdittextView)view.findViewById(R.id.topMenu_mInputAdress);//1.2
		mCode=(TopImageView)view.findViewById(R.id.topMenu_mCode);//1.3
		mLand=(TopImageView)view.findViewById(R.id.topMneu_mLand);//1.4
		
		mSearch.setOnClickListener(this);
		mInputAdress.setOnClickListener(this);
		mCode.setOnClickListener(this);
		mLand.setOnClickListener(this);
		return view;
	}

	public void setTopActionbar(TopActionbarInterface i){
		topActionbarInterfaces=i;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.topMenu_mSearch:
			if(topActionbarInterfaces!=null){
				topActionbarInterfaces.goSearch();
			}
			break;
		case R.id.topMenu_mInputAdress:
			if(topActionbarInterfaces!=null){
				topActionbarInterfaces.goEditInput();
			}
			break;
		case R.id.topMenu_mCode:
			if(topActionbarInterfaces!=null){
				topActionbarInterfaces.goCode();
			}
			break;
		case R.id.topMneu_mLand:
			if(topActionbarInterfaces!=null){
				topActionbarInterfaces.goLand();
			}
			break;
		}
	}

	
}
