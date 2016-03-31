package com.hw.hardware.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.stereotype.Service;

import com.hw.hardware.common.Constants;
import com.hw.hardware.common.redis.RedisCache;
import com.hw.hardware.common.redis.RedisLocalCache;
import com.hw.hardware.domain.User;
import com.hw.hardware.service.RedisService;
import com.jd.ump.profiler.CallerInfo;
import com.jd.ump.profiler.proxy.Profiler;

/**
 * 
 * @author cfish
 * @since 2013-4-22
 */
@Service("redisService")
public class RedisServiceImpl extends RedisLocalCache implements RedisCache,RedisService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceImpl.class);
    private static final String ONLINE_USER_KEY= "USER_";
    private static final String MAP_CACHE_KEY = "DEFINE_SELF_KEY";
    //监控key
	private static final String UMP_METHED_ADDLOGINUSER = "jone.RedisServiceImpl.addLoginUser";
	private static final String UMP_METHED_LOADLOGINUSER = "jone.RedisServiceImpl.loadLoginUser";
	private static final String UMP_METHED_USERLOGOUT = "jone.RedisServiceImpl.userLogout";
	private static final String UMP_METHED_ADDMAPCACHE = "jone.RedisServiceImpl.addMapCache";
	private static final String UMP_METHED_GETMAPCACHE = "jone.RedisServiceImpl.getMapCache";
	private static final String UMP_METHED_ADDHMAPCACHE = "jone.RedisServiceImpl.addHMapCache";
	private static final String UMP_METHED_GETHMAPCACHE = "jone.RedisServiceImpl.getHMapCache";
	private static final String UMP_METHED_GETALLMAP = "jone.RedisServiceImpl.getAllHMap";
	private static final String UMP_METHED_EXPIREMAPCACHE = "jone.RedisServiceImpl.expireMapCache";
	
    @Override
    public void addLoginUser(String cookie, User user,String loginIp) {
    	CallerInfo info = Profiler.registerInfo(UMP_METHED_ADDLOGINUSER, false, true);
    	try{
	        if(StringUtils.isEmpty(cookie) || user == null) {return;}
	        Map<String, User> map = redisMap(ONLINE_USER_KEY+cookie);
	        user.setCreateDate(new Date());//登陆时间
	        user.setUuid(cookie);//登陆cookie
	        user.setCode(loginIp);//登陆IP地址
	        map.put(Constants.LOGIN_USER_COOKIE_NAME, user);
	        if(map instanceof RedisMap) {
	            ((RedisMap<String, User>)map).expire(30, TimeUnit.MINUTES);
	        }
    	}catch(Exception e){
    		Profiler.functionError(info);
    	}finally{
    		Profiler.registerInfoEnd(info);
    	}
    }

    @Override
    public User loadLoginUser(String cookie) {
    	CallerInfo info = Profiler.registerInfo(UMP_METHED_LOADLOGINUSER, false, true);
    	try{
	        if(StringUtils.isEmpty(cookie)) {return null;}
	        Map<String, User> map = redisMap(ONLINE_USER_KEY+cookie);
	        User user = map.get(Constants.LOGIN_USER_COOKIE_NAME);
	        if(user == null) {
	            delMap(ONLINE_USER_KEY+cookie);
	            return user;
	        }
	        user.setModifyDate(new Date());//最近访问时间
	        map.put(Constants.LOGIN_USER_COOKIE_NAME,user);
	        if(map instanceof RedisMap) {
	            ((RedisMap<String, User>)map).expire(30, TimeUnit.MINUTES);
	        }
	        return user;
    	}catch(Exception e){
    		Profiler.functionError(info);
    	}finally{
    		Profiler.registerInfoEnd(info);
    	}
    	return null;
    }

    @Override
    public User userLogout(String cookie) {
    	CallerInfo info = Profiler.registerInfo(UMP_METHED_USERLOGOUT, false, true);
        if(StringUtils.isEmpty(cookie)) {return null;}
        Map<String, User> map = redisMap(ONLINE_USER_KEY+cookie);
        User user = map.get(Constants.LOGIN_USER_COOKIE_NAME);
        delMap(ONLINE_USER_KEY+cookie);
        Profiler.registerInfoEnd(info);
        return user;
    }

	@Override
	public void addMapCache(String key, String value) {
		CallerInfo info = Profiler.registerInfo(UMP_METHED_ADDMAPCACHE, false, true);
		if(StringUtils.isBlank(key) || StringUtils.isBlank(value)){
			LOGGER.warn("add cache to redis fail key ["+key+"] value["+value+"]");
			return;
		}else{
			Map<String,String> map = redisMap(key);
			map.put(MAP_CACHE_KEY, value);
		}
		Profiler.registerInfoEnd(info);
	}

	@Override
	public String getMapCache(String key) {
		CallerInfo info = Profiler.registerInfo(UMP_METHED_GETMAPCACHE, false, true);
		try{
			if(key.isEmpty()){
				LOGGER.warn("get cache from redis fail key ["+key+"]");
				return null;
			}else{
				Map<String,String> map = redisMap(key);
				return map.get(MAP_CACHE_KEY);
			}
		}catch(Exception e){
			Profiler.functionError(info);
		}finally{
			Profiler.registerInfoEnd(info);
		}
		return null;
	}

	@Override
	public void addHMapCache(String key, String field, String value) {
		CallerInfo info = Profiler.registerInfo(UMP_METHED_ADDHMAPCACHE, false, true);
		if(key.isEmpty() || field.isEmpty()){
			LOGGER.warn("add Hmap cache to redis fail key ["+key+"] field["+field+"]value["+value+"]");
			return;
		}else{
			Map<String,String> map = redisMap(key);
			map.put(field, value);
		}
		Profiler.registerInfoEnd(info);
	}

	@Override
	public String getHMapCache(String key, String field) {
		CallerInfo info = Profiler.registerInfo(UMP_METHED_GETHMAPCACHE, false, true);
		try{
			if(key.isEmpty() || field.isEmpty()){
				LOGGER.warn("get Hmap cache from redis fail key ["+key+"] field["+field+"]");
				return null;
			}else{
				Map<String,String> map = redisMap(key);
				return map.get(field);
			}
		}catch(Exception e){
			Profiler.functionError(info);
		}finally{
			Profiler.registerInfoEnd(info);
		}
		return null;
	}

	@Override
	public Map<String, String> getAllHMap(String key) {
		CallerInfo info = Profiler.registerInfo(UMP_METHED_GETALLMAP, false, true);
		try{
			if(key.isEmpty()){
				LOGGER.warn("get All Hmap cache from redis fail key ["+key+"]");
				return null;
			}else{
				return redisMap(key);
			}
		}catch(Exception e){
			Profiler.functionError(info);
		}finally{
			Profiler.registerInfoEnd(info);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void expireMapCache(String key,long second) {
		CallerInfo info = Profiler.registerInfo(UMP_METHED_EXPIREMAPCACHE, false, true);
		if(key.isEmpty()){
			LOGGER.warn("expire map set redis fail key ["+key+"]");
			return;
		}else{
			Map map = redisMap(key);
			if(map instanceof RedisMap){
				((RedisMap)map).expire(second, TimeUnit.SECONDS);
			}
			delLocalMap(key);
		}
		Profiler.registerInfoEnd(info);
	}
    
}
