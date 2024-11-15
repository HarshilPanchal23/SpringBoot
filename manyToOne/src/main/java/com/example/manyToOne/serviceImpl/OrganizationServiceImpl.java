package com.example.manyToOne.serviceImpl;

import com.example.manyToOne.dto.OrganizationRequestDto;
import com.example.manyToOne.dto.OrganizationResponseDto;
import com.example.manyToOne.entity.OrganizationEntity;
import com.example.manyToOne.enums.ExceptionEnum;
import com.example.manyToOne.exception.CustomException;
import com.example.manyToOne.repository.OrganizationRepository;
import com.example.manyToOne.service.OrganizationService;
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

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final ModelMapper modelMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);


    @Override
    public Page<OrganizationResponseDto> getAllOrganization(String searchValue, Pageable pageable) {

        // Fetching the Organization entities based on the search value.
        Page<OrganizationEntity> organizationResponseDtoPage = organizationRepository.findByOrganizationNameLike(
                "%" + searchValue + "%", pageable);

        // Mapping the Organization entities to the DTO list
        List<OrganizationResponseDto> organizationResponseDtoList = mapToListOfOrganizationRequestDto(organizationResponseDtoPage.getContent());

        // Returning the result as a Page of OrganizationRequestDto
        return new PageImpl<>(organizationResponseDtoList, pageable, organizationResponseDtoPage.getTotalElements());

    }

    @Override
    public OrganizationResponseDto insertOrganization(OrganizationRequestDto organizationRequestDto) {

        organizationRequestDto.setId(null);
        OrganizationEntity organizationEntity = this.insertUpdateOrganization(organizationRequestDto);
        OrganizationResponseDto organizationResponseDto = modelMapper.map(organizationEntity, OrganizationResponseDto.class);
        return organizationResponseDto;
    }


    @Override
    public OrganizationResponseDto updateOrganization(OrganizationRequestDto organizationRequestDto) {
        OrganizationEntity organizationEntity = insertUpdateOrganization(organizationRequestDto);
        OrganizationResponseDto organizationResponseDto = modelMapper.map(organizationEntity, OrganizationResponseDto.class);
        return organizationResponseDto;
    }


    private OrganizationEntity insertUpdateOrganization(OrganizationRequestDto organizationRequestDto) {

        OrganizationEntity organizationEntity = null;

        if (organizationRequestDto.getId() != null) {

            OrganizationEntity organizationEntity1 = organizationRepository.findById(organizationRequestDto.getId()).orElseThrow(() ->
                    new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));


            organizationEntity = updateOrganizationEntity(organizationEntity1, organizationRequestDto);


        } else {


          organizationEntity =   organizationEntity.builder()
                    .organizationName(organizationRequestDto.getOrganizationName())
                    .address(organizationRequestDto.getAddress())
                    .status(organizationRequestDto.getStatus())
                    .deactivate(organizationRequestDto.getDeactivate())
                    .build();


        }

        return organizationRepository.save(organizationEntity);

    }

    private OrganizationEntity updateOrganizationEntity(OrganizationEntity organizationEntity1, OrganizationRequestDto organizationRequestDto) {

        organizationEntity1.setOrganizationName(organizationRequestDto.getOrganizationName());
        organizationEntity1.setAddress(organizationRequestDto.getAddress());
        organizationEntity1.setStatus(organizationRequestDto.getStatus());
        organizationEntity1.setDeactivate(organizationEntity1.getDeactivate());
        return organizationEntity1;

    }

    @Override
    public void deleteOrganizationById(Long organizationId) {

        Optional<OrganizationEntity> organizationEntityOptional = organizationRepository.findById(organizationId);

        if (!organizationEntityOptional.isPresent()) {
            LOGGER.error("delete Organization :: Organization details with id {} not found", organizationId);
            throw new CustomException(ExceptionEnum.Organization_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        }

        OrganizationEntity organizationEntity = organizationEntityOptional.get();

        if (Boolean.FALSE.equals(organizationEntity.getStatus())) {
            LOGGER.error("Organization is deleted with given id {}", organizationId);
            throw new CustomException(ExceptionEnum.Organization_DELETED_WITH_ID.getValue(), HttpStatus.BAD_REQUEST);
        }
        organizationEntity.setStatus(Boolean.FALSE);
        organizationEntity.setDeactivate(Boolean.TRUE);
        organizationRepository.save(organizationEntity);
    }


    private List<OrganizationResponseDto> mapToListOfOrganizationRequestDto(List<OrganizationEntity> organizationEntityList) {
        // Using ModelMapper to convert the list of OrganizationEntity to OrganizationRequestDto.
        return modelMapper.map(organizationEntityList, new TypeToken<List<OrganizationResponseDto>>() {
        }.getType());
    }


}
