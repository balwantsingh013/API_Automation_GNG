Feature: Verify UpdateMailingAddress Api

  @UpdateMailingAddress @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateMailingAddress Api for "<testCondition>"
    Then verify response code of "UpdateMailingAddress" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                      | errorCode | errorMessage                       |
      | TC_72__Negative__Missing_Request_ID                | 10001     | Missing Request ID                 |
      | TC_73__Negative__Invalid_Request_ID_Length         | 10002     | Invalid Request ID                 |
      | TC_74__Negative__Duplicate_Request_ID              | 10003     | Duplicate Request ID               |
      | TC_75__Negative__Missing_customerCode              | 10011     | Missing Customer Code              |
      | TC_76__Negative__Invalid_customerCode_Length       | 10015     | Invalid Customer Code Format       |
      | TC_77__Negative__Invalid_customerCode_Format       | 10015     | Invalid Customer Code Format       |
      | TC_78__Negative__Invalid_customerCode              | 40015     | Invalid Account Number             |
      | TC_79__Negative__Missing_premisesCode              | 10013     | Missing Premises Code              |
      | TC_80__Negative__Invalid_premisesCode_Length       | 10005     | Invalid Premises Code Format       |
      | TC_81__Negative__Invalid_premisesCode_Format       | 10005     | Invalid Premises Code Format       |
      | TC_82__Negative__Invalid_premisesCode              | 40015     | Invalid Account Number             |
      | TC_83__Negative__Invalid_Address_Fields_Missing    | 10365     | Invalid Address Fields             |
      | TC_84__Negative__Invalid_Address_Fields_Too_Many   | 10365     | Invalid Address Fields             |
      | TC_85__Negative__Invalid_Street_Number_Length      | 10129     | Invalid Street Number Format       |
      | TC_86__Negative__Invalid_Street_Pre_Direction_Length | 10131   | Invalid Street Pre-Direction Format|
      | TC_87__Negative__Invalid_Street_Pre_Direction      | 40227     | Invalid Street Pre-Direction       |
      | TC_88__Negative__Invalid_Street_Name_Length        | 10133     | Invalid Street Name Format         |
      | TC_89__Negative__Missing_StreetName                | 10365     | Invalid Address Fields             |
      | TC_90__Negative__Invalid_StreetSuffix_Length       | 10135     | Invalid Street Suffix Format       |
      | TC_91__Negative__Invalid_StreetSuffix              | 40221     | Invalid Street Suffix              |
      | TC_92__Negative__Invalid_Street_Post_Direction_Length | 10137   | Invalid Street Post-Direction Format|
      | TC_93__Negative__Invalid_Street_Post_Direction     | 40229     | Invalid Street Post-Direction      |
      | TC_94__Negative__Invalid_Unit_Type_Format          | 10139     | Invalid Unit Type Format           |
      | TC_95__Negative__Missing_Unit_Type                 | 40223     | Invalid Street Unit Type           |
      | TC_96__Negative__Invalid_Unit_Number_Format        | 10141     | Invalid Unit Number Format         |
      | TC_97__Negative__Invalid_City_Length               | 10143     | Invalid City Format                |
      | TC_98__Negative__Missing_City                      | 10089     | Missing City                       |
      | TC_99__Negative__Invalid_Zip_Code_Format_Length    | 10147     | Invalid Zip Code Format            |
      | TC_100__Negative__Missing_Zip_Code                 | 10091     | Missing Zip Code                   |
      | TC_101__Negative__Invalid_Zip_Code                 | 40225     | Invalid Zip Code                   |
      | TC_102__Negative__Invalid_City_and_Zip_Code_Combination | 40237 | Invalid City and Zip combination   |
      | TC_103__Negative__Invalid_County_Code              | 40231     | Invalid County Code                |
      | TC_104__Negative__Invalid_Delivery_Point_Format    | 10367     | Invalid Delivery Point Format      |
      | TC_105__Negative__Invalid_Carrier_Route_Length     | 10369     | Invalid Carrier Route Format       |
      | TC_106__Negative__Invalid_Attention_To_Length      | 10373     | Invalid Attention To Format        |
      | TC_107__Negative__Invalid_Additional_Address_Line_Length | 10375 | Invalid Additional Address Line Format |

  @UpdateMailingAddressPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateMailingAddress Api for "<testCondition>"
    Then verify response code of "UpdateMailingAddress" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                           | errorCode | errorMessage |
      | TC_108__Positive__Valid_Street_Address  | 0         |              |
      | TC_109__Positive__Valid_PO_Box_Address  | 0         |              |
      | TC_110__Positive__Valid_Rural_Route_Address | 0      |              |
      | TC_111__Positive__LoginID_Saved         | 0         |              |
