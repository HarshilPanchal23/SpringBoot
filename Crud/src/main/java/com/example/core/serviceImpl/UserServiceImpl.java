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

        Optional<UserEntity> userEntityByUser = userRepository.findById(userId);

        if (!userEntityByUser.isPresent()) {
            throw new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        }

        return modelMapper.map(userEntityByUser, UserResponseDto.class);

    }


    private UserEntity insertUpdateUser(UserRequestDto userRequestDto) throws CustomException {

        LOGGER.info("in insertUpdate user method : {}", userRequestDto);

        UserEntity userEntity;

        System.out.println("userRequestDto = " + userRequestDto);

        if (userRequestDto.getId() != null) {

            Optional<UserEntity> userEntityOptional = userRepository.findByEmail(userRequestDto.getEmail());

            userEntityOptional.ifPresentOrElse(
                    existingUser -> {
                        // Check if user with provided ID exists
                        if (!existingUser.getId().equals(userRequestDto.getId())) {
                            LOGGER.error("User with email {} exists but with different ID", userRequestDto.getEmail());
                            throw new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
                        }
                    },
                    () -> {
                        // Throw exception if user doesn't exist
                        LOGGER.error("User with email {} not found for update", userRequestDto.getEmail());
                        throw new CustomException(ExceptionEnum.USER_WITH_EMAIL_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
                    }
            );
            System.out.println("userEntityOptional.get() = " + userEntityOptional.get());

        } else {

            userEntity = new UserEntity();

        }

        userEntity = modelMapper.map(userRequestDto, UserEntity.class);
        return userRepository.save(userEntity);
    }


    private List<UserResponseDto> mapToListOfUserRequestDto(List<UserEntity> userEntityList) {
        // Using ModelMapper to convert the list of UserEntity to UserRequestDto.
        return modelMapper.map(userEntityList, new TypeToken<List<UserResponseDto>>() {
        }.getType());
    }

}
