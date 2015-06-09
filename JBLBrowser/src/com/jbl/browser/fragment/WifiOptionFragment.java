package com.jbl.browser.fragment;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.activity.WifiOptionActivity;
import com.jbl.browser.wifi.AuthFailedState;
import com.jbl.browser.wifi.CMCCState;
import com.jbl.browser.wifi.ExceptionState;
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

	View onlineLayout,offlineLayout;
	Button online;//连接
	Button offline;//下线
	TextView  moreFree;//获取更多免费时长
	String info="";//对话框注释
	AlertDialog infoDialog;
	TextView infoTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wifi, container, false);
		online=(Button)view.findViewById(R.id.connect_cmcc);
		offline=(Button)view.findViewById(R.id.logout);
		onlineLayout=view.findViewById(R.id.cmcc_online);
		offlineLayout=view.findViewById(R.id.cmcc_offline);
		moreFree=(TextView)view.findViewById(R.id.for_more);
		moreFree.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//获取更多免费时长加下划线
		online.setOnClickListener(this);
		offline.setOnClickListener(this);
		moreFree.setOnClickListener(this);
		
		timeHandler.sendEmptyMessage(0);
		infoTextView=new TextView(getActivity());
		infoDialog = new AlertDialog.Builder(getActivity()).setTitle("连接网络").setView(infoTextView).create();
		infoDialog.setCancelable(false);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.connect_cmcc:
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
			offlineLayout.setVisibility(View.GONE);
			onlineLayout.setVisibility(View.VISIBLE);
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

			infoDialog.show();
			IState state = ((WifiOptionActivity)WifiOptionFragment.this.getActivity()).getWifiStatus();
			
			if(state instanceof InitState){//初始状态
				info = "正在获取当前热点信息...";
				infoTextView.setText(info);
			}else if(state instanceof MobileDataState){//数据网络状态
				info = "正在获取当前热点信息...";
				infoTextView.setText(info);
			}else if(state instanceof NormalWifiState){//普通网络状态
				info = "正在获取当前热点信息...";
				infoTextView.setText(info);
			}else if(state instanceof NoCMCCState){//no cmcc状态
				info += "\n没有检测到热点...";
				infoTextView.setText(info);
			}else if(state instanceof CMCCState){//cmcc状态 未验证
				info += "\n正在验证...";
				infoTextView.setText(info);
			}else if(state instanceof AuthFailedState){//cmcc验证失败
				info += "\n验证失败...";
				infoTextView.setText(info);
			}else if(state instanceof HeartBeatState){//心跳状态
				offlineLayout.setVisibility(View.VISIBLE);
				onlineLayout.setVisibility(View.GONE);
				info += "\n验证成功...";
				infoTextView.setText(info);
				infoDialog.dismiss();
				return;
			}else if(state instanceof OfflineState){//cmcc 下线
				info="正在下线";
				infoTextView.setText(info);
			}else if(state instanceof ExceptionState){//异常
				((WifiOptionActivity)WifiOptionFragment.this.getActivity()).changeState(InitState.class);
				infoDialog.dismiss();
				return;
			}
			
			onLineChecker.sendEmptyMessageDelayed(0,1000);
		};
	};
	
	
	
}
