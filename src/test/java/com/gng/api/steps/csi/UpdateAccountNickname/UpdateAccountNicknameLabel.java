package com.gng.api.steps.csi.UpdateAccountNickname;

public enum UpdateAccountNicknameLabel {

    update_account_nickname,
    set_account_nickname_mandatory,

    TC_56__Negative__Missing_Request_ID,
    TC_57__Negative__Invalid_Request_ID__Length,
    TC_58__Negative__Duplicate_Request_ID,

    TC_59__Negative__Missing_CustomerCode,
    TC_60__Negative__Invalid_CustomerCode_Length,
    TC_61__Negative__Invalid_CustomerCode_Format__Not_String,
    TC_62__Negative__Invalid_CustomerCode__Not_Found,

    TC_63__Negative__Missing_PremisesCode,
    TC_64__Negative__Invalid_PremisesCode_Length,
    TC_65__Negative__Invalid_PremisesCode_Format__Not_String,
    TC_66__Negative__Invalid_PremisesCode__Not_Found,

    TC_67__Negative__Nickname_Not_Allowed_For_New_Account,
    TC_68__Negative__Nickname_Already_Exists,
    TC_69__Negative__Nickname_Missing,

    TC_70__Positive__Nickname_Updated,
    TC_71__Positive__LoginID_Saved
}
