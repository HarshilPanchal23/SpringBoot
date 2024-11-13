package com.example.manyToOne.service;

import com.example.manyToOne.dto.UserRequestDto;
import com.example.manyToOne.dto.UserResponseDto;
import jakarta.validation.Valid;

public interface UserService {
    UserResponseDto insertUser(@Valid UserRequestDto userRequestDto);

}
