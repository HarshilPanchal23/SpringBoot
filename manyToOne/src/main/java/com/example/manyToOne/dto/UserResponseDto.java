package com.example.manyToOne.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResponseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Boolean status;

    private Boolean deactivate;

    private OrganizationResponseDto organization;
}
