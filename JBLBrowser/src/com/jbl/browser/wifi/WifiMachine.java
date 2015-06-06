package com.jbl.browser.wifi;


/**
 * wifi 状态机
 * @author Desmond
 *   
 *       
 *   InitState------------->MoblieDataState-------------->NormalWifiState---------->CMCCState------>FreeWifiState
 *                                                               |                       |
 *                                                              \|/                      |
 *                                                               |                      \|/
 *                                                        NoCMCCState                    |
 *                                                                                 AuthFiledState     
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
    
    public void changeState(IState state){
    	if(this.state!=null)
    		this.state.exit();
    	state.enter();
    	this.state=state;
    	this.state.excute();
    }

}  
