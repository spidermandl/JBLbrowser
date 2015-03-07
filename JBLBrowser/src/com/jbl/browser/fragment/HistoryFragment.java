package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.activity.HistoryFavourateActivity;
import com.jbl.browser.activity.MainFragActivity;
import com.jbl.browser.adapter.HistoryAdapter;
import com.jbl.browser.bean.History;
import com.jbl.browser.db.HistoryDao;
import com.jbl.browser.utils.JBLPreference;

/**
 * 历史记录fragment
 * @author huyingying
 *
 */
public class HistoryFragment extends SherlockFragment implements OnItemClickListener, OnItemLongClickListener{
	
	/**
	 * 全部变成静态，在activity中调用
	 * */
	public final static String TAG="HistoryFragment";
	//历史记录listView
	public static ListView listview;
	//历史记录数据
	public static List<History> list=new ArrayList<History>();
	/*//历史记录时间段标示
	TextView tv_history_time;*/
	//历史记录适配器
	public static HistoryAdapter historyAdapter;
	//无历史记录
	public static ImageView noHistory;
	//返回图标
	ImageView back;
	//删除ID
	public static int deleteId;
	//选择长按监听之后，再点击返回
	boolean mFlagSelsct=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.history_fragment, container, false);
		listview=(ListView)view.findViewById(R.id.list_view_history_today);
		noHistory=(ImageView)view.findViewById(R.id.cloud_history_empty);		
		initDataHistory();
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(this);
		return view;
	}
	/**
	 * 初始化ListView中历史记录的数据
	 * */
	public  void initDataHistory() {	
		listview.setVisibility(View.GONE);
		list=new HistoryDao(getActivity()).queryAll();//从数据库中获得数据
		if(list.size()==0){      //没有历史记录时屏幕中间显示“没有历史记录”文字
			noHistory.setVisibility(View.VISIBLE);
		}else{                   //有历史记录时显示历史记录
			Collections.sort(list,new Comparator<History>() { //倒序排列

				@Override
				public int compare(History lhs, History rhs) {
					// TODO Auto-generated method stub
					if(lhs.getId()<rhs.getId())
						return 1;
					return -1;
				}
			});
			noHistory.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			historyAdapter=new HistoryAdapter(getActivity(), list);
			listview.setAdapter(historyAdapter);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		//String webAddress=((TextView)view.findViewById(R.id.url_address)).getText().toString();
		//当mFlagSelsct=false(默认)时，正常执行点击操作，当为true时，返回Item默认颜色。不进行点击操作。
		if(!mFlagSelsct){
		String webAddress=list.get(position).getWebAddress();
		JBLPreference.getInstance(getActivity()).writeString(JBLPreference.BOOKMARK_HISTORY_KEY, webAddress);
		this.getActivity().finish();
		Intent intent=new Intent();
        intent.setClass(getActivity(), MainFragActivity.class);
        getActivity().startActivity(intent);
		}
		//长按监听之后再次点击，选项删除
		else{
			view.setBackgroundColor(Color.WHITE);//恢复默认颜色
			//恢复actionbar的返回图标与title
			HistoryFavourateActivity.ab.setDisplayHomeAsUpEnabled(true);
			HistoryFavourateActivity.ab.setDisplayShowTitleEnabled(true);
			mFlagSelsct=false;
		}
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		HistoryFavourateActivity.mMenuFlag=false;
		deleteId=list.get(position).getId();//获取删除的ItemID
		view.setBackgroundColor(Color.LTGRAY);//设置选中背景
		//删除actionbar的返回图标和title
		HistoryFavourateActivity.ab.setDisplayHomeAsUpEnabled(false);
		HistoryFavourateActivity.ab.setDisplayShowTitleEnabled(false);
		mFlagSelsct=true;//选中开关
		return true;
	}
}