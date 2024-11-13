package com.example.manyToOne.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class OrganizationRequestDto {

    private Long id;
    private String organizationName;
    private String address;
    private Boolean status;

    private Boolean deactivate;

}
