package com.gng.api.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class CommonUtil {

    private CommonUtil()
    {

    }

    public static void nullifyFields(Object object, String... fieldsToNullify) {
        if (object == null || fieldsToNullify == null) {
            log.warn("Object or fieldsToNullify is null. Skipping nullifyFields.");
            return;
        }

        Set<String> fieldsSet = new HashSet<>(Set.of(fieldsToNullify));

        try {
            Class<?> clazz = object.getClass();
            while (clazz != null) { // Traverse the class hierarchy
                for (Field field : clazz.getDeclaredFields()) {
                    if (fieldsSet.contains(field.getName())) {
                        field.setAccessible(true);
                        if (!field.getType().isPrimitive()) {
                            field.set(object, null);
                        } else {
                            log.warn("Cannot nullify primitive field: {}", field.getName());
                        }
                    }
                }
                clazz = clazz.getSuperclass(); // Move to superclass
            }
        } catch (IllegalAccessException e) {
            log.error("Failed to nullify field: {}", e.getMessage(), e);
        }
    }

    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HHmmss");
        return LocalDateTime.now().format(formatter);
    }


    public static String removeFieldFromJson(Object object, String fieldToRemove) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Convert the object to a Map
            Map<String, Object> objectMap = mapper.convertValue(object, Map.class);

            // Remove the field dynamically
            objectMap.remove(fieldToRemove);

            // Convert back to JSON string
            return mapper.writeValueAsString(objectMap);
        } catch (Exception e) {
            log.error("Failed to remove field from JSON: {}", e.getMessage(), e);
            return null;
        }
    }
}

