package com.hw.hardware.common.spring.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import com.hw.hardware.common.Constants;
import com.hw.hardware.common.spring.ApplicationContextUtil;
import com.hw.hardware.common.tools.DateUtils;

/**
 * Velocity代理类
 * @author cfish
 * @since 2013-09-09
 */
public class VelocityProxy {
	
	private static VelocityEngine velocityEngine;
	private static String loadpath;

	/**
	 * @description 获取Velocity引擎
	 * @since 2013/11/12
	 */
	private static VelocityEngine getEngine(){
		//从Spring上下文对象中获取Velocity初始化对象
		if(velocityEngine == null){
			VelocityConfigurer velocityConfigurer = ApplicationContextUtil.getBean("velocityConfigurer");
			velocityEngine = velocityConfigurer.getVelocityEngine();
		}
		return velocityEngine;
	}
	
	/**
	 * 获取Velocity页面
	 * @param data
	 * @param vm
	 * @return
	 * @throws IOException 
	 */
	public static String merge(Map<String,Object> data, String vm) throws IOException {
		StringWriter w = null;
		try{
			Template template = getEngine().getTemplate(vm, Constants.DEFAULT_ENCODING);
			VelocityContext context = new VelocityContext(data);
			w = new StringWriter();
			template.merge(context, w);
			return w.toString();
		}finally{
			if(w != null){
				w.close();
			}
		}
	}
	
	/**
	 * @description 获取项目的绝对路径
	 * @author cdliliang 
	 * @since 2013/11/12
	 */
	public static String getVMDir() throws Exception{
		if(loadpath == null){
			ServletContext context = ApplicationContextUtil.getBean(ServletContext.class);
			loadpath = context.getRealPath("/WEB-INF/vm");
		}
		return loadpath;
    }
	
	/**
	 * @description 解析Velocity模板
	 * @param data 数据参数(组件,数据库,项目信息...)
	 * @param vm 模板路径
	 * @param encoding 编码
	 * @return 解析后的vm页面内容
	 * @author cdliliang 
	 * @since 2013/11/12
	 * @throws Exception 
	 * @throws ParseErrorException 
	 * @throws ResourceNotFoundException 
	 */
	public static String merge(Map<String,Object> data, String vm, String encoding) throws ResourceNotFoundException, ParseErrorException, Exception {
		Template template = getEngine().getTemplate(vm, encoding);
		VelocityContext context = new VelocityContext(data);
		context.put("DateUtils", DateUtils.class);
		context.put("cmd", "$");
		context.put("cmd2", "#");
		StringWriter writer = null;
		try {
			writer = new StringWriter();
			template.merge(context, writer);
			return writer.toString();
		} finally{
			if(writer != null )
				writer.close();
		}
	}
	/**
	 * @description 解析Velocity字符串
	 * @param data 数据参数(组件,项目信息...)
	 * @param content Velocity字符串
	 * @return	解析后的vm字符串内容
	 * @throws ResourceNotFoundException
	 * @throws ParseErrorException
	 * @throws Exception
	 */
	public static String mergeContent(Map<String,Object> data, String content) throws ResourceNotFoundException, ParseErrorException, Exception {
		VelocityContext context = new VelocityContext(data);
		StringWriter writer = null;
		try {
			writer = new StringWriter();
			getEngine().evaluate(context, writer, "", content);
			return writer.toString();
		} finally{
			if(writer != null )
				writer.close();
		}
	}

}
