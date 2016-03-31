package com.hw.hardware.common.redis;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingDeque;

/**
 * Redis集中缓存
 * @author cfish
 * @since 2013-09-09
 */
public interface RedisCache {
	
	<K,V> Map<K, V> redisMap(String key);
	void delMap(String key);
	
	BlockingDeque<Object> redisList(String key);
	void delList(String key);
	
	Set<Object> redisSet(String key);
	void delSet(String key);
	
}
