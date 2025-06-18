Feature: Verify SearchAccounts TurnOff Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

    @Invalid_ValidLastNameZipcode @Phase1 @HappyFlow @SearchAccountTOFF
    Scenario Outline: SearchAccountsApi- Verify Response  when a Last Name And Zipcode And transactionType As TOFF is input With "<combinationType>"
    When a request is made to the SearchAccounts Api with Last Name And Zipcode And transactionType As TOFF With "<combinationType>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as <noOfMatches>

      Examples:
      |combinationType                             |noOfMatches|
      |INVALID_COMBINATION_OF_LASTNAME_ZIPCODE_TC77|0          |
      |VALID_COMBINATION_OF_LASTNAME_ZIPCODE_TC78  |30          |

    @validFirstNameLastNameZipcodeRSOrSR @Phase1 @HappyFlow @SearchAccountTOFF
    Scenario Outline: SearchAccountsApi- Verify Response when a Valid Combination Of First Name, Last Name And Zipcode with transactionType As TOFF is input For "<accountType>"
    When a request is made to the SearchAccounts Api with First Name, Last Name And Zipcode with transactionType As TOFF is input For "<accountType>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as <noOfMatches>
    And response should have "customerType" as "<customerType>"
    And response should have "accountStatus" as "<accountStatus>"
    And response should have "meteredAccount" flag as "<isMeteredAccount>"
    And response should have "turnOffAllowed" flag as "<isTurnOffAllowed>"
    Examples:
      |accountType                                 |noOfMatches|customerType|accountStatus|isMeteredAccount|isTurnOffAllowed|
      |RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC79       |1          |RS          |A            |true            |true            |
      |SENIOR_RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC80|1          |SR          |A            |true            |true            |

    @validCustomerBusinessNamePastDueBalanceOrSONP @Phase1 @HappyFlow @SearchAccountTOFF @testrun1234
    Scenario Outline: SearchAccountsApi- Verify Response For Verify that when a Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial Active And "<accountType>"
    When a request is made to the SearchAccounts Api with Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial Active And "<accountType>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "customerType" as "<customerType>"
    And response should have "accountStatus" as "<accountStatus>"
    And response should have "meteredAccount" flag as "<isMeteredAccount>"
    And response should have "turnOffAllowed" flag as "<isTurnOffAllowed>"
    And response should have "sonpAccount" flag as "<isSONPAccount>"
    And response should have pastDueAmount as <pastDueAmount>
    Examples:
      |accountType                                        |customerType|accountStatus|isMeteredAccount|isTurnOffAllowed|isSONPAccount|pastDueAmount|
      |COMMERCIAL_VALID_ACTIVE_PASTDUEBALANCE_ACCOUNT_TC81|CM          |A            |true            |true            |false         |0            |
      |COMMERCIAL_VALID_ACTIVE_SONP_ACCOUNT_TC82          |CM          |A            |true            |true            |true         |0            |
      |COMMERCIAL_VALID_ACTIVE_PENDING_REWARDS_TC83       |CM          |A            |true            |true            |false        |0            |
      |COMMERCIAL_VALID_ACTIVE_ETC_TC_85                  |CM          |A            |true            |true            |false        |0            |
      |COMMERCIAL_VALID_ACTIVE_NO_ETC_TC_86               |CM          |A            |true            |true            |false        |0            |
      |COMMERCIAL_VALID_ACTIVE_PRICE_PLAN_CCV_TC_87       |            |N            |false            |false            |false        |0            |

  @validCustPremCode @Phase1 @HappyFlow @SearchAccountTOFF
  Scenario Outline: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for <testCondition>
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
    |ACTIVE_MSB_ACCOUNT_TC_57                  |A            |true            |CM          |false           |true           |true        |
    |ACTIVE_RS_NEW_NON_METERED_TC_58           |A            |false           |            |false           |false          |false        |
    |INACTIVE_RS_METERED_TC_59                 |I            |true            |RS          |false           |false          |false        |
    |INACTIVE_BAD_DEBT_BALANCE_TC_60           |I            |true            |RS          |false           |false          |true        |
    |RS_INACTIVE_WITH_SONP_TC_61               |I            |true            |RS          |false           |false          |true        |
    |RS_INACTIVE_NON_METER_TC_62               |I            |false           |          |false           |false          |false        |
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
    #TC74 needs to be moved
    #|VALID_CUST_CODE_INVALID_PREM_CODE_TC_74   |NA           |false           |NA          |
    |RS_ACTIVE_ACCOUNT_TC_75                   |A            |true            |RS          |true            |false          |false        |
    |CM_ACTIVE_ACCOUNT_TC_76                   |A            |true            |CM          |true            |false          |false        |
    |RS_ACTIVE_SONP_NON_MASTER_TC_91           |A            |true            |RS          |true            |false          |true         |
    |RS_ACTIVE_PENDING_REWARDS_TC_92           |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_UNAPPLIED_DEPOSIT_TC_93         |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_NO_UNAPPLIED_DEPOSIT_TC_94      |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_PGB_PRICE_PLAN_EXP_DATE_TC_95   |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_MKT_PRICE_PLAN_NO_EXP_DATE_TC_96|A            |true            |RS          |true            |false          |true        |
    |RS_ACTIVE_ETC_TC_97                       |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_ETC_RGB_PRICE_PLAN_TC_98        |A            |true            |RS          |true            |false          |false        |
    |RS_ACTIVE_GREENER_LIFE_TC_99              |A            |true            |RS          |true            |false          |false         |
    |RS_ACTIVE_CSV_PRICE_PLAN_TC_100           |A            |true            |RS          |true            |false          |false         |
    |RS_FINAL_ACCOUNT_TC_101                   |F            |true            |RS          |true            |false          |false         |
    |RS_ACTIVE_ACCOUNT_TC_102                  |A            |true            |RS          |true            |false          |false         |
    |SSP_ACCOUNT_WITH_ETC_TC_105A              |A            |true            |RS          |true            |false          |false         |
    |SSP_ACCOUNT_WITHOUT_ETC_105B              |A            |true            |CM          |true            |false          |false         |
    |NON_SSP_ACCOUNT_WITH_ETC_105C             |A            |true            |RS          |true            |false          |false         |
    |NON_SSP_ACCOUNT_WITHOUT_ETC_105D          |A            |true            |CM          |true            |false          |false         |

  @validCustomerBusinessNameCMFinalAccount @Phase1 @HappyFlow @SearchAccountTOFF @TC-88
  Scenario: SearchAccountsApi- Verify Response when a Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial Final Account TC_88
    When a request is made to the SearchAccounts Api with Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial final account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validAGLCAccountNumberRSActiveAccount @Phase1 @HappyFlow @SearchAccountTOFF @TC-103
  Scenario: SearchAccountsApi- Verify Response when a Valid AGLC Account Number Parameter with transactionType As TOFF is input For Residential Active Account TC_103
    When a request is made to the SearchAccounts Api with Valid AGLC Account Number Parameter with transactionType As TOFF is input For Residential Active account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @ValidAddressDetailsRSActiveAccount @Phase1 @HappyFlow @SearchAccountTOFF
  Scenario Outline: SearchAccountsApi- Verify Response when a Valid Address Details parameters with transactionType As TOFF is input For Residential Active Account <testCondition>
    When a request is made to the SearchAccounts Api with Valid Address Details parameters with transactionType As TOFF is input For Residential Active account "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
  Examples:
  |testCondition                             |
  |ALL_PREMISES_FIELDS_TC_104                |
  |PREMISES_STREET_NAME_CITY_STATE_ZIP_TC_105|


  #Encryption API is failing
#  @validSSNActiveRSPastDueBalance @Phase1 @HappyFlow @SearchAccountTOFF @TC-89
#  Scenario: SearchAccountsApi- Verify Response when a Valid SSN Parameter with transactionType As TOFF is input For Active RS Account with Past Due Balance TC_89
#    When a request is made to the SearchAccounts Api with Valid SSN Parameter with transactionType As TOFF is input For Active RS Account with Past Due Balance
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode 0 and ErrorMessage ""



