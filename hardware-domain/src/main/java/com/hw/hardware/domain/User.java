package com.hw.hardware.domain;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Sets;
import com.hw.hardware.domain.base.BaseDomain;

/**
 * user
 * @author cfish
 * @since 2013-09-09
 */
public class User extends BaseDomain {
	private static final long serialVersionUID = 1L;
	
	//以下字段与ERP系统对应
	private String name;//erp
    private String password;//密码
    private String realName;//真实姓名
    private String phone;//电话号码
    private String email;//邮箱
    private String deptmentName;//部门
    private String positionName;//职务
    
    private String deptcode;
	private String deptrank;
	private Date createTime;
	
	//用户是否同步到RB，NO未同步，YES已经同步
	private String syncRb;
	
    //扩展字段
	private boolean admin;
	private Set<String> permission = Sets.newHashSet("#");//新的权限认证只支持权限码方式,用户所拥有的所有资源码集合
	
	public User(){
		//默认无参构造方法
	}
	
	/**
	 * 添加用户权限码
	 * @param code
	 */
	public void addPermissionCode(String code) {
        if(StringUtils.isNotEmpty(code) && !permission.contains(code)) {
            permission.add(code);
        }
    }
	
	
	//验证权限
    public boolean verify(boolean or,String... code) {
        if(admin) {return true;}
        if(code == null || code.length<=0) {return false;}
        for(String c : code) {
            if(or&&permission.contains(c)){return true;}
            if(!or&&!permission.contains(c)){return false;}
        }
        return false;
    }
    public boolean verify(String... code) {
        return verify(false,code);
    }
	
	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDeptcode() {
		return deptcode;
	}


	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getDeptrank() {
		return deptrank;
	}


	public void setDeptrank(String deptrank) {
		this.deptrank = deptrank;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
    
    public String getRealName() {
        return realName;
    }

    
    public void setRealName(String realName) {
        this.realName = realName;
    }

    
    public String getDeptmentName() {
        return deptmentName;
    }

    
    public void setDeptmentName(String deptmentName) {
        this.deptmentName = deptmentName;
    }

    
    public String getPositionName() {
        return positionName;
    }

    
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    
    public Set<String> getPermission() {
        return permission;
    }

    
    public void setPermission(Set<String> permission) {
        this.permission = permission;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    
    public boolean isAdmin() {
        return admin;
    }

	public String getSyncRb() {
		return syncRb;
	}

	public void setSyncRb(String syncRb) {
		this.syncRb = syncRb;
	}
}