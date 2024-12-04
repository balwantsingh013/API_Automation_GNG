package com.gng.api.steps.ServiceOrdersSteps;

import com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentApiPage;
import com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentLabels;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;



@Slf4j
public class SaveEnrollmentApiSteps {


    private final TestContext testContext;
    private final SaveEnrollmentApiPage saveEnrollmentApiPage;

    public SaveEnrollmentApiSteps(TestContext testContext, SaveEnrollmentApiPage saveEnrollmentApiPage) {
        this.testContext = testContext;
        this.saveEnrollmentApiPage = saveEnrollmentApiPage;
        testContext.setSaveEnrollmentApiPage(saveEnrollmentApiPage);
    }

    @When("a valid SaveEnrollment API request payload")
    public void a_valid_save_enrollment_api_request_payload() {
        saveEnrollmentApiPage.sendSaveEnrollmentRequest();
    }
    @When("a request is made to the SaveEnrollment Api with invalid promotion code")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_invalid_promotion_code() {
        saveEnrollmentApiPage.sendSaveEnrollmentRequestForInvalidPromotionCode();
    }
    @When("a request is made to the SaveEnrollment Api with invalid {string} code")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_invalid_customer_code(String invalidparam) {
        saveEnrollmentApiPage.sendSaveEnrollmentRequestWithInvalidParam(invalidparam);
    }

}