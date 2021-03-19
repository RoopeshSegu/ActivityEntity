package com.tacx.activity.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ActivityEntityException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String customMessage;

	public ActivityEntityException (String customMessage) {
		this.customMessage = customMessage;
	}

}
