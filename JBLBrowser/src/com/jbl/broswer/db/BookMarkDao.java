package com.jbl.broswer.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import orm.sqlite.bean.Article;
import orm.sqlite.db.DatabaseHelper;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.jbl.broswer.bean.BookMark;
/*
 * 创建BookMark的Dao
 * 在这个Dao文件中对bookMark表进行操作
 */
public class BookMarkDao {
	private Context context;
	private Dao<BookMark, Integer> BookMarkDaoOpe;
	private DatabaseHelper helper;
	
	@SuppressWarnings("unchecked")
	public BookMarkDao(Context context){
		this.context = context;
		try
		{
			helper = DatabaseHelper.getHelper(context);
			BookMarkDaoOpe = helper.getDao(BookMark.class);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	public int addBookMark(BookMark bookmark){
		try {
			return BookMarkDaoOpe.create(bookmark);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	public int deleteBookMarkByWebAddress(String webAddress){
		try {
			DeleteBuilder<BookMark,Integer> deleteBuilder=BookMarkDaoOpe.deleteBuilder();
			deleteBuilder.where().eq("WebAddress", webAddress);
			return deleteBuilder.delete();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	public BookMark get(int id)
	{
		BookMark bookmark = null;
		try
		{
			bookmark = BookMarkDaoOpe.queryForId(id);

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return bookmark;
	}
	public List<BookMark> queryAll(){
		List<BookMark> bookmark=null;
		try {
			bookmark = BookMarkDaoOpe.queryForAll();
			return bookmark;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return null;
	} 
}
