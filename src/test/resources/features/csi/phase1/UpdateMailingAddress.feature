Feature: Verify UpdateMailingAddress Api

  @UpdateMailingAddress @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateMailingAddress Api for "<testCondition>"
    Then verify response code of "UpdateMailingAddress" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                      | errorCode | errorMessage                               |
      | TC_80__Negative__Missing_Request_ID                | 10001     | Missing Request ID                          |
      | TC_81__Negative__Invalid_Request_ID_Length         | 10002     | Invalid Request ID                          |
      | TC_82__Negative__Duplicate_Request_ID              | 10003     | Duplicate Request ID                        |
      | TC_83__Negative__Missing_customerCode              | 10011     | Missing Customer Code                       |
      | TC_84__Negative__Invalid_customerCode_Length       | 10015     | Invalid Customer Code Format                |
      | TC_85__Negative__Invalid_customerCode_Format       | 10015     | Invalid Customer Code Format                |
      | TC_86__Negative__Invalid_Account_Number            | 40015     | Invalid Account Number                      |
      | TC_87__Negative__Missing_premisesCode              | 10013     | Missing Premises Code                       |
      | TC_88__Negative__Invalid_premisesCode_Length       | 10005     | Invalid Premises Code Format                |
      | TC_89__Negative__Invalid_premisesCode_Format       | 10005     | Invalid Premises Code Format                |
      | TC_90__Negative__Invalid_Address_Fields_Missing    | 10365     | Invalid Address Fields                      |
      | TC_91__Negative__Invalid_Address_Fields_Too_Many   | 10365     | Invalid Address Fields                      |
      | TC_92__Negative__Invalid_Street_Number_Length      | 10146     | Invalid Street Number Format                |
      | TC_93__Negative__Invalid_Street_Pre_Direction_Length | 10148   | Invalid Street Pre-Direction Format         |
      | TC_94__Negative__Invalid_Street_Pre_Direction      | 40227     | Invalid Street Pre-Direction                |
      | TC_95__Negative__Invalid_Street_Name_Length        | 10133     | Invalid Street Name Format                  |
      | TC_96__Negative__Missing_StreetName                | 10365     | Invalid Address Fields                      |
      | TC_97__Negative__Invalid_StreetSuffix_Length       | 10149     | Invalid Street Suffix Format                |
      | TC_98__Negative__Invalid_StreetSuffix              | 40221     | Invalid Street Suffix                       |
      | TC_99__Negative__Invalid_Street_Post_Direction_Length | 10150   | Invalid Street Post-Direction Format        |
      | TC_100__Negative__Invalid_Street_Post_Direction    | 40229     | Invalid Street Post-Direction               |
      | TC_101__Negative__Invalid_Unit_Type_Format         | 10151     | Invalid Unit Type Format                    |
      | TC_102__Negative__Invalid_Unit_Type                | 40223     | Invalid Unit Type                           |
      | TC_103__Negative__Invalid_Unit_Number_Format       | 10141     | Invalid Unit Number Format                  |
      | TC_104__Negative__Invalid_City_Length              | 10143     | Invalid City Format                         |
      | TC_105__Negative__Missing_City                     | 10089     | Missing City                                |
      | TC_106__Negative__Invalid_Zip_Code_Format_Length   | 10147     | Invalid Zip Code Format                     |
      | TC_107__Negative__Missing_Zip_Code                 | 10091     | Missing Zip Code                            |
      | TC_108__Negative__Invalid_Zip_Code                 | 40225     | Invalid Zip Code                            |
      | TC_109__Negative__Invalid_City_and_Zip_Code_Combination | 40237 | Invalid City and Zip Code Combination       |
      | TC_110__Negative__Invalid_Delivery_Point_Format    | 10367     | Invalid Delivery Point Format               |
      | TC_111__Negative__Invalid_Carrier_Route_Length     | 10369     | Invalid Carrier Route Format                |
      | TC_112__Negative__Invalid_Attention_To_Length      | 10373     | Invalid Attention To Format                 |
      | TC_113__Negative__Invalid_Additional_Address_Line_Length | 10375 | Invalid Additional Address Line Format      |
      | TC_114__Negative__Invalid_Rural_Route_Length       | 10255     | Invalid Rural Route Format                  |
      | TC_115__Negative__Invalid_PO_Box_Length            | 10259     | Invalid PO Box Format                       |
      | TC_116__Negative__Invalid_PO_Box_Format            | 10259     | Invalid PO Box Format                       |

  @UpdateMailingAddressPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateMailingAddress Api for "<testCondition>"
    Then verify response code of "UpdateMailingAddress" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Then perform database validation for "<testCondition>"

    Examples:
      | testCondition                           | errorCode | errorMessage |
      | TC_117__Positive__Valid_Street_Address___Minimum_parameters_and_No_Existing_Address____  | 0         |              |
      | TC_118__Positive__Valid_Street_Address___Maximum_parameters_and_No_Existing_Address____  | 0         |              |
      | TC_119__Positive__Valid_Street_Address___Mixed_parameters_and_No_Existing_Address____  | 0         |              |
      | TC_120__Positive__Valid_Street_Address___Minimum_parameters_and_Existing_Address_and_Same_Day____  | 0         |              |
      | TC_121__Positive__Valid_Street_Address___Maximum_parameters_and_Existing_Address_and_Same_Day____  | 0         |              |
      | TC_122__Positive__Valid_Street_Address___Mixed_parameters_and_Existing_Address_and_Same_Day____  | 0         |              |
      | TC_123__Positive__Valid_Street_Address___Minimum_parameters_and_Existing_Address_and_Different_Day____  | 0         |              |
      | TC_124__Positive__Valid_Street_Address___Maximum_parameters_and_Existing_Address_and_Different_Day____  | 0         |              |
      | TC_125__Positive__Valid_Street_Address___Mixed_parameters_and_Existing_Address_and_Different_Day____  | 0         |              |
      | TC_126__Positive__Valid_PO_Box_Address  | 0         |              |
      | TC_127__Positive__Valid_Rural_Route_Address | 0      |              |

  @UpdateMailingAddressPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateMailingAddress Api for "<testCondition>"
    Then verify response code of "UpdateMailingAddress" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And verify if UpdateMailingAddress login id is saved

    Examples:
      | testCondition                           | errorCode | errorMessage |
      | TC_128__Positive__LoginID_Saved         | 0         |              |
