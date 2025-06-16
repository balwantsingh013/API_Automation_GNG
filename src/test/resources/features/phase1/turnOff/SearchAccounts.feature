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

#updated
  @validCustPremCode @Phase1 @HappyFlow @SearchAccountTOFF
  Scenario Outline: SearchAccountsApi- Verify Response when a valid customer and premisesCode are provided for "TOFF" for <testCondition>
    When a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
    |testCondition                          |
    |ACTIVE_RS_NEW_NON_METERED_TC_56        |
    |ACTIVE_MSB_ACCOUNT_TC_57               |
    |ACTIVE_RS_NEW_NON_METERED_TC_58        |
    |INACTIVE_RS_METERED_TC_59              |
    |INACTIVE_BAD_DEBT_BALANCE_TC_60        |
    |RS_INACTIVE_WITH_SONP_TC_61            |
    |RS_INACTIVE_NON_METER_TC_62            |
    |RS_NEW_BANKRUPCY_TC_63                 |
    |RS_INACTIVE_BANKRUPCY_TC_64            |
    |CM_NEW_NON_METERED_TC_65               |
    |CM_ACTIVE_NON_METERED_TC_67            |
    |CM_INACTIVE_METERED_TC_68              |
    |CM_INACTIVE_BAD_DEBT_TC_69             |
    |CM_INACTIVE_SONP_TC_70                 |
    |CM_INACTIVE_NON_METERED_TC_71          |
    |CM_NEW_BANKRUPCY_TC_72                 |
    |CM_INACTIVE_BANKRUPCY_TC_73            |
    |VALID_CUST_CODE_INVALID_PREM_CODE_TC_74|
    |RS_ACTIVE_ACCOUNT_TC_75                |
    |CM_ACTIVE_ACCOUNT_TC_76                |



