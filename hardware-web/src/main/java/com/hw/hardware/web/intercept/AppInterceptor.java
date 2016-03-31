package com.hw.hardware.web.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;

import com.hw.hardware.common.Constants;
import com.hw.hardware.common.ServletContextUtil;
import com.hw.hardware.common.spring.secret.Authentication;
import com.hw.hardware.domain.User;


/**
 * 默认拦截器
 * @author cfish
 * @since 2013-09-09
 */
public class AppInterceptor extends JDSSOInterceptor {

    @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setCharacterEncoding(Constants.DEFAULT_ENCODING);
		response.setCharacterEncoding(request.getCharacterEncoding());
		if (!(handler instanceof HandlerMethod)) {
            request.setAttribute("RESOURCE_REQUEST", true);
            return true;
        }

		if ((handler instanceof HandlerMethod) == false) {
			return true;// 排除其他请求
		}

		//设置本地线程变量
		ServletContextUtil context = ServletContextUtil.getContext();
		context.setRequest(request);
		context.setResponse(response);

		//安全认证
        HandlerMethod method = (HandlerMethod)handler;
        boolean result = authentication(method.getMethodAnnotation(Authentication.class));
        if(result) {//认证通过
            request.setAttribute("MONITOR.START.TIME", System.currentTimeMillis());
        }
        return result;
	}

    @Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	    Long startTime = (Long)request.getAttribute("MONITOR.START.TIME");
        if(LOGGER.isDebugEnabled() && startTime != null && startTime > 0) {
            //记录用户访问日志
            long cost = System.currentTimeMillis() - startTime;
            User user = (User)request.getAttribute(Constants.LOGIN_USER_COOKIE_NAME);
            String uri = request.getRequestURI();
            String queryString = (request.getQueryString() != null)?request.getQueryString():"";
            String userName = "anonymous";
            if(user != null) {userName = user.getName();}
            Object[] params = new Object[]{userName,request.getMethod(),uri,queryString,cost};
            //要求必须[ user={} ]前后2个空格
            LOGGER.debug("[PV/UV][ user={} ][{}={}] [{}] [{}]",params);
        }
	}

}
