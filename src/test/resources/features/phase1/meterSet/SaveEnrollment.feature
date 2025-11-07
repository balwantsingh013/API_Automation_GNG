Feature: Verify MeterSet SaveEnrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @MeterSetSaveEnrollmentNegative @NegativeFlow @Phase1
  Scenario Outline: Verify MeterSet SaveEnrollment with invalid parameters "<testCondition>"
    Given a request is made to get Marketer Reference Data
    When a request is made to the SaveEnrollment Api for meterSet with invalid parameters for "<testCondition>" condition
    Then verify response code of "MeterSet/SaveEnrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                     | errorCode | errorMessage                                                                                                                                                     |




  @MeterSetSaveEnrollmentPositive @HappyFlow @Phase1
  Scenario Outline: Verify MeterSet SaveEnrollment with valid parameters "<testCondition>"
    Given a request is made to get Marketer Reference Data
    When a request is made to the SaveEnrollment Api for external meterSet calls with valid parameters for "<testCondition>" condition
    Then verify response code of "MeterSet/SaveEnrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                     | errorCode | errorMessage                                                                                                                                                     |






