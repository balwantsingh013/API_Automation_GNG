Feature: Verify SearchAccounts TurnOff Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

@InvalidAccountParameters @Phase1 @HappyFlow @SATOFF
Scenario: Verify Response when an invalid Banner Account Number parameters with transactionType As TOFF is input TC_74
When a request is made to the SearchAccounts Api with invalid Banner Account Number parameter with transactionType As TOFF TC_74
Then verify response code of "SearchAccounts" Api is 200
And response should have ErrorCode 0 and ErrorMessage ""
And response should return numberOfMatches as 0

  @ValidRSOrCMAccountParameter @Phase1 @HappyFlow @SATOFF
  Scenario Outline:  Verify Response when an Valid Banner Account Number parameters with transactionType As TOFF is input For Residential/Commercial Active Account  TC75_TC76
    When a request is made to the SearchAccounts Api with valid Banner Account Number parameters with transactionType As TOFF For a "<Acct_type>" Active Account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
      | Acct_type |
      |   RS      |
      |   CM      |

  @Invalid_ValidLastNameZipcode @Phase1 @HappyFlow @SATOFF
  Scenario Outline: Verify Response  when a InValid/Valid Combination Of Last Name And Zipcode with transactionType As TOFF is input  TC77_TC78
    When a request is made to the SearchAccounts Api with "<combination>" Combination Of Last Name And Zipcode with transactionType As TOFF
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
      | combination |
      |   InValid   |
      |   Valid     |

  @validFirstNameLastNameZipcodeRSOrSR @Phase1 @HappyFlow @SATOFF
  Scenario Outline: Verify Response  when a Valid Combination Of First Name, Last Name And Zipcode with transactionType As TOFF is input For Residential Metered Account TC79_TC80
    When a request is made to the SearchAccounts Api with First Name, Last Name And Zipcode with transactionType As TOFF is input For "<Acct_type>" Account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
      | Acct_type |
      |    RS     |
      |    SR     |


  @validCustomerBusinessNamePastDueBalanceOrSONP @Phase1 @HappyFlow @SATOFF
  Scenario Outline: Verify Response For Verify that when a Valid CustomerBusinessName Parameter with transactionType As TOFF is input For "<Acct_type>" Account TC81_TC82
    When a request is made to the SearchAccounts Api with Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial Active And "<Acct_type>" Account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
      | Acct_type     |
      | PastDueBalance|
      |    SONP       |


  @validCustomerBusinessNameActPendReward @Phase1 @HappyFlow @SATOFF
  Scenario: Verify Response For Verify that when a Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial Active And  Active/Pending Rewards Account TC_82
    When a request is made to the SearchAccounts Api with Valid CustomerBusinessName Parameter with transactionType As TOFF is input For Commercial Active And ActiveOrPending Rewards Account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""



