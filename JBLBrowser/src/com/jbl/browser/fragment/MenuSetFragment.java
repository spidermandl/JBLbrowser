package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.jbl.browser.BrowserSettings;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.adapter.MenuSetAdapter;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.bean.SetContent;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.utils.BrightnessSettings;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.StringUtils;
/*
 * 菜单设置选项fragment
 */
public class MenuSetFragment extends SherlockFragment implements OnItemClickListener{
	public final static String TAG="MenuSetFragment";
	//菜单设置选项内容
	ListView listview;
	//设置数据
	List<SetContent> list=new ArrayList<SetContent>();
	MenuSetAdapter menuSetAdapter;
	SetContent s1,s2,s3;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	/**
	 *添加数据
	 */
	public void init(){
		SetContent s1=new SetContent();
		s1.setSetText("字体大小");
		s1.setTextSize(JBLPreference.getInstance(getActivity()).readString(StringUtils.FONT_MAX));
		list.add(s1);
		SetContent s2=new SetContent();
		s2.setSetText("屏幕亮度");
		s2.setTextSize("适中");
		list.add(s2);
		SetContent s3=new SetContent();
		s3.setSetText("旋转屏幕");
		s3.setTextSize("锁定竖屏");
		list.add(s3);
		SetContent s4=new SetContent();
		s4.setSetText("关于");
		list.add(s4);
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
		listview=(ListView)view.findViewById(R.id.list_view_set);
		init();
		menuSetAdapter=new MenuSetAdapter(getActivity(), list);
		listview.setAdapter(menuSetAdapter);
		listview.setOnItemClickListener(this);
		return view;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			AlertDialog.Builder builder1=new Builder(getActivity());
			builder1.setTitle("字体大小");
			final String[] items=new String[]{"小","中","大"};
			builder1.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), "您选择的字体为:"+items[which], 1).show();
					switch (which) {
					case 0:
						String fontSize1="小";
						JBLPreference.getInstance(getActivity()).writeString(StringUtils.FONT_MAX, fontSize1);
						((BaseFragActivity)getActivity()).navigateTo(MainPageFragment.class, null, false,MainPageFragment.TAG);
						break;
					case 1:
						String fontSize2="中";
						JBLPreference.getInstance(getActivity()).writeString(StringUtils.FONT_MAX, fontSize2);
						((BaseFragActivity)getActivity()).navigateTo(MainPageFragment.class, null, false,MainPageFragment.TAG);
						break;
					case 2:	
						String fontSize3="大";
						JBLPreference.getInstance(getActivity()).writeString(StringUtils.FONT_MAX, fontSize3);
						((BaseFragActivity)getActivity()).navigateTo(MainPageFragment.class, null, false,MainPageFragment.TAG);
						break;
					default:
						break;
					}
				}
			});
			builder1.setNegativeButton("取消",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder1.create().show();
			break;
		case 1 :
			BrightnessSettings bs=new BrightnessSettings();
			bs.showBrightnessSettingsDialog(getActivity());
			break;
		case 2:
			AlertDialog.Builder builder3=new Builder(getActivity());
			builder3.setTitle("旋转屏幕");
			final String[] items1=new String[]{"跟随系统","锁定竖屏","锁定横屏"};
			builder3.setSingleChoiceItems(items1, 1, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), "您选择了:"+items1[which], 1).show();
				}
			});
			builder3.setPositiveButton("取消",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder3.create().show();
			break;
		case 3:  //关于浏览器信息
			AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
			dialog.show();
			Window window = dialog.getWindow();
			window.setContentView(R.layout.activity_about);
			break;
		default:
			break;
		}

	}
}
