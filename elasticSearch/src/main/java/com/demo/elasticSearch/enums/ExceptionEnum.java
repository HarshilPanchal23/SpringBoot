package com.demo.elasticSearch.enums;

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

    USER_WITH_ID_NOT_FOUND("User with id not found", "USER_WITH_ID_NOT_FOUND"),

    USER_DELETED_WITH_ID("User Deleted Successfully", "USER_DELETED_WITH_ID"),

    NO_PROPERTY_WITH_NAME_FOUND_IN_DATABASE("No Property Found in Database", "NO_PROPERTY_WITH_NAME_FOUND_IN_DATABASE"),

    USER_WITH_EMAIL_NOT_FOUND("User with email not found","USER_WITH_EMAIL_NOT_FOUND" );


    private final String value;

    private String message;


}
