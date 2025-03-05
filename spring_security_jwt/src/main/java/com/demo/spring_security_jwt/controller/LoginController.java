package com.demo.spring_security_jwt.controller;

import com.demo.spring_security_jwt.dto.JwtResponseDto;
import com.demo.spring_security_jwt.dto.LoginRequestDto;
import com.demo.spring_security_jwt.dto.RefreshTokenRequestDto;
import com.demo.spring_security_jwt.dto.RefreshTokenResponse;
import com.demo.spring_security_jwt.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;


    @PostMapping("/login")
    public JwtResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return loginService.verify(loginRequestDto);
    }

    @PostMapping("/refreshToken")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        return loginService.generateRefreshToken(refreshTokenRequestDto);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        loginService.forgotPassword(email);
        return ResponseEntity.ok("Password reset link sent to your email.");
    }





}
