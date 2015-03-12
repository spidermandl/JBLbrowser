package com.jbl.browser.fragment;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.WebWindowManagement;
import com.jbl.browser.adapter.MultipageAdapter;
import com.jbl.browser.adapter.WebHorizontalViewAdapter;
import com.jbl.browser.view.ScaleImageView;
import com.jbl.browser.view.WebHorizontalView;
import com.viewpager.indicator.CirclePageIndicator;

/**
 * 多页操作界面
 * @author Desmond
 *
 */
public class MultipageFragment extends SherlockFragment implements OnClickListener{
	
	public final static String TAG="MultipageFragment";
	
	private ViewPager multiViewPager;//多页效果
	CirclePageIndicator multipageIndicator;
	private ArrayList<View> mViewPages;
	
	private WebHorizontalView mHorizontalScrollView;
	private WebHorizontalViewAdapter mAdapter;
	
	private TextView newWindow;
	private ScaleImageView multiPageNum;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_multipage, container, false);
		multiViewPager=(ViewPager) view.findViewById(R.id.multipage_viewpager);
		multipageIndicator=(CirclePageIndicator)view.findViewById(R.id.multipage_indicator);
		mViewPages=new ArrayList<View>();
		for(int i=0;i<WebWindowManagement.getInstance().getCount();i++){
			mViewPages.add(inflater.inflate(R.layout.multipage_bg, container,false));
		}
		multiViewPager.setAdapter(new MultipageAdapter(mViewPages));
        mHorizontalScrollView = (WebHorizontalView) view.findViewById(R.id.id_horizontalScrollView);
        mAdapter = new WebHorizontalViewAdapter();

        // 设置适配器
        multipageIndicator.setViewPager(multiViewPager);
        mHorizontalScrollView.initDatas(mAdapter);
        mHorizontalScrollView.setViewPager(multiViewPager);
        mHorizontalScrollView.setIndicator(multipageIndicator);
        
        newWindow = (TextView)view.findViewById(R.id.new_window);
        newWindow.setOnClickListener(this);
        multiPageNum=(ScaleImageView)view.findViewById(R.id.multi_page_num);
        multiPageNum.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_window:
			WebWindowManagement.getInstance().replaceWebViewWithIndex(
					null, WebWindowManagement.getInstance().getCount(), false);
			getFragmentManager().popBackStack();
			break;
		case R.id.multi_page_num:
			getFragmentManager().popBackStack();
		default:
			break;
		}
		
		
	}

}
