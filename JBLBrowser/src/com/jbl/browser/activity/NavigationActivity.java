package com.jbl.browser.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.jbl.browser.R;


public class NavigationActivity extends BaseFragActivity {
	private final String TAG = NavigationActivity.class.getSimpleName();
	ViewPager mViewPager;
	// 导航页图片资源
	public int[] guides = new int[] { 
			R.drawable.navigation2,R.drawable.two,R.drawable.navigation3 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		mViewPager = (ViewPager) findViewById(R.id.viewFlipper1);
		initWithPageGuideMode();
	}

	/**
	 * 程序导航页效果
	 */
	public void initWithPageGuideMode() {

		List<View> mList = new ArrayList<View>();
		LayoutInflater inflat = LayoutInflater.from(this);
		// 先添加一个最左侧空的view
		View item = inflat.inflate(R.layout.pageguide, null);
		mList.add(item);
		for (int index : guides) {
			item = inflat.inflate(R.layout.pageguide, null);
			item.setBackgroundResource(index);
			mList.add(item);
		}
		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NavigationActivity.this,
						MainFragActivity.class);
				startActivity(intent);
				NavigationActivity.this.finish();

			}
		});
		// 再添加一个最右侧空的view
		item = inflat.inflate(R.layout.pageguide, null);
		mList.add(item);
		// ViewPager最重要的设置Adapter，这和ListView一样的原理
		MViewPageAdapter adapter = new MViewPageAdapter(mList);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(adapter);
		mViewPager.setCurrentItem(1);

	}

	/**
	 * 内部类，继承PagerAdapter，当然你也可以直接 new PageAdapter
	 */
	class MViewPageAdapter extends PagerAdapter implements OnPageChangeListener {

		private List<View> mViewList;

		public MViewPageAdapter(List<View> views) {
			mViewList = views;
		}

		@Override
		public int getCount() {
			return mViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mViewList.get(position), 0);
			return mViewList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mViewList.get(position));
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {

			if (position == 0) {
				mViewPager.setCurrentItem(1);
			} else if (position == mViewList.size() - 1) {
				mViewPager.setCurrentItem(position - 1);

			}

		}

	}
}
