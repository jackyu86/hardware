package com.hw.hardware.common.spring.secret;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 安全认证
 * @author cfish
 * @since 2013-09-09
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {

	/**
	 * 认证方式
	 * @return
	 */
	AuthType type() default (AuthType.NONE);
	
	/**
	 * 权限编码
	 * @return
	 */
	String[] code() default {};
	
	/**
	 * 权限编码条件(并且、或者)
	 * @return
	 */
	AuthCondtion condtion() default (AuthCondtion.AND);
}
