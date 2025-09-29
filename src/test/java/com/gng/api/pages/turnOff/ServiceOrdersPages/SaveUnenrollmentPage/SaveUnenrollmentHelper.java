package com.gng.api.pages.turnOff.ServiceOrdersPages.SaveUnenrollmentPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel;
import com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.TurnOffReason;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.gng.api.constants.GlobalEnums.AddressType.*;
import static com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel.NEW_ADDRESS;

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

    public void setRequestAndLoginIDBasedOnType(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel testCondition) {
        switch (testCondition) {
            case REQUEST_ID_EMPTY_NEGATIVE_TC106:
                payload.setRequestID("");
                break;
            case REQUEST_ID_MAX_LENGTH_NEGATIVE_TC107:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(200));
                break;
            case REQUEST_ID_DUPLICATE_NEGATIVE_TC108:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;
            case LOGIN_ID_EMPTY_NEGATIVE_TC109:
                payload.setLoginID("");
                break;
            case LOGIN_ID_MAX_LENGTH_NEGATIVE_TC110:
                payload.setLoginID(FakerDataGenerator.getRandomString(50));
                break;
            case LOGIN_ID_INVALID_ALPHA_NEGATIVE_TC111:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(6));
                break;
            case LOGIN_ID_NOT_EXISTS_NEGATIVE_TC112:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateString(8));
                break;
        }
    }

    public void setParametersBasedOnType(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));

        switch (testCondition) {
            case TRANSACTION_TYPE_EMPTY_NEGATIVE_TC113:
               payload.setTransactionType("");
                break;
            case TRANSACTION_TYPE_MAX_LENGTH_NEGATIVE_TC114:
                payload.setTransactionType(FakerDataGenerator.getRandomString(10));
                break;
            case TRANSACTION_TYPE_INVALID_NEGATIVE_TC115:
                payload.setTransactionType(GlobalEnums.TransactionType.INVALID.getValue());
                break;
            case CUSTOMER_CODE_EMPTY_NEGATIVE_TC116:
                payload.setCustomerCode(null);
                break;
            case CUSTOMER_CODE_MAX_LENGTH_NEGATIVE_TC117:
                payload.setCustomerCode(FakerDataGenerator.getRandomNumericString(20));
                break;
            case CUSTOMER_CODE_NON_INT_NEGATIVE_TC118:
                payload.setCustomerCode(FakerDataGenerator.generateString(5));
                break;
            case CUSTOMER_CODE_NOT_EXISTS_NEGATIVE_TC119:
                payload.setCustomerCode(0);
                break;
            case PREMISES_CODE_EMPTY_NEGATIVE_TC120:
                payload.setPremisesCode("");
                break;
            case PREMISES_CODE_MAX_LENGTH_NEGATIVE_TC121:
                payload.setPremisesCode(FakerDataGenerator.getRandomNumericString(15));
                break;
            case PREMISES_CODE_NOT_EXISTS_NEGATIVE_TC122:
                payload.setPremisesCode("0");
                break;
            case ACCOUNT_COMBINATION_INVALID_NEGATIVE_TC123:
                payload.setCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
                payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());
                payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(5));
                break;
            case AGLC_ACCOUNT_EMPTY_NEGATIVE_TC124:
                payload.setAglcAccountNumber("");
                break;
            case AGLC_ACCOUNT_MAX_LENGTH_NEGATIVE_TC125:
                payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(30));
                break;
            case FWD_ADDRESS_EMPTY_NEGATIVE_TC129:
                payload.setForwardingAddressIs("");
                break;
            case FWD_ADDRESS_MAX_LENGTH_NEGATIVE_TC130:
                payload.setForwardingAddressIs(FakerDataGenerator.getRandomString(4));
                break;
            case FWD_ADDRESS_INVALID_VALUE_NEGATIVE_TC131:
                payload.setForwardingAddressIs(GlobalEnums.ForwardingAddressType.INVALID.getValue());
                break;
            case FWD_ADDRESS_TYPE_EMPTY_NEGATIVE_TC132:
                payload.setForwardingAddressIs(GlobalEnums.ForwardingAddressType.NEW_ADDRESS.getValue());
                payload.setForwardingAddressType("");
                break;
            case FWD_ADDRESS_TYPE_MAX_LENGTH_NEGATIVE_TC133:
                payload.setForwardingAddressIs(GlobalEnums.ForwardingAddressType.NEW_ADDRESS.getValue());
                payload.setForwardingAddressType(FakerDataGenerator.getRandomString(5));
                break;
            case FWD_ADDRESS_TYPE_INVALID_VALUE_NEGATIVE_TC134:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setForwardingAddressType(GlobalEnums.ForwardingAddressType.INVALID.getValue());
                break;
            case FWD_ADD_STR_NUM_MAX_LENGTH_NEGATIVE_TC135:
                payload.setForwardingAddressStreetNumber(FakerDataGenerator.getRandomString(20));
                break;
            case FWD_ADD_STR_PRE_DIR_MAX_LENGTH_NEGATIVE_TC136:
                payload.setForwardingAddressStreetPreDirection(FakerDataGenerator.getRandomString(5));
                break;
            case FWD_ADD_STR_NAME_EMPTY_NEGATIVE_TC137:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setForwardingAddressStreetName("");
                break;
            case FWD_ADD_STR_NAME_MAX_LENGTH_NEGATIVE_TC138:
                payload.setForwardingAddressStreetName(FakerDataGenerator.getRandomString(50));
                break;
            case FWD_ADD_STR_SFX_MAX_LENGTH_NEGATIVE_TC139:
                payload.setForwardingAddressStreetSuffix(FakerDataGenerator.getRandomString(15));
                break;
            case FWD_ADD_STR_POST_DIR_MAX_LENGTH_NEGATIVE_TC140:
                payload.setForwardingAddressStreetPostDirection(FakerDataGenerator.getRandomString(5));
                break;
            case FWD_ADD_UNIT_TYPE_MAX_LENGTH_NEGATIVE_TC141:
                payload.setForwardingAddressUnitType(FakerDataGenerator.getRandomString(10));
                break;
            case FWD_ADD_UNIT_NUM_MAX_LENGTH_NEGATIVE_TC142:
                payload.setForwardingAddressUnitNumber(FakerDataGenerator.getRandomString(15));
                break;
            case FWD_ADD_RURAL_ROUTE_MAX_LENGTH_NEGATIVE_TC143:
                payload.setForwardingAddressRuralRoute(FakerDataGenerator.getRandomString(25));
                break;
            case FWD_ADD_PO_BOX_MAX_LENGTH_NEGATIVE_TC144:
                payload.setForwardingAddressPOBox(FakerDataGenerator.getRandomString(15));
                break;
            case FWD_ADD_LINE2_MAX_LENGTH_NEGATIVE_TC145:
                payload.setForwardingAddressLine2(FakerDataGenerator.getRandomString(50));
                break;
            case FWD_ADD_CITY_EMPTY_NEGATIVE_TC146:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setForwardingAddressCity("");
                break;
            case FWD_ADD_CITY_MAX_LENGTH_NEGATIVE_TC147:
                payload.setForwardingAddressCity(FakerDataGenerator.getRandomString(40));
                break;
            case FWD_ADD_STATE_CODE_EMPTY_NEGATIVE_TC148:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setForwardingAddressStateCode("");
                break;
            case FWD_ADD_STATE_CODE_MAX_LENGTH_NEGATIVE_TC149:
                payload.setForwardingAddressStateCode(FakerDataGenerator.getRandomString(5));
                break;
            case FWD_ADD_STATE_CODE_INVALID_NEGATIVE_TC150:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setForwardingAddressStateCode(GlobalEnums.InvalidValues.INVALID_PREMISE_STATE_CODE.getValue());
                break;
            case FWD_ADD_ZIP_CODE_EMPTY_NEGATIVE_TC151:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setForwardingAddressZipCode("");
                break;
            case FWD_ADD_ZIP_CODE_MAX_LENGTH_NEGATIVE_TC152:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setForwardingAddressZipCode(FakerDataGenerator.getRandomNumericString(12));
                break;
            case FWD_ADD_ZIP_CODE_INVALID_NEGATIVE_TC153:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setForwardingAddressZipCode(String.valueOf(FakerDataGenerator.generateNumber(4000,4999)));
                break;
            case FWD_ADD_ZIP_CODE_INVALID_FORMAT_NEGATIVE_TC154:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setForwardingAddressZipCode(FakerDataGenerator.generateDigits(3) + "-" + FakerDataGenerator.generateDigits(2));
                break;
            case TURN_OFF_REASON_EMPTY_NEGATIVE_TC155, TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC162,
                 TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC164, TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC163,
                 TURN_OFF_SUB_REASON_EMPTY_NEGATIVE_TC158, TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC165,
                 TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC166, TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC167,
                 TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC168, TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC169,
                 TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC178, TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC179:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                setTurnOffReasonAndSubReason(payload, testCondition);
                break;
            case TURN_OFF_REASON_MAX_LENGTH_NEGATIVE_TC156:
                payload.setTurnOffReason(FakerDataGenerator.getRandomString(200));
                break;
            case TURN_OFF_REASON_INVALID_NEGATIVE_TC157:
                payload.setTurnOffReason(TurnOffReason.INVALID.getReason());
                break;
            case TURN_OFF_SUB_REASON_MAX_LENGTH_NEGATIVE_TC159:
                payload.setTurnOffSubReason(FakerDataGenerator.getRandomString(200));
                break;
            case TURN_OFF_SUB_REASON_EMPTY_MOVING_NEGATIVE_TC160:
                setTurnOffReasonAndSubReason(payload, testCondition);
                break;
            case TURN_OFF_SUB_REASON_MAX_LENGTH_MOVING_NEGATIVE_TC161:
                payload.setTurnOffSubReason(FakerDataGenerator.getRandomString(201));
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC170, TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC171,
                 TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC172, TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC173,
                 TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC174, TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC175,
                 TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC176, TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC177,
                 TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC185, TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC186,
                 TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC187, TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC188,
                 TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC189, TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC190,
                 TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC191:
                payload.setEtcExists(true);
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                setTurnOffReasonAndSubReason(payload, testCondition);
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC180,
                 TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC181,
                 TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC182,
                 TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC183,
                 TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC184:
                payload.setEtcExists(false);
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                setTurnOffReasonAndSubReason(payload, testCondition);
                break;
            case EMAIL_MAX_LENGTH_NEGATIVE_TC192:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setEmailAddress(FakerDataGenerator.getRandomString(150) + "@" + FakerDataGenerator.getRandomString(4) + "." + FakerDataGenerator.generateString(3));
                break;
            case EMAIL_INVALID_FORMAT_NEGATIVE_TC193:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setEmailAddress(FakerDataGenerator.getRandomString(8) + "." + FakerDataGenerator.getRandomString(3));
                break;
            case REQ_TURN_OFF_DATE_EMPTY_NEGATIVE_TC194:
                payload.setRequestedTurnOffDate("");
                break;
            case REQ_TURN_OFF_DATE_INVALID_FORMAT_NEGATIVE_TC195:
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
                String invalidFormatedTurnOffDate = LocalDate.now()
                        .minusDays(30)
                        .format(formatter);
                payload.setRequestedTurnOffDate(invalidFormatedTurnOffDate);
                break;

            case AGLC_SVC_ORDER_NUM_EMPTY_NEGATIVE_TC196:
                payload.setAglcServiceOrderNumber("");
                break;
            case AGLC_SVC_ORDER_NUM_MAX_LENGTH_NEGATIVE_TC197:
                payload.setAglcServiceOrderNumber(FakerDataGenerator.getRandomNumericString(20));
                break;
            case AGLC_SVC_ORDER_NUM_NON_NUMERIC_NEGATIVE_TC198:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setAglcServiceOrderNumber(FakerDataGenerator.getRandomString(7));
                break;
            case ETC_EXISTS_INVALID_NEGATIVE_TC199:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                payload.setEtcExists(FakerDataGenerator.getRandomString(7));
                break;
            case ETC_EXISTS_EMPTY_NEGATIVE_TC200:
                payload.setEtcExists(null);
                break;
            case TURN_OFF_SUB_REASON_NOT_NULL_SEASONAL_ETC_TRUE_NEGATIVE_TC201,
                 TURN_OFF_SUB_REASON_NOT_NULL_REAP_ETC_TRUE_NEGATIVE_TC203,
                 TURN_OFF_SUB_REASON_NOT_NULL_HOUSEHOLD_CHANGE_ETC_TRUE_NEGATIVE_TC205:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                setTurnOffReasonAndSubReason(payload, testCondition);
                payload.setEtcExists(true);
                break;

            case TURN_OFF_SUB_REASON_NOT_NULL_SEASONAL_ETC_FALSE_NEGATIVE_TC202,
                 TURN_OFF_SUB_REASON_NOT_NULL_REAP_ETC_FALSE_NEGATIVE_TC204,
                 TURN_OFF_SUB_REASON_NOT_NULL_HOUSEHOLD_CHANGE_ETC_FALSE_NEGATIVE_TC206:
                setForwardingAddressDetailsBasedOnType(payload, NEW_ADDRESS, SaveUnenrollmentApiLabel.ADDRESS_TYPE_STREET);
                setTurnOffReasonAndSubReason(payload, testCondition);
                payload.setEtcExists(false);
                break;

            default:
                break;
        }

    }

    public void setTurnOffReasonAndSubReason(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel testCondition) {
        switch (testCondition) {
            case SEASONAL_OR_HEAT_ONLY_TC223:
            case SEASONAL_OR_HEAT_ONLY_TC207:
                payload.setTurnOffReason(TurnOffReason.SEASONAL_OR_HEAT_ONLY.getReason());
                payload.setTurnOffSubReason(TurnOffReason.SEASONAL_OR_HEAT_ONLY.getSubReason());
                break;

            case MOVING_OUTSIDE_AGLC_TC230,
                 MOVING_OUTSIDE_AGLC_TC208,
                 TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC175:
                payload.setTurnOffReason(TurnOffReason.MOVING_OUTSIDE_AGLC.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_OUTSIDE_AGLC.getSubReason());
                break;

            case OTHER_MILITARY_TC209:
                payload.setTurnOffReason(TurnOffReason.OTHER_MILITARY.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_MILITARY.getSubReason());
                break;

            case REAP_REALTOR_INSPECTION_TC227:
            case REAP_REALTOR_INSPECTION_TC210:
                payload.setTurnOffReason(TurnOffReason.REAP_REALTOR_INSPECTION.getReason());
                payload.setTurnOffSubReason(TurnOffReason.REAP_REALTOR_INSPECTION.getSubReason());
                break;

            case OTHER_FINANCIAL_SITUATION_TC225:
            case OTHER_FINANCIAL_SITUATION_TC215:
                payload.setTurnOffReason(TurnOffReason.OTHER_FINANCIAL_SITUATION.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_FINANCIAL_SITUATION.getSubReason());
                break;

            case MOVING_NOT_STAYING_WITH_GNG_TC229:
            case MOVING_NOT_STAYING_WITH_GNG_TC216:
                payload.setTurnOffReason(TurnOffReason.MOVING_NOT_STAYING_WITH_GNG.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_NOT_STAYING_WITH_GNG.getSubReason());
                break;

            case OTHER_REGULATED_PROVIDER_TC219:
                payload.setTurnOffReason(TurnOffReason.OTHER_REGULATED_PROVIDER.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_REGULATED_PROVIDER.getSubReason());
                break;

            case OTHER_DECEASED_TC222:
                payload.setTurnOffReason(TurnOffReason.OTHER_DECEASED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_DECEASED.getSubReason());
                break;

            case MOVING_SERVICE_TRANSFER_TC232:
            case MOVING_SERVICE_TRANSFER_TC228:
                payload.setTurnOffReason(TurnOffReason.MOVING_SERVICE_TRANSFER.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_SERVICE_TRANSFER.getSubReason());
                break;

            case MOVING_OUTSIDE_ETC_WAIVED_TC211:
                payload.setTurnOffReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP_ETC_WAIVED.getSubReason());
                break;

            case MOVING_OUTSIDE_AGLC_ETC_WAIVED_TC_218:
                payload.setTurnOffReason(TurnOffReason.MOVING_OUTSIDE_AGLC_TERRITORY_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_OUTSIDE_AGLC_TERRITORY_ETC_WAIVED.getSubReason());
                break;

            case MOVING_SERVICE_TRANSFER_ETC_WAIVED_TC_213:
                payload.setTurnOffReason(TurnOffReason.MOVING_SERVICE_TRANSFER_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_SERVICE_TRANSFER_ETC_WAIVED.getSubReason());
                break;

            case OTHER_MILITARY_ETC_WAIVED_TC_217:
            case OTHER_MILITARY_ETC_WAIVED_TC_231:
                payload.setTurnOffReason(TurnOffReason.OTHER_MILITARY_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_MILITARY_ETC_WAIVED.getSubReason());
                break;

            case OTHER_DECEASED_ETC_WAIVED_TC_220:
                payload.setTurnOffReason(TurnOffReason.OTHER_DECEASED_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_DECEASED_ETC_WAIVED.getSubReason());
                break;

            case OTHER_REGULATED_PROVIDER_ETC_WAIVED_TC212:
                payload.setTurnOffReason(TurnOffReason.OTHER_REGULATED_PROVIDER_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_REGULATED_PROVIDER_ETC_WAIVED.getSubReason());
                break;

            case OTHER_RENOVATION_ELECTRIC_CONVERSION_TC221:
            case OTHER_RENOVATION_ELECTRIC_CONVERSION_TC224:
                payload.setTurnOffReason(TurnOffReason.OTHER_RENOVATION_ELECTRIC_CONVERSION.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_RENOVATION_ELECTRIC_CONVERSION.getSubReason());
                break;
            case HOUSEHOLD_ACCOUNT_CHANGE_TC_214:
                payload.setTurnOffReason(TurnOffReason.HOUSEHOLD_ACCOUNT_CHANGE.getReason());
                payload.setTurnOffSubReason(TurnOffReason.HOUSEHOLD_ACCOUNT_CHANGE.getSubReason());
                break;
            case MOVING_OUTSIDE_POOL_GROUP_TC_226:
                payload.setTurnOffReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP.getSubReason());
                break;
            case TURN_OFF_REASON_EMPTY_NEGATIVE_TC155:
                payload.setTurnOffReason("");
                payload.setTurnOffSubReason(TurnOffReason.MOVING_MILITARY.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_EMPTY_NEGATIVE_TC158:
                payload.setTurnOffReason(TurnOffReason.OTHER_REGULATED_PROVIDER.getReason());
                payload.setTurnOffSubReason("");
                break;
            case TURN_OFF_SUB_REASON_MAX_LENGTH_NEGATIVE_TC159:
                payload.setTurnOffSubReason(FakerDataGenerator.getRandomString(200));
                break;
            case TURN_OFF_SUB_REASON_EMPTY_MOVING_NEGATIVE_TC160:
                payload.setTurnOffReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP.getReason());
                payload.setTurnOffSubReason("");
                break;
            case TURN_OFF_SUB_REASON_MAX_LENGTH_MOVING_NEGATIVE_TC161:
                payload.setTurnOffSubReason(FakerDataGenerator.getRandomString(201));
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC162:
                payload.setTurnOffReason(TurnOffReason.MOVING_REGULATED_PROVIDER.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_REGULATED_PROVIDER.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC163:
                payload.setTurnOffReason(TurnOffReason.MOVING_MILITARY.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_MILITARY.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC164:
                payload.setTurnOffReason(TurnOffReason.MOVING_RENOVATION_ELECTRIC.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_RENOVATION_ELECTRIC.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC165:
                payload.setTurnOffReason(TurnOffReason.MOVING_DECEASED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_DECEASED.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC166:
                payload.setTurnOffReason(TurnOffReason.MOVING_FINANCIAL_SITUATION.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_FINANCIAL_SITUATION.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC167:
                payload.setTurnOffReason(TurnOffReason.MOVING_OUTSIDE_AGLC_TERRITORY_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_OUTSIDE_AGLC_TERRITORY_ETC_WAIVED.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC168:
                payload.setTurnOffReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP_ETC_WAIVED.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC169:
                payload.setTurnOffReason(TurnOffReason.MOVING_SERVICE_TRANSFER_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_SERVICE_TRANSFER_ETC_WAIVED.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC170:
                payload.setTurnOffReason(TurnOffReason.MOVING_REGULATED_PROVIDER.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_REGULATED_PROVIDER.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC171:
                payload.setTurnOffReason(TurnOffReason.MOVING_MILITARY.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_MILITARY.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC172:
                payload.setTurnOffReason(TurnOffReason.MOVING_RENOVATION_ELECTRIC.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_RENOVATION_ELECTRIC.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC173:
                payload.setTurnOffReason(TurnOffReason.MOVING_DECEASED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_DECEASED.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC174:
                payload.setTurnOffReason(TurnOffReason.MOVING_FINANCIAL_SITUATION.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_FINANCIAL_SITUATION.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC176:
                payload.setTurnOffReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_OUTSIDE_POOL_GROUP.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC177:
                payload.setTurnOffReason(TurnOffReason.MOVING_SERVICE_TRANSFER.getReason());
                payload.setTurnOffSubReason(TurnOffReason.MOVING_SERVICE_TRANSFER.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC178:
                payload.setTurnOffReason(TurnOffReason.OTHER_OUTSIDE_TERRITORY.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_OUTSIDE_TERRITORY.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC179:
                payload.setTurnOffReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC180:
                payload.setTurnOffReason(TurnOffReason.OTHER_SERVICE_TRANSFER.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_SERVICE_TRANSFER.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC181:
                payload.setTurnOffReason(TurnOffReason.OTHER_WITHIN_POOL_NOT_GNG.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_WITHIN_POOL_NOT_GNG.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC182:
                payload.setTurnOffReason(TurnOffReason.OTHER_REGULATED_PROVIDER_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_REGULATED_PROVIDER_ETC_WAIVED.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC183:
                payload.setTurnOffReason(TurnOffReason.OTHER_MILITARY_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_MILITARY_ETC_WAIVED.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC184:
                payload.setTurnOffReason(TurnOffReason.OTHER_DECEASED_ETC_WAIVED.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_DECEASED_ETC_WAIVED.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC185:
                payload.setTurnOffReason(TurnOffReason.OTHER_OUTSIDE_TERRITORY.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_OUTSIDE_TERRITORY.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC186:
                payload.setTurnOffReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC187:
                payload.setTurnOffReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC188:
                payload.setTurnOffReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC189:
                payload.setTurnOffReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC190:
                payload.setTurnOffReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC191:
                payload.setTurnOffReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHER_OUTSIDE_POOL_GROUP.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_NOT_NULL_SEASONAL_ETC_TRUE_NEGATIVE_TC201:
                payload.setTurnOffReason(TurnOffReason.SEASONAL_OR_HEAT_SERVICE_TRANSFER.getReason());
                payload.setTurnOffSubReason(TurnOffReason.SEASONAL_OR_HEAT_SERVICE_TRANSFER.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_NOT_NULL_SEASONAL_ETC_FALSE_NEGATIVE_TC202:
                payload.setTurnOffReason(TurnOffReason.SEASONAL_OR_HEAT_SERVICE_TERRITORY.getReason());
                payload.setTurnOffSubReason(TurnOffReason.SEASONAL_OR_HEAT_SERVICE_TERRITORY.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_NOT_NULL_REAP_ETC_TRUE_NEGATIVE_TC203:
                payload.setTurnOffReason(TurnOffReason.REAP_REALTOR_INSPECTION_SERVICE_TRANSFER.getReason());
                payload.setTurnOffSubReason(TurnOffReason.REAP_REALTOR_INSPECTION_SERVICE_TRANSFER.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_NOT_NULL_REAP_ETC_FALSE_NEGATIVE_TC204:
                payload.setTurnOffReason(TurnOffReason.REAP_REALTOR_INSPECTION_TERRITORY.getReason());
                payload.setTurnOffSubReason(TurnOffReason.REAP_REALTOR_INSPECTION_TERRITORY.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_NOT_NULL_HOUSEHOLD_CHANGE_ETC_TRUE_NEGATIVE_TC205:
                payload.setTurnOffReason(TurnOffReason.HOUSEHOLD_ACCOUNT_SERVICE_TRANSFER.getReason());
                payload.setTurnOffSubReason(TurnOffReason.HOUSEHOLD_ACCOUNT_SERVICE_TRANSFER.getSubReason());
                break;
            case TURN_OFF_SUB_REASON_NOT_NULL_HOUSEHOLD_CHANGE_ETC_FALSE_NEGATIVE_TC206:
                payload.setTurnOffReason(TurnOffReason.HOUSEHOLD_ACCOUNT_TERRITORY.getReason());
                payload.setTurnOffSubReason(TurnOffReason.HOUSEHOLD_ACCOUNT_TERRITORY.getSubReason());
                break;

            default:
                throw new IllegalArgumentException("Unhandled reason: " + testCondition);
        }
    }


    public void setEmailAddress(SaveUnenrollmentRequest payload,Boolean setEmail){
        if(setEmail){
            payload.setEmailAddress(FakerDataGenerator.generateEmail());
        }
    }

    public void performDatabaseValidationsPostUnenrollment(){

        String customerCode= testContext.getCustomerCode();
        Map<String, Object> unEnrollmentRecord= ApplicationContext.get()
                .getDbAction()
                .validateAllTheTablesAfterUnenrollment(
                        customerCode
                );

        Assert.assertEquals(unEnrollmentRecord.get("UZBENRO_CUST_CODE").toString(), customerCode);

    }

    public void setEtcExists(SaveUnenrollmentRequest payload, Boolean etcExists ){
            payload.setEtcExists(etcExists);
    }

    public void setMarketerReferenceData(SaveUnenrollmentRequest payload, long marketerReferenceData){
        payload.setMarketerReferenceData(marketerReferenceData);
    }

    public void setForwardingAddressDetailsBasedOnType(SaveUnenrollmentRequest payload,SaveUnenrollmentApiLabel forwardingAddressIs, SaveUnenrollmentApiLabel type){
        if(forwardingAddressIs.equals(NEW_ADDRESS)){
            payload.setForwardingAddressIs(GlobalEnums.ForwardingAddressType.NEW_ADDRESS.getValue());

            Map<String, Object> addressDetails = ApplicationContext.get().getDbAction().getAddressDetails();
            String city = addressDetails.get("UCRADDR_CITY").toString();
            String state = addressDetails.get("UCRADDR_STAT_CODE").toString();
            String zip= addressDetails.get("UCRADDR_ZIP").toString();

            payload.setForwardingAddressCity(city);
            payload.setForwardingAddressStateCode(state);
            payload.setForwardingAddressZipCode(zip);

            switch(type){
                case ADDRESS_TYPE_STREET:
                    payload.setForwardingAddressType(STREET.getValue());
                    payload.setForwardingAddressStreetNumber(addressDetails.get("UCRADDR_STREET_NUMBER").toString());
                    payload.setForwardingAddressStreetName(addressDetails.get("UCRADDR_STREET_NAME").toString());
                    payload.setForwardingAddressStreetSuffix(addressDetails.get("UCRADDR_SSFX_CODE").toString());
                    payload.setForwardingAddressStreetPostDirection(addressDetails.get("UCRADDR_PDIR_CODE_POST").toString());
                    payload.setForwardingAddressStreetPreDirection(addressDetails.get("UCRADDR_PDIR_CODE_PRE").toString());
                    break;

                case ADDRESS_TYPE_RURAL:
                    payload.setForwardingAddressType(RURAL.getValue());
                    payload.setForwardingAddressRuralRoute(FakerDataGenerator.generateAlphanumeric(3));
                    break;

                case ADDRESS_TYPE_POBOX:
                    payload.setForwardingAddressType(POBOX.getValue());
                    payload.setForwardingAddressPOBox(FakerDataGenerator.generateAlphanumeric(2));
                    break;

                default:

            }
        }
    }

    public void setCustomerCodePremCodeAGLCServiceNo(SaveUnenrollmentRequest payload, String pricePlan, String sclsCode, SaveUnenrollmentApiLabel testCondition) {
        Map<String, Object> custPremAGLCServCode= null;
        switch (testCondition) {
            case MOVING_OUTSIDE_ETC_WAIVED_TC211, MOVING_SERVICE_TRANSFER_ETC_WAIVED_TC_213, OTHER_RENOVATION_ELECTRIC_CONVERSION_TC224 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC211(pricePlan, sclsCode);
            case OTHER_MILITARY_ETC_WAIVED_TC_217 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC217(pricePlan, sclsCode);
            case MOVING_OUTSIDE_AGLC_ETC_WAIVED_TC_218 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC218(pricePlan);
            case OTHER_DECEASED_ETC_WAIVED_TC_220, OTHER_MILITARY_ETC_WAIVED_TC_231 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC220(pricePlan);
            case OTHER_REGULATED_PROVIDER_ETC_WAIVED_TC212 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC212(pricePlan, sclsCode);
            case OTHER_RENOVATION_ELECTRIC_CONVERSION_TC221 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC221(pricePlan, sclsCode);
            case HOUSEHOLD_ACCOUNT_CHANGE_TC_214 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC214(pricePlan, sclsCode);
            case MOVING_OUTSIDE_POOL_GROUP_TC_226 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC226(pricePlan, sclsCode);
            case OTHER_MILITARY_TC209 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC209(pricePlan, sclsCode);
            case REAP_REALTOR_INSPECTION_TC210 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC210(pricePlan, sclsCode);
            case MOVING_OUTSIDE_AGLC_TC230 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC230(pricePlan, sclsCode);
            case MOVING_NOT_STAYING_WITH_GNG_TC216 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC216(pricePlan, sclsCode);
            case OTHER_REGULATED_PROVIDER_TC219 ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC219(pricePlan, sclsCode);
            default ->
                    custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC207(pricePlan, sclsCode);
        }


        String premisesCode = custPremAGLCServCode.get("GTBTRNH_PREM_CODE").toString();
        String customerCode = custPremAGLCServCode.get("GTBTRNH_CUST_CODE").toString();
        testContext.setCustomerCode(customerCode);
        String aglcAccountNumber= custPremAGLCServCode.get("GTBTRNH_AGLC_ACCT_NBR").toString();
        String aglcServiceOrderNumber= custPremAGLCServCode.get("GTRRNDN_SERV_ORD_NUM").toString();

        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setPremisesCode(premisesCode);
        payload.setCustomerCode(customerCode);
        payload.setAglcAccountNumber(aglcAccountNumber);
        payload.setAglcServiceOrderNumber(aglcServiceOrderNumber);
    }
}
