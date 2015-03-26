package com.jbl.browser.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbl.browser.R;
import com.jbl.browser.fragment.SettingPagerFragment.SettingMenuType;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.StringUtils;
/**
 * 设置界面中每一个小项布局界面
 * @author Desmond
 *
 */
public class SettingGridItemAdapter extends BaseAdapter  
{  
	private List<SettingMenuType> mList;// 定义一个list对象
	private Context mContext;// 上下文
	public static final int APP_PAGE_SIZE = 8;// 每一页装载数据的大小
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
	public SettingGridItemAdapter(Context context,  ArrayList<SettingMenuType> list, int page) {
		mContext = context;
		this.page = page;
		mList = new ArrayList<SettingMenuType>();
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
		
		SettingMenuType appInfo = mList.get(position);
		ImageView appicon = (ImageView) convertView
				.findViewById(R.id.viewpage_test_icon);
		TextView appname = (TextView) convertView.findViewById(R.id.viewpage_test_text);
		appicon.setImageResource(appInfo.getDrawable());
		appname.setText(appInfo.getName());
		if(appname.getText().equals(StringUtils.ADD_BOOKMARK)){      
			if(JBLPreference.getInstance(mContext).readInt(JBLPreference.HOST_URL_BOOLEAN)==JBLPreference.IS_HOST_URL){
				appname.setTextColor(Color.GRAY);
				convertView.setFocusable(false);
			}
		}
		if(appname.getText().equals(StringUtils.TURNING_BUTTON_OPEN)){
			if(JBLPreference.getInstance(mContext).readInt(JBLPreference.HOST_URL_BOOLEAN)==JBLPreference.IS_HOST_URL){
				appname.setTextColor(Color.GRAY);
				convertView.setFocusable(false);
			}
		}
		if(appname.getText().equals(StringUtils.TURNING_BUTTON_CLOSE)){
			if(JBLPreference.getInstance(mContext).readInt(JBLPreference.HOST_URL_BOOLEAN)==JBLPreference.IS_HOST_URL){
				appname.setTextColor(Color.GRAY);
				convertView.setFocusable(false);
				convertView.setSelected(false);
			}
		}
		return convertView;
	}

}
