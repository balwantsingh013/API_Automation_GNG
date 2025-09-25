package com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.turnOff.ServiceOrdersPages.SaveUnenrollmentPage.SaveUnenrollmentApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.ExtentReportManager;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.When;

import static com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel.save_unenrollment;
import static com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel.save_unenrollment_mandatory;

public class SaveUnenrollmentApiSteps {

    private final TestContext testContext;
    private final SaveUnenrollmentApiPage saveUnenrollmentApiPage;

    @ParameterType("true|false")
    public Boolean booleanValue(String value) {
        return Boolean.valueOf(value);
    }

    public SaveUnenrollmentApiSteps(TestContext testContext, SaveUnenrollmentApiPage saveUnenrollmentApiPage) {
        this.testContext = testContext;
        this.saveUnenrollmentApiPage = saveUnenrollmentApiPage;
        testContext.setSaveEnrollmentApiPage(saveUnenrollmentApiPage);
    }

    @When("a request is made to the SaveUnenrollment Api with {string}")
    public void a_request_is_made_to_the_SaveUnenrollment_Api_with(String requestID) {
        String description = GlobalEnums.ScenarioDescriptions.valueOf(requestID).getValue();

        // Log to Extent report
        ExtentReportManager.logTestDescription(description);

        // Log to Allure report
        io.qameta.allure.Allure.label("Scenario Description", description);

        saveUnenrollmentApiPage.validateInvalidRequestIDCases(
                save_unenrollment_mandatory,
                SaveUnenrollmentApiLabel.valueOf(requestID)
        );
    }


    @When("a request is made to the SaveUnenrollment Api for account with {string} plan {string} type with forwardingAddressIs {string} with type {string} and turnoffreason {string} and setEmail {string} with etcExists {string}")
    public void a_request_is_made_to_the_SaveUnenrollment_Api_Active(String pricePlan, String sclsCode, String forwardingAddressIs, String type, String testCondition, String setEmail, String etcExists)
    {
        saveUnenrollmentApiPage.validateForActiveRAMVS(save_unenrollment, pricePlan, sclsCode, SaveUnenrollmentApiLabel.valueOf(forwardingAddressIs), SaveUnenrollmentApiLabel.valueOf(type), SaveUnenrollmentApiLabel.valueOf(testCondition), booleanValue(setEmail), booleanValue(etcExists));
    }
}
