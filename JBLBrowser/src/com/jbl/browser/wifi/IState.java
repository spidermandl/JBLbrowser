package com.jbl.browser.wifi;

import java.util.Observable;

public interface IState {  
//    /** 
//     * 连接3g／4g网络 
//     */  
//    void toMoblieData();  
//    /** 
//     * 连接指定wifi
//     */  
//    void toSpecificWifi();  
    /** 
     * 投入硬币 
     */  
    void insertQuarter();  
    /** 
     * 根据摇动情况，处理摇动结果，返回处理结果，释放糖果 
     */  
    void ejectQuarter();  
    /** 
     * 转动摇柄 
     */  
    void turnCrank();  
    /** 
     * 机器放出糖果，处理机器内部状态，返回初始可投币状态 
     */  
    void dispense();  
}  
