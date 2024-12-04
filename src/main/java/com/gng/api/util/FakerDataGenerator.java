package com.gng.api.util;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FakerDataGenerator {
    private static final Faker faker = new Faker(Locale.forLanguageTag("en-US"));

    private FakerDataGenerator()
    {
        // Private constructor to prevent instantiation
    }

    // Names
    public static String generateFullName() {
        return faker.name().fullName();
    }

    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generateLastName() {
        return faker.name().lastName();
    }

    public static String generateMiddleName() {
        return faker.name().nameWithMiddle();
    }

    // Email variations
    public static String generateEmail(String prefix, String domain) {
        return prefix + "." + faker.name().username() + "@" + domain;
    }

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateBusinessEmail() {
        return faker.internet().emailAddress(generateFullName().replaceAll("\\s", ".").toLowerCase());
    }

    // String generators
    public static String generateString(int length) {
        return faker.regexify("[a-zA-Z]{" + length + "}");
    }

    public static String getRandomNumericString(int length) {
        String value = String.valueOf(faker.number().digits(length));
        log.info("Random Numeric String: {}", value);
        return value;
    }

    public static String getRandomString(int length) {
        String value = faker.lorem().characters(length);
        log.info("Random String: {}", value);
        return value;
    }


    public static String generateLowerCaseString(int length) {
        return faker.regexify("[a-z]{" + length + "}");
    }

    public static String generateUpperCaseString(int length) {
        return faker.regexify("[A-Z]{" + length + "}");
    }

    // Number generators
    public static String generateDigits(int length) {
        return faker.number().digits(length);
    }

    public static int generateNumber(int min, int max) {
        return faker.number().numberBetween(min, max);
    }

    public static double generateDouble(double min, double max) {
        return faker.number().randomDouble(3, (long)min, (long)max);
    }

    // Alphanumeric generators
    public static String generateAlphanumeric(int length) {
        return faker.regexify("[a-zA-Z0-9]{" + length + "}");
    }

    public static String generateAlphanumericWithSpecialChars(int length) {
        return faker.regexify("[a-zA-Z0-9!@#$%^&*]{" + length + "}");
    }

    public static String generateStringWithSpecialChars(int length) {
        return faker.regexify("[a-zA-Z!@#$%^&*]{" + length + "}");
    }


    // Phone numbers
    public static String generatePhoneNumber(String format) {
        return faker.numerify(format);
    }

    public static String generatePhoneNumber() {
        return faker.phoneNumber().cellPhone();
    }

    // Address related
    public static String generateStreetAddress() {
        return faker.address().streetAddress();
    }

    public static String generateCity() {
        return faker.address().city();
    }

    public static String generateState() {
        return faker.address().state();
    }

    public static String generateZipCode() {
        return faker.address().zipCode();
    }

    public static String generateCountry() {
        return faker.address().country();
    }

    // Business related
    public static String generateCompanyName() {
        return faker.company().name();
    }

    public static String generateJobTitle() {
        return faker.job().title();
    }

    // Internet related
    public static String generateUsername(int length) {
        return faker.regexify("[a-z]{" + length + "}");
    }

    public static String generatePassword(int minLength, int maxLength, boolean includeSpecial) {
        if (includeSpecial) {
            return faker.internet().password(minLength, maxLength, true, true, true);
        }
        return faker.internet().password(minLength, maxLength);
    }

    public static String generateUrl() {
        return faker.internet().url();
    }

    public static String generateIpV4Address() {
        return faker.internet().ipV4Address();
    }

    // Date and time
    public static String generateDate(String format) {
        return faker.date().birthday().toString();
    }

    public static String generateFutureDate(int daysInFuture, String... format) {
        Date date = faker.date().future(daysInFuture, TimeUnit.DAYS);
        String dateFormat = format.length > 0 ? format[0] : "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }


    // Custom format generators
    public static String generateCustomPattern(String pattern) {
        return faker.regexify(pattern);
    }

    // UUID and identifiers
    public static String generateUUID() {
        return faker.internet().uuid();
    }
}

