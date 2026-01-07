package com.gng.api.steps.csi.UpdateUsername;

public enum UpdateUsernameLabel {

    update_username,
    update_username_mandatory,

    TC_28__Negative__Missing_Request_ID,
    TC_29__Negative__Invalid_Request_ID__Length,
    TC_30__Negative__Duplicate_Request_ID,
    TC_31__Negative__Missing_Username,
    TC_32__Negative__Invalid_Username__Inactive,
    TC_33__Negative__Invalid_Username__Inactive,
    TC_34__Negative__Invalid_Username_format__Length___Too_Short____,
    TC_35__Negative__Invalid_Username_format__Length___Too_Long____,
    TC_36__Negative__Invalid_Username_format__Alphanumeric,
    TC_37__Negative__Missing_Password,
    TC_38__Negative__Invalid_Password_Format__Length___Too_Short____,
    TC_39__Negative__Invalid_Password_Format__Length___Too_Long____,
    TC_40__Negative__Invalid_Password_Format___Not_A_String____,
    TC_41__Negative__Invalid_Password_Format__Policy_Requirement___s____,
    TC_42__Negative__Account_username_already_exists,

    TC_43__Negative__Invalid_credentials___Password____,
    TC_44__Negative__Invalid_credentials___Username_does_not_exist____,
    TC_45__Negative__Invalid_credentials___Username_Inactive____,
    TC_46__Negative__Invalid_credentials___Username_Inactive____,
    TC_47__Negative__Missing_customerCode,
    TC_48__Negative__Invalid_customerCode_Length,
    TC_49__Negative__Invalid_customerCode_Format,
    TC_50__Negative__Invalid_customerCode,
    TC_51__Negative__Missing_premisesCode,
    TC_52__Negative__Invalid_premisesCode_Length,
    TC_53__Negative__Invalid_premisesCode_Format,
    TC_54__Positive__Username_Available___Banner_Active____,
    TC_55__Positive__Username_Available___Banner_New____,
    TC_56__Positive__Username_Available___Banner_Final____,
    TC_57__Positive__Username_Active___Banner_Active____,
    TC_58__Positive__Username_Active___Banner_Final____,
    TC_59__Positive__Username_Active___Banner_Inactive____
}
