package com.example.core.enums;

import lombok.AllArgsConstructor;
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
