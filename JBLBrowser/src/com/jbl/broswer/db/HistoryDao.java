package com.jbl.broswer.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.jbl.broswer.bean.History;
/*
 * 创建History的Dao
 * 在这个Dao文件中对history表进行操作
 */
public class HistoryDao {
	private Context context;
	private Dao<History, Integer> HistoryDaoOpe;
	private DatabaseHelper helper;
	private Boolean flag=false;
	
	@SuppressWarnings("unchecked")
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
	public Boolean addHistory(History history){
		flag=false;
		try {
			int temp=0;
			temp=HistoryDaoOpe.create(history);
			if(temp!=0)
				flag=true;
			else
				flag=false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	//清空所有
	public Boolean clearHistory(){
		flag=false;
		try {
			int temp=HistoryDaoOpe.delete(HistoryDaoOpe.queryForAll());
			if(temp!=0)
				flag=true;
			else
				flag=false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	//查询所有
	public List<History> queryAll(){
		 List<History> history=null;
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
