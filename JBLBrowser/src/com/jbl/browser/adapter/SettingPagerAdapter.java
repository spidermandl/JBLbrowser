package com.jbl.browser.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;
import com.viewpager.indicator.IconPagerAdapter;

/**
 * 设置界面滑动adapter
 * 
 * @author Desmond
 * 
 */
public class SettingPagerAdapter extends PagerAdapter implements
		IconPagerAdapter {
	private List<GridView> mViewPages;

	public SettingPagerAdapter(Context context, List<GridView> array) {
		mViewPages=array;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mViewPages.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(mViewPages.get(arg1));

		return mViewPages.get(arg1);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
		//
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		// TODO Auto-generated method stub
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View container) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return 0;
	}
}
