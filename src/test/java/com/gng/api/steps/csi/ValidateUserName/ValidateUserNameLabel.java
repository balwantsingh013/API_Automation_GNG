package com.gng.api.steps.csi.ValidateUserName;

public enum ValidateUserNameLabel {

    validate_username,

    Missing_request_id_TC_1,
    Length_of_request_id_larger_than_32_TC_2,
    Invalid_request_id_format_TC_3,
    Duplicate_request_id_TC_4,
    Missing_username_TC_5,
    Length_of_username_smaller_than_5_TC_6,
    Length_of_username_greater_than_15_TC_7,
    Username_not_alphanumeric_TC_8
}
