package com.jbl.browser.wifi;

import com.jbl.browser.activity.WIFIService;

/**
 * cmcc 验证通过，发心跳包状态
 * @author Desmond
 *
 */
public class HeartBeatState extends BaseState {

	public HeartBeatState(WIFIService m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter() {
		service.startHeartBeatSync("start");

	}

	@Override
	public void excute() {
		service.startHeartBeatSync("heartbeat");
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

}
