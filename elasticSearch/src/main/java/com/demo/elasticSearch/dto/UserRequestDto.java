package com.demo.elasticSearch.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class UserRequestDto {

    private Long id;

    @NotNull(message = "MISSING_USER_NAME")
    @NotEmpty(message = "INVALID_FIRST_NAME")
    private String firstName;

    @NotNull(message = "MISSING_LAST_NAME")
    @NotEmpty(message = "INVALID_LAST_NAME")
    private String lastName;

    @NotNull(message = "MISSING_EMAIL")
    @NotEmpty(message = "INVALID_EMAIL")
    private String email;

    @NotNull(message = "MISSING_PASSWORD")
    @NotEmpty(message = "INVALID_PASSWORD")
    private String password;

    private Boolean status;

    private Boolean deactivate;

}
