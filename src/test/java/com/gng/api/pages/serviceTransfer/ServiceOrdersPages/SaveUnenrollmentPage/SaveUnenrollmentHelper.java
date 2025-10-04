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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import static com.gng.api.constants.GlobalEnums.*;
import static com.gng.api.constants.GlobalEnums.AddressType.*;
import static com.gng.api.constants.GlobalEnums.ForwardingAddressType.*;

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

    public void setParametersBasedOnTypeForServiceTransfer(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel testCondition) {
        payload.setEtcExists(null);
        payload.setTurnOffReason(null);
        payload.setTurnOffSubReason(null);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerType(CustomerType.RESIDENTIAL.getValue());
        payload.setTransactionType(TransactionType.TRANSFER.getValue());
        setAddressDetailsBasedOnType(payload, testCondition);
        setCustomerCodePremCodeAGLCServiceNo(payload, testCondition);
        setReasonAndSubReason(payload, testCondition);
        setEtcExists(payload, testCondition);
        setMarketerReferenceData(payload, String.valueOf(testContext.getMarketerReferenceData()));

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
            case CUSTOMER_CODE_MAX_LENGTH_NEGATIVE_TC108 -> payload.setCustomerCode(FakerDataGenerator.getRandomNumericString(12));
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
            case FWD_ADDRESS_TYPE_INVALID_NEGATIVE_TC120 -> payload.setForwardingAddressType("");
            case FWD_ADDRESS_TYPE_EMPTY_NEGATIVE_TC121 ->  payload.setForwardingAddressType("");
            case FWD_ADDRESS_TYPE_INVALID_VALUE_NEGATIVE_TC122 ->  payload.setForwardingAddressType(FakerDataGenerator.getRandomString(5));

            /* Address Fields */
            case FWD_ADD_STR_NUM_MAX_LENGTH_NEGATIVE_TC123 -> payload.setForwardingAddressStreetNumber(FakerDataGenerator.getRandomString(20));
            case FWD_ADD_STR_PRE_DIR_MAX_LENGTH_NEGATIVE_TC124 -> payload.setForwardingAddressStreetPreDirection(FakerDataGenerator.getRandomString(5));
            case FWD_ADD_STR_NAME_EMPTY_NEGATIVE_TC125 ->  payload.setForwardingAddressStreetName("");
            case FWD_ADD_STR_NAME_MAX_LENGTH_NEGATIVE_TC126 -> payload.setForwardingAddressStreetName(FakerDataGenerator.getRandomString(50));
            case FWD_ADD_STR_SFX_MAX_LENGTH_NEGATIVE_TC127 -> payload.setForwardingAddressStreetSuffix(FakerDataGenerator.getRandomString(15));
            case FWD_ADD_STR_POST_DIR_MAX_LENGTH_NEGATIVE_TC128 -> payload.setForwardingAddressStreetPostDirection(FakerDataGenerator.getRandomString(5));
            case FWD_ADD_UNIT_TYPE_MAX_LENGTH_NEGATIVE_TC129 -> payload.setForwardingAddressUnitType(FakerDataGenerator.getRandomString(10));
            case FWD_ADD_UNIT_NUM_MAX_LENGTH_NEGATIVE_TC130 -> payload.setForwardingAddressUnitNumber(FakerDataGenerator.getRandomString(15));
            case FWD_ADD_RURAL_ROUTE_MAX_LENGTH_NEGATIVE_TC131 -> payload.setForwardingAddressRuralRoute(FakerDataGenerator.getRandomString(25));
            case FWD_ADD_PO_BOX_MAX_LENGTH_NEGATIVE_TC132 -> payload.setForwardingAddressPOBox(FakerDataGenerator.getRandomString(15));
            case FWD_ADD_LINE2_MAX_LENGTH_NEGATIVE_TC133 -> payload.setForwardingAddressLine2(FakerDataGenerator.getRandomString(50));
            case FWD_ADD_CITY_EMPTY_NEGATIVE_TC134 ->  payload.setForwardingAddressCity("");
            case FWD_ADD_CITY_MAX_LENGTH_NEGATIVE_TC135 -> payload.setForwardingAddressCity(FakerDataGenerator.getRandomString(40));
            case FWD_ADD_STATE_CODE_EMPTY_NEGATIVE_TC136 ->  payload.setForwardingAddressStateCode("");
            case FWD_ADD_STATE_CODE_MAX_LENGTH_NEGATIVE_TC137 -> payload.setForwardingAddressStateCode(FakerDataGenerator.getRandomString(5));
            case FWD_ADD_STATE_CODE_INVALID_NEGATIVE_TC138 ->  payload.setForwardingAddressStateCode(InvalidValues.INVALID_PREMISE_STATE_CODE.getValue());
            case FWD_ADD_ZIP_CODE_EMPTY_NEGATIVE_TC139 -> payload.setForwardingAddressZipCode("");
            case FWD_ADD_ZIP_CODE_MAX_LENGTH_NEGATIVE_TC140 ->  payload.setForwardingAddressZipCode(FakerDataGenerator.getRandomNumericString(12));
            case FWD_ADD_ZIP_CODE_INVALID_NEGATIVE_TC141 -> payload.setForwardingAddressZipCode(InvalidValues.INVALID_ZIP_CODE.getValue());
            case FWD_ADD_ZIP_CODE_INVALID_FORMAT_NEGATIVE_TC142 -> payload.setForwardingAddressZipCode(FakerDataGenerator.generateDigits(3) + "-" + FakerDataGenerator.generateDigits(2));

            /* Turn Off / Sub Reason  */
            case TURN_OFF_REASON_EMPTY_NEGATIVE_TC143 -> {
            }
            case TURN_OFF_REASON_MAX_LENGTH_NEGATIVE_TC144 -> payload.setTurnOffReason(FakerDataGenerator.getRandomString(200));
            case TURN_OFF_REASON_INVALID_NEGATIVE_TC145, TURN_OFF_SUB_REASON_EMPTY_NEGATIVE_TC146 -> {
            }
            case TURN_OFF_SUB_REASON_MAX_LENGTH_NEGATIVE_TC147 -> payload.setTurnOffSubReason(FakerDataGenerator.getRandomString(200));
            case TURN_OFF_SUB_REASON_MAX_LENGTH_MOVING_NEGATIVE_TC149 -> payload.setTurnOffSubReason(FakerDataGenerator.getRandomString(201));

            /* Invalid-for-service-transfer reason/subreason combos */
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC150,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC151,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC152,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC153,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC168,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC169,
                 TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC154,
                 TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC155,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC157,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC158,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC159,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC160,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC161,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC162,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC163,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC164,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC165,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC166,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC167,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC170,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC171,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC172,
                 TURN_OFF_SUB_REASON_MISSING_FOR_SERVICE_TRANSFER_NEGATIVE_TC174B
                 -> {
                //invalid turn off reason and or sub reason
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC173,
                 TURN_OFF_SUB_REASON_MISSING_FOR_SERVICE_TRANSFER_NEGATIVE_TC174A -> {
                //Only can get result by using CA for forwardingAddressIs
            }
            case  EMAIL_ADDRESS_INVALID_LENGTH_NEGATIVE_TC175A -> payload.setEmailAddress(FakerDataGenerator.generateString(105));
            case  EMAIL_ADDRESS_INVALID_FORMAT_NEGATIVE_TC175B -> payload.setEmailAddress(FakerDataGenerator.generateString(4) + "." + FakerDataGenerator.generateString(8) + "." + FakerDataGenerator.generateString(3));

            // requestedTurnOffDate
            case REQUESTED_TURNOFF_DATE_MISSING_NEGATIVE_TC176 -> payload.setRequestedTurnOffDate(""); // missing
            case REQUESTED_TURNOFF_DATE_INVALID_NEGATIVE_TC177 -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate serviceDate = LocalDate.now();
                String serviceDateString = serviceDate.plusDays(1).format(formatter);
                payload.setRequestedTurnOffDate(serviceDateString); // invalid format/value
            }

            // aglcServiceOrderNumber
            case AGLC_SERVICE_ORDER_NUMBER_MISSING_NEGATIVE_TC178 -> payload.setAglcServiceOrderNumber(""); // missing
            case AGLC_SERVICE_ORDER_NUMBER_INVALID_LENGTH_MAX_NEGATIVE_TC179 -> payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(12)); // too long
            case AGLC_SERVICE_ORDER_NUMBER_INVALID_LENGTH_MIN_NEGATIVE_TC180 ->  payload.setAglcServiceOrderNumber(FakerDataGenerator.generateAlphanumeric(5)); // too short

            // etcExists
            case ETC_EXISTS_MISSING_NEGATIVE_TC181 -> payload.setEtcExists(FakerDataGenerator.getRandomString(4));
            case ETC_MISSING_NEGATIVE_TC182 -> setMarketerReferenceData(payload, String.valueOf(testContext.getMarketerReferenceData()));
            case MRD_INVALID_VALUE_NEGATIVE_TC183 -> payload.setMarketerReferenceData(null);
            case MRD_LENGTH_MAX_NEGATIVE_TC184 -> payload.setMarketerReferenceData("");
            case MRD_LENGTH_MIN_NEGATIVE_TC185 -> payload.setMarketerReferenceData(String.valueOf(FakerDataGenerator.generateDigits(15)));
            case MRD_DUPLICATE_NEGATIVE_TC186 -> payload.setMarketerReferenceData(FakerDataGenerator.generateDigits(4));
            case MRD_NOT_NUMERIC_NEGATIVE_TC187 -> payload.setMarketerReferenceData(InvalidValues.DUPLICATE_MARKETER_REFERENCE_NUMBER.getValue());
            default -> { }
        }
    }

    // Put this inside SaveUnenrollmentHelper
    private void setEtcExists(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel testCondition) {
        switch (testCondition) {
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC173 -> payload.setEtcExists(true);
            case  TURN_OFF_SUB_REASON_MISSING_FOR_SERVICE_TRANSFER_NEGATIVE_TC174A -> payload.setEtcExists(false);

            // etcExists
            case ETC_EXISTS_MISSING_NEGATIVE_TC181 -> payload.setEtcExists(FakerDataGenerator.generateString(4));
            case ETC_MISSING_NEGATIVE_TC182 -> payload.setEtcExists("");

            // Everything else on Service Transfer should leave it NULL
            default -> payload.setEtcExists(null);
        }
    }

    public void setCustomerCodePremCodeAGLCServiceNo(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel testCondition) {

        Map<String, Object> row;

        switch (testCondition) {
            // ==== WITH-ETC (RGB) ====
           case TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC154 -> {
                       row = ApplicationContext.get().getDbAction()
                        .custCodeParamCodeAGLCAccNo_WitEtcGPP(PlanCode.RGB.getValue(), CustomerType.RESIDENTIAL.getValue());
               if (row == null || row.isEmpty()) {
                   throw new IllegalStateException("Empty DB row for " + testCondition);
               }
               setCustomerInfo(payload, row);
           }

            // ==== WITH-ETC (RGB) ====
            case TURN_OFF_REASON_EMPTY_NEGATIVE_TC143,
                 TURN_OFF_REASON_INVALID_NEGATIVE_TC145,
                 TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC155,
                 TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC156,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC157,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC158,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC159,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC161,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC162,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC163,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC164,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC165,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC166,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC167,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC170,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC171,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC172,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC173,
                 TURN_OFF_SUB_REASON_MISSING_FOR_SERVICE_TRANSFER_NEGATIVE_TC174A,
                 ETC_EXISTS_MISSING_NEGATIVE_TC181,
                 ETC_MISSING_NEGATIVE_TC182 -> {
                row = ApplicationContext.get().getDbAction()
                        .custCodeParamCodeAGLCAccNoServByPlanCodeCustomerType(PlanCode.RGB.getValue(), CustomerType.RESIDENTIAL.getValue());
                if (row == null || row.isEmpty()) {
                    throw new IllegalStateException("Empty DB row for " + testCondition);
                }
                setCustomerInfo(payload, row);
            }

            // ==== NO-ETC + HAS SERVICE ORDER (MVS) ====
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC150,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC160,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC168,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC169,
                 TURN_OFF_SUB_REASON_MISSING_FOR_SERVICE_TRANSFER_NEGATIVE_TC174B,
                 EMAIL_ADDRESS_INVALID_LENGTH_NEGATIVE_TC175A,
                 EMAIL_ADDRESS_INVALID_FORMAT_NEGATIVE_TC175B,
                 REQUESTED_TURNOFF_DATE_MISSING_NEGATIVE_TC176,
                 REQUESTED_TURNOFF_DATE_INVALID_NEGATIVE_TC177,
                 AGLC_SERVICE_ORDER_NUMBER_MISSING_NEGATIVE_TC178,
                 AGLC_SERVICE_ORDER_NUMBER_INVALID_LENGTH_MAX_NEGATIVE_TC179,
                 AGLC_SERVICE_ORDER_NUMBER_INVALID_LENGTH_MIN_NEGATIVE_TC180,
                 MRD_INVALID_VALUE_NEGATIVE_TC183,
                 MRD_LENGTH_MAX_NEGATIVE_TC184,
                 MRD_LENGTH_MIN_NEGATIVE_TC185,
                 MRD_DUPLICATE_NEGATIVE_TC186,
                 MRD_NOT_NUMERIC_NEGATIVE_TC187 -> {
                row = ApplicationContext.get().getDbAction()
                    .custCodeParamCodeAGLCAccNoServByPlanCodeCustomerType(PlanCode.MVS.getValue(), CustomerType.RESIDENTIAL.getValue());
                if (row == null || row.isEmpty()) {
                    throw new IllegalStateException("Empty DB row for " + testCondition);
                }
                setCustomerInfo(payload, row);
            }
            default -> {}
        }
    }

    private void setCustomerInfo(SaveUnenrollmentRequest payload, Map<String, Object> row) {
        payload.setPremisesCode(row.get("GTBTRNH_PREM_CODE").toString());
        payload.setCustomerCode(row.get("GTBTRNH_CUST_CODE").toString());
        payload.setAglcAccountNumber(row.get("GTBTRNH_AGLC_ACCT_NBR").toString());
        payload.setAglcServiceOrderNumber(row.get("GTRRNDN_SERV_ORD_NUM").toString());
    }

    private static void set(SaveUnenrollmentRequest payload, TurnOffReason t) {
        payload.setTurnOffReason(t.getReason());
        payload.setTurnOffSubReason(t.getSubReason());
    }

    public void setReasonAndSubReason(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel testCondition) {

        switch (testCondition) {
            case TURN_OFF_REASON_EMPTY_NEGATIVE_TC143 -> {
                payload.setTurnOffReason("");
                payload.setTurnOffSubReason(""); // explicit empty
            }
            case TURN_OFF_REASON_INVALID_NEGATIVE_TC145 -> {
                payload.setTurnOffReason(TurnOffReason.INVALID.getReason());
                payload.setTurnOffSubReason("");
            }
            // “Sub reason empty” negatives —
            case TURN_OFF_SUB_REASON_EMPTY_NEGATIVE_TC146 -> {
                payload.setTurnOffReason(TurnOffReason.MOVING_DECEASED.getReason());
                payload.setTurnOffSubReason("");
            }
            case TURN_OFF_SUB_REASON_EMPTY_MOVING_NEGATIVE_TC148 -> {
                payload.setTurnOffReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP.getReason());
                payload.setTurnOffSubReason("");
            }
            // Invalid for Service Transfer
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC150 -> {
                set(payload, TurnOffReason.MOVING_REGULATED_PROVIDER);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC151 -> {
                set(payload, TurnOffReason.MOVING_DECEASED);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC152 -> {
                set(payload, TurnOffReason.MOVING_FINANCIAL_SITUATION);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC153 -> {
                set(payload, TurnOffReason.MOVING_RENOVATION_ELECTRIC);
            }
            // Sub-reason invalid for Service Transfer (use ST reason but wrong subreason)
            case TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC154 -> {
                payload.setTurnOffReason(TurnOffReason.MOVING_SERVICE_TRANSFER.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP.getSubReason()); // <- no “ETC Waived”
            }
            case TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC155 -> {
                payload.setTurnOffReason(TurnOffReason.MOVING_SERVICE_TRANSFER.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP_ETC_WAIVED.getSubReason());
            }
            case TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC156 -> {
                payload.setTurnOffReason(TurnOffReason.MOVING_NOT_STAYING_WITH_GNG.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_NOT_STAYING_WITH_GNG.getSubReason());
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC157 -> {
                set(payload, TurnOffReason.SEASONAL_OR_HEAT_SERVICE_TERRITORY);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC158 -> {
                set(payload, TurnOffReason.OTHER_OUTSIDE_POOL_GROUP);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC159 -> {
                set(payload, TurnOffReason.OTHER_OUTSIDE_TERRITORY);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC160 -> {
                set(payload, TurnOffReason.OTHER_DECEASED);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC161,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC167 -> {
                set(payload, TurnOffReason.OTHER_REGULATED_PROVIDER_ETC_WAIVED);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC162,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC166 -> {
                set(payload, TurnOffReason.OTHER_MILITARY_ETC_WAIVED);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC163,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC165 -> {
                set(payload, TurnOffReason.OTHER_DECEASED_ETC_WAIVED);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC164 -> {
                set(payload, TurnOffReason.OTHER_RENOVATION_ELECTRIC_CONVERSION);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC168,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC171 -> {
                set(payload, TurnOffReason.REAP_REALTOR_INSPECTION);
                payload.setTurnOffSubReason(null);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC169,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC172 -> {
                set(payload, TurnOffReason.HOUSEHOLD_ACCOUNT_CHANGE);
                payload.setTurnOffSubReason(null);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC170 -> {
                set(payload, TurnOffReason.SEASONAL_OR_HEAT_ONLY);
            }
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC173 -> {
                set(payload, TurnOffReason.MOVING_SERVICE_TRANSFER_ETC_WAIVED);
            }
            case TURN_OFF_SUB_REASON_MISSING_FOR_SERVICE_TRANSFER_NEGATIVE_TC174A,
                 ETC_EXISTS_MISSING_NEGATIVE_TC181,
                 ETC_MISSING_NEGATIVE_TC182,
                 MRD_INVALID_VALUE_NEGATIVE_TC183-> {
                payload.setTurnOffReason(TurnOffReason.MOVING_SERVICE_TRANSFER_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_SERVICE_TRANSFER_ETC_WAIVED.getSubReason());
            }
            case TURN_OFF_SUB_REASON_MISSING_FOR_SERVICE_TRANSFER_NEGATIVE_TC174B -> {
                payload.setTurnOffReason(TurnOffReason.MOVING_SERVICE_TRANSFER_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason("");
            }
            case EMAIL_ADDRESS_INVALID_LENGTH_NEGATIVE_TC175A,
                 EMAIL_ADDRESS_INVALID_FORMAT_NEGATIVE_TC175B,
                 REQUESTED_TURNOFF_DATE_MISSING_NEGATIVE_TC176,
                 REQUESTED_TURNOFF_DATE_INVALID_NEGATIVE_TC177,
                 AGLC_SERVICE_ORDER_NUMBER_MISSING_NEGATIVE_TC178,
                 AGLC_SERVICE_ORDER_NUMBER_INVALID_LENGTH_MAX_NEGATIVE_TC179,
                 AGLC_SERVICE_ORDER_NUMBER_INVALID_LENGTH_MIN_NEGATIVE_TC180,
                 MRD_LENGTH_MAX_NEGATIVE_TC184,
                 MRD_LENGTH_MIN_NEGATIVE_TC185,
                 MRD_DUPLICATE_NEGATIVE_TC186,
                 MRD_NOT_NUMERIC_NEGATIVE_TC187 ->  payload.setTurnOffReason(null);

            default -> {
                payload.setTurnOffReason(null);
                payload.setTurnOffSubReason(null);
            }
        }
    }

    public void setMarketerReferenceData(SaveUnenrollmentRequest payload, String marketerReferenceData) {
        payload.setMarketerReferenceData(marketerReferenceData);
    }

    public void setAddressDetailsBasedOnType(SaveUnenrollmentRequest payload,  SaveUnenrollmentApiLabel testCondition) {
            switch (testCondition) {
                // CA address leave as is
                case ETC_MISSING_NEGATIVE_TC182,
                     MRD_INVALID_VALUE_NEGATIVE_TC183,
                     MRD_DUPLICATE_NEGATIVE_TC186,
                     MRD_NOT_NUMERIC_NEGATIVE_TC187,
                     TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC173-> {
                }

                //NA new Address
                default -> {
                    Map<String, Object> addr = ApplicationContext.get().getDbAction().getAddressDetails();
                    payload.setForwardingAddressCity(addr.get("UCRADDR_CITY").toString());
                    payload.setForwardingAddressStateCode(addr.get("UCRADDR_STAT_CODE").toString());
                    payload.setForwardingAddressZipCode(addr.get("UCRADDR_ZIP").toString());
                    payload.setForwardingAddressIs(NEW_ADDRESS.getValue());
                    payload.setForwardingAddressType(STREET.getValue());
                    payload.setForwardingAddressStreetNumber(addr.get("UCRADDR_STREET_NUMBER").toString());
                    payload.setForwardingAddressStreetName(addr.get("UCRADDR_STREET_NAME").toString());
                    payload.setForwardingAddressStreetSuffix(addr.get("UCRADDR_SSFX_CODE").toString());
                    payload.setForwardingAddressStreetPostDirection(addr.get("UCRADDR_PDIR_CODE_POST").toString());
                    payload.setForwardingAddressStreetPreDirection(addr.get("UCRADDR_PDIR_CODE_PRE").toString()); }
            }
    }
}
