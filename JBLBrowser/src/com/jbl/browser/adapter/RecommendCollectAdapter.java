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
import com.jbl.browser.adapter.RecommendCustomAdapter.ViewHolder;
import com.jbl.browser.fragment.RecommendMainFragment;

public class RecommendCollectAdapter extends BaseAdapter{
	RecommendMainFragment rmf;
	private LayoutInflater layoutInflater;
	private Context context;
	private List <String> listUrlName=new ArrayList<String>();
	private List <String> listUrlAddress=new ArrayList<String>();
	public RecommendCollectAdapter(Context context, List<String> listUrlNmae,
			List<String> listUrlAddress) {
		super();
		this.layoutInflater=layoutInflater.from(context);
		this.context = context;
		this.listUrlName = listUrlNmae;
		this.listUrlAddress = listUrlAddress;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listUrlName==null?0:listUrlName.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listUrlName.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=layoutInflater.inflate(R.layout.fragment_recommend_all_item,null);
			holder.imv=(ImageView)convertView.findViewById(R.id.imv);
			holder.urlName=(TextView)convertView.findViewById(R.id.tv1);
			holder.urlAddress=(TextView)convertView.findViewById(R.id.tv2);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		holder.urlName.setText(listUrlName.get(position));
		holder.urlAddress.setText(listUrlAddress.get(position));
		return convertView;
	}
	class ViewHolder{
		private ImageView imv;
		private TextView urlName;
		private TextView urlAddress;
	}

}
