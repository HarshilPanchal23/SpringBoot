package com.example.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    ACCESS_DENIED("Access denied"),
    SOMETHING_WENT_WRONG("Something went wrong"),
    USER_EXISTS("User with email '%s' already Exists", "USER_EXISTS"),

    USER_WITH_ID_NOT_FOUND("User with Id not found", "USER_WITH_ID_NOT_FOUND");


    private final String value;

    private String message;


}
