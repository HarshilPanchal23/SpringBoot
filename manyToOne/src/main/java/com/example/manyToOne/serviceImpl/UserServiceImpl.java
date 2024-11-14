package com.example.manyToOne.serviceImpl;

import com.example.manyToOne.dto.OrganizationResponseDto;
import com.example.manyToOne.dto.UserRequestDto;
import com.example.manyToOne.dto.UserResponseDto;
import com.example.manyToOne.entity.OrganizationEntity;
import com.example.manyToOne.entity.UserEntity;
import com.example.manyToOne.enums.ExceptionEnum;
import com.example.manyToOne.exception.CustomException;
import com.example.manyToOne.projection.UserProjection;
import com.example.manyToOne.repository.OrganizationRepository;
import com.example.manyToOne.repository.UserRepository;
import com.example.manyToOne.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
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

    @Override
    public Page<UserProjection> getAllUserByOrganizationId(Long organizationId, String trim, Pageable pageable) {
        // Retrieve the user entities based on organization ID, search value, and active status
        Page<UserProjection> users;

        users = userRepository
                .findByOrganizationId(organizationId, pageable);

//        System.out.println("users.getContent() = " + users.getContent().get(2).getOrganization().getOrganizationName());

//        System.out.println("users.getContent().get(1) = " + users.getContent().get(1).getOrganization());
        // Map each UserEntity to UserResponseDto
        return users;
    }

    private UserResponseDto objectToDTO(UserProjection userProjection) {

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setFirstName(userProjection.getFirstName());
        userResponseDto.setLastName(userProjection.getLastName());
        userResponseDto.setStatus(userProjection.getStatus());
        userResponseDto.setEmail(userProjection.getEmail());
        userResponseDto.setPassword(userProjection.getPassword());
        userResponseDto.setDeactivate(userProjection.getDeactivate());

        OrganizationResponseDto organizationDto = new OrganizationResponseDto();
        organizationDto.setOrganizationName(userProjection.getOrganizationName());
        organizationDto.setAddress(userProjection.getAddress());
        userResponseDto.setOrganization(organizationDto);
        return userResponseDto;
    }


//    private UserResponseDto objectToDTO(UserEntity userEntity) {
//        UserResponseDto userDTO = new UserResponseDto();
//        userDTO.setFirstName(userEntity.getFirstName());
//        userDTO.setLastName(userEntity.getLastName());
//        userDTO.setEmail(userEntity.getEmail());
//        userDTO.setPassword(userEntity.getPassword());
//        userDTO.setStatus(userEntity.getStatus());
//        userDTO.setOrganizationId(userEntity.getOrganization().getId());
//        return userDTO;
//    }
//
//    // Example method to convert an organization entity to a DTO
//    private OrganizationResponseDto objectToOrganizationDto(OrganizationEntity organization) {
//        OrganizationResponseDto organizationDto = new OrganizationResponseDto();
//        organizationDto.setOrganizationName(organization.getOrganizationName());
//        return organizationDto;
//    }


    private List<OrganizationResponseDto> mapToListOfOrganizationRequestDto(List<OrganizationEntity> organizationEntityList) {
        // Using ModelMapper to convert the list of OrganizationEntity to OrganizationRequestDto.
        return modelMapper.map(organizationEntityList, new TypeToken() {
        }.getType());
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
