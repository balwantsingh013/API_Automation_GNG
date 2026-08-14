package com.gng.api.pages.practice.UpdatePaperlessCommunicationsPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsRequest;
import com.gng.api.steps.practice.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsLabel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdatePaperlessCommunicationsApiHelper {

    UpdatePaperlessCommunicationsRequest preparePayload(UpdatePaperlessCommunicationsLabel apiLabel) {
        log.info("Preparing practice payload for {}", apiLabel);
        return BasePage.deserializeJsonToPojo(apiLabel.toString(), UpdatePaperlessCommunicationsRequest.class);
    }

    public void preparePayloadForTestCondition(UpdatePaperlessCommunicationsRequest payload,
                                               UpdatePaperlessCommunicationsLabel testCondition) {
        if (testCondition == UpdatePaperlessCommunicationsLabel.TC_53__Negative__Missing_Request_ID_) {
            payload.setRequestID("");
        } else {
            log.warn("No payload adjustment for test condition: {}", testCondition);
        }
    }
}
