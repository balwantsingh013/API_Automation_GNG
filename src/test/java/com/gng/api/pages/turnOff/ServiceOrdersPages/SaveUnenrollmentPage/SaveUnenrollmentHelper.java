package com.gng.api.pages.turnOff.ServiceOrdersPages.SaveUnenrollmentPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel;
import com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.TurnOffReason;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

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

    public void setRequestIDBasedOnType(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel requestID) {
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
        }
    }

    public void setTurnOffReasonAndSubReason(SaveUnenrollmentRequest payload, SaveUnenrollmentApiLabel testCondition) {
        switch (testCondition) {
            case SEASONAL_OR_HEAT_ONLY_TC223:
            case SEASONAL_OR_HEAT_ONLY_TC207:
                payload.setTurnOffReason(TurnOffReason.SEASONAL_OR_HEAT_ONLY.getReason());
                payload.setTurnOffSubReason(TurnOffReason.SEASONAL_OR_HEAT_ONLY.getSubReason());
                break;

            case MOVING_OUTSIDE_AGLC_TC230:
            case MOVING_OUTSIDE_AGLC_TC208:
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

            default:
                throw new IllegalArgumentException("Unhandled reason: " + testCondition);
        }
    }


    public void setEmailAddress(SaveUnenrollmentRequest payload,Boolean setEmail){
        if(setEmail){
            payload.setEmailAddress(FakerDataGenerator.generateEmail());
        }
    }

    public void setEtcExists(SaveUnenrollmentRequest payload, Boolean etcExists ){
            payload.setEtcExists(etcExists);
    }

    public void setMarketerReferenceData(SaveUnenrollmentRequest payload, long marketerReferenceData){
        payload.setMarketerReferenceData(marketerReferenceData);
    }

    public void setForwardingAddressDetailsBasedOnType(SaveUnenrollmentRequest payload,SaveUnenrollmentApiLabel forwardingAddressIs, SaveUnenrollmentApiLabel type){
        if(forwardingAddressIs.equals(NEW_ADDRESS)){
            payload.setForwardingAddressIs("NA");

            Map<String, Object> addressDetails = ApplicationContext.get().getDbAction().getAddressDetails();
            String city = addressDetails.get("UCRADDR_CITY").toString();
            String state = addressDetails.get("UCRADDR_STAT_CODE").toString();
            String zip= addressDetails.get("UCRADDR_ZIP").toString();

            payload.setForwardingAddressCity(city);
            payload.setForwardingAddressStateCode(state);
            payload.setForwardingAddressZipCode(zip);

            switch(type){
                case ADDRESS_TYPE_STREET:
                    payload.setForwardingAddressType("S");
                    payload.setForwardingAddressStreetNumber(addressDetails.get("UCRADDR_STREET_NUMBER").toString());
                    payload.setForwardingAddressStreetName(addressDetails.get("UCRADDR_STREET_NAME").toString());
                    payload.setForwardingAddressStreetSuffix(addressDetails.get("UCRADDR_SSFX_CODE").toString());
                    payload.setForwardingAddressStreetPostDirection(addressDetails.get("UCRADDR_PDIR_CODE_POST").toString());
                    payload.setForwardingAddressStreetPreDirection(addressDetails.get("UCRADDR_PDIR_CODE_PRE").toString());
                    break;

                case ADDRESS_TYPE_RURAL:
                    payload.setForwardingAddressType("R");
                    payload.setForwardingAddressRuralRoute(FakerDataGenerator.generateAlphanumeric(3));
                    break;

                case ADDRESS_TYPE_POBOX:
                    payload.setForwardingAddressType("P");
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
        String aglcAccountNumber= custPremAGLCServCode.get("GTBTRNH_AGLC_ACCT_NBR").toString();
        String aglcServiceOrderNumber= custPremAGLCServCode.get("GTRRNDN_SERV_ORD_NUM").toString();

        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setPremisesCode(premisesCode);
        payload.setCustomerCode(customerCode);
        payload.setAglcAccountNumber(aglcAccountNumber);
        payload.setAglcServiceOrderNumber(aglcServiceOrderNumber);
    }
}
