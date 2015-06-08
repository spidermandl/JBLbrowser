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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excute() {

		service.startMoblieData();
		
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dead(String info) {
		service.openWifiSetting();
		super.dead(info);
	}
      
  
  
}  
