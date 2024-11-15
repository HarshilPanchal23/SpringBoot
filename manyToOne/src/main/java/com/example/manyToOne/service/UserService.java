package com.example.manyToOne.service;

import com.example.manyToOne.dto.UserRequestDto;
import com.example.manyToOne.dto.UserResponseDto;
import com.example.manyToOne.projection.UserProjection;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto insertUser(@Valid UserRequestDto userRequestDto);

    Page<UserResponseDto> getAllUserByOrganizationId(Long organizationId, String trim, Pageable pageable);


    String enableDisableUserById(Long userId, Long organizationId, Boolean status);

    UserResponseDto updateUser(UserRequestDto userRequestDto);

}
