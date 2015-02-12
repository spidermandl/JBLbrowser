package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
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

import com.jbl.browser.R;
import com.jbl.browser.adapter.RecommendAdapter;
import com.jbl.browser.utils.RecommendCollectData;
import com.jbl.browser.utils.StringUtils;


/*
 * 收藏界面
 * 
 * */
public class RecommendCollectFragment extends Fragment implements OnItemLongClickListener,OnItemClickListener{
	ListView lv;
	RecommendAdapter tabAdapter;
	List<Integer> image=new ArrayList<Integer>(); //网站图标
	List<String> urlName=new ArrayList<String>(); //网站名字
	List<String> urlAddress=new ArrayList<String>(); //网站网址
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View tab1= inflater.inflate(R.layout.fragment_recommend_collect, container,false);
		lv=(ListView)tab1.findViewById(R.id.lv);
		initData();   //listview链接数据
		lv.setOnItemClickListener(this);  //点击监听
		lv.setOnItemLongClickListener(this);  //长按监听实现删除
		return tab1;	
	}
	/*  添加适配器  */
	private void initData() {
		// TODO Auto-generated method stub
		getData();
		tabAdapter=new RecommendAdapter(getActivity(), image, urlName, urlAddress);
		lv.setAdapter(tabAdapter);
	}
 /* 获取数据资源 */
	private void getData() {
		// TODO Auto-generated method stub
		image=RecommendCollectData.image;
		urlAddress=RecommendCollectData.urlAddress;
		urlName=RecommendCollectData.urlName;
	}
	/* 删除相应数据 */
	public void delete(int i){
		RecommendCollectData.image.remove(i);
		RecommendCollectData.urlName.remove(i);
		RecommendCollectData.urlAddress.remove(i);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		// TODO Auto-generated method stub	
		final String urlName1;
		urlName1=((TextView)view.findViewById(R.id.tv1)).getText().toString();
		AlertDialog.Builder builder=new Builder(getActivity());
		builder.setTitle(StringUtils.DELETE_RECOMMEND);
		builder.setMessage("是否要删除\""+urlName1+"\"这个推荐?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {		
						delete(position);					
				Toast.makeText(getActivity(), StringUtils.SUCCESS_DELETE, 100).show();
				initData();
				lv.invalidate();//重新加载数据，实现刷新
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
