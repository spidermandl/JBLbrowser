package com.jbl.browser.model;

import java.io.Serializable;


/**服务器的返回结果对应的实体*/
public class ResponseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int responseCode;  //请求结果状态码
	private String responseMessageType; //请求结果对应的信息的类型
	private String responseMessage; //请求结果对应的信息
	private int code;  //保留位，代码
	
	/**
	 * 
	 * @param 待解析的服务器返回数据
	 * */
	public ResponseModel(String response) throws Exception{
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}
	
	/**获取请求结果状态码*/
	public int getResponseCode() {
		return responseCode;
	}
	/**获取请求结果对应的信息的类型*/
	public String getResponseMessageType() {
		return responseMessageType;
	}
	/**获取请求结果对应的信息*/
	public String getResponseMessage() {
		return responseMessage;
	}
	/**获取保留位，代码*/
	public int getCode() {
		return code;
	}
	
	/**
	 * 解析请求结果
	 * 
	 * @param response 请求结果对应的字符串
	 * 
	 * */
	private void parseResponse(String response) throws Exception{
		String mResponse = null;
		Integer responseCode = null;
		String responseMessageType = null;
		String responseMessage = null;
		Integer code = null;
		try {
			if(response == null){
				throw new Exception("请求结果为空");
			}
			if(response.contains("维护")){
				this.responseMessage = response;
				return;
			}
			mResponse = response;
			responseCode = Integer.valueOf(mResponse.substring(0,mResponse.indexOf(",")));
//			Log.e("MODEL", "" + responseCode);
			mResponse = mResponse.substring(mResponse.indexOf(",") + 1,mResponse.length());
			responseMessageType = mResponse.substring(0,mResponse.indexOf(","));
//			Log.e("MODEL", "" + responseMessageType);
			mResponse = mResponse.substring(mResponse.indexOf(",") + 1,mResponse.length());
			responseMessage = mResponse.substring(0,mResponse.indexOf(","));
//			Log.e("MODEL", "" + responseMessage);
			mResponse = mResponse.substring(mResponse.indexOf(",") + 1,mResponse.length());
			code = Integer.valueOf(mResponse.substring(0,mResponse.indexOf(",")));
//			Log.e("MODEL", "" + code);
			
			this.responseCode = responseCode;
			this.responseMessageType = responseMessageType;
			this.responseMessage = responseMessage;
			this.code = code;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception("请求结果数据格式错误");
			
		}finally{
			mResponse = null;
			responseCode = null;
			responseMessage = null;
			responseMessageType = null;
			code = null;
		}
	}
}
