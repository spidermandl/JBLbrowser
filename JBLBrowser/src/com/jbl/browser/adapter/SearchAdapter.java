package com.jbl.browser.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbl.browser.R;

public class SearchAdapter extends BaseAdapter{

	private LayoutInflater layoutInflater=null;
	private Context context;
	List<Integer> image=new ArrayList<Integer>();
	List<String> urlName=new ArrayList<String>();
	List<String> urlAddress=new ArrayList<String>();
	public SearchAdapter(Context context, List<Integer> image,
			List<String> urlName, List<String> urlAddress) {
		super();
		this.layoutInflater=layoutInflater.from(context);
		this.context = context;
		this.image = image;
		this.urlName = urlName;
		this.urlAddress = urlAddress;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return urlAddress==null?0:urlAddress.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return urlAddress.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=layoutInflater.inflate(R.layout.fragment_recommend_all_item,null);
			holder.imv=(ImageView)convertView.findViewById(R.id.mSearch_imv);
			holder.tv1=(TextView)convertView.findViewById(R.id.mSearch_tv1);
			holder.tv2=(TextView)convertView.findViewById(R.id.mSearch_tv2);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.imv.setBackgroundResource(image.get(position));
		holder.tv1.setText(urlName.get(position));
		holder.tv2.setText(urlAddress.get(position));
		return convertView;
	}
class ViewHolder{
	private ImageView imv;
	private TextView tv1;
	private TextView tv2;
   }
	
}

