package com.gng.api.steps.csi.UpdateMailingAddress;

public enum UpdateMailingAddressLabel {

    update_mailing_address,
    update_mailing_address_mandatory,

    TC_79__Negative__Missing_Request_ID,
    TC_80__Negative__Invalid_Request_ID_Length,
    TC_81__Negative__Duplicate_Request_ID,

    TC_82__Negative__Missing_customerCode,
    TC_83__Negative__Invalid_customerCode_Length,
    TC_84__Negative__Invalid_customerCode_Format,
    TC_85__Negative__Invalid_Account_number,

    TC_86__Negative__Missing_premisesCode,
    TC_87__Negative__Invalid_premisesCode_Length,
    TC_88__Negative__Invalid_premisesCode_Format,

    TC_89__Negative__Invalid_Address_Fields_Missing,
    TC_90__Negative__Invalid_Address_Fields_Too_Many,

    TC_91__Negative__Invalid_Street_Number_Length,
    TC_92__Negative__Invalid_Street_Pre_Direction_Length,
    TC_93__Negative__Invalid_Street_Pre_Direction,
    TC_94__Negative__Invalid_Street_Name_Length,
    TC_95__Negative__Missing_StreetName,
    TC_96__Negative__Invalid_StreetSuffix_Length,
    TC_97__Negative__Invalid_StreetSuffix,
    TC_98__Negative__Invalid_Street_Post_Direction_Length,
    TC_99__Negative__Invalid_Street_Post_Direction,

    TC_100__Negative__Invalid_Unit_Type_Format,
    TC_101__Negative__Invalid_Unit_Type,
    TC_102__Negative__Invalid_Unit_Number_Format,

    TC_103__Negative__Invalid_City_Length,
    TC_104__Negative__Missing_City,

    TC_105__Negative__Invalid_Zip_Code_Format_Length,
    TC_106__Negative__Missing_Zip_Code,
    TC_107__Negative__Invalid_Zip_Code,
    TC_108__Negative__Invalid_City_and_Zip_Code_Combination,

    TC_109__Negative__Invalid_Delivery_Point_Format,
    TC_110__Negative__Invalid_Carrier_Route_Length,
    TC_111__Negative__Invalid_Attention_To_Length,
    TC_112__Negative__Invalid_Additional_Address_Line_Length,

    TC_113__Positive__Valid_Street_Address___Minimum_parameters_or_No_Existing_Address____,
    TC_114__Positive__Valid_Street_Address___Maximum_parameters_or_No_Existing_Address____,
    TC_115__Positive__Valid_Street_Address___Mixed_parameters_or_No_Existing_Address____,
    TC_116__Positive__Valid_Street_Address___Minimum_parameters_or_Existing_Address_or_Same_Day____,
    TC_117__Positive__Valid_Street_Address___Maximum_parameters_or_Existing_Address_or_Same_Day____,
    TC_118__Positive__Valid_Street_Address___Mixed_parameters_or_Existing_Address_or_Same_Day____,
    TC_119__Positive__Valid_Street_Address___Minimum_parameters_or_Existing_Address_or_Different_Day____,
    TC_120__Positive__Valid_Street_Address___Maximum_parameters_or_Existing_Address_or_Different_Day____,
    TC_121__Positive__Valid_Street_Address___Mixed_parameters_or_Existing_Address_or_Different_Day____,

    TC_122__Positive__Valid_PO_Box_Address,
    TC_123__Positive__Valid_Rural_Route_Address,
    TC_124__Positive__LoginID_Saved
}
