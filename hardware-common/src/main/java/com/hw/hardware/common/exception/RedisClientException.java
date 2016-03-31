package com.hw.hardware.common.exception;

public class RedisClientException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public RedisClientException(){
		super();
	}
	
	public RedisClientException(String message, Exception exception) {
	        super(message, exception);
	}
	
	public RedisClientException( String message ) {
        super(message);
    }

    public RedisClientException( Exception exception) {
        super( exception );
    }
}
