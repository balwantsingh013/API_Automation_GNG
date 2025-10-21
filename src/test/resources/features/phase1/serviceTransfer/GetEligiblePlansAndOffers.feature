Feature: GetEligiblePlansAndOffers - Negative validations (Service Transfer)

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetEligiblePlansAndOffersTransfer @NegativeFlow @Phase1
  Scenario Outline: Verify ServiceTransfer GetEligiblePlansAndOffers with invalid parameters "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api serviceTransfer for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                      | errorCode | errorMessage                                                                                                                               |
      | ST_GE_MISSING_REQUEST_ID_NEG_TC188                                 | 10001     | Missing Request ID                                                                                                                         |
      | ST_GE_INVALID_REQUEST_ID_LENGTH_NEG_TC189                          | 10002     | Invalid Request ID                                                                                                                         |
      | ST_GE_DUPLICATE_REQUEST_ID_NEG_TC190                               | 10003     | Duplicate Request ID                                                                                                                       |
      | ST_GE_MISSING_LOGIN_ID_NEG_TC191                                   | 10000     | Missing Login ID                                                                                                                           |
      | ST_GE_INVALID_LOGIN_ID_LENGTH_NEG_TC192                            | 10000     | The Login ID must be a string with a maximum length of 30                                                                                  |
      | ST_GE_INVALID_LOGIN_ID_FORMAT_NEG_TC193                            | 2000      | Invalid Login ID                                                                                                                           |
      | ST_GE_INVALID_LOGIN_ID_NOT_FOUND_NEG_TC194                         | 2000      | Invalid Login ID                                                                                                                           |
      | ST_GE_MISSING_TRANSACTION_TYPE_NEG_TC195                           | 10000     | Missing Transaction Type                                                                                                                   |
      | ST_GE_INVALID_TRANSACTION_TYPE_LENGTH_NEG_TC196                    | 10000     | The Transaction Type must be a string with a maximum length of 4                                                                           |
      | ST_GE_INVALID_TRANSACTION_TYPE_VALUE_NEG_TC197                     | 1000      | Invalid Request: Invalid Transaction Type                                                                                                  |
      | ST_GE_MISSING_CUSTOMER_CODE_NEG_TC199                              | 10000     | Missing Customer Code                                                                                                                      |
      | ST_GE_INVALID_CUSTOMER_CODE_LENGTH_NEG_TC200                       | 10000     | The Customer Code must be an integer with a maximum length of 9                                                                            |
      | ST_GE_INVALID_CUSTOMER_CODE_FORMAT_NEG_TC201                       | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.customerCode \| LineNumber: 0 \| BytePositionInLine: 391.|
      | ST_GE_INVALID_CUSTOMER_CODE_NOT_FOUND_NEG_TC202                    | 2000      | Invalid Request: Invalid Customer Code                                                                                                     |
      | ST_GE_MISSING_PREMISES_CODE_NEG_TC203                              | 10000     | Missing Premises Code                                                                                                                      |
      | ST_GE_INVALID_PREMISES_CODE_LENGTH_NEG_TC204                       | 10000     | The Premises Code must be a numeric string with a maximum length of 7                                                                      |
      | ST_GE_INVALID_PREMISES_CODE_NOT_FOUND_NEG_TC205                    | 2000      | Invalid Request: Invalid Premises Code                                                                                                     |
      | ST_GE_INVALID_ACCOUNT_COMBINATION_NEG_TC206                        | 2000      | Invalid Request: Invalid Account                                                                                                           |
      | ST_GE_REFERRAL_CODE_NOT_REQUIRED_FOR_TRAN_NEG_TC207                | 2200      | Parameter Value should be null-Referral Code                                                                                               |
      | ST_GE_INVALID_CREDIT_CHECK_OPTION_LENGTH_NEG_TC208                 | 10000     | The Credit Check Option must be a string with a maximum length of 32.                                                                      |
      | ST_GE_INVALID_CREDIT_CHECK_OPTION_VALUE_NEG_TC209                  | 2000      | Invalid Request: Invalid Credit Check Option for Transaction Type                                                                          |
      | ST_GE_MISSING_CREDIT_CHECK_OPTION_NEG_TC210                        | 10000     | Invalid or missing Credit Check Option                                                                                                     |
      | ST_GE_INITIAL_CREDIT_CHECK_CUST_CODE_NOT_REQUIRED_NEG_TC211        | 2200      | Parameter Value should be null-Initial Credit Check Cust Code                                                                              |
      | ST_GE_CONFIRM_CREDIT_CHECK_NOT_REQUIRED_NEG_TC212                  | 2200      | Parameter Value should be null-Confirm Credit Check                                                                                        |
      | ST_GE_SSP_PARTICIPANT_CODE_NOT_REQUIRED_NEG_TC213                  | 2200      | Parameter Value should be null-SSP Participant Code                                                                                        |
      | ST_GE_COMMERCIAL_CREDIT_CHECK_BIN_NOT_REQUIRED_NEG_TC214           | 10000     | Parameter Value should be null-Commercial Credit Check Business BIN                                                                        |
      | ST_GE_ST_CURRENT_FLAG_MISSING_WHEN_PLAN_FIXED_OR_CEILING_NEG_TC216 | 10000     | Missing Service Transfer Current Price Plan flag                                                                                           |

  @GetEligiblePlansAndOffersTransferWithSearchAccountsNotAllowed @NegativeFlow @Phase1
  Scenario Outline: Verify ServiceTransfer GetEligiblePlansAndOffers from SearchAccounts response with invalid parameters "<testCondition>"
    When a request is made to the SearchAccounts Api ServiceTransfer with transactionType for "<testCondition>" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the GetEligiblePlansAndOffers Api serviceTransfer for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                               | errorCode | errorMessage                                     |
      | ST_GE_ENROLLMENT_STATE_INVALID_FOR_TRAN_NEG_TC198           | 2200      | enrollmentState: Parameter Value should be null  |
      | ST_GE_ST_CURRENT_TRUE_PLAN_NOT_FIXED_OR_CEILING_NEG_TC215A  | 2000      | Current Plan or Offer Transfer Not Allowed       |
      | ST_GE_ST_CURRENT_TRUE_NOT_TRAN_NEG_TC217                    | 2000      | Plan or Offer Transfer Only Applicable to TRAN   |
      | ST_GE_ST_OFFER_REMAINDER_TRUE_NOT_TRAN_NEG_TC220            | 2000      | Current Plan or Offer Transfer Not Allowed       |

