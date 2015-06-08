package com.jbl.browser.wifi;

public interface IState {  
	/**
	 * 进入状态
	 */
	void enter();
	/**
	 * 执行状态
	 */
    void excute(); 
    
    /**
     * 退出状态
     */
    void exit();
    
    /**
     * 状态进入异常
     * @param info
     */
    void dead(String info);
   
    /**
     * 获取异常信息
     * 状态转成无效
     * @return
     */
    String getError();

}  
