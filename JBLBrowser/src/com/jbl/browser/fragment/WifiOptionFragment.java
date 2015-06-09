package com.jbl.browser.fragment;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
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
import com.jbl.browser.activity.WIFIService;
import com.jbl.browser.activity.WifiOptionActivity;
import com.jbl.browser.utils.UrlUtils;
import com.jbl.browser.wifi.AuthFailedState;
import com.jbl.browser.wifi.CMCCState;
import com.jbl.browser.wifi.HeartBeatState;
import com.jbl.browser.wifi.IState;
import com.jbl.browser.wifi.InitState;
import com.jbl.browser.wifi.MobileDataState;
import com.jbl.browser.wifi.NoCMCCState;
import com.jbl.browser.wifi.NormalWifiState;
import com.jbl.browser.wifi.OfflineState;

/**
 * wifi 连接界面
 * @author osondesmond
 *
 */
public class WifiOptionFragment extends SherlockFragment implements OnClickListener{

	ImageView connectView;
	Button insertingCoil;//下线
	TextView  moreFree;//获取更多免费时长
	String info;//对话框注释
	TextView infoTextView;
	AlertDialog.Builder infoDialog;
	
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
		infoTextView = new TextView(getActivity());
		infoDialog = new AlertDialog.Builder(getActivity()).setTitle("连接网络").setView(infoTextView);
		infoDialog.setCancelable(false);
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
	 * 检测联网进程
	 */
	Handler onLineChecker = new Handler(){
		public void handleMessage(android.os.Message msg) {

			IState state = ((WifiOptionActivity)WifiOptionFragment.this.getActivity()).getWifiStatus();
			if(state instanceof InitState){//初始状态
				info = "正在获取当前热点信息...";
			}else if(state instanceof MobileDataState){//数据网络状态
				info = "正在获取当前热点信息...";
			}else if(state instanceof NormalWifiState){//普通网络状态
				info = "正在获取当前热点信息...";
			}else if(state instanceof NoCMCCState){//no cmcc状态
				info = infoTextView.getText()+"\n没有检测到热点...";
			}else if(state instanceof CMCCState){//cmcc状态 未验证
				info = infoTextView.getText()+"\n正在验证...";
			}else if(state instanceof AuthFailedState){//cmcc验证失败
				info = infoTextView.getText()+"\n验证失败...";
			}else if(state instanceof HeartBeatState){//心跳状态
				insertingCoil.setVisibility(View.VISIBLE);
				info = infoTextView.getText()+"\n验证成功...";
				return;
			}
			else if(state instanceof OfflineState){//cmcc 下线
				
			}
			
			timeHandler.sendEmptyMessageDelayed(0,1000);
		};
	};
	
	
	
}
