package com.gng.api.pages.turnOff.CommonPages.GetReasonsForLeavingPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CommonPojo.GetReasonsForLeaving.GetReasonsForLeavingRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.Common.GetReasonsForLeaving.GetReasonsForLeavingLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetReasonsForLeavingHelper {

    private final TestContext testContext;

    public GetReasonsForLeavingHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetReasonsForLeavingRequest preparePayload(GetReasonsForLeavingLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = GetReasonsForLeavingLabel.get_reasons_for_leaving.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetReasonsForLeavingRequest.class);
    }

    public void setEtcExistsAndRequestID(GetReasonsForLeavingRequest payload, Boolean etcExists ){
        payload.setEtcExists(etcExists);
        payload.setRequestID(FakerDataGenerator.generateString(10));
    }

}
