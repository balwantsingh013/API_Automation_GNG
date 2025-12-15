package com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.SaveEnrollment;

import com.gng.api.pages.marketerSwitch.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel.save_enrollment;

@Slf4j
public class SaveEnrollmentApiSteps {

    private final TestContext testContext;
    private final SaveEnrollmentApiPage saveEnrollmentApiPage;

    public SaveEnrollmentApiSteps(TestContext testContext, SaveEnrollmentApiPage saveEnrollmentApiPage) {
        this.testContext = testContext;
        this.saveEnrollmentApiPage = saveEnrollmentApiPage;
        testContext.setSaveEnrollmentApiPage(saveEnrollmentApiPage);
    }

    @When("a request is made to the SaveEnrollment Api for marketer switch calls with invalid parameters for {string} condition")
    public void sendInValidRequest(String testCondition) {
        saveEnrollmentApiPage.validateNegativeConditions(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SaveEnrollment Api for marketer switch calls with valid parameters for {string} condition")
    public void sendValidRequest(String testCondition) {
        saveEnrollmentApiPage.validatePositiveConditions(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a second request is made to the SaveEnrollment Api for marketer switch calls with valid parameters for {string} condition")
    public void sendValidSecondRequest(String testCondition) {
        saveEnrollmentApiPage.validateSecondRequestPositiveConditions(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SaveEnrollment Api for external marketerSwitch calls with valid parameters for {string} condition")
    public void sendValidExternalRequest(String testCondition) {
        saveEnrollmentApiPage.validateExternalConditions(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SaveEnrollment Api for external marketerSwitch second calls with valid parameters for {string} condition")
    public void sendValidSecondExternalRequest(String testCondition) {
        saveEnrollmentApiPage.validateSecondExternalConditions(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition));
    }
}
