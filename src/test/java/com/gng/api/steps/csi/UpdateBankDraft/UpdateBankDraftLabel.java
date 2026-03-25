package com.gng.api.steps.csi.UpdateBankDraft;

public enum UpdateBankDraftLabel {

    update_bank_draft,
    update_bank_draft_mandatory,

    // Negative Test Cases
    TC_97__Negative__Missing_Request_ID,
    TC_98__Negative__Invalid_Request_ID_Length,
    TC_99__Negative__Duplicate_Request_ID,

    TC_100__Negative__Missing_customerCode,
    TC_101__Negative__Invalid_customerCode_Length,
    TC_102__Negative__Invalid_customerCode_Format,

    TC_103__Negative__Missing_premisesCode,
    TC_104__Negative__Invalid_premisesCode_Length,
    TC_105__Negative__Invalid_premisesCode_Format,

    TC_106__Negative__Invalid_Account_Number,

    TC_107__Negative__Missing_Bank_Account_Type,
    TC_108__Negative__Invalid_Bank_Account_Type_Format,

    TC_109__Negative__Missing_Routing_Number,
    TC_110__Negative__Invalid_Routing_Number_Format,
    TC_111__Negative__Invalid_Routing_Number_Length,
    TC_112__Negative__Invalid_Routing_Number,

    TC_113__Negative__Missing_Bank_Account_Number,
    TC_114__Negative__Invalid_Bank_Account_Number_Format,
    TC_115__Negative__Invalid_Bank_Account_Number_Length,

    TC_116__Negative__No_Active_ABD_Configured,
    TC_117__Negative__Invalid_Service_Number,
    TC_118__Negative__Active_Payment_Arrangement,
    TC_119__Negative__Account_Ineligible_Future_Payment,
    TC_120__Negative__No_Update_Required,

    // Positive Test Cases
    TC_121__Positive__Active_Checking_Account_Updated,
    TC_122__Positive__Active_Savings_Account_Updated,
    TC_123__Positive__Prenotification_Checking_Updated,
    TC_124__Positive__Prenotification_Savings_Updated,
    TC_125__Positive__Login_ID_Saved
}
