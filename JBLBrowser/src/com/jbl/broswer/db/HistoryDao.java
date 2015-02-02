package com.jbl.broswer.db;

import java.sql.SQLException;
import java.util.List;

import orm.sqlite.db.DatabaseHelper;
import android.content.Context;

import com.ccit.mmwlan.a.h;
import com.j256.ormlite.dao.Dao;
import com.jbl.broswer.bean.BookMark;
import com.jbl.broswer.bean.History;
/*
 * 创建History的Dao
 * 在这个Dao文件中对history表进行操作
 */
public class HistoryDao {
	private Context context;
	private Dao<History, Integer> HistoryDaoOpe;
	private DatabaseHelper helper;
	
	public HistoryDao(Context context){
		this.context = context;
		try
		{
			helper = DatabaseHelper.getHelper(context);
			HistoryDaoOpe = helper.getDao(History.class);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	//添加历史记录
	public void addHistory(String webName,String webAddress){
		History history=new History();
		history.setWebName(webName);
		history.setWebAddress(webAddress);
		try {
			HistoryDaoOpe.create(history);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//清空所有
	public void clearHistory(){
		try {
			HistoryDaoOpe.delete(HistoryDaoOpe.queryForAll());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//查询所有
	public List<History> queryAll(){
		 List<History> history;
		try {
			history = HistoryDaoOpe.queryForAll();
			return history;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
