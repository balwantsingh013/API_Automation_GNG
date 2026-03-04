package com.gng.api.steps.csi.GetBankDraftInfo;

public enum GetBankDraftInfoLabel {

    get_bank_draft_info,
    get_bank_draft_info_mandatory,

    TC_40__Negative__Missing_Request_ID,
    TC_41__Negative__Invalid_Request_ID_Length,
    TC_42__Negative__Duplicate_Request_ID,
    TC_43__Negative__Missing_CustomerCode,
    TC_44__Negative__Invalid_CustomerCode_Length,
    TC_45__Negative__Invalid_CustomerCode_Format,
    TC_46__Negative__Missing_PremisesCode,
    TC_47__Negative__Invalid_PremisesCode_Length,
    TC_48__Negative__Invalid_PremisesCode_Format,
    TC_49__Negative__Invalid_Account_Number,

    TC_50__Positive__No_BankDraft_Info,
    TC_51__Positive__BankDraftStatus_Active,
    TC_52__Positive__BankDraftStatus_PreNotification,
    TC_53__Positive__BankDraftStatus_Canceled,
    TC_54__Positive__BankDraftStatus_Inactive,
    TC_55__Positive__BankRoutingNumber,
    TC_56__Positive__BankAccountNumber,
    TC_57__Positive__BankAccountType_Checking,
    TC_58__Positive__BankAccountType_Savings,
    TC_59__Positive__BankName
}
