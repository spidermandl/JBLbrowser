package com.jbl.browser.fragment;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.adapter.MultipageAdapter;
import com.viewpager.indicator.LinePageIndicator;
import com.viewpager.indicator.PageIndicator;
public class MultipageFragment extends SherlockFragment{
	public final static String TAG="MultipageFragment";
	private ViewPager multiViewPager;//多页效果
	PageIndicator multipageIndicator;
	MultipageAdapter multipageAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.multipage_panel, container, false);
		multiViewPager=(ViewPager) view.findViewById(R.id.multipage_viewpager);
		multipageIndicator=(LinePageIndicator)view.findViewById(R.id.multipage_indicator);
		multiPage();
		return view;
	}
	private void multiPage() {
			MultipageAdapter.mViewPages = new ArrayList<View>();
	        addView(MultipageAdapter.mViewPages, "file:///android_asset/experience/exp_article2.html");
			addView(MultipageAdapter.mViewPages, "file:///android_asset/experience/exp_article6.html");
			addView(MultipageAdapter.mViewPages, "file:///android_asset/experience/exp_article10.html");
			multipageAdapter = new MultipageAdapter();
			multiViewPager.setAdapter(multipageAdapter);
			multipageIndicator.setViewPager(multiViewPager);
	} 
	private void addView(List<View> viewList,String url){
			 WebView webView=new WebView(getActivity());
			 webView.loadUrl(url);
			 viewList.add(webView);
    }
}
