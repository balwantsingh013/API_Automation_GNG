Feature: Verify GetReasonsForLeaving Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetReasonsForLeavingInvalidParameters @NegativeFlow @Phase1
  Scenario Outline: GetReasonsForLeaving Api - Verify invalid requestID "<testCondition>"
    When a request is made to the GetReasonsForLeaving Api with invalid parameters for "<testCondition>" condition
    Then verify response code of "GetReasonsForLeaving" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                   | errorCode | errorMessage                                                     |
      | DUPLICATE_REQUEST_ID_NEGATIVE_TC76              | 10003     | Duplicate Request ID                                             |
      | MISSING_REQUEST_ID_NEGATIVE_TC77                | 10001     | Missing Request ID                                               |
      | INVALID_REQUEST_ID_LENGTH_NEGATIVE_TC78         | 10002     | Invalid Request ID                                               |
      | MISSING_LOGIN_ID_NEGATIVE_TC79                  | 10000     | Missing Login ID                                                 |
      | INVALID_LOGIN_ID_TOO_LONG_NEGATIVE_TC80         | 10000     | The Login ID must be a string with a maximum length of 30        |
      | INVALID_LOGIN_ID_NOT_ALPHANUMERIC_NEGATIVE_TC81 | 2000      | Invalid Login ID                                                 |
      | INVALID_LOGIN_ID_NOT_FOUND_NEGATIVE_TC82        | 2000      | Invalid Login ID                                                 |
      | MISSING_TRANSACTION_TYPE_NEGATIVE_TC84          | 10000     | Missing Transaction Type                                         |
      | INVALID_TRANSACTION_TYPE_VALUE_NEGATIVE_TC85    | 1000      | Invalid Request: Invalid Transaction Type                        |
      | INVALID_TRANSACTION_TYPE_LENGTH_NEGATIVE_TC86   | 10000     | The Transaction Type must be a string with a maximum length of 4 |



  @GetReasonsForLeavingPositive @Phase1 @HappyFlow
  Scenario Outline: GetReasonsForLeaving Api - Verify positive responses for "<testCondition>"
    When a request is made to the GetReasonsForLeaving Api with valid parameters for "<testCondition>" condition
    Then verify response code of "GetReasonsForLeaving" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the response should contain the "<turnOffReasonSubReason>" turnOffReason SubReason

    Examples:
      | testCondition                                                  | turnOffReasonSubReason             |
      | ETC_EXISTS_TRUE_RETURNS_MOVING_SERVICE_TRANSFER_POSITIVE_TC87  | MOVING_SERVICE_TRANSFER_ETC_WAIVED |
      | ETC_EXISTS_FALSE_RETURNS_NO_RECORDS_POSITIVE_TC88              |                                    |
