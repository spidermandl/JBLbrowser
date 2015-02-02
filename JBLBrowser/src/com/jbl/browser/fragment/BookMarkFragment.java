package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.jbl.broswer.bean.BookMark;
import com.jbl.broswer.db.BookMarkDao;
import com.jbl.browser.R;
import com.jbl.browser.adapter.BookMarkAdapter;

/**
 * 书签fragment
 * @author Desmond
 *
 */
public class BookMarkFragment extends SherlockFragment implements OnItemLongClickListener{
	public final static String TAG="BookMarkFragment";
	//书签listView
	ListView listview;
	//书签数据
	List<BookMark> list=new ArrayList<BookMark>();
	//书签操作类
	BookMarkDao bookmarkdao;
	BookMarkAdapter bookMarkAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	/**
	 * 测试数据
	 */
	public void init(){
		BookMark b1=new BookMark();
		b1.setWebName("百度");
		b1.setWebAddress("http:baidu.com");
		list.add(b1);
	}
	//从数据库中获得数据
	public List<BookMark> getData(){
		bookmarkdao=new BookMarkDao(getActivity());
		List<BookMark> list=bookmarkdao.queryAll();
		if(list==null){
			Toast.makeText(getActivity(), "没有书签", 100).show();
		}
		return list;
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
		init();
		//list=getData();
		bookMarkAdapter=new BookMarkAdapter(getActivity(), list);
		listview.setAdapter(bookMarkAdapter);
		listview.setOnItemLongClickListener(this);
		return view;
	}
	//长按显示删除确定对话框
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub		
		final String webAddress;
		webAddress=((TextView)view.findViewById(R.id.url_address)).getText().toString();
		//1获取一个对话框的创建器
		AlertDialog.Builder builder=new Builder(getActivity());
		//2所有builder设置一些参数
		builder.setTitle("删除书签");
		builder.setMessage("是否删除");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				int i=bookmarkdao.deleteBookMarkByWebAddress(webAddress);
				if(i!=0){
					Toast.makeText(getActivity(), "删除成功", 100).show();
				}
			}
		});
		builder.setNeutralButton("取消",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		builder.create().show();
		return false;
	}
	
}
