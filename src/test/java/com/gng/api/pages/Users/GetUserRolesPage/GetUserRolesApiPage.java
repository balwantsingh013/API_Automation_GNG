package com.gng.api.pages.Users.GetUserRolesPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pages.Users.GetUserRolesPage.GetUserRolesHelper;
import com.gng.api.pojo.Users.GetUserRoles.GetUserRolesRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.UsersApiSteps.GetUserRoles.GetUserRolesApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_ELIGIBLE_PLANS_AND_OFFERS;
import static com.gng.api.constants.ApiEndPoint.GET_USER_ROLES;


public class GetUserRolesApiPage extends BasePage {

    private final GetUserRolesHelper helper;

    public GetUserRolesApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetUserRolesHelper(testContext);
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

}
