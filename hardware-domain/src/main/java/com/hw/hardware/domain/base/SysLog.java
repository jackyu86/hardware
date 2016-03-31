package com.hw.hardware.domain.base;

/**
 * 系统操作日志
 * @author cfish
 * @since 2013-09-09
 */
public class SysLog extends BaseDomain {
	private static final long serialVersionUID = 1L;
	private String uri;//请求地址
	private String parameter;//请求参数
	private String actionName;//动作名称
	private String result;//执行结果
	private String ipAddress;//客户端IP地址
	
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getParameter() {
		return parameter;
	}
	
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	public String getActionName() {
		return actionName;
	}
	
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
