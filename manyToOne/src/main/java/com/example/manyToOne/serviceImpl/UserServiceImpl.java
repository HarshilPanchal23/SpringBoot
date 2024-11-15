package com.example.manyToOne.serviceImpl;

import com.example.manyToOne.dto.OrganizationResponseDto;
import com.example.manyToOne.dto.UserRequestDto;
import com.example.manyToOne.dto.UserResponseDto;
import com.example.manyToOne.entity.OrganizationEntity;
import com.example.manyToOne.entity.UserEntity;
import com.example.manyToOne.enums.ApiResponsesEnum;
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
        UserResponseDto userResponseDto = modelMapper.map(userEntity, UserResponseDto.class);
        System.out.println("userResponseDto = " + userResponseDto.toString());
        return userResponseDto;
    }

    @Override
    public UserResponseDto updateUser(UserRequestDto userRequestDto) {
        UserEntity userEntity = insertUpdateUser(userRequestDto);
        UserResponseDto userResponseDto = modelMapper.map(userEntity, UserResponseDto.class);
        System.out.println("userResponseDto = " + userResponseDto.toString());
        return userResponseDto;
    }

    @Override
    public Page<UserResponseDto> getAllUserByOrganizationId(Long organizationId, String trim, Pageable pageable) {

        Page<UserProjection> users;

        users = userRepository
                .findByOrganizationId(organizationId, pageable);

        return users.map(this::objectToDTO);
    }

    @Override
    public String enableDisableUserById(Long userId, Long organizationId, Boolean status) {

        OrganizationEntity organizationEntity = organizationRepository.
                findById(organizationId).orElseThrow(() -> new CustomException(ExceptionEnum.Organization_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));

        UserEntity userEntity = userRepository.findByIdAndOrganization(userId, organizationEntity).
                orElseThrow(() -> new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));

        userEntity.setStatus(status);
        userEntity.setDeactivate(userEntity.getStatus().equals(Boolean.TRUE) ? Boolean.FALSE : Boolean.TRUE);
        userRepository.save(userEntity);

        return Boolean.TRUE.equals(status) ? ApiResponsesEnum.USER_ENABLE_SUCCESSFULLY.getValue() : ApiResponsesEnum.USER_DISABLE_SUCCESSFULLY.getValue();
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


    private List<OrganizationResponseDto> mapToListOfOrganizationRequestDto(List<OrganizationEntity> organizationEntityList) {
        // Using ModelMapper to convert the list of OrganizationEntity to OrganizationRequestDto.
        return modelMapper.map(organizationEntityList, new TypeToken() {
        }.getType());
    }

    private UserEntity insertUpdateUser(UserRequestDto userRequestDto) {

        UserEntity userEntity = null;

        OrganizationEntity organizationEntity = organizationRepository.findById(userRequestDto.getOrganizationId()).
                orElseThrow(() -> new CustomException(ExceptionEnum.Organization_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));


        if (userRequestDto.getId() != null) {

            UserEntity userEntity1 = userRepository.
                    findByIdAndStatusAndDeactivate(userRequestDto.getId(), Boolean.TRUE, Boolean.FALSE).
                    orElseThrow(() -> new CustomException(ExceptionEnum.Organization_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));

            userEntity = updateUserEntity(userEntity1, userRequestDto, organizationEntity);

        } else {

            userEntity = userEntity.builder()
                    .firstName(userRequestDto.getFirstName())
                    .lastName(userRequestDto.getLastName())
                    .email(userRequestDto.getEmail())
                    .password(userRequestDto.getPassword())
                    .status(userRequestDto.getStatus())
                    .deactivate(userRequestDto.getDeactivate())
                    .organization(organizationEntity)
                    .build();
        }

        return userRepository.save(userEntity);

    }

    private UserEntity updateUserEntity(UserEntity userEntityByOrganization, UserRequestDto userRequestDto, OrganizationEntity organizationEntity) {

        userEntityByOrganization.setFirstName(userRequestDto.getFirstName());
        userEntityByOrganization.setLastName(userRequestDto.getLastName());
        userEntityByOrganization.setStatus(userRequestDto.getStatus());
        userEntityByOrganization.setDeactivate(userRequestDto.getDeactivate());
        userEntityByOrganization.setEmail(userRequestDto.getEmail());
        userEntityByOrganization.setPassword(userRequestDto.getPassword());
        userEntityByOrganization.setOrganization(organizationEntity);

        return userEntityByOrganization;
    }

    private UserEntity checkForUserExistOrNot(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));

    }


}
