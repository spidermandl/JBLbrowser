package com.jbl.browser.adapter;

import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbl.browser.R;
import com.jbl.browser.utils.ImageInfo;
/**
 * 设置界面中每一个小项布局界面
 * @author Desmond
 *
 */
public class SettingGridItemAdapter extends BaseAdapter  
{  
	private List<ImageInfo> mList;// 定义一个list对象
	private Context mContext;// 上下文
	public static final int APP_PAGE_SIZE = 8;// 每一页装载数据的大小
	private PackageManager pm;// 定义一个PackageManager对象
	private int page;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文
	 * @param list
	 *            所有APP的集合
	 * @param page
	 *            当前页
	 */
	public SettingGridItemAdapter(Context context,  ArrayList<ImageInfo> list, int page) {
		mContext = context;
		pm = context.getPackageManager();
		this.page = page;
		mList = new ArrayList<ImageInfo>();
		// 根据当前页计算装载的应用，每页只装载16个
		int i = page * APP_PAGE_SIZE;// 当前页的其实位置
		int iEnd = i + APP_PAGE_SIZE;// 所有数据的结束位置
		while ((i < list.size()) && (i < iEnd)) {
			mList.add(list.get(i));
			i++;
		}
	}
	public int getCount() {
		return mList.size();
	}
	public Object getItem(int position) {
		return mList.get(position);
	}
	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.main_setting_item, parent, false);
		}
		
		ImageInfo appInfo = mList.get(position);
		ImageView appicon = (ImageView) convertView
				.findViewById(R.id.viewpage_test_icon);
		TextView appname = (TextView) convertView.findViewById(R.id.viewpage_test_text);
		appicon.setImageResource(appInfo.imgId);
		appname.setText(appInfo.imgMsg);
		return convertView;
	}

}
