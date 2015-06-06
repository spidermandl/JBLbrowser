package com.jbl.browser.wifi;

import com.jbl.browser.activity.WIFIService;

/**
 * 连接到cmcc wifi，未进行验证
 * @author Desmond
 *
 */
public class CMCCState extends BaseState {

	public CMCCState(WIFIService m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void excute() {
		service.sendCMCCAuth();

	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

}
