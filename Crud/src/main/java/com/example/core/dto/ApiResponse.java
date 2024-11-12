package com.example.core.dto;

import com.example.core.enums.CustomStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiResponse {

    private int status;

    private String message;

    private Object data;

    @JsonIgnore
    private HttpStatus httpStatus;

    public ApiResponse(HttpStatus httpStatus, String message, Object data) {
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.message = message;
        this.data = data;
    }

    public ApiResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }

    public ApiResponse(CustomStatusEnum status, String message, Object data) {
        this.status = status.getValue();
        this.message = message;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.data = data;
    }
}
