package com.gng.api.steps.csi.AuthenticateCustomer;

import com.gng.api.pages.csi.AuthenticateCustomerPage.AuthenticateCustomerPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.AuthenticateCustomer.AuthenticateCustomerLabel.authenticate_customer;

@Slf4j
public class AuthenticateCustomerApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final AuthenticateCustomerPage authenticateCustomerPage;

    public AuthenticateCustomerApiSteps(TestContext testContext, AuthenticateCustomerPage authenticateCustomerPage) {
        this.testContext = testContext;
        this.authenticateCustomerPage = authenticateCustomerPage;
        testContext.setAuthenticateCustomerApiPage(authenticateCustomerPage);
        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to AuthenticateCustomer Api for {string}")
    public void a_request_is_made_to_the_AuthenticateCustomer_Api_with_test_condition(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        authenticateCustomerPage.validateResponseForTestConditions(
                authenticate_customer,
                AuthenticateCustomerLabel.valueOf(testCondition)
        );
    }
}
