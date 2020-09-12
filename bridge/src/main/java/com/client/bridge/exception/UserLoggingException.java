package com.client.bridge.exception;

import org.springframework.http.HttpStatus;

public class UserLoggingException extends GenericException implements CommonException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6664020609555381727L;

	public UserLoggingException(String message, HttpStatus status) {
		super(message, status);
	}
	
	public UserLoggingException() 
	{
		super("There is not user logged into the application, please LogIn first", HttpStatus.CONFLICT);
	}
	
}
