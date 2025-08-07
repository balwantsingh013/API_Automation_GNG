package com.gng.api.pojo.TestContext;

import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetPrepayPlansRequote.GetPrepayPlansRequoteResponse;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentResponse;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentResponse;
import com.gng.api.pojo.Users.GetUserRoles.GetUserRolesResponse;
import com.gng.api.pojo.Users.ResetPassword.ResetPasswordResponse;
import io.restassured.response.Response;
import lombok.Data;
import com.gng.api.pojo.AccountsPojo.getAccountInfo.GetAccountInfoResponse;
import com.gng.api.pojo.AccountsPojo.createAccountNote.CreateAccountNoteResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class TestContext {
    private Response response;
    private String authToken;
    private String customerCode;
    private String premisesCode;
    private String noteSequenceNumber;
    private String requestId;
    private String serviceNumber;
    private String noteTypeCode;
    private String noteText;
    private String origin;
    private String expirationDate;
    private String suspenseDate;
    private String userIDRemind;
    private Long marketerReferenceData;

    // Add type-safe getters and setters
    // Use Object type to avoid direct dependency on test classes
    @Getter @Setter
    private String customRequestPayload;

    @Getter @Setter
    private Object getAccountInfoApiPage;
    private GetAccountInfoResponse getAccountInfoResponse;
    @Getter @Setter
    private Object createAccountNoteApiPage;
    private CreateAccountNoteResponse createAccountNoteResponse;
    @Getter @Setter
    private Object saveEnrollmentApiPage;
    private SaveEnrollmentResponse saveEnrollmentResponse;
    @Getter @Setter
    private Object getMarketerReferenceDataApiPage;
    private SaveEnrollmentResponse getMarketerReferenceDataResponse;
    @Getter @Setter
    private Object getReasonsForLeavingApiPage;
    private SaveEnrollmentResponse getReasonsForLeavingResponse;
    @Getter @Setter
    private Object saveUnenrollmentApiPage;
    private SaveUnenrollmentResponse saveUnenrollmentResponse;
    @Getter @Setter
    private Object getEligiblePlansAndOffersApiPage;
    private GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse;
    @Getter @Setter
    private List<GetEligiblePlansAndOffersResponse.Plan> getValidationEligiblePlansAndOffersPlans;
    @Getter @Setter
    private Object getDefaultPlansAndOffersApiPage;
    private GetDefaultPlansAndOffersResponse getDefaultPlansAndOffersResponse;
    @Getter @Setter
    private List<GetDefaultPlansAndOffersResponse.Plan> getValidationDefaultPlansAndOffersPlans;
    @Getter @Setter
    private Object getPrepayPlansRequoteApiPage;
    private GetPrepayPlansRequoteResponse GetPrepayPlansRequoteResponse;
    @Getter @Setter
    private Object getUserRolesApiPage;
    private GetUserRolesResponse getUserRolesResponse;
    @Getter @Setter
    private Object resetPasswordApiPage;
    private ResetPasswordResponse resetPasswordResponse;
    @Getter @Setter
    private Object searchAccountsApiPage;
    private SearchAccountsResponse searchAccountsResponse;
    @Getter @Setter
    private String transactionId;

    public void storeRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String retrieveRequestId() {
        return this.requestId;
    }

}
