Feature: Verify GetPrepayPlansRequote Api MeterSet

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetPrepayPlansRequoteMeterSetPositive @HappyFlow
  Scenario Outline: GetPrepayPlansRequote API – MeterSet returns quotes for <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api from customer file meterSet to seed data for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the response should contain the expected plans
    And the response plans should contains a "<planCode>" plan
    Then a request is made to get Marketer Reference Data
    Then a request is made to the SaveEnrollment Api for external meterSet calls with valid parameters for "<testCondition>" condition
    And verify response code of "SaveEnrollment" Api is 200
    Then a request is made to the MeterSet SearchAccounts Api from GetEligiblePlansAndOffers API for external cases for "<testCondition>" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the GetPrepayPlansRequote Api meterSet for "<testCondition>" condition
    And verify response code of "GetPrepayPlansRequote" Api is 200
    And the response should contain the expected meterSet prepay plans and "<planCode>" planCode

    Examples:
      | planCode | testCondition              |
      #needs billingPlan = null, otherwise error: prepay customers are not eligible for budget billing
      | PRP      | GP_MS_PREPAY_ONLY_PRP_TC60 |


  @GetPrepayPlansRequoteMeterSetNegativeConditions @Phase1 @NegativeFlow
  Scenario Outline: GetPrepayPlansRequote Api - meterSet Verify GetPrepayPlansRequote Api for "<testCondition>" negative condition
    When a request is made to the GetPrepayPlansRequote Api meterSet for "<testCondition>" negative condition
    Then verify response code of "GetPrepayPlansRequote" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                            | errorCode | errorMessage                                                      |
      | GP_MS_MISSING_TRANSACTION_TYPE_TC_57     | 10000     | Missing Transaction Type                                          |
      | GP_MS_MISSING_TRANSACTION_TYPE_TC_57_2   | 10000     | Missing Transaction Type                                          |
      | GP_MS_TRANSACTION_TYPE_MAX_LENGTH_TC_58  | 10000     | The Transaction Type must be a string with a maximum length of 4  |
      | GP_MS_TRANSACTION_TYPE_INVALID_TC_59     | 1000      | Invalid Request: Invalid Transaction Type                         |



