package com.example.core.serviceImpl;

import com.example.core.dto.UserRequestDto;
import com.example.core.entity.UserEntity;
import com.example.core.enums.ExceptionEnum;
import com.example.core.exception.CustomException;
import com.example.core.repository.UserRepository;
import com.example.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;


    @Override

    public UserRequestDto insertUser(UserRequestDto userRequestDto) throws CustomException {

        userRequestDto.setId(null);
        UserEntity userEntity = this.insertUpdateUser(userRequestDto);
        UserRequestDto userRequestDto1 = modelMapper.map(userEntity, UserRequestDto.class);
        System.out.println("userRequestDto1 = " + userRequestDto1.toString());
        return userRequestDto1;
    }

    @Override
    public UserRequestDto updateUser(UserRequestDto userRequestDto) throws CustomException {
        UserEntity userEntity = insertUpdateUser(userRequestDto);
        UserRequestDto userRequestDto1 = modelMapper.map(userEntity, UserRequestDto.class);
        System.out.println("userRequestDto1 = " + userRequestDto1.toString());
        return userRequestDto1;
    }

    @Override
    public void deleteUserById(Long userId) throws CustomException {

        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);

        if (!userEntityOptional.isPresent()) {
            LOGGER.error("delete User :: User details with id {} not found", userId);
            throw new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        }
        UserEntity userEntity = userEntityOptional.get();

        if (Boolean.FALSE.equals(userEntity.getStatus())) {
            LOGGER.error("User is deleted with given id {}", userId);
            throw new CustomException(ExceptionEnum.USER_DELETED_WITH_ID.getValue(), HttpStatus.BAD_REQUEST);
        }
        userEntity.setStatus(Boolean.FALSE);
        userRepository.save(userEntity);
    }

    private UserEntity insertUpdateUser(UserRequestDto userRequestDto) throws CustomException {

        LOGGER.info("in insertUpdate user method : {}", userRequestDto);

        UserEntity userEntity;

        System.out.println("userRequestDto = " + userRequestDto);

        if (userRequestDto.getId() != null) {

            Optional<UserEntity> userEntityOptional = userRepository.findById(userRequestDto.getId());

            if (!userEntityOptional.isPresent()) {
                LOGGER.error("insertUpdate User :: User with id {} not found", userRequestDto.getId());
                throw new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
            }

            System.out.println("userEntityOptional.get() = " + userEntityOptional.get());

        } else {

            userEntity = new UserEntity();

        }
        userEntity = modelMapper.map(userRequestDto, UserEntity.class);
        return userRepository.save(userEntity);
    }




}
