Feature: Get Reasons For Leaving Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetReasonsForLeavingEtcExistsTrue @Phase1 @HappyFlow @GetReasonsForLeavingTOFF
  Scenario: GetReasonsForLeavingApiTOFF- Verify Response when a Get reasons for leaving request is send with valid parameters TC_09
    When a request is made to the GetReasonsForLeaving Api with Valid parameters and etcExists flag is "true"
    Then verify response code of "GetReasonsForLeaving" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have 12 turnOffReasons
    And response should have turnOffReason with reasonForTurnOff as "Seasonal or Heat Only" and subReasonForTurnOff as ""
    And response should have reasonForTurnOffAlert as "Remind the customer to call us back in the Fall" for reasonForTurnOff "Seasonal or Heat Only"
    And response should have turnOffReason with reasonForTurnOff as "Moving" and subReasonForTurnOff as "Outside AGLC Territory/Outside Georgia - ETC Waived"
    And response should have reasonForTurnOffAlert as "" for reasonForTurnOff "Moving"
    And response should have turnOffReason with reasonForTurnOff as "Moving" and subReasonForTurnOff as "Outside Pool (Delivery) Group - ETC Waived"
    And response should have turnOffReason with reasonForTurnOff as "Moving" and subReasonForTurnOff as "Service Transfer - ETC Waived"
    And response should have turnOffReason with reasonForTurnOff as "Moving" and subReasonForTurnOff as "Within Pool Group but Not Staying with GNG"
    And response should have turnOffReason with reasonForTurnOff as "REAP/Realtor Inspection" and subReasonForTurnOff as ""
    And response should have reasonForTurnOffAlert as "" for reasonForTurnOff "REAP/Realtor Inspection"
    And response should have turnOffReason with reasonForTurnOff as "Household Account Change" and subReasonForTurnOff as ""
    And response should have reasonForTurnOffAlert as "" for reasonForTurnOff "Household Account Change"
    And response should have turnOffReason with reasonForTurnOff as "Other" and subReasonForTurnOff as "Regulated Provider - ETC Waived"
    And response should have turnOffReason with reasonForTurnOff as "Other" and subReasonForTurnOff as "Military - ETC Waived"
    And response should have turnOffReason with reasonForTurnOff as "Other" and subReasonForTurnOff as "Renovation/Electric Conversion"
    And response should have turnOffReason with reasonForTurnOff as "Other" and subReasonForTurnOff as "Deceased - ETC Waived"
    And response should have turnOffReason with reasonForTurnOff as "Other" and subReasonForTurnOff as "Financial Situation"


  @GetReasonsForLeavingEtcExistsFalse @Phase1 @HappyFlow @GetReasonsForLeavingTOFF
  Scenario: GetReasonsForLeavingApiTOFF- Verify Response when a Get reasons for leaving request is send with valid parameters TC-10
    When a request is made to the GetReasonsForLeaving Api with Valid parameters and etcExists flag is "false"
    Then verify response code of "GetReasonsForLeaving" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have 12 turnOffReasons
    And response should have turnOffReason with reasonForTurnOff as "Seasonal or Heat Only" and subReasonForTurnOff as ""
    And response should have reasonForTurnOffAlert as "Remind the customer to call us back in the Fall" for reasonForTurnOff "Seasonal or Heat Only"
    And response should have turnOffReason with reasonForTurnOff as "Moving" and subReasonForTurnOff as "Outside AGLC Territory/Outside Georgia"
    And response should have reasonForTurnOffAlert as "" for reasonForTurnOff "Moving"
    And response should have turnOffReason with reasonForTurnOff as "Moving" and subReasonForTurnOff as "Outside Pool (Delivery) Group"
    And response should have turnOffReason with reasonForTurnOff as "Moving" and subReasonForTurnOff as "Service Transfer"
    And response should have turnOffReason with reasonForTurnOff as "Moving" and subReasonForTurnOff as "Within Pool Group but Not Staying with GNG"
    And response should have turnOffReason with reasonForTurnOff as "REAP/Realtor Inspection" and subReasonForTurnOff as ""
    And response should have reasonForTurnOffAlert as "" for reasonForTurnOff "REAP/Realtor Inspection"
    And response should have turnOffReason with reasonForTurnOff as "Household Account Change" and subReasonForTurnOff as ""
    And response should have reasonForTurnOffAlert as "" for reasonForTurnOff "Household Account Change"
    And response should have turnOffReason with reasonForTurnOff as "Other" and subReasonForTurnOff as "Regulated Provider"
    And response should have turnOffReason with reasonForTurnOff as "Other" and subReasonForTurnOff as "Military"
    And response should have turnOffReason with reasonForTurnOff as "Other" and subReasonForTurnOff as "Renovation/Electric Conversion"
    And response should have turnOffReason with reasonForTurnOff as "Other" and subReasonForTurnOff as "Deceased"
    And response should have turnOffReason with reasonForTurnOff as "Other" and subReasonForTurnOff as "Financial Situation"