package com.hw.hardware.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * ApplicationContext
 * 普通java程序可以从此处获取bean
 * @author cfish
 * @since 2013-09-09
 */
public class ApplicationContextUtil implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		context = ctx;
	}
	
	public static ApplicationContext getContext() {
		return context;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name){
		if(context != null) {
			return (T)context.getBean(name);
		}
		return null;
	}
	
	public static <T> T getBean(Class<T> clz){
		if(context != null) {
			return context.getBean(clz);
		}
		return null;
	}
}
