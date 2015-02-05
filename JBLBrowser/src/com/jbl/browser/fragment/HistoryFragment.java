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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.jbl.browser.R;
import com.jbl.browser.adapter.BookMarkAdapter;
import com.jbl.browser.adapter.HistoryAdapter;
import com.jbl.browser.bean.History;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.db.HistoryDao;

/**
 * 历史记录fragment
 * @author Desmond
 *
 */
public class HistoryFragment extends SherlockFragment implements OnItemClickListener, OnItemLongClickListener{
	public final static String TAG="BookMarkFragment";
	//历史记录listView
	ListView listview;
	//历史记录数据
	List<History> list=new ArrayList<History>();
	/*//历史记录时间段标示
	TextView tv_history_time;*/
	//历史记录适配器
	HistoryAdapter historyAdapter;
	//历史记录操作类
	HistoryDao historydao;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		historydao=new HistoryDao(getActivity());
		super.onCreate(savedInstanceState);
	}
	//从数据库中获得数据
	public List<History> getData(){
		List<History> history=new ArrayList<History>();
		history=historydao.queryAll();
		return history;
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
		initDataHistory();
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(this);
		return view;
	}
	/**
	 * 初始化ListView中历史记录的数据
	 * */
	@SuppressWarnings("deprecation")
	private void initDataHistory() {
		list=getData();
		historyAdapter=new HistoryAdapter(getActivity(), list);
		listview.setAdapter(historyAdapter);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		final String webAddress;
		webAddress=((TextView)view.findViewById(R.id.url_address)).getText().toString();
		//1获取一个对话框的创建器
		AlertDialog.Builder builder=new Builder(getActivity());
		//2所有builder设置一些参数
		builder.setTitle("清空记录");
		builder.setMessage("是否清空");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Boolean flag=historydao.clearHistory();
				if(flag){
					Toast.makeText(getActivity(), "清空成功", 100).show();
					initDataHistory();	
					listview.invalidate();
				}
				else{
					Toast.makeText(getActivity(), "清空失败", 100);
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