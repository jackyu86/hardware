package com.hw.hardware.service;

import java.util.Map;

import com.hw.hardware.domain.User;

/**
 * Redis相关服务
 * @author cfish
 * @since 2013-4-22
 */
public interface RedisService {

    //缓存登录用户
    void addLoginUser(String cookie,User user,String loginIp);
    User loadLoginUser(String cookie);
    User userLogout(String cookie);
    void addMapCache(String key,String value);
    void expireMapCache(String key,long second);
    String getMapCache(String key);
    void addHMapCache(String key,String field,String value);
    String getHMapCache(String key,String field);
    Map<String,String> getAllHMap(String key);
}
