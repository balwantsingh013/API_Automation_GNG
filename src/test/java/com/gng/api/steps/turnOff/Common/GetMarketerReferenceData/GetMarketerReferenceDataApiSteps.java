package com.gng.api.steps.turnOff.Common.GetMarketerReferenceData;

import com.gng.api.pages.turnOff.CommonPages.GetMarketerRefrenceDataPage.GetMarketerReferenceDataApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;

import static com.gng.api.steps.turnOff.Common.GetMarketerReferenceData.GetMarketerReferenceDataLabel.get_marketer_reference_data;

public class GetMarketerReferenceDataApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final GetMarketerReferenceDataApiPage marketerReferenceDataApiPage;


    public GetMarketerReferenceDataApiSteps(TestContext testContext, GetMarketerReferenceDataApiPage marketerReferenceDataApiPage) {
        this.testContext = testContext;
        this.marketerReferenceDataApiPage = marketerReferenceDataApiPage;
        testContext.setGetMarketerReferenceDataApiPage(marketerReferenceDataApiPage);

        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @Given("a request is made to get Marketer Reference Data")
    public void requestToGetMarketerReferenceData() {
        marketerReferenceDataApiPage.requestToGenerateMarketerReferenceData(get_marketer_reference_data);
    }


}
