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

    Given a request is made to get Marketer Reference Data
    When a request is made to the SaveEnrollment Api for prepay with "<planCode>" planCode for "<testCondition>" condition
    Then verify response code of "SaveEnrollment" Api is 200

    Given a prepay transaction is returned from searchAccounts api for "<testCondition>"
    When a request is made to the GetPrepayPlansRequote Api for "<testCondition>" condition
    Then verify response code of "GetPrepayPlansRequote" Api is 200
    Then the response should contain the expected prepay plans and "<planCode>" planCode

    Examples:
      | planCode | testCondition                            |
      | PRP      | GET_PREPAY_PLANS_REQUOTE_POSITIVE_TC_456 |
      | PRP      | GET_PREPAY_PLANS_REQUOTE_POSITIVE_TC_471 |
      | PGB      | GET_PREPAY_PLANS_REQUOTE_POSITIVE_TC_472 |