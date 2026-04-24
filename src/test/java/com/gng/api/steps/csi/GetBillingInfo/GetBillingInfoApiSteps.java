package com.gng.api.steps.csi.GetBillingInfo;

import com.gng.api.pages.csi.GetBillingInfoPage.GetBillingInfoPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import static com.gng.api.steps.csi.GetBillingInfo.GetBillingInfoLabel.get_billing_info;

@Slf4j
public class GetBillingInfoApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken();
    }

    private final TestContext testContext;
    private final GetBillingInfoPage getBillingInfoPage;

    public GetBillingInfoApiSteps(TestContext testContext, GetBillingInfoPage getBillingInfoPage) {
        this.testContext = testContext;
        this.getBillingInfoPage = getBillingInfoPage;
        testContext.setGetAccountInfoApiPage(getBillingInfoPage);
        TestContextHolder.set(testContext);
    }

    @When("a request is made to GetBillingInfo Api for {string}")
    public void a_request_is_made_to_the_GetBillingInfo_Api_with_invalid_values(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        getBillingInfoPage.validateResponseForNegativeTestConditions(get_billing_info, GetBillingInfoLabel.valueOf(testCondition));
    }

}