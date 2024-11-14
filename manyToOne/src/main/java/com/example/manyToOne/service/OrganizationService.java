package com.example.manyToOne.service;


import com.example.manyToOne.dto.OrganizationRequestDto;
import com.example.manyToOne.dto.OrganizationResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrganizationService {


    Page<OrganizationResponseDto> getAllOrganization(String searchValue, Pageable pageable);

    OrganizationResponseDto insertOrganization(@Valid OrganizationRequestDto organizationRequestDto);

    OrganizationResponseDto updateOrganization(OrganizationRequestDto organizationRequestDto);

    void deleteOrganizationById(Long organizationId);

}
