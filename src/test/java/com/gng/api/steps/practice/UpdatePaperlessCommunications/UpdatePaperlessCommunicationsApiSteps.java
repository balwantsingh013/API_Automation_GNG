package com.gng.api.steps.practice.UpdatePaperlessCommunications;

import com.gng.api.pages.practice.UpdatePaperlessCommunicationsPage.UpdatePaperlessCommunicationsPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;

import static com.gng.api.steps.practice.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsLabel.practice_update_paperless_communications;

public class UpdatePaperlessCommunicationsApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken();
    }

    private final UpdatePaperlessCommunicationsPage updatePaperlessCommunicationsPage;

    public UpdatePaperlessCommunicationsApiSteps(TestContext testContext,
                                                 UpdatePaperlessCommunicationsPage updatePaperlessCommunicationsPage) {
        this.updatePaperlessCommunicationsPage = updatePaperlessCommunicationsPage;
        testContext.setUpdatePaperlessCommunicationsApiPage(updatePaperlessCommunicationsPage);
        TestContextHolder.set(testContext);
    }

    @When("a practice request is made to UpdatePaperlessCommunications Api for {string}")
    public void a_practice_request_is_made_to_update_paperless_communications_api(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        updatePaperlessCommunicationsPage.validateResponseForTestCondition(
                practice_update_paperless_communications,
                UpdatePaperlessCommunicationsLabel.valueOf(testCondition));
    }
}
