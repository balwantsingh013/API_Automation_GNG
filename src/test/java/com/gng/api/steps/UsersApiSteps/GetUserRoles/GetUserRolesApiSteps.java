package com.gng.api.steps.UsersApiSteps.GetUserRoles;

import com.gng.api.pages.Users.GetUserRolesPage.GetUserRolesApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;

import static com.gng.api.steps.UsersApiSteps.GetUserRoles.GetUserRolesApiLabel.*;

public class GetUserRolesApiSteps {

    private final TestContext testContext;
    private final GetUserRolesApiPage getUserRolesApiPage;

    public GetUserRolesApiSteps(TestContext testContext, GetUserRolesApiPage getUserRolesApiPage) {
        this.testContext = testContext;
        this.getUserRolesApiPage = getUserRolesApiPage;
        testContext.setGetUserRolesApiPage(getUserRolesApiPage);
    }

    @When("we query the database for validate UZBPSTO_OBJECT value")
    public void query_the_database_for_validate_UZBPSTO_OBJECT_value()
    {
        getUserRolesApiPage.validate_UZBPSTO_OBJECT_Value_In_DB_TC1();
    }

    @When("we query the database for validate UZRPSTO_PARM_NAME value")
    public void query_the_database_for_validate_UZRPSTO_PARM_NAME_value()
    {
        getUserRolesApiPage.validate_UZRPSTO_PARM_NAME_Value_In_DB_TC2();
    }


    @When("a request is made to the GetUserRoles Api with {string}TC3_TC5")
    public void a_request_is_made_to_the_GetUserRoles_Api_with_TC3_TC5(String requestID) {
        getUserRolesApiPage.validateInvalidRequestIDCasesTC3_TC5(get_user_roles, GetUserRolesApiLabel.valueOf(requestID));
    }
    @When("a request is made to the GetUserRoles Api with {string}TC6_TC9")
    public void a_request_is_made_to_the_GetUserRoles_Api_with_TC6_TC9(String loginID) {
        getUserRolesApiPage.validateInvalidLoginIDCasesTC6_TC9(get_user_roles, GetUserRolesApiLabel.valueOf(loginID));
    }
    @When("a request is made to the GetUserRoles Api with {string} TC10_TC12")
    public void a_request_is_made_to_the_GetUserRoles_Api_with_TC10_TC12(String password) {
        getUserRolesApiPage.validateInvalidPasswordCasesTC10_TC12(get_user_roles, GetUserRolesApiLabel.valueOf(password));
    }

    @When("a request is made to the GetUserRoles Api with {string}TC13_TC14")
    public void a_request_is_made_to_the_GetUserRoles_Api_with_TC13_TC14(String testCondition) {
        getUserRolesApiPage.validateInvalidTestConditionCasesTC13_TC14(get_user_roles, GetUserRolesApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the GetUserRoles Api with TC15")
    public void a_request_is_made_to_the_GetUserRoles_Api_with_TC15( ) {
        getUserRolesApiPage.validateInvalidTestConditionCasesTC15(get_user_roles);
    }

    @When("a request is made to the GetUserRoles Api with TC16")
    public void a_request_is_made_to_the_GetUserRoles_Api_with_TC16( ) {
        getUserRolesApiPage.validatePasswordExpiredTestConditionCasesTC16(get_user_roles);
    }
    @When("a request is made to the GetUserRoles Api with TC17")
    public void a_request_is_made_to_the_GetUserRoles_Api_with_TC17( ) {
        getUserRolesApiPage.validateLockedOutLoginIDTestConditionCasesTC17(get_user_roles);
    }
    @When("a request is made to the GetUserRoles Api with TC19")
    public void a_request_is_made_to_the_GetUserRoles_Api_with_TC19( ) {
        getUserRolesApiPage.validateSuccessfulResponseCasesTC19(get_user_roles);
    }


}
