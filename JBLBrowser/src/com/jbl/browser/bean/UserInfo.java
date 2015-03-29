package com.jbl.browser.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/*
 * 创建一张表(UserInfo)
 * 包含的字段有(id,user_name,password,deviceid)
 */
@DatabaseTable(tableName="userinfo")
public class UserInfo {
	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField(columnName="user_name",canBeNull = true)
	private String user_name;
	@DatabaseField(columnName="password",canBeNull = true)
	private String password;
	@DatabaseField(columnName="device_id")
	private String device_id;
	
	public UserInfo() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDeviceID(){
		return device_id;
	}
	public void setDeviceID(String device_id){
		this.device_id=device_id;
	}
	
	
}
