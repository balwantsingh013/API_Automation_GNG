package com.gng.api.steps.csi.SetAccountNickname;

public enum SetAccountNicknameLabel {

    set_account_nickname,
    set_account_nickname_mandatory,

    Missing_request_Id_TC_1,
    Length_of_request_Id_larger_than_32_TC_2,
    Duplicate_request_Id_TC_3,
    Missing_customerCode_TC_4,
    CustomerCode_length_greater_than_9_TC_5,
    CustomerCode_not_a_string_TC_6,
    CustomerCode_not_found_in_UCBCUST_TC_7,
    Missing_premisesCode_TC_8,
    PremisesCode_length_greater_than_7_TC_9,
    PremisesCode_not_a_string_TC_10,
    PremisesCode_not_found_in_UCBPREM_TC_11,
    Nickname_not_allowed_for_new_account_TC_12,
    Nickname_already_exists_for_account_TC_13,
    Nickname_missing_for_existing_account_TC_14,
    Valid_account_with_new_nickname_TC_15
}
