Feature: Verify SaveEnrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @SaveEnrollmentWithValidData @Phase1 @HappyFlow
  Scenario: Verify SaveEnrollment Api with valid data
    When a request is made to the SaveEnrollment Api
    Then verify response code of "SaveEnrollment" Api is <200>
