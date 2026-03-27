package com.gng.api.steps.csi.GetPaymentArrangementInfo;

public enum GetPaymentArrangementInfoLabel {

    get_payment_arrangement_info,
    get_payment_arrangement_info_mandatory,

    // ---------------- NEGATIVE TEST CASES ----------------
    TC_156__Negative__Missing_Request_ID,
    TC_157__Negative__Invalid_Request_ID_Length,
    TC_158__Negative__Duplicate_Request_ID,

    TC_159__Negative__Missing_customerCode,
    TC_160__Negative__Invalid_customerCode_Length,
    TC_161__Negative__Invalid_customerCode_Format,

    TC_162__Negative__Missing_premisesCode,
    TC_163__Negative__Invalid_premisesCode_Length,
    TC_164__Negative__Invalid_premisesCode_Format,

    TC_165__Negative__Invalid_Account_Number,

    // ---------------- POSITIVE TEST CASES ----------------
    TC_166__Positive__No_Payment_Arrangement,
    TC_167__Positive__Has_Inactive_Payment_Arrangement,
    TC_168__Positive__Has_Active_Payment_Arrangement,

    TC_169__Positive__Payment_Arrangement_Number_Format,
    TC_170__Positive__Payment_Arrangement_Type_Code_Format,
    TC_171__Positive__Payment_Arrangement_Total_Amount_Format,
    TC_172__Positive__Payment_Arrangement_Date_Created_Format,
    TC_173__Positive__Payment_Arrangement_Number_Of_Installments_Format,
    TC_174__Positive__Payment_Arrangement_Amount_Due_Format,
    TC_175__Positive__Payment_Arrangement_Balance_Format,
    TC_176__Positive__Payment_Arrangement_Date_Due_Format,
    TC_177__Positive__Payment_Arrangement_Date_Paid_Format,
    TC_178__Positive__Payment_Arrangement_No_Of_Installments_Greater_than_1
}
