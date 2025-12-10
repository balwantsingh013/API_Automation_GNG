Feature: Verify MarketSwitch GetDefaultPlansAndOffers API

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetDefaultPlansAndOffersMarketerSwitchNegative @Phase2 @NegativeFlow
  Scenario Outline: GetDefaultPlansAndOffers Api - Verify GetDefaultPlansAndOffers Api marketer switch with invalid requestID for "<testCondition>" condition
    When a request is made to the GetDefaultPlansAndOffers Api marketer switch with an invalid params for "<testCondition>"
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                    | errorCode | errorMessage                        |
      | GD_MARK_SWITCH_MISSING_TRANSACTION_ID_TC_001     | 10113     | Invalid or missing Transaction Type |
      | GD_MARK_SWITCH_MAX_LENGTH_TRANSACTION_ID_TC_002  | 10113     | Invalid or missing Transaction Type |
      | GD_MARK_SWITCH_INVALID_TRANSACTION_ID_TC_003     | 10113     | Invalid or missing Transaction Type |