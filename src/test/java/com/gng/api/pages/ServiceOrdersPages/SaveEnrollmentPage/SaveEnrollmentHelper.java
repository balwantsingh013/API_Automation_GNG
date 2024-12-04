package com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SaveEnrollmentHelper
{

    private final TestContext testContext;

    public SaveEnrollmentHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    SaveEnrollmentRequest preparePayload(SaveEnrollmentApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(SaveEnrollmentApiLabel.save_enrollment)
                ? SaveEnrollmentApiLabel.save_enrollment.toString()
                : SaveEnrollmentApiLabel.save_enrollment_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, SaveEnrollmentRequest.class);
    }

}
