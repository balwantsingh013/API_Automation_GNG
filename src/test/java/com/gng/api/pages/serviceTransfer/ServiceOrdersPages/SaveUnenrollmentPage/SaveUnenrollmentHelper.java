package com.gng.api.pages.serviceTransfer.ServiceOrdersPages.SaveUnenrollmentPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;

import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel;

import com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.SaveUnenrollment.TurnOffReason;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import static com.gng.api.constants.GlobalEnums.*;
import static com.gng.api.constants.GlobalEnums.AddressType.*;
import static com.gng.api.constants.GlobalEnums.ForwardingAddressType.*;
import static com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel.*;



@Slf4j
public class SaveUnenrollmentHelper {

    private final TestContext testContext;

    public SaveUnenrollmentHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    SaveUnenrollmentRequest preparePayload(SaveUnenrollmentApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(SaveUnenrollmentApiLabel.save_unenrollment)
                ? SaveUnenrollmentApiLabel.save_unenrollment.toString()
                : SaveUnenrollmentApiLabel.save_unenrollment_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, SaveUnenrollmentRequest.class);
    }


    public void setRequestAndLoginIDBasedOnType(SaveUnenrollmentRequest payload,
                                                SaveUnenrollmentApiLabel testCondition) {
        switch (testCondition) {
            // Request ID
            case REQUEST_ID_EMPTY_NEGATIVE_TC97 -> payload.setRequestID("");
            case REQUEST_ID_MAX_LENGTH_NEGATIVE_TC98 -> payload.setRequestID(FakerDataGenerator.generateAlphanumeric(200));
            case REQUEST_ID_DUPLICATE_NEGATIVE_TC99 -> payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());

            // Login ID
            case LOGIN_ID_EMPTY_NEGATIVE_TC100 -> payload.setLoginID("");
            case LOGIN_ID_MAX_LENGTH_NEGATIVE_TC101 -> payload.setLoginID(FakerDataGenerator.getRandomString(50));
            case LOGIN_ID_INVALID_FORMAT_NEGATIVE_TC102 -> {
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(6));
            }
            case LOGIN_ID_NOT_EXISTS_NEGATIVE_TC103 -> {
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateString(8));
            }

            default -> { }
        }
    }


    public void setParametersBasedOnTypeForServiceTransfer(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TransactionType.TRANSFER.getValue());
        setForwardingAddressDetailsBasedOnType(payload, CURRENT_ADDRESS, testCondition);
        payload.setTurnOffReason("");
        payload.setEtcExists(null);

        switch (testCondition) {

            case REQUEST_ID_EMPTY_NEGATIVE_TC97 -> payload.setRequestID("");
            case REQUEST_ID_MAX_LENGTH_NEGATIVE_TC98 -> payload.setRequestID(FakerDataGenerator.generateAlphanumeric(200));
            case REQUEST_ID_DUPLICATE_NEGATIVE_TC99 -> payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());

            // Login ID
            case LOGIN_ID_EMPTY_NEGATIVE_TC100 -> payload.setLoginID("");
            case LOGIN_ID_MAX_LENGTH_NEGATIVE_TC101 -> payload.setLoginID(FakerDataGenerator.getRandomString(50));
            case LOGIN_ID_INVALID_FORMAT_NEGATIVE_TC102 -> {
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(6));
            }
            case LOGIN_ID_NOT_EXISTS_NEGATIVE_TC103 -> {
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateString(8));
            }

            /* Transaction Type */
            case TRANSACTION_TYPE_EMPTY_NEGATIVE_TC104 -> payload.setTransactionType("");
            case TRANSACTION_TYPE_MAX_LENGTH_NEGATIVE_TC105 -> payload.setTransactionType(FakerDataGenerator.getRandomString(10));
            case TRANSACTION_TYPE_INVALID_NEGATIVE_TC106 -> payload.setTransactionType(GlobalEnums.TransactionType.INVALID.getValue());

            /* Customer Code */
            case CUSTOMER_CODE_EMPTY_NEGATIVE_TC107 -> payload.setCustomerCode(null);
            case CUSTOMER_CODE_MAX_LENGTH_NEGATIVE_TC108 -> payload.setCustomerCode(FakerDataGenerator.getRandomNumericString(20));
            case CUSTOMER_CODE_NON_INT_NEGATIVE_TC109 -> payload.setCustomerCode(FakerDataGenerator.generateString(5));
            case CUSTOMER_CODE_NOT_EXISTS_NEGATIVE_TC110 -> payload.setCustomerCode(0);

            /* Premises Code */
            case PREMISES_CODE_EMPTY_NEGATIVE_TC111 -> payload.setPremisesCode("");
            case PREMISES_CODE_MAX_LENGTH_NEGATIVE_TC112 -> payload.setPremisesCode(FakerDataGenerator.getRandomNumericString(15));
            case PREMISES_CODE_NOT_EXISTS_NEGATIVE_TC113 -> payload.setPremisesCode("0");

            /* Invalid Account Combo (uses context values then wrong service number) */
            case ACCOUNT_COMBINATION_INVALID_NEGATIVE_TC114 -> {
                payload.setCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
                payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());
                payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(5));
            }

            /* AGLC Account */
            case AGLC_ACCOUNT_EMPTY_NEGATIVE_TC115 -> payload.setAglcAccountNumber("");
            case AGLC_ACCOUNT_MAX_LENGTH_NEGATIVE_TC116 -> payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(30));

            /* Forwarding Address (selector) */
            case FWD_ADDRESS_EMPTY_NEGATIVE_TC117 -> payload.setForwardingAddressIs("");
            case FWD_ADDRESS_MAX_LENGTH_NEGATIVE_TC118 -> payload.setForwardingAddressIs(FakerDataGenerator.getRandomString(4));
            case FWD_ADDRESS_INVALID_VALUE_NEGATIVE_TC119 -> payload.setForwardingAddressIs(GlobalEnums.ForwardingAddressType.INVALID.getValue());

            /* Forwarding Address Type */
            case FWD_ADDRESS_TYPE_INVALID_NEGATIVE_TC120 -> {
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, ADDRESS_TYPE_STREET);
                payload.setForwardingAddressType("");
            }
            case FWD_ADDRESS_TYPE_EMPTY_NEGATIVE_TC121 -> {
                payload.setForwardingAddressIs(NEW_ADDRESS.getValue());
                payload.setForwardingAddressType("");
            }
            case FWD_ADDRESS_TYPE_INVALID_VALUE_NEGATIVE_TC122 -> {
                payload.setForwardingAddressIs(NEW_ADDRESS.getValue());
                payload.setForwardingAddressType(FakerDataGenerator.getRandomString(5));
            }

            /* Address Fields */
            case FWD_ADD_STR_NUM_MAX_LENGTH_NEGATIVE_TC123 -> payload.setForwardingAddressStreetNumber(FakerDataGenerator.getRandomString(20));
            case FWD_ADD_STR_PRE_DIR_MAX_LENGTH_NEGATIVE_TC124 -> payload.setForwardingAddressStreetPreDirection(FakerDataGenerator.getRandomString(5));

            case FWD_ADD_STR_NAME_EMPTY_NEGATIVE_TC125 -> {
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, ADDRESS_TYPE_STREET);
                payload.setForwardingAddressStreetName("");
            }
            case FWD_ADD_STR_NAME_MAX_LENGTH_NEGATIVE_TC126 -> payload.setForwardingAddressStreetName(FakerDataGenerator.getRandomString(50));
            case FWD_ADD_STR_SFX_MAX_LENGTH_NEGATIVE_TC127 -> payload.setForwardingAddressStreetSuffix(FakerDataGenerator.getRandomString(15));
            case FWD_ADD_STR_POST_DIR_MAX_LENGTH_NEGATIVE_TC128 -> payload.setForwardingAddressStreetPostDirection(FakerDataGenerator.getRandomString(5));


            default -> { }
        }
    }

    public void setMarketerReferenceData(SaveUnenrollmentRequest payload, long marketerReferenceData) {
        payload.setMarketerReferenceData(marketerReferenceData);
    }

    public void setForwardingAddressDetailsBasedOnType(SaveUnenrollmentRequest payload,
                                                       GlobalEnums.ForwardingAddressType forwardingAddressType,
                                                       SaveUnenrollmentApiLabel type) {
        if (forwardingAddressType.equals(NEW_ADDRESS)) {
            payload.setForwardingAddressIs(NEW_ADDRESS.getValue());

            Map<String, Object> addr = ApplicationContext.get().getDbAction().getAddressDetails();
            payload.setForwardingAddressCity(addr.get("UCRADDR_CITY").toString());
            payload.setForwardingAddressStateCode(addr.get("UCRADDR_STAT_CODE").toString());
            payload.setForwardingAddressZipCode(addr.get("UCRADDR_ZIP").toString());

            switch (type) {
                case ADDRESS_TYPE_STREET -> {
                    payload.setForwardingAddressType(STREET.getValue());
                    payload.setForwardingAddressStreetNumber(addr.get("UCRADDR_STREET_NUMBER").toString());
                    payload.setForwardingAddressStreetName(addr.get("UCRADDR_STREET_NAME").toString());
                    payload.setForwardingAddressStreetSuffix(addr.get("UCRADDR_SSFX_CODE").toString());
                    payload.setForwardingAddressStreetPostDirection(addr.get("UCRADDR_PDIR_CODE_POST").toString());
                    payload.setForwardingAddressStreetPreDirection(addr.get("UCRADDR_PDIR_CODE_PRE").toString());
                }
                case ADDRESS_TYPE_RURAL -> {
                    payload.setForwardingAddressType(RURAL.getValue());
                    payload.setForwardingAddressRuralRoute(FakerDataGenerator.generateAlphanumeric(3));
                }
                case ADDRESS_TYPE_POBOX -> {
                    payload.setForwardingAddressType(AddressType.POBOX.getValue());
                    payload.setForwardingAddressPOBox(FakerDataGenerator.generateAlphanumeric(2));
                }
                default -> { /* no-op */ }
            }
        }
    }


    public void setCustomerCodePremCodeAGLCServiceNo(SaveUnenrollmentRequest payload,
                                                     String pricePlan,
                                                     String sclsCode,
                                                     SaveUnenrollmentApiLabel forCase) {

        Map<String, Object> row = ApplicationContext.get()
                .getDbAction()
                .custCodeParamCodeAGLCAccNoServNoTC207(pricePlan, sclsCode);

        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setPremisesCode(row.get("GTBTRNH_PREM_CODE").toString());
        payload.setCustomerCode(row.get("GTBTRNH_CUST_CODE").toString());
        payload.setAglcAccountNumber(row.get("GTBTRNH_AGLC_ACCT_NBR").toString());
        payload.setAglcServiceOrderNumber(row.get("GTRRNDN_SERV_ORD_NUM").toString());
    }
}
