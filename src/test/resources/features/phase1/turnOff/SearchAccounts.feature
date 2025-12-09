Feature: Verify SearchAccounts TurnOff Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

    @InvalidAndValidCombinationLastNameZipcode @Phase1 @turnOff @HappyFlow @SearchAccountTOFF
    Scenario Outline: SearchAccountsApiTOFF- Verify Response when Last Name, Zipcode And transactionType As TOFF is provided With "<combinationType>"
    When a request is made to the SearchAccounts Api with Last Name And Zipcode And transactionType As TOFF With "<combinationType>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as <noOfMatches>

      Examples:
      |combinationType                              |noOfMatches|
      |INVALID_COMBINATION_OF_LASTNAME_ZIPCODE_TC_77|0          |
      |VALID_COMBINATION_OF_LASTNAME_ZIPCODE_TC_78  |30         |

    @validFirstNameLastNameZipcodeRSOrSR @Phase1 @turnOff @HappyFlow @SearchAccountTOFF
    Scenario Outline: SearchAccountsApiTOFF- Verify Response when a Valid Combination Of First Name, Last Name And Zipcode with transactionType As TOFF is provided For "<accountType>"
    When a request is made to the SearchAccounts Api with First Name, Last Name And Zipcode with transactionType As TOFF is provided For "<accountType>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as <noOfMatches>
    And response should have "customerType" as "<customerType>"
    And response should have "accountStatus" as "<accountStatus>"
    And response should have "meteredAccount" flag as "<isMeteredAccount>"
    And response should have "turnOffAllowed" flag as "<isTurnOffAllowed>"
    Examples:
      |accountType                                 |noOfMatches|customerType|accountStatus|isMeteredAccount|isTurnOffAllowed|
      |RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC_79      |1          |RS          |A            |true            |true            |
      |SENIOR_RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC_80|1          |SR          |A            |true            |true            |

    @validCustomerBusinessName @Phase1 @turnOff @HappyFlow @SearchAccountTOFF
    Scenario Outline: SearchAccountsApiTOFF- Verify Response when a Valid CustomerBusinessName Parameter with transactionType As TOFF is provided For "<accountType>"
    When a request is made to the SearchAccounts Api with Valid CustomerBusinessName Parameter with transactionType As TOFF is provided For "<accountType>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "customerType" as "<customerType>"
    And response should have "accountStatus" as "<accountStatus>"
    And response should have "meteredAccount" flag as "<isMeteredAccount>"
    And response should have "turnOffAllowed" flag as "<isTurnOffAllowed>"
    And response should have "sonpAccount" flag as "<isSONPAccount>"
    And response should have "pastDueAmount" as "<pastDueAmount>"
    Examples:
      |accountType                                         |customerType|accountStatus|isMeteredAccount|isTurnOffAllowed|isSONPAccount|pastDueAmount|
      |COMMERCIAL_VALID_ACTIVE_PASTDUEBALANCE_ACCOUNT_TC_81|CM          |A            |true            |true            |false        |0            |
      |COMMERCIAL_VALID_ACTIVE_SONP_ACCOUNT_TC_82          |CM          |A            |true            |true            |true         |0            |
      |COMMERCIAL_VALID_ACTIVE_PENDING_REWARDS_TC_83       |CM          |A            |true            |true            |false        |0            |
      |COMMERCIAL_VALID_ACTIVE_ETC_TC_85                   |CM          |A            |true            |true            |false        |0            |
      |COMMERCIAL_VALID_ACTIVE_NO_ETC_TC_86                |CM          |A            |true            |true            |false        |0            |
      |COMMERCIAL_VALID_ACTIVE_PRICE_PLAN_CCV_TC_87        |CM          |A            |true            |true            |false        |0            |
      |COMMERCIAL_VALID_FINAL_ACCOUNT_TC_88                |CM          |F            |true            |true            |false        |0            |

  @validCustPremCode @Phase1 @turnOff @HappyFlow @SearchAccountTOFF
  Scenario Outline: SearchAccountsApiTOFF- Verify Response when a valid customer and premisesCode are provided for "TOFF" for <testCondition>
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "accountStatus" as "<accountStatus>"
    And response should have "meteredAccount" flag as "<isMeteredAccount>"
    And response should have "customerType" as "<customerType>"
    And response should have "turnOffAllowed" flag as "<isTurnOffAllowed>"
    And response should have "masterAccount" flag as "<isMasterAccount>"
    And response should have "sonpAccount" flag as "<isSONPAccount>"
    Examples:
    |testCondition                             |accountStatus|isMeteredAccount|customerType|isTurnOffAllowed|isMasterAccount|isSONPAccount|
    |ACTIVE_RS_NEW_NON_METERED_TC_56           |N            |false           |            |false           |false          |false        |
    |ACTIVE_MSB_ACCOUNT_TC_57                  |A            |true            |CM          |false           |true           |true         |
    |ACTIVE_RS_NEW_NON_METERED_TC_58           |A            |false           |            |false           |false          |false        |
    |INACTIVE_RS_METERED_TC_59                 |I            |true            |RS          |false           |false          |false        |
    |INACTIVE_BAD_DEBT_BALANCE_TC_60           |I            |true            |RS          |false           |false          |true         |
    |RS_INACTIVE_WITH_SONP_TC_61               |I            |true            |RS          |false           |false          |true         |
    |RS_INACTIVE_NON_METER_TC_62               |I            |false           |            |false           |false          |false        |
    |RS_NEW_BANKRUPCY_TC_63                    |N            |false           |            |false           |false          |false        |
    |RS_INACTIVE_BANKRUPCY_TC_64               |I            |false           |            |false           |false          |false        |
    |CM_NEW_NON_METERED_TC_65                  |N            |false           |            |false           |false          |false        |
    |CM_ACTIVE_NON_METERED_TC_67               |A            |false           |            |false           |false          |false        |
    |CM_INACTIVE_METERED_TC_68                 |I            |true            |CM          |false           |false          |true         |
    |CM_INACTIVE_BAD_DEBT_TC_69                |I            |true            |CM          |false           |false          |false        |
    |CM_INACTIVE_SONP_TC_70                    |I            |true            |CM          |false           |false          |true         |
    |CM_INACTIVE_NON_METERED_TC_71             |I            |false           |            |false           |false          |false        |
    |CM_NEW_BANKRUPCY_TC_72                    |N            |false           |            |false           |false          |false        |
    |CM_INACTIVE_BANKRUPCY_TC_73               |I            |false           |            |false           |false          |false        |
    |RS_ACTIVE_ACCOUNT_TC_75                   |A            |true            |RS          |true            |false          |false        |
    |CM_ACTIVE_ACCOUNT_TC_76                   |A            |true            |CM          |true            |false          |false        |
    |RS_ACTIVE_SONP_NON_MASTER_TC_91           |A            |true            |RS          |true            |false          |true         |
    |RS_ACTIVE_PENDING_REWARDS_TC_92           |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_UNAPPLIED_DEPOSIT_TC_93         |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_NO_UNAPPLIED_DEPOSIT_TC_94      |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_PGB_PRICE_PLAN_EXP_DATE_TC_95   |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_MKT_PRICE_PLAN_NO_EXP_DATE_TC_96|A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_ETC_TC_97                       |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_ETC_RGB_PRICE_PLAN_TC_98        |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_GREENER_LIFE_TC_99              |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_CSV_PRICE_PLAN_TC_100           |A            |true            |RS          |true            |false          |false        |
    |RS_FINAL_ACCOUNT_TC_101                   |F            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_ACCOUNT_TC_102                  |A            |true            |RS          |true            |false          |false        |
    |SSP_ACCOUNT_WITH_ETC_TC_105A              |A            |true            |RS          |true            |false          |false        |
    |SSP_ACCOUNT_WITHOUT_ETC_TC_105B           |A            |true            |CM          |true            |false          |false        |
    |NON_SSP_ACCOUNT_WITH_ETC_TC_105C          |A            |true            |CM          |true            |false          |false        |
    |NON_SSP_ACCOUNT_WITHOUT_ETC_TC_105D       |A            |true            |RS          |true            |false          |false        |

  @validCustCodeInvalidPremCode @Phase1 @turnOff @HappyFlow @SearchAccountTOFF
  Scenario: SearchAccountsApiTOFF- Verify Response when a valid customer and premisesCode are provided for "TOFF" for VALID_CUST_CODE_INVALID_PREM_CODE_TC_74
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for "VALID_CUST_CODE_INVALID_PREM_CODE_TC_74"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 0

  @validAGLCAccountNumberRSActiveAccount @Phase1 @turnOff @HappyFlow @SearchAccountTOFF
  Scenario: SearchAccountsApiTOFF- Verify Response when a Valid AGLC Account Number Parameter with transactionType As TOFF is input For Residential Active Account TC_103
    When a request is made to the SearchAccounts Api with Valid AGLC Account Number Parameter with transactionType As TOFF is input For Residential Active account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 1
    And response should have "accountStatus" as "A"
    And response should have "meteredAccount" flag as "true"
    And response should have "customerType" as "RS"
    And response should have "turnOffAllowed" flag as "true"

  @ValidAddressDetailsRSActiveAccount @Phase1 @turnOff @HappyFlow @SearchAccountTOFF
  Scenario Outline: SearchAccountsApiTOFF- Verify Response when a Valid Address Details parameters with transactionType As TOFF is input For <testCondition>
    When a request is made to the SearchAccounts Api with Valid Address Details parameters with transactionType As TOFF is input For "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as <noOfMatches>
    And response should have "customerType" as "<customerType>"
    And response should have "meteredAccount" flag as "<isMeteredAccount>"
    And response should have "turnOffAllowed" flag as "<isTurnOffAllowed>"
  Examples:
  |testCondition                             |noOfMatches|customerType|isMeteredAccount|isTurnOffAllowed |
  |ALL_PREMISES_FIELDS_TC_104                |2          |RS          |true            |false            |
  |PREMISES_STREET_NAME_CITY_STATE_ZIP_TC_105|30         |RS          |true            |false            |

