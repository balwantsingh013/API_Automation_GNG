package com.gng.api.pages.turnOn.ServiceOrdersPages.SaveEnrollmentPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SaveEnrollmentHelper {

    private final TestContext testContext;

    public SaveEnrollmentHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    SaveEnrollmentRequest preparePayload(SaveEnrollmentApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(SaveEnrollmentApiLabel.save_enrollment)
                ? SaveEnrollmentApiLabel.save_enrollment.toString()
                : SaveEnrollmentApiLabel.save_enrollment_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, SaveEnrollmentRequest.class);
    }

    public void setRequestIDBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel requestID) {
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

            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void setCustomerCodeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel customerCODE) {
        switch (customerCODE) {
            case DUPLICATE_CUSTOMER_CODE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerCode(12356);
                break;
            case MAX_LENGTH_CUSTOMER_CODE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                int customerCode = FakerDataGenerator.generateNumber(1, 11);
                payload.setCustomerCode(customerCode);
                break;
            case UNICODE_CHARS_CUSTOMER_CODE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerCode(FakerDataGenerator.generateNumber(0, 15));
                break;
            default:
                payload.setCustomerCode(FakerDataGenerator.generateNumber(0, 9));
        }
    }

    public void setPremisesCodeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel premisesCode) {
        switch (premisesCode) {
            case EMPTY_PREMISES_CODE:
                payload.setPremisesCode("");
                break;
            case DUPLICATE_PREMISES_CODE:
                payload.setPremisesCode("25 CENTS FOR 12 MONTH");
                break;
            case SPECIAL_CHARS_PREMISES_CODE:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(10));
                break;
            case MAX_LENGTH_PREMISES_CODE:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(36));
                break;
            case MIN_LENGTH_PREMISES_CODE:
                payload.setPremisesCode(FakerDataGenerator.generateString(2));
                break;
            default:
                payload.setPremisesCode(FakerDataGenerator.generateString(10));
        }
    }

    public void setTransactionIDBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel transactionID) {
        switch (transactionID) {
            case MIN_LENGTH_TRANSACTION_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionID(FakerDataGenerator.generateNumber(0, 0));
                break;
            case WHITESPACE_BETWEEN_TRANSACTION_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionID(FakerDataGenerator.generateNumber(4, 9));
                break;
            default:
                payload.setTransactionID(FakerDataGenerator.generateNumber(0, 10));
        }
    }

    public void setTransactionTypeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel transactionType) {
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

            case ALPHANUMERIC_TRANSACTION_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(FakerDataGenerator.generateAlphanumeric(2));
                break;
            default:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
        }


    }

    public void setPlanCodeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel planCode) {
        switch (planCode) {
            case NUMERIC_PLAN_CODE:
                payload.setPlanCode(FakerDataGenerator.generateDigits(7));
                break;
            case SPECIAL_CHARS_PLAN_CODE:
                payload.setPlanCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_PLAN_CODE:
                payload.setPlanCode("");
                break;
            case UPPERCASE_PLAN_CODE:
                payload.setPlanCode(FakerDataGenerator.generateUpperCaseString(4));
                break;
            case ALPHANUMERIC_PLAN_CODE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setPlanCode(FakerDataGenerator.generateAlphanumeric(2));
                break;
            default:
                payload.setPlanCode(FakerDataGenerator.generateUpperCaseString(3));
        }
    }

    public void setLoginIDBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel loginID) {
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
                payload.setLoginID(FakerDataGenerator.generateUpperCaseString(4));
                break;
            case ALPHANUMERIC_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(2));
                break;
            case MAX_LENGTH_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(9));
                break;
            default:
                payload.setLoginID(FakerDataGenerator.generateLowerCaseString(5));
        }
    }

    public void setEnrollmentStatusBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel enrollmentStatus) {
        switch (enrollmentStatus) {
            case MIN_LENGTH_ENROLLMENT_STATUS:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus(FakerDataGenerator.getRandomNumericString(1));
                break;
            case SPECIAL_CHARS_ENROLLMENT_STATUS:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_ENROLLMENT_STATUS:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus("");
                break;
            case LOWERCASE_ENROLLMENT_STATUS:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus(FakerDataGenerator.generateUpperCaseString(4));
                break;
            case MAX_LENGTH_ENROLLMENT_STATUS:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus(FakerDataGenerator.getRandomNumericString(3));
                break;
            case WHITESPACE_CONTAINS_ENROLLMENT_STATUS:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus("S I");
                break;
            default:
                payload.setEnrollmentStatus(FakerDataGenerator.generateUpperCaseString(2));
        }
    }

    public void setBillingPlanBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel billingPlan) {
        switch (billingPlan) {
            case MAX_LENGTH_BILLING_PLAN:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setBillingPlan(FakerDataGenerator.getRandomNumericString(4));
                break;
            case SPECIAL_CHARS_BILLING_PLAN:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setBillingPlan(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_BILLING_PLAN:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setBillingPlan("");
                break;
            case LOWERCASE_BILLING_PLAN:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setBillingPlan(FakerDataGenerator.generateUpperCaseString(4));
                break;
            default:
                payload.setEnrollmentStatus(FakerDataGenerator.generateUpperCaseString(1));
        }
    }
}
