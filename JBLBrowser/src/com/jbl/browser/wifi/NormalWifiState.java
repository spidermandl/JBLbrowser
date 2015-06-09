package com.jbl.browser.wifi;

import com.jbl.browser.activity.WIFIService;

/**
 * 普通wifi状态
 * @author Desmond
 *
 */
public class NormalWifiState extends BaseState {
	
	boolean isConnectingWifi=false;//是否正在连接wifi
	
	public NormalWifiState(WIFIService m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter() {

		service.startWifiConnection();//连接wifi
	}

	@Override
	public void excute() {
        if(!isConnectingWifi){
		    service.startCMCCWifi();
		    isConnectingWifi=true;
        }
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

     
  
}  
