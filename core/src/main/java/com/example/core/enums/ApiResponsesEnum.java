package com.example.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum ApiResponsesEnum {

    ALL_USER_RETRIEVAL_SUCCESS("All User retrieval Success", "ALL_USER_RETRIEVAL_SUCCESS"),

    USER_CREATION_SUCCESSFULLY("User created successfully", "USER_CREATION_SUCCESSFULLY"),
    USER_DELETED_SUCCESSFULLY("User Deleted Successfully", "USER_DELETED_SUCCESSFULLY");


    private final String value;

    private String message;
}
