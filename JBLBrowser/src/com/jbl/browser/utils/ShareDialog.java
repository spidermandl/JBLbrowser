package com.jbl.browser.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jbl.browser.R;
import com.jbl.browser.adapter.ShareDialogAdapter;

public class ShareDialog extends Dialog{

	/**
	 * 分享界面自定义dialog
	 * @author zhangjian
	 * */
	private Context context;
	private GridView dlg_grid = null;
	private ShareDialogAdapter mAdapter;//分享界面适配器
	List<Integer> imv=new ArrayList<Integer>();


	public ShareDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ShareDialog(Context context, List<Integer> imv) {
		super(context);
		context = context;
		this.imv = imv;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置对话框使用的布局文件
		this.setContentView(R.layout.share_gridview);

		dlg_grid = (GridView) findViewById(R.id.gridview);

		// 设置GridView的数据源
		mAdapter=new ShareDialogAdapter(getContext(), imv);
		initData();
		dlg_grid.setAdapter(mAdapter);
		// 为GridView设置监听器
		dlg_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			}
		});
	}
	//初始数据源，需要判断手机内应用程序，在进行改变
	public void initData(){
		imv.add(R.drawable.ic_launcher);
		imv.add(R.drawable.ic_launcher);
		imv.add(R.drawable.ic_launcher);
		imv.add(R.drawable.ic_launcher);
	}
}
