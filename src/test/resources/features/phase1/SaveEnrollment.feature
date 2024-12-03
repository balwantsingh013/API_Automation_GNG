Feature: Verify SaveEnrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

@SaveEnrollmentWithValidData @Phase1 @HappyFlow
    Scenario: Verify SaveEnrollment API with valid data
    Given a valid SaveEnrollment API request payload
    When a request is made to the SaveEnrollment Api
    Then verify response code of "SaveEnrollment" Api is <200>
    And the response should contain a valid transactionID

@SaveEnrollmentInvalidPromotionCode @Phase1
Scenario: Verify response code for invalid promotion code
  When a request is made to the Save Enrollment API with an invalid promotion code "INVALID_CODE"
  Then verify response code of "Save Enrollment" Api is <400>
  And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"


@SaveEnrollmentInvalidCustomerCode @Phase1
Scenario: Verify response code for invalid customer code
  When a request is made to the Save Enrollment API with an invalid customer code "12345678910"
  Then verify response code of "Save Enrollment" Api is <400>
  And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"