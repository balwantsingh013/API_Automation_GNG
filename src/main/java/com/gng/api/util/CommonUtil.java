package com.gng.api.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.pojo.shared.PlanType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public static String getCurrentDateTimeFormatted() {
        // Format: dd-MM-yyyy HH:mm:ss (e.g., 10-07-2025 15:42:10)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
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

    public static String removeFieldsFromJson(Object object, String fieldsToRemove) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Convert the object to a Map
            Map<String, Object> objectMap = mapper.convertValue(object, Map.class);

            // Remove multiple fields dynamically
            Arrays.stream(fieldsToRemove.split(","))
                    .map(String::trim)  // Trim spaces to handle "field1, field2"
                    .forEach(objectMap::remove);

            // Convert back to JSON string
            return mapper.writeValueAsString(objectMap);
        } catch (Exception e) {
            log.error("Failed to remove fields from JSON: {}", e.getMessage(), e);
            return null;
        }
    }

    public static void normalizeBlankStringsToNull(Object object) {
        if (object == null) {
            return;
        }

        Class<?> clazz = object.getClass();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType() == String.class) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(object);
                        if (value != null && ((String) value).trim().isEmpty()) {
                            field.set(object, null);
                        }
                    } catch (IllegalAccessException e) {
                        // Optional: log or rethrow depending on your setup
                        System.err.println("Failed to process field: " + field.getName());
                    }
                }
            }
            clazz = clazz.getSuperclass(); // Handle inheritance
        }
    }

    public static void mapDataToRequestFromExcel(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        Class<?> srcClass = source.getClass();
        Class<?> tgtClass = target.getClass();

        // walk the class hierarchy for the source (handle superclasses)
        while (srcClass != null) {
            for (Field srcField : srcClass.getDeclaredFields()) {
                try {
                    srcField.setAccessible(true);
                    Object value = srcField.get(source);
                    if (value == null) {
                        continue;  // don't copy nulls
                    }
                    // try to find a field with the same name in the target class hierarchy
                    Field tgtField = findFieldInHierarchy(tgtClass, srcField.getName());
                    if (tgtField != null
                            && tgtField.getType().isAssignableFrom(srcField.getType())) {
                        tgtField.setAccessible(true);
                        tgtField.set(target, value);
                    }
                } catch (IllegalAccessException ignore) {
                    // ignore inaccessible fields
                }
            }
            srcClass = srcClass.getSuperclass();
        }
    }

    private static Field findFieldInHierarchy(Class<?> clazz, String name) {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }
}

