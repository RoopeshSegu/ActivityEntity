package com.tacx.activity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(ActivityEntityException.class)
	public final ResponseEntity<Object> handleAllExceptions(ActivityEntityException ex) {
		return new ResponseEntity<Object>(ex.getCustomMessage(), HttpStatus.NOT_FOUND);
	}
}
