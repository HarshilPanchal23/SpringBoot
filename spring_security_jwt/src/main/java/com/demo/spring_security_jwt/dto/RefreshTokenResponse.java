package com.demo.spring_security_jwt.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {

    private String accessToken;


}
