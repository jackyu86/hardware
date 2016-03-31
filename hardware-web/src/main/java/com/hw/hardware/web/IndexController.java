package com.hw.hardware.web;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hw.hardware.common.Constants;
import com.hw.hardware.common.spring.secret.AuthType;
import com.hw.hardware.common.spring.secret.Authentication;
import com.hw.hardware.common.tools.CookieUtils;
import com.hw.hardware.common.tools.EncryptUtils;
import com.hw.hardware.common.tools.ValidateUtils;
import com.hw.hardware.domain.User;
import com.hw.hardware.service.RedisService;
import com.hw.hardware.service.UserService;
import com.hw.hardware.web.base.BaseWebSite;

/**
 * 首页
 * @author cfish
 * @since 2013-09-09
 */
@Controller("indexController")
@RequestMapping(value = "/", method = {RequestMethod.GET,RequestMethod.POST})
public class IndexController extends BaseWebSite {
	@Resource private UserService userService;
	@Resource private RedisService redisService;

	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
    @Authentication(type=AuthType.NONE)
    public ModelAndView login(String userName,String password,String id) {
		Result result = new Result();
        result.addDefaultModel("returnUrl", id); 
        if(getLoginUser() != null) {
            sendRedirect(StringUtils.isEmpty(id) ? "welcome/" : "welcome?id=" + id);
            return null;//已经有用户登录成功啦
        }
        //系统登录界面包含用户名密码
        if(StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
            boolean res = userService.verify(userName, password);
            if(res) {
                String loginKey = EncryptUtils.desEncode(CookieUtils.randomValue(userName));
                CookieUtils.addCookie(getResponse(), Constants.LOGIN_USER_COOKIE_NAME, loginKey);
                sendRedirect(StringUtils.isEmpty(id) ? "welcome/" : "welcome?id=" + id);
                return null;
            } 
        }
        //判断有无登录ERP系统
        if(parseDotnetTicket(getRequest())) {
            sendRedirect(StringUtils.isEmpty(id) ? "welcome/" : "welcome?id=" + id);
            return null;
        }
		return loginPage(result);
	}
	
	@RequestMapping("/main")
	@Authentication(type=AuthType.PUBLIC)
    public ModelAndView main(){
		Result result = new Result();
        return toResult("main", result);
    }
	
	@RequestMapping(value="welcome",method={RequestMethod.GET,RequestMethod.POST})
    @Authentication(type=AuthType.PUBLIC)
	public ModelAndView welcome(String id){
		User user = getLoginUser();
		LOGGER.info("statPV=1 statUV=" + user.getName());
		Result result = new Result();
        return toResult("index", result);
	}

	@RequestMapping(value="logout")
    @Authentication(type=AuthType.NONE)
    public ModelAndView logout() {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();
        String key = CookieUtils.getCookieValue(request, Constants.LOGIN_USER_COOKIE_NAME);
        User user = redisService.userLogout(key);
        if(user != null) {
            LOGGER.info("用户 {}[{}] 登出",user.getName(),user.getRealName());
        }else{
        	LOGGER.info("user is null");
        }
        CookieUtils.delAllCookie(request, response);
        String keys = Constants.getSystemCfg("hrm.auth.cookie.name");
        CookieUtils.delCookie(response, ".jd.com", keys);
        Result result = new Result();
        return toJSON(result); 
    }

	private ModelAndView loginPage(Result result) {
        HttpServletRequest request = getRequest();
        boolean ssoSupport = Constants.getBooleanCfg("login.sso.support",true);
        boolean localLogin = !ssoSupport || "false".equals(request.getParameter("sso")) || ValidateUtils.checkIP(request.getServerName());
        String returnUrl = CookieUtils.getRequestURL(request);
        if(!localLogin) {
            String loginUrl = Constants.getSystemCfg("hrm.login.auth.address") +"?returnUrl=" + EncryptUtils.encodeURI(returnUrl);
            sendRedirect(loginUrl);
            return null;
        }
        return toResultSkipLayout("login", result);
    }
}
