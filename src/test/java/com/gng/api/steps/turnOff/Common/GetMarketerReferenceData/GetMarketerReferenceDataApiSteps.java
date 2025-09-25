package com.gng.api.steps.turnOff.Common.GetMarketerReferenceData;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.turnOff.CommonPages.GetMarketerRefrenceDataPage.GetMarketerReferenceDataApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import com.gng.api.util.CommonUtil;

import static com.gng.api.steps.turnOff.Common.GetMarketerReferenceData.GetMarketerReferenceDataLabel.get_marketer_reference_data;
import static com.gng.api.util.LogUtil.logError;
import static io.restassured.RestAssured.given;

public class GetMarketerReferenceDataApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    public void storeAuthToken(Response response) {
        testContext.setResponse(response);
        testContext.setAuthToken(response.jsonPath().getString("token"));
    }

    private void handleException(Exception e) {
        logError("Error occurred: " + e.getMessage());
        String responseDetails = testContext.getResponse() != null ? testContext.getResponse().prettyPrint() : "No response received";
        throw new IllegalStateException(e.getMessage() + "\n" + responseDetails, e);
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
