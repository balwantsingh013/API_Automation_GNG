package com.gng.api.steps.csi.UpdateUsername;

public enum UpdateUsernameLabel {

    update_username,
    update_username_mandatory,

    // NEGATIVE CASES
    TC_26__Negative__Missing_Request_ID,
    TC_27__Negative__Invalid_Request_ID__Length,
    TC_28__Negative__Duplicate_Request_ID,
    TC_29__Negative__Missing_Username,
    TC_30__Negative__Invalid_Username__Inactive,
    TC_31__Negative__Invalid_Username_format__Length___Too_Short____,
    TC_32__Negative__Invalid_Username_format__Length___Too_Long____,
    TC_33__Negative__Invalid_Username_format__Alphanumeric,
    TC_34__Negative__Missing_Password,
    TC_35__Negative__Invalid_Password_Format__Length___Too_Short____,
    TC_36__Negative__Invalid_Password_Format__Length___Too_Long____,
    TC_37__Negative__Account_username_already_exists,
    TC_38__Negative__Invalid_credentials___Password____,
    TC_39__Negative__Invalid_credentials___Username_does_not_exist____,
    TC_40__Negative__Invalid_credentials___Username_Inactive____,
    TC_41__Negative__Missing_customerCode,
    TC_42__Negative__Invalid_customerCode_Length,
    TC_43__Negative__Invalid_customerCode_Format,
    TC_44__Negative__Invalid_Account_number,
    TC_45__Negative__Missing_premisesCode,
    TC_46__Negative__Invalid_premisesCode_Length,
    TC_47__Negative__Invalid_premisesCode_Format,

    // POSITIVE CASES
    TC_48__Positive__Username_Available___Banner_Active____,
    TC_49__Positive__Username_Available___Banner_New____,
    TC_50__Positive__Username_Available___Banner_Final____,
    TC_51__Positive__Username_Active___Banner_Active____,
    TC_52__Positive__Username_Active___Banner_Final____,
    TC_53__Positive__Username_Active___Banner_Inactive____
}
