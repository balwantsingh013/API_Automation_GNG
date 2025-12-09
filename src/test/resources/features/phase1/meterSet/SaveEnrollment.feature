Feature: Verify MeterSet SaveEnrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @MeterSetSaveEnrollmentNegative @NegativeFlow @Phase2 @meterSet
  Scenario Outline: Verify MeterSet SaveEnrollment with invalid parameters "<testCondition>"
    Given a request is made to get Marketer Reference Data
    When a request is made to the GetEligiblePlansAndOffers Api from customer file meterSet to seed data for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the meter set response should contain the expected plans

    Then a request is made to the SaveEnrollment Api for meterSet with invalid parameters for "<testCondition>" condition
    Then verify response code of "MeterSet/SaveEnrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                 | errorCode | errorMessage                                                                                                                                         |
      | MS_SE_MISSING_TRANSACTION_TYPE_TC_035                         | 10000     | Missing Transaction Type                                                                                                                             |
      | MS_SE_TRANSACTION_TYPE_MAX_LENGTH_TC_036                      | 10000     | The Transaction Type must be a string with a maximum length of 4                                                                                     |
      | MS_SE_TRANSACTION_TYPE_INVALID_VALUE_TC_037                   | 1000      | Invalid Request: Invalid Transaction Type                                                                                                            |
      | MS_SE_SSP_RESULT_SHOULD_BE_NULL_TC_038                        | 2200      | Parameter Value should be null -SSP Result                                                                                                           |
      | MS_SE_AGLC_ACCOUNT_NUMBER_SHOULD_BE_NULL_TC_039               | 2200      | Parameter Value should be null-AGLC Account Number                                                                                                   |
      | MS_SE_AGLC_SERVICE_ORDER_NUMBER_SHOULD_BE_NULL_TC_040         | 2200      | Parameter Value should be null-AGLC Service Order Number                                                                                             |
      | MS_SE_SSP_PARTICIPANT_CODE_SHOULD_BE_NULL_TC_041              | 2200      | Parameter Value should be null-SSP Participant Code                                                                                                  |
      | MS_SE_CURRENT_MARKETER_CODE_SHOULD_BE_NULL_TC_042             | 2200      | Parameter Value should be null-Current Marketer Code                                                                                                 |
      | MS_SE_MISSING_REQUESTED_TURN_ON_DATE_TC_043                   | 10000     | Requested Turn On Date is required for Set Meter                                                                                                     |
      | MS_SE_REQUESTED_TURN_ON_DATE_INVALID_FORMAT_TC_044            | 10000     | The Requested Turn On Date must be a date in the format of yyyyMMdd                                                                                  |
      | MS_SE_BUDGET_BILLING_PRP_NULL_AMOUNT_NOT_ALLOWED_TC_044_1     | 2000      | Invalid Request: Invalid Billing Option -PRP Customers are not eligible for Budget Billing.                                                          |
      | MS_SE_BUDGET_BILLING_PRP_AMOUNT_PROVIDED_NOT_ALLOWED_TC_044_2 | 2000      | Invalid Request: Invalid Billing Option -PRP Customers are not eligible for Budget Billing.                                                          |
      | MS_SE_BUDGET_BILLING_NON_PRP_MISSING_AMOUNT_TC_044_3          | 2000      | Invalid Request: Missing conditional parameters-Budget Amount                                                                                       |
      | MS_SE_BUDGET_BILLING_NON_PRP_AMOUNT_TOO_LONG_TC_044_4         | 10000     | The Estimated Budget Amount must be an integer with a maximum length of 5                                                                            |
      | MS_SE_BUDGET_BILLING_NON_PRP_DECIMAL_AMOUNT_INVALID_TC_044_5  | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.estimatedBudgetAmount \| LineNumber: 0 \| BytePositionInLine: 296. |
      | MS_SE_BUDGET_BILLING_PLAN_CODE_MISSING_TC_044_6               | 10000     | Missing Plan Code                                                                                                                                    |



  @MeterSetSaveEnrollmentPositive @HappyFlow @Phase2 @meterSet
  Scenario Outline: Verify MeterSet SaveEnrollment with valid parameters "<testCondition>"
    Given a request is made to get Marketer Reference Data
    When a request is made to the GetEligiblePlansAndOffers Api from customer file meterSet to seed data for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the meter set response should contain the expected plans
    When a request is made to the SaveEnrollment Api for meterSet with valid parameters for "<testCondition>" condition
    Then verify response code of "MeterSet/SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

    Examples:
      | testCondition                                        |
      | MS_SE_RS_B_ENROLLMENT_STATUS_CE_TC_045               |
      | MS_SE_CM_R_ENROLLMENT_STATUS_DP_TC_046               |
      | MS_SE_RS_R_ENROLLMENT_STATUS_PC_REQUOTE_TC_047       |
      | MS_SE_CM_R_ENROLLMENT_STATUS_SI_TC_049               |
      | MS_SE_RS_R_ENROLLMENT_STATUS_DR_TC_050               |
      | MS_SE_RS_NA_ENROLLMENT_STATUS_RP_PRP_TC_050A         |
      | MS_SE_RS_PRP_ENROLLMENT_STATUS_PR_TC_051             |
      | MS_SE_CM_R_ENROLLMENT_STATUS_RD_PROMO_TC_053         |
    #053 does not return ccv plan
      | MS_SE_RS_PRP_ENROLLMENT_STATUS_CP_TC_054             |
      | MS_SE_RS_B_ENROLLMENT_STATUS_BD_BUDGET_TC_055        |

  @MeterSetSaveEnrollmentPositiveWithNote @HappyFlow @Phase2 @meterSet
  Scenario Outline: Verify MeterSet SaveEnrollment with valid parameters "<testCondition>"
    Given a request is made to get Marketer Reference Data
    When a request is made to the GetEligiblePlansAndOffers Api from customer file meterSet to seed data for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the meter set response should contain the expected plans
    When a request is made to the SaveEnrollment Api for meterSet with noteText "<noteText>" and valid parameters for "<testCondition>" condition
    Then verify response code of "MeterSet/SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And verify meterSet note is created successfully with "<noteText>" noteText for "<testCondition>" condition

    Examples:
      | testCondition                                        | noteText                                         |
      | MS_SE_CM_R_CE_WITH_NOTES_TC_056                      | This is a test notes                             |



  @MeterSetDoubleSaveEnrollmentPositive @HappyFlow @Phase2 @meterSet
  Scenario Outline: Verify MeterSet SaveEnrollment with valid parameters "<testCondition>"
    Given a request is made to get Marketer Reference Data
    When a request is made to the GetEligiblePlansAndOffers Api from customer file meterSet to seed data for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the meter set response should contain the expected plans
    When a request is made to the SaveEnrollment Api for meterSet with valid parameters for "<testCondition>" condition
    Then verify response code of "MeterSet/SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    When a second request is made to the SaveEnrollment Api for meterSet with valid parameters for "<testCondition>" condition
    Then verify response code of "MeterSet/SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

    Examples:
      | testCondition                                        |
      | MS_SE_RS_R_ENROLLMENT_STATUS_PC_REQUOTE_TC_047       |
      | MS_SE_RS_PRP_ENROLLMENT_STATUS_DB_TC_048             |




