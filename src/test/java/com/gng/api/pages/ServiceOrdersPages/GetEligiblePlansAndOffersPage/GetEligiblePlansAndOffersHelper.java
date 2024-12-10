package com.gng.api.pages.ServiceOrdersPages.GetEligiblePlansAndOffersPage;


import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import com.gng.api.util.FakerDataGenerator;
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

    public void setRequestIDBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel requestID) {
        switch (requestID) {
            case EMPTY_REQUEST_ID:
                payload.setRequestID("");
                break;
            case DUPLICATE_REQUEST_ID:
                payload.setRequestID("123");
                break;
            case LONG_REQUEST_ID:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(200));
                break;
            case UNICODE_CHARS_REQUEST_ID:
                payload.setRequestID(FakerDataGenerator.generateUnicode());
                break;
            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void setLoginIDBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel loginID) {
        switch (loginID) {
            case MIN_LENGTH_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(1));
                break;
            case SPECIAL_CHARS_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("");
                break;
            case UPPERCASE_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateUpperCaseString(9));
                break;
            case ALPHANUMERIC_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(8));
                break;
            case MAX_LENGTH_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(35));
                break;
            default:
                payload.setLoginID(FakerDataGenerator.generateLowerCaseString(10));
        }
    }

    public void setTransactionTypeBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel transactionType) {
        switch (transactionType) {
            case EMPTY_TRANSACTION_TYPE:
                payload.setTransactionType("");
                break;
            case NUMERIC_TRANSACTION_TYPE:
                payload.setTransactionType(FakerDataGenerator.generateDigits(10));
                break;
            case UPPERCASE_TRANSACTION_TYPE:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(36));
                break;
            case LOWERCASE_TRANSACTION_TYPE:
                payload.setTransactionType(FakerDataGenerator.generateLowerCaseString(4));
                break;
            case WHITESPACE_CONTAINS_TRANSACTION_TYPE:
                payload.setTransactionType("DF HJ");
                break;
            case ALPHANUMERIC_TRANSACTION_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(FakerDataGenerator.generateAlphanumeric(2));
                break;
            default:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
        }
    }

    public void setCustomerTypeBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel customerTYPE) {
        switch (customerTYPE) {
            case EMPTY_CUSTOMER_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerType("");
                break;
            case MIN_LENGTH_CUSTOMER_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerType(FakerDataGenerator.getRandomString(3));
                break;
            case NUMERIC_CUSTOMER_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerCode(FakerDataGenerator.getRandomNumericString(4));
                break;
            case SPL_CHAR_CUSTOMER_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerType(FakerDataGenerator.generateAlphanumericWithSpecialChars(3));
                break;
            case MAX_LENGTH_CUSTOMER_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerType(FakerDataGenerator.getRandomString(5));
                break;

            default:
                payload.setCustomerType(FakerDataGenerator.getRandomString(2));
        }
    }

    public void setEnrollmentSourcesBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel enrollmentSources) {
        switch (enrollmentSources) {
            case SPL_CHAR_ENROLLMENT_SOURCES:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentSource(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_ENROLLMENT_SOURCES:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentSource("");
                break;
            case NUMERIC_ENROLLMENT_SOURCES:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentSource(FakerDataGenerator.getRandomNumericString(5));
                break;
            case MAX_LENGTH_ENROLLMENT_SOURCES:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentSource(FakerDataGenerator.getRandomNumericString(38));
                break;

            default:
                payload.setEnrollmentSource(FakerDataGenerator.generateUpperCaseString(35));
        }

    }

    public void setCustomerLastNameBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel customerLastName) {
        switch (customerLastName) {
            case SPL_CHAR_CUSTOMER_LAST_NAME:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerLastName(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_CUSTOMER_LAST_NAME:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerLastName("");
                break;
            case LOWERCASE_CUSTOMER_LAST_NAME:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerLastName(FakerDataGenerator.generateLowerCaseString(5));
                break;
            case MAX_LENGTH_CUSTOMER_LAST_NAME:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerLastName(FakerDataGenerator.getRandomString(10));
                break;
            case NUMERIC_CUSTOMER_LAST_NAME:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerLastName(FakerDataGenerator.getRandomNumericString(5));
                break;

            default:
                payload.setCustomerLastName(FakerDataGenerator.generateUpperCaseString(5));
        }

    }
}
