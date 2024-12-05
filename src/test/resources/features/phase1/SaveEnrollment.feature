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
      | requestID                  | errorCode | errorMessage                    |
      | EMPTY_REQUEST_ID           | 10001     | Missing Request ID              |
      | DUPLICATE_REQUEST_ID       | 10003     | Duplicate Request ID            |
      | SPECIAL_CHARS_REQUEST_ID   | 10004     | Invalid Request ID Format       |
      | LONG_REQUEST_ID            | 10002     | Invalid Request ID              |
      | UNICODE_CHARS_REQUEST_ID   | 10007     | Unsupported Characters          |

  @SaveEnrollmentInvalidCustomerCODE @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<customerCODE>" code
    When a request is made to the SaveEnrollment Api with invalid "<customerCODE>" code
    Then verify response code of "Save Enrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | customerCODE                | errorCode | errorMessage                 |
      | MAX_LENGTH_CUSTOMER_CODE    | 10000     | The Customer Code must be an integer with a maximum length of 9 |


  @SaveEnrollmentInvalidPromotionCODE @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<premisesCODE>" code
    When a request is made to the SaveEnrollment Api with  premises "<premisesCODE>" code
    Then verify response code of "Save Enrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesCODE                | errorCode | errorMessage                 |
      | MAX_LENGTH_CUSTOMER_CODE    | 10000     | The Customer Code must be an integer with a maximum length of 9 |
