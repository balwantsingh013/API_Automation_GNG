package com.gng.api.steps.meterSet.ServiceOrdersSteps.SaveEnrollment;
import com.gng.api.pages.meterSet.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.meterSet.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel.save_enrollment;


@Slf4j
public class SaveEnrollmentApiSteps {

    private final TestContext testContext;
    private final SaveEnrollmentApiPage saveEnrollmentApiPage;

    public SaveEnrollmentApiSteps(TestContext testContext, SaveEnrollmentApiPage saveEnrollmentApiPage) {
        this.testContext = testContext;
        this.saveEnrollmentApiPage = saveEnrollmentApiPage;
        testContext.setSaveEnrollmentApiPage(saveEnrollmentApiPage);
    }

    @When("a request is made to the SaveEnrollment Api for meterSet with invalid parameters for {string} condition")
    public void sendInvalidRequest(String testCondition) {
        saveEnrollmentApiPage.validateInvalidParameters(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SaveEnrollment Api for meterSet with valid parameters for {string} condition")
    public void sendValidRequest(String testCondition) {
        saveEnrollmentApiPage.validateValidParameters(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a second request is made to the SaveEnrollment Api for meterSet with valid parameters for {string} condition")
    public void sendValidSecondRequest(String testCondition) {
        saveEnrollmentApiPage.validateValidSecondParameters(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SaveEnrollment Api for external meterSet calls with valid parameters for {string} condition")
    public void sendValidExternalRequest(String testCondition) {
        saveEnrollmentApiPage.validateExternalConditions(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SaveEnrollment Api for external meterSet second calls with valid parameters for {string} condition")
    public void sendValidSecondExternalRequest(String testCondition) {
        saveEnrollmentApiPage.validateSecondCallExternalConditions(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

}

