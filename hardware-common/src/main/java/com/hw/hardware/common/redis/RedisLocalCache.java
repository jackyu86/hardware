package com.hw.hardware.common.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.DefaultRedisSet;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.data.redis.support.collections.RedisSet;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.hw.hardware.common.Constants;
import com.hw.hardware.common.exception.AppException;
import com.jd.ump.profiler.CallerInfo;
import com.jd.ump.profiler.proxy.Profiler;

/**
 * redis和本地缓存（支持Map、Queue、Set三种数据类型）
 * @author cfish
 * @since 2013-09-09
 */
public abstract class RedisLocalCache {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	//本地缓存,redis异常时可以临时使用,采用自动销毁策略
	private final static Map<String,Map<Object, Object>> LOCAL_MAP_CACHE = Maps.newHashMap();
	private final static Map<String,BlockingDeque<Object>> LOCAL_LIST_CACHE = Maps.newHashMap();
	private final static Map<String,Set<Object>> LOCAL_SET_CACHE = Maps.newHashMap();
	
	private static final int ERROR_LONG_TIME = 60000*3;//每三分钟检查一次Redis是否可以,否则使用本地缓存
	private long lastErrorTime = 0;//访问Redis的最后错误时间
	private String defaultKeyPrefix = Constants.getSystemCfg("redis.key.prefix","UWP_");//默认前缀
	//监控点key
	private static final String UMP_DEFINESELF_REDIS = "jone.RedisLocalCache.rediserror";
	private static final String UMP_METHED_REDISMAP = "jone.RedisLocalCache.redisMap";
	private static final String UMP_BUSINESS_REDIS_INVOKE_SUCC = "jone.RedisLocalCache.redisMap.redisSuccessFetchData";
	
	//由Spring注入redisTemplate对象
	@Resource protected RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 返回缓存MAP对象<br/>
	 * 如果要使用RedisMap的一些特性请使用 if(map instanceof RedisMap)来判断后再用
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized <K,V> Map<K, V> redisMap(String key) {
		Map<K,V> map = null;
		CallerInfo info = Profiler.registerInfo(UMP_METHED_REDISMAP, false, true);
		String redisKey = defaultKeyPrefix+key;
		try {
			if(isError()){
				Profiler.businessAlarm(UMP_DEFINESELF_REDIS, "redis can't used,local cache start!");
				throw new AppException("当前Redis不可用,需本地缓存响应!");
			}
			if(LOCAL_MAP_CACHE.containsKey(redisKey)){
				return (Map<K,V>)LOCAL_MAP_CACHE.remove(redisKey);
			}
			map = new DefaultRedisMap<K,V>(redisKey,redisTemplate);
			Profiler.countAccumulate(UMP_BUSINESS_REDIS_INVOKE_SUCC);
			if(map.size() > 0 && LOGGER.isDebugEnabled()) {
				LOGGER.debug("fetch redisMap [{}] {} instance",redisKey, map.size());
			}
		} catch (AppException e) {
			if(!LOCAL_MAP_CACHE.containsKey(redisKey)) {
				LOCAL_MAP_CACHE.put(redisKey, new HashMap<Object, Object>());
			} 
			map = (Map<K,V>)LOCAL_MAP_CACHE.get(redisKey);
			Profiler.functionError(info);
		} catch (Exception e) {
			if(!LOCAL_MAP_CACHE.containsKey(redisKey)) {
				LOCAL_MAP_CACHE.put(redisKey, new HashMap<Object, Object>());
			} 
			map = (Map<K,V>)LOCAL_MAP_CACHE.get(redisKey);
			lastErrorTime = System.currentTimeMillis();
			LOGGER.warn("Redis不可用,通过本地缓存响应MapKey==>[{}] {} 实例",redisKey,map.size());
			Profiler.functionError(info);
		}finally{
			Profiler.registerInfoEnd(info);;
		}
		return map;
	}
	
	/**
	 * 删除Map对象
	 * @param key
	 */
	@SuppressWarnings("rawtypes")
	public void delMap(String key) {
		Map map = redisMap(key);
		if(map instanceof RedisMap) {
			((RedisMap)map).expire(0, TimeUnit.MINUTES);
		}
		LOCAL_MAP_CACHE.remove(defaultKeyPrefix+key);
	}
	
