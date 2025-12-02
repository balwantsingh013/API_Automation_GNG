package com.gng.api.constants;

public final class ApiEndPoint {

    public static final String GET_ACCOUNT_INFO = "/Accounts/GetAccountInfo";
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
    public static final String SET_ACCOUNT_NICKNAME= "/Accounts/SetAccountNickname";


    private ApiEndPoint() {
    }

}
