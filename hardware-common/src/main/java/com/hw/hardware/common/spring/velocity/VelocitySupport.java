package com.hw.hardware.common.spring.velocity;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import com.hw.hardware.common.exception.AppException;

/**
 * VelocitySupport
 * @author cfish
 * @since 2013-09-09
 */
public abstract class VelocitySupport {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	//Velocity参数
	public final static String VELOCITY_DETAULT_LAYOUT = "layout/default";
	public final static String VELOCITY_ERROR_VIEW = "common/error";
	public final static String VELOCITY_VM_SUFFIX = ".vm";
	public final static String VELOCITY_SCREEN_CONTENT = "screen_content";
	//请求后缀
	public final static String HTML_SUFFIX = ".html";
	public final static String JSON_SUFFIX = ".json";
	public final static String XML_SUFFIX = ".xml";
	public final static String TXT_SUFFIX = ".txt";
	
	@Resource private VelocityConfigurer velocityConfigurer;
	
	/**
	 * 输出VM模板,自定义布局
	 * @param layout 布局
	 * @param view 视图
	 * @param model 数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ModelAndView toVM(String layout,String view,Object model){
		VelocityEngine engine = velocityConfigurer.getVelocityEngine();
		// 判断模板是否存在
		String viewVM = view + VELOCITY_VM_SUFFIX;
		boolean exists = engine.resourceExists(viewVM);
		if (!exists) {
			throw new AppException("未找到指定视图[" + view + "].");
		}
		Map<String, Object> context = getDefaultContext();
		if(context == null) {
			context = new HashMap<String, Object>();
		}
		// 添加默认数据
		if (model instanceof Map) {
			context.putAll((Map<String, Object>) model);
		} else if(model instanceof Result) {
			context.putAll(((Result) model).getMap());
		} else {
			context.put(Result.DEFAULT_KEY, model);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("process vm==============" + viewVM);
		}
		if (layout == null) {// 跳过模板直接输出
			return new ModelAndView(view, context);
		}
		// 判断布局模板是否存在
		exists = engine.resourceExists(layout + VELOCITY_VM_SUFFIX);
		if (!exists) {
			throw new AppException("未找到指定布局模板[" + layout + "].");
		}
		// 输入输出编码
		Template bodyVM = engine.getTemplate(viewVM, (String)engine.getProperty(VelocityEngine.INPUT_ENCODING));
		// 合并screen_content
		StringWriter screenContent = new StringWriter();
		bodyVM.merge(new VelocityContext(context), screenContent);
		// 添加到默认布局中
		context.put(VELOCITY_SCREEN_CONTENT, screenContent.toString());
		return new ModelAndView(layout, context);
	}
	
	
	/**
	 * velocity默认数据
	 * @return
	 */
	protected abstract Map<String, Object> getDefaultContext();
	
	/**
	 * 页面响应结果
	 * @author cfish
	 * @since 2013-09-09
	 */
	public class Result {
		public final static String DEFAULT_KEY = "value";
		private Map<String, Object> data = new HashMap<String, Object>();
		
		/**
		 * 添加数据
		 * @param key
		 * @param value
		 */
		public void addDefaultModel(String key, Object value) {
			data.put(key, value);
		}
	
		/**
		 * 获取数据
		 * @param key
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public <E> E get(String key) {
			return (E)data.get(key);
		}
	
		/**
		 * 获取Map数据
		 * @return
		 */
		public Map<String, Object> getMap() {
			return data;
		}
	}
}
