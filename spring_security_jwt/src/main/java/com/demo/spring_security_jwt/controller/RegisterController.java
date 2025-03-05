package com.demo.spring_security_jwt.controller;

import com.demo.spring_security_jwt.dto.ApiResponse;
import com.demo.spring_security_jwt.dto.UserRequestDto;
import com.demo.spring_security_jwt.dto.UserResponseDto;
import com.demo.spring_security_jwt.entity.UserEntity;
import com.demo.spring_security_jwt.enums.ApiResponsesEnum;
import com.demo.spring_security_jwt.enums.ExceptionEnum;
import com.demo.spring_security_jwt.exception.CustomException;
import com.demo.spring_security_jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);


    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserRequestDto userRequestDto) throws CustomException {

        try {
            UserResponseDto responseDTO = userService.insertUser(userRequestDto);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.USER_CREATION_SUCCESSFULLY.getValue(), responseDTO),
                    HttpStatus.OK);
        } catch (CustomException e) {
            LOGGER.error("Exception while update Organization info : {1}", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
