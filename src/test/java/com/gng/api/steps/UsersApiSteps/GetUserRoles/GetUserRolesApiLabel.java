package com.gng.api.steps.UsersApiSteps.GetUserRoles;

public enum GetUserRolesApiLabel {


    get_user_roles,
    get_user_roles_mandatory,


    //requestID
    NULL_REQUEST_ID_TC3,
    DUPLICATE_REQUEST_ID_TC4,
    LONG_REQUEST_ID_TC5,

    //LoginID
    NULL_LOGIN_ID_AND_PASSWORD_TC6,
    NULL_LOGIN_ID_TC7,
    MAX_LENGTH_LOGIN_ID_TC9,
    ALPHANUMERIC_LOGIN_ID_TC8,



    //password
    NULL_PASSWORD_TC10,
    INVALID_PASSWORD_FORMAT_NOT_ENCRYPTED_TC11,
    INVALID_PASSWORD_FORMAT_ENCRYPTED_10_CHAR_TC12,
    INVALID_PASSWORD_FORMAT_ENCRYPTED_7_CHAR_TC12_1

}
