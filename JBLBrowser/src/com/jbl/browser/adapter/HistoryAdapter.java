package com.jbl.browser.adapter;
/**
 * 浏览记录适配器
 */
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jbl.browser.R;
import com.jbl.browser.bean.History;
import com.jbl.browser.fragment.HistoryFragment;

public class HistoryAdapter extends BaseAdapter{
	List<History> list_history=new ArrayList<History>();
	 private LayoutInflater mInflater; 
	 private ListView listView;
	 private Context mContext;
	 public HistoryAdapter(Context context, List<History> list,ListView listView) {  
	       mContext=context;
	        mInflater = LayoutInflater.from(context);  
	        list_history= list;  
	        this.listView=listView;
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
    public boolean hasStableIds() {
          // TODO Auto-generated method stub
          return true ;
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
            holder.image=(ImageView)convertView.findViewById(R.id.iv_url_image);
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();
        }
        holder.urlName.setText(list_history.get(position).getWebName());
        holder.urlAddress.setText(list_history.get(position).getWebAddress());
        updateBackground(position, convertView);
		return convertView;
	}
	public class ViewHolder  
    {  
        public View line; 
        public TextView urlName,urlAddress;  
        public ImageView image;
    } 
	@SuppressLint({ "NewApi", "ResourceAsColor" })
	public void updateBackground(int position, View view) {
		int backgroundId=0;
		if (listView.isItemChecked(position)) {
			backgroundId = R.drawable.list_selected_holo_light;
			Drawable background = mContext.getResources().getDrawable(backgroundId);
			view.setBackgroundDrawable(background);
		}else{
			view.setBackgroundDrawable(null);
		}
		
		
	}
}
