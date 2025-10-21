package com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.SaveEnrollment;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import com.gng.api.pages.serviceTransfer.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentApiPage;
import static com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel.*;

@Slf4j
public class SaveEnrollmentApiSteps {

    private final TestContext testContext;
    private final SaveEnrollmentApiPage saveEnrollmentApiPage;

    public SaveEnrollmentApiSteps(TestContext testContext, SaveEnrollmentApiPage saveEnrollmentApiPage) {
        this.testContext = testContext;
        this.saveEnrollmentApiPage = saveEnrollmentApiPage;
        testContext.setSaveEnrollmentApiPage(saveEnrollmentApiPage);
    }

    @When("a request is made to the SaveEnrollment Api for serviceTransfer with invalid parameters for {string} condition")
    public void sendInvalidRequest(String testCondition) {
        saveEnrollmentApiPage.validateInvalidParameters(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SaveEnrollment Api for serviceTransfer for external calls with valid parameters for {string} condition")
    public void sendValidRequest(String testCondition) {
        saveEnrollmentApiPage.validateExternalConditions(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

}
