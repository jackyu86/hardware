package com.hw.hardware.common.tools;

import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie操作
 * @author cfish
 * @since 2013-09-09
 */
public class CookieUtils {

	public static final String COOKIE_SPLIT_KEY = "$$";
	
	/**
	 * 在value的前后随机加上1000以内的数字
	 * @param value
	 * @return
	 */
	public static String randomValue(String value) {
		StringBuffer str = new StringBuffer();
		Random random = new Random();
		str.append(random.nextInt(1000)).append(COOKIE_SPLIT_KEY);
		str.append(value);
		str.append(COOKIE_SPLIT_KEY).append(random.nextInt(1000));
		return str.toString();
	}
	
	/**
	 * 获取Cookie值,如果没有获取到自动从请求参数中获取
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String key) {
		try {
			Cookie[] cs = request.getCookies();
			if (cs != null) {
				for (Cookie cookie : cs) {
					if (cookie.getName().equals(key)) {
						return cookie.getValue();
					}
				}
			}
			return request.getParameter(key);// 尝试在请求参数中获取
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 响应Cookie到浏览器
	 * @param response
	 * @param key
	 * @param value
	 */
	public static void addCookie(HttpServletResponse response, String key, String value) {
		try {
			Cookie cookie = new Cookie(key, value);
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			;
		}
	}
	
	/**
	 * 删除指定域下的cookie
	 * @param response
	 * @param domain
	 * @param key
	 */
	public static void delCookie(HttpServletResponse response, String domain,String key) {
		try {
			Cookie cookie = new Cookie(key, null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			if (domain != null) {
				cookie.setDomain(domain);
			}
			response.addCookie(cookie);
		} catch (Exception e) {
			;
		}
	}
	
	/**
	 * 删除所有cookie信息
	 * @param request
	 * @param response
	 */
	public static void delAllCookie(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Cookie[] cs = request.getCookies();
			if (cs != null && cs.length > 0) {
				for (Cookie cookie : cs) {
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
		} catch (Exception e) {
			;
		}
	}
	
	/**
     * 获取访问URL
     * @param request
     * @return
     */
    public static String getRequestURL(HttpServletRequest request){
        StringBuffer url = new StringBuffer(request.getScheme());
        url.append("://").append(request.getServerName()).append(':').append(request.getServerPort());
        url.append(request.getContextPath());
        if(request.getQueryString() != null) {
            url.append("?").append(request.getQueryString());
        }
        return url.toString();
    }
    
	/**
	 * 获取客户端请求IP地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
