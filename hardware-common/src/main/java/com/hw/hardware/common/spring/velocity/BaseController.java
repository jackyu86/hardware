package com.hw.hardware.common.spring.velocity;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Strings;
import com.hw.hardware.common.Constants;
import com.hw.hardware.common.ServletContextUtil;
import com.hw.hardware.common.exception.AppException;
import com.hw.hardware.common.spring.CustomDateEditor;
import com.hw.hardware.common.tools.DateUtils;
import com.hw.hardware.common.tools.FileUtils;
import com.hw.hardware.common.tools.ObjectUtils;
import com.hw.hardware.common.tools.StreamUtils;


/**
 * BaseController
 * @author cfish
 * @since 2013-09-09
 */
public class BaseController extends VelocitySupport {
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
	}
	
	/**
	 * 获取请求对象
	 * @return
	 */
	protected HttpServletRequest getRequest() {
		return ServletContextUtil.getContext().getRequest();
	}
	
	/**
	 * 获取响应对象
	 * @return
	 */
	protected HttpServletResponse getResponse() {
		return ServletContextUtil.getContext().getResponse();
	}
	
	/**
	 * 跳转
	 * @param location
	 */
	public void sendRedirect(String location) {
		try {
			HttpServletResponse response = getResponse();
			response.sendRedirect(location);
		} catch (Exception e) {
			throw new AppException(e);
		}
	}
	
	/**
	 * 输出文本
	 * @param txt
	 * @param contextType
	 * @return
	 */
	protected ModelAndView write(String txt,String contextType) {
		try {
			if(Strings.isNullOrEmpty(txt)){return null;}
			HttpServletResponse response = getResponse();
			response.setContentType(contextType);
			StreamUtils.writeData(txt.getBytes(Constants.DEFAULT_ENCODING), response.getOutputStream());
		} catch (Exception ex) {
			throw new AppException(ex);
		}
		return null;
	}
	
	/**
	 * 输出JSON对象
	 * @param model
	 * @return
	 */
	protected ModelAndView toJSON(Object model) {
		return write(ObjectUtils.object2json(model),"application/json");
	}
	
	/**
	 * 输出JSON对象(兼容IE弹出下载框)
	 * @param model
	 * @return
	 */
	protected ModelAndView toIEJSON(Object model) {
		return write(ObjectUtils.object2json(model),"text/html");
	}
	
	/**
	 * 输出XML对象
	 * @param model
	 */
	protected ModelAndView toXML(Object model) {
		return write(ObjectUtils.object2xml(model),"text/xml");
	}
	
	/**
	 * 输出TEXT文本
	 * @param model
	 */
	protected ModelAndView toText(String text) {
		return write(text,"text/plain");
	}
	
	/**
	 * 返回结果可指定模板
	 * @param layout :为Null则不包含布局模板
	 * @param view
	 * @param model
	 * @return
	 */
	protected ModelAndView toResult(String layout, String view, Object model) {
		ModelAndView mv = null;
		String uri = getRequest().getRequestURI();
		if (uri.endsWith(JSON_SUFFIX)) {
			mv = toJSON(model);
		} else if (uri.endsWith(XML_SUFFIX)) {
			mv = toXML(model);
		} else if (uri.endsWith(TXT_SUFFIX)) {
			mv = toText(model != null ? model.toString() : "");
		} else {// 默认HTML返回
			mv = toVM(layout, view, model);
		}
		return mv;
	}
	
	/**
	 * 返回结果,默认模板
	 * @param view
	 * @param model
	 * @return
	 */
	protected ModelAndView toResult(String view, Object model) {
		return toResult(VELOCITY_DETAULT_LAYOUT, view, model);
	}
	
	/**
	 * 返回结果,跳过模板
	 * @param view
	 * @param model
	 * @return
	 */
	protected ModelAndView toResultSkipLayout(String view, Object model) {
		return toResult(null, view, model);
	}
	
	/**
	 * 默认页面内容
	 * @return
	 */
	protected Map<String, Object> getDefaultContext() {
		Map<String, Object> context = new HashMap<String, Object>();
		HttpServletRequest request = getRequest();
		//将request中的对象放入页面数据中
		Enumeration<?> attrsEnum = request.getAttributeNames();
		while(attrsEnum.hasMoreElements()) {
			String key = (String)attrsEnum.nextElement();
			context.put(key, request.getAttribute(key));
		}
		//将request的请求参数放入页面数据中
		Enumeration<?> paramsEnum = request.getParameterNames();
		while(paramsEnum.hasMoreElements()) {
			String key = (String)paramsEnum.nextElement();
			String[] val = request.getParameterValues(key);
			if(val != null && val.length == 0) {
				context.put(key, val[0]);
			} else {
				context.put(key, val);
			}
		}
		context.put("request", request);
		context.put("contextPath", request.getContextPath());
		context.put("pageEncoding", Constants.DEFAULT_ENCODING);
		context.put("v", Constants.getSystemCfg("jone.vm.version"));
		//TOOLS
		context.put("DateUtils", DateUtils.class);
		context.put("ObjectUtils", ObjectUtils.class);
		context.put("FileUtils", FileUtils.class);
		context.put("StreamUtils", StreamUtils.class);
		return context;
	}
}
