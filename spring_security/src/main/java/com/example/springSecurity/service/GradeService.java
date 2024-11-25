package com.example.springSecurity.service;

import com.example.springSecurity.dto.GradeProjection;
import com.example.springSecurity.dto.GradeRequestDto;
import com.example.springSecurity.dto.GradeResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GradeService {
    GradeResponseDto insertGrade(@Valid GradeRequestDto gradeRequestDto);

    GradeResponseDto updateGrade(GradeRequestDto gradeRequestDto);

    Page<GradeProjection> getGradeByUser(Long userId, String trim, Pageable pageable);

}
