package com.gng.api.steps.ServiceOrdersSteps;

import com.gng.api.pages.AccountsApiPages.CreateAccountNotePage.CreateAccountNoteApiPage;
import com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentApiPage;
import com.gng.api.pojo.TestContext.TestContext;

public class SaveEnrollmentApiSteps {

    private final TestContext testContext;
    private final SaveEnrollmentApiPage saveEnrollmentApiPage;

    public SaveEnrollmentApiSteps(TestContext testContext, SaveEnrollmentApiPage saveEnrollmentApiPage) {
        this.testContext = testContext;
        this.saveEnrollmentApiPage = saveEnrollmentApiPage;
        testContext.setSaveEnrollmentApiPage(saveEnrollmentApiPage);
    }

}
