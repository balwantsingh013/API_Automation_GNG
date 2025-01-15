package com.gng.api.steps.AccountsApiSteps.SearchAccounts;

public enum SearchAccountsApiLabel {

    search_accounts,
    search_accounts_mandatory,

//requestID
    NULL_REQUEST_ID_TC42,
    DUPLICATE_REQUEST_ID_TC43,
    LONG_REQUEST_ID_TC44,

    //loginID
    NULL_LOGIN_ID_TC45,
    ALPHANUMERIC_LOGIN_ID_TC47,
    MAX_LENGTH_LOGIN_ID_TC46,
    INVALID_LOGIN_ID_NOT_PRESENT_USER_TABLE_TC48,

    MAX_LENGTH_CUSTOMER_CODE_TC49,
    MAX_LENGTH_PREMISES_CODE_TC50,

    //transactionType
    MISSING_TRANSACTION_TYPE_TC51,
    INVALID_TRANSACTION_TYPE_TC52,

    MAX_LENGTH_BUSINESS_NAME_TC53

}
