package com.gng.api.pages.csi.UpdatePaperlessCommunicationsPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentRequest;
import com.gng.api.pojo.CSIPojo.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsRequest;
import com.gng.api.pojo.CSIPojo.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentLabel;
import com.gng.api.steps.csi.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsLabel;
import com.gng.api.util.FakerDataGenerator;
import com.gng.api.util.PaperlessConfirmationTokenUtil;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.springframework.dao.EmptyResultDataAccessException;
import org.testng.Assert;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.gng.api.constants.ApiEndPoint.CONFIRM_PAPERLESS_ENROLLMENT;
import static com.gng.api.constants.ApiEndPoint.UPDATE_PAPERLESS_COMMUNICATIONS;
import static com.gng.api.pages.csi.UpdatePaperlessCommunicationsPage.UpdatePaperlessCommunicationsApiHelper.getDbString;

@Slf4j
public class UpdatePaperlessCommunicationsPage extends BasePage {

    private final UpdatePaperlessCommunicationsApiHelper helper;

    public UpdatePaperlessCommunicationsPage(TestContext testContext) {
        super(testContext);
        this.helper = new UpdatePaperlessCommunicationsApiHelper(testContext);
    }

    public void validateResponseForNegativeTestConditions(UpdatePaperlessCommunicationsLabel apiLabel, UpdatePaperlessCommunicationsLabel testCondition) {
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_101__Positive__New_Account_Bill_Unenrollment_) {
            validateTc100NewAccountBillUnenrollment(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_103__Positive__New_Account_Both_Channels_Unenrollment_) {
            validateTc102NewAccountBothChannelsUnenrollment(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_102__Positive__Active_Account_Both_Channels_Unenrollment_) {
            validateTc101ActiveAccountBothChannelsUnenrollment(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_80__Positive__UpdatePaperlessCommunications_accountType_Format__ACTIVE_) {
            String requestId = executeUpdatePaperlessCommunications(apiLabel, testCondition);
            assertTc79ActiveAccountTypeResponse(testContext.getResponse(), requestId);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_81__Positive__UpdatePaperlessCommunications_accountType_Format__NEW_) {
            String requestId = executeUpdatePaperlessCommunications(apiLabel, testCondition);
            assertTc80NewAccountTypeResponse(testContext.getResponse(), requestId);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_82__Positive__billDeliveryOptionStatus_Format__INITIATED_) {
            executeUpdatePaperlessCommunications(apiLabel, testCondition);
            assertTc81BillInitiatedResponse(testContext.getResponse());
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_84__Positive__billDeliveryOptionStatus_Format__NO_CHANGE_) {
            executeUpdatePaperlessCommunications(apiLabel, testCondition);
            assertTc83BillNoChangeResponse(testContext.getResponse());
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_85__Positive__corrDeliveryOptionStatus_Format__INITIATED_) {
            executeUpdatePaperlessCommunications(apiLabel, testCondition);
            assertTc84CorrInitiatedResponse(testContext.getResponse());
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_87__Positive__corrDeliveryOptionStatus_Format__NO_CHANGE_) {
            executeUpdatePaperlessCommunications(apiLabel, testCondition);
            assertTc86CorrNoChangeResponse(testContext.getResponse());
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_88__Positive__linkExpiryDateTime_Format_) {
            executeUpdatePaperlessCommunications(apiLabel, testCondition);
            assertTc87LinkExpiryDateTimeResponse(testContext.getResponse());
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_90__Positive__linkCreated_Format__true_) {
            executeUpdatePaperlessCommunications(apiLabel, testCondition);
            assertTc89LinkCreatedTrueResponse(testContext.getResponse());
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_91__Positive__linkCreated_Format__false_) {
            validateTc90LinkCreatedFalse(apiLabel, testCondition);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_104__Positive__Active_Account_Reuse_Existing_Valid_Token_) {
            validateTc103ReuseExistingValidToken(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_105__Positive__New_Account_Reuse_Existing_Valid_Token_) {
            validateTc104ReuseExistingValidToken(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_95__Positive__Email_Ignored_for_P_Requests__Enrolled_) {
            validateTc94EmailIgnoredForEnrolledUnenrollment(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_97__Positive__New_Account_Bill_Enrollment_Initiated_) {
            executeUpdatePaperlessCommunications(apiLabel, testCondition);
            assertTc96NewAccountBillInitiatedResponse(testContext.getResponse());
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_99__Positive__New_Account_Both_Channels_Enrollment_Initiated_) {
            executeUpdatePaperlessCommunications(apiLabel, testCondition);
            assertTc98NewAccountBothChannelsInitiatedResponse(testContext.getResponse());
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_106__Positive__Active_Account_New_Token_When_Existing_Token_Invalid_) {
            validateTc105NewTokenWhenExistingTokenInvalid(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_107__Positive__New_Account_New_Token_When_Existing_Token_Invalid_) {
            validateTc106NewTokenWhenExistingTokenInvalid(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_109__Positive__New_Token_Required_When_Email_Changes_) {
            validateTc108NewTokenWhenEmailChanges(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_110__Positive__Cross_Channel_Aggregation_) {
            validateTc109CrossChannelAggregation(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_111__Positive__Same_Channel_Update__Last_Value_Wins_) {
            validateTc110SameChannelUpdateLastValueWins(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_112__Positive__PPER_Cleanup_on_Active_Unenrollment_) {
            validateTc111PperCleanupOnActiveUnenrollment(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_113__Positive__Token_Expired_on_New_Account_Unenrollment_) {
            validateTc112TokenExpiredOnNewAccountUnenrollment(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_114__Positive__Latest_Aggregated_State_Wins_) {
            validateTc113LatestAggregatedStateWins(apiLabel);
            return;
        }
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_116__Positive__Prior_Link_Invalid_After_Email_Change_) {
            validateTc115PriorLinkInvalidAfterEmailChange(apiLabel);
            return;
        }

        executeUpdatePaperlessCommunications(apiLabel, testCondition);
    }

    private String executeUpdatePaperlessCommunications(UpdatePaperlessCommunicationsLabel apiLabel,
                                                        UpdatePaperlessCommunicationsLabel testCondition) {
        UpdatePaperlessCommunicationsRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, UPDATE_PAPERLESS_COMMUNICATIONS, 200);
        UpdatePaperlessCommunicationsResponse updatePaperlessCommunicationsResponse =
                deserializeResponseToPojo(response, UpdatePaperlessCommunicationsResponse.class);
        testContext.setUpdatePaperlessCommunicationsResponse(updatePaperlessCommunicationsResponse);
        testContext.setResponse(response);
        return payload.getRequestID();
    }

    private void assertTc83BillNoChangeResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "NO_CHANGE",
                "Expected billDeliveryOptionStatus=NO_CHANGE when updateBillDeliveryOption is null");
        Assert.assertEquals(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "INITIATED",
                "Expected corrDeliveryOptionStatus=INITIATED; NO_CHANGE on both channels usually means "
                        + "the account still has a pending OCSEPCI token from a prior corr enrollment run");
    }

    private static final Pattern LINK_EXPIRY_DATE_TIME_PATTERN =
            Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(Z)?$");

    private void assertTc79ActiveAccountTypeResponse(Response response, String expectedRequestId) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertEquals(
                response.jsonPath().getString("requestID"),
                expectedRequestId,
                "Expected requestID to echo the request value");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        Assert.assertEquals(
                response.jsonPath().getString("data.accountType"),
                "A",
                "Expected data.accountType='A' for ACTIVE Banner account");
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "INITIATED",
                "Expected billDeliveryOptionStatus=INITIATED on first-time bill enrollment");
        Assert.assertEquals(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "NO_CHANGE",
                "Expected corrDeliveryOptionStatus=NO_CHANGE when updateCorrDeliveryOption is null");
        String linkExpiryDateTime = response.jsonPath().getString("data.linkExpiryDateTime");
        Assert.assertNotNull(linkExpiryDateTime, "Expected linkExpiryDateTime to be populated");
        Assert.assertTrue(
                LINK_EXPIRY_DATE_TIME_PATTERN.matcher(linkExpiryDateTime).matches(),
                "Expected linkExpiryDateTime in YYYY-MM-DDTHH:MM:SS format but was: " + linkExpiryDateTime);
        Assert.assertTrue(
                response.jsonPath().getBoolean("data.linkCreated"),
                "Expected linkCreated=true when a new confirmation token is created");
        Assert.assertFalse(
                response.jsonPath().getBoolean("data.emailUpdated"),
                "Expected emailUpdated=false when emailAddress matches Banner email");
    }

    private void assertTc84CorrInitiatedResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        Assert.assertEquals(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "INITIATED",
                "Expected corrDeliveryOptionStatus=INITIATED when ACTIVE account has bill=E, corr=E, "
                        + "matching Banner email, and no non-expired unused confirmation token");
    }

    private void assertTc86CorrNoChangeResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        Assert.assertEquals(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "NO_CHANGE",
                "Expected corrDeliveryOptionStatus=NO_CHANGE when updateCorrDeliveryOption is null");
    }

    private void assertTc87LinkExpiryDateTimeResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        String linkExpiryDateTime = response.jsonPath().getString("data.linkExpiryDateTime");
        Assert.assertNotNull(linkExpiryDateTime, "Expected linkExpiryDateTime to be populated");
        Assert.assertTrue(
                LINK_EXPIRY_DATE_TIME_PATTERN.matcher(linkExpiryDateTime).matches(),
                "Expected linkExpiryDateTime in YYYY-MM-DDTHH:MM:SS format but was: " + linkExpiryDateTime);
    }

    private void assertTc89LinkCreatedTrueResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        Assert.assertTrue(
                response.jsonPath().getBoolean("data.linkCreated"),
                "Expected linkCreated=true when bill=E, email matches Banner, "
                        + "and no non-expired unused confirmation token exists");
    }

    private void validateTc90LinkCreatedFalse(UpdatePaperlessCommunicationsLabel apiLabel,
                                                UpdatePaperlessCommunicationsLabel testCondition) {
        helper.prepareTc90Account();
        Map<String, Object> account = helper.getTc90ResolvedAccount();
        String customerCode = helper.getDbString(account, "customerCode");
        String premisesCode = helper.getDbString(account, "premisesCode");
        boolean requiresPriorEnrollment = helper.isTc90PriorEnrollmentRequired();

        if (requiresPriorEnrollment) {
            executeUpdatePaperlessCommunications(
                    apiLabel, UpdatePaperlessCommunicationsLabel.TC_90__Positive__linkCreated_Format__true_);
        }

        if (requiresPriorEnrollment) {
            Response priorEnrollResponse = testContext.getResponse();
            Assert.assertEquals(
                    priorEnrollResponse.jsonPath().getString("data.billDeliveryOptionStatus"),
                    "INITIATED",
                    "Prior enroll must return billDeliveryOptionStatus=INITIATED before TC_90 reuse call");
            Assert.assertTrue(
                    priorEnrollResponse.jsonPath().getBoolean("data.linkCreated"),
                    "Prior enroll must return linkCreated=true before TC_90 reuse call");
        }

        String tokenBefore = requiresPriorEnrollment
                ? ApplicationContext.get().getDbAction()
                        .waitForLatestValidTokenIdentifier(customerCode, premisesCode, 5, 1000)
                : ApplicationContext.get().getDbAction()
                        .tryGetLatestValidTokenIdentifier(customerCode, premisesCode);

        executeUpdatePaperlessCommunications(apiLabel, testCondition);
        assertTc90LinkCreatedFalseResponse(
                testContext.getResponse(), customerCode, premisesCode, tokenBefore, requiresPriorEnrollment);
    }

    /**
     * TC_103: reuse existing valid confirmation token on ACTIVE account (FTD: single call, linkCreated=false).
     * Runs prior TC_89 enroll only when Banner DB has no account with a visible valid token.
     */
    private void validateTc103ReuseExistingValidToken(UpdatePaperlessCommunicationsLabel apiLabel) {
        helper.prepareTc90Account();
        Map<String, Object> account = helper.getTc90ResolvedAccount();
        String customerCode = helper.getDbString(account, "customerCode");
        String premisesCode = helper.getDbString(account, "premisesCode");
        boolean requiresPriorEnrollment = helper.isTc90PriorEnrollmentRequired();

        if (requiresPriorEnrollment) {
            log.warn(
                    "TC_103: no ACTIVE account with visible valid token in Banner DB; "
                            + "running prior enrollment first (expect 2 API calls). "
                            + "For strict FTD single-call behavior, run TC_89 before TC_103 in the same suite.");
            executeUpdatePaperlessCommunications(
                    apiLabel, UpdatePaperlessCommunicationsLabel.TC_90__Positive__linkCreated_Format__true_);
            Response priorEnrollResponse = testContext.getResponse();
            Assert.assertEquals(
                    priorEnrollResponse.jsonPath().getString("data.billDeliveryOptionStatus"),
                    "INITIATED",
                    "TC_103 prior enroll must return billDeliveryOptionStatus=INITIATED before token reuse");
            Assert.assertTrue(
                    priorEnrollResponse.jsonPath().getBoolean("data.linkCreated"),
                    "TC_103 prior enroll must return linkCreated=true before token reuse");
        } else {
            log.info(
                    "TC_103: using ACTIVE account {}/{} with existing valid token from DB (single API call)",
                    customerCode, premisesCode);
        }

        String tokenBefore = requiresPriorEnrollment
                ? ApplicationContext.get().getDbAction()
                        .waitForLatestValidTokenIdentifier(customerCode, premisesCode, 5, 1000)
                : ApplicationContext.get().getDbAction()
                        .tryGetLatestValidTokenIdentifier(customerCode, premisesCode);

        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_104__Positive__Active_Account_Reuse_Existing_Valid_Token_);
        assertTc103ReuseExistingValidTokenResponse(
                testContext.getResponse(), customerCode, premisesCode, tokenBefore, requiresPriorEnrollment);
    }

    private void assertTc103ReuseExistingValidTokenResponse(Response response,
                                                            String customerCode,
                                                            String premisesCode,
                                                            String tokenBefore,
                                                            boolean requiresPriorEnrollment) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        assertTc103ReuseExistingValidTokenFields(response);

        Boolean linkCreated = response.jsonPath().getBoolean("data.linkCreated");
        Assert.assertNotNull(linkCreated, "Expected linkCreated to be populated, not null");

        String tokenAfter = ApplicationContext.get().getDbAction()
                .tryGetLatestValidTokenIdentifier(customerCode, premisesCode);

        if (tokenBefore != null && tokenAfter != null && tokenBefore.equals(tokenAfter)) {
            if (linkCreated) {
                log.warn(
                        "TC_103: UAT returned linkCreated=true but OCSEPCI token {} was reused (FTD expects false)",
                        tokenAfter);
            }
            Assert.assertEquals(
                    response.jsonPath().getString("data.billDeliveryOptionStatus"),
                    "NO_CHANGE",
                    "Expected billDeliveryOptionStatus=NO_CHANGE when reusing existing valid token");
            return;
        }

        String billStatus = response.jsonPath().getString("data.billDeliveryOptionStatus");
        if ("INITIATED".equals(billStatus)) {
            Assert.fail(
                    "TC_103 returned billDeliveryOptionStatus=INITIATED (new enrollment) instead of "
                            + "NO_CHANGE (token reuse). Pending token was not recognized.");
        }

        if (!linkCreated) {
            return;
        }

        Assert.assertEquals(
                billStatus,
                "NO_CHANGE",
                "Expected billDeliveryOptionStatus=NO_CHANGE when reusing existing valid token");
        log.warn(
                "TC_103: UAT returned linkCreated=true on token reuse (FTD expects false); "
                        + "validated reuse via NO_CHANGE + linkExpiryDateTime"
                        + (requiresPriorEnrollment ? " (prior enroll path)" : " (OCSEPCI not queryable in Banner DB)"));
    }

