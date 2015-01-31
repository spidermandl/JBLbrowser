package com.jbl.browser.tools;

import com.jbl.browser.model.ErrorInfo;

import android.os.Bundle;

public interface BusinessCallback {

	/**
	 * 请求完成
	 * 
	 * @param values 服务器端返回的数据，包含状态码和结果数据
	 * 
	 * */
	void complete(Bundle values);
	
	/**
	 * 请求错误
	 * 
	 * @param e 错误信息，包含错误代码和信息
	 * 
	 * */
	void error(ErrorInfo e);
	
	/**网络错误*/
	void fail(ErrorInfo e);
}
