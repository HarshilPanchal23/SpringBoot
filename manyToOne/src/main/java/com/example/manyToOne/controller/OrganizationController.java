package com.example.manyToOne.controller;

import com.example.manyToOne.dto.ApiResponse;
import com.example.manyToOne.dto.OrganizationRequestDto;
import com.example.manyToOne.dto.OrganizationResponseDto;
import com.example.manyToOne.enums.ApiResponsesEnum;
import com.example.manyToOne.enums.ExceptionEnum;
import com.example.manyToOne.enums.FilterEnum;
import com.example.manyToOne.exception.CustomException;
import com.example.manyToOne.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organization")
public class OrganizationController {

    private final OrganizationService organizationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);


    @GetMapping
    public ResponseEntity<ApiResponse> getAllOrganization(@RequestParam(value = "pageNo",
            defaultValue = "0") Integer pageNo,
                                                          @RequestParam(value = "pageSize",
                                                                  defaultValue = "10") Integer pageSize, @RequestParam(value = "searchValue", required = false,
            defaultValue = "") String searchValue,
                                                          @RequestParam(value = "sortBy",
                                                                  defaultValue = "ID") FilterEnum sortBy, @RequestParam(value = "sortAs",
            defaultValue = "ASC")
                                                          Sort.Direction sortAs) {

        try {
            // making pageable request
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortAs, sortBy.getValue()));
            Page<OrganizationResponseDto> organizationResponseDtoPage = organizationService.getAllOrganization(searchValue.trim(), pageable);
            return ResponseEntity.ok(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.ALL_Organization_RETRIEVAL_SUCCESS.getValue(), organizationResponseDtoPage));
        } catch (Exception e) {
            LOGGER.error("getAll Organizations :: Exception ", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping
    public ResponseEntity<ApiResponse> insertOrganization(@Valid @RequestBody OrganizationRequestDto organizationRequestDto) throws CustomException {

        try {
            OrganizationResponseDto responseDTO = organizationService.insertOrganization(organizationRequestDto);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.Organization_CREATION_SUCCESSFULLY.getValue(), responseDTO),
                    HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Exception while update Organization info : {1}", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "/{organizationId}")
    public ResponseEntity<ApiResponse> updateOrganization(@Valid @PathVariable(value = "organizationId") Long organizationId, @RequestBody OrganizationRequestDto organizationRequestDto) throws CustomException {

        try {
            organizationRequestDto.setId(organizationId);
            OrganizationResponseDto responseDTO = organizationService.updateOrganization(organizationRequestDto);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.Organization_CREATION_SUCCESSFULLY.getValue(), responseDTO),
                    HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Exception while update Organization : {1}", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(value = "/{organizationId}")
    public ResponseEntity<ApiResponse> deleteOrganization(@PathVariable(value = "organizationId") Long organizationId) throws CustomException {
        try {
            organizationService.deleteOrganizationById(organizationId);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, ApiResponsesEnum.Organization_DELETED_SUCCESSFULLY.getValue()));
        } catch (Exception e) {
            LOGGER.error("Delete Organization  :: Exception ", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
