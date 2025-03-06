package com.demo.spring_security_jwt.controller;

import com.demo.spring_security_jwt.dto.*;
import com.demo.spring_security_jwt.enums.ApiResponsesEnum;
import com.demo.spring_security_jwt.enums.ExceptionEnum;
import com.demo.spring_security_jwt.exception.CustomException;
import com.demo.spring_security_jwt.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDto loginRequestDto) {

        try {
            JwtResponseDto verify = loginService.verify(loginRequestDto);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, ApiResponsesEnum.TOKEN_RETRIEVAL_SUCCESS.getValue(), verify), HttpStatus.OK);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

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
