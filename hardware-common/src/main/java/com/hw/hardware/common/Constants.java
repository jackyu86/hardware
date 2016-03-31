package com.hw.hardware.common;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 基础常量
 * @author cfish
 * @since 2013-09-09
 */
public class Constants {
	public static final long startTime = System.currentTimeMillis();//服务启动时间
	public final static String DEFAULT_ENCODING = "UTF-8";
	public final static String LOGIN_USER_COOKIE_NAME = "sccd.jd.com";
	public final static String LOGIN_USER_ONLINE_KEY = "loginUser";
	public final static Map<String, String> SPRING_PROPERTIES = Maps.newHashMap();//Spring加载的属性文件
	public final static int DELETE_REDIS_CACHE = 0;
	public final static String API_CODE_NAME = "CommonApi###abc";
	public final static String API_NAME = "CommonApi";
	public final static String API_SPILT_SIGN = "###";
	
    public final static Integer DAYS_OF_WEEK = 7; // 一周的天数

    private final static Logger log = LoggerFactory.getLogger(Constants.class);

	/**
	 * 获取系统配置值(由spring加载的属性文件)
	 * @param key
	 * @return
	 */
	public static String getSystemCfg(String key){
		return SPRING_PROPERTIES.get(key);
	}
	
	public static String getSystemCfg(String key,String defaultValue){
		String value = getSystemCfg(key);
		if(StringUtils.isEmpty(value)) {
			value = defaultValue;
		}
		return value;
	}

	public static List<String> getSystemCfgList(String key,String split){
		try {
			return Lists.newArrayList(getSystemCfg(key,"").split(split));
		} catch (Exception e) {
			return Lists.newArrayList();
		}
	}
	
	public static boolean getBooleanCfg(String key){
		return getBooleanCfg(key, false);
	}
	
	public static boolean getBooleanCfg(String key,boolean defaultValue){
		return "true".equalsIgnoreCase(getSystemCfg(key,String.valueOf(defaultValue)));
	}
	
	public static int getIntegerCfg(String key){
		return getIntegerCfg(key, 0);
	}
	
	public static int getIntegerCfg(String key,Integer defaultValue){
		try {
			return Integer.valueOf(getSystemCfg(key,String.valueOf(defaultValue)));
		} catch (Exception e) {
            log.error("获取配置项:" + key + "错误，将使用默认值：" + defaultValue + "。", e);
			return defaultValue;
		}
	}
}
