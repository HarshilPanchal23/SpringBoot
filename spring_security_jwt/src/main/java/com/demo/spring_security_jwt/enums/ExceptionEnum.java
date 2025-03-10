package com.demo.spring_security_jwt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    ACCESS_DENIED("Access denied","ACCESS_DENIED"),
    SOMETHING_WENT_WRONG("Something went wrong","SOMETHING_WENT_WRONG"),
    PASSWORD_NOT_MATCHED("Password not matched","PASSWORD_NOT_MATCHED"),
    ACCOUNT_IS_LOCKED("Account is locked","ACCOUNT_IS_LOCKED"),
    USER_EXISTS("User with email '%s' already Exists", "USER_EXISTS"),

    USER_WITH_ID_NOT_FOUND("User with id not found", "USER_WITH_ID_NOT_FOUND"),

    USER_DELETED_WITH_ID("User Deleted Successfully", "USER_DELETED_WITH_ID"),

    NO_PROPERTY_WITH_NAME_FOUND_IN_DATABASE("No Property Found in Database", "NO_PROPERTY_WITH_NAME_FOUND_IN_DATABASE"),

    USER_WITH_EMAIL_NOT_FOUND("User with email not found", "USER_WITH_EMAIL_NOT_FOUND"),

    GRADE_WITH_ID_NOT_FOUND("Grade with id not found", "GRADE_WITH_ID_NOT_FOUND"),
    ROLE_WITH_ID_NOT_FOUND("Role with id not found", "ROLE_WITH_ID_NOT_FOUND");


    private final String value;

    private String message;


}