#  @validSSNActiveRSPastDueBalance @Phase1 @HappyFlow @SearchAccountTOFF
#  Scenario: SearchAccountsApiTOFF- Verify Response when a Valid SSN Parameter with transactionType As TOFF is input For Active RS Account with Past Due Balance TC_89
#    When a request is made to the SearchAccounts Api with Valid SSN Parameter with transactionType As TOFF is input For Active RS Account with Past Due Balance
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode 0 and ErrorMessage ""
#    And response should return numberOfMatches as 1
#    And response should have "accountStatus" as "A"
#    And response should have "meteredAccount" flag as "true"
#    And response should have "customerType" as "RS"
#    And response should have "turnOffAllowed" flag as "true"

  @SearchAccountsTurnOffInvalidRequestId @Phase1 @turnOff @NegativeFlow
  Scenario Outline: SearchAccountsApiTOFF - Verify SearchAccounts Api with invalid requestID for "<testCondition>"
    When a request is made to the SearchAccounts Api with an invalid requestID for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                    | errorCode | errorMessage         |
      | SEARCH_ACCOUNTS_MISSING_REQUEST_ID_TC_12         | 10001     | Missing Request ID   |
      | SEARCH_ACCOUNTS_INVALID_REQUEST_ID_LENGTH_TC_13  | 10002     | Invalid Request ID   |
      | SEARCH_ACCOUNTS_DUPLICATE_REQUEST_ID_TC_14       | 10003     | Duplicate Request ID |


  @SearchAccountsTurnOffNegativeInvalidLoginId @Phase1 @turnOff @NegativeFlow
  Scenario Outline: SearchAccountsApiTOFF Api - Verify SearchAccounts Api with invalid loginId for "<testCondition>" condition
    When a request is made to the SearchAccounts Api with an invalid loginId for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                  | errorCode | errorMessage              |
      | SEARCH_ACCOUNTS_MISSING_LOGIN_ID_TC_15         | 10112     |Invalid or missing Login ID|
      | SEARCH_ACCOUNTS_INVALID_LOGIN_ID_TC_16         | 10112     |Invalid or missing Login ID|
      | SEARCH_ACCOUNTS_NON_ALPHANUMERIC_ID_TC_17      | 10112     |Invalid or missing Login ID|
      |SEARCH_ACCOUNT_LOGIN_ID_NOT_IN_USERS_TABLE_TC_18|2000       |Invalid Login ID           |

  @SearchAccountsTurnOffNegativeInvalidCustomerCode @Phase1 @turnOff @NegativeFlow
  Scenario Outline: SearchAccountsApiTOFF Api - Verify SearchAccounts Api with invalid CustomerCode for "<testCondition>" condition
    When a request is made to the SearchAccounts Api with an invalid field for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                              | errorCode | errorMessage                                                                                                                                                                     |
      | SEARCH_ACCOUNTS_MISSING_CUSTOMER_CODE_TC_19                |1001       |Invalid Request: Invalid required search field combination - customerCode required.                                                                                               |
      |SEARCH_ACCOUNTS_MISSING_PREMISES_CODE_TC_20                 |1001       |Invalid Request: Invalid required search field combination - premisesCode required.                                                                                               |
      |SEARCH_ACCOUNTS_NO_SEARCH_PARAMETERS_ARE_PROVIDED_TC_11     |10116      |At least one of the following is required: GNG Account Number, Business Name, First and Last Name (with Zip Code), Social Security Number, AGLC Account Number or Premise Address.|
      |INVALID_LENGTH_CUSTOMER_CODE_TC_21                          |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |INVALID_LENGTH_PREMISES_CODE_TC_22                          |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |MISSING_TRANSACTION_TYPE_TC_23                              |10113      |Invalid or missing Transaction Type                                                                                                                                               |
      |INVALID_TRANSACTION_TYPE_TC_24                              |10113      |Invalid or missing Transaction Type                                                                                                                                               |
      |INVALID_TRANSACTION_TYPE_LENGTH_TC_25                       |10113      |Invalid or missing Transaction Type                                                                                                                                               |
      |INVALID_CUSTOMER_BUSINESS_NAME_LENGTH_TC_26                 |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |INVALID_LAST_NAME_LENGTH_TC_27                              |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |MISSING_ZIP_CODE_TC_29                                      |1001       |Invalid Request: Invalid required search field combination - premisesZipCode required.                                                                                            |
      |MISSING_TRANSACTION_TYPE_LAST_NAME_AND_ZIP_GIVEN_TC_30      |10113      |Invalid or missing Transaction Type                                                                                                                                               |
      |INVALID_FIRST_NAME_LENGTH_TC_31                             |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |UNENCRYPTED_SSN_TC_32                                       |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |INVALID_SSN_LENGTH_TC_33                                    |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |FEDERAL_TAX_ID_NOT_ALLOWED_TC_34                            |2200       |Parameter Value should be null-Federal Tax ID                                                                                                                                     |
      |SSN_AND_FEDERAL_TAX_ID_PROVIDED_TC_35                       |2200       |Parameter Value should be null-Federal Tax ID                                                                                                                                     |
      |INVALID_PARAMETER_PROVIDED_PHONE_NUMBER_TC_36               |2200       |Parameter Value should be null-Phone Number                                                                                                                                       |
      |INVALID_AGLC_ACCOUNT_NUMBER_LENGTH_TC_37                    |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |INVALID_AGLC_ACCOUNT_NUMBER_LENGTH_BUSINESS_NAME_GIVEN_TC_38|10115      |Invalid search parameter(s)                                                                                                                                                       |
      |NON_NUMERIC_AGLC_ACCOUNT_NUMBER_TC_39                       |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |INVALID_PREMISES_STREET_NUMBER_LENGTH_TC_40                 |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |INVALID_STREET_PREDIRECTION_LENGTH_TC_41                    |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |INVALID_PREMISES_STREET_NAME_LENGTH_TC_42                   |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |MISSING_PREMISES_STREET_NAME_TC_43                          |1001       |Invalid Request: Invalid required search field combination - premisesStreetName required.                                                                                         |
      |INVALID_STREET_SUFFIX_LENGTH_TC_44                          |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |INVALID_PREMISES_STREET_POST_DIRECTION_LENGTH_TC_45         |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |INVALID_UNIT_TYPE_LENGTH_TC_46                              |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |PREMISES_UNIT_TYPE_MISSING_TC_47                            |1001       |Invalid Request: Invalid required search field combination - premisesUnitType required.                                                                                           |
      |INVALID_PREMISES_UNIT_NUMBER_FORMAT_TC_48                   |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |MISSING_UNIT_NUMBER_TC_49                                   |1001       |Invalid Request: Invalid required search field combination - premisesUnitNumber required.                                                                                         |
      |INVALID_PREMISES_CITY_LENGTH_TC_50                          |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |MISSING_PREMISES_CITY_TC_51                                 |1001       |Invalid Request: Invalid required search field combination - premisesCity required.                                                                                               |
      |INVALID_STATE_CODE_LENGTH_TC_52                             |10115      |Invalid PremisesStateCode provided                                                                                                                                                |
      |MISSING_STATE_CODE_TC_53                                    |1001       |Invalid Request: Invalid required search field combination - premisesStateCode required.                                                                                          |
      |INVALID_ZIP_CODE_LENGTH_TC_54A                              |10115      |Invalid PremisesZipCode provided                                                                                                                                                  |
      |INVALID_ZIP_CODE_LENGTH_LESS_THAN_5_TC_54B                  |10115      |Invalid PremisesZipCode provided                                                                                                                                                  |
      |MISSING_ZIP_CODE_TC_55                                      |1001       |Invalid Request: Invalid required search field combination - premisesZipCode required.                                                                                            |

  @InvalidFirstNameAndZipCombination @Phase1 @NegativeFlow @turnOff
  Scenario: SearchAccountsApiTOFF Api - Verify SearchAccounts Api with invalid last name and zip for TC_28
    When a request is made to the SearchAccounts Api with an invalid field for "INVALID_LASTNAME_AND_ZIP_COMBINATION_TC_28"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 0