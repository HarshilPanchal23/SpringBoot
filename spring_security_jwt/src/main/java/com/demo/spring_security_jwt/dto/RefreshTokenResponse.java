package com.demo.spring_security_jwt.dto;

import lombok.Data;

@Data
public class RefreshTokenResponse {

    private String accessToken;

    private String refreshToken;

}
