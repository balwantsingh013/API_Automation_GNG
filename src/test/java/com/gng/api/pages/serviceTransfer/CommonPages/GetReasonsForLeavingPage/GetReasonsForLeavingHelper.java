package com.gng.api.pages.serviceTransfer.CommonPages.GetReasonsForLeavingPage;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CommonPojo.GetReasonsForLeaving.GetReasonsForLeavingRequest;
import com.gng.api.pojo.CommonPojo.GetReasonsForLeaving.Response.TransferTurnOffReason;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.serviceTransfer.Common.GetReasonsForLeaving.GetReasonsForLeavingApiLabel;
import com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.TurnOffReason;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.List;

import static com.gng.api.steps.serviceTransfer.Common.GetReasonsForLeaving.GetReasonsForLeavingApiLabel.get_reasons_for_leaving;
import static com.gng.api.steps.serviceTransfer.Common.GetReasonsForLeaving.GetReasonsForLeavingApiLabel.get_reasons_for_leaving_mandatory;

@Slf4j
public class GetReasonsForLeavingHelper {

    private final TestContext testContext;

    public GetReasonsForLeavingHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetReasonsForLeavingRequest preparePayload(GetReasonsForLeavingApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(get_reasons_for_leaving)
                ? get_reasons_for_leaving.toString()
                : get_reasons_for_leaving_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetReasonsForLeavingRequest.class);
    }

    public void setParametersBasedOnType(GetReasonsForLeavingRequest payload,  GetReasonsForLeavingApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());

        switch (testCondition) {
            // Request ID
            case DUPLICATE_REQUEST_ID_NEGATIVE_TC76:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;
            case MISSING_REQUEST_ID_NEGATIVE_TC77:
                payload.setRequestID("");
                break;
            case INVALID_REQUEST_ID_LENGTH_NEGATIVE_TC78:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(200));
                break;

            // Login ID
            case MISSING_LOGIN_ID_NEGATIVE_TC79:
                payload.setLoginID("");
                break;
            case INVALID_LOGIN_ID_TOO_LONG_NEGATIVE_TC80:
                payload.setLoginID(FakerDataGenerator.getRandomString(50));
                break;
            case INVALID_LOGIN_ID_NOT_ALPHANUMERIC_NEGATIVE_TC81:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(6));
                break;
            case INVALID_LOGIN_ID_NOT_FOUND_NEGATIVE_TC82:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateString(8)); // non-existent username
                break;

            // Transaction Type validations
            case MISSING_TRANSACTION_TYPE_NEGATIVE_TC84:
                payload.setTransactionType("");
                break;
            case INVALID_TRANSACTION_TYPE_VALUE_NEGATIVE_TC85:
                payload.setTransactionType(GlobalEnums.TransactionType.INVALID.getValue());
                break;
            case INVALID_TRANSACTION_TYPE_LENGTH_NEGATIVE_TC86:
                payload.setTransactionType(FakerDataGenerator.getRandomString(10));
                break;

            // Missing etcExists
            case MISSING_ETC_EXISTS_NEGATIVE_TC83:
                payload.setEtcExists(null);
                break;

            // Positive cases
            case ETC_EXISTS_TRUE_RETURNS_MOVING_SERVICE_TRANSFER_POSITIVE_TC87:
                payload.setEtcExists(Boolean.TRUE);
                break;
            case ETC_EXISTS_FALSE_RETURNS_NO_RECORDS_POSITIVE_TC88:
                payload.setEtcExists(Boolean.FALSE);
                break;
            default:
                break;
        }
    }

    public void validateSingleTurnOffReason(String expectedTurnOffReasonString){
        Assert.assertEquals(testContext.getGetReasonsForLeavingResponse().getErrorCode(), 0, "Expected error code 0");
        Assert.assertEquals(testContext.getGetReasonsForLeavingResponse().getErrorMessage(), "", "Expected empty error message");

        List<TransferTurnOffReason> reasons = testContext.getGetReasonsForLeavingResponse().getData().getTurnOffReasons();
        if (expectedTurnOffReasonString == null || expectedTurnOffReasonString.trim().isEmpty()) {
            boolean hasZeroPairOrEmpty = reasons.isEmpty() || reasons.stream()
                    .anyMatch(r -> "0".equals(r.getReasonForTurnOff()) &&
                            "0".equals(r.getSubReasonForTurnOff()));
            Assert.assertTrue(hasZeroPairOrEmpty,
                    "Expected either empty turnOffReasons or (reason=0, subReason=0), but got: " + reasons);
        }
         else {
             TurnOffReason expectedTurnOffReason = TurnOffReason.valueOf(expectedTurnOffReasonString);

            boolean anyMatch = reasons.stream().anyMatch(r ->
                    expectedTurnOffReason.getReason().equals(r.getReasonForTurnOff()) &&
                            expectedTurnOffReason.getSubReason().equals(r.getSubReasonForTurnOff())
            );
            Assert.assertEquals(testContext.getGetReasonsForLeavingResponse().getData().getTurnOffReasons().size(),
                    1,
                    "Expected one turn off reason"
            );

            Assert.assertTrue(anyMatch,
                    "Did not find expected [" + expectedTurnOffReason.getReason() + " | " + expectedTurnOffReason.getSubReason() +
                            "], actual: " + reasons);
        }
    }

}
