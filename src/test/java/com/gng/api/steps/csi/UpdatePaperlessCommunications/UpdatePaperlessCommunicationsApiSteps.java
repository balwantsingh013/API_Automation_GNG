package com.gng.api.steps.csi.UpdatePaperlessCommunications;

import com.gng.api.pages.csi.UpdatePaperlessCommunicationsPage.UpdatePaperlessCommunicationsPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import static com.gng.api.steps.csi.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsLabel.update_paperless_communications;

@Slf4j
public class UpdatePaperlessCommunicationsApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken();
    }

    private final TestContext testContext;
    private final UpdatePaperlessCommunicationsPage updatePaperlessCommunicationsPage;

    public UpdatePaperlessCommunicationsApiSteps(TestContext testContext, UpdatePaperlessCommunicationsPage updatePaperlessCommunicationsPage) {
        this.testContext = testContext;
        this.updatePaperlessCommunicationsPage = updatePaperlessCommunicationsPage;
        testContext.setGetAccountInfoApiPage(updatePaperlessCommunicationsPage);
        TestContextHolder.set(testContext);
    }

    @When("a request is made to UpdatePaperlessCommunications Api for {string}")
    public void a_request_is_made_to_the_UpdatePaperlessCommunications_Api_with_invalid_values(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        updatePaperlessCommunicationsPage.validateResponseForNegativeTestConditions(update_paperless_communications, UpdatePaperlessCommunicationsLabel.valueOf(testCondition));
    }

}