package com.gng.api.steps.csi.VerifyAccount;

public enum VerifyAccountLabel {

    verify_account,
    verify_account_swagger,

    // TC_206–227: Swagger + UCRADDR billing
    TC_206__Positive__Billing_Street_Number_Value_,
    TC_207__Positive__Billing_Street_Number_Format_,
    TC_208__Positive__Billing_PreDir_Value_,
    TC_209__Positive__Billing_PreDir_Format_,
    TC_210__Positive__Billing_Street_Name_Value_,
    TC_211__Positive__Billing_Street_Name_Format_,
    TC_212__Positive__Billing_Street_Suffix_Value_,
    TC_213__Positive__Billing_Street_Suffix_Format_,
    TC_214__Positive__Billing_PostDir_Value_,
    TC_215__Positive__Billing_PostDir_Format_,
    TC_216__Positive__Billing_Unit_Type_Value_,
    TC_217__Positive__Billing_Unit_Type_Format_,
    TC_218__Positive__Billing_Unit_Number_Value_,
    TC_219__Positive__Billing_Unit_Number_Format_,
    TC_220__Positive__Billing_City_Value_,
    TC_221__Positive__Billing_City_Format_,
    TC_222__Positive__Billing_State_Value_,
    TC_223__Positive__Billing_State_Format_,
    TC_224__Positive__Billing_ZIP_Value_,
    TC_225__Positive__Billing_ZIP_Format_,
    TC_226__Positive__Billing_PO_Box_Value_,
    TC_227__Positive__Billing_PO_Box_Format_,

    // TC_228–229: Preferences + GTBENRL billing
    TC_228__Positive__GTBENRL_Billing_Address_Source_,
    TC_229__Positive__GTBENRL_Billing_Address_Parsing_,

    // TC_230–241: Preferences + NEW prefs / confirm dates
    TC_230__Positive__Bill_Delivery_Confirmation_Date_Value_,
    TC_231__Positive__Bill_Delivery_Confirmation_Date_Format_,
    TC_232__Positive__Corr_Delivery_Confirmation_Date_Value_,
    TC_233__Positive__Corr_Delivery_Confirmation_Date_Format_,
    TC_234__Positive__Bill_Delivery_Preference_Confirmed_,
    TC_235__Positive__Correspondence_Delivery_Preference_Confirmed_,
    TC_236__Positive__Bill_Delivery_Preference_Initiated_,
    TC_237__Positive__Correspondence_Delivery_Preference_Initiated_,
    TC_238__Positive__Bill_Delivery_Preference_Paper_,
    TC_239__Positive__Correspondence_Delivery_Preference_Paper_,
    TC_240__Positive__Bill_Delivery_Preference_Expired_,
    TC_241__Positive__Correspondence_Delivery_Preference_Expired_
}
