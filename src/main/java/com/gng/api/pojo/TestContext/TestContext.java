package com.gng.api.pojo.TestContext;

import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsResponse;
import com.gng.api.pojo.CSIPojo.GetBankDraftInfo.GetBankDraftInfoResponse;
import com.gng.api.pojo.CSIPojo.GetBillHistory.GetBillHistoryResponse;
import com.gng.api.pojo.CSIPojo.GetBillingInfo.GetBillingInfoResponse;
import com.gng.api.pojo.CSIPojo.GetPaymentArrangementInfo.GetPaymentArrangementInfoResponse;
import com.gng.api.pojo.CSIPojo.GetPaymentHistory.GetPaymentHistoryResponse;
import com.gng.api.pojo.CSIPojo.GetUsageHistory.GetUsageHistoryResponse;
import com.gng.api.pojo.CSIPojo.SearchAccounts.SearchAccountsResponseCSI;

import com.gng.api.pojo.CSIPojo.GetAccountInfo.GetAccountInformationResponse;
import com.gng.api.pojo.CSIPojo.GetAccountRewards.GetAccountRewardsResponse;
import com.gng.api.pojo.CSIPojo.UpdateAccountNickname.UpdateAccountNicknameResponse;
import com.gng.api.pojo.CSIPojo.UpdateBankDraft.UpdateBankDraftResponse;
import com.gng.api.pojo.CSIPojo.UpdateMailingAddress.UpdateMailingAddressResponse;
import com.gng.api.pojo.CSIPojo.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsResponse;
import com.gng.api.pojo.CSIPojo.UpdatePassword.UpdatePasswordResponse;
import com.gng.api.pojo.CSIPojo.UpdateUsername.UpdateUsernameResponse;
import com.gng.api.pojo.CSIPojo.ValidateUsername.ValidateUsernameResponse;
import com.gng.api.pojo.CommonPojo.GetReasonsForLeaving.Response.GetReasonsForLeavingResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.CommonPojo.GetMarketerCodes.response.GetMarketerCodesResponse;
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
    private String loginId;
    private String password;
    private String username;
    private String accountNickname;
    private String rewardId;
    private String activeRewardId;
    private String pendingRewardId;
    private String firstName;
    private String customerCode1;
    private String customerCode2;
    private String customerCode3;
    private String customerCode4;
    private String customerCode5;
    private String customerCode6;
    private String customerCode7;
    private String customerCode8;
    private String bankAccountNo;

    // Add type-safe getters and setters
    // Use Object type to avoid direct dependency on test classes
    @Getter @Setter
    private String customRequestPayload;

    @Getter @Setter
    private Object getAccountInfoApiPage;
    private GetAccountInfoResponse getAccountInfoResponse;
    @Getter @Setter
    private Object getAccountInformationApiPage;
    private GetAccountInformationResponse getAccountInformationResponse;
    @Getter @Setter
    private Object updateUsernameApiPage;
    private UpdateUsernameResponse updateUsernameResponse;
    @Getter @Setter
    private Object createAccountNoteApiPage;
    private CreateAccountNoteResponse createAccountNoteResponse;
    @Getter @Setter
    private Object saveEnrollmentApiPage;
    private SaveEnrollmentResponse saveEnrollmentResponse;
    @Getter @Setter
    private Object getUsageHistoryApiPage;
    private GetUsageHistoryResponse getUsageHistoryResponse;
    @Getter @Setter
    private Object getBillHistoryApiPage;
    private GetBillHistoryResponse getBillHistoryResponse;
    @Getter @Setter
    private Object getBillingInfoApiPage;
    private GetBillingInfoResponse getBillingInfoResponse;
    @Getter @Setter
    private Object getPaymentHistoryApiPage;
    private GetPaymentHistoryResponse getPaymentHistoryResponse;
    @Getter @Setter
    private Object getPaymentArrangementInfoApiPage;
    private GetPaymentArrangementInfoResponse getPaymentArrangementInfoResponse;
    @Getter @Setter
    private Object getMarketerReferenceDataApiPage;
    private SaveEnrollmentResponse getMarketerReferenceDataResponse;
    @Getter @Setter
    private Object getBankDraftInfoApiPage;
    private GetBankDraftInfoResponse getBankDraftInfoResponse;
    @Getter @Setter
    private Object updateBankDraftApiPage;
    private UpdateBankDraftResponse updateBankDraftResponse;
    @Getter @Setter
    private Object getReasonsForLeavingApiPage;
    private GetReasonsForLeavingResponse getReasonsForLeavingResponse;
    @Getter @Setter
    private Object updateMailingAddressApiPage;
    private UpdateMailingAddressResponse updateMailingAddressResponse;
    @Getter @Setter
    private Object saveUnenrollmentApiPage;
    private SaveUnenrollmentResponse saveUnenrollmentResponse;
    @Getter @Setter
    private Object getAccountRewardsApiPage;
    private GetAccountRewardsResponse getAccountRewardsResponse;
    @Getter @Setter
    private Object validateUsernameApiPage;
    private ValidateUsernameResponse validateUsernameResponse;
    @Getter @Setter
    private Object updatePaperlessCommunicationsApiPage;
    private UpdatePaperlessCommunicationsResponse updatePaperlessCommunicationsResponse;
    @Getter @Setter
    private Object updatePasswordApiPage;
    private UpdatePasswordResponse updatePasswordResponse;
    @Getter @Setter
    private Object updateAccountNicknameApiPage;
    private UpdateAccountNicknameResponse updateAccountNicknameResponse;
    @Getter @Setter
    private Object getEligiblePlansAndOffersApiPage;
    private GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse;
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
    private Object searchAccountsApiPageCSI;
    private SearchAccountsResponseCSI searchAccountsResponseCSI;
    @Getter @Setter
    private Object getMarketerCodesApiPage;
    private GetMarketerCodesResponse getMarketerCodesResponse;

    public void storeRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String retrieveRequestId() {
        return this.requestId;
    }

}
