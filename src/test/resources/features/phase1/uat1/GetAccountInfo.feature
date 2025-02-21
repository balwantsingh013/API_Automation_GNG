Feature: Verify GetAccountInfo Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetAccountInfoWithValidData @Phase1 @HappyFlow
  Scenario: Verify GetAccountInfo Api with valid data
    When a request is made to the GetAccountInfo Api
    Then verify response code of "GetAccountInfo" Api is 200
    Then verify the account information in the response should match the information in the database

  @GetAccountInfoMissingParam @Phase1
  Scenario Outline: Verify GetAccountInfo Api request with missing "<param>"
    When a request is made to the GetAccountInfo Api with missing param "<param>"
    Then verify response code of "GetAccountInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | param        | errorCode | errorMessage          |
      | RequestID    | 10001     | Missing Request ID    |
      | PremisesCode | 10013     | Missing Premises Code |
      | CustomerCode | 10011     | Missing Customer Code |

  @GetAccountInfoInvalidParamLength @Phase1
  Scenario Outline: Verify GetAccountInfo Api request with Invalid "<param>" Length
    When a request is made to the GetAccountInfo Api with invalid param "<param>" length
    Then verify response code of "GetAccountInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | param        | errorCode | errorMessage                 |
      | PremCode     | 10005     | Invalid Premises Code Format |
      | CustomerCode | 10015     | Invalid Customer Code Format |

  @GetAccountInfoDuplicateRequestID @Phase1
  Scenario: Verify GetAccountInfo Api request with Duplicate requestID
    When a request is made to the GetAccountInfo Api with duplicate requestID
    Then verify response code of "GetAccountInfo" Api is 200
    And response should have ErrorCode 10003 and ErrorMessage 'Duplicate Request ID'

  @GetAccountInfoNonExistentCustPremCode @Phase1
  Scenario: Verify GetAccountInfo Api request with non-existent combination of custCode and premCode
    When a request is made to the GetAccountInfo Api with non-existent combination of custCode and premCode
    Then verify response code of "GetAccountInfo" Api is 200
    And response should have ErrorCode 40015 and ErrorMessage 'Invalid Account Number'