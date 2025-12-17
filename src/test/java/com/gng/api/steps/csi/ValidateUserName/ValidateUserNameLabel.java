package com.gng.api.steps.csi.ValidateUserName;

public enum ValidateUserNameLabel {

    validate_username,
    validate_username_mandatory,

    TC_1__Negative__Missing_Request_ID,
    TC_2__Negative__Invalid_Request_ID__Length,
    TC_3__Negative__Duplicate_Request_ID,
    TC_4__Negative__Missing_Username,
    TC_5__Negative__Invalid_Username_format__Length___Too_Short____,
    TC_6__Negative__Invalid_Username_format__Length___Too_Long____,
    TC_7__Negative__Invalid_Username_format__Alphanumeric,
    TC_9__Positive__Username_Available,
    TC_8__Positive__Username_Available,
    TC_10__Positive__Username_Active__users_table,
    TC_12__Positive__Username_Inactive,
    TC_11__Positive__Username_Active__custadv_pending_registrations_table,
    TC_13__Positive__LoginID_Saved
}
