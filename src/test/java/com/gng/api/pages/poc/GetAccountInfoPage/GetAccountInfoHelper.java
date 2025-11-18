package com.gng.api.pages.poc.GetAccountInfoPage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.getAccountInfo.GetAccountInfoResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.AccountsPojo.getAccountInfo.GetAccountInfoRequest;
import com.gng.api.steps.poc.GetAccountInfo.GetAccountInfoApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import java.util.List;
import java.util.Map;

import static com.gng.api.constants.DBConstant.UCRACCT_CUST_CODE;
import static com.gng.api.constants.DBConstant.UCRACCT_PREM_CODE;
import static com.gng.api.util.LogUtil.logInfo;

@Slf4j
public class GetAccountInfoHelper {
    private final TestContext testContext;

    public GetAccountInfoHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetAccountInfoRequest preparePayload(GetAccountInfoApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetAccountInfoApiLabel.get_account_info)
                ? GetAccountInfoApiLabel.get_account_info.toString()
                : null;
        return BasePage.deserializeJsonToPojo(jsonFileName, GetAccountInfoRequest.class);
    }

    public void verifyAccountInformationWithDatabase() {
        GetAccountInfoResponse response = testContext.getGetAccountInfoResponse();
        String status = response.getData().getAccountStatus();
        String rateSchedule = response.getData().getRateSchedule();
        List<Map<String, Object>> accountInformationDB = ApplicationContext.get().getDbAction()
                .getAccountInformationResponseHappy(response.getData().getCustomerCode(), status, rateSchedule);
        if (accountInformationDB.isEmpty()) {
            throw new AssertionError("Validation query returned 0 rows for customer " + response.getData().getCustomerCode());
        }

        Map<String, Object> row = accountInformationDB.getFirst();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.convertValue(response.getData(), new TypeReference<Map<String, Object>>() {});

        logInfo("Database Values: {}"+ row);
        logInfo("Response Values: {}"+ responseMap);

        SoftAssertions softAssert = new SoftAssertions();

        for (String key : row.keySet()) {
            Object dbValue = row.get(key);
            Object responseValue = responseMap.get(key);

            String dbComparable = toComparableString(dbValue);
            String respComparable = toComparableString(responseValue);

            log.warn("Field: {} | DB: {} | API: {}", key, dbValue, responseValue);

            softAssert.assertThat(respComparable)
                    .as("Field: " + key)
                    .isEqualTo(dbComparable);
        }
        softAssert.assertAll();
    }

    private static String toComparableString(Object value) {
        switch (value) {
            case null -> {
                return "";
            }
            case CharSequence cs -> {
                String s = cs.toString().trim();
                return s.isEmpty() ? "" : s;
            }
            case java.util.Date d -> {
                return new java.text.SimpleDateFormat("yyyyMMdd").format(d);
            }
            case java.time.LocalDate ld -> {
                return ld.toString();
            }
            case java.time.LocalDateTime ldt -> {
                return ldt.toLocalDate().toString();
            }
            case java.math.BigDecimal bd -> {
                return bd.stripTrailingZeros().toPlainString();
            }
            case java.math.BigInteger bi -> {
                return bi.toString();
            }
            case Number n -> {
                try {
                    java.math.BigDecimal bd = new java.math.BigDecimal(n.toString());
                    return bd.stripTrailingZeros().toPlainString();
                } catch (NumberFormatException e) {
                    return String.valueOf(n);
                }
            }
            default -> {
            }
        }
        return String.valueOf(value);
    }

    public void setParametersBasedOnTypeNegative(GetAccountInfoRequest payload, GetAccountInfoApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(12));

        switch (testCondition) {
            case MISSING_REQUEST_ID_NEGATIVE_TC15 -> payload.setRequestID(null);
            case DUPLICATE_REQUEST_ID_NEGATIVE_TC16 ->  payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
            case MISSING_CUSTOMER_CODE_NEGATIVE_TC17 ->  payload.setCustomerCode(null);
            case INVALID_CUSTOMER_CODE_LENGTH_NEGATIVE_TC19 -> payload.setCustomerCode(FakerDataGenerator.generateDigits(11));
            case MISSING_PREMISES_CODE_NEGATIVE_TC18 -> payload.setPremisesCode(null);
            case INVALID_PREMISES_CODE_LENGTH_NEGATIVE_TC20 -> payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
            case INVALID_ACCOUNT_COMBINATION_NEGATIVE_TC21 -> {
                List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerOnPaymentArrangementWithBalanceDetails();
                payload.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
            }
            default -> log.warn("No negative test case implemented for {}", testCondition);
        }
    }

    public void setParametersBasedOnTypePositive(GetAccountInfoRequest payload, GetAccountInfoApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(12));

        switch (testCondition) {

            case ACTIVE_WITH_PA_PAST_DUE_POSITIVE_TC22 -> {
                List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerOnPaymentArrangementWithBalanceDetails();
                payload.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
                payload.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
            }

            case INACTIVE_WITH_RECURRING_CC_POSITIVE_TC23 -> {
                Map<String, Object> row = ApplicationContext.get().getDbAction().getCustomerInformationInactiveABDAccount();
                payload.setCustomerCode(row.get(UCRACCT_CUST_CODE).toString());
                payload.setPremisesCode(row.get(UCRACCT_PREM_CODE).toString());
            }

            case FINAL_WITH_ABD_POSITIVE_TC24 -> {
                List<Map<String, Object>> rows = ApplicationContext.get().getDbAction().getCustomerInformationByStatusAndPlanTypeWithMiddleName
                        (GlobalEnums.AccountStatus.FINAL_ACCOUNT.getValue(), GlobalEnums.PlanCode.MVS.getValue(), true, true);

                payload.setCustomerCode(rows.getFirst().get(UCRACCT_CUST_CODE).toString());
                payload.setPremisesCode(rows.getFirst().get(UCRACCT_PREM_CODE).toString());
            }

            case ACTIVE_DEFAULTED_PA_WITH_BUDGET_POSITIVE_TC25 -> {
                List<Map<String, Object>> rows = ApplicationContext.get().getDbAction().getCustomerInformationDefaultedPaBudget();

                payload.setCustomerCode(rows.getFirst().get(UCRACCT_CUST_CODE).toString());
                payload.setPremisesCode(rows.getFirst().get(UCRACCT_PREM_CODE).toString());
            }

            case INACTIVE_BAD_DEBT_SONP_DISCLETTERS_POSITIVE_TC26 -> {
                List<Map<String, Object>> rows = ApplicationContext.get().getDbAction().getCustomerInformationByStatusWithBadDebt
                        (GlobalEnums.AccountStatus.INACTIVE.getValue(), "", GlobalEnums.PlanTypeIndicator.VARIABLE_SELECT.getValue(), GlobalEnums.PlanCode.MVS.getValue());

                payload.setCustomerCode(rows.getFirst().get(UCRACCT_CUST_CODE).toString());
                payload.setPremisesCode(rows.getFirst().get(UCRACCT_PREM_CODE).toString());
            }

            case NEW_NO_BILLS_YET_POSITIVE_TC27 -> {
                List<Map<String, Object>> rows = ApplicationContext.get().getDbAction().getCustomerInformationByStatusNoBills
                        (GlobalEnums.AccountStatus.NOT_ACTIVE_YET.getValue(), "", GlobalEnums.PlanTypeIndicator.GUARANTEED_BILL.getValue(), GlobalEnums.PlanCode.RGB.getValue());

                payload.setCustomerCode(rows.getFirst().get(UCRACCT_CUST_CODE).toString());
                payload.setPremisesCode(rows.getFirst().get(UCRACCT_PREM_CODE).toString());
            }

            default -> log.warn("No positive mutation implemented for {}", testCondition);
        }
    }

}
