package com.jbl.browser.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jbl.broswer.bean.BookMark;
import com.jbl.browser.R;
import com.jbl.browser.adapter.BookMarkAdapter.ViewHolder;
import com.jbl.browser.fragment.BookMarkFragment;
import com.jbl.browser.fragment.MenuSetFragment;
/*
 * 菜单设置选项的适配器
 */
public class MenuSetAdapter extends BaseAdapter{
	List<String> list=new ArrayList<String>();
	 private LayoutInflater mInflater; 
	 private MenuSetFragment fragment;
	 private Context mContext;  
	 public MenuSetAdapter(MenuSetFragment menuSetFragment, List<String> list) {  
	        this.fragment=menuSetFragment;  
	        mInflater = LayoutInflater.from(menuSetFragment.getActivity());  
	        list=list;  
	    } 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list==null?0:list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;  
       if (convertView == null) {  
           convertView = mInflater.inflate(R.layout.menusetitem, null);  
           holder = new ViewHolder();  
           holder.textcontent = (TextView) convertView.findViewById(R.id.setcontent);   
           convertView.setTag(holder);  
       } else {  
           holder = (ViewHolder) convertView.getTag();
       }
       holder.textcontent.setText(list.get(position));
		return convertView;
	}
	public class ViewHolder  
   {  
       public TextView textcontent;  
   }  
}
