package com.example.manyToOne.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum ApiResponsesEnum {

    ALL_USER_RETRIEVAL_SUCCESS("All User retrieval Success Successfully", "ALL_USER_RETRIEVAL_SUCCESS"),

    USER_CREATION_SUCCESSFULLY("User created successfully", "USER_CREATION_SUCCESSFULLY"),

    USER_DELETED_SUCCESSFULLY("User Deleted Successfully", "USER_DELETED_SUCCESSFULLY"),

    USER_FETCH_SUCCESSFULLY("User fetch successfully", "USER_FETCH_SUCCESSFULLY"),

    ALL_Organization_RETRIEVAL_SUCCESS("All Organization retrieval Successfully ", "ALL_Organization_RETRIEVAL_SUCCESS"),

    Organization_CREATION_SUCCESSFULLY("Organization created successfully", "Organization_CREATION_SUCCESSFULLY"),

    Organization_DELETED_SUCCESSFULLY("Organization Deleted Successfully", "Organization_DELETED_SUCCESSFULLY");

    private final String value;

    private String message;
}
