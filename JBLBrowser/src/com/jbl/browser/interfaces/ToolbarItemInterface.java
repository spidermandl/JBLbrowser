package com.jbl.browser.interfaces;

/**
 * 底部按钮功能接口
 * @author yyjoy-mac3
 *
 */
public interface ToolbarItemInterface {

	/**
	 * 返回上一页web
	 */
	void goBack();
	/**
	 * 返回下一页web
	 */
	void goForward();
	/**
	 * 返回主页
	 */
	void goHome();
	/**
	 * 菜单栏显示
	 */
	void goMenu();
	/**
	 * 多页按钮
	 */
	void goMultiWindow();
	
	/**
	 * wifi按钮
	 */
	void goWifi();
	
}
