package com.jbl.browser.wifi;


/**
 * wifi 状态机
 * @author Desmond
 *   
 *                                                                                                                            |-----------|           
 *   InitState------------->MoblieDataState-------------->NormalWifiState---------->CMCCState------>FreeWifiState----->HeartBearState------
 *                                                               |                       |                                   |
 *                                                              \|/                      |                                   |
 *                                                               |                      \|/                                 \|/   
 *                                                        NoCMCCState                    |                                   |
 *                                                                                 AuthFiledState                       OfflineState
 */
public class WifiMachine{  
    private IState state; //机器的当前状态  

    
    public WifiMachine() {  
        
    }  
  
    public IState getState() {  
        return state;  
    }  

  
    public void setState(IState state) {  
        this.state = state;  
    }
    
    /**
     * 切换状态机状态
     * @param state
     */
    public void changeState(IState state){
    	if(this.state!=null)
    		this.state.exit();
    	state.enter();
    	this.state=state;
    	this.state.excute();
    }

    /**
     * 运行当前状态
     */
    public void runState(){
    	if(state!=null){
    		state.excute();
    	}
    }
}  
