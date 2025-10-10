package com.gng.api.pages.poc.CreateAccountNotePage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.AccountsPojo.createAccountNote.CreateAccountNoteRequest;
import com.gng.api.steps.poc.CreateAccountNote.CreateAccountNoteApiLabel;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.FakerDataGenerator;
import io.cucumber.datatable.DataTable;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.constants.DBConstant.UCBACCT_CUST_CODE;
import static com.gng.api.constants.DBConstant.UCBACCT_PREM_CODE;
import static com.gng.api.util.LogUtil.logInfo;
import static com.gng.api.pages.poc.CreateAccountNotePage.CreateAccountNoteLabels.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
public class CreateAccountNoteHelper {
    private final TestContext testContext;

    private static final String TOKEN = "|~";
    private static final Pattern TOKEN_PATTERN = Pattern.compile(Pattern.quote(TOKEN));

    public CreateAccountNoteHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    CreateAccountNoteRequest preparePayload(CreateAccountNoteApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(CreateAccountNoteApiLabel.create_account_note)
                ? CreateAccountNoteApiLabel.create_account_note.toString()
                : null;
        return BasePage.deserializeJsonToPojo(jsonFileName, CreateAccountNoteRequest.class);
    }

    public void setParametersBasedOnTypeNegative(CreateAccountNoteRequest payload, CreateAccountNoteApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (testCondition) {
            /* Request ID */
            case REQUEST_ID_MISSING_NEGATIVE_TC65 -> payload.setRequestID("");
            case REQUEST_ID_DUPLICATE_NEGATIVE_TC66 ->  payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());

            /* Customer Code */
            case CUSTOMER_CODE_NULL_NEGATIVE_TC67 -> payload.setCustomerCode("");
            case CUSTOMER_CODE_LENGTH_GT9_NEGATIVE_TC69 -> payload.setCustomerCode(FakerDataGenerator.generateDigits(10));

            /* Premises Code */
            case PREMISES_CODE_NULL_NEGATIVE_TC68 -> payload.setPremisesCode("");
            case PREMISES_CODE_LENGTH_GT7_NEGATIVE_TC70 -> payload.setPremisesCode(FakerDataGenerator.generateDigits(8));

            case ACCOUNT_COMBINATION_INVALID_NEGATIVE_TC71 -> {
                Map<String, Object> row;
                row = ApplicationContext.get().getDbAction()
                        .custCodeParamCodeAGLCAccNo_WitEtcGPP(GlobalEnums.PlanCode.RGB.getValue(), GlobalEnums.CustomerType.RESIDENTIAL.getValue());
                if (row == null || row.isEmpty()) {
                    throw new IllegalStateException("Empty DB row for " + testCondition);
                }
                payload.setPremisesCode(row.get("GTBTRNH_PREM_CODE").toString());
            }

            case NOTE_TYPE_CODE_NULL_NEGATIVE_TC72 -> payload.setNoteTypeCode("");
            case NOTE_TEXT_NULL_NEGATIVE_TC73 -> payload.setNoteText("");
            case ORIGIN_NULL_NEGATIVE_TC74 -> payload.setOrigin("");
            case EXPIRATION_DATE_INVALID_FORMAT_NEGATIVE_TC75 -> {
                payload.setSuspenseDate(LocalDate.now().plusDays(20).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                payload.setExpirationDate(LocalDate.now().plusDays(40).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            }
            case SUSPENSE_DATE_INVALID_FORMAT_NEGATIVE_TC76 -> {
                payload.setSuspenseDate(LocalDate.now().plusDays(20).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                payload.setExpirationDate(LocalDate.now().plusDays(40).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            }

            case SERVICE_NUMBER_NOT_FOUND_FOR_PREMISES_NEGATIVE_TC77 -> payload.setServiceNumber(FakerDataGenerator.generateDigits(3));
            case SERVICE_NUMBER_INVALID_FORMAT_NEGATIVE_TC78 -> payload.setServiceNumber(FakerDataGenerator.generateAlphanumeric(3));
            case NOTE_TYPE_INVALID_NEGATIVE_TC79 -> payload.setNoteTypeCode(GlobalEnums.InvalidValues.INVALID_ACCOUNT_NOTE.getValue());

            default -> log.warn("No negative mutation implemented for {}", testCondition);
        }
    }

    public void setParametersBasedOnTypePositive(CreateAccountNoteRequest payload, String noteText, CreateAccountNoteApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setUserIDRemind("");

        switch (testCondition) {
            case CREATE_NOTE_ACCT_POSITIVE_TC80 -> {
                payload.setNoteText(noteText);
            }
            case CREATE_NOTE_PMT_RPT_POSITIVE_TC81 -> {
                payload.setNoteTypeCode("PMTRPT");
                payload.setUserIDRemind("SYSTEM");

                payload.setNoteText("Payment received 10/01 via IVR|~ Ref: 555001");
            }
            case CREATE_NOTE_IVR_NPA_POSITIVE_TC82 -> {
                payload.setNoteTypeCode("IVRNPA");
                payload.setNoteText("IVR NPA/NXX captured 404-555|~ 1212");
            }
            case CREATE_NOTE_IVR_NPA_ALT_POSITIVE_TC83 -> {
                payload.setNoteTypeCode("IVRNPA");
                payload.setNoteText("IVR NPA/NXX captured 770-555|~ 8989 ext|~ 42");
            }
            default -> log.warn("No positive mutation implemented for {}", testCondition);
        }
    }

public void verifyNoteCreatedWithCorrectLines(String noteTextFromExamples, String testCondition) {
    String expected = noteTextFromExamples.replace("\\|~", "|~");
    getNoteSequenceNumberData();
    List<String> storedValues = getNoteSequenceNumberData();
    assertRowsMatchTokenCount(expected, storedValues);

}
    public List<String> getNoteSequenceNumberData(){
        String seq = testContext.getCreateAccountNoteResponse().getData().getNoteSequenceNumber();

        List<Map<String, Object>> rows = ApplicationContext.get()
                .getDbAction()
                .getNoteSequenceNumber(seq);

        if (rows == null || rows.isEmpty()) {
            throw new IllegalStateException("No DB rows returned for note sequence " + seq);
        }

        final String VALUE_KEY = firstExistingKey(rows,"UCBNOTE_SEQ_NUMBER");

        return rows.stream()
                .map(r -> String.valueOf(r.get(VALUE_KEY)))
                .toList();
    }

    private static String firstExistingKey(List<Map<String, Object>> rows, String... candidates) {
        for (String k : candidates) {
            if (rows.getFirst().containsKey(k)) return k;
        }
        throw new IllegalStateException("Could not find note-value column in DB rows.");
    }

    public static int countTokens(String noteTextWithTokens) {
        if (noteTextWithTokens == null || noteTextWithTokens.isEmpty()) return 0;
        return (int) TOKEN_PATTERN.matcher(noteTextWithTokens).results().count();
    }

    public static void assertRowsMatchTokenCount(String expectedNoteText, List<String> storedRows) {
        int tokenCount = countTokens(expectedNoteText);
        int expectedRows = tokenCount + 1;

        assertThat("Stored note rows should equal |~ token count + 1",
                storedRows, hasSize(expectedRows));
    }

    public void setupRequestData(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        setTestContextData(data);
    }

    public CreateAccountNoteLabels getApiLabelForParam(String param) {
        return switch (param) {
            case "PremisesCode" -> INVALID_PREM_CODE_LENGTH;
            case "CustomerCode" -> INVALID_CUSTOMER_CODE_LENGTH;
            default -> throw new IllegalStateException("Invalid param: " + param);
        };
    }

    public CreateAccountNoteRequest getApiPayload(CreateAccountNoteLabels apiName, CreateAccountNoteRequest request) {
        logInfo("Get Api Payload");
        return switch (apiName) {
            case HAPPY_FLOW -> buildHappyFlowPayload(request);
            case MISSING_REQUEST_ID -> buildMissingRequestIdPayload(request);
            case NULL_CUSTOMER_CODE -> buildNullCustomerCodePayload(request);
            case NULL_NOTE_TYPE_CODE -> buildNullNoteTypeCodePayload(request);
            case NULL_NOTE_TEXT -> buildNullNoteTextPayload(request);
            case NULL_ORIGIN -> buildNullOriginPayload(request);
            case INVALID_CUSTOMER_CODE_LENGTH -> buildInvalidCustomerCodeLengthPayload(request);
            case INVALID_PREM_CODE_LENGTH -> buildInvalidPremCodeLengthPayload(request);
            case INVALID_EXPIRATION_DATE -> buildInvalidExpirationDatePayload(request);
            case NONEXISTENT_SERVICE_NO_PREM_CODE -> buildNonExistentServiceNumberPayload(request);
            case INVALID_SERVICE_NO_FORMAT -> buildInvalidServiceNumberFormatPayload(request);
            case NONEXISTENT_NOTE_TYPE -> buildNonExistentNoteTypePayload(request);
            case NONEXISTENT_CUST_PREM_CODE -> buildNonExistentCustomerPremCodePayload(request);
        };
    }

    private void setTestContextData(Map<String, String> data) {
        testContext.setServiceNumber(data.get("serviceNumber"));
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
    }

    private CreateAccountNoteRequest buildHappyFlowPayload(CreateAccountNoteRequest request) {
        request.setRequestID(UUID.randomUUID().toString());
        request.setCustomerCode(testContext.getCustomerCode());
        request.setPremisesCode(testContext.getPremisesCode());
        request.setServiceNumber(testContext.getServiceNumber());
        request.setNoteTypeCode(testContext.getNoteTypeCode());
        request.setNoteText(testContext.getNoteText());
        request.setOrigin(testContext.getOrigin());
        return request;
    }

    private CreateAccountNoteRequest buildMissingRequestIdPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        CommonUtil.nullifyFields(request, "requestID");
        return request;
    }

    private CreateAccountNoteRequest buildNullCustomerCodePayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setCustomerCode(null);
        return request;
    }

    private CreateAccountNoteRequest buildNullNoteTypeCodePayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setNoteTypeCode(null);
        return request;
    }

