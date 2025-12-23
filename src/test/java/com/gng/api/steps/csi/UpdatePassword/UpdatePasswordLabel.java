package com.gng.api.steps.csi.UpdatePassword;

public enum UpdatePasswordLabel {

    update_password,
    update_password_mandatory,

    TC_13__Negative__Missing_Request_ID,
    TC_14__Negative__Invalid_Request_ID__Length,
    TC_15__Negative__Duplicate_Request_ID,
    TC_16__Negative__Missing_Username,
    TC_17__Negative__Invalid_Username_format__Length___Too_Short____,
    TC_18__Negative__Invalid_Username_format__Length___Too_Long____,
    TC_19__Negative__Invalid_Username_format__Alphanumeric,
    TC_20__Negative__Invalid_Username__Not_Found,
    TC_21__Negative__Invalid_Username__Inactive,
    TC_22__Negative__Missing_Password,
    TC_23__Negative__Invalid_Password_Format__Length___Too_Short____,
    TC_24__Negative__Invalid_Password_Format__Length___Too_Long____,
    TC_25__Negative__Invalid_Password__Reused_Password,

    TC_26__Positive__Password__Updated,
    TC_27__Positive__LoginID_Saved,
    TC_26_DB_Validation
}
