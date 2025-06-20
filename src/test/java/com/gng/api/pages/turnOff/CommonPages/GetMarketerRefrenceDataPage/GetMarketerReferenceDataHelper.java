package com.gng.api.pages.turnOff.CommonPages.GetMarketerRefrenceDataPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CommonPojo.GetMerketerReferenceData.GetMarketerReferenceDataRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.Common.GetMarketerReferenceData.GetMarketerReferenceDataLabel;
import com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetMarketerReferenceDataHelper {

    private final TestContext testContext;

    public GetMarketerReferenceDataHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetMarketerReferenceDataRequest preparePayload(GetMarketerReferenceDataLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = GetMarketerReferenceDataLabel.get_marketer_reference_data.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetMarketerReferenceDataRequest.class);
    }
}
