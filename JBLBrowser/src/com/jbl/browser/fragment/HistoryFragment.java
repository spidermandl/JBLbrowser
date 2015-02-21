package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.jbl.browser.bean.History;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.activity.MainFragActivity;
import com.jbl.browser.adapter.HistoryAdapter;
import com.jbl.browser.db.HistoryDao;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.StringUtils;

/**
 * 历史记录fragment
 * @author huyingying
 *
 */
public class HistoryFragment extends SherlockFragment implements OnItemClickListener, OnItemLongClickListener{
	
	public final static String TAG="HistoryFragment";
	//历史记录listView
	ListView listview;
	//历史记录数据
	List<History> list=new ArrayList<History>();
	/*//历史记录时间段标示
	TextView tv_history_time;*/
	//历史记录适配器
	HistoryAdapter historyAdapter;
	//无历史记录
	TextView noHistory;
	//返回图标
	ImageView back;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.history_fragment, container, false);
		listview=(ListView)view.findViewById(R.id.list_view_history_today);
		noHistory=(TextView)view.findViewById(R.id.empty);
		initDataHistory();
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(this);
		return view;
	}
	/**
	 * 初始化ListView中历史记录的数据
	 * */
	private void initDataHistory() {
		listview.setVisibility(View.GONE);
		list=new HistoryDao(getActivity()).queryAll();//从数据库中获得数据
		if(list.size()==0){      //没有历史记录时屏幕中间显示“没有历史记录”文字
			noHistory.setVisibility(View.VISIBLE);
		}else{                   //有历史记录时显示历史记录
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
		String webAddress=((TextView)view.findViewById(R.id.url_address)).getText().toString();
		JBLPreference.getInstance(getActivity()).writeString(JBLPreference.BOOKMARK_HISTORY_KEY, webAddress);
		Intent intent=new Intent();
        intent.setClass(getActivity(), MainFragActivity.class);
        getActivity().startActivity(intent);
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
				Boolean flag=new HistoryDao(getActivity()).clearHistory();
				if(flag){
					Toast.makeText(getActivity(), "清空成功", 100).show();
					initDataHistory();	
					listview.invalidate();
				}
				else{
					Toast.makeText(getActivity(), "清空失败", 100).show();
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