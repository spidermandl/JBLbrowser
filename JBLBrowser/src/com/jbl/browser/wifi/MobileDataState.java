package com.jbl.browser.wifi;

import com.jbl.browser.activity.WIFIService;

/**
 * 连接移动数据状态
 * @author Desmond
 *
 */
public class MobileDataState extends BaseState {

	public MobileDataState(WIFIService m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter() {
		service.requestAccount();
	}

	@Override
	public void excute() {
		
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dead(String info) {
		service.startWifiConnection();
		super.dead(info);
	}


  
}  