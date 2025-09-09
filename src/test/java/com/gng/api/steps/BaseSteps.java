package com.gng.api.steps;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.*;

@Slf4j
public class BaseSteps {
    private final TestContext testContext;

    @ParameterType("true|false")
    public Boolean booleanVal(String value) {
        return Boolean.valueOf(value);
    }

    public BaseSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Then("verify response code of {string} Api is {int}")
    public void verify_response_code_of_api_is(String apiName, Integer statusCode) {
        verifyResponseCode(apiName, statusCode);
    }

    @And("response should have ErrorCode {int} and ErrorMessage {string}")
    public void responseShouldHaveErrorCodeAndErrorMessage(int errorCode, String errorMessage) {
        verifyErrorCodeAndMessage(errorCode, errorMessage);
    }

    @And("response should have a SSP eligible as {string} and {string}")
    public void responseShouldHaveWarningForSSPEligibility(String sspEligibility, String Warning){
        verifySSPEligibilityAndWarning(booleanVal(sspEligibility),Warning);
    }

    @And("response should return numberOfMatches as {int}")
    public void responseShouldReturnNumberOfMatchesAs(int numberOfMatches) {
        verifyNumberOfMatches(numberOfMatches);
    }

    @And("response should have {string} to {string}")
    public void responseShouldHavePaymentFieldsAs(String field, String value){
        verifyPaymentFields(field,value);
    }

    @And("response should have {string} as {string}")
    public void enrollmentStateresponseShouldHaveFieldAs(String field, String value){
        verifyFieldInResponse(field, value);
    }

    @And("response should have {string} flag as {string}")
    public void responseShouldHaveFlagAs(String flag, String value){
        verifyFlagValueInResponse(flag, booleanVal(value));
    }

    @And("response should have {int} turnOffReasons")
    public void responseShouldHaveTurnOffReasons(int count) {
        verifyNumberOfTurnOffReasons(count);
    }

    @And("the response should contain the following turnOffReasons:")
    public void verifyTurnOffReasons(DataTable dataTable) {
        List<Map<String, String>> expectedReasons = dataTable.asMaps(String.class, String.class);
        validateTurnOffReasons(expectedReasons);
    }

    @And("the response should contain the following reasonForTurnOffAlerts:")
    public void verifyReasonForTurnOffAlerts(DataTable dataTable) {
        List<Map<String, String>> expectedAlerts = dataTable.asMaps(String.class, String.class);
        validateReasonForTurnOffAlerts(expectedAlerts);
    }

    @And("the response should contain the following plans:")
    public void verifyPlans(DataTable dataTable) {
        List<Map<String, String>> expectedPlans = dataTable.asMaps(String.class, String.class);
        validatePlans(expectedPlans);
    }

    @And("response should have the following roles")
    public void responseShouldHaveTheFollowingRoles(DataTable dataTable) {
        List<Map<String, String>> expectedRoles = dataTable.asMaps(String.class, String.class);
        verifyRoleDetails(expectedRoles);
    }

    private void verifyRoleDetails(List<Map<String, String>> expectedRoles) {
        Response response = testContext.getResponse();
        List<Map<String, String>> actualRoles = response.jsonPath().getList("data.roles");

        // Step 1: Validate each expected role is present in the response
        for (Map<String, String> expectedRole : expectedRoles) {
            String expectedRoleID = expectedRole.get("roleID");
            String expectedRoleDescription = expectedRole.get("roleDescription");

            boolean roleFound = actualRoles.stream()
                    .anyMatch(actualRole -> expectedRoleID.equals(actualRole.get("roleID")) &&
                                            expectedRoleDescription.equals(actualRole.get("roleDescription")));

            assertThat("Expected role with ID '" + expectedRoleID + "' and description '" + expectedRoleDescription + "' not found", roleFound);
        }

        // Step 2: Validate role IDs match with database
        List<Map<String, Object>> dbRoleRecords = ApplicationContext.get().getDbAction().getUserRoleIDs("autotester");

        List<String> dbRoleIDs = dbRoleRecords.stream()
                .map(record -> Objects.toString(record.get("role_id"), "").trim())
                .filter(roleId -> !roleId.isEmpty())
                .toList();

        List<String> apiRoleIDs = actualRoles.stream()
                .map(role -> role.get("roleID"))
                .filter(Objects::nonNull)
                .map(String::trim)
                .toList();

        assertThat("Mismatch between role IDs from API and database",
                new HashSet<>(apiRoleIDs).equals(new HashSet<>(dbRoleIDs)));
    }



    private void validatePlans(List<Map<String, String>> expectedPlans) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> actualPlans = response.jsonPath().getList("data.plans");

