Feature: Get Reasons For Leaving Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @EtcExistsBasedValidation @Phase1 @HappyFlow @GetReasonsForLeavingTOFF
  Scenario: GetReasonsForLeavingApiTOFF- Verify Response when a valid
