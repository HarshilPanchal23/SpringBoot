package com.example.core.service;

import com.example.core.dto.UserRequestDto;
import com.example.core.exception.CustomException;

public interface UserService {
    UserRequestDto insertUser(UserRequestDto userRequestDto) throws CustomException;

    UserRequestDto updateUser(UserRequestDto userRequestDto) throws CustomException;
}

