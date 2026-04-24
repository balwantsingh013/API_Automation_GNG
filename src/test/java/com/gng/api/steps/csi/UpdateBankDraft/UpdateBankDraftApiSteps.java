package com.gng.api.steps.csi.UpdateBankDraft;

import com.gng.api.pages.csi.UpdateBankDraftPage.UpdateBankDraftPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.UpdateBankDraft.UpdateBankDraftLabel.update_bank_draft;

@Slf4j
public class UpdateBankDraftApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final UpdateBankDraftPage updateBankDraftPage;

    public UpdateBankDraftApiSteps(TestContext testContext, UpdateBankDraftPage updateBankDraftPage) {
        this.testContext = testContext;
        this.updateBankDraftPage = updateBankDraftPage;
        testContext.setUpdateBankDraftApiPage(updateBankDraftPage);
        TestContextHolder.set(testContext);
    }

    @When("a request is made to UpdateBankDraft Api for {string}")
    public void a_request_is_made_to_UpdateBankDraft_Api(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        updateBankDraftPage.validateResponseForTestConditions(
                update_bank_draft,
                UpdateBankDraftLabel.valueOf(testCondition)
        );
    }
}
