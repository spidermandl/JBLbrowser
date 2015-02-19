package com.jbl.browser.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.interfaces.TopActionbarInterface;

/**
 * 顶部搜索ActionBar
 * @author Desmond
 *
 */
public class TopMenuFragment extends SherlockFragment implements View.OnClickListener,View.OnTouchListener{

	TopActionbarInterface topActionbarInterfaces;
	
	//定义顶部ActionBar控件
	private EditText mSearch; //输入网址搜索
	private ImageView mCode; //二维码搜索
	private ImageView mLand; //登录注册
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_main_topmenu, container, false);
		mSearch=(EditText)view.findViewById(R.id.top_menu_search);
		mCode=(ImageView)view.findViewById(R.id.top_menu_qrcode);
		mLand=(ImageView)view.findViewById(R.id.top_menu_person);
		
		mCode.setOnClickListener(this);
		mLand.setOnClickListener(this);
		mSearch.setOnTouchListener(this);
		mSearch.setInputType(InputType.TYPE_NULL);
		mSearch.requestFocus();
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
		case R.id.top_menu_search:
			if(topActionbarInterfaces!=null){
				//topActionbarInterfaces.goEditInput();
			}
			break;
		case R.id.top_menu_qrcode:
			if(topActionbarInterfaces!=null){
				topActionbarInterfaces.goCode();
			}
			break;
		case R.id.top_menu_person:
			if(topActionbarInterfaces!=null){
				topActionbarInterfaces.goLand();
			}
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			switch (v.getId()){
			case R.id.top_menu_search:
				if(topActionbarInterfaces!=null){
					topActionbarInterfaces.goEditInput();
				}
				break;
			}
			break;

		default:
			break;
		}
		return false;
	}

	
}
