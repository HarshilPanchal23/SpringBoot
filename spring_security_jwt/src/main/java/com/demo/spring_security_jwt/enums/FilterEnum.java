package com.demo.spring_security_jwt.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilterEnum {

    ID("id"),
    NAME("name"),
    ;
    private final String value;

}
