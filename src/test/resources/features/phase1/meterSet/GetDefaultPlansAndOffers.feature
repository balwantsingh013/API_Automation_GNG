Feature: Verify GetDefaultPlansAndOffers Api MeterSet

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response


  @GetDefaultPlansAndOffersMeterSetNegativeRequest @Phase1 @NegativeFlow
  Scenario Outline: GetDefaultPlansAndOffers Api - Verify GetDefaultPlansAndOffers Api with invalid requestID for "<testCondition>" condition
    When a request is made to the GetDefaultPlansAndOffers Api MeterSet with an invalid params for "<testCondition>"
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                              | errorCode | errorMessage                        |
      | GD_MS_MISSING_TRANSACTION_ID_TC_03         | 10113     | Invalid or missing Transaction Type |
      | GD_MS_INVALID_TRANSACTION_ID_LENGTH_TC_04  | 10113     | Invalid or missing Transaction Type |
      | GD_MS_MISSING_TRANSACTION_ID_TC_05         | 10113     | Invalid or missing Transaction Type |


