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

    public void setTurnOffReasonAndSubReason(SaveUnenrollmentRequest payload, TurnOffReason reason) {

        switch (reason) {
            case SEASONAL_OR_HEAT_ONLY:
                payload.setTurnOffReason(reason.getReason());
                payload.setTurnOffSubReason(reason.getSubReason());
                break;

            case MOVING_OUTSIDE_AGLC:
                payload.setTurnOffReason(reason.getReason());
                payload.setTurnOffSubReason(reason.getSubReason());
                break;

            case OTHERS_MILITARY:
                payload.setTurnOffReason(reason.getReason());
                payload.setTurnOffSubReason(reason.getSubReason());
                break;

            case OTHERS_MILITARY_ETC_WAIVED:
                payload.setTurnOffReason(reason.getReason());
                payload.setTurnOffSubReason(reason.getSubReason());
                break;

            case REAP_REALTOR_INSPECTION:
                payload.setTurnOffReason(reason.getReason());
                payload.setTurnOffSubReason(reason.getSubReason());
                break;

            case HOUSEHOLD_ACCOUNT_CHANGE:
                payload.setTurnOffReason(reason.getReason());
                payload.setTurnOffSubReason(reason.getSubReason());
                break;

            case OTHER_FINANCIAL_SITUATION:
                payload.setTurnOffReason(reason.getReason());
                payload.setTurnOffSubReason(reason.getSubReason());
                break;

            case MOVING_NOT_STAYING_WITH_GNG:
                payload.setTurnOffReason(reason.getReason());
                payload.setTurnOffSubReason(reason.getSubReason());
                break;

            case OTHER_REGULATED_PROVIDER:
                payload.setTurnOffReason(reason.getReason());
                payload.setTurnOffSubReason(reason.getSubReason());
                break;

            case OTHER_DECEASED:
                payload.setTurnOffReason(reason.getReason());
                payload.setTurnOffSubReason(reason.getSubReason());
                break;

            case MOVING_SERVICE_TRANSFER:
                payload.setTurnOffReason(reason.getReason());
                payload.setTurnOffSubReason(reason.getSubReason());
                break;

            default:
                throw new IllegalArgumentException("Unhandled reason: " + reason);
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
