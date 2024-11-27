package com.demo.spring_security_jwt.service;

import com.demo.spring_security_jwt.dto.GradeProjection;
import com.demo.spring_security_jwt.dto.GradeRequestDto;
import com.demo.spring_security_jwt.dto.GradeResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GradeService {
    GradeResponseDto insertGrade(@Valid GradeRequestDto gradeRequestDto);

    GradeResponseDto updateGrade(GradeRequestDto gradeRequestDto);

    Page<GradeProjection> getGradeByUser(Long userId, String trim, Pageable pageable);

}
