package com.gng.api.pages.marketerSwitch.ServiceOrdersPages.GetPrepayPlanRequotePage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.Account;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.Plans;
import com.gng.api.pojo.ServiceOrdersPojo.GetPrepayPlansRequote.GetPrepayPlansRequoteRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.GetPrepayPlanRequote.GetPrepayPlansRequoteApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public class GetPrepayPlansRequoteHelper {
    private final TestContext testContext;
    private String transactionId;
    public static final String USERNAME = "autotester";

    public GetPrepayPlansRequoteHelper(TestContext testContext) {
        this.testContext = testContext;

    }

    GetPrepayPlansRequoteRequest preparePayload(GetPrepayPlansRequoteApiLabel apiLabel) {
       log.info("Preparing payload for {}", apiLabel);
       String jsonFileName = apiLabel.equals(GetPrepayPlansRequoteApiLabel.get_prepay_plans_requote)
               ? GetPrepayPlansRequoteApiLabel.get_prepay_plans_requote.toString()
               : GetPrepayPlansRequoteApiLabel.get_prepay_plans_requote_mandatory.toString();
       return BasePage.deserializeJsonToPojo(jsonFileName, GetPrepayPlansRequoteRequest.class);
   }

    public void verifyNegativePrepayPlanRequote(GetPrepayPlansRequoteRequest payload, GetPrepayPlansRequoteApiLabel testCondition) {
        payload.setTransactionType(GlobalEnums.TransactionType.MKSW.getValue());
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(12));

        switch (testCondition) {
            case GPP_PR_MISSING_TRANSACTION_TYPE_TC_056 ->
                payload.setTransactionType(null);
            case GPP_PR_MAX_LENGTH_TRANSACTION_TYPE_TC_057 ->
                    payload.setTransactionType(FakerDataGenerator.generateString(5));
            case GPP_PR_INVALID_TRANSACTION_TYPE_TC_058 ->
                    payload.setTransactionType(GlobalEnums.InvalidValues.INVALID_TRANSACTION_TYPE.getValue());
        }
    }
}
