package com.demo.spring_security_jwt.controller;

import com.demo.spring_security_jwt.dto.ApiResponse;
import com.demo.spring_security_jwt.dto.GradeProjection;
import com.demo.spring_security_jwt.dto.GradeRequestDto;
import com.demo.spring_security_jwt.dto.GradeResponseDto;
import com.demo.spring_security_jwt.enums.ApiResponsesEnum;
import com.demo.spring_security_jwt.enums.ExceptionEnum;
import com.demo.spring_security_jwt.enums.FilterEnum;
import com.demo.spring_security_jwt.exception.CustomException;
import com.demo.spring_security_jwt.service.GradeService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/grade")
public class GradeController {

    private final GradeService gradeService;
    private static final Logger LOGGER = LoggerFactory.getLogger(GradeController.class);

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getAllGradeByUser(@RequestParam(value = "pageNo",
            defaultValue = "0") Integer pageNo,
                                                         @RequestParam(value = "pageSize",
                                                                 defaultValue = "10") Integer pageSize, @RequestParam(value = "searchValue", required = false,
            defaultValue = "") String searchValue,
                                                         @RequestParam(value = "sortBy",
                                                                 defaultValue = "ID") FilterEnum sortBy, @RequestParam(value = "sortAs",
            defaultValue = "ASC")
                                                         Sort.Direction sortAs, @PathVariable(value = "userId") Long userId) {

        try {
            // making pageable request
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortAs, sortBy.getValue()));
            Page<GradeProjection> gradeResponseDtoPage = gradeService.getGradeByUser(userId, searchValue.trim(), pageable);
            return ResponseEntity.ok(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.GRADE_RETRIEVAL_SUCCESSFULLY.getValue(), gradeResponseDtoPage));
        } catch (Exception e) {
            LOGGER.error("getAll Organizations :: Exception ", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping
    public ResponseEntity<ApiResponse> insertGrade(@Valid @RequestBody GradeRequestDto gradeRequestDto) throws CustomException {

        try {
            GradeResponseDto responseDTO = gradeService.insertGrade(gradeRequestDto);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.GRADE_INSERTED_SUCCESSFULLY.getValue(), responseDTO),
                    HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Exception while update Organization info : {1}", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<ApiResponse> updateGrade(@Valid @PathVariable(value = "userId") Long userId, @RequestBody GradeRequestDto gradeRequestDto) throws CustomException {

        try {
            gradeRequestDto.setId(userId);
            GradeResponseDto responseDTO = gradeService.updateGrade(gradeRequestDto);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK, ApiResponsesEnum.GRADE_UPDATED_SUCCESSFULLY.getValue(), responseDTO),
                    HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Exception while update user : {1}", e);
            throw new CustomException(ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
