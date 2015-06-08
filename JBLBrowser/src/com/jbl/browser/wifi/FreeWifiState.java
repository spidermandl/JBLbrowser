package com.jbl.browser.wifi;

import com.jbl.browser.activity.WIFIService;

/**
 * 验证通过wifi（cmcc edu）状态  
 * @author Desmond
 *
 */
public class FreeWifiState extends BaseState{

	public FreeWifiState(WIFIService m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excute() {
		service.startHeartBeatSync("start");
		
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}
     
  
} 