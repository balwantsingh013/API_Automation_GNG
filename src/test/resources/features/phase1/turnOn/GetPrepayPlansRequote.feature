Feature: Verify GetPrepayPlansRequote Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetPrepayPlansRequotePositive @HappyFlow
  Scenario Outline: GetPrepayPlansRequote API – returns quotes for <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then the response should contain the expected plans
    Then the response plans should contains a "<planCode>" plan
    Then a request is made to get Marketer Reference Data
    And a request is made to the SaveEnrollment Api for with "<planCode>" planCode for "<testCondition>" condition
    Then verify response code of "SaveEnrollment" Api is 200
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the GetPrepayPlansRequote Api for "<testCondition>" condition
    Then verify response code of "GetPrepayPlansRequote" Api is 200
    Then the response should contain the expected prepay plans and "<planCode>" planCode

    Examples:
      | planCode | testCondition                            |
      | PRP      | GET_PREPAY_PLANS_REQUOTE_POSITIVE_TC_456 |
      | PRP      | GET_PREPAY_PLANS_REQUOTE_POSITIVE_TC_471 |
      | PGB      | GET_PREPAY_PLANS_REQUOTE_POSITIVE_TC_472 |

  @GetPrepayPlansRequoteNegativeInvalidRequestId @Phase1 @NegativeFlow
  Scenario Outline: GetPrepayPlansRequote Api - Verify GetPrepayPlansRequote Api with invalid requestID for "<testCondition>" condition
    When a request is made to the GetPrepayPlansRequote Api with an invalid requestID for "<testCondition>" condition
    Then verify response code of "GetPrepayPlansRequote" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                            | errorCode | errorMessage         |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_457 | 10001     | Missing Request ID   |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_458 | 10002     | Invalid Request ID   |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_456 | 10003     | Duplicate Request ID |

  @GetPrepayPlansRequoteNegativeInvalidLoginId @Phase1 @NegativeFlow
  Scenario Outline: GetPrepayPlansRequote Api - Verify GetPrepayPlansRequote Api with invalid requestID for "<testCondition>" condition
    When a request is made to the GetPrepayPlansRequote Api with an invalid loginID for "<testCondition>" condition
    Then verify response code of "GetPrepayPlansRequote" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                            | errorCode | errorMessage                                              |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_459 | 10000     | Missing Login ID                                          |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_460 | 10000     | The Login ID must be a string with a maximum length of 30 |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_461 | 2000      | Invalid Login ID                                          |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_462 | 2000      | Invalid Login ID                                          |

  @GetPrepayPlansRequoteNegativeConditions @Phase1 @NegativeFlow
  Scenario Outline: GetPrepayPlansRequote Api - Verify GetPrepayPlansRequote Api for "<testCondition>" negative condition
    When a request is made to the GetPrepayPlansRequote Api for "<testCondition>" negative condition
    Then verify response code of "GetPrepayPlansRequote" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                            | errorCode | errorMessage                                                                                                                                |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_463 | 10000     | Missing Transaction ID                                                                                                                      |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_464 | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int32]. Path: $.transactionID \| LineNumber: 0 \| BytePositionInLine: 67. |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_465 | 2000      | Invalid Request: Prepay Data Not Found.                                                                                                     |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_466 | 10000     | Missing Transaction Type                                                                                                                    |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_467 | 10000     | The Transaction Type must be a string with a maximum length of 4                                                                            |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_468 | 1000      | Invalid Request: Invalid Transaction Type                                                                                                   |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_468 | 1000      | Invalid Request: Invalid Transaction Type                                                                                                   |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_468 | 1000      | Invalid Request: Invalid Transaction Type                                                                                                   |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_468 | 1000      | Invalid Request: Invalid Transaction Type                                                                                                   |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_468 | 1000      | Invalid Request: Invalid Transaction Type                                                                                                   |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_461 | 2000      | Invalid Login ID                                                                                                                            |
      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_462 | 2000      | Invalid Login ID                                                                                                                            |

  @GetPrepayPlansRequoteNegativeCompleteFlow @NegativeFlow
  Scenario Outline: GetPrepayPlansRequote API – returns quotes for <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then the response should contain the expected plans
    Then the response plans should contains a "<planCode>" plan
    Then a request is made to get Marketer Reference Data
    And a request is made to the SaveEnrollment Api for with "<planCode>" planCode for "<testCondition>" condition
    Then verify response code of "SaveEnrollment" Api is 200
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the GetPrepayPlansRequote Api with complete flow for negative "<testCondition>" condition
    Then verify response code of "GetPrepayPlansRequote" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | planCode | testCondition                                                       | errorCode | errorMessage                                        |
      | PRP      | GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_469                            | 2000      | Invalid Request: Existing Prepay Quote Not Expired. |
      | PGB      | GET_PREPAY_PLANS_REQUOTE_EXISTING_QUOTE_NOT_EXPIRED_NEGATIVE_TC_470 | 2000      | Invalid Request: Existing Prepay Quote Not Expired. |
      | PRP      | GET_PREPAY_PLANS_REQUOTE_PRP_ENROLLED_NEGATIVE_TC_471               | 2000      | Invalid Request: Existing Prepay Quote Not Expired. |

