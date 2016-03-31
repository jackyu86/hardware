package com.hw.hardware.dao;

import com.hw.hardware.dao.base.BaseDao;
import com.hw.hardware.domain.User;

/**
 * UserDao 接口
 * @author cfish
 * @since 2013-09-09
 */
public interface UserDao extends BaseDao<User,Long>{
	//自定义扩展
	
    /**
     * 修改用户是否同步到RB状态
     * @param userList
     */
    void updateUserSyncRbStatus(User user) ;
}