package com.example.manyToOne.serviceImpl;

import com.example.manyToOne.dto.UserRequestDto;
import com.example.manyToOne.dto.UserResponseDto;
import com.example.manyToOne.entity.OrganizationEntity;
import com.example.manyToOne.entity.UserEntity;
import com.example.manyToOne.enums.ExceptionEnum;
import com.example.manyToOne.exception.CustomException;
import com.example.manyToOne.repository.OrganizationRepository;
import com.example.manyToOne.repository.UserRepository;
import com.example.manyToOne.service.UserService;
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


    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    private final OrganizationRepository organizationRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserResponseDto insertUser(UserRequestDto userRequestDto) {


        UserEntity userEntity = insertUpdateUser(userRequestDto);
        UserResponseDto userRequestDto1 = modelMapper.map(userEntity, UserResponseDto.class);
        System.out.println("userRequestDto1 = " + userRequestDto1.toString());
        return userRequestDto1;


    }

    private UserEntity insertUpdateUser(UserRequestDto userRequestDto) {

        UserEntity userEntity;


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

            Optional<OrganizationEntity> organizationEntity = organizationRepository.findById(userRequestDto.getOrganizationId());

            userEntity.setOrganization(organizationEntity.get());

        }

        userEntity = modelMapper.map(userRequestDto, UserEntity.class);
        return userRepository.save(userEntity);

    }
}
