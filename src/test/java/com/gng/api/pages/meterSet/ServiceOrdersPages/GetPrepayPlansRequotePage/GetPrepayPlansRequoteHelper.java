package com.gng.api.pages.meterSet.ServiceOrdersPages.GetPrepayPlansRequotePage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.Account;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.Plans;
import com.gng.api.pojo.ServiceOrdersPojo.GetPrepayPlansRequote.GetPrepayPlansRequoteRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.meterSet.ServiceOrdersSteps.GetPrepayPlansRequote.GetPrepayPlansRequoteApiLabel;
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

    public void setInvalidParamsByCondition(GetPrepayPlansRequoteRequest payload, GetPrepayPlansRequoteApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(GlobalEnums.TransactionType.METER_SET.getValue());
        payload.setTransactionID(FakerDataGenerator.getRandomNumericString(4));
        switch (testCondition) {
            case GP_MS_MISSING_TRANSACTION_TYPE_TC_57 ->
                payload.setTransactionType("");

            case GP_MS_MISSING_TRANSACTION_TYPE_TC_57_2 ->
                    payload.setTransactionType(null);

            case GP_MS_TRANSACTION_TYPE_MAX_LENGTH_TC_58 ->
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(5));

            case GP_MS_TRANSACTION_TYPE_INVALID_TC_59 ->
                payload.setTransactionType(GlobalEnums.InvalidValues.INVALID_TRANSACTION_TYPE.getValue());

            default -> {}
        }
    }

    public void setRequestParams(GetPrepayPlansRequoteRequest payload) {

        String transactionId = "";
        if (testContext.getSearchAccountsResponse() == null || testContext.getSearchAccountsResponse().getData() == null || testContext.getSearchAccountsResponse().getData().getAccounts() == null) {
            throw new IllegalArgumentException("SearchAccountsResponse is null or incomplete");
        }
        for (Account account : testContext.getSearchAccountsResponse().getData().getAccounts()) {
            String enrollmentStatus = account.getEnrollmentState();
            if (account.isPrepayPlanIndicator() ||
                    (!account.getPrepayCustomerPayByDate().trim().isEmpty()
                            && Objects.equals(account.getCustomerCode(), testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode()))) {

                System.out.println("Prepay account found: TransactionID = "
                        + account.getTransactionID() + ", Status = " + enrollmentStatus);

                transactionId = String.valueOf(account.getTransactionID());
                break;
            }
        }
        payload.setTransactionID(transactionId);
        payload.setTransactionType(GlobalEnums.TransactionType.TURN_ON.getValue());
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

    public void verifyResidentialPrepayPlansReceivedAgainstDatabase(GlobalEnums.PlanCode requestPlanCode) {
        List<Map<String, Object>> eligiblePlansList = ApplicationContext.get().getDbAction().getValidationPrepayPlans(testContext.getGetPrepayPlansRequoteResponse().getData().getTransactionID());
        GetEligiblePlansAndOffersResponse response = testContext.getGetEligiblePlansAndOffersResponse();

        for (Map<String, Object> eligiblePlan : eligiblePlansList) {
            comparePlanFields(eligiblePlan, response.getData().getPlans().stream()
                    .filter(plan -> plan.getPrepayCustomerPayByDate() != null)
                    .collect(Collectors.toList()), requestPlanCode);
        }
    }

    public static void comparePlanFields(Map<String, Object> eligiblePlan, List<Plans> plans, GlobalEnums.PlanCode validationPlanCode)
    {
        String dbPlanCode = String.valueOf(eligiblePlan.get("planCode")).trim();
        String dbPlanDescription = String.valueOf(eligiblePlan.get("planDescription")).trim();
        String validationCode = validationPlanCode == null ? null : validationPlanCode.name().trim();

        boolean validationFound = false;
        boolean dbMatchFound = false;

        for (Plans plan : plans) {
            String code = plan.getPlanCode() == null ? null : plan.getPlanCode().trim();

            if (validationCode != null && validationCode.equals(code)) {
                validationFound = true;
            }

            if (dbPlanCode.equals(code)) {
                dbMatchFound = true;
                Assert.assertEquals(code, dbPlanCode, "Plan code mismatch");
                Assert.assertEquals(normalize(plan.getPlanDescription()), dbPlanDescription,
                        "Plan description mismatch for planCode: " + dbPlanCode);
            }
        }

        Assert.assertTrue(dbMatchFound, "No matching planCode found in API response for: " + dbPlanCode);
        if (validationCode != null) {
            Assert.assertTrue(validationFound, "validationPlanCode not present in API results: " + validationCode);
        }
    }

    private static String normalize(String s) {
        return s == null ? null : s.trim();
    }

}
