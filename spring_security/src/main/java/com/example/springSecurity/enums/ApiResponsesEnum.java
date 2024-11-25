package com.example.springSecurity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum ApiResponsesEnum {

    ALL_USER_RETRIEVAL_SUCCESS("All User retrieval Successfully", "ALL_USER_RETRIEVAL_SUCCESS"),

    USER_CREATION_SUCCESSFULLY("User created successfully", "USER_CREATION_SUCCESSFULLY"),

    USER_DELETED_SUCCESSFULLY("User Deleted Successfully", "USER_DELETED_SUCCESSFULLY"),

    USER_FETCH_SUCCESSFULLY("User fetch successfully", "USER_FETCH_SUCCESSFULLY"),
    GRADE_INSERTED_SUCCESSFULLY("Grade inserted Successfully", "GRADE_INSERTED_SUCCESSFULLY"),

    GRADE_UPDATED_SUCCESSFULLY("Grade Updated Successfully", "GRADE_UPDATED_SUCCESSFULLY"),

    GRADE_RETRIEVAL_SUCCESSFULLY("Grade retrieval Successfully", "GRADE_RETRIEVAL_SUCCESSFULLY");

    private final String value;

    private String message;
}
