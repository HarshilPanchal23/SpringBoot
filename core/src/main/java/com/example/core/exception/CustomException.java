package com.example.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;


@Getter

public class CustomException extends Throwable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomException.class);
    private final String message;
    private final HttpStatus httpStatus;

    public  CustomException(String message, HttpStatus httpStatus) {
        LOGGER.info("message : {}, httpStatus : {}",message,httpStatus);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
