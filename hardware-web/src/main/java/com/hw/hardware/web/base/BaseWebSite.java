package com.hw.hardware.web.base;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.hw.hardware.common.Constants;
import com.hw.hardware.common.exception.AppException;
import com.hw.hardware.common.spring.velocity.BaseController;
import com.hw.hardware.common.tools.CookieUtils;
import com.hw.hardware.domain.User;
import com.hw.hardware.domain.base.Message;
import com.hw.hardware.service.RedisService;
import com.jd.common.struts.context.LoginContext;
import com.jd.common.struts.interceptor.dotnet.DotnetAuthenticationUtil;
import com.jd.common.struts.interceptor.dotnet.FormsAuthenticationTicket;

public class BaseWebSite extends BaseController {
    protected static final int SUCCESS_CODE = 1;
    protected static final int FAILURE_CODE = 0;
    
    /** 
	 * 操作结果：成功标识
	 */
    protected static final String SUCCESS = "success";
	/** 
	 * 操作结果：失败标识
	 */
    protected static final String FAIL = "failure";

    @Resource protected RedisService redisService;
	
	protected ModelAndView toError(Message msg) {
		return toResult(VELOCITY_ERROR_VIEW, msg);
	}
	
	/**
     * 获取登录用户
     * @return
     */
    protected User getLoginUser() {
        HttpServletRequest request = getRequest();
        User user = (User)request.getAttribute(Constants.LOGIN_USER_ONLINE_KEY);
        if(user == null) {
            user = redisService.loadLoginUser(CookieUtils.getCookieValue(request, Constants.LOGIN_USER_COOKIE_NAME));
        }
        return user;
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
     * 判断有无登录ERP
     * @param request
     * @return
     */
    public boolean parseDotnetTicket(HttpServletRequest request) {
        String keys = Constants.getSystemCfg("hrm.auth.cookie.name");
        String cookieValue = CookieUtils.getCookieValue(request, keys);//获取多域下的cookie信息
        if (StringUtils.isNotBlank(cookieValue)) {
            FormsAuthenticationTicket ticket = null;
            try {
                ticket = DotnetAuthenticationUtil.getFormsAuthenticationTicket(cookieValue, Constants.getSystemCfg("hrm.login.auth.key"));
            } catch (Exception e) {
                LOGGER.error("decrypt dotnet cookie error!", e);
            }
            if (ticket != null && !ticket.isExpired()) {
                request.setAttribute(LoginContext.HTTP_DOTNET_LOGIN_TICKET_CONTEXT, ticket);
                return true;
            } else {
                LOGGER.debug("tick error or ticket expired!");
            }
        }
        return false;
    }
}
