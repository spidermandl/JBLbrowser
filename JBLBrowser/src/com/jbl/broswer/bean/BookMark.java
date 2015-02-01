package com.jbl.broswer.bean;

import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/*
 * 创建一张表(bookMark)
 * 包含的字段有(id,webName,webAddress,created_time,update_time)
 */
@DatabaseTable(tableName="bookMark")
public class BookMark {
	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField(columnName="webName")
	private String webName;
	@DatabaseField(columnName="webAddress")
	private String webAddress;
	@DatabaseField(columnName="created_time")
	private Date created_time;
	@DatabaseField(columnName="update_time")
	private Date update_time;
	
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

	public BookMark() {
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
