package com.gng.api.steps.csi.ValidateUserName;

public enum ValidateUserNameLabel {

    validate_username,
    validate_username_mandatory,

    Missing_request_id_TC_1,
    Length_of_request_id_larger_than_32_TC_2,
    Duplicate_request_id_TC_3,
    Missing_username_TC_4,
    Length_of_username_smaller_than_5_TC_5,
    Length_of_username_greater_than_15_TC_6,
    Username_not_alphanumeric_TC_7,
    Username_does_not_exist_in_mariadb_TC_9,
    Username_exists_in_mariadb_AVAILABLE_TC_8,
    Username_exists_in_mariadb__ACTIVE_TC_10,
    Inactive_username_exists_in_mariadb_TC_12,
    Username_exists_in_custadv_pending_registrations_table_TC_11
}
