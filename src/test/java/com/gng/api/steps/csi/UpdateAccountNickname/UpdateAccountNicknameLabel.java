package com.gng.api.steps.csi.UpdateAccountNickname;

public enum UpdateAccountNicknameLabel {

    update_account_nickname,
    set_account_nickname_mandatory,

    // NEGATIVE CASES
    TC_55__Negative__Missing_Request_ID,
    TC_56__Negative__Invalid_Request_ID__Length,
    TC_57__Negative__Duplicate_Request_ID,

    TC_58__Negative__Missing_CustomerCode,
    TC_59__Negative__Invalid_CustomerCode_Length,
    TC_60__Negative__Invalid_CustomerCode_Format,
    TC_61__Negative__Invalid_Account_Number,

    TC_62__Negative__Missing_PremisesCode,
    TC_63__Negative__Invalid_PremisesCode_Length,
    TC_64__Negative__Invalid_PremisesCode_Format,

    TC_65__Negative__Nickname_Not_Allowed_For_New_Account,
    TC_66__Negative__Nickname_Already_Exists__Active,
    TC_67__Negative__Nickname_Already_Exists__Final,
    TC_68__Negative__Nickname_Already_Exists__Inactive,
    TC_69__Negative__Nickname_Missing,

    // POSITIVE CASES
    TC_70__Positive__Nickname_Set__Active,
    TC_71__Positive__Nickname_Set__Final,
    TC_72__Positive__Nickname_Set__Inactive,

    TC_73__Positive__Nickname_Updated__Active,
    TC_74__Positive__Nickname_Updated__Final,
    TC_75__Positive__Nickname_Updated__Inactive,

    TC_76__Positive__Nickname_Removed__Active,
    TC_77__Positive__Nickname_Removed__Final,
    TC_78__Positive__Nickname_Removed__Inactive,

    TC_79__Positive__LoginID_Saved
}
