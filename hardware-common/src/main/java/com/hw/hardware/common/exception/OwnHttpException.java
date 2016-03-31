package com.hw.hardware.common.exception;

/**
 * AppException
 * @author cfish
 * @since 2013-09-09
 */
public class OwnHttpException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private int code;
	private String message;
	
	public OwnHttpException(int code, String message) {
		super(message);
		this.message = message;
		this.code = code;
	}
	
	public OwnHttpException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
}
