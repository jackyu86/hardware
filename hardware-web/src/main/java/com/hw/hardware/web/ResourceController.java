package com.hw.hardware.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hw.hardware.common.spring.secret.AuthType;
import com.hw.hardware.common.spring.secret.Authentication;
import com.hw.hardware.common.spring.velocity.BaseController;
import com.hw.hardware.domain.base.Message;

/**
 * 资源整合
 * @author cfish
 * @since 2013-09-09
 */
@Controller("resourceController")
@RequestMapping(value = "/", method = RequestMethod.GET)
public class ResourceController extends BaseController {

	
	@RequestMapping(value="resourceNotFound")
	@Authentication(type=AuthType.NONE)
	public ModelAndView resourceNotFound(){
		return error(Message.create("resourceNotFound", "指定资源未找到!"));
	}
	
	public ModelAndView error(Message message){
		return toResult(VELOCITY_ERROR_VIEW,message);
	}
}
