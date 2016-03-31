package com.hw.hardware.service;

import com.hw.hardware.domain.User;
import com.hw.hardware.service.base.BaseService;

/**
 * UserService接口
 * @author cfish
 * @since 2013-09-09
 */
public interface UserService extends BaseService<User,Long> {
	
    /**
	 * 通过ERP账号登陆系统
	 * @param erpName
	 * @return
	 */
    User loginUser(String erpName);
    
    /**
     * 用户认证(支持ERP认证)
     * @param userName 用户名或邮箱
     * @param password ERP密码或邮箱密码
     * @return
     */
    boolean verify(String userName,String password);
    
    /**
     * 修改用户是否同步到RB状态
     * @param userList
     */
    void updateUserSyncRbStatus(User user) ;
    
    /**
     * 根据erp获取姓名
     * @param erp 逗号分隔 格式为 erp1,erp2,erp3
     * @return
     */
    String getNamesByErps(String erps);
}