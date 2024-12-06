package com.demo.spring_security_jwt.dto;

import lombok.Data;

@Data
public class RefreshTokenRequestDto {

    private String refreshToken;
}