        for (Map<String, String> expected : expectedPlans) {
            String expectedCode = normalize(expected.get("planCode"));
            String expectedDesc = normalize(expected.get("planDescription"));

            boolean matchFound = actualPlans.stream().anyMatch(plan -> {
                String actualCode = normalize(plan.get("planCode"));
                String actualDesc = normalize(plan.get("planDescription"));
                return expectedCode.equals(actualCode) && expectedDesc.equals(actualDesc);
            });

            assertThat("Plan not found: code=" + expectedCode + ", description=" + expectedDesc, matchFound);
        }
    }

    private void validateReasonForTurnOffAlerts(List<Map<String, String>> expectedAlerts) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> actualReasons = response.jsonPath().getList("data.turnOffReasons");

        for (Map<String, String> expected : expectedAlerts) {
            String reason = normalize(expected.get("reasonForTurnOff"));
            String expectedAlert = normalize(expected.get("reasonForTurnOffAlert"));

            String actualAlert = actualReasons.stream()
                    .filter(r -> reason.equals(normalize(r.get("reasonForTurnOff"))))
                    .map(r -> normalize(r.get("reasonForTurnOffAlert")))
                    .findFirst()
                    .orElse("");

            assertThat("Unexpected reasonForTurnOffAlert for: " + reason, actualAlert, equalTo(expectedAlert));
        }
    }

    private void verifyNumberOfTurnOffReasons(int count) {
        Response response = testContext.getResponse();
        assertThat("Unexpected number of turnOffReasons returned", response.jsonPath().getList("data.turnOffReasons").size(), equalTo(count));
    }

    private String normalize(Object value) {
        return Optional.ofNullable(value)
                .map(Object::toString)
                .map(s -> s.replaceAll("\\u00A0", " "))
                .map(String::trim)
                .orElse("");
    }

    private void validateTurnOffReasons(List<Map<String, String>> expectedReasons) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> actualReasons = response.jsonPath().getList("data.turnOffReasons");

        for (Map<String, String> expected : expectedReasons) {
            String expectedReason = normalize(expected.get("reasonForTurnOff"));
            String expectedSubReason = normalize(expected.get("subReasonForTurnOff"));

            boolean matchFound = actualReasons.stream().anyMatch(actual -> {
                String actualReason = normalize(actual.get("reasonForTurnOff"));
                String actualSubReason = normalize(actual.get("subReasonForTurnOff"));
                return expectedReason.equals(actualReason) && expectedSubReason.equals(actualSubReason);
            });

            assertThat("Expected turnOffReason not found: " + expectedReason + " / " + expectedSubReason, matchFound);
        }
    }

    private void verifyFlagValueInResponse(String flag, Boolean expectedValue) {
        Response response = testContext.getResponse();

        List<Boolean> flagValues = response.jsonPath().getList("data.accounts." + flag, Boolean.class);
        assertThat("Expected at least one account with " + flag + " = " + expectedValue,
                flagValues, hasItem(expectedValue));
    }

    private void verifyFieldInResponse(String field, String value) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> accounts = response.jsonPath().getList("data.accounts");

        boolean matchFound = accounts.stream()
                .anyMatch(account -> value.equals(String.valueOf(account.get(field))));

        assertThat("Expected value not found in any account for field: " + field, matchFound, is(true));
    }

    private void verifyResponseCode(String apiName, Integer statusCode) {
        assertThat("Invalid Response Code for API: " + apiName,
                testContext.getResponse().statusCode(),
                equalTo(statusCode));
    }

    private void verifyErrorCodeAndMessage(int errorCode, String errorMessage) {
        Response response = testContext.getResponse();
        String actualErrorMessage = response.jsonPath().getString("errorMessage");

        // Normalize expected message
        boolean containsPipe = errorMessage.contains("[PIPE]");
        String normalizedExpectedMessage = errorMessage.replace("[PIPE]", "|");

        // Validate error code
        assertThat("Incorrect ErrorCode returned",
                response.jsonPath().getInt("errorCode"),
                equalTo(errorCode));

        // Conditional validation based on presence of [PIPE]
        if (containsPipe) {
            assertThat("ErrorMessage does not contain expected content",
                    actualErrorMessage,
                    containsString(normalizedExpectedMessage));
        } else {
            assertThat("Incorrect ErrorMessage returned",
                    actualErrorMessage,
                    equalTo(normalizedExpectedMessage));
        }
    }

    private void verifySSPEligibilityAndWarning(boolean sspEligibility, String warning) {
        Response response = testContext.getResponse();

        // Validate ssp Eligibility flag at data.sspEligible
        assertThat("Incorrect Eligibility flag returned",
                response.jsonPath().getBoolean("data.sspEligible"),
                equalTo(sspEligibility));

        // Validate the warning message at data.outMessage
        assertThat("Incorrect Warning message returned",
                response.jsonPath().getString("data.outMessage"),
                equalTo(warning));
    }


    private void verifyNumberOfMatches(int expectedMatches) {
        Response response = testContext.getResponse();

        assertThat("Unexpected number of matches returned",
                response.jsonPath().getInt("data.numberOfMatches"),
                equalTo(expectedMatches));
    }

    private void verifyPaymentFields(String field, String value){
        Response response = testContext.getResponse();
        switch (value.toLowerCase()) {
            case "exist" -> {
                assertTrue("Unexpected " + field + " amount",
                        response.jsonPath().getDouble("data.accounts[0]." + field) > 0);
            }
            case "not empty" -> {
                assertNotNull("Unexpected " + field + " is null", response.jsonPath().getString("data.accounts[0]." + field));
                assertFalse("Unexpected " + field + " is empty", response.jsonPath().getString("data.accounts[0]." + field).trim().isEmpty());
            }
            case "empty" ->{
                assertTrue("Unexpected " + field + " is empty", response.jsonPath().getString("data.accounts[0]." + field).trim().isEmpty());
            }
            case "null" -> {
                assertNull("Unexpected " + field + " is not null", response.jsonPath().getString("data.accounts[0]." + field));
            }
            case "not exist" -> {
                assertEquals("Unexpected " + field + " amount", 0.0,
                        response.jsonPath().getDouble("data.accounts[0]." + field));
            }
        }
    }
}
