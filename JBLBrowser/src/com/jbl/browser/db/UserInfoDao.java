package com.jbl.browser.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.jbl.browser.bean.UserInfo;
/*
 * 创建UserInfo的Dao
 * 在这个Dao文件中对userInfo表进行操作
 */
public class UserInfoDao {
	private Context context;
	private Dao<UserInfo, Integer> userDao;
	private DatabaseHelper helper;
	
	@SuppressWarnings("unchecked")
	public UserInfoDao(Context context) {
		this.context = context;
		try {
			helper = DatabaseHelper.getHelper(context);
			userDao = helper.getDao(UserInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean userApproved(UserInfo userInfo) {
		boolean flag = false;
		try {
			int temp = userDao.create(userInfo);
			if (temp != 0) {
				flag = true;
			} else
				flag = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	public int deleteUserById(int id){
		try {
			return userDao.deleteById(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public UserInfo get(int id) {
		UserInfo user = null;
		try {
			user = userDao.queryForId(id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	/**
	 * 判断是否已经验证过
	 * @param cid
	 * @return
	 */
	public boolean hasApproved(String cid){
		try {
			List<UserInfo> users=userDao.queryForEq("device_id", cid);
			if(users==null||users.size()==0)
				return false;
		} catch (SQLException e) {
            
			e.printStackTrace();
			return false;
		}

	    return true;
	}
	
	/**
	 * 取手机号
	 * @param cid
	 * @return
	 */
	public String getPhoneID(String cid){
		try {
			List<UserInfo> users=userDao.queryForEq("device_id", cid);
			if(users!=null&&users.size()>0){
				return users.get(0).getPhoneID();
			}
		} catch (SQLException e) {
            
			e.printStackTrace();
		}

	    return "";
	}

}
