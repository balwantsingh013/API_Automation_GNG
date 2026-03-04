package com.gng.api.steps.csi.UpdateBankDraft;

public enum UpdateBankDraftLabel {

    update_bank_draft,
    update_bank_draft_mandatory,

    // Negative Test Cases
    TC_50__Negative__Missing_Request_ID,
    TC_51__Negative__Invalid_Request_ID_Length,
    TC_52__Negative__Duplicate_Request_ID,

    TC_53__Negative__Missing_customerCode,
    TC_54__Negative__Invalid_customerCode_Length,
    TC_55__Negative__Invalid_customerCode_Format,

    TC_56__Negative__Missing_premisesCode,
    TC_57__Negative__Invalid_premisesCode_Length,
    TC_58__Negative__Invalid_premisesCode_Format,

    TC_59__Negative__Invalid_Account_Number,

    TC_60__Negative__Missing_Bank_Account_Type,
    TC_61__Negative__Invalid_Bank_Account_Type_Format,

    TC_62__Negative__Missing_Routing_Number,
    TC_63__Negative__Invalid_Routing_Number_Format,
    TC_64__Negative__Invalid_Routing_Number_Length,
    TC_65__Negative__Invalid_Routing_Number,

    TC_66__Negative__Missing_Bank_Account_Number,
    TC_67__Negative__Invalid_Bank_Account_Number_Format,
    TC_68__Negative__Invalid_Bank_Account_Number_Length,

    TC_69__Negative__No_Active_ABD_Configured,
    TC_70__Negative__Invalid_Service_Number,
    TC_71__Negative__Active_Payment_Arrangement,
    TC_72__Negative__Account_Ineligible_Future_Payment,
    TC_73__Negative__No_Update_Required,

    // Positive Test Cases
    TC_74__Positive__Active_Checking_Account_Updated,
    TC_75__Positive__Active_Savings_Account_Updated,
    TC_76__Positive__Prenotification_Checking_Updated,
    TC_77__Positive__Prenotification_Savings_Updated,
    TC_78__Positive__Login_ID_Saved
}
