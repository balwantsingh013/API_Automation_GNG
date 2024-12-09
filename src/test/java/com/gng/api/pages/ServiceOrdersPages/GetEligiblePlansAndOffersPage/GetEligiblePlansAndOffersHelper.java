package com.gng.api.pages.ServiceOrdersPages.GetEligiblePlansAndOffersPage;


import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetEligiblePlansAndOffersHelper {


        private final TestContext testContext;

        public GetEligiblePlansAndOffersHelper(TestContext testContext) {
            this.testContext = testContext;
        }

    GetEligiblePlansAndOffersRequest preparePayload(GetEligiblePlansAndOffersApiLabel apiLabel) {
            log.info("Preparing payload for {}", apiLabel);
            String jsonFileName = apiLabel.equals(GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers)
                    ? GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers.toString()
                    : GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers_mandatory.toString();
            return BasePage.deserializeJsonToPojo(jsonFileName, GetEligiblePlansAndOffersRequest.class);
        }

    }
