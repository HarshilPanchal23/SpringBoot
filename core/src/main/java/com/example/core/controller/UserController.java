package com.example.core.controller;

import com.example.core.dto.ApiResponse;
import com.example.core.dto.UserRequestDto;
import com.example.core.enums.ApiResponsesEnum;
import com.example.core.enums.ExceptionEnum;
import com.example.core.exception.CustomException;
import com.example.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public ResponseEntity<ApiResponse> insertUser(@RequestBody UserRequestDto userRequestDto) throws CustomException {

        try {
            UserRequestDto responseDTO = userService.insertUser(userRequestDto);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.USER_CREATION_SUCCESSFULLY.getValue(), responseDTO),
                    HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Exception while update Datasource info : {1}", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable(value = "userId") Long userId, @RequestBody UserRequestDto userRequestDto) throws CustomException {

        try {
            userRequestDto.setId(userId);
            UserRequestDto responseDTO = userService.updateUser(userRequestDto);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.USER_CREATION_SUCCESSFULLY.getValue(), responseDTO),
                    HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Exception while update Datasource info : {1}", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
