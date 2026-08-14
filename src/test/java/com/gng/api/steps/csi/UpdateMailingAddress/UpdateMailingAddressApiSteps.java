package com.gng.api.steps.csi.UpdateMailingAddress;

import com.gng.api.pages.csi.UpdateMailingAddressPage.UpdateMailingAddressPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import static com.gng.api.steps.csi.UpdateMailingAddress.UpdateMailingAddressLabel.update_mailing_address;

@Slf4j
public class UpdateMailingAddressApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final UpdateMailingAddressPage updateMailingAddressPage;

    public UpdateMailingAddressApiSteps(TestContext testContext, UpdateMailingAddressPage updateMailingAddressPage) {
        this.testContext = testContext;
        this.updateMailingAddressPage = updateMailingAddressPage;
        testContext.setGetAccountInfoApiPage(updateMailingAddressPage);
        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to UpdateMailingAddress Api for {string}")
    public void a_request_is_made_to_the_UpdateMailingAddress_Api_with_test_condition(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        updateMailingAddressPage.validateResponseForNegativeTestConditions(
                update_mailing_address,
                UpdateMailingAddressLabel.valueOf(testCondition)
        );
    }

    @When("perform database validation for {string}")
    public void perform_database_validation(String testCondition){
        updateMailingAddressPage.databaseValidations(UpdateMailingAddressLabel.valueOf(testCondition));
    }

    @When("verify if UpdateMailingAddress login id is saved")
    public void verify_if_update_mailing_address_login_id_is_saved() {
        updateMailingAddressPage.validateLoginId();
    }
}
