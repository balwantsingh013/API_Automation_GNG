package com.gng.api.steps.csi.GetPaymentHistory;

public enum GetPaymentHistoryLabel {

    get_payment_history,
    get_payment_history_mandatory,

    // ---------------- NEGATIVE TEST CASES ----------------
    TC_126__Negative__Missing_Request_ID,
    TC_127__Negative__Invalid_Request_ID_Length,
    TC_128__Negative__Duplicate_Request_ID,

    TC_129__Negative__Missing_customerCode,
    TC_130__Negative__Invalid_customerCode_Length,
    TC_131__Negative__Invalid_customerCode_Format,

    TC_132__Negative__Missing_premisesCode,
    TC_133__Negative__Invalid_premisesCode_Length,
    TC_134__Negative__Invalid_premisesCode_Format,

    TC_135__Negative__Invalid_Account_Number,

    TC_136__Negative__Missing_Number_of_Months,
    TC_137__Negative__Invalid_Number_of_Months_Format__Not_a_Number,
    TC_138__Negative__Invalid_Number_of_Months_Format__Zero_Value,
    TC_139__Negative__Invalid_Number_of_Months_Format__Negative_Number,
    TC_140__Negative__Invalid_Number_of_Months_Length,

    // ---------------- POSITIVE TEST CASES ----------------
    TC_141__Positive__Payment_Date_Format,
    TC_142__Positive__Payment_Amount_Format,
    TC_143__Positive__Payment_Code_Format,
    TC_144__Positive__Payment_Description_Format,

    TC_145__Positive__No_Payment_History_New,
    TC_146__Positive__No_Payment_History_Active_Final_Inactive,

    TC_147__Positive__Valid_NumberOfMonths_PaymentHistory_Too_Old,
    TC_148__Positive__Valid_NumberOfMonths_Less_Than_Requested,
    TC_149__Positive__Valid_NumberOfMonths_Equals_Requested,
    TC_150__Positive__Valid_NumberOfMonths_Greater_Than_Requested,

    TC_151__Positive__Posted_Reversal_Payment,
    TC_152__Positive__Not_Posted_Reversal_Payment,
    TC_153__Positive__Posted_Payments,
    TC_154__Positive__Pending_Payments,
    TC_155__Positive__Posted_and_Pending_Payments
}
