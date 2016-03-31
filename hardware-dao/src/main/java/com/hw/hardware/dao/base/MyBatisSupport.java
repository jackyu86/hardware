package com.hw.hardware.dao.base;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hw.hardware.common.exception.AppException;

/**
 * 对mybatis的支持<br/>
 * spring配置文件需定义sqlTemplate与batchSqlTemplate
 * @author cfish
 * @since 2013-09-09
 */
abstract class MyBatisSupport {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Resource protected SqlSessionTemplate sqlTemplate;
	@Resource protected SqlSessionTemplate batchSqlTemplate;
	
	/**
	 * 新增对象
	 * @param statement
	 * @param parameter
	 * @return
	 */
	protected int insert(String statement,Object parameter){
		int res = 0;
		try {
			if(parameter != null) {
				res = sqlTemplate.insert(statement, parameter);
			}
		} catch (Exception ex) {
			throw new AppException("Mybatis执行新增异常", ex);
		}
		return res;
	}
	
	/**
	 * 删除对象
	 * @param statement
	 * @param parameter
	 * @return
	 */
	protected int delete(String statement,Object parameter){
		int res = 0;
		try {
			res = sqlTemplate.delete(statement, parameter);
		} catch (Exception ex) {
			throw new AppException("Mybatis执行删除异常", ex);
		}
		return res;
	}
	
	/**
	 * 更新对象
	 * @param statement
	 * @param parameter
	 * @return
	 */
	protected int update(String statement,Object parameter){
		int res = 0;
		try {
			if(parameter != null) {
				res = sqlTemplate.update(statement, parameter);
			}
		} catch (Exception ex) {
			throw new AppException("Mybatis执行更新异常", ex);
		}
		return res;
	}
	
	/**
	 * 查询一条记录
	 * @param <T>
	 * @param statement
	 * @param parameter
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T select(String statement,Object parameter) {
		T obj = null;
		try {
			obj = (T)sqlTemplate.selectOne(statement, parameter);
		} catch (Exception ex) {
			throw new AppException("Mybatis执行单条查询异常",ex);
		}
		return obj;
	}
	
	/**
	 * 查询列表
	 * @param <T>
	 * @param statement
	 * @param parameter
	 * @param clz
	 * @return
	 */
	protected <T> List<T> selectList(String statement,Object parameter) {
		List<T> list = null;
		try {
			list = sqlTemplate.selectList(statement, parameter);
		} catch (Exception ex) {
			throw new AppException("Mybatis执行列表查询异常",ex);
		}
		return list;
	}
	
	/**
	 *  查询Map
	 * @param <K>
	 * @param <V>
	 * @param statement
	 * @param parameter
	 * @param mapKey
	 * @return
	 */
	protected <K, V> Map<K, V> selectMap(String statement,Object parameter, String mapKey) {
		Map<K, V> map = null;
		try {
			map = sqlTemplate.selectMap(statement, parameter,mapKey);
		} catch (Exception ex) {
			throw new AppException("Mybatis执行Map查询异常",ex);
		}
		return map;
	}
}
