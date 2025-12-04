Feature: Verify UpdateMailingAddress Api

  @UpdateMailingAddress @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateMailingAddress Api for "<testCondition>"
    Then verify response code of "UpdateMailingAddress" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                               | errorCode | errorMessage                       |
      | TC_68__Negative__Missing_Request_ID                         | 10001     | Missing Request ID                 |
      | TC_69__Negative__Invalid_Request_ID__Length                 | 10002     | Invalid Request ID                 |
      | TC_70__Negative__Duplicate_Request_ID                       | 10003     | Duplicate Request ID               |
      | TC_71__Negative__Missing_customerCode                       | 10011     | Missing Customer Code              |
      | TC_72__Negative__Invalid_customerCode__Length               | 10015     | Invalid Customer Code Format       |
      | TC_73__Negative__Invalid_customerCode__Format               | 10015     | Invalid Customer Code Format       |
      | TC_74__Negative__Invalid_customerCode                       | 40015     | Invalid Account Number             |
      | TC_75__Negative__Missing_premisesCode                       | 10013     | Missing Premises Code              |
      | TC_76__Negative__Invalid_premisesCode__Length               | 10005     | Invalid Premises Code Format       |
      | TC_77__Negative__Invalid_premisesCode__Format               | 10005     | Invalid Premises Code Format       |
      | TC_78__Negative__Invalid_premisesCode                       | 40015     | Invalid Account Number             |
      | TC_79__Negative__Invalid_Address_Fields___Missing____       | 10365     | Invalid Address Fields             |
      | TC_80__Negative__Invalid_Address_Fields___Too_Many____      | 10365     | Invalid Address Fields             |
      | TC_81__Negative__Invalid_Street_Number__Length              | 10129     | Invalid Street Number Format       |
      | TC_82__Negative__Invalid_Street_Pre__Direction__Length      | 10131     | Invalid Street Pre-Direction Format|
      | TC_83__Negative__Invalid_Street_Pre__Direction              | 40227     | Invalid Street Pre-Direction       |
      | TC_84__Negative__Invalid_Street_Name__Length                | 10133     | Invalid Street Name Format         |
      | TC_85__Negative__Missing_StreetName                         | 10365     | Invalid Address Fields             |
      | TC_86__Negative__Invalid_StreetSuffix__Length               | 10135     | Invalid Street Suffix Format       |
      | TC_87__Negative__Invalid_StreetSuffix                       | 40221     | Invalid Street Suffix              |
      | TC_88__Negative__Invalid_Street_Post__Direction__Length     | 10137     |Invalid Street Post-Direction Format|
      | TC_89__Negative__Invalid_Street_Post__Direction             | 40229     | Invalid Street Post-Direction      |
      | TC_90__Negative__Invalid_Unit_Type__Format                  | 10139     | Invalid Unit Type Format           |
      | TC_91__Negative__Missing_Unit_Type                          | 40223     | Invalid Street Unit Type           |
      | TC_92__Negative__Invalid_Unit_Number__Format                | 10141     | Invalid Unit Number Format         |
      | TC_93__Negative__Invalid_City__Length                       | 10143     | Invalid City Format                |
      | TC_94__Negative__Missing_City                               | 10089     | Missing City                       |
      | TC_95__Negative__Invalid_Zip_Code__Format___Length__10____  | 10147     | Invalid Zip Code Format            |
      | TC_96__Negative__Missing_Zip_Code                           | 10091     | Missing Zip Code                   |
      | TC_97__Negative__Invalid_Zip_Code                           | 40225     | Invalid Zip Code                   |
      | TC_98__Negative__Invalid_City_and_Zip_Code__Combination     | 40237     | Invalid City and Zip combination   |
      | TC_99__Negative__Invalid_County_Code                        | 40231     | Invalid County Code                |
      | TC_100__Negative__Invalid_Delivery_Point__Format            | 10367     | Invalid Delivery Point Format      |
      | TC_101__Negative__Invalid_Carrier_Route__Length             | 10369     | Invalid Carrier Route Format       |
      | TC_102__Negative__Invalid_Attention_To__Length              | 10373     | Invalid Attention To Format        |
      | TC_103__Negative__Invalid_Additional_Address_Line__Length   | 10375     |Invalid Additional Address Line Format|

  @UpdateMailingAddressPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateMailingAddress Api for "<testCondition>"
    Then verify response code of "UpdateMailingAddress" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                | errorCode | errorMessage |
      | TC_104__Positive__Valid_Street_Address       | 0         |              |
      | TC_105__Positive__Valid_PO_Box_Address       | 0         |              |
      | TC_106__Positive__Valid_Rural_Route_Address  | 0         |              |
