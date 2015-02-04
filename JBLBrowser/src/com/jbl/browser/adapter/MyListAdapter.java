package com.jbl.browser.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbl.browser.R;

public class MyListAdapter extends BaseAdapter  
{  
    private LayoutInflater mInflater;  
    private List<String> mList;  
    private Context mContext;  
    public MyListAdapter(Context context, List<String> list) {  
        mContext = context;  
        mInflater = LayoutInflater.from(mContext);  
        mList = list;  
    }  
    @Override  
    public int getCount()  
    {  
        return mList.size();  
    }  
    @Override  
    public Object getItem(int position)  
    {  
        return mList.get(position);  
    }  
    @Override  
    public long getItemId(int position)  
    {  
        return position;  
    }  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent)  
    {  
        ViewHolder holder;  
        if (convertView == null) {  
            convertView = mInflater.inflate(R.layout.gridview_item, null);  
            holder = new ViewHolder();  
            holder.icon = (ImageView) convertView.findViewById(R.id.viewpage_test_icon);  
            holder.text = (TextView) convertView.findViewById(R.id.viewpage_test_text);  
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();// ȡ��ViewHolder����  
        }  
        holder.text.setText(mList.get(position));  
        return convertView;  
    }  
      
    public final class ViewHolder  
    {  
        public ImageView icon;  
        public TextView text;  
  
    }  
}  