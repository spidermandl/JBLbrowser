package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
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
import com.jbl.browser.utils.RecommendData;

@SuppressLint("NewApi")
public class RecommendCollectFragment extends Fragment implements OnItemLongClickListener,OnItemClickListener{
	ListView lv;
	RecommendAdapter tabAdapter;
	List<Integer> image=new ArrayList<Integer>();
	List<String> urlName=new ArrayList<String>();
	List<String> urlAddress=new ArrayList<String>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View tab1= inflater.inflate(R.layout.fragment_recommend_collect, container,false);
		lv=(ListView)tab1.findViewById(R.id.lv);
		initData();
		lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);
		return tab1;	
	}
	private void initData() {
		// TODO Auto-generated method stub
		getData();
		tabAdapter=new RecommendAdapter(getActivity(), image, urlName, urlAddress);
		lv.setAdapter(tabAdapter);
	}

	private void getData() {
		// TODO Auto-generated method stub
		image=RecommendData.image;
		urlAddress=RecommendData.urlAddress;
		urlName=RecommendData.urlName;
	}
	public void delete(int i){
		RecommendData.image.remove(i);
		RecommendData.urlName.remove(i);
		RecommendData.urlAddress.remove(i);
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
		builder.setTitle("删除推荐");
		builder.setMessage("是否要删除\""+urlName1+"\"这个推荐?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {		
						delete(position);					
				Toast.makeText(getActivity(), "删除成功", 100).show();
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
