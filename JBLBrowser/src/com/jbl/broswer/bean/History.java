package com.jbl.broswer.bean;


import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/*
 * 创建一张表(history)
 * 包含的字段有(id,webName,webAddress,browsed_time,created_time,update_time)
 */
@DatabaseTable(tableName="history")
public class History {
	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField(columnName="webName")
	private String webName;                                                                                                                                                                                                                                  
	@DatabaseField(columnName="webAddress")
	private String webAddress;
	@DatabaseField(columnName="browsed_time")
	private Date browsed_time;
	@DatabaseField(columnName="created_time")
	private Date created_time;
	@DatabaseField(columnName="update_time")
	private Date update_time;

	public History() {
	}

	public Date getBrowsed_time() {
		return browsed_time;
	}

	public void setBrowsed_time(Date browsed_time) {
		this.browsed_time = browsed_time;
	}

	public Date getCreated_time() {
		return created_time;
	}

	public void setCreated_time(Date created_time) {
		this.created_time = created_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWebName() {
		return webName;
	}

	public void setWebName(String webName) {
		this.webName = webName;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}



	
	
	
	
}
