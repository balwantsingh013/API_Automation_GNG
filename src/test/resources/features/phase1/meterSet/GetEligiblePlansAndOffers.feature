Feature: Verify MeterSet GetEligiblePlansAndOffers API

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetEligiblePlansAndOffersMeterSetNegative @NegativeFlow @Phase1
  Scenario Outline: Verify meter set GetEligiblePlansAndOffers with invalid parameters "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api meterSet for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                      | errorCode | errorMessage                                                    |
      | MS_GE_MISSING_TRANSACTION_TYPE_TC_006                              | 10000 | Missing Transaction Type                                            |
      | MS_GE_TRANSACTION_TYPE_MAX_LENGTH_TC_007                           | 10000 | The Transaction Type must be a string with a maximum length of 4    |
      | MS_GE_TRANSACTION_TYPE_INVALID_TC_008                              | 1000  | Invalid Request: Invalid Transaction Type                           |
      | MS_GE_AGLC_ACCOUNT_PROVIDED_TC_009                                 | 2200  | Parameter Value should be null-AGLC Account Number                  |


