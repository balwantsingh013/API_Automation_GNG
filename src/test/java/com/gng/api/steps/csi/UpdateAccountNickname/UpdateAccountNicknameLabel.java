package com.gng.api.steps.csi.UpdateAccountNickname;

public enum UpdateAccountNicknameLabel {

    update_account_nickname,
    set_account_nickname_mandatory,

    TC_53__Negative__Missing_Request_ID,
    TC_54__Negative__Invalid_Request_ID__Length,
    TC_55__Negative__Duplicate_Request_ID,
    TC_56__Negative__Missing_customerCode,
    TC_57__Negative__Invalid_customerCode_Length,
    TC_58__Negative__Invalid_customerCode_Format,
    TC_59__Negative__Invalid_customerCode,
    TC_60__Negative__Missing_premisesCode,
    TC_61__Negative__Invalid_premisesCode_Length,
    TC_62__Negative__Invalid_premisesCode_Format,
    TC_63__Negative__Invalid_premisesCode,
    TC_64__Negative__Nickname_Not_Allowed,
    TC_65__Negative__Nickname_Already_Exists,
    TC_66__Negative__Nickname_Missing,
    TC_67__Positive__Nickname_Set
}
