package com.gng.api.steps.csi.GetBankDraftInfo;

public enum GetBankDraftInfoLabel {

    get_bank_draft_info,
    get_bank_draft_info_mandatory,

    TC1__Negative__Missing_Request_ID,
    TC_2__Negative__Invalid_Request_ID_Length,
    TC3__Negative__Duplicate_Request_ID,
    TC_4__Negative__Missing_CustomerCode,
    TC_5__Negative__Invalid_CustomerCode_Length,
    TC_6__Negative__Invalid_CustomerCode_Format,
    TC_7__Negative__Missing_PremisesCode,
    TC_8__Negative__Invalid_PremisesCode_Length,
    TC_9__Negative__Invalid_PremisesCode_Format,
    TC10__Negative__Invalid_Account_Number,

    TC_11__Positive__No_BankDraft_Info,
    TC_12__Positive__BankDraftStatus_Active,
    TC_13__Positive__BankDraftStatus_PreNotification,
    TC_14__Positive__BankDraftStatus_Canceled,
    TC_15__Positive__BankDraftStatus_Inactive,
    TC_16__Positive__BankRoutingNumber,
    TC_17__Positive__BankAccountNumber,
    TC_18__Positive__BankAccountType_Checking,
    TC_19__Positive__BankAccountType_Savings,
    TC_20__Positive__BankName
}
