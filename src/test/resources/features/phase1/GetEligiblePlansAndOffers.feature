Feature: Verify GetEligiblePlansAndOffers Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetEligiblePlansAndOffersWithValidData @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with valid data
    When a request is made to the GetEligiblePlansAndOffers Api
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>

  @GetEligiblePlansAndOffersWithInvalidRequestID @Phase1 @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid requestID "<requestID>"
    When a request is made to the GetEligiblePlansAndOffers Api with "<requestID>"
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID                | errorCode | errorMessage           |
      | EMPTY_REQUEST_ID         | 10001     | Missing Request ID     |
      | DUPLICATE_REQUEST_ID     | 10003     | Duplicate Request ID   |
      | LONG_REQUEST_ID          | 10002     | Invalid Request ID     |
      | UNICODE_CHARS_REQUEST_ID | 10007     | Unsupported Characters |

  @GetEligiblePlansAndOffersInvalidLoginID @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<loginID>"
    When a request is made to the GetEligiblePlansAndOffers Api with login "<loginID>" ID
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | loginID                | errorCode | errorMessage                                              |
      | MIN_LENGTH_LOGIN_ID    | 2000      | Invalid Login ID                                          |
      | SPECIAL_CHARS_LOGIN_ID | 2000      | Invalid Login ID                                          |
      | EMPTY_LOGIN_ID         | 10000     | Missing Login ID                                          |
      | UPPERCASE_LOGIN_ID     | 2000      | Invalid Login ID                                          |
      | ALPHANUMERIC_LOGIN_ID  | 2000      | Invalid Login ID                                          |
      | MAX_LENGTH_LOGIN_ID    | 10000     | The Login ID must be a string with a maximum length of 30 |

  @GetEligiblePlansAndOffersInvalidTransactionType @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<transactionType>"Type
    When a request is made to the GetEligiblePlansAndOffers Api with  transaction "<transactionType>" Type
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | transactionType                      | errorCode | errorMessage                                                     |
      | EMPTY_TRANSACTION_TYPE               | 10000     | Missing Transaction Type                                         |
      | NUMERIC_TRANSACTION_TYPE             | 10000     | The Transaction Type must be a string with a maximum length of 4 |
      | UPPERCASE_TRANSACTION_TYPE           | 10000     | The Transaction Type must be a string with a maximum length of 4 |
      | ALPHANUMERIC_TRANSACTION_TYPE        | 1000      | Invalid Request: Invalid Transaction Type                        |
      | MAX_LENGTH_TRANSACTION_TYPE          | 1000      | Invalid Request: Invalid Transaction Type                        |
      | WHITESPACE_CONTAINS_TRANSACTION_TYPE | 10000     | The Transaction Type must be a string with a maximum length of 4 |

  @GetEligiblePlansAndOffersInvalidCustomerTYPE @Phase1  @NegativeFlow
  Scenario Outline: Verify SaveEnrollment Api with invalid "<customerTYPE>" type
    When a request is made to the GetEligiblePlansAndOffers Api with  customer "<customerTYPE>" Type
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | customerTYPE             | errorCode | errorMessage                                                    |
      | EMPTY_CUSTOMER_TYPE      | 10000     | The Customer Type must be a string with a maximum length of 2.  |
      | MIN_LENGTH_CUSTOMER_TYPE | 10000     | The Customer Type must be a string with a maximum length of 2 . |
      | NUMERIC_CUSTOMER_TYPE    | 2000      | Invalid Request: Invalid Customer Type                          |
      | SPL_CHAR_CUSTOMER_TYPE   | 10000     | The Customer Type must be a string with a maximum length of 2.  |
      | MAX_LENGTH_CUSTOMER_TYPE | 1000      | The Customer Type must be a string with a maximum length of 2.  |

  @GetEligiblePlansAndOffersInvalidEnrollmentSources @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<enrollmentSources>"
    When a request is made to the GetEligiblePlansAndOffers Api with enrollment "<enrollmentSources>" Sources
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | enrollmentSources             | errorCode | errorMessage                                                        |
      | EMPTY_ENROLLMENT_SOURCES      | 10000     | Invalid or missing Enrollment Source                                |
      | NUMERIC_ENROLLMENT_SOURCES    | 1000      | Invalid Request: Invalid Enrollment Source                          |
      | SPL_CHAR_ENROLLMENT_SOURCES   | 1000      | Invalid Request: Invalid Enrollment Source                          |
      | MAX_LENGTH_ENROLLMENT_SOURCES | 10000     | The Enrollment Source must be a string with a maximum length of 35. |

  @GetEligiblePlansAndOffersInvalidCustomerLastName @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<CustomerLastName>"
    When a request is made to the GetEligiblePlansAndOffers Api with customer "<CustomerLastName>" LastName
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | CustomerLastName              | errorCode | errorMessage                                                        |
      | EMPTY_CUSTOMER_LAST_NAME      | 2000      | Invalid Request: Missing conditional parameters-Customer Last Name  |
      | LOWERCASE_CUSTOMER_LAST_NAME  | 11112     | CUSTOMER NOT FOUND,  PLEASE CHECK SPELLING of CUSTOMER NAME and SS  |
      | SPL_CHAR_CUSTOMER_LAST_NAME   | 11115     | No Data in the Experian response                                    |
      | MAX_LENGTH_CUSTOMER_LAST_NAME | 11112     | CUSTOMER NOT FOUND,  PLEASE CHECK SPELLING of CUSTOMER NAME and SSN |
      | NUMERIC_CUSTOMER_LAST_NAME    | 11115     | No Data in the Experian response                                    |

