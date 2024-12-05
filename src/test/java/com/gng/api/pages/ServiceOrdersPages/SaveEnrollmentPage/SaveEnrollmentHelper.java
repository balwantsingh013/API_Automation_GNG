package com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
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
            case SPECIAL_CHARS_REQUEST_ID:
                payload.setRequestID(FakerDataGenerator.generateAlphanumericWithSpecialChars(10));
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

        public void setCustomerCodeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel customerCODE) {
        switch (customerCODE) {
            case DUPLICATE_CUSTOMER_CODE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerCode(12356);
                break;
            case MAX_LENGTH_CUSTOMER_CODE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                int customerCode = FakerDataGenerator.generateNumber(1,11);
                payload.setCustomerCode(customerCode);
                break;
            case UNICODE_CHARS_CUSTOMER_CODE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerCode(FakerDataGenerator.generateNumber(0,15));
                break;
            default:
                payload.setCustomerCode(FakerDataGenerator.generateNumber(0,9));
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
                payload.setPremisesCode(FakerDataGenerator.generateString(2));                break;
            default:
                payload.setPremisesCode(FakerDataGenerator.generateString(10));
        }
    }
    public void setTransactionIDBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel transactionID) {
        switch (transactionID) {
            case MIN_LENGTH_TRANSACTION_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionID(FakerDataGenerator.generateNumber(0,0));
                break;
            case WHITESPACE_BETWEEN_TRANSACTION_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionID(FakerDataGenerator.generateNumber(4,9));
                break;
            default:
                payload.setTransactionID(FakerDataGenerator.generateNumber(0,10));
        }
    }
    public void setTransactionTypeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel transactionType) {
        switch (transactionType) {
            case EMPTY_TRANSACTION_TYPE:
                payload.setPremisesCode("");
                break;
            case NULL_TRANSACTION_TYPE:
                payload.setPremisesCode(null);
                break;
            case NUMERIC_TRANSACTION_TYPE:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(10));
                break;
            case UPPERCASE_TRANSACTION_TYPE:
                payload.setPremisesCode(FakerDataGenerator.generateUpperCaseString(36));
                break;
            case LOWERCASE_TRANSACTION_TYPE:
                payload.setPremisesCode(FakerDataGenerator.generateLowerCaseString(4));
                break;

            case ALPHANUMERIC_TRANSACTION_TYPE:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(2));                break;
            default:
                payload.setPremisesCode(FakerDataGenerator.generateUpperCaseString(4));
        }

        }
    }
