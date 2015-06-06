package com.jbl.browser.wifi;

/** 
 * 机器处于没有投硬币的状态 
 * @author seacean 
 * @date 2013-8-29 
 */  
public class NoQuarterState implements IState {  
    private Machine machine;  
  
    public NoQuarterState(Machine machine) {  
        this.machine = machine;  
    }  
  
    @Override  
    public void insertQuarter() {  
        System.out.println("please insert a quarter!");  
        machine.setState(machine.getHasQuarterState());  
    }  
  
    @Override  
    public void ejectQuarter() {  
        System.out.println("please insert a quarter!");  
    }  
  
    @Override  
    public void turnCrank() {  
        System.out.println("please insert a quarter!");  
    }  
  
    @Override  
    public void dispense() {  
        System.out.println("please insert a quarter!");  
    }  
  
}  