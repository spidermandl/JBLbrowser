package com.jbl.browser.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.ActionMode;
import com.jbl.browser.R;
import com.jbl.browser.adapter.RecommendAdapter;
/*
 * 推荐主页面
 * 
 * */
public class RecommendMainFragment extends SherlockFragment{
	public final static String TAG="RecommendMainFragment";
	public static final int DEFAULT_OFFSCREEN_PAGES = 2;//加载页面数量
	ViewPager mViewPager;
	RecommendAdapter mAdapter;
	ActionMode mActionMode;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.recommend_main, container,false);
		mViewPager=(ViewPager)view.findViewById(R.id.mViewPager);
		mViewPager.setOffscreenPageLimit(DEFAULT_OFFSCREEN_PAGES);//预告加载页面数量
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}	
}
