Feature: Verify GetPrepayPlansRequote Api marketer switch

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetPrepayPlansRequoteNegativeInvalidParamsMarketerSwitch @Phase2 @NegativeFlow
  Scenario Outline: GetPrepayPlansRequote Api - Verify GetPrepayPlansRequote Api marketer switch with invalid params for "<testCondition>" condition
    When a request is made to the GetPrepayPlansRequote Api marketer switch with an invalid params for "<testCondition>" condition
    Then verify response code of "GetPrepayPlansRequote" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                             | errorCode | errorMessage                                                      |
      | GPP_PR_MISSING_TRANSACTION_TYPE_TC_056    | 10000     | Missing Transaction Type                                          |
      | GPP_PR_MAX_LENGTH_TRANSACTION_TYPE_TC_057 | 10000     | The Transaction Type must be a string with a maximum length of 4  |
      | GPP_PR_INVALID_TRANSACTION_TYPE_TC_058    | 1000      | Invalid Request: Invalid Transaction Type                         |
