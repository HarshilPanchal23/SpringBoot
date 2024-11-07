package com.example.core.serviceImpl;

import com.example.core.dto.UserRequestDto;
import com.example.core.dto.UserResponseDto;
import com.example.core.entity.UserEntity;
import com.example.core.enums.ExceptionEnum;
import com.example.core.exception.CustomException;
import com.example.core.repository.UserRepository;
import com.example.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


//    private final ModelMapper modelMapper;


    @Override

    public UserResponseDto insertUser(UserRequestDto userRequestDto) throws CustomException {

        userRequestDto.setId(null);
        UserEntity userEntity = this.insertUpdateUser(userRequestDto);
        return null;
    }

    @Override
    public UserResponseDto updateUser(UserRequestDto userRequestDto) throws CustomException {
        UserEntity userEntity = insertUpdateUser(userRequestDto);
        return null;
    }

    private UserEntity insertUpdateUser(UserRequestDto userRequestDto) throws CustomException {

        if (userRequestDto.getId() != null) {
            Optional<UserEntity> userEntityOptional = userRepository.findById(userRequestDto.getId());
            if (!userEntityOptional.isPresent()) {
                LOGGER.error("insertUpdate User :: User with id {} not found", userRequestDto.getId());
                throw new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
            }


        } else {


        }

        return null;
    }

}
