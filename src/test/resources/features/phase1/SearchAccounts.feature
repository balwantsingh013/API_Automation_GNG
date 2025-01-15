Feature: Verify SearchAccounts Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @SearchAccountsWithInvalidRequestID @Phase1 @NegativeFlow
  Scenario Outline: Verify SearchAccounts Api with invalid requestID "<requestID>"TC42_TC44
    When a request is made to the SearchAccounts Api with "<requestID>"TC42_TC44
    Then verify response code of "SearchAccounts" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID                 | errorCode | errorMessage         |
      | NULL_REQUEST_ID_TC21      | 10001     | Missing Request ID   |
      | DUPLICATE_REQUEST_ID_TC22 | 10003     | Duplicate Request ID |
      | LONG_REQUEST_ID_TC23      | 10002     | Invalid Request ID   |

  @SearchAccountsInvalidLoginID @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid loginID "<loginID>"TC45_TC48
    When a request is made to the SearchAccounts Api with "<loginID>"TC45_TC48
    Then verify response code of "SearchAccounts" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | loginID                        | errorCode | errorMessage              |
      | NULL_LOGIN_ID_TC45              | 10112     | Invalid or missing Login ID |
      | ALPHANUMERIC_LOGIN_ID_TC47      | 10112     | Invalid or missing Login ID |
      | MAX_LENGTH_LOGIN_ID_TC46        | 10112     | Invalid or missing Login ID |
      | INVALID_LOGIN_ID_NOT_PRESENT_USER_TABLE_TC48       | 20000    | Invalid Login ID |

  @SearchAccountsInvalidCustomerCode @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid customerCode "<customerCode>"TC49
    When a request is made to the SearchAccounts Api with "<customerCode>"TC49
    Then verify response code of "SearchAccounts" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | customerCode                        | errorCode | errorMessage              |
      | MAX_LENGTH_CUSTOMER_CODE_TC49              | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesCode @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid premisesCode "<premisesCode>"TC49
    When a request is made to the SearchAccounts Api with "<premisesCode>"TC49
    Then verify response code of "SearchAccounts" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesCode                        | errorCode | errorMessage              |
      | MAX_LENGTH_PREMISES_CODE_TC50              | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidTransactionType @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid transactionType "<transactionType>"TC51_TC52
    When a request is made to the SearchAccounts Api with "<transactionType>"TC51_TC52
    Then verify response code of "SearchAccounts" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | transactionType                        | errorCode | errorMessage              |
      | MISSING_TRANSACTION_TYPE_TC51              | 10113    | Invalid or missing Transaction Type |
      | INVALID_TRANSACTION_TYPE_TC52             | 10113    | Invalid or missing Transaction Type |

  @SearchAccountsInvalidBusinessName @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid businessName "<businessName>"TC53
    When a request is made to the SearchAccounts Api with "<businessName>"TC53
    Then verify response code of "SearchAccounts" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | businessName                        | errorCode | errorMessage              |
      | MAX_LENGTH_BUSINESS_NAME_TC53             | 10115    | Invalid search parameter(s) |
