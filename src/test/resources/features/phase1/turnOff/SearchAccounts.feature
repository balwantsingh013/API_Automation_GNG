Feature: Verify SearchAccounts TurnOff Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

@SearchAccountsInvalidParameter1 @Phase1 @HappyFlow @SATOFF
Scenario: Verify Response For Verify that when an invalid Banner Account Number parameter with transactionType As TOFF is input TC_74
When a request is made to the SearchAccounts Api with invalid Banner Account Number parameter with transactionType As TOFF TC_74
Then verify response code of "SearchAccounts" Api is 200
And response should have ErrorCode 0 and ErrorMessage ""
And response should return numberOfMatches as 0

  @SearchAccountsInvalidParameter2 @Phase1 @HappyFlow @SATOFF
  Scenario: Verify Response For Verify that when an Valid Banner Account Number parameter with transactionType As TOFF is input For Residential Active Account TC_75
    When a request is made to the SearchAccounts Api with valid Banner Account Number parameter with transactionType As TOFF For a "Residential" Active Account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @SearchAccountsInvalidParameter3 @Phase1 @HappyFlow @SATOFF
  Scenario: Verify Response For Verify that when an Valid Banner Account Number parameter with transactionType As TOFF is input  For Residential Active Account  TC_75
    When a request is made to the SearchAccounts Api with valid Banner Account Number parameter with transactionType As TOFF For a "Commercial" Active Account
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

