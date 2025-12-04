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
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import static com.gng.api.constants.GlobalEnums.*;
import static com.gng.api.constants.GlobalEnums.AddressType.*;
import static com.gng.api.constants.GlobalEnums.ForwardingAddressType.*;

@Slf4j
public class SaveUnenrollmentHelper {

    private final TestContext testContext;
    String forwardingAddressIs = "NA";
    String forwardingAddressType = "S";
    String forwardingAddressPOBox= "P";
    String forwardingAddressStreetNumber = "609";
    String forwardingAddressStreetPreDirection = "";
    String forwardingAddressStreetName = "STOKESWOOD";
    String forwardingAddressStreetSuffix = "AVE";
    String forwardingAddressStreetPostDirection = "SE";
    String forwardingAddressCity = "ATLANTA";
    String forwardingAddressStateCode = "GA";
    String forwardingAddressZipCode = "30339";



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

            case TRANSACTION_TYPE_EMPTY_NEGATIVE_TC104 -> payload.setTransactionType("");
            case TRANSACTION_TYPE_MAX_LENGTH_NEGATIVE_TC105 -> payload.setTransactionType(FakerDataGenerator.getRandomString(10));
            case TRANSACTION_TYPE_INVALID_NEGATIVE_TC106 -> payload.setTransactionType(GlobalEnums.TransactionType.INVALID.getValue());

            case CUSTOMER_CODE_EMPTY_NEGATIVE_TC107 -> payload.setCustomerCode(null);
            case CUSTOMER_CODE_MAX_LENGTH_NEGATIVE_TC108 -> payload.setCustomerCode(FakerDataGenerator.getRandomNumericString(12));
            case CUSTOMER_CODE_NON_INT_NEGATIVE_TC109 -> payload.setCustomerCode(FakerDataGenerator.generateString(5));
            case CUSTOMER_CODE_NOT_EXISTS_NEGATIVE_TC110 -> payload.setCustomerCode(0);

            case PREMISES_CODE_EMPTY_NEGATIVE_TC111 -> payload.setPremisesCode("");
            case PREMISES_CODE_MAX_LENGTH_NEGATIVE_TC112 -> payload.setPremisesCode(FakerDataGenerator.getRandomNumericString(15));
            case PREMISES_CODE_NOT_EXISTS_NEGATIVE_TC113 -> payload.setPremisesCode("0");

