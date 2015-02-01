package com.jbl.broswer.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/*
 * 创建一张表(UserInfo)
 * 包含的字段有(id,user_name,password)
 */
@DatabaseTable(tableName="userinfo")
public class UserInfo {
	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField(columnName="user_name")
	private String user_name;
	@DatabaseField(columnName="password")
	private String password;
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
	
	
}
