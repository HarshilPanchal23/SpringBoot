package com.demo.spring_security_jwt.service;

import com.demo.spring_security_jwt.dto.JwtResponseDto;
import com.demo.spring_security_jwt.dto.LoginRequestDto;
import com.demo.spring_security_jwt.dto.RefreshTokenRequestDto;
import com.demo.spring_security_jwt.dto.RefreshTokenResponse;

public interface LoginService {
    JwtResponseDto verify(LoginRequestDto loginRequestDto);

    RefreshTokenResponse generateRefreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
}
