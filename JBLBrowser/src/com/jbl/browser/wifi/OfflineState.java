package com.jbl.browser.wifi;

import com.jbl.browser.activity.WIFIService;

/**
 * 下线状态
 * @author Desmond
 *
 */
public class OfflineState extends BaseState {

	public OfflineState(WIFIService m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void excute() {
		service.cmccLogout();

	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

}
