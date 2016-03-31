package com.hw.hardware.common.spring.secret;

/**
 * 权限认证类型
 * @author cfish
 * @since 2013-09-09
 */
public enum AuthType {
	
	/**
	 * 无需认证直接通过
	 */
	NONE,
	
	/**
	 * 公共资源,需登录
	 */
	PUBLIC,
	
	/**
	 * 用户权限码认证
	 */
	CODE,
	
	/**
	 * 请求需中包含指定Token值
	 */
	TOKEN;
}
