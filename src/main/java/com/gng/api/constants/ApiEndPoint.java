package com.gng.api.constants;

public final class ApiEndPoint {

    public static final String GET_ACCOUNT_INFO = "/Accounts/GetAccountInfo";
    public static final String CSI_GET_ACCOUNT_INFO = "/Accounts/GetAccountInfo";

    public static final String CREATE_ACCOUNT_NOTE = "/Accounts/CreateAccountNote";
    public static final String SEARCH_ACCOUNTS = "/Accounts/SearchAccounts";

    public static final String SAVE_ENROLLMENT = "/ServiceOrders/Enrollment/SaveEnrollment";
    public static final String SAVE_UNENROLLMENT= "/ServiceOrders/Unenrollment/SaveUnenrollment";
    public static final String GET_ELIGIBLE_PLANS_AND_OFFERS = "/ServiceOrders/Enrollment/GetEligiblePlansAndOffers";
    public static final String GET_DEFAULT_PLANS_AND_OFFERS = "/ServiceOrders/Enrollment/GetDefaultPlansAndOffers";
    public static final String GET_PREPAY_PLANS_REQUOTE = "/ServiceOrders/Enrollment/GetPrepayPlansRequote";
    public static final String GET_USER_ROLES = "Users/GetUserRoles";
    public static final String RESET_PASSWORD = "Users/ResetPassword";

    public static final String AES_ENCRYPTION = "/api/AesEncryption/Encrypt";
    public static final String AES_DECRYPTION = "/api/AesEncryption/Decrypt";

    public static final String GET_MARKETER_REFERENCE_DATA= "/Common/GetMarketerReferenceData";
    public static final String GET_REASONS_FOR_LEAVING ="/Common/GetReasonsForLeaving";
    public static final String GET_MARKETER_CODES = "/Common/GetMarketerCodes";


    public static final String VALIDATE_USERNAME= "/CustomerService/ValidateUsername";
    public static final String UPDATE_PASSWORD= "/CustomerService/UpdatePassword";
    public static final String UPDATE_ACCOUNT_NICKNAME= "/Accounts/UpdateAccountNickname";
    public static final String UPDATE_MAILING_ADDRESS= "/Accounts/UpdateMailingAddress";
    public static final String GET_ACCOUNT_REWARDS= "/Accounts/GetAccountRewards";
    public static final String UPDATE_USERNAME="/CustomerService/UpdateUsername";

    public static final String SEARCH_ACCOUNTS2 = "/CustomerService/SearchAccounts";
    public static final String GET_USAGE_HISTORY="/CustomerService/GetUsageHistory";
    public static final String GET_BANK_DRAFT_INFO="/CustomerService/GetBankDraftInfo";
    public static final String GET_BILL_HISTORY="/CustomerService/GetBillHistory";
    public static final String UPDATE_BANK_DRAFT="/CustomerService/UpdateBankDraft";
    public static final String GET_PAYMENT_HISTORY="/CustomerService/GetPaymentHistory";


    private ApiEndPoint() {
    }

}
