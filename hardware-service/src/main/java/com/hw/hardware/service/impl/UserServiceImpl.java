package com.hw.hardware.service.impl;

import com.hw.hardware.common.Constants;
import com.hw.hardware.dao.UserDao;
import com.hw.hardware.dao.base.BaseDao;
import com.hw.hardware.domain.User;
import com.hw.hardware.service.UserService;
import com.hw.hardware.service.base.BaseServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * UserService 实现类
 * @author cfish
 * @since 2013-09-09
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User,Long> implements UserService {
	
	@Resource private UserDao userDao;
	
	public BaseDao<User,Long> getDao() {
		return userDao;
	}
	
    @Override
    public User loginUser(String erpName) {
        if(StringUtils.isEmpty(erpName)) {return null;}
        
        //TODO 1.查询数据库用户是否存在(如果存在直接返回,好处是即使erp接口不可以系统也不瘫痪)
        
        //TODO 2.如果数据库没有找到用户 则通过erp接口获取用户基本信息
        User user = JDERPUser.getERPUser(erpName);
        if(user != null ){
        	List<String> adminList = Constants.getSystemCfgList("sccd.admin", ",");
        	if(adminList.contains(erpName)){
        		user.setAdmin(true);
        	}
        }
        //TODO 3.如果erp接口获取到了用户存在,由于系统中不存在该用户,这时候保存到本地数据库供下次使用
        
        return user;
    }

    @Override
    public boolean verify(String userName, String password) {
        return JDERPUser.verify_ERP(userName, password);
    }

	@Override
	public void updateUserSyncRbStatus(User user) {
		userDao.updateUserSyncRbStatus(user);
	}

	@Override
	public String getNamesByErps(String erps) {
		if(StringUtils.isNotBlank(erps)){
			StringBuilder names = new StringBuilder();
			String[] userArr = StringUtils.split(erps,",");
			for(String name : userArr) {
				User queryUser = new User();
				queryUser.setName(name);
				List<User> userList = userDao.selectEntryList(queryUser);
				if(userList != null && !userList.isEmpty()) {
					User user = userList.get(0);
					names.append(user.getRealName()).append(",");
				}
			}
			
			if(names.length() > 0) {
				return StringUtils.substringBeforeLast(names.toString(), ",");
			}else{
				return null;
			}
		}
		return null;
	}

}