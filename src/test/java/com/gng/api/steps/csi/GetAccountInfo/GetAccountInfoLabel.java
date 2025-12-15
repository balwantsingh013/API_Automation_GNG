package com.gng.api.steps.csi.GetAccountInfo;

public enum GetAccountInfoLabel {

    csi_get_account_info,
    csi_get_account_info_mandatory,

    TC_185__Negative__Missing_Request_ID,
    TC_186__Negative__Invalid_Request_ID__Length,
    TC_187__Negative__Duplicate_Request_ID,
    TC_188__Negative__Invalid_customerCode_Length,
    TC_189__Negative__Invalid_customerCode_Format,
    TC_190__Negative__Invalid_premisesCode_Length,
    TC_191__Negative__Invalid_premisesCode_Format,
    TC_192__Negative__Invalid_customerCode,
    TC_193__Negative__Invalid_premisesCode,
    TC_194__Positive__Account_Info_Returned,
    TC_195__Positive__Login_ID_Saved
}
