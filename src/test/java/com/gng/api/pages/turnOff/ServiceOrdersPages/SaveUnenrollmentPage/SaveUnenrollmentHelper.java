package com.gng.api.pages.turnOff.ServiceOrdersPages.SaveUnenrollmentPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel;
import com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.TurnOffReason;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

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

            case OTHERS_MILITARY_TC209:
                payload.setTurnOffReason(TurnOffReason.OTHERS_MILITARY.getReason());
                payload.setTurnOffSubReason(TurnOffReason.OTHERS_MILITARY.getSubReason());
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

    public void setForwardingAddressDetailsBasedOnType(SaveUnenrollmentRequest payload,String forwardingAddressIs, String type){
        if(forwardingAddressIs.equals("NA")){
            payload.setForwardingAddressIs("NA");

            Map<String, Object> addressCityStateZip = ApplicationContext.get().getDbAction().cityStateZip();
            String city = addressCityStateZip.get("UCRADDR_CITY").toString();
            String state = addressCityStateZip.get("UCRADDR_STAT_CODE").toString();
            String zip= addressCityStateZip.get("UCRADDR_ZIP").toString();

            payload.setForwardingAddressCity(city);
            payload.setForwardingAddressStateCode(state);
            payload.setForwardingAddressZipCode(zip);

            switch(type){
                case "S":
                    payload.setForwardingAddressType("S");
                    payload.setForwardingAddressStreetNumber("609");
                    payload.setForwardingAddressStreetName("STOKESWOOD");
                    payload.setForwardingAddressStreetSuffix("AVE");
                    payload.setForwardingAddressStreetPostDirection("SE");
                    break;

                case "R":
                    payload.setForwardingAddressType("R");
                    payload.setForwardingAddressRuralRoute("RR2");
                    break;

                case "P":
                    payload.setForwardingAddressType("P");
                    payload.setForwardingAddressPOBox("2A");
                    break;

                default:

            }
        }
    }

    public void setCustomerCodePremCodeAGLCServiceNo(SaveUnenrollmentRequest payload, String pricePlan, String sclsCode) {
        Map<String, Object> custPremAGLCServCode = ApplicationContext.get().getDbAction().custCodeParamCodeAGLCAccNoServNoTC207(pricePlan, sclsCode);
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
