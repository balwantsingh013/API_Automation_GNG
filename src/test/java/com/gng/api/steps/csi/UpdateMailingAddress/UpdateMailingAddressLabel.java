package com.gng.api.steps.csi.UpdateMailingAddress;

public enum UpdateMailingAddressLabel {

    update_mailing_address,
    update_mailing_address_mandatory,

    TC_72__Negative__Missing_Request_ID,
    TC_73__Negative__Invalid_Request_ID_Length,
    TC_74__Negative__Duplicate_Request_ID,

    TC_75__Negative__Missing_customerCode,
    TC_76__Negative__Invalid_customerCode_Length,
    TC_77__Negative__Invalid_customerCode_Format,
    TC_78__Negative__Invalid_customerCode,

    TC_79__Negative__Missing_premisesCode,
    TC_80__Negative__Invalid_premisesCode_Length,
    TC_81__Negative__Invalid_premisesCode_Format,
    TC_82__Negative__Invalid_premisesCode,

    TC_83__Negative__Invalid_Address_Fields_Missing,
    TC_84__Negative__Invalid_Address_Fields_Too_Many,

    TC_85__Negative__Invalid_Street_Number_Length,
    TC_86__Negative__Invalid_Street_Pre_Direction_Length,
    TC_87__Negative__Invalid_Street_Pre_Direction,
    TC_88__Negative__Invalid_Street_Name_Length,
    TC_89__Negative__Missing_StreetName,
    TC_90__Negative__Invalid_StreetSuffix_Length,
    TC_91__Negative__Invalid_StreetSuffix,
    TC_92__Negative__Invalid_Street_Post_Direction_Length,
    TC_93__Negative__Invalid_Street_Post_Direction,

    TC_94__Negative__Invalid_Unit_Type_Format,
    TC_95__Negative__Missing_Unit_Type,
    TC_96__Negative__Invalid_Unit_Number_Format,

    TC_97__Negative__Invalid_City_Length,
    TC_98__Negative__Missing_City,

    TC_99__Negative__Invalid_Zip_Code_Format_Length,
    TC_100__Negative__Missing_Zip_Code,
    TC_101__Negative__Invalid_Zip_Code,
    TC_102__Negative__Invalid_City_and_Zip_Code_Combination,

    TC_103__Negative__Invalid_County_Code,
    TC_104__Negative__Invalid_Delivery_Point_Format,
    TC_105__Negative__Invalid_Carrier_Route_Length,
    TC_106__Negative__Invalid_Attention_To_Length,
    TC_107__Negative__Invalid_Additional_Address_Line_Length,

    TC_108__Positive__Valid_Street_Address,
    TC_109__Positive__Valid_PO_Box_Address,
    TC_110__Positive__Valid_Rural_Route_Address,
    TC_111__Positive__LoginID_Saved
}
