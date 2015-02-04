package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.jbl.browser.R;
import com.jbl.browser.adapter.RecommendMainAdapter;

public class RecommendMainFragment extends Fragment {
	private ListView lv;
	private RecommendMainAdapter mAdapter;
	private List <String> listUrlName=new ArrayList<String>();
	private List <String> listUrlAddress=new ArrayList<String>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View recommendMain= inflater.inflate(R.layout.fragment_recomment_main, container,false);
		lv=(ListView)recommendMain.findViewById(R.id.lv);
		initUrlNameData();
		initUrlAddressData();
		/*lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);*/
		return recommendMain;	
	}
	private void initUrlAddressData() {
		// TODO Auto-generated method stub
		listUrlAddress.add("http://www.baidu.com");
	}
	private void initUrlNameData() {
		// TODO Auto-generated method stub
		listUrlName.add("百度");
	}
	 public int imageids []={
			 R.drawable.abs__ic_ab_back_holo_light
	 };
	/*@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		 Dialog dialog = new AlertDialog.Builder(getActivity())
         .setTitle("删除")
         .setMessage("确定要删除吗？")
         .setPositiveButton("确定",
                         new DialogInterface.OnClickListener()
                         {

                                 @Override
                                 public void onClick(DialogInterface dialog,
                                                 int which)
                                 {
                                         lv.remove(mAdapter.getItem(position));
                                         mAdapter.notifyDataSetChanged();
                                 }
                         }).setNegativeButton("取消", null).create();
		 dialog.show();
		 return true;
}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}*/
}
