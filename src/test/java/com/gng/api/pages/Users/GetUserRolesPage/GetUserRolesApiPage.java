package com.gng.api.pages.Users.GetUserRolesPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.Users.GetUserRoles.GetUserRolesRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.UsersApiSteps.GetUserRoles.GetUserRolesApiLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_USER_ROLES;


public class GetUserRolesApiPage extends BasePage {

    private final GetUserRolesHelper helper;

    public GetUserRolesApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetUserRolesHelper(testContext, this);
    }
    public void validateExternalParameterObjectAddedT1(GetUserRolesApiLabel apiLabel) {
        GetUserRolesRequest payload = helper.preparePayload(apiLabel);
       // helper.setRequestIDBasedOnTypeTCTC3_TC5();
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_USER_ROLES, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidRequestIDCasesTC3_TC5(GetUserRolesApiLabel apiLabel, GetUserRolesApiLabel requestID) {
        GetUserRolesRequest payload = helper.preparePayload(apiLabel);
        helper.setRequestIDBasedOnTypeTCTC3_TC5(payload, requestID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_USER_ROLES, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidLoginIDCasesTC6_TC9(GetUserRolesApiLabel apiLabel, GetUserRolesApiLabel loginID) {
        GetUserRolesRequest payload = helper.preparePayload(apiLabel);
        helper.setLoginIDBasedOnTypeTC6_TC9(payload, loginID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_USER_ROLES, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidPasswordCasesTC10_TC12(GetUserRolesApiLabel apiLabel, GetUserRolesApiLabel password) {
        GetUserRolesRequest payload = helper.preparePayload(apiLabel);
        helper.setPasswordBasedOnTypeTC10_TC12(payload, password);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_USER_ROLES, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidTestConditionCasesTC13_TC14(GetUserRolesApiLabel apiLabel, GetUserRolesApiLabel testCondition) {
        GetUserRolesRequest payload = helper.preparePayload(apiLabel);
        helper.setTestConditionBasedOnTypeTC13_TC14(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_USER_ROLES, 200);
        testContext.setResponse(response);
        if (testCondition == GetUserRolesApiLabel.PASSWORD_MISMATCH_WITH_LOGIN_ID_TC14) {
            helper.validateDatabaseForMismatchCase();
        }
    }

    public void validateInvalidTestConditionCasesTC15(GetUserRolesApiLabel apiLabel) {
        GetUserRolesRequest payload = helper.preparePayload(apiLabel);
        helper.validatePasswordNotMatchLoginIDInDB();
        helper.passwordNotMatchValue(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_USER_ROLES, 200);
        testContext.setResponse(response);
        helper.validateFailedCountAs4();
        helper.rollBackQuery();

    }

    public void validatePasswordExpiredTestConditionCasesTC16(GetUserRolesApiLabel apiLabel) {
        GetUserRolesRequest payload = helper.preparePayload(apiLabel);
        helper.validatePasswordExpiredInDB();
        helper.validatePasswordCredentials(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_USER_ROLES, 200);
        testContext.setResponse(response);
        helper.rollbackDatabaseQuery();

    }
    public void validateLockedOutLoginIDTestConditionCasesTC17(GetUserRolesApiLabel apiLabel) {
        GetUserRolesRequest payload = helper.preparePayload(apiLabel);
      helper.validateLockedOutLoginIDInDBUpdateQuery();
        helper.validatePasswordCredentials(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_USER_ROLES, 200);
        testContext.setResponse(response);
        helper.validateLockedOutLoginIDInDBRollBackQuery();
    }
    public void validateSuccessfulResponseCasesTC19(GetUserRolesApiLabel apiLabel) {
        GetUserRolesRequest payload = helper.preparePayload(apiLabel);
        helper.updateFailedLoginsQuery();
        helper.validatePasswordCredentials(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_USER_ROLES, 200);
        testContext.setResponse(response);
        helper.validateRolesCount();
        helper.validateFailedCountUserRoles();
        helper.validateFailedCountUserRollback();
    }

    public String getOrGenerateRequestID() {
        if (testContext.retrieveRequestId() != null) {
            return testContext.retrieveRequestId();
        }
        GetUserRolesRequest payload = new GetUserRolesRequest();
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_USER_ROLES, 200);
        String generatedRequestID = response.jsonPath().getString("requestID");
        testContext.storeRequestId(generatedRequestID);
        return generatedRequestID;
    }

}
