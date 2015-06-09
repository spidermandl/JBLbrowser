package com.jbl.browser.wifi;

import com.jbl.browser.activity.WIFIService;

/** 
 * 初始状态 
 */  
public class InitState extends BaseState {  

    
	public InitState(WIFIService m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter() {
		service.startMoblieData();
		
	}

	@Override
	public void excute() {
		service.statusLooping(500);
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
