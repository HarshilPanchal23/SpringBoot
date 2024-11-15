package com.example.core.serviceImpl;

import com.example.core.dto.UserRequestDto;
import com.example.core.dto.UserResponseDto;
import com.example.core.entity.UserEntity;
import com.example.core.enums.ExceptionEnum;
import com.example.core.exception.CustomException;
import com.example.core.repository.UserRepository;
import com.example.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
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


        UserEntity user = checkForUserExistOrNot(userId);

        UserEntity userEntity = user;

        if (Boolean.FALSE.equals(userEntity.getStatus())) {
            LOGGER.error("User is deleted with given id {}", userId);
            throw new CustomException(ExceptionEnum.USER_DELETED_WITH_ID.getValue(), HttpStatus.BAD_REQUEST);
        }

        userEntity.setStatus(Boolean.FALSE);
        userEntity.setDeactivate(Boolean.TRUE);
        userRepository.save(userEntity);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(String searchValue, Pageable pageable) {

        // Fetching the user entities based on the search value.
        Page<UserEntity> userEntityPageList = userRepository.findByFirstNameLike(
                "%" + searchValue + "%", pageable);

        // Mapping the user entities to the DTO list
        List<UserResponseDto> userResponseDtoList = mapToListOfUserRequestDto(userEntityPageList.getContent());

        // Returning the result as a Page of UserRequestDto
        return new PageImpl<>(userResponseDtoList, pageable, userEntityPageList.getTotalElements());
    }

    @Override
    public UserResponseDto getUserbyId(Long userId) {

        Optional<UserEntity> userEntityByUser = userRepository.findByIdAndStatusAndDeactivate(userId, Boolean.TRUE, Boolean.FALSE);

        if (!userEntityByUser.isPresent()) {
            throw new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        }

        return modelMapper.map(userEntityByUser, UserResponseDto.class);

    }


    private UserEntity insertUpdateUser(UserRequestDto userRequestDto) throws CustomException {

        UserEntity userEntity = null;

        if (userRequestDto.getId() != null) {

            UserEntity userEntityOptional = checkForUserExistOrNot(userRequestDto.getId());

            userEntity = updateUserEntity(userEntityOptional, userRequestDto);

        } else {

            userEntity.builder()
                    .email(userRequestDto.getEmail())
                    .firstName(userRequestDto.getFirstName())
                    .lastName(userRequestDto.getLastName())
                    .password(userRequestDto.getPassword())
                    .status(userRequestDto.getStatus())
                    .deactivate(userRequestDto.getDeactivate())
                    .build();
        }

        return userRepository.save(userEntity);

    }

    private UserEntity updateUserEntity(UserEntity userEntityOptional, UserRequestDto userRequestDto) {

        userEntityOptional.setFirstName(userRequestDto.getFirstName());
        userEntityOptional.setLastName(userRequestDto.getLastName());
        userEntityOptional.setEmail(userRequestDto.getEmail());
        userEntityOptional.setPassword(userRequestDto.getPassword());
        userEntityOptional.setStatus(userRequestDto.getStatus());
        userEntityOptional.setDeactivate(userRequestDto.getDeactivate());
        return userEntityOptional;

    }


    private List<UserResponseDto> mapToListOfUserRequestDto(List<UserEntity> userEntityList) {
        // Using ModelMapper to convert the list of UserEntity to UserRequestDto.
        return modelMapper.map(userEntityList, new TypeToken<List<UserResponseDto>>() {
        }.getType());
    }

    private UserEntity checkForUserExistOrNot(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));

    }


}
