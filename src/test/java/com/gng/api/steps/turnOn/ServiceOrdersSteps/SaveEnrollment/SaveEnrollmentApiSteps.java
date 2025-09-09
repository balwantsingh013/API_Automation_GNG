package com.gng.api.steps.turnOn.ServiceOrdersSteps.SaveEnrollment;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.turnOn.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;

import static com.gng.api.steps.turnOn.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel.*;

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

    @When("a request is made to the SaveEnrollment Api with {string}")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with(String requestID)
    {
        saveEnrollmentApiPage.validateInvalidRequestIDCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(requestID));
    }
    @When("a request is made to the SaveEnrollment Api with invalid customer code for {string} condition")
    public void a_request_is_made_to_the_SaveEnrollment_with_customer(String testCondition)
    {
        saveEnrollmentApiPage.validateInvalidCustomerCodeCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the SaveEnrollment Api with an invalid premises code for {string} condition")
    public void  a_request_is_made_to_the_SaveEnrollment_with_premises (String testCondition)
    {
        saveEnrollmentApiPage.validateInvalidPremisesCodeCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the SaveEnrollment Api with invalid transactionID for {string} condition")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_transaction (String testCondition)
    {
        saveEnrollmentApiPage.validateInvalidTransactionIDCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the SaveEnrollment Api with invalid transaction type for {string} condition")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_transaction_type (String testCondition)
    {
        saveEnrollmentApiPage.validateInvalidTransactionTypeCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the SaveEnrollment Api with an invalid planCode for {string} condition")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_plan_code (String testCondition)
    {
        saveEnrollmentApiPage.validateInvalidPlanCodeCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the SaveEnrollment Api with an invalid promotionCode for {string} condition")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_invalid_promotion_code (String testCondition)
    {
        saveEnrollmentApiPage.validateInvalidPromotionCodeCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SaveEnrollment Api with login {string} ID")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_login_ID (String loginID)
    {
        saveEnrollmentApiPage.validateInvalidLoginIDCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(loginID));
    }
    @When("a request is made to the SaveEnrollment Api with an invalid enrollmentStatus for {string} condition")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_enrollment_status (String testCondition)
    {
        saveEnrollmentApiPage.validateInvalidEnrollmentStatusCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the SaveEnrollment Api with an invalid paymentConfirmation number for {string} condition")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_invalid_paymentConfirmation_number (String testCondition)
    {
        saveEnrollmentApiPage.validateInvalidPaymentConfirmationNumberCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the SaveEnrollment Api with an invalid billing plan for {string} condition")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_billing_plan (String testCondition)
    {
        saveEnrollmentApiPage.validateInvalidBillingPlanCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SaveEnrollment Api with an invalid estimated budget amount for {string} condition")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_invalid_estimated_budget_amount (String testCondition)
    {
        saveEnrollmentApiPage.validateInvalidEstimatedBudgetAmountCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SaveEnrollment Api with an invalid service date for {string} condition")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_invalid_service_date (String testCondition)
    {
        saveEnrollmentApiPage.validateInvalidServiceDateCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SaveEnrollment Api with invalid parameters for {string} condition")
    public void a_request_is_made_to_the_SaveEnrollment_Api_with_invalid_parameters (String testCondition)
    {
        saveEnrollmentApiPage.validateInvalidParametersCases(save_enrollment_mandatory, SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the Save Enrollment API for the {string} with {string} and {string}")
    public void a_request_is_made_to_the_SaveEnrollment_Api_positive(String testCondition, String planCode, String promotionCode)
    {
        saveEnrollmentApiPage.validateSaveEnrollmentPositiveTCs(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition), planCode, promotionCode);
    }

    @When("a request is made to the Save Enrollment API for completion for {string} with {string} and {string}")
    public void a_request_is_made_to_save_enrollment_to_complete_prev_saved_enrollment(String testCondition, String planCode, String promotionCode){
        saveEnrollmentApiPage.validateCEForPreviouslySavedEnrollment(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition), planCode, promotionCode);
    }
    @When("a request is made to the SaveEnrollment Api for with {string} planCode for {string} condition")
    public void saveEnrollment(String planCode, String testCondition) {
        saveEnrollmentApiPage.validateSaveEnrollmentByPlanCode(SaveEnrollmentApiLabel.save_enrollment, GlobalEnums.PlanCode.valueOf(planCode), SaveEnrollmentApiLabel.valueOf(testCondition));
    }

    @When("perform database validation with the following parameters:")
    public void validateDatabaseParameters(DataTable dataTable) {
        saveEnrollmentApiPage.performDatabaseValidationAfterEnrollment(dataTable);
        }

    @When("a request is made to the SaveEnrollment Api for prepay with {string} after requote for {string}")
    public void a_request_made_to_enrollment_after_requote(String planCode, String testCondition){
        saveEnrollmentApiPage.validateEnrollmentAfterRequote(save_enrollment, SaveEnrollmentApiLabel.valueOf(testCondition), planCode);
    }
    }

