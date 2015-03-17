package com.jbl.browser.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jbl.browser.R;
import com.jbl.browser.adapter.ShareDialogAdapter;
import com.jbl.browser.interfaces.ShareInterface;
import com.jbl.browser.interfaces.deleteHistory;

public class ShareDialog extends Dialog{

	/**
	 * 分享界面自定义dialog
	 * @author zhangjian
	 * */
	private Context context;
	private GridView dlg_grid = null;
	private ShareDialogAdapter mAdapter;//分享界面适配器
	private ShareInterface shareInterface;//分享接口
	List<Integer> imv=new ArrayList<Integer>();
	List<String> tt=new ArrayList<String>();

	public ShareDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public void setInterface(ShareInterface i){
		this.shareInterface=i;
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
		mAdapter=new ShareDialogAdapter(getContext(), imv,tt);
		initData();
		dlg_grid.setAdapter(mAdapter);
		dlg_grid.setSelector(R.drawable.menu_selector);
		// 为GridView设置监听器
		dlg_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch(arg2){
				case 0:
					break;
				case 1:
					break;
				case 2:
					if(shareInterface!=null){
						shareInterface.shareMore();
					}
					break;
				}
			}
		});
	}
	//初始数据源，需要判断手机内应用程序，在进行改变
	public void initData(){
		imv.add(R.drawable.share_sina_icon);
		imv.add(R.drawable.share_tenqt_icon);
		imv.add(R.drawable.share_more_icon);
		tt.add(getContext().getString(R.string.sina));
		tt.add(getContext().getString(R.string.tengt));
		tt.add(getContext().getString(R.string.more));

	}
}
