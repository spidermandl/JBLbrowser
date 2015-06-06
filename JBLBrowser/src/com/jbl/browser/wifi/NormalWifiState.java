package com.jbl.browser.wifi;

import com.jbl.browser.activity.WIFIService;

/**
 * 普通wifi状态
 * @author Desmond
 *
 */
public class NormalWifiState extends BaseState {

	/**
	 * 免费网络检测阶段
	 */
	public static enum AuthStage{
		ISCMCC(0),//检测是否有cmcc
		DATASTREAM(3),//数据流量
		CONNECTED(4), //cmcc连接上
		FAILED(5),//CMCC验证失败
	    AUTHORITHED(6);//CMCC验证通过
		// 定义私有变量
		private int nCode;

		// 构造函数，枚举类型只能为私有
		private AuthStage(int _nCode) {
			this.nCode = _nCode;
		}

		@Override
		public String toString() {
			return String.valueOf("wifi_status_type_"+this.nCode);
		}
	};
	
	public NormalWifiState(WIFIService m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excute() {
		service.startCMCCWifi();
		
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

     
  
}  
