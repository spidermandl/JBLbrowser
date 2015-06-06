package com.jbl.browser.wifi;

import com.jbl.browser.activity.WIFIService;

public abstract class BaseState implements IState {

	protected WIFIService service;
	
	public BaseState(WIFIService m){
		this.service=m;
	}

}
