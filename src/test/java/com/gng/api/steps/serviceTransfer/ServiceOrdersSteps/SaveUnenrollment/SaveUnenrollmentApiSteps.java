package com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.SaveUnenrollment;
import com.gng.api.pages.serviceTransfer.ServiceOrdersPages.SaveUnenrollmentPage.SaveUnenrollmentApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.When;

import static com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel.save_unenrollment;
import static com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel.save_unenrollment_mandatory;

public class SaveUnenrollmentApiSteps {

    private final TestContext testContext;
    private final SaveUnenrollmentApiPage saveUnenrollmentApiPage;

    public SaveUnenrollmentApiSteps(TestContext testContext, SaveUnenrollmentApiPage saveUnenrollmentApiPage) {
        this.testContext = testContext;
        this.saveUnenrollmentApiPage = saveUnenrollmentApiPage;
        testContext.setSaveEnrollmentApiPage(saveUnenrollmentApiPage);
    }

    @When("a request is made to the SaveUnenrollment ServiceTransfer Api with invalid parameters for {string} condition")
    public void request_with_invalid_parameters(String testCondition) {
        saveUnenrollmentApiPage.validateInvalidParametersCases(save_unenrollment_mandatory, SaveUnenrollmentApiLabel.valueOf(testCondition)
        );
    }

    @When("a request is made to the SaveUnenrollment Api for account with {string} type and turnoffreason {string} and setEmail {string} with etcExists {string}")
    public void a_request_is_made_to_the_SaveUnenrollment_Api_with_accountType(
            String accountType,
            String testCondition,
            String setEmail,
            String etcExists) {

        saveUnenrollmentApiPage.validateForActiveRSAccount(
                save_unenrollment,
                accountType,
                SaveUnenrollmentApiLabel.valueOf(testCondition),
                booleanValueNew(setEmail),
                etcExists
        );
    }


    @ParameterType("true|false")
    public Boolean booleanValueNew(String value) {
        return Boolean.valueOf(value);
    }

}
