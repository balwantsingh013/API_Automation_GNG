Feature: Verify ServiceTransfer SaveEnrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @MarketerSwitchSaveEnrollmentWithEligibleNegative @NegativeFlow @Phase2
  Scenario Outline: Verify MarketerSwitch SaveEnrollment error with eligible flow for "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api for marketer switch from SearchAccounts response for external cases for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to get Marketer Reference Data
    Then a request is made to the SaveEnrollment Api for marketer switch calls with invalid parameters for "<testCondition>" condition
    And verify response code of "MarketerSwitch/SaveEnrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                                   | errorCode | errorMessage                                                                 |

      #| SE_MRK_SW_MISSING_TRANSACTION_TYPE_TC_034                       | 10000     | Missing Transaction Type                                                     |
      #| SE_MRK_SW_MAX_LENGTH_TRANSACTION_TYPE_TC_035                    | 10000     | The Transaction Type must be a string with a maximum length of 4             |
      #| SE_MRK_SW_INVALID_TRANSACTION_TYPE_TC_036                       | 1000      | Invalid Request: Invalid Transaction Type                                    |
      #| SE_MRK_SW_SPLIT_CONN_FEE_TRUE_TC_037                            | 2000      | Invalid Request: Invalid Split Connection Fee Indicator for Transaction Type |
      | SE_MRK_SW_ABLC_ACCOUNT_PROVIDED_TC_038                          | 2000      | Parameter Value should be null-AGLC Account Number                           |
      | SE_MRK_SW_ABLC_SERVICE_PROVIDED_TC_039                          | 2000      | Parameter Value should be null- AGLC Service Order Number                    |
      | SE_MRK_SW_MAX_LENGTH_CURRENT_MRK_CODE_TC_040                    | 10000     | The Marketer Code must be a string with a maximum length of 4.               |
      | SE_MRK_SW_INVALID_CURRENT_MRK_CODE_TC_041                       | 2000      | Invalid Request: Invalid Marketer Code                                       |
      | SE_MRK_SW_NOT_ALPHA_CURRENT_MRK_CODE_TC_042                     | 2000      | Invalid Request: Invalid Marketer Code                                       |


