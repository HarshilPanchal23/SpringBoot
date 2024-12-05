package com.demo.spring_security_jwt.service;

import com.demo.spring_security_jwt.dto.JwtResponseDto;
import com.demo.spring_security_jwt.dto.LoginRequestDto;
import com.demo.spring_security_jwt.dto.RefreshTokenRequestDto;

public interface LoginService {
    JwtResponseDto verify(LoginRequestDto loginRequestDto);

    JwtResponseDto generateRefreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
}
