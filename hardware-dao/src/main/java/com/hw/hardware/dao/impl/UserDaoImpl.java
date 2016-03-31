package com.hw.hardware.dao.impl;

import org.springframework.stereotype.Repository;

import com.hw.hardware.dao.UserDao;
import com.hw.hardware.dao.base.BaseDaoImpl;
import com.hw.hardware.domain.User;

/**
 * UserDao 实现类
 * @author cfish
 * @since 2013-09-09
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User,Long> implements UserDao {
	private final static String NAMESPACE = "com.hw.hardware.dao.UserDao.";
	
	//返回本DAO命名空间,并添加statement
	public String getNameSpace(String statement) {
		return NAMESPACE + statement;
	}

	@Override
	public void updateUserSyncRbStatus(User user) {
		update(NAMESPACE+"updateUserSyncRbStatus",user);
	}
}