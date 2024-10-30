package com.gng.api.util;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class FakerUtil {

    private static Faker faker;

    private FakerUtil() {
    }

    private static Faker getFaker() {
        faker = Optional.ofNullable(faker).orElseGet(Faker::new);
        return faker;
    }

    public static Integer getRandomNumber(int length) {
        Integer value = Integer.parseInt(getFaker().number().digits(length));
        log.info("Random Number: {}", value);
        return value;
    }

    public static String getRandomNumericString(int length) {
        String value = String.valueOf(getFaker().number().digits(length));
        log.info("Random Numeric String: {}", value);
        return value;
    }

    public static String getRandomString(int length) {
        String value = getFaker().lorem().characters(length);
        log.info("Random String: {}", value);
        return value;
    }

}
