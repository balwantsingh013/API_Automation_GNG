package com.gng.api.steps.turnOff.Common.GetMarketerReferenceData;

import com.gng.api.pages.turnOff.CommonPages.GetMarketerRefrenceDataPage.GetMarketerReferenceDataApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;

import static com.gng.api.steps.turnOff.Common.GetMarketerReferenceData.GetMarketerReferenceDataLabel.get_marketer_reference_data;

public class GetMarketerReferenceDataApiSteps {

    private final TestContext testContext;
    private final GetMarketerReferenceDataApiPage marketerReferenceDataApiPage;


    public GetMarketerReferenceDataApiSteps(TestContext testContext, GetMarketerReferenceDataApiPage marketerReferenceDataApiPage) {
        this.testContext = testContext;
        this.marketerReferenceDataApiPage = marketerReferenceDataApiPage;
        testContext.setSaveEnrollmentApiPage(marketerReferenceDataApiPage);
    }

    @When("a request is made to get Marketer Reference Data")
    public void requestToGetMarketerReferenceData() {
        marketerReferenceDataApiPage.requestToGenerateMarketerReferenceData(get_marketer_reference_data);

    }


}
