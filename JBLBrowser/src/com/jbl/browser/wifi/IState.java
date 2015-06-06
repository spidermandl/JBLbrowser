package com.jbl.browser.wifi;

import java.util.Observable;

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
   

}  
