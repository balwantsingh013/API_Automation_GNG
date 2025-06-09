package com.gng.api.constants;

public final class ApiEndPoint {

    public static final String GET_ACCOUNT_INFO = "/Accounts/GetAccountInfo";
    public static final String CREATE_ACCOUNT_NOTE = "/Accounts/CreateAccountNote";
    public static final String SEARCH_ACCOUNTS = "/Accounts/SearchAccounts";

    public static final String SAVE_ENROLLMENT = "/ServiceOrders/Enrollment/SaveEnrollment";
    public static final String SAVE_UNENROLLMENT= "/ServiceOrders/Unenrollment/SaveUnenrollment";
    public static final String GET_ELIGIBLE_PLANS_AND_OFFERS = "/ServiceOrders/Enrollment/GetEligiblePlansAndOffers";
    public static final String GET_USER_ROLES = "Users/GetUserRoles";
    public static final String RESET_PASSWORD = "Users/ResetPassword";

    public static final String AES_ENCRYPTION = "/api/AesEncryption/Encrypt";
    public static final String AES_DECRYPTION = "/api/AesEncryption/Decrypt";


    private ApiEndPoint() {
    }

}
