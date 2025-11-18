package com.gng.api.pages.meterSet.ServiceOrdersPages.GetDefaultPlansAndOffersPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.meterSet.ServiceOrdersSteps.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetDefaultPlansAndOffersHelper {
    private final TestContext testContext;

    public GetDefaultPlansAndOffersHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetDefaultPlansAndOffersRequest preparePayload(GetDefaultPlansAndOffersApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers)
                ? GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers.toString()
                : GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetDefaultPlansAndOffersRequest.class);
    }

    public void setNegativeParameters(GetDefaultPlansAndOffersRequest payload, GetDefaultPlansAndOffersApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        switch (testCondition) {
            case GD_MS_MISSING_TRANSACTION_ID_TC_03 ->
                payload.setTransactionType(null);

            case GD_MS_INVALID_TRANSACTION_ID_LENGTH_TC_04 ->
                    payload.setTransactionType(FakerDataGenerator.getRandomString(4));

            case GD_MS_MISSING_TRANSACTION_ID_TC_05 ->
                    payload.setTransactionType(GlobalEnums.InvalidValues.INVALID_TRANSACTION_TYPE.getValue());
        }
    }

}
