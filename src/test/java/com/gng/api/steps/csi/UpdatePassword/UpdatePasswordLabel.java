package com.gng.api.steps.csi.UpdatePassword;

public enum UpdatePasswordLabel {

    update_password,
    update_password_mandatory,

    TC_12__Negative__Missing_Request_ID,
    TC_13__Negative__Invalid_Request_ID__Length,
    TC_14__Negative__Duplicate_Request_ID,
    TC_15__Negative__Missing_Username,
    TC_16__Negative__Invalid_Username_format__Length___Too_Short____,
    TC_17__Negative__Invalid_Username_format__Length___Too_Long____,
    TC_18__Negative__Invalid_Username_format__Alphanumeric,
    TC_19__Negative__Invalid_Username__Not_Found,
    TC_20__Negative__Invalid_Username__Inactive,
    TC_21__Negative__Missing_Password,
    TC_22__Negative__Invalid_Password_Format__Length___Too_Short____,
    TC_23__Negative__Invalid_Password_Format__Length___Too_Long____,
    TC_24__Negative__Invalid_Password__Reused_Password,
    TC_24__Negative__Invalid_Password__Reused_Password_2,
    TC_25__Positive__Password__Updated,
}
