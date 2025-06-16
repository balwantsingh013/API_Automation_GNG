Feature: Verify SearchAccounts TurnOff Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

    @InvalidAccountParameters @Phase1 @HappyFlow @SearchAccountTOFF
    Scenario: SearchAccountsApi-Verify Response when an invalid Banner Account Number parameters with transactionType As TOFF is input TC_74
    When a request is made to the SearchAccounts Api with invalid Banner Account Number parameter with transactionType As TOFF TC_74
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 0

    @ValidRSOrCMAccountParameter @Phase1 @HappyFlow @SearchAccountTOFF
    Scenario Outline: SearchAccountsApi- Verify Response when an Valid Banner Account Number parameters with transactionType As TOFF is input For "<accountType>"
    When a request is made to the SearchAccounts Api with valid Banner Account Number parameters with transactionType As TOFF For "<accountType>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
      | accountType                          |
      | RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC75|
      | COMMERCIAL_VALID_ACTIVE_ACCOUNT_TC76 |

    @Invalid_ValidLastNameZipcode @Phase1 @HappyFlow @SearchAccountTOFF
    Scenario Outline: SearchAccountsApi- Verify Response  when a Last Name And Zipcode And transactionType As TOFF is input With "<combinationType>"
    When a request is made to the SearchAccounts Api with Last Name And Zipcode And transactionType As TOFF With "<combinationType>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
      |combinationType                             |
      |INVALID_COMBINATION_OF_LASTNAME_ZIPCODE_TC77|
      |VALID_COMBINATION_OF_LASTNAME_ZIPCODE_TC78  |

    @validFirstNameLastNameZipcodeRSOrSR @Phase1 @HappyFlow @SearchAccountTOFF
    Scenario Outline: SearchAccountsApi- Verify Response when a Valid Combination Of First Name, Last Name And Zipcode with transactionType As TOFF is input For "<accountType>"
    When a request is made to the SearchAccounts Api with First Name, Last Name And Zipcode with transactionType As TOFF is input For "<accountType>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
      |accountType                                 |
      |RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC79       |
      |SENIOR_RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC80|


    @validCustomerBusinessNamePastDueBalanceOrSONP @Phase1 @HappyFlow @SearchAccountTOFF
    Scenario Outline: SearchAccountsApi- Verify Response For Verify that when a Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial Active And "<accountType>"
    When a request is made to the SearchAccounts Api with Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial Active And "<accountType>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
      |accountType                                        |
      |COMMERCIAL_VALID_ACTIVE_PASTDUEBALANCE_ACCOUNT_TC81|
      |COMMERCIAL_VALID_ACTIVE_SONP_ACCOUNT_TC82          |

    @validCustomerBusinessNameActPendReward @Phase1 @HappyFlow @SearchAccountTOFF
    Scenario: SearchAccountsApi- Verify Response For Verify that when a Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial Active And  Active/Pending Rewards Account TC_83
    When a request is made to the SearchAccounts Api with Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial Active And ActiveOrPending Rewards Account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeCommercialInactiveMetered @HappyFlow @SearchAccountTOFF @TC-68
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for CM Inactive metered account TC_68
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for CM inactive and metered account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeCommercialInactiveBadDebt @HappyFlow @SearchAccountTOFF @TC-69
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for CM Inactive account with bad debt TC_69
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for CM inactive account with bad debt
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeCommercialInactiveSONP @HappyFlow @SearchAccountTOFF @TC-70
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for CM Inactive account with SONP TC_70
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for CM inactive account with SONP
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeCommercialInactiveNonMetered @HappyFlow @SearchAccountTOFF @TC-71
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for CM Inactive Non-metered account TC_71
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for CM inactive non-metered account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeCommercialNewAndBankrupcy @HappyFlow @SearchAccountTOFF @TC-72
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for CM New and Bankrupcy account TC_72
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for CM New and Bankrupcy account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeCommercialInactiveAndBankrupcy @HappyFlow @SearchAccountTOFF @TC-73
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for CM Inactive and Bankrupcy account TC_73
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for CM Inactive and Bankrupcy account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

#updated
  @validCustPremCode @Phase1 @HappyFlow @SearchAccountTOFF
  Scenario Outline: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for <testCondition>
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
    |testCondition|
    |ACTIVE_RS_NEW_NON_METERED_TC_56|
    |ACTIVE_MSB_ACCOUNT_TC_57|
    |ACTIVE_RS_NEW_NON_METERED_TC_58|
    |INACTIVE_RS_METERED_TC_59      |
    |INACTIVE_BAD_DEBT_BALANCE_TC_60|
    |RS_INACTIVE_WITH_SONP_TC_61|
    |RS_INACTIVE_NON_METER_TC_62|
    |RS_NEW_BANKRUPCY_TC_63     |
    |RS_INACTIVE_BANKRUPCY_TC_64|
    |CM_NEW_NON_METERED_TC_65   |
    |CM_ACTIVE_NON_METERED_TC_67|




