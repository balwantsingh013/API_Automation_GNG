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

  @validCustomerBusinessNameActPendReward @Phase1 @HappyFlow @SearchAccountTOFF
  Scenario: SearchAccountsApi- Verify Response For Verify that when a Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial Active And  Active/Pending Rewards Account TC_83
    When a request is made to the SearchAccounts Api with Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial Active And ActiveOrPending Rewards Account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeActiveRSNewNonMetered @Phase1 @HappyFlow @SearchAccountTOFF @TC-56
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for RS New and Non-metered account TC_56
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for RS New and Non-metered account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeActiveMSBAccount @Phase1 @HappyFlow @SearchAccountTOFF @TC-57
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for active MSB account TC_57
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for active MSB account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodectiActiveRSNewNonMetered @Phase1 @HappyFlow @SearchAccountTOFF @TC-58
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for RS Active and Non-metered account TC_58
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for RS Active and Non-metered account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeInactiveRSMetered @Phase1 @HappyFlow @SearchAccountTOFF @TC-59
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for RS Inactive and Metered account TC_59
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for RS Inactive and metered account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeInactiveBadDebtBalance @HappyFlow @SearchAccountTOFF @TC-60
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for Inactive account with bad debt balance TC_60
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for Inactive account with bad debt balance
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeRSInactiveWithSONP @HappyFlow @SearchAccountTOFF @TC-61
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for RS Inactive account with SONP TC_61
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for RS Inactive account with SONP
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeRSInactiveNonMeter @HappyFlow @SearchAccountTOFF @TC-62
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for RS Inactive Non-metered account TC_62
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for RS Inactive Non metered account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeRSNewBankrupcy @HappyFlow @SearchAccountTOFF @TC-63
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for RS New and Bankrupcy account TC_63
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for RS New and Bankrupcy account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeRSInactiveBankrupcy @HappyFlow @SearchAccountTOFF @TC-64
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for RS Inactive and Bankrupcy account TC_64
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for RS Inactive and Bankrupcy account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @validCustPremCodeCMNewNonMetered @HappyFlow @SearchAccountTOFF @TC-65
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for CM new and Bankrupcy account TC_65
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for CM New and Bankrupcy account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @CommercialActiveNonMetered @HappyFlow @SearchAccountTOFF @TC-67
  Scenario: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for CM Active Non-metered account TC_67
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for CM Active and Non metered account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""




