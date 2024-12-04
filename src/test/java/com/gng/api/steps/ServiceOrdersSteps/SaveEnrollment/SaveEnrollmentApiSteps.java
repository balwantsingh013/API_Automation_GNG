package com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment;

import com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;

import static com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel.save_enrollment;

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

}
