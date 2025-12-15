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

    public void verifyPositivePrepayPlanRequote(GetPrepayPlansRequoteRequest payload, GetPrepayPlansRequoteApiLabel testCondition) {
        payload.setTransactionType(GlobalEnums.TransactionType.MKSW.getValue());
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(12));

//        switch (testCondition) {
//            case SE_MRK_SW_PAYMENT_COMPLETE_PRP_PREVIOUS_TC_45, SE_MRK_SW_PAYMENT_COMPLETE_PGB_NEW_TC_46,
//                 SE_MRK_SW_PREPAY_REQUIRED_PR_NEW_TC_49, SE_MRK_SW_PREPAY_REQUIRED_PR_PREVIOUS_TC_50 -> payload.setTransactionID(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID());
//        }
    }

    public void verifyPositiveExternalGetEligiblePrepayPlanRequote(GetPrepayPlansRequoteRequest payload, GetPrepayPlansRequoteApiLabel testCondition) {
        payload.setTransactionType(GlobalEnums.TransactionType.MKSW.getValue());
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(12));
         payload.setTransactionID(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID());
    }

    public void expirePrepayQuoteIfOpenDateInFuture(SearchAccountsResponse response, String customerCode) {
        if (response == null || response.getData() == null || response.getData().getAccounts() == null) {
            throw new IllegalArgumentException("SearchAccountsResponse is null or incomplete");
        }

        Optional<Account> accountOpt = response.getData().getAccounts().stream()
                .filter(account ->
                        account.isPrepayPlanIndicator() ||
                                (account.getPrepayCustomerPayByDate() != null &&
                                        !account.getPrepayCustomerPayByDate().trim().isEmpty() &&
                                        Objects.equals(account.getCustomerCode(), customerCode))
                )
                .findFirst();

        if (accountOpt.isEmpty()) {
            throw new IllegalStateException("No account with prepayPlanIndicator=true found");
        }

        Account account = accountOpt.get();
        Map<String, Object> quote = ApplicationContext.get().getDbAction().getPrepayQuote(account.getCustomerCode());

        if (quote == null || !quote.containsKey("UABOPEN_DUE_DATE")) {
            System.err.println("Quote not found or missing UABOPEN_DUE_DATE");
            return;
        }

        Object dueDateObj = quote.get("UABOPEN_DUE_DATE");
        if (!(dueDateObj instanceof Date dueDate)) {
            System.err.println("UABOPEN_DUE_DATE is not a valid Date type: " + dueDateObj);
            return;
        }

        LocalDate due = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (!due.isAfter(LocalDate.now())) {
            System.out.println("Quote not expired — due date is not in the future: " + due);
            return;
        }

        int result = ApplicationContext.get().getDbAction().expirePrepayQuote(account.getCustomerCode());
        System.out.println("Quote expired — due date was in the future: " + due + " (result=" + result + ")");
        int deleteResult = ApplicationContext.get().getDbAction().deleteUrblerxByCustomerCode(account.getCustomerCode());
        System.out.println("URBLERX record deleted:  (result=" + deleteResult + ")");
    }

}
