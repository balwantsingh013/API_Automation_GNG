package com.gng.api.steps.serviceTransfer.Common.GetMarketerReferenceData;

import com.gng.api.pages.serviceTransfer.CommonPages.GetMarketerReferenceDataPage.GetMarketerReferenceDataApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;

import static com.gng.api.steps.serviceTransfer.Common.GetMarketerReferenceData.GetMarketerReferenceDataApiLabel.get_marketer_reference_data;

public class GetMarketerReferenceDataApiSteps {

    private final TestContext testContext;
    private final GetMarketerReferenceDataApiPage getMarketerReferenceDataApiPage;


    public GetMarketerReferenceDataApiSteps(TestContext testContext,
                                            GetMarketerReferenceDataApiPage getMarketerReferenceDataApiPage) {
        this.testContext = testContext;
        this.getMarketerReferenceDataApiPage = getMarketerReferenceDataApiPage;
        testContext.setGetMarketerReferenceDataApiPage(getMarketerReferenceDataApiPage);
    }

    @When("a request is made to the GetMarketerReferenceData Api with invalid parameters for {string}")
    public void a_request_to_marketer_reference_data_with_invalid_testCondition(String testCondition){
        getMarketerReferenceDataApiPage.requestToGenerateMarketerReferenceDataWithInvalidTestCondition(get_marketer_reference_data,GetMarketerReferenceDataApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetMarketerReferenceData Api with valid parameters")
    public void requestToMarketerReferenceDataWithValidParameters(){
        getMarketerReferenceDataApiPage.requestToGenerateMarketerReferenceDataWithValidTestCondition(get_marketer_reference_data);
    }
}
