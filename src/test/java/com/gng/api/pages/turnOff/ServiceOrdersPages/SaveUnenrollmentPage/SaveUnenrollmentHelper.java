package com.gng.api.pages.turnOff.ServiceOrdersPages.SaveUnenrollmentPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel;
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

    public void setTurnOffReasonAndSubReason(SaveUnenrollmentRequest payload, String turnoffreason){
        switch (turnoffreason){
            case "Seasonal or Heat Only":
                payload.setTurnOffReason("Seasonal or Heat Only");
                payload.setTurnOffSubReason("");
                break;

            case "Moving - Outside AGLC":
                payload.setTurnOffSubReason("Outside AGLC Territory/Outside Georgia");
                payload.setTurnOffReason("Moving");
                break;

            case "Other - Military":
                payload.setTurnOffReason("Other");
                payload.setTurnOffSubReason("Military");
                break;

            case "REAP/Realtor Inspection":
                payload.setTurnOffReason("REAP/Realtor Inspection");
                payload.setTurnOffSubReason("");
                break;

            case "Household Account Change":
                payload.setTurnOffSubReason("");
                payload.setTurnOffReason("Household Account Change");
                break;

            case "Other - financial situation":
                payload.setTurnOffReason("Other");
                payload.setTurnOffSubReason("Financial Situation");
                break;

            case "Moving - Not staying with GNG":
                payload.setTurnOffReason("Moving");
                payload.setTurnOffSubReason("Within Pool Group but Not Staying with GNG");
                break;

            case "Other - Regulated Provider":
                payload.setTurnOffReason("Other");
                payload.setTurnOffSubReason("Regulated Provider");
                break;

            case "Other - Deceased":
                payload.setTurnOffReason("Other");
                payload.setTurnOffSubReason("Deceased");
                break;
        }
    }

    public void setEmailAddress(SaveUnenrollmentRequest payload,String setEmail){
        if(setEmail.equals("Yes")){
            payload.setEmailAddress(FakerDataGenerator.generateEmail());
        }
    }

    public void setEtcExists(SaveUnenrollmentRequest payload, String etcExists ){
        if(etcExists.equals("Yes")){
            payload.setEtcExists(true);
        }
    }

    public void setForwardingAddressDetailsBasedOnType(SaveUnenrollmentRequest payload,String forwardingAddressIs, String type){
        if(forwardingAddressIs.equals("NA")){
            payload.setForwardingAddressIs("NA");
            payload.setForwardingAddressCity("ATLANTA");
            payload.setForwardingAddressStateCode("GA");
            payload.setForwardingAddressZipCode("30316");

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
