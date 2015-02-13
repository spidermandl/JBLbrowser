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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.adapter.BookMarkAdapter;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.utils.JBLPreference;

/**
 * 书签fragment
 * @author Desmond
 *
 */
public class BookMarkFragment extends SherlockFragment implements OnItemLongClickListener, OnItemClickListener{
	public final static String TAG="BookMarkFragment";
	//书签listView
	ListView listview;
	//书签数据
	List<BookMark> list=new ArrayList<BookMark>();
	//网址
	String webAddress="";
	//网名
	String webName="";
	BookMarkAdapter bookMarkAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	//从数据库中获得数据
	public List<BookMark> getData(){
		List<BookMark> list;
		list=new BookMarkDao(getActivity()).queryAll();
		if(list==null){
			Toast.makeText(getActivity(), R.string.no_bookmark, 100).show();
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
		menu.removeGroup(0);
		//添加返回图标
		menu.add(1, 1, 0, "Back").setIcon(R.drawable.back_web).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.bookmark_fragment, container, false);
		listview=(ListView)view.findViewById(R.id.list_view_bookmark);
		//init();
		initDataFavorites();
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(this);
		return view;
	}
	/**
	 * 初始化ListView中书签的数据
	 * */
	@SuppressWarnings("deprecation")
	private void initDataFavorites() {
		list=getData();
		bookMarkAdapter=new BookMarkAdapter(getActivity(), list);
		listview.setAdapter(bookMarkAdapter);
	}
	//长按显示删除确定对话框
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub		
		final String webAddress;
		final String webName;
		webName=((TextView)view.findViewById(R.id.url_name)).getText().toString();
		webAddress=((TextView)view.findViewById(R.id.url_address)).getText().toString();
		//1获取一个对话框的创建器
		AlertDialog.Builder builder=new Builder(getActivity());
		//2所有builder设置一些参数
		builder.setTitle(R.string.delete_bookmark);
		builder.setMessage("是否要删除\""+webName+"\"这个书签?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				int i=new BookMarkDao(getActivity()).deleteBookMarkByWebAddress(webAddress);
				if(i!=0){
					Toast.makeText(getActivity(), R.string.delete_bookmark_succeed, 100).show();
					initDataFavorites();
					listview.invalidate();
				}
				else{
					Toast.makeText(getActivity(),R.string.delete_bookmark_fail, 100);
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
	//单击跳转到网页
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String webAddress=((TextView)view.findViewById(R.id.url_address)).getText().toString();
		JBLPreference.getInstance(getActivity()).writeString(JBLPreference.BOOKMARK_HISTORY_KEY, webAddress);
		((BaseFragActivity)getActivity()).navigateTo(MainPageFragment.class, null, false,MainPageFragment.TAG);
	}
	
}
