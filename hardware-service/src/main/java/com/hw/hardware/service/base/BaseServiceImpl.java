package com.hw.hardware.service.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hw.hardware.common.exception.AppException;
import com.hw.hardware.dao.base.BaseDao;
import com.hw.hardware.domain.base.Page;
import com.hw.hardware.domain.base.SysLog;

/**
 * service实现类
 * @author cfish
 * @since 2013-09-09
 */
public abstract class BaseServiceImpl<T, KEY extends Serializable> implements BaseService<T, KEY> {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/**
	 * 获取DAO操作类
	 */
	public abstract BaseDao<T, KEY> getDao();
	
	public int insertEntry(T...t) {
		return getDao().insertEntry(t);
	}
	
	@SuppressWarnings("unchecked")
	public int insertEntry(T t,SysLog log) {
		if(t == null || log == null) {return this.insertEntry(t);}
		int res = 0;
		try {
			if(log.getActionName() == null) {log.setActionName("新增["+t.getClass()+"]对象");}
			res = this.insertEntry(t);
		} finally {
			log.setResult(res > 0 ?"成功":"失败");
		}
		return res;
	}
	
	public KEY save(T t) {
		return getDao().save(t);
	}
	
	public T update(T t){
		return getDao().update(t);
	}
	
	public int deleteByKey(KEY...key) {
		return getDao().deleteByKey(key);
	}
	
	@SuppressWarnings("unchecked")
	public int deleteByKey(KEY key,SysLog log) {
		if(log == null) {return this.deleteByKey(key);}
		int res = 0;
		try {
			if(log.getActionName() == null) {log.setActionName("删除[key="+key+"]对象");}
			res = this.deleteByKey(key);
		} finally {
			log.setResult(res > 0 ?"成功":"失败");
		}
		return res;
	}
	
	public int deleteByKey(KEY[] key,SysLog log) {
		if(key == null || log == null) {return this.deleteByKey(key);}
		int res = 0;
		try {
			if(log.getActionName() == null) {log.setActionName("删除[KEY="+Arrays.toString(key)+"]对象");}
			res = this.deleteByKey(key);
		} finally {
			log.setResult(res > 0 ?"成功":"失败");
		}
		return res;
	}
	
	public int deleteByCondtion(T condtion) {
		return getDao().deleteByKey(condtion);
	}
	
	public int deleteByCondtion(T condtion,SysLog log) {
		if(condtion == null || log == null) {return this.deleteByCondtion(condtion);}
		int res = 0;
		try {
			if(log.getActionName() == null) {log.setActionName("删除["+condtion.getClass()+"]对象");}
			res = this.deleteByCondtion(condtion);
		} finally {
			log.setResult(res > 0 ?"成功":"失败");
		}
		return res;
	}
	
	public int updateByKey(T condtion) {
		return getDao().updateByKey(condtion);
	}
	
	public int updateByKey(T condtion,SysLog log) {
		if(condtion == null || log == null) {return this.updateByKey(condtion);}
		int res = 0;
		try {
			if(log.getActionName() == null) {log.setActionName("更新["+condtion.getClass()+"]对象");}
			res = this.updateByKey(condtion);
		} finally {
			log.setResult(res > 0 ?"成功":"失败");
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public int saveOrUpdate(T t) {
		Integer id = 0;
		try {
			Class<?> clz = t.getClass();
			id = (Integer)clz.getMethod("getId").invoke(t);
		} catch (Exception e) {
			LOGGER.warn("获取对象主键值失败!");
		}
		if(id != null && id > 0) {
			return this.updateByKey(t);
		} 
		return this.insertEntry(t);
	}
	
	public int saveOrUpdate(T t,SysLog log) {
		if(t == null || log == null) {return this.saveOrUpdate(t);}
		int res = 0;
		try {
			if(log.getActionName() == null) {log.setActionName("更新或保存["+t.getClass()+"]对象");}
			res = this.saveOrUpdate(t);
		} finally {
			log.setResult(res > 0 ?"成功":"失败");
		}
		return res;
	}
	
	public T selectEntry(KEY key) {
		return getDao().selectEntry(key);
	}
	
	public List<T> selectEntryList(KEY...key) {
		return getDao().selectEntryList(key);
	}
	
	public List<T> selectEntryList(T condtion) {
		return getDao().selectEntryList(condtion);
	}
	
	public int selectEntryListCount(T condtion) {
		return getDao().selectEntryListCount(condtion);
	}
	
	public Page<T> selectPage(T condtion, Page<T> page) {
		try {
			Class<?> clz = condtion.getClass();
			clz.getMethod("setStartIndex", Integer.class).invoke(condtion, page.getStartIndex());
			clz.getMethod("setEndIndex", Integer.class).invoke(condtion, page.getEndIndex());
		} catch (Exception e) {
			throw new AppException("设置分页参数失败", e);
		}
		Integer size = getDao().selectEntryListCount(condtion);
		if(size == null || size <= 0) {
			return page;
		}
		page.setTotalCount(size);
		page.setPageCount();
		page.setResult(this.selectEntryList(condtion));
		return page;
	}
}
