package com.jbl.browser.wifi;

import com.jbl.browser.activity.WIFIService;

/**
 * 普通wifi状态
 * @author Desmond
 *
 */
public class NormalWifiState extends BaseState {
	
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
		service.startWifiConnection();
		
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

     
  
}  
