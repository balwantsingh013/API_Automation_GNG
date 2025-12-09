Feature: Verify GetMarketerReferenceData Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetMarketerReferenceDataInvalid @NegativeFlow @Phase2 @serviceTransfer
  Scenario Outline: GetMarketerReferenceData Api - Verify invalid request for "<testCondition>"
    When a request is made to the GetMarketerReferenceData Api with invalid parameters for "<testCondition>"
    Then verify response code of "GetMarketerReferenceData" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                         | errorCode | errorMessage           |
      | DUPLICATE_REQUEST_ID_TC91             | 10003     | Duplicate Request ID   |
      | MISSING_REQUEST_ID_TC89               | 10001     | Missing Request ID     |
      | INVALID_REQUEST_ID_LENGTH_TC90        | 10002     | Invalid Request ID     |
      | MISSING_LOGIN_ID_TC92                 | 10000     | Missing Login ID       |
      | INVALID_LOGIN_ID_NOT_ALPHANUMERIC_TC93| 2000      | Invalid Login ID       |
      | INVALID_LOGIN_ID_NOT_FOUND_TC94       | 2000      | Invalid Login ID       |

  @GetMarketerReferenceDataValid @Phase2 @HappyFlow @serviceTransfer
  Scenario: GetMarketerReferenceData Api - Verify positive response for GetMarketerReferenceData
    When a request is made to the GetMarketerReferenceData Api with valid parameters
    Then verify response code of "GetReasonsForLeaving" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the MarketerReferenceData is of length 12

  @GetMarketerReferenceDataUniquenessCheck @Phase2 @HappyFlow @serviceTransfer
  Scenario: GetMarketerReferenceData Api - Verify positive response for GetMarketerReferenceData
    When a request is made to the GetMarketerReferenceData Api with valid parameters
    Then verify response code of "GetReasonsForLeaving" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the GetMarketerReferenceData Api with valid parameters
    Then verify if the newly generated marketer reference data is not matching with the previous one