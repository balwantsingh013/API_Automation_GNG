package com.gng.api.steps.turnOn.ServiceOrdersSteps.GetPrepayPlanRequote;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.turnOn.ServiceOrdersPages.GetPrepayPlanRequotePage.GetPrepayPlansRequoteApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pages.turnOn.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentHelper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.gng.api.steps.turnOn.ServiceOrdersSteps.GetPrepayPlanRequote.GetPrepayPlansRequoteApiLabel.get_prepay_plans_requote;


public class GetPrepayPlansRequoteApiSteps {
    private final TestContext testContext;
    private final GetPrepayPlansRequoteApiPage getPrepayPlansRequoteApiPage;

    public GetPrepayPlansRequoteApiSteps(TestContext testContext,
                                         GetPrepayPlansRequoteApiPage getPrepayPlansRequoteApiPage) {
        this.testContext = testContext;
        this.getPrepayPlansRequoteApiPage = getPrepayPlansRequoteApiPage;
    }

    @Before("@GetPrepayPlansRequote")
    public void initTransaction() {
        // Create or retrieve a transaction ID only once per scenario
//        if (!testContext.containsKey("transactionID")) {
//            String tranId = SaveEnrollmentHelper.createEnrollmentAndReturnTransactionId();
//            testContext.put("transactionID", tranId);
//        }
    }

    @Given("a valid non‑expired prepay transaction exists for {string}")
    public void validTransactionExists(String testCondition) {
        // Place holder step – transactionID is already in TestContext from @Before hook
    }

    @When("a request is made to the GetPrepayPlansRequote Api with {string} condition")
    public void callPrepayPlansRequote(String testCondition) {
     //   getPrepayPlansRequoteApiPage.setRequestParams(get_prepay_plans_requote, GetPrepayPlansRequoteApiLabel.values(testCondition));
    }

    @Then("response should have success true, errorCode {int} and errorMessage null")
    public void verifySuccess(int errorCode) {
        var response = testContext.getGetPrepayPlansRequoteResponse();
        assert response.isSuccess();
        assert response.getErrorCode() == errorCode;
        assert response.getErrorMessage() == null;
    }

    @Then("the response should have success false, errorCode {int} and errorMessage {string}")
    public void verifyDuplicateError(int code, String errorMsg) {
        var response = testContext.getGetPrepayPlansRequoteResponse();
        assert !response.isSuccess();
        assert response.getErrorCode() == code;
        assert response.getErrorMessage().equals(errorMsg);
    }
}
