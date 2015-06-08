package com.jbl.browser.wifi;

import com.jbl.browser.activity.WIFIService;

public abstract class BaseState implements IState {

	protected WIFIService service;
	protected String errorInfo;
	
	public BaseState(WIFIService m){
		this.service=m;
	}

	@Override
	public void dead(String info){
		errorInfo=info;
	}
	
	@Override
	public String getError(){
		return errorInfo;
	}
}
