Feature: Verify SaveEnrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @SaveEnrollmentWithInvalidRequestID @Phase1 @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify SaveEnrollment Api with invalid requestID "<requestID>"
    When a request is made to the SaveEnrollment Api with "<requestID>"
    Then verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID            | errorCode | errorMessage         |
      | EMPTY_REQUEST_ID     | 10001     | Missing Request ID   |
      | LONG_REQUEST_ID      | 10002     | Invalid Request ID   |
      | DUPLICATE_REQUEST_ID | 10003     | Duplicate Request ID |


  @SaveEnrollmentInvalidLoginID @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid "<loginID>"
    When a request is made to the SaveEnrollment Api with login "<loginID>" ID
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | loginID                                       | errorCode | errorMessage                                              |
      | EMPTY_LOGIN_ID                                | 10000     | Missing Login ID                                          |
      | MAX_LENGTH_LOGIN_ID                           | 10000     | The Login ID must be a string with a maximum length of 30 |
      | INVALID_LOGIN_ID_NOT_PRESENT_USER_TABLE_TC381 | 2000      | Invalid Login ID                                          |

  @SaveEnrollmentInvalidTransactionID @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid transactionID for "<testCondition>" condition
    When a request is made to the SaveEnrollment Api with invalid transactionID for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                               | errorCode | errorMessage                                                                                                                                |
      | MISSING_TRANSACTION_ID_TC382                | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.transactionID \| LineNumber: 0 \| BytePositionInLine: 67. |
      | INVALID_NON_INTEGER_TRANSACTION_ID_TC383    | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.transactionID \| LineNumber: 0 \| BytePositionInLine: 71. |
      | INVALID_DOES_NOT_MATCH_TRANSACTION_ID_TC384 | 2000      | Invalid Request: Transaction ID does not exist                                                                                              |

  @SaveEnrollmentInvalidTransactionType @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid transaction type for "<testCondition>" condition
    When a request is made to the SaveEnrollment Api with invalid transaction type for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                             | errorCode | errorMessage                                                     |
      | INVALID_TRANSACTION_TYPE_EMPTY_TC385      | 10000     | Missing Transaction Type                                         |
      | INVALID_TRANSACTION_TYPE_MAX_LENGTH_TC386 | 10000     | The Transaction Type must be a string with a maximum length of 4 |
      | INVALID_TRANSACTION_TYPE_NOT_EXISTS_TC387 | 1000      | Invalid Request: Invalid Transaction Type                        |

  @SaveEnrollmentInvalidCustomerCODE @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify SaveEnrollment Api with invalid customer code for "<testCondition>" condition
    When a request is made to the SaveEnrollment Api with invalid customer code for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                               | errorCode | errorMessage                                                                                                                                |
      | INVALID_CUSTOMER_CODE_EMPTY_TC388           | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.customerCode \| LineNumber: 0 \| BytePositionInLine: 112. |
      | INVALID_CUSTOMER_CODE_MAX_LENGTH_TC389      | 10000     | The Customer Code must be an integer with a maximum length of 9                                                                             |
      | INVALID_CUSTOMER_CODE_ALPHA_NUM_TC390       | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.customerCode \| LineNumber: 0 \| BytePositionInLine: 121. |
      | INVALID_CUSTOMER_CODE_DOES_NOT_EXIST_TC390a | 10000     | Invalid Request: Invalid Customer Code                                                                                                      |

  @SaveEnrollmentInvalidPremisesCODE @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code with invalid premises code for "<testCondition>" condition
    When a request is made to get Marketer Reference Data
    Then a request is made to the SaveEnrollment Api with an invalid premises code for "<testCondition>" condition
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                        | errorCode | errorMessage                                                          |
      | INVALID_PREMISES_CODE_EMPTY_TC391                    | 10000     | Missing Premises Code                                                 |
      | INVALID_PREMISES_CODE_MAX_LENGTH_TC392               | 10000     | The Premises Code must be a numeric string with a maximum length of 7 |
      | INVALID_PREMISES_CODE_NOT_EXISTS_TC392a              | 2000      | Invalid Request: Invalid Premises Code                                |
      | INVALID_PREMISES_CODE_CUSTOMER_CODE_NOT_EXISTS_TC393 | 2000     | Invalid Request: Invalid Account                                      |

  @SaveEnrollmentInvalidPlanCode @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code with invalid planCode for "<testCondition>" condition
    When a request is made to the SaveEnrollment Api with an invalid planCode for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                      | errorCode | errorMessage                                              |
      | INVALID_PLAN_CODE_EMPTY_TC394      | 10000     | Missing Plan Code                                         |
      | INVALID_PLAN_CODE_MAX_LENGTH_TC395 | 10000     | The Plan Code must be a string with a maximum length of 3 |
      | INVALID_PLAN_CODE_NOT_EXISTS_TC396 | 2000      | Invalid Request: Invalid Plan Code                        |

  @SaveEnrollmentInvalidPlanCodePrimeStatus @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code with invalid planCode for "<testCondition>" condition
    When a request is made to get Marketer Reference Data
    Then a request is made to the SaveEnrollment Api with an invalid planCode for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                      | errorCode | errorMessage                                              |
      | INVALID_PLAN_CODE_PRIME_STATUS_TC396a | 2000      | Invalid Request: Invalid Plan Code                     |

  @SaveEnrollmentInvalidPromotionCode @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code with invalid promotionCode for "<testCondition>" condition
    When a request is made to the SaveEnrollment Api with an invalid promotionCode for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                           | errorCode | errorMessage                                                    |
      | INVALID_PROMOTION_CODE_MAX_LENGTH_TC397 | 10000     | The Promotion Code must be a string with a maximum length of 35 |
      | INVALID_PROMOTION_CODE_MAX_LENGTH_TC397 | 10000     | The Promotion Code must be a string with a maximum length of 35 |

  @SaveEnrollmentInvalidPromotionCodeWIthEligiblePlansAndOffers @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code with invalid promotionCode for "<testCondition>" condition
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    When a request is made to the SaveEnrollment Api with an invalid promotionCode for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                         | errorCode | errorMessage                             |
      | SAVE_ENROLLMENT_INVALID_PROMOTION_CODE_NO_MATCH_TC398 | 2000      | Invalid Request: Invalid Plan Promo code |


  @SaveEnrollmentInvalidEnrollmentStatus @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid enrollmentStatus for "<testCondition>" condition
    When a request is made to the SaveEnrollment Api with an invalid enrollmentStatus for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                              | errorCode | errorMessage                                                      |
      | SAVE_ENROLLMENT_INVALID_ENROLLMENT_STATUS_MAX_LENGTH_TC400 | 10000     | The Enrollment Status must be a string with a maximum length of 2 |


  @SaveEnrollmentInvalidEnrollmentStatusWithEligiblePlansAndOffers @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid enrollmentStatus for "<testCondition>" condition
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    When a request is made to the SaveEnrollment Api with an invalid enrollmentStatus for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                                        | errorCode | errorMessage                               |
      | SAVE_ENROLLMENT_INVALID_ENROLLMENT_STATUS_NO_MATCH_TC401             | 2000      | Invalid Request: Invalid Enrollment Status |
      | SAVE_ENROLLMENT_INVALID_ENROLLMENT_STATUS_MISSING_CONFIRMATION_TC402 | 2000      | Invalid Request: Missing conditional parameters-Payment Confirmation Number |

  @SaveEnrollmentInvalidPaymentConfirmationNumberWithEligiblePlansAndOffers @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid paymentConfirmation number for "<testCondition>" condition
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    When a request is made to the SaveEnrollment Api with an invalid paymentConfirmation number for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                                     | errorCode | errorMessage                                                                 |
      | SAVE_ENROLLMENT_INVALID_PAYMENT_CONFIRMATION_NUM_MAX_LENGTH_TC403 | 10000     | The Payment Confirmation Number must be a string with a maximum length of 17 |

  @SaveEnrollmentInvalidBillingPlan @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid "<billingPlan>"
    When a request is made to the SaveEnrollment Api with an invalid billing plan for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                         | errorCode | errorMessage                                                  |
      | SAVE_ENROLLMENT_INVALID_MAX_LENGTH_BILLING_PLAN_TC405 | 10000     | The Billing Plan must be a string with a maximum length of 1  |
      | LOWERCASE_BILLING_PLAN                                | 10000     | The Billing Plan must be a string with a maximum length of 1  |

  @SaveEnrollmentInvalidBillingPlanWithEligiblePlansAndOffers @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid billingPlan for "<testCondition>" condition
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    When a request is made to the SaveEnrollment Api with an invalid billing plan for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                                  | errorCode | errorMessage                                                   |
      | SAVE_ENROLLMENT_INVALID_BILLING_PLAN_EMPTY_TC404               | 2000      | Invalid Request: Missing conditional parameters-Billing Option |
      | SAVE_ENROLLMENT_INVALID_VALUE_BILLING_PLAN_TC406               | 2000      | Invalid Request: Invalid Billing Option                        |
      | SAVE_ENROLLMENT_INVALID_BILLING_PLAN_EMPTY_BUDGET_AMOUNT_TC407 | 2000      | Invalid Request: Missing conditional parameters-Budget Amount  |

  @SaveEnrollmentInvalidBillingPlanWithEligiblePlansAndOffersByPlanCode @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid billingPlan for "<testCondition>" condition
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                            | planCode | promotionCode               | errorCode | errorMessage                                                                                    |
      | SAVE_ENROLLMENT_INVALID_VALUE_BILLING_PLAN_PREPAY_TC406a | PGB      | FIX 10 DOLLARS FOR 12 MONTHS|  2000     | Invalid Request: Invalid Billing Option -PRP/PGB Customers are not eligible for Budget Billing. |

  @SaveEnrollmentInvalidEstimatedBudgetAmountWithEligiblePlansAndOffers @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid estimatedBudgetAmount for "<testCondition>" condition
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    When a request is made to the SaveEnrollment Api with an invalid estimated budget amount for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                          | errorCode | errorMessage                                                                                                                                         |
      | SAVE_ENROLLMENT_INVALID_BUDGET_AMOUNT_MAX_LENGTH_TC408 | 10000     | The Estimated Budget Amount must be an integer with a maximum length of 5                                                                            |
      | SAVE_ENROLLMENT_INVALID_VALUE_BUDGET_AMOUNT_TC409      | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.estimatedBudgetAmount \| LineNumber: 0 \| BytePositionInLine: 276. |

  @SaveEnrollmentInvalidServiceDateWithEligiblePlansAndOffers @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid service date for "<testCondition>" condition
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    And a request is made to the SaveEnrollment Api with an invalid service date for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                      | errorCode | errorMessage                                                                 |
      | SAVE_ENROLLMENT_INVALID_SERVICE_DATE_EMPTY_TC410   | 2000      | Invalid Request: Missing conditional parameters-Customer Request Date        |
      | SAVE_ENROLLMENT_INVALID_SERVICE_DATE_FORMAT_TC411  | 10000     | The Customer Requested Service Date must be a date in the format of yyyyMMdd |
      | SAVE_ENROLLMENT_INVALID_SERVICE_DATE_FORMAT_TC411a | 10000     | The Customer Requested Service Date must be a date in the format of yyyyMMdd |

  @SaveEnrollmentInvalidParametersWithSearchAccountsAndEligiblePlansAndOffers @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid parameters for "<testCondition>" condition
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    And a request is made to the SaveEnrollment Api with invalid parameters for "<testCondition>" condition
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                     |planCode|promotionCode           | errorCode | errorMessage                                                        |
      | SAVE_ENROLLMENT_INVALID_SSP_EMPTY_TC412          |MVS     |25 CENTS FOR 12 MONTHS  | 2000      | Invalid Request: Missing conditional parameters-SSP Participant Code |
      | SAVE_ENROLLMENT_INVALID_SSP_RESULT_VALUE_TC414   |MVS     |25 CENTS FOR 12 MONTHS  | 2000      | Invalid Request: Invalid SSP Result Value                            |
      | SAVE_ENROLLMENT_INVALID_SSP_SPLIT_FEE_EMPTY_TC415 |MVS     |25 CENTS FOR 12 MONTHS  | 10000     | Missing Split Connection Fee Indicator                              |

  @SaveEnrollmentInvalidParametersWithEligiblePlansAndOffers @Phase1  @NegativeFlow
  Scenario Outline: SaveEnrollment Api- Verify response code for invalid parameters for "<testCondition>" condition
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    And a request is made to the SaveEnrollment Api with invalid parameters for "<testCondition>" condition
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                               | errorCode | errorMessage                                                                                                                                                 |
      | SAVE_ENROLLMENT_INVALID_SSP_SPLIT_FEE_VALUE_TC416           | 10000     | Missing Split Connection Fee Indicator                                                                                                                       |
      | SAVE_ENROLLMENT_INVALID_SPLIT_FEE_VALUE_TC417a              | 2000      | Invalid Request: Invalid Split Connection Fee Indicator                                                                                                      |
      | SAVE_ENROLLMENT_INVALID_AGLC_ACCOUNT_EMPTY_TC418            | 2000      | Invalid Request: Missing conditional parameters-AGLC Account Number                                                                                          |
      | SAVE_ENROLLMENT_INVALID_AGLC_ACCOUNT_MAX_LENGTH_TC419       | 10000     | The AGLC Account Number must be a numeric string with a maximum length of 20                                                                                 |
      | SAVE_ENROLLMENT_INVALID_AGLC_SERVICE_ORDER_EMPTY_TC420      | 2000      | Invalid Request: Missing conditional parameters-AGLC Service Order Number                                                                                    |
      | SAVE_ENROLLMENT_INVALID_AGLC_SERVICE_ORDER_MAX_LENGTH_TC421 | 10000     | The AGLC Service Order Number must be a numeric string with a maximum length of 9                                                                            |
      | SAVE_ENROLLMENT_INVALID_NOTES_MAX_LENGTH_TC422              | 10000     | The Notes must be a string with a maximum length of 600                                                                                                      |
      | SAVE_ENROLLMENT_INVALID_SSP_PC_MAX_LENGTH_TC422a            | 10000     | The Seasonal Savings Program Participant Code must be an integer with a maximum length of 9                                                                  |
      | SAVE_ENROLLMENT_INVALID_SSP_PC_VALUE_TC422b                 | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.sspParticipantCode \| LineNumber: 0 \| BytePositionInLine: 396.            |

  @SaveEnrollmentNewFlowMissingNotesCreditCheckSkip @Phase1 @NegativeFlow
  Scenario Outline: SaveEnrollment Api -Verify SaveEnrollment Api throws an appropriate error when notes are missing for <testCondition>
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 2000 and ErrorMessage "Invalid Request: Missing conditional parameters-Notes"
    Examples:
    |testCondition                                                 |planCode|promotionCode               |
    |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_CE_TC_498 |RGB     |FIX 5 DOLLARS FOR 12 MONTHS |
    |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_PC_TC_499 |PGB     |FIX 10 DOLLARS FOR 12 MONTHS|
    |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_PC_TC_500A|PRP     |15 CENTS FOR 12 MONTHS      |

  @SaveEnrollmentNewNotesAdded @Phase1 @HappyFlow
  Scenario: SaveEnrollment Api -Verify SaveEnrollment Api returns success when notes are added in request TC_500B
    When a request is made to the GetEligiblePlansAndOffers for a "GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_PC_TC_500B"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_PC_TC_500B" with "PRP" and ""
    And response should have ErrorCode 0 and ErrorMessage ""

    @SaveEnrollmentEnrollmentNewEnrollment @Phase1 @HappyFlow
    Scenario Outline: SaveEnrollment Api -Verify SaveEnrollment Api returns success when notes are added in request <testCondition>
      When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
      And response should have ErrorCode 0 and ErrorMessage ""
      And a request is made to get Marketer Reference Data
      And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
      And response should have ErrorCode 0 and ErrorMessage ""
      Then perform database validation with the following parameters:
        |cycleCode    |reasonCode    |enrollmentStatus    |accountStatusIdicator    |paymentArrear    |badDebtExemptIndicator    |NCOAProtectIndicator    |feedbackIndicator    |contactDirection    |referredIndicator    |OCRCDETStatus    |OCRCTIMAutomaticIndicator    |contactType  |testCondition  |
        | <cycleCode> | <reasonCode> | <enrollmentStatus> | <accountStatusIdicator> | <paymentArrear> | <badDebtExemptIndicator> | <NCOAProtectIndicator> | <feedbackIndicator> | <contactDirection> | <referredIndicator> | <OCRCDETStatus> | <OCRCTIMAutomaticIndicator> |<contactType>|<testCondition>|

      Examples:
        |testCondition                                                 |planCode|promotionCode                 |cycleCode|reasonCode|enrollmentStatus|accountStatusIdicator|paymentArrear|badDebtExemptIndicator|NCOAProtectIndicator|feedbackIndicator|contactDirection|referredIndicator|OCRCDETStatus|OCRCTIMAutomaticIndicator|contactType|
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_423       |RGB     |FIX 5 DOLLARS FOR 12 MONTHS   |ENRL     |ENRL1     |UDCS            |N                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |ENROLL     |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_DP_TC_425       |CMI     |                              |ENRL     |ENRL1     |UDCS            |N                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |ENROLL     |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PC_TC_431       |PRP     |                              |PRPY     |PRP-ENROLL|UDCS            |N                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |ENROLL     |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PC_TC_432       |PGB     |FIX 10 DOLLARS FOR 12 MONTHS   |PGBP     |PRP-ENROLL|UDCS            |N                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |ENROLL     |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_433       |MVS     |25 CENTS FOR 12 MONTHS        |         |INCL      |INCL            |                     |             |                      |                    |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_DR_TC_435       |CFM     |COM FIX 10 CENTS FOR 12 MONTHS|DEPO     |CRDS      |CRDS            |A                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PR_TC_438       |PGB     |FIX 10 DOLLARS FOR 12 MONTHS   |PGBP     |PRP-ENROLL|PRPY            |N                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |ENROLL     |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PR_TC_439       |PRP     |                              |PRPY     |PRP-ENROLL|PRPY            |N                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |ENROLL     |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RP_TC_442       |PGB     |FIX 10 DOLLARS FOR 12 MONTHS   |PGBP     |INCL      |INCL            | A                   |             |                      |                    |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RP_TC_443       |PRP     |                              |         |INCL      |INCL            |                     |             |                      |                    |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RD_TC_446       |CVS     |COM NO CSC FOR 12 MONTHS      |         |INCL      |INCL            |                     |             |                      |                    |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SF_TC_454       |CF6     |COM FIX 10 CENTS FOR 6 MONTHS |         |INCL      |INCL            |                     |             |                      |                    |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SF_TC_501       |MVS     |                              |         |INCL      |INCL            | A                   |             |                      |                    |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_503       |MVS     |                              |         |INCL      |INCL            | A                   |             |                      |                    |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_504       |CVS     |COM 25 CENTS FOR 12 MONTHS    |         |INCL      |INCL            | A                   |             |                      |                    |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_BD_TC_452       |VML     |                              |ENRL     |ENRL1     |UDCS            |N                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |ENROLL     |

  @SaveEnrollmentPreviouslySaved @Phase1 @HappyFlow
  Scenario Outline: SaveEnrollment Api -Verify SaveEnrollment Api returns success for previously saved enrollment for <testCondition>
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the Save Enrollment API for completion for "<testCondition>" with "<planCode2>" and "<promotionCode2>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then perform database validation with the following parameters:
      |cycleCode    |reasonCode    |enrollmentStatus    |accountStatusIdicator    |paymentArrear    |badDebtExemptIndicator    |NCOAProtectIndicator    |feedbackIndicator    |contactDirection    |referredIndicator    |OCRCDETStatus    |OCRCTIMAutomaticIndicator    |contactType|testCondition    |
      | <cycleCode> | <reasonCode> | <enrollmentStatus> | <accountStatusIdicator> | <paymentArrear> | <badDebtExemptIndicator> | <NCOAProtectIndicator> | <feedbackIndicator> | <contactDirection> | <referredIndicator> | <OCRCDETStatus> | <OCRCTIMAutomaticIndicator> |<contactType>|<testCondition>|

    Examples:
      |testCondition                                                         |planCode|promotionCode                    |planCode2|promotionCode2                  |cycleCode    |reasonCode    |enrollmentStatus    |accountStatusIdicator    |paymentArrear    |badDebtExemptIndicator    |NCOAProtectIndicator    |feedbackIndicator    |contactDirection    |referredIndicator    |OCRCDETStatus    |OCRCTIMAutomaticIndicator    |contactType|
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_424       |RGB     |FIX 5 DOLLARS FOR 12 MONTHS      |RF6      |FIX 8 CENTS FOR 6 MONTHS        |ENRL         |ENRL1         |UDCS                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426       |VML     |                                 |VML      |                                |ENRL         |ENRL1         |UDCS                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426_1     |VML     |                                 |VML      |                                |ENRL         |ENRL1         |UDCS                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_427       |PGB     |FIX 10 DOLLARS FOR 12 MONTHS     |PGB      |FIX 10 DOLLARS FOR 12 MONTHS    |PGBP         |PRP-ENROLL    |UDCS                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_428       |PRP     |                                 |PRP      |                                |PRPY         |PRP-ENROLL    |UDCS                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_434       |RGB     |FIX 5 DOLLARS FOR 12 MONTHS      |MI       |                                |             |INCL          |INCL                |                         |                 |                          |                        |N                    |I                   |N                    |A                |A                            |REQUEST    |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_436       |VML     |                                 |VML      |                                |DEPO         |CRDS          |CRDS                |A                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |REQUEST    |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_440       |PGB     |FIX 10 DOLLARS FOR 12 MONTHS     |PGB      |FIX 10 DOLLARS FOR 12 MONTHS    |PGBP         |PRP-ENROLL    |PRPY                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_502               |MVS     |25 CENTS FOR 12 MONTHS           |RF6      |FIX 8 CENTS FOR 6 MONTHS        | ENRL        | ENRL1        |UDCS                | N                       |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_505               |MVS     |25 CENTS FOR 12 MONTHS           |RF6      |FIX 8 CENTS FOR 6 MONTHS        | ENRL        | ENRL1        |UDCS                | N                       |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_506       |CGB     |COM FIX 10 DOLLARS FOR 12 MONTHS | CGB     |COM FIX 10 DOLLARS FOR 12 MONTHS| ENRL        | ENRL1        |UDCS                | N                       |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_507       |MVS     |                                 | MVS     |                                |  ENROLL     | INCL         |INCL                | N                       |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |REQUEST    |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_437       |VML     |                                 |VML      |                                |ENRL         |ENRL1         |UDCS                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_441       |PRP     |                                 |PRP      |                                |PRPY         |PRP-ENROLL    |PRPY                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_444       |PGB     |FIX 10 DOLLARS FOR 12 MONTHS     |PGB      |FIX 10 DOLLARS FOR 12 MONTHS    |PGBP         |INCL          |INCL                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |REQUEST    |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_445       |PRP     |                                 |PRP      |                                |PRPY         |INCL          |INCL                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |REQUEST    |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_447       |CGB     |COM FIX 10 DOLLARS FOR 12 MONTHS | CGB     |COM FIX 10 DOLLARS FOR 12 MONTHS| ENRL        |INCL          |INCL                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |REQUEST    |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_453       |VML     |                                 |VML      |                                |ENRL         |ENRL1         |UDCS                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |

  @SaveEnrollmentSSPValidations @Phase1 @NegativeFlow
  Scenario Outline: SaveEnrollment Api SSPValidations -Verify different errors returned due to SSPValidations based on <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "<recordType>"
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "<testCondition>"
    And response should have ErrorCode 2000 and ErrorMessage "<errorMessage>"

    Examples:
      |testCondition                                                         |planCode|promotionCode               |recordType                                |errorMessage                                                          |
      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_478                           |RF6     |                            |SSP ACCOUNT RECORD                        |Invalid Request: Missing conditional parameters-Customer Code         |
      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_480                           |MVS     |25 CENTS FOR 12 MONTHS      |SSP FALL TURN ON RECORD                   |Invalid Request: Missing conditional parameters-Customer Code         |
      |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_490                    |RF6     |                            |SSP ACCOUNT RECORD                        |Invalid Request: Missing conditional parameters - SSP Participant Code|

  @SSPValidations @Phase1 @NegativeFlow
  Scenario Outline: SaveEnrollment Api SSPValidations -Verify different errors returned due to SSPValidations based on <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "<recordType>"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for completion for "<testCondition>" with "<planCode2>" and "<promotionCode2>"
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      |testCondition                                                         |planCode|promotionCode|planCode2|promotionCode2        |recordType             |errorMessage                                                        |errorCode|
      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_479                           |RF6     |             |MVS      |25 CENTS FOR 12 MONTHS|SSP ACCOUNT RECORD     |Missing Customer Code                                               |10000    |
      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_481                           |RF6     |             |MVS      |25 CENTS FOR 12 MONTHS|SSP FALL TURN ON RECORD|Missing Customer Code                                               |10000    |
      |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_493                    |RF6     |             |MVS      |25 CENTS FOR 12 MONTHS|SSP FALL TURN ON RECORD|Invalid Request: Missing conditional parameters-SSP Participant Code|2000     |
      |SSP_VALIDATION_PREMISES_CODE_MISSING_TC_492                           |RF6     |             |MVS      |25 CENTS FOR 12 MONTHS|SSP FALL TURN ON RECORD|Invalid Request: Missing conditional parameters-SSP Participant Code|2000     |

  @SSPValidations @Phase1 @NegativeFlow
  Scenario Outline: SaveEnrollment Api SSPValidations -Verify different errors returned due to SSPValidations based on <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "<recordType>"
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the Save Enrollment API for completion for "<testCondition>" with "<planCode2>" and "<promotionCode2>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "<recordType2>"
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "<testCondition2>"
    And response should have ErrorCode 2000 and ErrorMessage "<errorMessage>"

    Examples:
      |testCondition                                                         |planCode|promotionCode               |recordType        |recordType2                             |planCode2|promotionCode2|testCondition2                                      |errorMessage                                                          |
      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_482                           |MVS     |25 CENTS FOR 12 MONTHS      |SSP ACCOUNT RECORD|SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD|MVS      |              |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_482_2       |Invalid Request: Missing conditional parameters-Customer Code         |
      |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_494                    |MVS     |25 CENTS FOR 12 MONTHS      |SSP ACCOUNT RECORD|SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD|MVS      |              |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_494_2|Invalid Request: Missing conditional parameters - SSP Participant Code|

  @SSPValidations @Phase1 @NegativeFlow
  Scenario Outline: SaveEnrollment Api SSPValidations -Verify different errors returned due to SSPValidations based on <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "<recordType>"
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the Save Enrollment API for completion for "<testCondition2>" with "<planCode2>" and "<promotionCode2>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "<recordType2>"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for completion for "<testCondition>" with "<planCode2>" and "<promotionCode2>"
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      |testCondition                                                         |planCode|promotionCode               |recordType        |recordType2                             |planCode2|promotionCode2|testCondition2                                      |errorMessage                                                        |errorCode|
      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_483                           |MVS     |25 CENTS FOR 12 MONTHS      |SSP ACCOUNT RECORD|SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD|MVS      |              |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_483_2       |Missing Customer Code                                               |10000    |
      |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_495                    |MVS     |25 CENTS FOR 12 MONTHS      |SSP ACCOUNT RECORD|SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD|MVS      |              |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_495_2|Invalid Request: Missing conditional parameters-SSP Participant Code|2000     |

  @SSPValidations @Phase1 @NegativeFlow
  Scenario Outline: SaveEnrollment Api SSPValidations -Verify different errors returned due to SSPValidations based on <testCondition>
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "<recordType>"
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "<testCondition>"
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      |testCondition                                                         |planCode|promotionCode                      |recordType             |errorCode|errorMessage                                                 |
      |SSP_VALIDATION_PREMISES_CODE_MISSING_TC_484                           |CGB     |COM FIX 10 DOLLARS FOR 12 MONTHS   |SSP ACCOUNT RECORD     |2000     |Invalid Request: Missing conditional parameters-Premises Code|
      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_486                           |CGB     |COM FIX 10 DOLLARS FOR 12 MONTHS   |SSP FALL TURN ON RECORD|2000     |Invalid Request: Missing conditional parameters-Customer Code|

  @SSPValidations @Phase1 @NegativeFlow
  Scenario: SaveEnrollment Api SSPValidations -Verify different errors returned due to SSPValidations based on SSP_VALIDATION_PREMISES_CODE_MISSING_TC_485
    When a request is made to the GetEligiblePlansAndOffers for a "SSP_VALIDATION_PREMISES_CODE_MISSING_TC_485"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "SSP_VALIDATION_PREMISES_CODE_MISSING_TC_485"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "SSP_VALIDATION_PREMISES_CODE_MISSING_TC_485" with "CGB" and "COM FIX 10 DOLLARS FOR 12 MONTHS"
    And response should have ErrorCode 10000 and ErrorMessage "Missing Premises Code"

  @SSPValidations @Phase1 @NegativeFlow
  Scenario Outline: SaveEnrollment Api SSPValidations -Verify different errors returned due to SSPValidations based on <testCondition>
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "<recordType>"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for completion for "<testCondition>" with "<planCode2>" and "<promotionCode2>"
    And response should have ErrorCode 10000 and ErrorMessage "<errorMessage>"

    Examples:
      |testCondition                                                         |planCode|promotionCode                      |recordType             |planCode2|promotionCode2            |errorMessage                |
      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_487                           |CGB     |COM FIX 10 DOLLARS FOR 12 MONTHS   |SSP FALL TURN ON RECORD|CVS      |COM 25 CENTS FOR 12 MONTHS|Missing Customer Code       |
      |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_491                    |CVS     |COM 25 CENTS FOR 12 MONTHS         |SSP ACCOUNT RECORD     |CVS      |                          |Missing SSP Participant Code|

  @SSPValidations @Phase1 @NegativeFlow
  Scenario: SaveEnrollment Api SSPValidations -Verify different errors returned due to SSPValidations based on SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488
    When a request is made to the GetEligiblePlansAndOffers for a "SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488" with "CF6" and "COM FIX 5 DOLLARS FOR 6 MONTHS"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "SSP ACCOUNT RECORD"
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the Save Enrollment API for completion for "SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488" with "CVS" and ""
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD"
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488_2"
    And response should have ErrorCode 2000 and ErrorMessage "Invalid Request: Missing conditional parameters-Premises Code"

  @SSPValidations @Phase1 @NegativeFlow
  Scenario: SaveEnrollment Api SSPValidations -Verify different errors returned due to SSPValidations based on SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496
    When a request is made to the GetEligiblePlansAndOffers Api for "SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496" with "RF6" and ""
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "SSP ACCOUNT RECORD"
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for completion for "SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496" with "MVS" and ""
    And response should have ErrorCode 10000 and ErrorMessage "Invalid Request: Missing conditional parameters-Payment Confirmation Number"

  @SSPValidations @Phase1 @NegativeFlow
  Scenario Outline: SaveEnrollment Api SSPValidations -Verify different errors returned due to SSPValidations based on <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      |testCondition                                  |planCode|promotionCode               |errorMessage                                       |errorCode       |
      |SSP_VALIDATION_INVALID_SSP_RESULT_TC_497B      |RF6     |                            |Parameter Value should be null -SSP Result         |2200            |
      |SSP_VALIDATION_INVALID_SSP_CODE_TC_497C        |RF6     |                            |Parameter Value should be null-SSP Participant Code|2200            |


  @SaveEnrollmentGetPrepayPlansRequotePositive @HappyFlow @Phase1
  Scenario Outline: GetPrepayPlansRequote API – returns quotes for <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then the response should contain the expected plans
    Then the response plans should contains a "<planCode>" plan
    Then a request is made to get Marketer Reference Data
    And a request is made to the SaveEnrollment Api for with "<planCode>" planCode for "<testCondition>" condition
    Then verify response code of "SaveEnrollment" Api is 200
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the GetPrepayPlansRequote Api for "<testCondition>" condition
    Then verify response code of "GetPrepayPlansRequote" Api is 200
    Then the response should contain the expected prepay plans and "<planCode>" planCode
    Then a request is made to get Marketer Reference Data
    And a request is made to the SaveEnrollment Api for prepay with "<planCode>" after requote for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""

    Examples:
      | planCode | testCondition                                                        |
      | PRP      | GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_429|
      | PGB      | GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_430|
      | PRP      | GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_448|
      | PGB      | GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_449|

    @SaveErollmentPositive @HappyFlow @Phase1
  Scenario Outline: SaveEnrollment Api -Verify SaveEnrollment API for <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<pricePlan>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for completion for "<testCondition>" with "<pricePlan>" and ""
    And response should have ErrorCode 0 and ErrorMessage ""
      Examples:
      |testCondition                                                      |pricePlan|promotionCode               |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_450    |PRP      |                            |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_451    |PGB      |FIX 10 DOLLARS FOR 12 MONTHS|
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_SSP_TC_455|MVS      |25 CENTS FOR 12 MONTHS      |
