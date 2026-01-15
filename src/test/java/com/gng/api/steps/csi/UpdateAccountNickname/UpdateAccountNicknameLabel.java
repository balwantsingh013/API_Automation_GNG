package com.gng.api.steps.csi.UpdateAccountNickname;

public enum UpdateAccountNicknameLabel {

    update_account_nickname,
    set_account_nickname_mandatory,

    // NEGATIVE CASES
    TC_53__Negative__Missing_Request_ID,
    TC_54__Negative__Invalid_Request_ID__Length,
    TC_55__Negative__Duplicate_Request_ID,

    TC_56__Negative__Missing_CustomerCode,
    TC_57__Negative__Invalid_CustomerCode_Length,
    TC_58__Negative__Invalid_CustomerCode_Format__Not_String,
    TC_59__Negative__Invalid_Account_number,

    TC_60__Negative__Missing_PremisesCode,
    TC_61__Negative__Invalid_PremisesCode_Length,
    TC_62__Negative__Invalid_PremisesCode_Format__Not_String,

    TC_63__Negative__Nickname_Not_Allowed_For_New_Account,
    TC_64__Negative__Nickname_Already_Exists,
    TC_65__Negative__Nickname_Already_Exists,
    TC_66__Negative__Nickname_Already_Exists,
    TC_67__Negative__Nickname_Missing,

    // POSITIVE CASES
    TC_68__Positive__Nickname_Set__Active,
    TC_69__Positive__Nickname_Set__Final,
    TC_70__Positive__Nickname_Set__Inactive,

    TC_71__Positive__Nickname_Updated__Active,
    TC_72__Positive__Nickname_Updated__Final,
    TC_73__Positive__Nickname_Updated__Inactive,

    TC_74__Positive__Nickname_Removed__Active,
    TC_75__Positive__Nickname_Removed__Final,
    TC_76__Positive__Nickname_Removed__Inactive,

    TC_77__Positive__LoginID_Saved
}
