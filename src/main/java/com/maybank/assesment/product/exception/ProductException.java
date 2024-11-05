package com.maybank.assesment.product.exception;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ProductException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private final String errorCode;
    private final String message_en;
    private final String message_ms;
    private final HttpStatus httpStatus;
    
    public ProductException(String errorCode, String messageEn, String messageMs, HttpStatus status) {
    	this.errorCode = errorCode;
    	this.message_en = messageEn;
    	this.message_ms = messageMs;
    	this.httpStatus = status;
    }

}
