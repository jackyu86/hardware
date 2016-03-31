package com.hw.hardware.service.base;

import java.io.Serializable;
import java.util.List;

import com.hw.hardware.domain.base.Page;
import com.hw.hardware.domain.base.SysLog;

/**
 * service基类<实体,主键>
 * @author cfish
 * @since 2013-09-09
 */
public interface BaseService<T,KEY extends Serializable> {

	/**
	 * 添加对象
	 * @param t
	 * @return
	 */
	int insertEntry(T...t);
	
	/**
	 * 添加对象
	 * @param t
	 * @param log
	 * @return
	 */
	int insertEntry(T t,SysLog log);
	
	/**
	 * 保存对象
	 * @param t
	 * @return
	 */
	KEY save(T t);
	
	/**
	 * 更新对象
	 * @return
	 */
	T update(T t);
	
	/**
	 * 删除对象,主键
	 * @param key 主键数组
	 * @return 影响条数
	 */
	int deleteByKey(KEY...key);
	
	/**
	 * 删除对象,主键
	 * @param key 主键
	 * @param log 日志对象
	 * @return 影响条数
	 */
	int deleteByKey(KEY key,SysLog log);
	
	/**
	 * 删除对象,主键
	 * @param key 主键数组
	 * @param log 日志对象
	 * @return 影响条数
	 */
	int deleteByKey(KEY[] key,SysLog log);
	
	/**
	 * 按条件删除对象
	 * @param condtion
	 * @return 影响条数
	 */
	int deleteByCondtion(T condtion);
	
	/**
	 * 按条件删除对象
	 * @param condtion
	 * @param log 日志对象
	 * @return 影响条数
	 */
	int deleteByCondtion(T condtion,SysLog log);
	
	/**
	 * 更新对象,条件主键Id
	 * @param condtion 更新对象
	 * @return 影响条数
	 */
	int updateByKey(T condtion);
	
	/**
	 * 更新对象,条件主键Id
	 * @param condtion 更新对象
	 * @param log 日志对象
	 * @return 影响条数
	 */
	int updateByKey(T condtion,SysLog log);
	
	/**
	 * 保存或更新对象(条件主键Id)
	 * @param t 需更新的对象
	 * @return 影响条数
	 */
	int saveOrUpdate(T t);
	
	/**
	 * 保存或更新对象(条件主键Id)
	 * @param t 需更新的对象
	 * @param log 日志对象
	 * @return 影响条数
	 */
	int saveOrUpdate(T t,SysLog log);
	
	/**
	 * 查询对象,条件主键
	 * @param key
	 * @return 实体对象
	 */
	T selectEntry(KEY key);
	
	/**
	 * 查询对象列表,主键数组
	 * @param key
	 * @return 对象列表
	 */
	List<T> selectEntryList(KEY...key);
	
	/**
	 * 查询对象,只要不为NULL与空则为条件
	 * @param condtion 查询条件
	 * @return 对象列表
	 */
	List<T> selectEntryList(T condtion);
	
	/**
	 * 查询对象条数,只要不为NULL与空则为条件
	 * @param condtion 查询条件
	 * @return 对象条数
	 */
	int selectEntryListCount(T condtion);
	
	/**
	 * 分页查询
	 * @param condtion 查询条件
	 * @return 分页对象
	 */
	Page<T> selectPage(T condtion, Page<T> page);
}