            case ACCOUNT_COMBINATION_INVALID_NEGATIVE_TC114 -> {
                payload.setCustomerCode(FakerDataGenerator.generateDigits(5));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(5));
            }

            case AGLC_ACCOUNT_EMPTY_NEGATIVE_TC115 -> payload.setAglcAccountNumber("");
            case AGLC_ACCOUNT_MAX_LENGTH_NEGATIVE_TC116 -> payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(30));

            case FWD_ADDRESS_EMPTY_NEGATIVE_TC117 -> payload.setForwardingAddressIs("");
            case FWD_ADDRESS_MAX_LENGTH_NEGATIVE_TC118 -> payload.setForwardingAddressIs(FakerDataGenerator.getRandomString(4));
            case FWD_ADDRESS_INVALID_VALUE_NEGATIVE_TC119 -> payload.setForwardingAddressIs(GlobalEnums.ForwardingAddressType.INVALID.getValue());

            case FWD_ADDRESS_TYPE_INVALID_NEGATIVE_TC120, FWD_ADDRESS_TYPE_EMPTY_NEGATIVE_TC121 -> payload.setForwardingAddressType("");
            case FWD_ADDRESS_TYPE_INVALID_VALUE_NEGATIVE_TC122 ->  payload.setForwardingAddressType(FakerDataGenerator.getRandomString(5));

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

            case TURN_OFF_REASON_EMPTY_NEGATIVE_TC143, TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC173,
                 TURN_OFF_SUB_REASON_MISSING_FOR_SERVICE_TRANSFER_NEGATIVE_TC174A,
                 TURN_OFF_REASON_INVALID_NEGATIVE_TC145, TURN_OFF_SUB_REASON_EMPTY_NEGATIVE_TC146 -> {
            }
            case TURN_OFF_REASON_MAX_LENGTH_NEGATIVE_TC144 -> payload.setTurnOffReason(FakerDataGenerator.getRandomString(200));
            case TURN_OFF_SUB_REASON_MAX_LENGTH_NEGATIVE_TC147 -> payload.setTurnOffSubReason(FakerDataGenerator.getRandomString(200));
            case TURN_OFF_SUB_REASON_MAX_LENGTH_MOVING_NEGATIVE_TC149 -> payload.setTurnOffSubReason(FakerDataGenerator.getRandomString(201));

            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC150,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC151,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC152,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC153,
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
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC168,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC169,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC170,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC171,
                 TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC172,
                 TURN_OFF_SUB_REASON_MISSING_FOR_SERVICE_TRANSFER_NEGATIVE_TC174B
                 -> {
                //invalid turn off reason and or sub reason
            }
            case  EMAIL_ADDRESS_INVALID_LENGTH_NEGATIVE_TC175A -> payload.setEmailAddress(FakerDataGenerator.generateString(105));
            case  EMAIL_ADDRESS_INVALID_FORMAT_NEGATIVE_TC175B -> payload.setEmailAddress(FakerDataGenerator.generateString(4) + "." + FakerDataGenerator.generateString(8) + "." + FakerDataGenerator.generateString(3));

            case REQUESTED_TURNOFF_DATE_MISSING_NEGATIVE_TC176 -> payload.setRequestedTurnOffDate("");
            case REQUESTED_TURNOFF_DATE_INVALID_NEGATIVE_TC177 -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate serviceDate = LocalDate.now();
                String serviceDateString = serviceDate.plusDays(1).format(formatter);
                payload.setRequestedTurnOffDate(serviceDateString);
            }

            case AGLC_SERVICE_ORDER_NUMBER_MISSING_NEGATIVE_TC178 -> payload.setAglcServiceOrderNumber("");
            case AGLC_SERVICE_ORDER_NUMBER_INVALID_LENGTH_MAX_NEGATIVE_TC179 -> payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(12));
            case AGLC_SERVICE_ORDER_NUMBER_INVALID_LENGTH_MIN_NEGATIVE_TC180 ->  payload.setAglcServiceOrderNumber(FakerDataGenerator.generateAlphanumeric(5));

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


    private void setEtcExists(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel testCondition) {
        switch (testCondition) {
            case TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC173 -> payload.setEtcExists(true);
            case  TURN_OFF_SUB_REASON_MISSING_FOR_SERVICE_TRANSFER_NEGATIVE_TC174A -> payload.setEtcExists(false);
            case ETC_EXISTS_MISSING_NEGATIVE_TC181 -> payload.setEtcExists(FakerDataGenerator.generateString(4));
            case ETC_MISSING_NEGATIVE_TC182 -> payload.setEtcExists("");

            default -> payload.setEtcExists(null);
        }
    }

    public void setCustomerCodePremCodeAGLCServiceNo(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel testCondition) {

        Map<String, Object> row;

        switch (testCondition) {
           case TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC154 -> {
                       row = ApplicationContext.get().getDbAction()
                        .custCodeParamCodeAGLCAccNo_WitEtcGPP(PlanCode.RGB.getValue(), CustomerType.RESIDENTIAL.getValue());
               if (row == null || row.isEmpty()) {
                   throw new IllegalStateException("Empty DB row for " + testCondition);
               }
               setCustomerInfo(payload, row);
           }

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
                payload.setTurnOffSubReason("");
            }
            case TURN_OFF_REASON_INVALID_NEGATIVE_TC145 -> {
                payload.setTurnOffReason(TurnOffReason.INVALID.getReason());
                payload.setTurnOffSubReason("");
            }
            case TURN_OFF_SUB_REASON_EMPTY_NEGATIVE_TC146 -> {
                payload.setTurnOffReason(TurnOffReason.MOVING_DECEASED.getReason());
                payload.setTurnOffSubReason("");
            }
            case TURN_OFF_SUB_REASON_EMPTY_MOVING_NEGATIVE_TC148 -> {
                payload.setTurnOffReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP.getReason());
                payload.setTurnOffSubReason("");
            }
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
            case TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC154 ->{
                payload.setTurnOffReason(TurnOffReason.MOVING_OUTSIDE_AGLC_TERRITORY_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_OUTSIDE_AGLC_TERRITORY_ETC_WAIVED.getSubReason());
            }
            case TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC155 -> {
                payload.setTurnOffReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP_ETC_WAIVED.getReason());
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
                case ETC_MISSING_NEGATIVE_TC182,
                     MRD_INVALID_VALUE_NEGATIVE_TC183,
                     MRD_DUPLICATE_NEGATIVE_TC186,
                     MRD_NOT_NUMERIC_NEGATIVE_TC187,
                     TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC173-> {
                }

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

    public void setCustomerCodePremCodeAGLCServiceNoForAccountType(
            SaveUnenrollmentRequest payload,
            String accountType,
            SaveUnenrollmentApiLabel testCondition) {

        payload.setRequestID(FakerDataGenerator.generateString(10));

        switch (testCondition) {
            case ACN_RS_TC_271, NACN_RS_TC_270, ACN_RS_TC_269, ACN_RS_TC_268, NACN_RS_TC_267, NACN_RS_TC_266, ACN_RS_TC_265,
                 ACN_RS_REMAINS_ON_TIER_1_TC_251, ACN_RS_REMAINS_ON_TIER_1_NACN_TC_252, ACN_RS_TC_253, NACN_RS_TC_254,
                 ACN_RS_TC_255, NACN_RS_TC_256, NACN_SR_TC_258, ACN_RS_TC_259, NACN_RS_TC_260, NACN_SR_TC_262,
                 ACN_RS_TC_263, NACN_RS_TC_264,
                 NACN_RS_TC_273, ACN_RS_TC_274, ACN_RS_TC_275, NACN_RS_TC_276, NACN_RS_TC_277, ACN_RS_TC_278,
                 NACN_RS_TC_279, NACN_RS_TC_280, ACN_CM_TC_281, NACN_CM_TC_282, NACN_CM_TC_283, ACN_CM_TC_284,
                 NACN_RS_TC_285, NACN_RS_TC_286, NACN_CM_TC_287, NACN_CM_TC_288, NACN_RS_TC_289, NACN_RS_TC_290,
                 NACN_RS_TC_291, NACN_RS_TC_292, NACN_RS_TC_293, NACN_RS_TC_294, NACN_RS_TC_295, NACN_RS_TC_296,
                 NACN_RS_TC_297, ACN_RS_TC_298, NACN_RS_TC_300, NACN_CM_TC_301 -> {
                payload.setCustomerCode(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getCustomerCode());
                payload.setPremisesCode(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesCode());
                var acct = testContext.getSearchAccountsResponse().getData().getAccounts().getFirst();
                String aglcAcct = acct.getAglcAccountNumber();
                payload.setAglcAccountNumber(aglcAcct);
                String last9 = aglcAcct.substring(Math.max(0, aglcAcct.length() - 9));
                payload.setAglcServiceOrderNumber(last9);
            }


        default ->
                    throw new IllegalArgumentException("Unsupported test condition: " + testCondition);
        }

    }

    public void setTurnOffReasonAndSubReason(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel testCondition) {
        switch (testCondition) {
            case ACN_RS_REMAINS_ON_TIER_1_TC_251:
            case ACN_RS_REMAINS_ON_TIER_1_NACN_TC_252:
            case ACN_RS_TC_255:
            case NACN_SR_TC_258:
            case ACN_RS_TC_259:
            case NACN_SR_TC_262:
            case ACN_RS_TC_263:
            case NACN_RS_TC_264:
            case ACN_RS_TC_265:
            case NACN_RS_TC_266:
            case NACN_RS_TC_267:
            case ACN_RS_TC_269:
            case NACN_RS_TC_270:
            case ACN_RS_TC_271:
            case NACN_RS_TC_273:
            case ACN_RS_TC_274:
            case ACN_RS_TC_275:
            case NACN_RS_TC_276:
            case NACN_RS_TC_277:
            case ACN_RS_TC_278:
            case NACN_RS_TC_279:
            case NACN_RS_TC_286:
            case NACN_RS_TC_289:
            case NACN_RS_TC_290:
            case NACN_RS_TC_291:
            case NACN_RS_TC_292:
            case NACN_RS_TC_293:
            case NACN_RS_TC_294:
            case NACN_RS_TC_295:
            case NACN_RS_TC_296:
            case NACN_RS_TC_297:
            case NACN_RS_TC_300:
            case NACN_RS_TC_280:
                payload.setTurnOffReason("");
                payload.setTurnOffSubReason("");
                payload.setForwardingAddressIs("CA");
                break;

            case ACN_CM_TC_281:
            case NACN_CM_TC_282:
            case NACN_CM_TC_283:
            case ACN_CM_TC_284:
            case NACN_CM_TC_287:
            case NACN_CM_TC_288:
            case NACN_CM_TC_301:
                payload.setTurnOffReason("");
                payload.setTurnOffSubReason("");
                payload.setForwardingAddressIs("CA");
                payload.setCustomerType("CM");
                break;

            case ACN_RS_TC_253:
            case ACN_RS_TC_268:
                payload.setTurnOffReason("");
                payload.setTurnOffSubReason("");
                payload.setForwardingAddressIs(forwardingAddressIs);
                payload.setForwardingAddressType(forwardingAddressType);
                payload.setForwardingAddressStreetNumber(forwardingAddressStreetNumber);
                payload.setForwardingAddressStreetPreDirection(forwardingAddressStreetPreDirection);
                payload.setForwardingAddressStreetName(forwardingAddressStreetName);
                payload.setForwardingAddressStreetSuffix(forwardingAddressStreetSuffix);
                payload.setForwardingAddressStreetPostDirection(forwardingAddressStreetPostDirection);
                payload.setForwardingAddressCity(forwardingAddressCity);
                payload.setForwardingAddressStateCode(forwardingAddressStateCode);
                payload.setForwardingAddressZipCode(forwardingAddressZipCode);
                break;

            case NACN_RS_TC_254:
            case NACN_RS_TC_260:
            case NACN_RS_TC_285:
            case ACN_RS_TC_298:
                payload.setTurnOffReason("MOVING");
                payload.setTurnOffSubReason("SERVICE TRANSFER - ETC WAIVED");
                payload.setForwardingAddressIs("CA");
                break;

            case NACN_RS_TC_256:
                payload.setTurnOffReason("");
                payload.setTurnOffSubReason("");
                payload.setForwardingAddressIs(forwardingAddressIs);
                payload.setForwardingAddressType(forwardingAddressPOBox);
                payload.setForwardingAddressPOBox(forwardingAddressStreetNumber);
                payload.setForwardingAddressCity(forwardingAddressCity);
                payload.setForwardingAddressStateCode(forwardingAddressStateCode);
                payload.setForwardingAddressZipCode(forwardingAddressZipCode);
        }
    }


    public void setEmailAddress(SaveUnenrollmentRequest payload, Boolean setEmail) {
        if (setEmail) {
            payload.setEmailAddress(FakerDataGenerator.generateEmail());
        }
    }

    public void setEtcExists(SaveUnenrollmentRequest payload, Object etcExists) {
        if(etcExists.equals("null")){
            payload.setEtcExists(null);
        }
        else {
            payload.setEtcExists(Boolean.parseBoolean(etcExists.toString()));
        }
    }

    public void performDatabaseValidationsPostUnenrollment() {
        String customerCode = testContext.getCustomerCode();
        Map<String, Object> unEnrollmentRecord = ApplicationContext.get()
                .getDbAction()
                .validateAllTheTablesAfterUnenrollment(customerCode);

        Assert.assertEquals(unEnrollmentRecord.get("UZBENRO_CUST_CODE").toString(), customerCode);
    }

}