    private CreateAccountNoteRequest buildNullNoteTextPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setNoteText(null);
        return request;
    }

    private CreateAccountNoteRequest buildNullOriginPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setOrigin(null);
        return request;
    }

    private CreateAccountNoteRequest buildInvalidCustomerCodeLengthPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setCustomerCode(FakerDataGenerator.getRandomNumericString(10));
        return request;
    }

    private CreateAccountNoteRequest buildInvalidPremCodeLengthPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setPremisesCode(FakerDataGenerator.getRandomNumericString(8));
        return request;
    }

    private CreateAccountNoteRequest buildInvalidExpirationDatePayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setExpirationDate(testContext.getExpirationDate());
        return request;
    }

    private CreateAccountNoteRequest buildNonExistentServiceNumberPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setServiceNumber(FakerDataGenerator.getRandomNumericString(4));
        return request;
    }

    private CreateAccountNoteRequest buildInvalidServiceNumberFormatPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setServiceNumber(FakerDataGenerator.getRandomNumericString(5));
        return request;
    }

    private CreateAccountNoteRequest buildNonExistentNoteTypePayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setNoteTypeCode(FakerDataGenerator.getRandomString(3));
        return request;
    }

    private CreateAccountNoteRequest buildNonExistentCustomerPremCodePayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setCustomerCode(FakerDataGenerator.getRandomNumericString(8));
        return request;
    }
}
