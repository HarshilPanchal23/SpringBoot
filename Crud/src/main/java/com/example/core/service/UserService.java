package com.example.core.service;

import com.example.core.dto.UserRequestDto;
import com.example.core.dto.UserResponseDto;
import com.example.core.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserRequestDto insertUser(UserRequestDto userRequestDto) throws CustomException;

    UserRequestDto updateUser(UserRequestDto userRequestDto) throws CustomException;

    void deleteUserById(Long userId) throws CustomException;

    Page<UserResponseDto> getAllUsers(String searchValue, Pageable pageable);

    UserResponseDto getUserById(Long userId);
}


