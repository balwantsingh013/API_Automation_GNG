Feature: Verify ServiceTransfer SaveEnrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @ServiceTransferSaveEnrollmentNegative @NegativeFlow @Phase1
  Scenario Outline: Verify ServiceTransfer SaveEnrollment with invalid parameters "<testCondition>"
    Given a request is made to get Marketer Reference Data
    When a request is made to the SaveEnrollment Api for serviceTransfer with invalid parameters for "<testCondition>" condition
    Then verify response code of "ServiceTransfer/SaveEnrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                   | errorCode | errorMessage                                                                                                                                                     |
      | ST_SE_MISSING_TRANSACTION_TYPE_TC229                            | 10000     | Missing Transaction Type                                                                                                                                         |
      | ST_SE_INVALID_TRANSACTION_TYPE_LENGTH_TC230                     | 10000     | The Transaction Type must be a string with a maximum length of 4                                                                                                 |
      | ST_SE_INVALID_TRANSACTION_TYPE_VALUE_TC231                      | 1000      | Invalid Request: Invalid Transaction Type                                                                                                                        |
      | ST_SE_MISSING_ENROLLMENT_STATUS_TC232                           | 10000     | Missing Enrollment Status                                                                                                                                        |
      | ST_SE_INVALID_ENROLLMENT_STATUS_LENGTH_TC233                    | 10000     | The Enrollment Status must be a string with a maximum length of 2                                                                                                |
      | ST_SE_CURRENT_PRICE_PLAN_FIXED_BOOLEAN_ONLY_TC248               | 10000     | The JSON value could not be converted to System.Nullable`1[System.Boolean]. Path: $.serviceTransferCurrentPricePlan \| LineNumber: 0 \| BytePositionInLine: 555. |

  @ServiceTransferSaveEnrollmentWithEligibleNegative @NegativeFlow @Phase1
  Scenario Outline: Verify ServiceTransfer SaveEnrollment error with eligible flow for "<testCondition>"
    When a request is made to the SearchAccounts Api ServiceTransfer with transactionType for "<testCondition>" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the GetEligiblePlansAndOffers Api from SearchAccounts response for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to get Marketer Reference Data
    Then a request is made to the SaveEnrollment Api for serviceTransfer with invalid parameters for "<testCondition>" condition
    And verify response code of "ServiceTransfer/SaveEnrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                   | errorCode | errorMessage                                                                                                                                                     |
      | ST_SE_INVALID_ENROLLMENT_STATUS_VALUE_TC234                     | 2000      | Invalid Request: Invalid Enrollment Status                                                                                                                       |
      | ST_SE_INVALID_ES_PAYMENT_CONFIRMATION_REQUIRED_TC235            | 2000      | Invalid Request: Invalid Enrollment Status                                                                                                                       |
      | ST_SE_INVALID_PAYMENT_CONFIRMATION_TC236                        | 2200      | Parameter Value should be null -Payment Confirmation Number                                                                                                      |
      | ST_SE_INVALID_SSP_PARTICIPANT_CODE_VALUE_TC237                  | 2200      | Parameter Value should be null -SSP Result                                                                                                                       |
      | ST_SE_SSP_PARTICIPANT_CODE_NOT_REQUIRED_TC238                   | 2200      | Parameter Value should be null-SSP Participant Code                                                                                                              |
      | ST_SE_MISSING_MARKETER_REFERENCE_CE_TRAN_TC239                  | 1000      | Invalid Request: Missing required parameters - Marketer Reference Number                                                                                         |
      | ST_SE_DUPLICATE_MARKETER_REFERENCE_DATA_TC240                   | 2000      | Invalid Request: Invalid Marketer Reference Number - Duplicate                                                                                                   |
      | ST_SE_MARKETER_REFERENCE_DATA_INVALID_TYPE_TC242                | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.marketerReferenceData \| LineNumber: 0 \| BytePositionInLine: 486.             |
      | ST_SE_MARKETER_REFERENCE_DATA_TOO_LONG_TC243                    | 10000     | The Marketer Reference Data must be a number with 12 digits                                                                                                      |
      | ST_SE_MARKETER_REFERENCE_DATA_TOO_SHORT_TC244                   | 10000     | The Marketer Reference Data must be a number with 12 digits                                                                                                      |
      | ST_SE_CURRENT_MARKETER_CODE_PROVIDED_TC245                      | 2200      | Parameter Value should be null -Marketer Code                                                                                                                    |
      | ST_SE_REQUESTED_TURN_ON_DATE_PROVIDED_TC246                     | 2200      | Parameter Value should be null-Requested TNON Date                                                                                                               |
      | ST_SE_SERVICE_TRANSFER_REWARD_BOOLEAN_ONLY_TC247                | 10000     | The JSON value could not be converted to System.Nullable`1[System.Boolean]. Path: $.serviceTransferReward \| LineNumber: 0 \| BytePositionInLine: 519.           |
      | ST_SE_CURRENT_PRICE_PLAN_FIXED_BOOLEAN_ONLY_TC248               | 10000     | The JSON value could not be converted to System.Nullable`1[System.Boolean]. Path: $.serviceTransferCurrentPricePlan \| LineNumber: 0 \| BytePositionInLine: 557. |
      | ST_SE_CURRENT_PRICE_PLAN_CEILING_BOOLEAN_ONLY_TC249             | 10000     | The JSON value could not be converted to System.Nullable`1[System.Boolean]. Path: $.serviceTransferCurrentPricePlan \| LineNumber: 0 \| BytePositionInLine: 555. |
      | ST_SE_CURRENT_PRICE_PLAN_APPLICABLE_FIXED_OR_CEILING_ONLY_TC250 | 2000      | Current Plan or Offer Transfer Not Allowed                                                                                                                       |
      | ST_SE_CURRENT_PRICE_PLAN_APPLICABLE_FIXED_OR_CEILING_ONLY_TC250 | 2000      | Invalid Request: Invalid Plan Code                                                                                                                               |





