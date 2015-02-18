package com.jbl.browser.fragment;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.adapter.BookMarkAdapter;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.utils.JBLPreference;

/**
 * 书签fragment
 * @author huyingying
 *
 */
public class BookMarkFragment extends SherlockFragment implements OnItemLongClickListener, OnItemClickListener{
	
	public final static String TAG="BookMarkFragment";
	
	//书签listView
	ListView listview;
	//书签数据
	List<BookMark> list=null;
	//网址
	String webAddress="";
	//网名
	String webName="";
	//没有书签
	TextView noBookmark;
	//返回图标
	ImageView back;
	BookMarkAdapter bookMarkAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_bookmark, container, false);	
		listview=(ListView)view.findViewById(R.id.list_view_bookmark);
		noBookmark=(TextView)view.findViewById(R.id.empty);
		initDataFavorites();
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(this);
		return view;
	}
	
	/**
	 * 初始化ListView中书签的数据
	 * */
	private void initDataFavorites() {
		listview.setVisibility(View.GONE);
		list=new BookMarkDao(getActivity()).queryAll();//从数据库中获得数据
		if(list.size()==0){//没有书签时屏幕中间显示“没有书签”文字
			noBookmark.setVisibility(View.VISIBLE);
		}else{            //有书签时显示书签
			noBookmark.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			bookMarkAdapter=new BookMarkAdapter(getActivity(), list);
			listview.setAdapter(bookMarkAdapter);
		}		
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
					bookMarkAdapter.notifyDataSetChanged();
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
        this.getActivity().finish();
	}
	
}
