package com.demo.spring_security_jwt.service;


import com.demo.spring_security_jwt.dto.UserRequestDto;
import com.demo.spring_security_jwt.dto.UserResponseDto;
import com.demo.spring_security_jwt.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto insertUser(UserRequestDto userRequestDto) throws CustomException;

    UserRequestDto updateUser(UserRequestDto userRequestDto) throws CustomException;

    void deleteUserById(Long userId) throws CustomException;

    Page<UserResponseDto> getAllUsers(String searchValue, Pageable pageable);

    UserResponseDto getUserById(Long userId);
}


