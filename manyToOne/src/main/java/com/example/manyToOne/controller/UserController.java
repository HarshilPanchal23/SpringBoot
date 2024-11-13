package com.example.manyToOne.controller;

import com.example.manyToOne.dto.ApiResponse;
import com.example.manyToOne.dto.UserRequestDto;
import com.example.manyToOne.dto.UserResponseDto;
import com.example.manyToOne.enums.ApiResponsesEnum;
import com.example.manyToOne.enums.ExceptionEnum;
import com.example.manyToOne.exception.CustomException;
import com.example.manyToOne.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse> insertUser(@Valid @RequestBody UserRequestDto userRequestDto) throws CustomException {

        try {
            UserResponseDto responseDTO = userService.insertUser(userRequestDto);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.USER_CREATION_SUCCESSFULLY.getValue(), responseDTO),
                    HttpStatus.OK);

        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
