package com.jbl.browser.adapter;
/**
 * 浏览记录适配器
 */
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jbl.broswer.bean.History;
import com.jbl.browser.R;
import com.jbl.browser.fragment.HistoryFragment;

public class HistoryAdapter extends BaseAdapter{
	List<History> list_history=new ArrayList<History>();
	 private LayoutInflater mInflater; 
	 private Activity activity;  
	 public HistoryAdapter(Activity activity, List<History> list) {  
	        this.activity=activity;  
	        mInflater = LayoutInflater.from(activity);  
	        list_history= list;  
	    } 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_history==null?0:list_history.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_history.get(position);
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
            convertView = mInflater.inflate(R.layout.history_item, null);  
            holder = new ViewHolder();  
            holder.line = (View) convertView.findViewById(R.id.line);  
            holder.urlName = (TextView) convertView.findViewById(R.id.url_name);  
            holder.urlAddress = (TextView) convertView.findViewById(R.id.url_address);  
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();
        }
        holder.urlName.setText(list_history.get(position).getWebName());
        holder.urlAddress.setText(list_history.get(position).getWebAddress());
		return null;
	}
	public class ViewHolder  
    {  
        public View line; 
        public TextView urlName,urlAddress;  
  
    } 
}
