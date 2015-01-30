package com.jbl.browser.model;

@SuppressWarnings("serial")
public class ErrorInfo extends Exception{

	private String mMessage;
    private int mErrorCode = -1;
    private Exception mException;
    
    /**
     * 仅仅包含错误信息的ErrorInfo
     * 
     * @param message 错误信息
     */
    public ErrorInfo(String message) {
        this.mMessage = message;
    }

    /**
     * 包含错误信息和错误代码的ErrorInfo
     * 
     * @param message 错误信息
     * @param errorCode 错误代码
     */
    public ErrorInfo(String message, int errorCode) {
        this.mMessage = message;
        this.mErrorCode = errorCode;
    }

    /**
     * 直接包含Exception信息的ErrorInfo
     * 
     * @param e Exception
     */
    public ErrorInfo(Exception e) {
        if(e != null){
        	if(e.getClass().getName().equals(this.getClass().getName())){
        		this.mErrorCode = ((ErrorInfo)e).getErrorCode();
        	}
        	this.mMessage = e.getMessage();
            this.mException = e;
        }
    }
    
    /**
     * 获取错误信息
     * 
     * @return 错误信息，可能为空
     */
    public String getMessage() {
    	return mMessage;
    }

    /**
     * 获取错误编码
     * 
     * @return 错误编码，为-1则表示无编码
     */
    public int getErrorCode() {
        return mErrorCode;
    }

    /**
     * 判断是否有Exception
     * 
     * @return Exception
     */
    public boolean hasException() {
        if (mException == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取Exception
     * 
     * @return Exception
     */
    public Exception getException() {
        return mException;
    }

}
