Feature: Verify GetAccountInfo Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetAccountInfoWithInvalidParameters @NegativeFlow @Phase1
  Scenario Outline: Verify GetAccountInfo with invalid parameters "<testCondition>"
    When a request is made to the GetAccountInfo Api with invalid parameters for "<testCondition>" condition
    Then verify response code of "GetAccountInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                   | errorCode | errorMessage                     |
      | MISSING_REQUEST_ID_NEGATIVE_TC15                | 10001     | Missing Request ID               |
      | DUPLICATE_REQUEST_ID_NEGATIVE_TC16              | 10003     | Duplicate Request ID             |
      | MISSING_CUSTOMER_CODE_NEGATIVE_TC17             | 10011     | Missing Customer Code            |
      | MISSING_PREMISES_CODE_NEGATIVE_TC18             | 10013     | Missing Premises Code            |
      | INVALID_CUSTOMER_CODE_LENGTH_NEGATIVE_TC19      | 10015     | Invalid Customer Code Format     |
      | INVALID_PREMISES_CODE_LENGTH_NEGATIVE_TC20      | 10005     | Invalid Premises Code Format     |
      | INVALID_ACCOUNT_COMBINATION_NEGATIVE_TC21       | 40015     | Invalid Account Number           |

  @GetAccountInfoPositiveFlows @HappyFlow @Phase1
  Scenario Outline: Verify GetAccountInfo positive flows for "<testCondition>"
    When a request is made to the GetAccountInfo Api with valid parameters for "<testCondition>" condition
    Then verify the account information in the response should match the information in the database
    Examples:
      | testCondition                                    |
      | ACTIVE_WITH_PA_PAST_DUE_POSITIVE_TC22            |
      | INACTIVE_WITH_RECURRING_CC_POSITIVE_TC23         |
      | FINAL_WITH_ABD_POSITIVE_TC24                     |
      | ACTIVE_DEFAULTED_PA_WITH_BUDGET_POSITIVE_TC25    |
      | INACTIVE_BAD_DEBT_SONP_DISCLETTERS_POSITIVE_TC26 |
      | NEW_NO_BILLS_YET_POSITIVE_TC27                   |

  @GetAccountInfoPositiveWithSearchAccountsFlows @HappyFlow @Phase1
  Scenario Outline: Verify GetAccountInfo positive flows for "<testCondition>"
    When a request is made to the MeterSet SearchAccounts Api for external cases for "<testCondition>" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the GetAccountInfo Api with valid parameters for "<testCondition>" condition
    Then verify the account information in the response should match the information in the database
    Examples:
      | testCondition                                    |
      | INACTIVE_WITH_RECURRING_CC_POSITIVE_TC23         |

