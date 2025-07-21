package com.gng.api.constants;

import lombok.Getter;

@Getter
public enum CustomerType {
    RESIDENTIAL("RS"),
    COMMERCIAL("CM"),
    INVALID("IN");

    private final String value;

    CustomerType(String value) {
        this.value = value;
    }
}
