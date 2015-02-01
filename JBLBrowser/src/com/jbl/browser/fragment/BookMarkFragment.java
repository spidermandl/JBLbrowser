package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.jbl.browser.BookMarkAdapter;
import com.jbl.browser.R;
import com.jbl.browser.model.BookMarkBean;

/**
 * 书签fragment
 * @author Desmond
 *
 */
public class BookMarkFragment extends SherlockFragment{
	public final static String TAG="BookMarkFragment";
	//书签listView
	ListView listview;
	//书签数据
	List<BookMarkBean> list=new ArrayList<BookMarkBean>();
	BookMarkAdapter bookMarkAdapter=new BookMarkAdapter(BookMarkFragment.this, list);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		listview.setAdapter(bookMarkAdapter);
		super.onCreate(savedInstanceState);
	}
	/**
	 * 测试数据
	 */
	public void init(){
		BookMarkBean b1=new BookMarkBean("百度","http:\\baidu.com");
		list.add(b1);
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
		View view = inflater.inflate(R.layout.bookmark_fragment, container, false);
		listview=(ListView)view.findViewById(R.id.list_view_bookmark);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
}
