package com.gng.api.util;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@UtilityClass
public class CommonUtil {

    /**
     * Use Reflection to Skip Fields Dynamically.
     * This involves modifying the original object before it's passed as the request body.
     */
    public static void nullifyFields(Object object, String... fieldsToNullify) {
        try {
            for (String fieldName : fieldsToNullify) {
                Field field = object.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, null);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }

    public static String getRandomNumber(int digits) {
        Faker faker = new Faker();
        return faker.number().digits(digits);
    }

    public static String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("ddMMyy-hhmmss");
        return dateFormat.format(new Date());
    }
}
