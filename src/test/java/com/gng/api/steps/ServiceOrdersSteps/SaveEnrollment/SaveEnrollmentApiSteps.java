package com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment;

import com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;

import static com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel.*;

public class SaveEnrollmentApiSteps {

    private final TestContext testContext;
    private final SaveEnrollmentApiPage saveEnrollmentApiPage;

    public SaveEnrollmentApiSteps(TestContext testContext, SaveEnrollmentApiPage saveEnrollmentApiPage) {
        this.testContext = testContext;
        this.saveEnrollmentApiPage = saveEnrollmentApiPage;
        testContext.setSaveEnrollmentApiPage(saveEnrollmentApiPage);
    }


    @When("a request is made to the SaveEnrollment Api")
    public void a_request_is_made_to_the_get_account_info_api() {
        saveEnrollmentApiPage.sendSaveEnrollmentRequest(save_enrollment);
    }

    @When("a request is made to the SaveEnrollment Api with {string}")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with(String requestID)
    {
        saveEnrollmentApiPage.validateInvalidRequestIDCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(requestID));
    }
}
