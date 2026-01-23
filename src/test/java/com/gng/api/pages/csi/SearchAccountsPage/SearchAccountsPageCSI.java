package com.gng.api.pages.csi.SearchAccountsPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.SearchAccounts.SearchAccountsRequestCSI;
import com.gng.api.pojo.CSIPojo.SearchAccounts.SearchAccountsResponseCSI;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.SearchAccounts.SearchAccountsLabelCSI;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.SEARCH_ACCOUNTS2;

public class SearchAccountsPageCSI extends BasePage {

    private final SearchAccountsApiHelperCSI helper;

    public SearchAccountsPageCSI(TestContext testContext) {
        super(testContext);
        this.helper = new SearchAccountsApiHelperCSI(testContext);
    }

    public void validateResponseForTestConditions(SearchAccountsLabelCSI apiLabel, SearchAccountsLabelCSI testCondition) {
        // Build base payload
        SearchAccountsRequestCSI payload = helper.preparePayload(apiLabel);

        // Apply negative test condition modifications
        helper.preparePayloadForTestCondition(payload, testCondition);

        // Set request spec with token
        setRequestSpecification(payload, testContext.getAuthToken());

        // Execute API call
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS2, 200);

        // Deserialize response
        SearchAccountsResponseCSI searchAccountsResponse =
                deserializeResponseToPojo(response, SearchAccountsResponseCSI.class);

        // Store in test context
        testContext.setSearchAccountsResponseCSI(searchAccountsResponse);
        testContext.setResponse(response);
    }
}
