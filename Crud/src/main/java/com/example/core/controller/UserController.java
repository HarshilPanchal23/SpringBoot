package com.example.core.controller;

import com.example.core.dto.ApiResponse;
import com.example.core.dto.UserRequestDto;
import com.example.core.dto.UserResponseDto;
import com.example.core.enums.ApiResponsesEnum;
import com.example.core.enums.ExceptionEnum;
import com.example.core.enums.FilterEnum;
import com.example.core.exception.CustomException;
import com.example.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @GetMapping
    public ResponseEntity<ApiResponse> getAllUser(@RequestParam(value = "pageNo",
            defaultValue = "0") Integer pageNo,
                                                  @RequestParam(value = "pageSize",
                                                          defaultValue = "10") Integer pageSize, @RequestParam(value = "searchValue", required = false,
            defaultValue = "") String searchValue,
                                                  @RequestParam(value = "sortBy",
                                                          defaultValue = "ID") FilterEnum sortBy, @RequestParam(value = "sortAs",
            defaultValue = "ASC")
                                                  Sort.Direction sortAs) {

        try {
            // making pageable request
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortAs, sortBy.getValue()));
            Page<UserResponseDto> UserResponseDtoPage = userService.getAllUsers(searchValue.trim(), pageable);
            return ResponseEntity.ok(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.ALL_USER_RETRIEVAL_SUCCESS.getValue(), UserResponseDtoPage));
        } catch (Exception e) {
            LOGGER.error("getAll Users :: Exception ", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<ApiResponse> getUserbyId(@PathVariable(value = "userId") Long userId) {

        try {
            UserResponseDto responseDTO = userService.getUserById(userId);

            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.USER_FETCH_SUCCESSFULLY.getValue(), responseDTO),
                    HttpStatus.OK);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("get User By Id Error : {1}", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(value = "/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@Valid @PathVariable(value = "userId") Long userId, @RequestBody UserRequestDto userRequestDto) throws CustomException {

        try {
            userRequestDto.setId(userId);
            UserRequestDto responseDTO = userService.updateUser(userRequestDto);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.USER_CREATION_SUCCESSFULLY.getValue(), responseDTO),
                    HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Exception while update user : {1}", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable(value = "userId") Long userId) throws CustomException {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, ApiResponsesEnum.USER_DELETED_SUCCESSFULLY.getValue()));
        } catch (Exception e) {
            LOGGER.error("Delete User  :: Exception ", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
