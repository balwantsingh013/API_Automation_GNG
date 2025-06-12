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



