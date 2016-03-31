package com.hw.hardware.common.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;

/**
 * 对象工具类
 * @author cfish
 * @since 2013-09-09
 */
public class ObjectUtils {

	private final static Logger LOGGER = LoggerFactory.getLogger(ObjectUtils.class);

	/**
	 * 对象转JSON
	 * @param object
	 * @return
	 */
	public static String object2json(Object object) {
		if (object == null) {
			return null;
		}
		return JSON.toJSONString(object);
	}

	/**
	 * 对象转XML
	 * @param object
	 * @return
	 */
	public static String object2xml(Object object) {
		if (object == null) {
			return null;
		}
		XStream stream = new XStream();
		stream.autodetectAnnotations(true);
		return stream.toXML(object);
	}
	
	/**
	 * 复制对象
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cpoy(Object obj) {
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out= new ObjectOutputStream(byteOut);
			out.writeObject(obj);
			
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			return (T)in.readObject();
		} catch (Exception e) {
			LOGGER.warn("对象拷贝失败",e.getMessage());
		}
		return null;
	}
	
	/**
	 * 判断对象是否为Null
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		return obj == null;
	}
}
