package com.gng.api.steps.csi.UpdatePassword;

public enum UpdatePasswordLabel {

    update_password,
    update_password_mandatory,

    Missing_Request_id_TC_1,
    Length_of_Request_id_larger_than_32_TC_2,
    Duplicate_Request_id_TC_3,
    Missing_Username_TC_4,
    Length_of_Username_smaller_than_5_TC_5,
    Length_of_Username_greater_than_15_TC_6,
    Username_not_Alphanumeric_TC_7,
    Username_not_found_TC_8,
    Inactive_username_TC_9,
    Missing_password_TC_10,
    Password_shorter_than_8_characters_TC_11,
    Password_longer_than_64_characters_TC_12,
    Password_matches_current_password_TC_13,
    Valid_username_and_password_TC_14
}

