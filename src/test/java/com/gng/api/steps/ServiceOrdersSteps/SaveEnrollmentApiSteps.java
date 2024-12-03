package com.gng.api.steps.ServiceOrdersSteps;

import com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SaveEnrollmentApiSteps {


    private SaveEnrollmentApiPage saveEnrollmentApiPage;

    public void SavaEnrollmentApiSteps(TestContext testContext, SaveEnrollmentApiPage saveEnrollmentApiPage) {
        this.saveEnrollmentApiPage = saveEnrollmentApiPage;
        testContext.setSaveEnrollmentApiPage(saveEnrollmentApiPage);
    }

    @When("a valid SaveEnrollment API request payload")
    public void a_valid_save_enrollment_api_request_payload() {
        saveEnrollmentApiPage.sendSaveEnrollmentRequest();
    }

//    @When("a request is made to the Save Enrollment  Api with Null Note")
//    public void RequestIsMadeToTheSaveEnrollmentApiWithNullNote() {
//        saveEnrollmentApiPage.sendSaveEnrollmentRequestWithHappyFlow();
//    }
    @When("a request is made to the Save Enrollment Api with invalid Customer Code")
    public void RequestIsMadeToTheSaveEnrollmentApiWithMissingRequestID( ) {
        saveEnrollmentApiPage.sendSaveEnrollmentRequestWithInvalidCustomerCode();
    }
    @When("a request is made to the Save Enrollment Api with Invalid Promotion Code")
    public void RequestIsMadeToTheSaveEnrollmentApiWithInvalidPromotionCode( ) {
        saveEnrollmentApiPage.sendSaveEnrollmentRequestWithInvalidPromotionCode();
    }
    @Then("verify response code of save enrollment Api with invalid promo  {string} code is {int}")
    public void verify_response_code_of_save_enrollment_api_invalid_promotion_code(String param, Integer statusCode) {
        saveEnrollmentApiPage.verifyResponseCodeForInvalidPromotionCode(param, statusCode);
    }
    @Then("verify response code of save enrollment Api with invalid customer  {string} code is {int}")
    public void verify_response_code_of_save_enrollment_api_invalid_customer_code(String param, Integer statusCode) {
        saveEnrollmentApiPage.verifyResponseCodeForInvalidCustomerCode(param, statusCode);
    }


    @Then("the response should contain a valid transactionID")
    public void the_response_should_contain_a_valid_transaction_id() {
    }


}