package com.example.manyToOne.controller;

import com.example.manyToOne.dto.ApiResponse;
import com.example.manyToOne.dto.UserRequestDto;
import com.example.manyToOne.dto.UserResponseDto;
import com.example.manyToOne.enums.ApiResponsesEnum;
import com.example.manyToOne.enums.ExceptionEnum;
import com.example.manyToOne.enums.FilterEnum;
import com.example.manyToOne.exception.CustomException;
import com.example.manyToOne.service.UserService;
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


    @GetMapping("/{organizationId}")
    public ResponseEntity<ApiResponse> getAllUser(@RequestParam(value = "pageNo",
            defaultValue = "0") Integer pageNo,
                                                          @RequestParam(value = "pageSize",
                                                                  defaultValue = "10") Integer pageSize, @RequestParam(value = "searchValue", required = false,
            defaultValue = "") String searchValue,
                                                          @RequestParam(value = "sortBy",
                                                                  defaultValue = "ID") FilterEnum sortBy, @RequestParam(value = "sortAs",
            defaultValue = "ASC")
                                                          Sort.Direction sortAs, @PathVariable(value = "organizationId") Long organizationId) {

        try {
            // making pageable request
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortAs, sortBy.getValue()));
            Page<UserResponseDto> userResponseDtoPage = userService.getAllUserByOrganizationId(organizationId, searchValue.trim(), pageable);
            return ResponseEntity.ok(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.ALL_USER_RETRIEVAL_SUCCESSFULLY.getValue(), userResponseDtoPage));
        } catch (Exception e) {
            LOGGER.error("getAll Organizations :: Exception ", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


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

    @PutMapping(value = "/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@Valid @PathVariable(value = "userId") Long userId, @RequestBody UserRequestDto userRequestDto) throws CustomException {

        try {
            userRequestDto.setId(userId);
            UserResponseDto responseDTO = userService.updateUser(userRequestDto);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.USER_UPDATED_SUCCESSFULLY.getValue(), responseDTO),
                    HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Exception while update user : {1}", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "/status/{userId}/{organizationId}/{status}")
    public ResponseEntity<ApiResponse> enableDisableUserById(@PathVariable(name = "userId") Long userId,
                                                             @PathVariable(name = "organizationId") Long organizationId, @PathVariable(name = "status") Boolean status) {
        try {
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, this.userService.enableDisableUserById(userId, organizationId,status)));
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("enableDisableUserById :: Exception ", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
