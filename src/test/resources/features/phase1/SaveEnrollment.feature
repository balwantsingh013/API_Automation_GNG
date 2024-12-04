Feature: Verify SaveEnrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @SaveEnrollmentWithValidData @Phase1 @HappyFlow
  Scenario: Verify SaveEnrollment Api with valid data
    When a request is made to the SaveEnrollment Api
    Then verify response code of "SaveEnrollment" Api is <200>

  @SaveEnrollmentWithInvalidRequestID @Phase1 @NegativeFlow
  Scenario Outline: Verify SaveEnrollment Api with invalid requestID "<requestID>"
    When a request is made to the SaveEnrollment Api with "<requestID>"
    Then verify response code of "SaveEnrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID | errorCode  | errorMessage                    |
      |           | 10001      | Missing Request ID              |
      | 123       | 10003      | Duplicate Request ID            |

