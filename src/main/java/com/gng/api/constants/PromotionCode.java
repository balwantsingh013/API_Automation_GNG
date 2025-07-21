package com.gng.api.constants;

import lombok.Getter;

@Getter
public enum PromotionCode {
    DEALS("DEALS"),
    SAVE100("SAVE100"),
    AAA("AAA");

    private final String value;

    PromotionCode(String value) {
        this.value = value;
    }
}
