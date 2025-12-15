Feature: Verify ServiceTransfer SaveEnrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @SaveEnrollmentEligibleNegative @NegativeFlow @Phase2
  Scenario Outline: Verify MarketerSwitch SaveEnrollment error with eligible flow for "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api for marketer switch for external cases for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to get Marketer Reference Data
    Then a request is made to the SaveEnrollment Api for marketer switch calls with invalid parameters for "<testCondition>" condition
    And verify response code of "MarketerSwitch/SaveEnrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                                   | errorCode | errorMessage                                                                 |
      | SE_MRK_SW_MISSING_TRANSACTION_TYPE_TC_034                       | 10000     | Missing Transaction Type                                                     |
      | SE_MRK_SW_MAX_LENGTH_TRANSACTION_TYPE_TC_035                    | 10000     | The Transaction Type must be a string with a maximum length of 4             |
      | SE_MRK_SW_INVALID_TRANSACTION_TYPE_TC_036                       | 1000      | Invalid Request: Invalid Transaction Type                                    |
      | SE_MRK_SW_SPLIT_CONN_FEE_TRUE_TC_037                            | 2000      | Invalid Request: Invalid Split Connection Fee Indicator for Transaction Type |
      | SE_MRK_SW_ABLC_ACCOUNT_PROVIDED_TC_038                          | 2000      | Parameter Value should be null-AGLC Account Number                           |
      | SE_MRK_SW_ABLC_SERVICE_PROVIDED_TC_039                          | 2000      | Parameter Value should be null- AGLC Service Order Number                    |
      | SE_MRK_SW_MAX_LENGTH_CURRENT_MRK_CODE_TC_040                    | 10000     | The Marketer Code must be a string with a maximum length of 4.               |
      | SE_MRK_SW_INVALID_CURRENT_MRK_CODE_TC_041                       | 2000      | Invalid Request: Invalid Marketer Code                                       |
      | SE_MRK_SW_NOT_ALPHA_CURRENT_MRK_CODE_TC_042                     | 2000      | Invalid Request: Invalid Marketer Code                                       |

  @SaveEnrollmentEligiblePositive @HappyFlow @Phase2
  Scenario Outline: Verify MarketerSwitch SaveEnrollment error with eligible flow for "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api for marketer switch response for external positive cases for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the marketer switch response should contain the expected plans
    Then a request is made to get Marketer Reference Data
    Then a request is made to the SaveEnrollment Api for marketer switch calls with valid parameters for "<testCondition>" condition
    And verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
      | testCondition                                                |
      | SE_MRK_SW_UPDATE_ENROLLMENT_GOOD_TC_43                       |
      | SE_MRK_SW_DEPOSIT_PAID_NEW_FLOW_TC_44                        |
      | SE_MRK_SW_SAVE_INCOMPLETE_SI_NEW_TC_47                       |
      | SE_MRK_SW_DEPOSIT_REQUIRED_DR_PREVIOUS_TC_48                 |
      | SE_MRK_SW_PREPAY_REQUIRED_PR_NEW_TC_49                       |
      | SE_MRK_SW_PREPAY_REQUIRED_PR_PREVIOUS_TC_50                  |
      | SE_MRK_SW_REFUSED_PREPAY_RP_NEW_TC_51                        |
      | SE_MRK_SW_REFUSED_DEPOSIT_RD_NEW_TC_52                       |
      | SE_MRK_SW_BUDGET_BILL_BD_NEW_TC_54                           |



  @SaveEnrollmentTwiceEligiblePositive @HappyFlow @Phase2
  Scenario Outline: Verify MarketerSwitch SaveEnrollment error with eligible flow for "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api for marketer switch response for external positive cases for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the marketer switch response should contain the expected plans
    Then a request is made to get Marketer Reference Data
    Then a request is made to the SaveEnrollment Api for marketer switch calls with valid parameters for "<testCondition>" condition
    And verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a second request is made to the SaveEnrollment Api for marketer switch calls with valid parameters for "<testCondition>" condition
    And verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
      | testCondition                                                |
      | SE_MRK_SW_CANCEL_PREPAY_CP_PREVIOUS_TC_53                    |
      | SE_MRK_SW_COMPLETE_ENROLLMENT_CE_NOTES_TC_55                 |


  @SaveEnrollmentTwiceEligibleRequotePositive @HappyFlow @Phase2
  Scenario Outline: Verify MarketerSwitch SaveEnrollment error with eligible flow for "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api for marketer switch response for external positive cases for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the marketer switch response should contain the expected plans
    Then a request is made to get Marketer Reference Data
    Then a request is made to the SaveEnrollment Api for marketer switch calls with valid parameters for "<testCondition>" condition
    And verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the MarketerSwitch SearchAccounts Api from GetEligiblePlansAndOffers API for external cases for "<testCondition>" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a external request is made to the GetPrepayPlansRequote Api marketer switch with an valid params for "<testCondition>" condition
    Then verify response code of "GetPrepayPlansRequote" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a second request is made to the SaveEnrollment Api for marketer switch calls with valid parameters for "<testCondition>" condition
    And verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
      | testCondition                                                |
      | SE_MRK_SW_PAYMENT_COMPLETE_PRP_PREVIOUS_TC_45                |
      | SE_MRK_SW_PAYMENT_COMPLETE_PGB_NEW_TC_46                     |



