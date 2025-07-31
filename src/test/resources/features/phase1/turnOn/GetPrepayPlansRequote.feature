@Feature @GetPrepayPlansRequotePositive
Feature: GetPrepayPlansRequote – Positive Scenario

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetPrepayPlansRequotePositive @HappyFlow
  Scenario Outline: GetPrepayPlansRequote API – returns quotes for <testCondition>
    Given a valid non‑expired prepay transaction exists for "<testCondition>"
    When a request is made to the GetPrepayPlansRequote Api with "<testCondition>" condition
    Then verify response code of "GetPrepayPlansRequote" Api is 200
    And response should have success true, errorCode 0 and errorMessage null

    When the same request is made again to GetPrepayPlansRequote Api with "<testCondition>" condition
    Then the response should have success false, errorCode 10003 and errorMessage "Duplicate Request ID"

    Examples:
      | testCondition                         |
      | GET_PREPAY_PLANS_REQUOTE_TC_456       |
