package com.jbl.browser.wifi;

/** 
 * 机器处于有硬币，有糖果，没有摇动的状态 
 * @author seacean 
 * @date 2013-8-29 
 */  
public class HasQuarterState implements IState {  
    private Machine machine;  
      
    public HasQuarterState(Machine machine){  
        this.machine=machine;  
    }  
    @Override  
    public void insertQuarter() {  
        System.out.println("You can not insert another quarter!");  
    }  
  
    @Override  
    public void ejectQuarter() {  
        System.out.println("Quarter returned!");  
        machine.setState(machine.getNoQuarterState());  
    }  
  
    @Override  
    public void turnCrank() {  
        System.out.println("You turned ... ");  
        machine.setState(machine.getSoldState());  
    }  
  
    @Override  
    public void dispense() {  
        System.out.println("No gumball dispensed!");  
    }  
  
}  