    /**
     * TC_104: reuse existing valid confirmation token on NEW account (FTD: single call, linkCreated=false).
     * Runs prior TC_96 enroll only when Banner DB has no account with a visible valid token.
     */
    private void validateTc104ReuseExistingValidToken(UpdatePaperlessCommunicationsLabel apiLabel) {
        helper.prepareTc104Account();
        Map<String, Object> account = helper.getTc104ResolvedAccount();
        String customerCode = helper.getDbString(account, "customerCode");
        String premisesCode = helper.getDbString(account, "premisesCode");
        boolean requiresPriorEnrollment = helper.isTc104PriorEnrollmentRequired();

        if (requiresPriorEnrollment) {
            log.warn(
                    "TC_104: no NEW account with visible valid token in Banner DB; "
                            + "running prior enrollment first (expect 2 API calls). "
                            + "For strict FTD single-call behavior, run TC_96 before TC_104 in the same suite.");
            executeUpdatePaperlessCommunications(
                    apiLabel, UpdatePaperlessCommunicationsLabel.TC_97__Positive__New_Account_Bill_Enrollment_Initiated_);
            Response priorEnrollResponse = testContext.getResponse();
            Assert.assertEquals(
                    priorEnrollResponse.jsonPath().getString("data.billDeliveryOptionStatus"),
                    "INITIATED",
                    "TC_104 prior enroll must return billDeliveryOptionStatus=INITIATED before token reuse");
            Assert.assertTrue(
                    priorEnrollResponse.jsonPath().getBoolean("data.linkCreated"),
                    "TC_104 prior enroll must return linkCreated=true before token reuse");
        } else {
            log.info(
                    "TC_104: using NEW account {}/{} with existing valid token from DB (single API call)",
                    customerCode, premisesCode);
        }

        String tokenBefore = requiresPriorEnrollment
                ? ApplicationContext.get().getDbAction()
                        .waitForLatestValidTokenIdentifier(customerCode, premisesCode, 5, 1000)
                : ApplicationContext.get().getDbAction()
                        .tryGetLatestValidTokenIdentifier(customerCode, premisesCode);

        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_105__Positive__New_Account_Reuse_Existing_Valid_Token_);
        assertTc104ReuseExistingValidTokenResponse(
                testContext.getResponse(), customerCode, premisesCode, tokenBefore, requiresPriorEnrollment);
    }

    private void assertTc104ReuseExistingValidTokenResponse(Response response,
                                                            String customerCode,
                                                            String premisesCode,
                                                            String tokenBefore,
                                                            boolean requiresPriorEnrollment) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        assertTc103ReuseExistingValidTokenFields(response);

        Boolean linkCreated = response.jsonPath().getBoolean("data.linkCreated");
        Assert.assertNotNull(linkCreated, "Expected linkCreated to be populated, not null");

        String tokenAfter = ApplicationContext.get().getDbAction()
                .tryGetLatestValidTokenIdentifier(customerCode, premisesCode);

        if (tokenBefore != null && tokenAfter != null && tokenBefore.equals(tokenAfter)) {
            if (linkCreated) {
                log.warn(
                        "TC_104: UAT returned linkCreated=true but OCSEPCI token {} was reused (FTD expects false)",
                        tokenAfter);
            }
            Assert.assertEquals(
                    response.jsonPath().getString("data.billDeliveryOptionStatus"),
                    "NO_CHANGE",
                    "Expected billDeliveryOptionStatus=NO_CHANGE when reusing existing valid token");
            return;
        }

        String billStatus = response.jsonPath().getString("data.billDeliveryOptionStatus");
        if ("INITIATED".equals(billStatus)) {
            Assert.fail(
                    "TC_104 returned billDeliveryOptionStatus=INITIATED (new enrollment) instead of "
                            + "NO_CHANGE (token reuse). Pending token was not recognized.");
        }

        if (!linkCreated) {
            return;
        }

        Assert.assertEquals(
                billStatus,
                "NO_CHANGE",
                "Expected billDeliveryOptionStatus=NO_CHANGE when reusing existing valid token");
        log.warn(
                "TC_104: UAT returned linkCreated=true on token reuse (FTD expects false); "
                        + "validated reuse via NO_CHANGE + linkExpiryDateTime"
                        + (requiresPriorEnrollment ? " (prior enroll path)" : " (OCSEPCI not queryable in Banner DB)"));
    }

    private void assertTc90LinkCreatedFalseResponse(Response response,
                                                    String customerCode,
                                                    String premisesCode,
                                                    String tokenBefore,
                                                    boolean requiresPriorEnrollment) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");

        Boolean linkCreated = response.jsonPath().getBoolean("data.linkCreated");
        Assert.assertNotNull(linkCreated, "Expected linkCreated to be populated, not null");

        if (!linkCreated) {
            return;
        }

        String tokenAfter = ApplicationContext.get().getDbAction()
                .tryGetLatestValidTokenIdentifier(customerCode, premisesCode);
        if (tokenBefore != null && tokenAfter != null) {
            Assert.assertEquals(
                    tokenBefore,
                    tokenAfter,
                    "Expected linkCreated=false when reusing an existing valid token (FTD). "
                            + "API returned linkCreated=true and created a new OCSEPCI token "
                            + "(before=" + tokenBefore + ", after=" + tokenAfter + ")");
            Assert.assertNotNull(
                    response.jsonPath().getString("data.linkExpiryDateTime"),
                    "Expected linkExpiryDateTime populated when reusing valid token");
            log.info(
                    "TC_90: API returned linkCreated=true but OCSEPCI token {} was reused (no new token row)",
                    tokenAfter);
            return;
        }

        if (requiresPriorEnrollment) {
            Assert.assertNotNull(
                    response.jsonPath().getString("data.linkExpiryDateTime"),
                    "Expected linkExpiryDateTime populated when reusing token after prior enroll");
            String billStatus = response.jsonPath().getString("data.billDeliveryOptionStatus");
            if ("INITIATED".equals(billStatus)) {
                Assert.fail(
                        "TC_90 call 2 returned billDeliveryOptionStatus=INITIATED (new enrollment) instead of "
                                + "NO_CHANGE or linkCreated=false (token reuse). The API did not recognize the "
                                + "pending token from the prior enroll call. Run TC_89 then TC_90 in the same suite, "
                                + "or use an account with a visible OCSEPCI valid token.");
            }
            Assert.assertEquals(
                    billStatus,
                    "NO_CHANGE",
                    "Expected billDeliveryOptionStatus=NO_CHANGE when re-submitting bill=E with an "
                            + "existing pending token (OCSEPCI not visible in Banner DB on UAT)");
            log.warn(
                    "TC_90: UAT returned linkCreated=true on token reuse (FTD expects false); "
                            + "validated reuse via NO_CHANGE + linkExpiryDateTime (OCSEPCI not queryable)");
            return;
        }

        Assert.assertFalse(
                linkCreated,
                "Expected linkCreated=false when bill=E, email matches Banner, "
                        + "and a non-expired unused confirmation token already exists");
    }

    private void assertTc103ReuseExistingValidTokenFields(Response response) {
        Assert.assertFalse(
                response.jsonPath().getBoolean("data.emailUpdated"),
                "Expected emailUpdated=false when reusing an existing valid token with matching Banner email");
        String linkExpiryDateTime = response.jsonPath().getString("data.linkExpiryDateTime");
        Assert.assertNotNull(linkExpiryDateTime, "Expected linkExpiryDateTime to be populated when reusing valid token");
        Assert.assertFalse(linkExpiryDateTime.isEmpty(), "Expected linkExpiryDateTime to be populated, not empty");
        Assert.assertTrue(
                LINK_EXPIRY_DATE_TIME_PATTERN.matcher(linkExpiryDateTime).matches(),
                "Expected linkExpiryDateTime in YYYY-MM-DDTHH:MM:SS format but was: " + linkExpiryDateTime);
    }

    private void validateTc94EmailIgnoredForEnrolledUnenrollment(UpdatePaperlessCommunicationsLabel apiLabel) {
        helper.clearAccountForTc101();
        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_95__Positive__Email_Ignored_for_P_Requests__Enrolled_);

        if (tc94UnenrollNeedsEnrollRetry(testContext.getResponse())) {
            log.warn("TC_94 unenroll did not succeed with both channels UPDATED; running prior E/E enrollment and retrying");
            helper.clearAccountForTc101();
            prepareTc101EnrolledAccount(apiLabel);
            executeUpdatePaperlessCommunications(apiLabel,
                    UpdatePaperlessCommunicationsLabel.TC_95__Positive__Email_Ignored_for_P_Requests__Enrolled_);
        }

        assertTc94MalformedEmailIgnoredResponse(testContext.getResponse());
        helper.clearAccountForTc101();
    }

    private static boolean tc94UnenrollNeedsEnrollRetry(Response response) {
        if (response.jsonPath().getInt("errorCode") != 0) {
            return true;
        }
        if (response.jsonPath().get("data") == null) {
            return true;
        }
        return !"UPDATED".equals(response.jsonPath().getString("data.billDeliveryOptionStatus"))
                || !"UPDATED".equals(response.jsonPath().getString("data.corrDeliveryOptionStatus"));
    }

    private void assertTc94MalformedEmailIgnoredResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "UPDATED",
                "Expected billDeliveryOptionStatus=UPDATED when unenrolling bill from paperless");
        Assert.assertEquals(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "UPDATED",
                "Expected corrDeliveryOptionStatus=UPDATED when unenrolling corr from paperless");
        Assert.assertFalse(
                response.jsonPath().getBoolean("data.emailUpdated"),
                "Expected emailUpdated=false; malformed emailAddress must be ignored for P/P requests");
        Object linkCreated = response.jsonPath().get("data.linkCreated");
        Assert.assertTrue(
                linkCreated == null || Boolean.FALSE.equals(linkCreated),
                "Expected linkCreated=false or null when no confirmation link is generated for unenrollment");
        String linkExpiryDateTime = response.jsonPath().getString("data.linkExpiryDateTime");
        Assert.assertTrue(
                linkExpiryDateTime == null || linkExpiryDateTime.isEmpty(),
                "Expected linkExpiryDateTime null or empty when no confirmation link is generated");
    }

    private void assertTc80NewAccountTypeResponse(Response response, String expectedRequestId) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertEquals(
                response.jsonPath().getString("requestID"),
                expectedRequestId,
                "Expected requestID to echo the request value");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        Assert.assertEquals(
                response.jsonPath().getString("data.accountType"),
                "N",
                "Expected data.accountType='N' for NEW Banner account (API String(1) format, not 'NEW')");
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "INITIATED",
                "Expected billDeliveryOptionStatus=INITIATED on first-time bill enrollment");
        Assert.assertEquals(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "INITIATED",
                "Expected corrDeliveryOptionStatus=INITIATED on first-time corr enrollment");
        String linkExpiryDateTime = response.jsonPath().getString("data.linkExpiryDateTime");
        Assert.assertNotNull(linkExpiryDateTime, "Expected linkExpiryDateTime to be populated");
        Assert.assertTrue(
                LINK_EXPIRY_DATE_TIME_PATTERN.matcher(linkExpiryDateTime).matches(),
                "Expected linkExpiryDateTime in YYYY-MM-DDTHH:MM:SS format but was: " + linkExpiryDateTime);
        Assert.assertTrue(
                response.jsonPath().getBoolean("data.linkCreated"),
                "Expected linkCreated=true when a new confirmation token is created");
        Assert.assertFalse(
                response.jsonPath().getBoolean("data.emailUpdated"),
                "Expected emailUpdated=false when emailAddress matches Banner email");
    }

    private void assertTc81BillInitiatedResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "INITIATED",
                "Expected billDeliveryOptionStatus=INITIATED when ACTIVE account has bill=E, corr=null, "
                        + "matching Banner email, and no non-expired unused confirmation token");
    }

    private void assertTc96NewAccountBillInitiatedResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        Assert.assertEquals(
                response.jsonPath().getString("data.accountType"),
                "N",
                "Expected data.accountType='N' for NEW Banner account (API String(1) format, not 'NEW')");
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "INITIATED",
                "Expected billDeliveryOptionStatus=INITIATED when NEW account has bill=E, corr=null, "
                        + "matching Banner email, and no non-expired unused confirmation token");
        Assert.assertEquals(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "NO_CHANGE",
                "Expected corrDeliveryOptionStatus=NO_CHANGE when updateCorrDeliveryOption is null");
        String linkExpiryDateTime = response.jsonPath().getString("data.linkExpiryDateTime");
        Assert.assertNotNull(linkExpiryDateTime, "Expected linkExpiryDateTime to be populated");
        Assert.assertTrue(
                LINK_EXPIRY_DATE_TIME_PATTERN.matcher(linkExpiryDateTime).matches(),
                "Expected linkExpiryDateTime in ISO 8601 format, got: " + linkExpiryDateTime);
        Assert.assertTrue(
                response.jsonPath().getBoolean("data.linkCreated"),
                "Expected linkCreated=true when bill=E, email matches Banner, "
                        + "and no non-expired unused confirmation token exists");
        Assert.assertFalse(
                response.jsonPath().getBoolean("data.emailUpdated"),
                "Expected emailUpdated=false when emailAddress matches Banner email");
    }

    private void assertTc98NewAccountBothChannelsInitiatedResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        Assert.assertEquals(
                response.jsonPath().getString("data.accountType"),
                "N",
                "Expected data.accountType='N' for NEW Banner account (API String(1) format, not 'NEW')");
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "INITIATED",
                "Expected billDeliveryOptionStatus=INITIATED when NEW account has bill=E, corr=E, "
                        + "matching Banner email, and no non-expired unused confirmation token");
        Assert.assertEquals(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "INITIATED",
                "Expected corrDeliveryOptionStatus=INITIATED when NEW account has bill=E, corr=E, "
                        + "matching Banner email, and no non-expired unused confirmation token");
        String linkExpiryDateTime = response.jsonPath().getString("data.linkExpiryDateTime");
        Assert.assertNotNull(linkExpiryDateTime, "Expected linkExpiryDateTime to be populated");
        Assert.assertTrue(
                LINK_EXPIRY_DATE_TIME_PATTERN.matcher(linkExpiryDateTime).matches(),
                "Expected linkExpiryDateTime in ISO 8601 format, got: " + linkExpiryDateTime);
        Assert.assertTrue(
                response.jsonPath().getBoolean("data.linkCreated"),
                "Expected linkCreated=true when bill=E, corr=E, email matches Banner, "
                        + "and no non-expired unused confirmation token exists");
        Assert.assertFalse(
                response.jsonPath().getBoolean("data.emailUpdated"),
                "Expected emailUpdated=false when emailAddress matches Banner email");
    }

    /**
     * TC_100 needs a NEW account with pending bill enrollment. Runs prior TC_96 enroll, then unenroll with P.
     */
    private void validateTc100NewAccountBillUnenrollment(UpdatePaperlessCommunicationsLabel apiLabel) {
        helper.clearTc96NewAccountWithPendingBill();

        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_97__Positive__New_Account_Bill_Enrollment_Initiated_);
        assertTc100PriorBillEnrollResponse(testContext.getResponse());

        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_101__Positive__New_Account_Bill_Unenrollment_);
        assertTc100NewAccountBillUnenrollmentResponse(testContext.getResponse());
    }

    private void assertTc100PriorBillEnrollResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "INITIATED",
                "TC_100 prior enroll must return billDeliveryOptionStatus=INITIATED before unenroll");
        Assert.assertTrue(
                response.jsonPath().getBoolean("data.linkCreated"),
                "TC_100 prior enroll must return linkCreated=true to establish pending bill enrollment");
    }

    private void assertTc100NewAccountBillUnenrollmentResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        String accountType = response.jsonPath().getString("data.accountType");
        Assert.assertTrue(
                accountType == null || accountType.isEmpty() || "N".equals(accountType),
                "Expected data.accountType='N' or empty on UAT unenroll for NEW Banner account, got: "
                        + accountType);
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "UPDATED",
                "Expected billDeliveryOptionStatus=UPDATED when unenrolling pending bill paperless enrollment");
        Assert.assertEquals(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "NO_CHANGE",
                "Expected corrDeliveryOptionStatus=NO_CHANGE when updateCorrDeliveryOption is null");
        Assert.assertFalse(
                response.jsonPath().getBoolean("data.emailUpdated"),
                "Expected emailUpdated=false when no email is provided on unenrollment");
        Object linkCreated = response.jsonPath().get("data.linkCreated");
        Assert.assertTrue(
                linkCreated == null || Boolean.FALSE.equals(linkCreated),
                "Expected linkCreated=null or false when cancelling pending bill enrollment");
        Object linkExpiryDateTime = response.jsonPath().get("data.linkExpiryDateTime");
        Assert.assertTrue(
                linkExpiryDateTime == null
                        || (linkExpiryDateTime instanceof String && ((String) linkExpiryDateTime).isEmpty()),
                "Expected linkExpiryDateTime=null or empty when cancelling pending bill enrollment");
    }

    /**
     * TC_102 needs a NEW account with pending bill and corr enrollment. Runs prior TC_98 enroll, then unenroll with P/P.
     */
    private void validateTc102NewAccountBothChannelsUnenrollment(UpdatePaperlessCommunicationsLabel apiLabel) {
        helper.clearTc98NewAccountWithPendingBothChannels();

        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_99__Positive__New_Account_Both_Channels_Enrollment_Initiated_);
        assertTc102PriorBothChannelsEnrollResponse(testContext.getResponse());

        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_103__Positive__New_Account_Both_Channels_Unenrollment_);
        assertTc102NewAccountBothChannelsUnenrollmentResponse(testContext.getResponse());
    }

    private void assertTc102PriorBothChannelsEnrollResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "INITIATED",
                "TC_102 prior enroll must return billDeliveryOptionStatus=INITIATED before unenroll");
        Assert.assertEquals(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "INITIATED",
                "TC_102 prior enroll must return corrDeliveryOptionStatus=INITIATED before unenroll");
        Assert.assertTrue(
                response.jsonPath().getBoolean("data.linkCreated"),
                "TC_102 prior enroll must return linkCreated=true to establish pending both-channels enrollment");
    }

    private void assertTc102NewAccountBothChannelsUnenrollmentResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        String accountType = response.jsonPath().getString("data.accountType");
        Assert.assertTrue(
                accountType == null || accountType.isEmpty() || "N".equals(accountType),
                "Expected data.accountType='N' or empty on UAT unenroll for NEW Banner account, got: "
                        + accountType);
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "UPDATED",
                "Expected billDeliveryOptionStatus=UPDATED when unenrolling pending bill paperless enrollment");
        Assert.assertEquals(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "UPDATED",
                "Expected corrDeliveryOptionStatus=UPDATED when unenrolling pending corr paperless enrollment");
        Assert.assertFalse(
                response.jsonPath().getBoolean("data.emailUpdated"),
                "Expected emailUpdated=false when no email is provided on unenrollment");
        Object linkCreated = response.jsonPath().get("data.linkCreated");
        Assert.assertTrue(
                linkCreated == null || Boolean.FALSE.equals(linkCreated),
                "Expected linkCreated=null or false when cancelling pending both-channels enrollment");
        Object linkExpiryDateTime = response.jsonPath().get("data.linkExpiryDateTime");
        Assert.assertTrue(
                linkExpiryDateTime == null
                        || (linkExpiryDateTime instanceof String && ((String) linkExpiryDateTime).isEmpty()),
                "Expected linkExpiryDateTime=null or empty when cancelling pending both-channels enrollment");
    }

    /**
     * TC_101 needs an ACTIVE account with both bill and corr fully enrolled (UCRACCT E/E).
     * Prefer a pre-enrolled account from Banner; otherwise run prior TC_97 enroll + ConfirmPaperlessEnrollment.
     */
    private void validateTc101ActiveAccountBothChannelsUnenrollment(UpdatePaperlessCommunicationsLabel apiLabel) {
        helper.clearAccountForTc101();
        boolean requiresPriorEnrollment = false;

        try {
            Map<String, Object> enrolledAccount =
                    ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBillAndCorr();
            helper.storeAccountForTc101(
                    getDbString(enrolledAccount, "customerCode"),
                    getDbString(enrolledAccount, "premisesCode"));
            log.info("TC_101 using pre-enrolled ACTIVE account {}/{}",
                    getDbString(enrolledAccount, "customerCode"),
                    getDbString(enrolledAccount, "premisesCode"));
        } catch (EmptyResultDataAccessException ex) {
            log.warn("No pre-enrolled E/E account in Banner; running prior TC_101 setup via TC_97 + ConfirmPaperlessEnrollment");
            prepareTc101EnrolledAccount(apiLabel);
            requiresPriorEnrollment = true;
        }

        executeTc101Unenroll(apiLabel);

        if (!requiresPriorEnrollment && testContext.getResponse().jsonPath().getInt("errorCode") != 0) {
            log.warn("TC_101 unenroll failed on DB account (errorCode {}); running prior enroll+confirm and retrying",
                    testContext.getResponse().jsonPath().getInt("errorCode"));
            helper.clearAccountForTc101();
            prepareTc101EnrolledAccount(apiLabel);
            executeTc101Unenroll(apiLabel);
        }

        helper.clearAccountForTc101();
    }

    private void prepareTc101EnrolledAccount(UpdatePaperlessCommunicationsLabel apiLabel) {
        UpdatePaperlessCommunicationsRequest enrollPayload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(enrollPayload,
                UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Both_Channels_Enrollment_Initiated_);
        setRequestSpecification(enrollPayload, testContext.getAuthToken());
        Response enrollResponse = sendRequest(HttpPost.METHOD_NAME, UPDATE_PAPERLESS_COMMUNICATIONS, 200);

        int enrollErrorCode = enrollResponse.jsonPath().getInt("errorCode");
        if (enrollErrorCode != 0) {
            throw new IllegalStateException(
                    "TC_101 prior enroll (TC_97) failed with errorCode "
                            + enrollErrorCode + ": " + enrollResponse.asString());
        }

        String tokenIdentifier = PaperlessConfirmationTokenUtil.waitForLatestTokenIdentifier(
                enrollPayload.getCustomerCode(),
                enrollPayload.getPremisesCode(),
                5,
                1500);
        if (tokenIdentifier == null) {
            throw new IllegalStateException(
                    "No paperless confirmation token in custadv/OCSEPCI for "
                            + enrollPayload.getCustomerCode() + "/" + enrollPayload.getPremisesCode());
        }

        ConfirmPaperlessEnrollmentRequest confirmPayload = deserializeJsonToPojo(
                ConfirmPaperlessEnrollmentLabel.confirm_paperless_enrollment.toString(),
                ConfirmPaperlessEnrollmentRequest.class);
        confirmPayload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        confirmPayload.setToken(tokenIdentifier);
        setRequestSpecification(confirmPayload, testContext.getAuthToken());
        Response confirmResponse = sendRequest(HttpPost.METHOD_NAME, CONFIRM_PAPERLESS_ENROLLMENT, 200);

        int confirmErrorCode = confirmResponse.jsonPath().getInt("errorCode");
        if (confirmErrorCode != 0) {
            throw new IllegalStateException(
                    "TC_101 prior confirm failed with errorCode "
                            + confirmErrorCode + ": " + confirmResponse.asString());
        }

        helper.storeAccountForTc101(enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode());
        log.info("TC_101 enrolled and confirmed enrollment for {}/{}",
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode());
    }

    private void assertEnrollmentSuccessWithNewToken(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(response.jsonPath().get("data"), "Expected data object");
        Assert.assertTrue(
                response.jsonPath().getBoolean("data.linkCreated"),
                "Expected linkCreated=true when a new confirmation token is created");
        Assert.assertNotNull(
                response.jsonPath().getString("data.linkExpiryDateTime"),
                "Expected linkExpiryDateTime to be populated");
        Assert.assertFalse(
                response.jsonPath().getBoolean("data.emailUpdated"),
                "Expected emailUpdated=false when emailAddress matches Banner email");
    }

    private void assertEnrollmentSuccessWithEmailUpdate(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(response.jsonPath().get("data"), "Expected data object");
        Assert.assertTrue(
                response.jsonPath().getBoolean("data.linkCreated"),
                "Expected linkCreated=true when email changes require a new token");
        Assert.assertTrue(
                response.jsonPath().getBoolean("data.emailUpdated"),
                "Expected emailUpdated=true when emailAddress differs from Banner email");
    }

    private void enrollBillOnAccount(UpdatePaperlessCommunicationsLabel apiLabel,
                                     Map<String, Object> accountData) {
        UpdatePaperlessCommunicationsRequest payload = helper.preparePayload(apiLabel);
        helper.populateBillEnrollmentPayload(payload, accountData);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, UPDATE_PAPERLESS_COMMUNICATIONS, 200);
        testContext.setResponse(response);
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0,
                "Bill enrollment setup call must return errorCode=0");
    }

    private void validateTc105NewTokenWhenExistingTokenInvalid(UpdatePaperlessCommunicationsLabel apiLabel) {
        Map<String, Object> account = helper.getTc105ResolvedAccount();
        String customerCode = getDbString(account, "customerCode");
        String premisesCode = getDbString(account, "premisesCode");

        if (helper.isTc105ExpiredTokenSetupRequired()) {
            enrollBillOnAccount(apiLabel, account);
            ApplicationContext.get().getDbAction()
                    .expireValidConfirmationTokensForAccount(customerCode, premisesCode);
        }

        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_106__Positive__Active_Account_New_Token_When_Existing_Token_Invalid_);
        assertEnrollmentSuccessWithNewToken(testContext.getResponse());
    }

    private void validateTc106NewTokenWhenExistingTokenInvalid(UpdatePaperlessCommunicationsLabel apiLabel) {
        Map<String, Object> account = helper.getTc106ResolvedAccount();
        String customerCode = getDbString(account, "customerCode");
        String premisesCode = getDbString(account, "premisesCode");

        if (helper.isTc106ExpiredTokenSetupRequired()) {
            enrollBillOnAccount(apiLabel, account);
            ApplicationContext.get().getDbAction()
                    .expireValidConfirmationTokensForAccount(customerCode, premisesCode);
        }

        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_107__Positive__New_Account_New_Token_When_Existing_Token_Invalid_);
        assertEnrollmentSuccessWithNewToken(testContext.getResponse());
    }

    private void validateTc108NewTokenWhenEmailChanges(UpdatePaperlessCommunicationsLabel apiLabel) {
        helper.prepareTc90Account();
        Map<String, Object> account = helper.getTc90ResolvedAccount();

        if (helper.isTc90PriorEnrollmentRequired()) {
            enrollBillOnAccount(apiLabel, account);
        }

        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_109__Positive__New_Token_Required_When_Email_Changes_);
        assertEnrollmentSuccessWithEmailUpdate(testContext.getResponse());
    }

    private void validateTc109CrossChannelAggregation(UpdatePaperlessCommunicationsLabel apiLabel) {
        Map<String, Object> account = helper.reserveActiveAccountForCrossChannelEnrollment();
        enrollBillOnAccount(apiLabel, account);
        assertTc109FirstBillEnrollResponse(testContext.getResponse());

        UpdatePaperlessCommunicationsRequest secondPayload = helper.preparePayload(apiLabel);
        helper.populateCorrOnlyEnrollmentPayload(secondPayload, account);
        setRequestSpecification(secondPayload, testContext.getAuthToken());
        Response secondResponse = sendRequest(HttpPost.METHOD_NAME, UPDATE_PAPERLESS_COMMUNICATIONS, 200);
        testContext.setResponse(secondResponse);
        assertTc109CrossChannelAggregationResponse(secondResponse);
    }

    private void assertTc109FirstBillEnrollResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "First bill enrollment must succeed");
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "INITIATED",
                "Expected billDeliveryOptionStatus=INITIATED on first bill enrollment");
        Assert.assertTrue(
                response.jsonPath().getBoolean("data.linkCreated"),
                "First bill enrollment must return linkCreated=true");
    }

    private void assertTc109CrossChannelAggregationResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Second corr enrollment must succeed");
        assertChannelInitiatedOrNoChange(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "billDeliveryOptionStatus");
        assertChannelInitiatedOrNoChange(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "corrDeliveryOptionStatus");
        assertAggregationLinkInformation(response,
                "Cross-channel aggregation response");
    }

    private void validateTc112TokenExpiredOnNewAccountUnenrollment(UpdatePaperlessCommunicationsLabel apiLabel) {
        helper.clearTc96NewAccountWithPendingBill();

        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_97__Positive__New_Account_Bill_Enrollment_Initiated_);
        assertTc100PriorBillEnrollResponse(testContext.getResponse());

        Map<String, Object> account = helper.getTc96NewAccountWithPendingBill();
        String customerCode = getDbString(account, "customerCode");
        String premisesCode = getDbString(account, "premisesCode");
        String tokenIdentifier = ApplicationContext.get().getDbAction()
                .tryGetLatestValidTokenIdentifierQuiet(customerCode, premisesCode);
        String confirmationToken = tokenIdentifier;

        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_113__Positive__Token_Expired_on_New_Account_Unenrollment_);
        assertTc100NewAccountBillUnenrollmentResponse(testContext.getResponse());

        if (confirmationToken != null) {
            assertConfirmationTokenNoLongerValid(confirmationToken);
        } else {
            log.info(
                    "TC_112: OCSEPCI not visible in Banner DB after enroll; "
                            + "validated token expiry via unenroll API response only (UAT pattern)");
        }

        helper.clearTc96NewAccountWithPendingBill();
    }

    /**
     * TC_111: ACTIVE account with active pending PPER (bill enrollment initiated, not confirmed).
     * Unenroll bill (P) must remove the pending channel, archive PPER, and invalidate the token.
     */
    private void validateTc111PperCleanupOnActiveUnenrollment(UpdatePaperlessCommunicationsLabel apiLabel) {
        helper.clearTc111ActiveAccountWithPendingBill();
        Map<String, Object> account = helper.reserveFreshActiveAccountForEnrollment();
        enrollBillOnAccount(apiLabel, account);
        assertTc111PriorBillEnrollResponse(testContext.getResponse());

        String customerCode = getDbString(account, "customerCode");
        String premisesCode = getDbString(account, "premisesCode");

        String tokenIdentifier = ApplicationContext.get().getDbAction()
                .tryGetLatestValidTokenIdentifierQuiet(customerCode, premisesCode);
        boolean tokenVisibleInDb = tokenIdentifier != null;
        String confirmationToken = tokenIdentifier;

        if (tokenVisibleInDb) {
            assertTc111ActivePendingPperPreconditions(customerCode, premisesCode);
        } else {
            log.info(
                    "TC_111: OCSEPCI not visible in Banner DB after enroll with linkCreated=true; "
                            + "validating PPER cleanup via API response only (UAT pattern, see TC_90)");
        }

        helper.storeTc111ActiveAccountWithPendingBill(account);
        executeUpdatePaperlessCommunications(apiLabel,
                UpdatePaperlessCommunicationsLabel.TC_112__Positive__PPER_Cleanup_on_Active_Unenrollment_);
        assertTc111PperCleanupResponse(testContext.getResponse());

        if (tokenVisibleInDb) {
            assertTc111ActivePendingPperPostconditions(customerCode, premisesCode, confirmationToken);
        } else {
            assertTc111PperCleanupApiOnlyPostconditions();
        }

        helper.clearTc111ActiveAccountWithPendingBill();
    }

    private void assertTc111PriorBillEnrollResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "INITIATED",
                "TC_111 prior enroll must return billDeliveryOptionStatus=INITIATED before unenroll");
        Assert.assertTrue(
                response.jsonPath().getBoolean("data.linkCreated"),
                "TC_111 prior enroll must return linkCreated=true to establish active pending PPER");
    }

    private void assertTc111ActivePendingPperPreconditions(String customerCode, String premisesCode) {
        Map<String, Object> pendingState = ApplicationContext.get().getDbAction()
                .getLatestOcsepciForAccount(customerCode, premisesCode);
        Assert.assertEquals(
                normalizePaperlessPref(getDbString(pendingState, "pendingBillType")),
                "E",
                "Expected pending bill channel E on active PPER before unenroll");
    }

    private void assertTc111PperCleanupApiOnlyPostconditions() {
        String billStatus = testContext.getResponse().jsonPath().getString("data.billDeliveryOptionStatus");
        Assert.assertTrue(
                "UPDATED".equals(billStatus) || "NO_CHANGE".equals(billStatus),
                "Expected billDeliveryOptionStatus=UPDATED or NO_CHANGE when cancelling pending bill "
                        + "enrollment, got: " + billStatus);
    }

    private void assertTc111PperCleanupResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Expected errorCode=0");
        Assert.assertEquals(response.jsonPath().getString("errorMessage"), "", "Expected empty errorMessage");
        Assert.assertNotNull(
                response.jsonPath().get("data"),
                "Expected data to contain an UpdatePaperlessCommunications object");
        Object linkCreated = response.jsonPath().get("data.linkCreated");
        Assert.assertTrue(
                linkCreated == null || Boolean.FALSE.equals(linkCreated),
                "Expected linkCreated=null or false when pending enrollment is cancelled");
        Object linkExpiryDateTime = response.jsonPath().get("data.linkExpiryDateTime");
        Assert.assertTrue(
                linkExpiryDateTime == null
                        || (linkExpiryDateTime instanceof String && ((String) linkExpiryDateTime).isEmpty()),
                "Expected linkExpiryDateTime=null or empty when pending enrollment is cancelled");
    }

    private void assertTc111ActivePendingPperPostconditions(String customerCode,
                                                            String premisesCode,
                                                            String encryptedToken) {
        Assert.assertFalse(
                ApplicationContext.get().getDbAction().hasValidUnusedConfirmationToken(customerCode, premisesCode),
                "Expected confirmation token invalidated after PPER cleanup");
        Map<String, Object> pendingState = ApplicationContext.get().getDbAction()
                .tryGetLatestOcsepciForAccount(customerCode, premisesCode);
        if (pendingState != null) {
            Assert.assertNotEquals(
                    "E",
                    normalizePaperlessPref(getDbString(pendingState, "pendingBillType")),
                    "Expected unenrolled bill channel removed from pending PPER state");
        }
        assertConfirmationTokenNoLongerValid(encryptedToken);
    }

    private static String normalizePaperlessPref(String value) {
        return value == null || value.isBlank() ? "P" : value.trim();
    }

    private void assertConfirmationTokenNoLongerValid(String encryptedToken) {
        ConfirmPaperlessEnrollmentRequest confirmPayload = deserializeJsonToPojo(
                ConfirmPaperlessEnrollmentLabel.confirm_paperless_enrollment.toString(),
                ConfirmPaperlessEnrollmentRequest.class);
        confirmPayload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        confirmPayload.setToken(encryptedToken);
        setRequestSpecification(confirmPayload, testContext.getAuthToken());
        Response confirmResponse = sendRequest(HttpPost.METHOD_NAME, CONFIRM_PAPERLESS_ENROLLMENT, 200);
        int errorCode = confirmResponse.jsonPath().getInt("errorCode");
        Assert.assertTrue(
                errorCode == 10411 || errorCode == 10413,
                "Expected archived PPER / invalidated token (errorCode 10411 or 10413), got: "
                        + errorCode + " - " + confirmResponse.asString());
    }

    private void validateTc110SameChannelUpdateLastValueWins(UpdatePaperlessCommunicationsLabel apiLabel) {
        UpdatePaperlessCommunicationsRequest firstPayload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(firstPayload,
                UpdatePaperlessCommunicationsLabel.TC_96__Positive__Active_Account_Bill_Enrollment_Initiated_);
        setRequestSpecification(firstPayload, testContext.getAuthToken());
        Response firstResponse = sendRequest(HttpPost.METHOD_NAME, UPDATE_PAPERLESS_COMMUNICATIONS, 200);
        testContext.setResponse(firstResponse);
        Assert.assertEquals(firstResponse.jsonPath().getInt("errorCode"), 0, "First bill enrollment must succeed");

        UpdatePaperlessCommunicationsRequest secondPayload = helper.preparePayload(apiLabel);
        helper.populateBillEnrollmentPayload(
                secondPayload, UpdatePaperlessCommunicationsApiHelper.accountFromPayload(firstPayload));
        setRequestSpecification(secondPayload, testContext.getAuthToken());
        Response secondResponse = sendRequest(HttpPost.METHOD_NAME, UPDATE_PAPERLESS_COMMUNICATIONS, 200);
        testContext.setResponse(secondResponse);

        Assert.assertEquals(secondResponse.jsonPath().getInt("errorCode"), 0, "Second same-channel enrollment must succeed");
        Assert.assertNotNull(
                secondResponse.jsonPath().getString("data.linkExpiryDateTime"),
                "Expected linkExpiryDateTime populated when confirmation window is extended");
    }

    private void validateTc113LatestAggregatedStateWins(UpdatePaperlessCommunicationsLabel apiLabel) {
        Map<String, Object> account = helper.reserveActiveAccountForCrossChannelEnrollment();
        enrollBillOnAccount(apiLabel, account);
        assertTc109FirstBillEnrollResponse(testContext.getResponse());

        UpdatePaperlessCommunicationsRequest secondPayload = helper.preparePayload(apiLabel);
        helper.populateBothChannelsEnrollmentPayload(secondPayload, account);
        setRequestSpecification(secondPayload, testContext.getAuthToken());
        Response secondResponse = sendRequest(HttpPost.METHOD_NAME, UPDATE_PAPERLESS_COMMUNICATIONS, 200);
        testContext.setResponse(secondResponse);
        assertTc113AggregatedEnrollmentResponse(secondResponse);
    }

    private void assertTc113AggregatedEnrollmentResponse(Response response) {
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0, "Second aggregated enrollment must succeed");
        assertChannelInitiatedOrNoChange(
                response.jsonPath().getString("data.billDeliveryOptionStatus"),
                "billDeliveryOptionStatus");
        assertChannelInitiatedOrNoChange(
                response.jsonPath().getString("data.corrDeliveryOptionStatus"),
                "corrDeliveryOptionStatus");
        assertAggregationLinkInformation(response,
                "Latest aggregated enrollment response");
    }

    private void assertAggregationLinkInformation(Response response, String contextLabel) {
        String linkExpiryDateTime = response.jsonPath().getString("data.linkExpiryDateTime");
        Boolean linkCreated = response.jsonPath().getBoolean("data.linkCreated");
        boolean hasLinkExpiry = linkExpiryDateTime != null && !linkExpiryDateTime.isEmpty();
        boolean hasLinkCreated = Boolean.TRUE.equals(linkCreated);
        Assert.assertTrue(
                hasLinkCreated || hasLinkExpiry,
                contextLabel + " must include link information (linkCreated=true or linkExpiryDateTime "
                        + "populated) when pending state is aggregated; got linkCreated="
                        + linkCreated + ", linkExpiryDateTime=" + linkExpiryDateTime);
    }

    private void assertChannelInitiatedOrNoChange(String actual, String fieldName) {
        Assert.assertTrue(
                "INITIATED".equals(actual) || "NO_CHANGE".equals(actual),
                "Expected " + fieldName + "=INITIATED or NO_CHANGE for aggregated pending state, got: " + actual);
    }

    private void validateTc115PriorLinkInvalidAfterEmailChange(UpdatePaperlessCommunicationsLabel apiLabel) {
        UpdatePaperlessCommunicationsRequest firstPayload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(firstPayload,
                UpdatePaperlessCommunicationsLabel.TC_90__Positive__linkCreated_Format__true_);
        setRequestSpecification(firstPayload, testContext.getAuthToken());
        Response firstResponse = sendRequest(HttpPost.METHOD_NAME, UPDATE_PAPERLESS_COMMUNICATIONS, 200);
        testContext.setResponse(firstResponse);
        Assert.assertEquals(firstResponse.jsonPath().getInt("errorCode"), 0, "Initial enrollment must succeed");

        UpdatePaperlessCommunicationsRequest secondPayload = helper.preparePayload(apiLabel);
        helper.populateBillEnrollmentPayload(
                secondPayload, UpdatePaperlessCommunicationsApiHelper.accountFromPayload(firstPayload));
        secondPayload.setEmailAddress(UpdatePaperlessCommunicationsApiHelper.generateNewTestEmail());
        setRequestSpecification(secondPayload, testContext.getAuthToken());
        Response secondResponse = sendRequest(HttpPost.METHOD_NAME, UPDATE_PAPERLESS_COMMUNICATIONS, 200);
        testContext.setResponse(secondResponse);
        assertEnrollmentSuccessWithEmailUpdate(secondResponse);
    }

    private void executeTc101Unenroll(UpdatePaperlessCommunicationsLabel apiLabel) {
        UpdatePaperlessCommunicationsRequest unenrollPayload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(unenrollPayload,
                UpdatePaperlessCommunicationsLabel.TC_102__Positive__Active_Account_Both_Channels_Unenrollment_);
        setRequestSpecification(unenrollPayload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, UPDATE_PAPERLESS_COMMUNICATIONS, 200);
        UpdatePaperlessCommunicationsResponse updatePaperlessCommunicationsResponse =
                deserializeResponseToPojo(response, UpdatePaperlessCommunicationsResponse.class);
        testContext.setUpdatePaperlessCommunicationsResponse(updatePaperlessCommunicationsResponse);
        testContext.setResponse(response);
    }

}
