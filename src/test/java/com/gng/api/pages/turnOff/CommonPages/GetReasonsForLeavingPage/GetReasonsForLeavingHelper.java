package com.gng.api.pages.turnOff.CommonPages.GetReasonsForLeavingPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CommonPojo.GetReasonsForLeaving.GetReasonsForLeavingRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.Common.GetReasonsForLeaving.GetReasonsForLeavingLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetReasonsForLeavingHelper {

    private final TestContext testContext;

    public GetReasonsForLeavingHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetReasonsForLeavingRequest preparePayload(GetReasonsForLeavingLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = GetReasonsForLeavingLabel.get_reasons_for_leaving.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetReasonsForLeavingRequest.class);
    }

    public void setEtcExistsAndRequestID(GetReasonsForLeavingRequest payload, Boolean etcExists) {
        payload.setEtcExists(etcExists);
        payload.setRequestID(FakerDataGenerator.generateString(10));
    }

    public void setRequestIDsBasedOnTestCodition(GetReasonsForLeavingRequest payload, GetReasonsForLeavingLabel testCondition) {
        switch (testCondition) {
            case TC1_DUPLICATE_REQUEST_ID:
                payload.setRequestID("123");
                break;

            case TC2_NULL_REQUEST_ID:
                payload.setRequestID(null);
                break;

            case TC3_INVALID_REQUEST_ID:
                payload.setRequestID(FakerDataGenerator.generateDigits(33));
                break;
        }
    }

    public void setLoginIDsBasedOnTestCodition(GetReasonsForLeavingRequest payload, GetReasonsForLeavingLabel testCondition) {
        switch (testCondition) {
            case TC4_1_LOGIN_ID_EMPTY:
                payload.setLoginID("");
                break;

            case TC4_2_LOGIN_ID_NULL:
                payload.setLoginID(null);
                break;

            case TC5_LOGIN_ID_INVALID_MAX_LENGTH_VALIDATION:
                payload.setLoginID(FakerDataGenerator.generateString(31));
                break;

            case TC6_LOGIN_ID_INVALID_WITH_SPECIAL_CHARACTERS:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(10));
                break;

            case TC7_LOGIN_ID_INVALID:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setLoginID("FAKE");
                break;

        }
    }

    public void setETCExistsBasedOnTestCodition(GetReasonsForLeavingRequest payload, GetReasonsForLeavingLabel testCondition) {
                payload.setEtcExists(null);
    }
}