package com.hw.hardware.web.intercept;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hw.hardware.common.Constants;
import com.hw.hardware.common.ServletContextUtil;
import com.hw.hardware.common.spring.secret.AuthCondtion;
import com.hw.hardware.common.spring.secret.AuthType;
import com.hw.hardware.common.spring.secret.Authentication;
import com.hw.hardware.common.tools.CookieUtils;
import com.hw.hardware.common.tools.EncryptUtils;
import com.hw.hardware.domain.User;
import com.hw.hardware.service.RedisService;
import com.hw.hardware.service.UserService;
import com.hw.hardware.web.IndexController;
import com.jd.common.struts.context.LoginContext;
import com.jd.common.struts.interceptor.dotnet.FormsAuthenticationTicket;

/**
 * SSO
 * @author cfish
 * @since 2013-4-23
 */
public class JDSSOInterceptor extends HandlerInterceptorAdapter {

    protected final Logger LOGGER = LoggerFactory.getLogger(JDSSOInterceptor.class);
    @Resource protected UserService userService;
    @Resource protected RedisService redisService;
    @Resource protected IndexController indexController;

    protected boolean authentication(Authentication auth) throws IOException {
        ServletContextUtil context = ServletContextUtil.getContext();
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();
        if(auth != null && auth.type() == AuthType.NONE) {
            return true;//不需要登录和权限认证的页面
        }
        if(auth != null && auth.type() == AuthType.TOKEN) {
            // Token 认证
            // 若注解中指定了code, 则以code进行token验证,
            // 若注解中没有指定code, 则以sccd-production.properties文件中的配置项jone.auth.token进行token验证
            // jone.auth.token可以设置为数组,以英文","号分割，因此token中不能包含英文","号.
            if( auth.code() != null && auth.code().length > 0 ) {
                return verifyToken(request, response, auth.code());//Token认证
            }
            else {
                String[] codes = Constants.getSystemCfg("jone.auth.token", "26FFE547-B28C-499A-8E9B-2323WE")
                        .trim().split(",");
                return verifyToken(request, response, codes);
            }
        }
        User user = getLoginUser();
        if(user == null) {//余下的都需要用户登录
            String loginUrl = request.getContextPath()+"/";
            if(request.getQueryString() != null) {
                loginUrl = loginUrl + ("?"+request.getQueryString());
            }
            indexController.sendRedirect(loginUrl);
            return false;
        }

        if(auth == null && !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return false;//没有配置权限也不是管理员直接拒绝
        }

        if(auth.type() == AuthType.PUBLIC || user.isAdmin()) {return true;}//公共资源或管理员访问

        boolean result = user.verify(auth.condtion() == AuthCondtion.OR,auth.code());;
        if(!result) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
        }
        return result;//权限码认证
    }

    /**
     * 获取登录用户
     * @return
     */
    protected User getLoginUser() {
        try {
            ServletContextUtil context = ServletContextUtil.getContext();
            HttpServletRequest request = context.getRequest();
            HttpServletResponse response = context.getResponse();

            User user = (User)request.getAttribute(Constants.LOGIN_USER_COOKIE_NAME);
            if(user != null) {return user;}
            String cookieValue = CookieUtils.getCookieValue(request, Constants.LOGIN_USER_COOKIE_NAME);
            user = redisService.loadLoginUser(cookieValue);
            request.setAttribute(Constants.LOGIN_USER_ONLINE_KEY,user);
            if(user != null) {return user;}

            String userName = null,des = null;//登录的用户名
            if(StringUtils.isNotEmpty(cookieValue) && StringUtils.isNotEmpty(des = EncryptUtils.desDecode(cookieValue))) {
                String[] c = des.split("\\Q"+CookieUtils.COOKIE_SPLIT_KEY+"\\E");
                if(c!=null&&c.length==3) {userName = c[1];}
            } else if(indexController.parseDotnetTicket(request)) {
                userName = ((FormsAuthenticationTicket)request.getAttribute(LoginContext.HTTP_DOTNET_LOGIN_TICKET_CONTEXT)).getUsername();
                cookieValue = EncryptUtils.desEncode(CookieUtils.randomValue(userName));
            }
            user = userService.loginUser(userName);
            if(user != null) {
                redisService.addLoginUser(cookieValue, user,CookieUtils.getIpAddr(request));
                CookieUtils.addCookie(response, Constants.LOGIN_USER_COOKIE_NAME, cookieValue);
            }
            request.setAttribute(Constants.LOGIN_USER_ONLINE_KEY,user);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * token验证
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    private boolean verifyToken(HttpServletRequest request,HttpServletResponse response,String...code) throws IOException {
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token) || code == null || code.length<=0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied, token:" + token + " is illegal");
            return false;//不允许访问
        }
        token = token.trim();
        //通过资源码查询该token有无访问权限
        for( String singleCode : code) {
            if( StringUtils.isNotBlank(singleCode) && token.equals(singleCode.trim())) {
                return true;
            }
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied, token:" + token + " is illegal");
        return false;
    }

}
