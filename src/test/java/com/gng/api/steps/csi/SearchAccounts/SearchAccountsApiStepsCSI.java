package com.gng.api.steps.csi.SearchAccounts;

import com.gng.api.pages.csi.SearchAccountsPage.SearchAccountsPageCSI;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.DualReportManager;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.gng.api.steps.csi.SearchAccounts.SearchAccountsLabelCSI.search_accounts_csi;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
public class SearchAccountsApiStepsCSI {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final SearchAccountsPageCSI searchAccountsPageCSI;

    public SearchAccountsApiStepsCSI(TestContext testContext, SearchAccountsPageCSI searchAccountsPageCSI) {
        this.testContext = testContext;
        this.searchAccountsPageCSI = searchAccountsPageCSI;
        testContext.setSearchAccountsApiPage(searchAccountsPageCSI);
        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to SearchAccounts Api for {string}")
    public void a_request_is_made_to_the_SearchAccounts_Api_for(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        searchAccountsPageCSI.validateResponseForTestConditions(
                search_accounts_csi,
                SearchAccountsLabelCSI.valueOf(testCondition)
        );
    }

    /**
     * TC_297 part 1: remember the username the first search path returned for the account under
     * test, so the second path can be compared against it.
     */
    @And("capture the SearchAccounts username for the searched account")
    public void capture_the_searchaccounts_username_for_the_searched_account() {
        String username = usernameForSearchedAccount("capture");
        testContext.setUsername(username);
        DualReportManager.logInfo(
                "TC_297: captured username '" + username + "' for "
                        + testContext.getCustomerCode() + "/" + testContext.getPremisesCode()
                        + " from the first search path");
    }

    /**
     * TC_297 part 2 (FTD): the same account retrieved through a second valid search combination
     * must report the same username. Previously the only assertion was errorCode=0, so the
     * username was never actually compared and the test could not fail for the reason it exists.
     */
    @And("the SearchAccounts username should match across both search paths")
    public void the_searchaccounts_username_should_match_across_both_search_paths() {
        String expected = testContext.getUsername();
        assertThat("TC_297: no username captured from the first search path — "
                        + "the capture step must run before this one",
                expected, notNullValue());

        String actual = usernameForSearchedAccount("compare");
        DualReportManager.logInfo(
                "TC_297: second search path returned username '" + actual + "' for "
                        + testContext.getCustomerCode() + "/" + testContext.getPremisesCode());
        assertThat(
                "TC_297: username must be identical across both search paths for "
                        + testContext.getCustomerCode() + "/" + testContext.getPremisesCode(),
                actual, equalTo(expected));
    }

    /** Username of the account matching the TestContext customer/premises code in the last response. */
    private String usernameForSearchedAccount(String phase) {
        String customerCode = testContext.getCustomerCode();
        String premisesCode = testContext.getPremisesCode();
        assertThat("TC_297 (" + phase + "): TestContext has no customerCode — "
                        + "the first search path must run first", customerCode, notNullValue());

        List<Map<String, Object>> accounts =
                testContext.getResponse().jsonPath().getList("data.accounts");
        assertThat("TC_297 (" + phase + "): response returned no accounts", accounts, notNullValue());

        for (Map<String, Object> account : accounts) {
            if (sameCode(account.get("customerCode"), customerCode)
                    && sameCode(account.get("premisesCode"), premisesCode)) {
                Object username = account.get("username");
                return username == null ? "" : username.toString();
            }
        }
        throw new AssertionError(
                "TC_297 (" + phase + "): account " + customerCode + "/" + premisesCode
                        + " was not present in the response (" + accounts.size() + " account(s) returned)");
    }

    /**
     * Compares account codes ignoring leading zeros.
     * <p>
     * TC_294 derives the codes with {@code accountNo.substring(0, 9)} on the MariaDB
     * account_number, which keeps the padding (e.g. {@code 006093055}), while the API returns the
     * unpadded form ({@code 6093055}). A plain string equals never matches.
     */
    private static boolean sameCode(Object responseValue, String contextValue) {
        return stripLeadingZeros(responseValue == null ? null : responseValue.toString())
                .equals(stripLeadingZeros(contextValue));
    }

    private static String stripLeadingZeros(String code) {
        if (code == null) {
            return "";
        }
        String trimmed = code.trim();
        return trimmed.replaceFirst("^0+(?=.)", "");
    }

    @When("perform rollback for maxlimit")
    public void perform_rollback_operation_maxlimit(){
        searchAccountsPageCSI.performRollbackMaxlimit();
    }

    @When("performs rollback operation")
    public void perform_rollback_operation(){
        searchAccountsPageCSI.performRollback();
    }
}
