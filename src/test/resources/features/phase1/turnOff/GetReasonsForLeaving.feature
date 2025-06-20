Feature: Get Reasons For Leaving Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetReasonsForLeavingEtcExistsTrueOrFalse @Phase1 @HappyFlow @GetReasonsForLeavingTOFF
  Scenario Outline: GetReasonsForLeavingApiTOFF- Verify Response when a Get reasons for leaving request is send with valid parameters
    When a request is made to the GetReasonsForLeaving Api with Valid parameters and etcExists flag is "<etcExistsValue>"
    Then verify response code of "GetReasonsForLeaving" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have 12 turnOffReasons
    And response should have turnOffReason with reasonForTurnOff as "Seasonal or Heat Only" and subReasonForTurnOff as ""
    And response should have reasonForTurnOffAlert as "Remind the customer to call us back in the Fall" for reasonForTurnOff "Seasonal or Heat Only"

  Examples:
    | etcExistsValue|
    | true          |
    | false         |