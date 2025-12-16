package com.gng.api.steps.csi.UpdateUsername;

public enum UpdateUsernameLabel {

    update_username,
    update_username_mandatory,

    TC_29__Negative__Missing_Request_ID,
    TC_30__Negative__Invalid_Request_ID__Length,
    TC_31__Negative__Duplicate_Request_ID,
    TC_32__Negative__Missing_Username,
    TC_33__Negative__Invalid_Username__Inactive,
    TC_34__Negative__Invalid_Username_format__Length___Too_Short____,
    TC_35__Negative__Invalid_Username_format__Length___Too_Long____,
    TC_36__Negative__Invalid_Username_format__Alphanumeric,
    TC_37__Negative__Invalid_Username__Not_Found,
    TC_38__Negative__Missing_Password,
    TC_39__Negative__Invalid_Password_Format__Length___Too_Short____,
    TC_40__Negative__Invalid_Password_Format__Length___Too_Long____,
    TC_41__Negative__Invalid_Password_Format___Not_A_String____,
    TC_42__Negative__Invalid_Password_Format__Policy_Requirement___s____,
    TC_43__Negative__Account_username_already_exists,
    TC_44__Negative__Password_does_not_match_username,
    TC_45__Negative__Missing_customerCode,
    TC_46__Negative__Invalid_customerCode_Length,
    TC_47__Negative__Invalid_customerCode_Format,
    TC_48__Negative__Invalid_customerCode,
    TC_49__Negative__Missing_premisesCode,
    TC_50__Negative__Invalid_premisesCode_Length,
    TC_51__Negative__Invalid_premisesCode_Format,
    TC_52__Negative__Invalid_premisesCode,

    TC_53__Positive__Username_Available,
    TC_54__Positive__Username_Active,
    TC_55__Positive__Login_ID_Saved
}
