package com.demo.spring_security_jwt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;


public class CustomException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomException.class);

    private static final long serialVersionUID = 1L;

    private final String message;
    private final HttpStatus httpStatus;

    public  CustomException(String message, HttpStatus httpStatus) {
        LOGGER.info("message : {}, httpStatus : {}",message,httpStatus);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
