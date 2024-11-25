package com.example.springSecurity.service;


import com.example.springSecurity.dto.UserRequestDto;
import com.example.springSecurity.dto.UserResponseDto;
import com.example.springSecurity.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto insertUser(UserRequestDto userRequestDto) throws CustomException;

    UserRequestDto updateUser(UserRequestDto userRequestDto) throws CustomException;

    void deleteUserById(Long userId) throws CustomException;

    Page<UserResponseDto> getAllUsers(String searchValue, Pageable pageable);

    UserResponseDto getUserById(Long userId);
}


