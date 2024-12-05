package com.demo.spring_security_jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginRequestDto {

    private String email;
    private String password;


}
