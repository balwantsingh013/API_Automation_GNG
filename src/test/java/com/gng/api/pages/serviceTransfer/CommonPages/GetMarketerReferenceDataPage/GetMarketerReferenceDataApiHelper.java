package com.gng.api.pages.serviceTransfer.CommonPages.GetMarketerReferenceDataPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CommonPojo.GetMerketerReferenceData.GetMarketerReferenceDataRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.serviceTransfer.Common.GetMarketerReferenceData.GetMarketerReferenceDataApiLabel;
import com.gng.api.steps.turnOff.Common.GetMarketerReferenceData.GetMarketerReferenceDataLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetMarketerReferenceDataApiHelper {
    private final TestContext testContext;

    public GetMarketerReferenceDataApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetMarketerReferenceDataRequest preparePayload(GetMarketerReferenceDataApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = GetMarketerReferenceDataLabel.get_marketer_reference_data.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetMarketerReferenceDataRequest.class);
    }

    public void setRequestPayloadAsPerTestCondition(GetMarketerReferenceDataRequest payload, GetMarketerReferenceDataApiLabel testCodition){

        switch(testCodition){
            case MISSING_REQUEST_ID_TC89:
                payload.setRequestID("");
                break;

            case INVALID_REQUEST_ID_LENGTH_TC90:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case DUPLICATE_REQUEST_ID_TC91:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case MISSING_LOGIN_ID_TC92:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));
                payload.setLoginID("");
                break;

            case INVALID_LOGIN_ID_NOT_ALPHANUMERIC_TC93:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case INVALID_LOGIN_ID_NOT_FOUND_TC94:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));
                payload.setLoginID(FakerDataGenerator.generateString(8));
                break;
        }

    }

    public void setRequestPayloadForValidTCs(GetMarketerReferenceDataRequest payload){
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));
    }
}
