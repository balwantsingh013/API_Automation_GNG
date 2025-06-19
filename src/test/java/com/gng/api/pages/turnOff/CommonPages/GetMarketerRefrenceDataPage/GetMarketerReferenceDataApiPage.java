package com.gng.api.pages.turnOff.CommonPages.GetMarketerRefrenceDataPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CommonPojo.GetMerketerReferenceData.GetMarketerReferenceDataRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.Common.GetMarketerReferenceData.GetMarketerReferenceDataLabel;
import com.gng.api.util.FakerDataGenerator;

public class GetMarketerReferenceDataApiPage extends BasePage {

    private final GetMarketerReferenceDataHelper helper;

    public GetMarketerReferenceDataApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetMarketerReferenceDataHelper(testContext);
    }

    public void requestToGenerateMarketerReferenceData(GetMarketerReferenceDataLabel apiLabel){
        GetMarketerReferenceDataRequest payload=helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
    }
}
