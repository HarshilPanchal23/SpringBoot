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
        System.out.println("organizationResponseDto = " + organizationResponseDto.toString());
        return organizationResponseDto;
    }


    @Override
    public OrganizationResponseDto updateOrganization(OrganizationRequestDto organizationRequestDto) {
        OrganizationEntity organizationEntity = insertUpdateOrganization(organizationRequestDto);
        OrganizationResponseDto organizationResponseDto = modelMapper.map(organizationEntity, OrganizationResponseDto.class);
        System.out.println("organizationResponseDto = " + organizationResponseDto.toString());
        return organizationResponseDto;
    }


    private OrganizationEntity insertUpdateOrganization(OrganizationRequestDto organizationRequestDto) {

        OrganizationEntity organizationEntity;

        if (organizationRequestDto.getId() != null) {
            Optional<OrganizationEntity> byOrganizationName = organizationRepository.
                    findByOrganizationName(organizationRequestDto.getOrganizationName());

            byOrganizationName.ifPresentOrElse(
                    existingOrganization -> {
                        if (!existingOrganization.getId().equals(organizationRequestDto.getId())) {
                            LOGGER.error("Organization with name {} exists but with different ID", organizationRequestDto.getOrganizationName());
                            throw new CustomException(ExceptionEnum.Organization_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
                        }
                    },
                    () -> {
                        // Throw exception if user doesn't exist
                        LOGGER.error("Organization with name {} not found for update", organizationRequestDto.getOrganizationName());
                        throw new CustomException(ExceptionEnum.USER_WITH_EMAIL_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
                    }
            );
        } else {
            organizationEntity = new OrganizationEntity();
            System.out.println("organizationEntity = " + organizationEntity);

        }

        System.out.println("organizationRequestDto = " + organizationRequestDto.toString());
        organizationEntity = modelMapper.map(organizationRequestDto, OrganizationEntity.class);
        return organizationRepository.save(organizationEntity);

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
