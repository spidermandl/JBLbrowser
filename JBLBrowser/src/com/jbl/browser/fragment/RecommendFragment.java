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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.adapter.RecommendAdapter;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.StringUtils;


/**
 * 推荐主页面
 * zhangjian
 */

public class RecommendFragment extends SherlockFragment implements OnItemLongClickListener, OnItemClickListener{
	
	public static final String TAG="RecommendMainFragment";
	
	ListView lv;
	RecommendAdapter tabAdapter;
	//读取推荐页面记录数据
	List<BookMark> list=null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View tab1= inflater.inflate(R.layout.fragment_recommend, container,false);
		lv=(ListView)tab1.findViewById(R.id.lv);
		initData();  //添加数据
		lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);
		return tab1;	
	}
	/* 添加数据 */
	private void initData() {
		list=new BookMarkDao(getActivity()).queryBookMarkAllByisRecommend(true);
		tabAdapter=new RecommendAdapter(getActivity(),list);
		lv.setAdapter(tabAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String webAddress=((TextView)view.findViewById(R.id.mSearch_tv2)).getText().toString();
		JBLPreference.getInstance(getActivity()).writeString(JBLPreference.RECOMMEND_KEY, webAddress);
		((BaseFragActivity)getActivity()).navigateTo(MainPageFragment.class, null, false,MainPageFragment.TAG);
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		// TODO Auto-generated method stub	
		final String urlName1;
		urlName1=((TextView)view.findViewById(R.id.mSearch_tv1)).getText().toString();
		AlertDialog.Builder builder=new Builder(getActivity());
		builder.setTitle(StringUtils.DELETE_RECOMMEND);
		builder.setMessage("是否要删除\""+urlName1+"\"这个推荐?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {					
				Toast.makeText(getActivity(), StringUtils.SUCCESS_DELETE, 100).show();
				initData();
				lv.invalidate();				
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
