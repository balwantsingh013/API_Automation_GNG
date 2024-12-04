Feature: Verify SaveEnrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

@SaveEnrollmentWithValidData @Phase1 @HappyFlow
    Scenario: Verify SaveEnrollment API with valid data
    When a valid SaveEnrollment API request payload
    Then verify response code of "SaveEnrollment" Api is <200>


@SaveEnrollmentInvalidPromotionCode @Phase1
Scenario: Verify response code for invalid promotion code
  When a request is made to the SaveEnrollment Api with invalid "<param>" code
  Then verify response code of "Save Enrollment" Api is <200>
  And response should have ErrorCode <10000> and ErrorMessage "<errorMessage>"

  @SaveEnrollmentInvalidParam @Phase1
  Scenario Outline: Verify response code for invalid "<param>" code
    When a request is made to the SaveEnrollment Api with invalid "<param>" code
    Then verify response code of "Save Enrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | param        | errorCode | errorMessage                 |
      | CustomerCode | 10000     | The Customer Code must be an integer with a maximum length of 9 |
      | PromoCode    | 10000     | The Promotion Code must be a string with a maximum length of 35 |

