package com.hw.hardware.web.intercept;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.hw.hardware.domain.base.Message;
import com.hw.hardware.web.ResourceController;

/**
 * 页面异常处理
 * @author cfish
 * @since 2013-09-09
 */
public class ExceptionResolver implements HandlerExceptionResolver {
	private final Logger LOG = LoggerFactory.getLogger(ExceptionResolver.class);
	@Resource private ResourceController resourceController;

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object bean, Exception ex) {
		LOG.error("系统发生异常", ex);
		Message message = Message.failure(ex);
		return resourceController.error(message);
	}
}

