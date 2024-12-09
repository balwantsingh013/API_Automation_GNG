Feature: Verify SaveEnrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @SaveEnrollmentWithValidData @Phase1 @HappyFlow
  Scenario: Verify SaveEnrollment Api with valid data
    When a request is made to the SaveEnrollment Api
    Then verify response code of "SaveEnrollment" Api is <200>

  @SaveEnrollmentWithInvalidRequestID @Phase1 @NegativeFlow
  Scenario Outline: Verify SaveEnrollment Api with invalid requestID "<requestID>"
    When a request is made to the SaveEnrollment Api with "<requestID>"
    Then verify response code of "SaveEnrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID                | errorCode | errorMessage              |
      | EMPTY_REQUEST_ID         | 10001     | Missing Request ID        |
      | DUPLICATE_REQUEST_ID     | 10003     | Duplicate Request ID      |
      | SPECIAL_CHARS_REQUEST_ID | 10004     | Invalid Request ID Format |
      | LONG_REQUEST_ID          | 10002     | Invalid Request ID        |
      | UNICODE_CHARS_REQUEST_ID | 10007     | Unsupported Characters    |

  @SaveEnrollmentInvalidCustomerCODE @Phase1  @NegativeFlow
  Scenario Outline: Verify SaveEnrollment Api with invalid "<customerCODE>" code
    When a request is made to the SaveEnrollment Api with  customer "<customerCODE>" code
    Then verify response code of "Save Enrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | customerCODE                | errorCode | errorMessage                                                    |
      | DUPLICATE_CUSTOMER_CODE     | 2000      | Invalid Request: Transaction ID does not exist |
      | MAX_LENGTH_CUSTOMER_CODE    | 2000      | Invalid Request: Transaction ID does not exist |
      | UNICODE_CHARS_CUSTOMER_CODE | 2000      | Invalid Request: Transaction ID does not exist |
      | DUPLICATE_CUSTOMER_CODE     | 2000      | Invalid Request: Transaction ID does not exist|


  @SaveEnrollmentInvalidPromotionCODE @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<premisesCODE>" code
    When a request is made to the SaveEnrollment Api with  premises "<premisesCODE>" code
    Then verify response code of "Save Enrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesCODE                | errorCode | errorMessage                                                          |
      | EMPTY_PREMISES_CODE         | 10000     | Missing Premises Code                                                 |
      | DUPLICATE_PREMISES_CODE     | 10000     | The Premises Code must be a numeric string with a maximum length of 7 |
      | SPECIAL_CHARS_PREMISES_CODE | 10000     | The Premises Code must be a numeric string with a maximum length of 7 |
      | MAX_LENGTH_PREMISES_CODE    | 10000     | The Premises Code must be a numeric string with a maximum length of 7 |
      | MIN_LENGTH_PREMISES_CODE    | 10000     | The Premises Code must be a numeric string with a maximum length of 7 |

  @SaveEnrollmentInvalidTransactionID @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<transactionID>"ID
    When a request is made to the SaveEnrollment Api with  transaction "<transactionID>" ID
    Then verify response code of "Save Enrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | transactionID                     | errorCode | errorMessage                            |
      | MIN_LENGTH_TRANSACTION_ID         | 2000      | Invalid Request: Invalid Transaction ID |
      | WHITESPACE_BETWEEN_TRANSACTION_ID | 2000      | Invalid Request: Invalid Transaction ID |


  @SaveEnrollmentInvalidTransactionType @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<transactionType>"Type
    When a request is made to the SaveEnrollment Api with  transaction "<transactionType>" Type
    Then verify response code of "Save Enrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | transactionType               | errorCode | errorMessage                                                     |
      | EMPTY_TRANSACTION_TYPE        | 10000     | Missing Transaction Type                                         |
      | NUMERIC_TRANSACTION_TYPE      | 10000     | The Transaction Type must be a string with a maximum length of 4 |
      | UPPERCASE_TRANSACTION_TYPE    | 10000     | The Transaction Type must be a string with a maximum length of 4 |
      | ALPHANUMERIC_TRANSACTION_TYPE | 1000      | Invalid Request: Invalid Transaction Type                        |

  @SaveEnrollmentInvalidPlanCode @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<planCode>"Code
    When a request is made to the SaveEnrollment Api with  plan "<planCode>" Code
    Then verify response code of "Save Enrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | planCode                | errorCode | errorMessage                                              |
      | NUMERIC_PLAN_CODE       | 10000     | The Plan Code must be a string with a maximum length of 3 |
      | SPECIAL_CHARS_PLAN_CODE | 10000     | The Plan Code must be a string with a maximum length of 3 |
      | EMPTY_PLAN_CODE         | 10000     | Missing Plan Code                                         |
      | UPPERCASE_PLAN_CODE     | 10000     | The Plan Code must be a string with a maximum length of 3 |
      | ALPHANUMERIC_PLAN_CODE  | 2000      | Invalid Request: Invalid Plan Code                        |

  @SaveEnrollmentInvalidLoginID @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<loginID>"
    When a request is made to the SaveEnrollment Api with login "<loginID>" ID
    Then verify response code of "Save Enrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | loginID                | errorCode | errorMessage     |
      | MIN_LENGTH_LOGIN_ID    | 2000      | Invalid Login ID |
      | SPECIAL_CHARS_LOGIN_ID | 2000      | Invalid Login ID |
      | EMPTY_LOGIN_ID         | 10000     | Missing Login ID |
      | UPPERCASE_LOGIN_ID     | 2000      | Invalid Login ID |
      | ALPHANUMERIC_LOGIN_ID  | 2000      | Invalid Login ID |
      | MAX_LENGTH_LOGIN_ID    | 2000      | Invalid Login ID |


  @SaveEnrollmentInvalidEnrollmentStatus @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<enrollmentStatus>"
    When a request is made to the SaveEnrollment Api with enrollment "<enrollmentStatus>" Status
    Then verify response code of "Save Enrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | enrollmentStatus                      | errorCode | errorMessage                                                      |
      | MIN_LENGTH_ENROLLMENT_STATUS          | 2000      | Invalid Request: Invalid Enrollment Status                        |
      | SPECIAL_CHARS_ENROLLMENT_STATUS       | 10000     | The Enrollment Status must be a string with a maximum length of 2 |
      | EMPTY_ENROLLMENT_STATUS               | 10000     | Missing Enrollment Status                                         |
      | LOWERCASE_ENROLLMENT_STATUS           | 10000     | The Enrollment Status must be a string with a maximum length of 2 |
      | ALPHANUMERIC_ENROLLMENT_STATUS        | 2000      | The Enrollment Status must be a string with a maximum length of 2 |
      | MAX_LENGTH_ENROLLMENT_STATUS          | 10000     | The Enrollment Status must be a string with a maximum length of 2 |
      | WHITESPACE_CONTAINS_ENROLLMENT_STATUS | 10000     | The Enrollment Status must be a string with a maximum length of 2 |

  @SaveEnrollmentInvalidBillingPlan @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<billingPlan>"
    When a request is made to the SaveEnrollment Api with billing "<billingPlan>" Plan
    Then verify response code of "Save Enrollment" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | billingPlan                | errorCode | errorMessage                                                   |
      | MAX_LENGTH_BILLING_PLAN    | 10000     | The Billing Plan must be a string with a maximum length of 1   |
      | SPECIAL_CHARS_BILLING_PLAN | 10000     | The Billing Plan must be a string with a maximum length of 1   |
      | EMPTY_BILLING_PLAN         | 2000      | Invalid Request: Missing conditional parameters-Billing Option |
      | LOWERCASE_BILLING_PLAN     | 10000     | The Billing Plan must be a string with a maximum length of 1   |