package com.example.core.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequestDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Boolean status;

}
