package com.example.springSecurity.enums;

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
