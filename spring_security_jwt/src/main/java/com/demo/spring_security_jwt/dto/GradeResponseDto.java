package com.demo.spring_security_jwt.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GradeResponseDto {

    private Integer totalGrade;
    private UserResponseDto userResponseDto;
}