	/**
	 * 删除本次map
	 * @param key
	 * @param second
	 */
	public void delLocalMap(String key){
		LOCAL_MAP_CACHE.remove(defaultKeyPrefix+key);
	}
	
	/**
	 * 返回缓存List对象<br/>
	 * 如果要使用RedisMap的一些特性请使用 if(queue instanceof RedisList)来判断后再用
	 * @param key
	 * @return
	 */
	public synchronized BlockingDeque<Object> redisList(String key) {
		BlockingDeque<Object> queue = null;
		String redisKey = defaultKeyPrefix+key;
		try {
			if(isError()){throw new AppException("当前Redis不可用,需本地缓存响应!");}
			if(LOCAL_LIST_CACHE.containsKey(redisKey)){return LOCAL_LIST_CACHE.remove(redisKey);}
			queue = new DefaultRedisList<Object>(redisKey, redisTemplate);
			if(queue.size() > 0 && LOGGER.isDebugEnabled()) {
				LOGGER.debug("fetch redisList [{}] {} instance",redisKey, queue.size());
			}
		} catch (AppException e) {
			if(!LOCAL_LIST_CACHE.containsKey(redisKey)) {
				LOCAL_LIST_CACHE.put(redisKey,Queues.newLinkedBlockingDeque());
			}
			queue = LOCAL_LIST_CACHE.get(redisKey);  
		} catch (Exception e) {
			if(!LOCAL_LIST_CACHE.containsKey(redisKey)) {
				LOCAL_LIST_CACHE.put(redisKey,Queues.newLinkedBlockingDeque());
			}
			queue = LOCAL_LIST_CACHE.get(redisKey);
			lastErrorTime = System.currentTimeMillis();
			LOGGER.warn("Redis不可用,通过本地缓存响应ListKey==>[{}] {} 实例",redisKey,queue.size());
		}
		return queue;
	}
	
	/**
	 * 删除List对象
	 * @param key
	 */
	@SuppressWarnings("rawtypes")
	public void delList(String key) {
		BlockingDeque list = redisList(key);
		if(list instanceof RedisList) {
			((RedisList)list).expire(0, TimeUnit.MINUTES);
		}
		LOCAL_LIST_CACHE.remove(defaultKeyPrefix+key);
	}
	
	/**
	 * 返回缓存Set对象<br/>
	 * 如果要使用RedisMap的一些特性请使用 if(set instanceof RedisSet)来判断后再用
	 * @param key
	 * @return
	 */
	public synchronized Set<Object> redisSet(String key) {
		Set<Object> set = null;
		String redisKey = defaultKeyPrefix+key;
		try {
			if(isError()){throw new AppException("当前Redis不可用,需本地缓存响应!");}
			if(LOCAL_SET_CACHE.containsKey(redisKey)){return LOCAL_SET_CACHE.remove(redisKey);}
			set = new DefaultRedisSet<Object>(redisKey, redisTemplate);
			if(set.size() > 0 && LOGGER.isDebugEnabled()) {
				LOGGER.debug("fetch redisSet [{}] {} instance",redisKey, set.size());
			}
		} catch (AppException e) {
			if(!LOCAL_SET_CACHE.containsKey(redisKey)) {
				LOCAL_SET_CACHE.put(redisKey, Sets.newHashSet());
			}
			set = LOCAL_SET_CACHE.get(redisKey);
		} catch (Exception e) {
			if(!LOCAL_SET_CACHE.containsKey(redisKey)) {
				LOCAL_SET_CACHE.put(redisKey, Sets.newHashSet());
			}
			set = LOCAL_SET_CACHE.get(redisKey);
			lastErrorTime = System.currentTimeMillis();
			LOGGER.warn("Redis不可用,通过本地缓存响应SetKey==>[{}] {} 实例",redisKey,set.size());
		}
		return set;
	}
	
	/**
	 * 删除Set对象
	 * @param key
	 */
	public void delSet(String key) {
		Set<Object> set = redisSet(key);
		if(set instanceof RedisSet) {
			((RedisSet<Object>)set).expire(0, TimeUnit.MINUTES);
		}
		LOCAL_SET_CACHE.remove(defaultKeyPrefix+key);
	}
	
	//判断Redis是否处于错误时间段内
	private boolean isError(){
		return System.currentTimeMillis() - lastErrorTime <= ERROR_LONG_TIME;
	}

}
