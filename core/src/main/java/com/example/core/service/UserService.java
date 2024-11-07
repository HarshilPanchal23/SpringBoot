package com.example.core.service;

import com.example.core.dto.UserRequestDto;
import com.example.core.dto.UserResponseDto;
import com.example.core.exception.CustomException;

public interface UserService {
    UserResponseDto insertUser(UserRequestDto userRequestDto) throws CustomException;

    UserResponseDto updateUser(UserRequestDto userRequestDto) throws CustomException;
}

