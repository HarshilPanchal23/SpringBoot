package com.example.core.dto;

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


    public ApiResponse(HttpStatus httpStatus, String value, UserRequestDto responseDTO) {
    }

}
