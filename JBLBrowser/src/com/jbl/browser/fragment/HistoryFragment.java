package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.jbl.browser.BookMarkAdapter;
import com.jbl.browser.HistoryAdapter;
import com.jbl.browser.R;
import com.jbl.browser.model.BookMarkBean;
import com.jbl.browser.model.HistoryBean;

/**
 * 历史记录fragment
 * @author Desmond
 *
 */
public class HistoryFragment extends SherlockFragment{
	public final static String TAG="BookMarkFragment";
	//历史记录listView
	ListView listview;
	//历史记录数据
	List<HistoryBean> list=new ArrayList<HistoryBean>();
	//历史记录时间段标示
	TextView tv_history_time;
	HistoryAdapter historyAdapter=new HistoryAdapter(HistoryFragment.this, list);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		listview.setAdapter(historyAdapter);
		super.onCreate(savedInstanceState);
	}
	/**
	 * 测试数据
	 */
	public void init(){
		HistoryBean b1=new HistoryBean("百度","http:\\baidu.com",201);
		list.add(b1);
	}
	//从数据库中获得数据
		public void getData(){
			
		}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		final ActionBar ab = this.getSherlockActivity().getSupportActionBar();

		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		setHasOptionsMenu(true);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 0, "Back").setIcon(R.drawable.back_web).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.history_fragment, container, false);
		listview=(ListView)view.findViewById(R.id.list_view_history_today);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}