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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.jbl.browser.R;
import com.jbl.browser.adapter.MenuSetAdapter;
/*
 * 菜单设置选项fragment
 */
public class MenuSetFragment extends SherlockFragment implements OnClickListener{
	public final static String TAG=" MenuSetFragment";
	//菜单栏设置listView
	ListView listview;
	//设置内容
	List<String> list=new ArrayList<String>();
	public AlertDialog dialog;
	MenuSetAdapter menuSetAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		listview.setAdapter(menuSetAdapter);
		super.onCreate(savedInstanceState);
	}
	/**
	 * 测试数据
	 */
	public void init(){
		String name1="字体大小";
		String name2="屏幕亮度";
		String name3="屏幕翻转";
		list.add(name1);
		list.add(name2);
		list.add(name3);
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
		View view = inflater.inflate(R.layout.menuset_fragment, container, false);
		menuSetAdapter=new MenuSetAdapter(MenuSetFragment.this, list);
		listview=(ListView)view.findViewById(R.id.list_view_set);
		listview.setOnClickListener(this);
		return view;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 0:
			AlertDialog.Builder builder1=new Builder(getActivity());
			builder1.setTitle("字体大小");
			final String[] items=new String[]{"小","中","大"};
			builder1.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), items[which], 1).show();
				}
			});
			builder1.setPositiveButton("取消",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			
			builder1.create().show();
			dialog.dismiss();
			break;
		case 1:
			AlertDialog.Builder builder2=new Builder(getActivity());
			builder2.setTitle("屏幕亮度");
			builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder2.setNeutralButton("取消",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			
			builder2.create().show();
			dialog.dismiss();
			break;
		case 3:
			AlertDialog.Builder builder3=new Builder(getActivity());
			builder3.setTitle("旋转屏幕");
			final String[] items1=new String[]{"跟随系统","锁定竖屏","锁定横屏"};
			builder3.setSingleChoiceItems(items1, 1, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), items1[which], 1).show();
				}
			});
			builder3.setPositiveButton("取消",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder3.create().show();
			dialog.dismiss();
			break;
		default:
			break;
		}
		
	}
}
