package com.example.springSecurity.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.id.IntegralDataTypeHolder;

@Setter
@Getter
public class GradeRequestDto {


    private Long id;
    private Integer totalGrade;
    private Long userId;
}
