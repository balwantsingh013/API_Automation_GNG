Feature: Get Reasons For Leaving Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetReasonsForLeavingEtcExistsTrue @Phase1 @turnOff @HappyFlow @GetReasonsForLeavingTOFF
  Scenario: GetReasonsForLeavingApiTOFF- Verify Response when a Get reasons for leaving request is send with valid parameters TC_09
    When a request is made to the GetReasonsForLeaving Api with Valid parameters and etcExists flag is "true"
    Then verify response code of "GetReasonsForLeaving" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have 12 turnOffReasons
    And the response should contain the following turnOffReasons:
      | reasonForTurnOff           | subReasonForTurnOff                                |
      | Seasonal or Heat Only      |                                                    |
      | Moving                     | Outside AGLC Territory/Outside Georgia - ETC Waived|
      | Moving                     | Outside Pool (Delivery) Group - ETC Waived         |
      | Moving                     | Service Transfer - ETC Waived                      |
      | Moving                     | Within Pool Group but Not Staying with GNG         |
      | REAP/Realtor Inspection    |                                                    |
      | Household Account Change   |                                                    |
      | Other                      | Regulated Provider - ETC Waived                    |
      | Other                      | Military - ETC Waived                              |
      | Other                      | Renovation/Electric Conversion                     |
      | Other                      | Deceased - ETC Waived                              |
      | Other                      | Financial Situation                                |
    And the response should contain the following reasonForTurnOffAlerts:
      | reasonForTurnOff           | reasonForTurnOffAlert                              |
      | Seasonal or Heat Only      | Remind the customer to call us back in the Fall    |
      | Moving                     |                                                    |
      | REAP/Realtor Inspection    |                                                    |
      | Household Account Change   |                                                    |
      | Other                      |                                                    |

  @GetReasonsForLeavingEtcExistsFalse @Phase1 @turnOff @HappyFlow @GetReasonsForLeavingTOFF
  Scenario: GetReasonsForLeavingApiTOFF - Verify Response when a Get reasons for leaving request is sent with valid parameters TC-10
    When a request is made to the GetReasonsForLeaving Api with Valid parameters and etcExists flag is "false"
    Then verify response code of "GetReasonsForLeaving" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have 12 turnOffReasons
    And the response should contain the following turnOffReasons:
      | reasonForTurnOff           | subReasonForTurnOff                                |
      | Seasonal or Heat Only      |                                                    |
      | Moving                     | Outside AGLC Territory/Outside Georgia             |
      | Moving                     | Outside Pool (Delivery) Group                      |
      | Moving                     | Service Transfer                                   |
      | Moving                     | Within Pool Group but Not Staying with GNG         |
      | REAP/Realtor Inspection    |                                                    |
      | Household Account Change   |                                                    |
      | Other                      | Regulated Provider                                 |
      | Other                      | Military                                           |
      | Other                      | Renovation/Electric Conversion                     |
      | Other                      | Deceased                                           |
      | Other                      | Financial Situation                                |
    And the response should contain the following reasonForTurnOffAlerts:
      | reasonForTurnOff           | reasonForTurnOffAlert                              |
      | Seasonal or Heat Only      | Remind the customer to call us back in the Fall    |
      | Moving                     |                                                    |
      | REAP/Realtor Inspection    |                                                    |
      | Household Account Change   |                                                    |


  @GetReasonsForLeavingNegative @Phase1 @turnOff @NegativeFlow @GetReasonsForLeavingTOFF
  Scenario Outline: GetReasonsForLeavingApiTOFF- Verify Response when a Get reasons for leaving request is send with invalid parameters <testCondition>
    When a request is made to the GetReasonsForLeaving Api for invalid requestId "<testCondition>"
    Then verify response code of "GetReasonsForLeaving" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
    |testCondition                |errorCode|errorMessage             |
    |TC1_DUPLICATE_REQUEST_ID     |10003    |Duplicate Request ID     |
    |TC2_NULL_REQUEST_ID          |10001    |Missing Request ID       |
    |TC3_INVALID_REQUEST_ID       |10002    |Invalid Request ID       |

  @GetReasonsForLeavingNegative1 @Phase1 @turnOff @NegativeFlow @GetReasonsForLeavingTOFF
  Scenario Outline: GetReasonsForLeavingApiTOFF- Verify Response when a Get reasons for leaving request is send with invalid parameters <testCondition>
    When a request is made to the GetReasonsForLeaving Api for invalid loginId "<testCondition>"
    Then verify response code of "GetReasonsForLeaving" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      |testCondition                |errorCode|errorMessage             |
      |TC4_1_LOGIN_ID_EMPTY     |10000    |Missing Login ID     |
      |TC4_2_LOGIN_ID_NULL          |10000    |Missing Login ID      |
      |TC5_LOGIN_ID_INVALID_MAX_LENGTH_VALIDATION          |10000    |The Login ID must be a string with a maximum length of 30      |
      |TC6_LOGIN_ID_INVALID_WITH_SPECIAL_CHARACTERS          |2000    |Invalid Login ID      |
      |TC7_LOGIN_ID_INVALID          |2000    |Invalid Login ID      |

  @GetReasonsForLeavingNegative2 @Phase1 @turnOff @NegativeFlow @GetReasonsForLeavingTOFF
  Scenario: GetReasonsForLeavingApiTOFF- Verify Response when a Get reasons for leaving request is send with invalid parameters TC8_ETC_EXISTS_NULL
    When a request is made to the GetReasonsForLeaving Api for "TC8_ETC_EXISTS_NULL"
    Then verify response code of "GetReasonsForLeaving" Api is 200
    And response should have ErrorCode 10000 and ErrorMessage "Missing Etc Exists flag"