package com.gng.api.steps.csi.GetBankDraftInfo;

import com.gng.api.pages.csi.GetBankDraftInfoPage.GetBankDraftInfoPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.GetBankDraftInfo.GetBankDraftInfoLabel.get_bank_draft_info;

@Slf4j
public class GetBankDraftInfoApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final GetBankDraftInfoPage getBankDraftInfoPage;

    public GetBankDraftInfoApiSteps(TestContext testContext, GetBankDraftInfoPage getBankDraftInfoPage) {
        this.testContext = testContext;
        this.getBankDraftInfoPage = getBankDraftInfoPage;
        testContext.setGetBankDraftInfoApiPage(getBankDraftInfoPage);
        TestContextHolder.set(testContext);
    }

    @When("a request is made to GetBankDraftInfo Api for {string}")
    public void a_request_is_made_to_GetBankDraftInfo_Api(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        getBankDraftInfoPage.validateResponseForTestConditions(
                get_bank_draft_info,
                GetBankDraftInfoLabel.valueOf(testCondition)
        );
    }
}
