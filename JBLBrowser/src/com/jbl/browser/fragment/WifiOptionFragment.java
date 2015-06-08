package com.jbl.browser.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.activity.WifiOptionActivity;
import com.jbl.browser.wifi.FreeWifiState;
import com.jbl.browser.wifi.IState;
import com.jbl.browser.wifi.InitState;
import com.jbl.browser.wifi.MobileDataState;

/**
 * wifi
 * @author osondesmond
 *
 */
public class WifiOptionFragment extends SherlockFragment implements OnClickListener{

	ImageView connectView;
	Button insertingCoil;//下线
	TextView  moreFree;//获取更多免费时长
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wifi, container, false);
		connectView=(ImageView)view.findViewById(R.id.wifi);
		insertingCoil=(Button)view.findViewById(R.id.logout);
		moreFree=(TextView)view.findViewById(R.id.for_more);
		moreFree.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//获取更多免费时长加下划线
		connectView.setOnClickListener(this);
		insertingCoil.setOnClickListener(this);
		moreFree.setOnClickListener(this);
		
		timeHandler.sendEmptyMessage(0);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wifi:
			onLineChecker.sendEmptyMessage(0);
			IState state = ((WifiOptionActivity)this.getActivity()).getWifiStatus();
			if(state instanceof InitState){//初始状态
				if(!state.invalid())
			         ((WifiOptionActivity)this.getActivity()).startConnection();
				else{
					((WifiOptionActivity)this.getActivity()).changeState(MobileDataState.class);
				}
			}else if(state instanceof MobileDataState){
				
			}
			break;
		case R.id.logout:
			((WifiOptionActivity)this.getActivity()).stopConnection();
			v.setVisibility(View.GONE);
		case R.id.for_more:
		default:
			break;
		}
	}
	/**
	 * 计时器
	 */
	Handler timeHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//long time = ((WifiOptionActivity)WifiOptionFragment.this.getActivity()).getOnlineTime();
			/**
			 * 更新时间界面
			 */
			
			/*******************/
			timeHandler.sendEmptyMessageDelayed(0,1000);
		};
	};
	
	/**
	 * 联网检测
	 */
	Handler onLineChecker = new Handler(){
		public void handleMessage(android.os.Message msg) {

			IState state = ((WifiOptionActivity)WifiOptionFragment.this.getActivity()).getWifiStatus();
			if(state instanceof FreeWifiState){
				insertingCoil.setVisibility(View.VISIBLE);
			}else{
				timeHandler.sendEmptyMessageDelayed(0,1000);
			}
		};
	};
}
