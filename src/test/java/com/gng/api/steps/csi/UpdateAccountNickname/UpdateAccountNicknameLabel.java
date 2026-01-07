package com.gng.api.steps.csi.UpdateAccountNickname;

public enum UpdateAccountNicknameLabel {

    update_account_nickname,
    set_account_nickname_mandatory,

    TC_60__Negative__Missing_Request_ID,
    TC_61__Negative__Invalid_Request_ID__Length,
    TC_62__Negative__Duplicate_Request_ID,

    TC_63__Negative__Missing_CustomerCode,
    TC_64__Negative__Invalid_CustomerCode_Length,
    TC_65__Negative__Invalid_CustomerCode_Format__Not_String,
    TC_66__Negative__Invalid_CustomerCode__Not_Found,

    TC_67__Negative__Missing_PremisesCode,
    TC_68__Negative__Invalid_PremisesCode_Length,
    TC_69__Negative__Invalid_PremisesCode_Format__Not_String,
    TC_70__Negative__Nickname_Not_Allowed_For_New_Account,
    TC_71__Negative__Nickname_Already_Exists,
    TC_72__Negative__Nickname_Missing,

    TC_73__Positive__Nickname_Set__Active,
    TC_74__Positive__Nickname_Set__Final,
    TC_75__Positive__Nickname_Set__Inactive,
    TC_76__Positive__Nickname_Updated__Active,
    TC_77__Positive__Nickname_Updated__Final,
    TC_78__Positive__Nickname_Updated__Inactive,
    TC_79__Positive__Nickname_Removed__Active,
    TC_80__Positive__Nickname_Removed__Final,
    TC_81__Positive__Nickname_Removed__Inactive,

    TC_82__Positive__LoginID_Saved
}
