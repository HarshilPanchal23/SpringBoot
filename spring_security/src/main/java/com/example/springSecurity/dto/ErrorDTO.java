package com.example.springSecurity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorDTO {

    private Long timestamp;
    private int status;
    private Object error;
    private String message;
    private String path;

    @JsonIgnore
    private HttpStatus httpStatus;

    public ErrorDTO(HttpStatus httpStatus, Long timestamp, String message, String path) {
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.message = message;
        this.path = path;
    }

    public ErrorDTO(HttpStatus httpStatus, Long timestamp, String message, String path, List<String> error) {
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.error = error;
        this.message = message;
        this.path = path;
    }

}
