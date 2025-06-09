package com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment;

import com.gng.api.pages.turnOff.ServiceOrdersPages.SaveUnenrollmentPage.SaveUnenrollmentApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.When;

import static com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel.save_unenrollment;
import static com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel.save_unenrollment_mandatory;

public class SaveUnenrollmentApiSteps {

    @ParameterType("true|false")
    public Boolean booleanValue(String value) {
        return Boolean.valueOf(value);
    }

    private final TestContext testContext;
    private final SaveUnenrollmentApiPage saveUnenrollmentApiPage;

    public SaveUnenrollmentApiSteps(TestContext testContext, SaveUnenrollmentApiPage saveUnenrollmentApiPage) {
        this.testContext = testContext;
        this.saveUnenrollmentApiPage = saveUnenrollmentApiPage;
        testContext.setSaveEnrollmentApiPage(saveUnenrollmentApiPage);
    }

    @When("a request is made to the SaveUnenrollment Api with {string}")
    public void a_request_is_made_to_the_SaveUnenrollment_Api_with(String requestID)
    {
        saveUnenrollmentApiPage.validateInvalidRequestIDCases(save_unenrollment_mandatory, SaveUnenrollmentApiLabel.valueOf(requestID));
    }

    @When("a request is made to the SaveUnenrollment Api for account with {string} plan {string} type with forwardingAddressIs {string} with type {string} and turnoffreason {string} and setEmail {booleanValue} with etcExists {booleanValue}")
    public void a_request_is_made_to_the_SaveUnenrollment_Api_Active(String pricePlan, String sclsCode, String forwardingAddressIs, String type, String turnoffreason, Boolean setEmail, Boolean etcExists)
    {
        saveUnenrollmentApiPage.validateForActiveRAMVS(save_unenrollment, pricePlan, sclsCode, forwardingAddressIs, type, turnoffreason, setEmail, etcExists);
    }
}
