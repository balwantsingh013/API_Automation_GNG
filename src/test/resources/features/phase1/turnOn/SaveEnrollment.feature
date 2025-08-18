Feature: Verify SaveEnrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @SaveEnrollmentWithValidData @Phase1 @HappyFlow
  Scenario: Verify SaveEnrollment Api with valid data
    When a request is made to the SaveEnrollment Api
    Then verify response code of "SaveEnrollment" Api is 200

  @SaveEnrollmentWithInvalidRequestID @Phase1 @NegativeFlow
  Scenario Outline: Verify SaveEnrollment Api with invalid requestID "<requestID>"
    When a request is made to the SaveEnrollment Api with "<requestID>"
    Then verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID            | errorCode | errorMessage         |
      | EMPTY_REQUEST_ID     | 10001     | Missing Request ID   |
      | DUPLICATE_REQUEST_ID | 10003     | Duplicate Request ID |
      | LONG_REQUEST_ID      | 10002     | Invalid Request ID   |

  @SaveEnrollmentInvalidCustomerCODE @Phase1  @NegativeFlow
  Scenario Outline: Verify SaveEnrollment Api with invalid "<customerCODE>" code
    When a request is made to the SaveEnrollment Api with  customer "<customerCODE>" code
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | customerCODE                | errorCode | errorMessage                                   |
      | DUPLICATE_CUSTOMER_CODE     | 2000      | Invalid Request: Transaction ID does not exist |
      | MAX_LENGTH_CUSTOMER_CODE    | 2000      | Invalid Request: Transaction ID does not exist |
      | UNICODE_CHARS_CUSTOMER_CODE | 2000      | Invalid Request: Transaction ID does not exist |
      | DUPLICATE_CUSTOMER_CODE     | 2000      | Invalid Request: Transaction ID does not exist |


  @SaveEnrollmentInvalidPromotionCODE @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<premisesCODE>" code
    When a request is made to the SaveEnrollment Api with  premises "<premisesCODE>" code
    Then verify response code of "Save Enrollment" Api is 200
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
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | transactionID                     | errorCode | errorMessage                            |
      | MIN_LENGTH_TRANSACTION_ID         | 2000      | Invalid Request: Invalid Transaction ID |
      | WHITESPACE_BETWEEN_TRANSACTION_ID | 2000      | Invalid Request: Invalid Transaction ID |


  @SaveEnrollmentInvalidTransactionType @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<transactionType>"Type
    When a request is made to the SaveEnrollment Api with  transaction "<transactionType>" Type
    Then verify response code of "Save Enrollment" Api is 200
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
    Then verify response code of "Save Enrollment" Api is 200
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
    Then verify response code of "Save Enrollment" Api is 200
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
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | enrollmentStatus                      | errorCode | errorMessage                                                      |
      | MIN_LENGTH_ENROLLMENT_STATUS          | 2000      | Invalid Request: Invalid Enrollment Status                        |
      | SPECIAL_CHARS_ENROLLMENT_STATUS       | 10000     | The Enrollment Status must be a string with a maximum length of 2 |
      | EMPTY_ENROLLMENT_STATUS               | 10000     | Missing Enrollment Status                                         |
      | LOWERCASE_ENROLLMENT_STATUS           | 10000     | The Enrollment Status must be a string with a maximum length of 2 |
      | MAX_LENGTH_ENROLLMENT_STATUS          | 10000     | The Enrollment Status must be a string with a maximum length of 2 |
      | WHITESPACE_CONTAINS_ENROLLMENT_STATUS | 10000     | The Enrollment Status must be a string with a maximum length of 2 |

  @SaveEnrollmentInvalidBillingPlan @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<billingPlan>"
    When a request is made to the SaveEnrollment Api with billing "<billingPlan>" Plan
    Then verify response code of "Save Enrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | billingPlan                | errorCode | errorMessage                                                   |
      | MAX_LENGTH_BILLING_PLAN    | 10000     | The Billing Plan must be a string with a maximum length of 1   |
      | SPECIAL_CHARS_BILLING_PLAN | 10000     | The Billing Plan must be a string with a maximum length of 1   |
      | EMPTY_BILLING_PLAN         | 2000      | Invalid Request: Missing conditional parameters-Billing Option |
      | LOWERCASE_BILLING_PLAN     | 10000     | The Billing Plan must be a string with a maximum length of 1   |

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
    And a request is made to the Save Enrollment API for the "GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_PC_TC_500B" with "PRP" and "15 CENTS FOR 12 MONTHS"
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
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PC_TC_432       |PGB     |FIX 5 DOLLARS FOR 12 MONTHS   |PRPY     |PRP-ENROLL|UDCS            |N                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |ENROLL     |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_433       |MVS     |25 CENTS FOR 12 MONTHS        |         |INCL      |INCL            |                     |             |                      |                    |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_DR_TC_435       |CFM     |COM FIX 10 CENTS FOR 12 MONTHS|DEPO     |CRDS      |CRDS            |A                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PR_TC_438       |PGB     |FIX 5 DOLLARS FOR 12 MONTHS   |PRPY     |PRP-ENROLL|UDCS            |N                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |ENROLL     |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PR_TC_439       |PRP     |                              |PRPY     |PRP-ENROLL|PRPY            |N                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |ENROLL     |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RP_TC_442       |PGB     |FIX 5 DOLLARS FOR 12 MONTHS   |PRPY     |PRP-ENROLL|UDCS            |N                    |N            |N                     |N                   |N                |I               |N                |A            |A                        |ENROLL     |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RP_TC_443       |PRP     |                              |         |INCL      |INCL            |                     |             |                      |                    |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RD_TC_446       |CVS     |COM NO CSC FOR 12 MONTHS      |         |INCL      |INCL            |                     |             |                      |                    |N                |I               |N                |A            |A                        |REQUEST    |
        |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SF_TC_454       |CF6     |COM FIX 10 CENTS FOR 6 MONTHS |         |INCL      |INCL            |                     |             |                      |                    |N                |I               |N                |A            |A                        |REQUEST    |


  @SaveEnrollmentProviouslySaved @Phase1 @HappyFlow
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
      |testCondition                                                         |planCode|promotionCode               |planCode2|promotionCode2              |cycleCode    |reasonCode    |enrollmentStatus    |accountStatusIdicator    |paymentArrear    |badDebtExemptIndicator    |NCOAProtectIndicator    |feedbackIndicator    |contactDirection    |referredIndicator    |OCRCDETStatus    |OCRCTIMAutomaticIndicator    |contactType|
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_424       |RGB     |FIX 5 DOLLARS FOR 12 MONTHS |RF6      |FIX 8 CENTS FOR 6 MONTHS    |ENRL         |ENRL1         |UDCS                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426       |VML     |                            |VML      |                            |ENRL         |ENRL1         |UDCS                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426_1     |VML     |                            |VML      |                            |ENRL         |ENRL1         |UDCS                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_427       |PGB     |FIX 10 DOLLARS FOR 12 MONTHS|PGB      |FIX 10 DOLLARS FOR 12 MONTHS|PGBP         |PRP-ENROLL    |UDCS                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_428       |PRP     |                            |PRP      |                            |PRPY         |PRP-ENROLL    |UDCS                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_434       |RGB     |FIX 5 DOLLARS FOR 12 MONTHS |MI       |                            |             |INCL          |INCL                |                         |                 |                          |                        |N                    |I                   |N                    |A                |A                            |REQUEST    |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_436       |VML     |                            |VML      |                            |DEPO         |CRDS          |CRDS                |A                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |REQUEST    |
      |GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_440       |PGB     |FIX 10 DOLLARS FOR 12 MONTHS|PGB      |FIX 10 DOLLARS FOR 12 MONTHS|PGBP         |PRP-ENROLL    |PRPY                |N                        |N                |N                         |N                       |N                    |I                   |N                    |A                |A                            |ENROLL     |


  @SSPValidations1 @Phase1 @NegativeFlow
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
      |testCondition                                                         |planCode|promotionCode               |recordType                                |errorMessage|
#      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_478                           |RF6     |                            |SSP ACCOUNT RECORD                        |Invalid Request: Missing conditional parameters-Customer Code|
#      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_480                           |MVS     |25 CENTS FOR 12 MONTHS      |SSP FALL TURN ON RECORD                   |Invalid Request: Missing conditional parameters-Customer Code|
#      |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_490                    |RF6     |                            |SSP ACCOUNT RECORD                        |Invalid Request: Missing conditional parameters - SSP Participant Code|

  @SSPValidations2 @Phase1 @NegativeFlow
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
      |testCondition                                                         |planCode|promotionCode               |planCode2|promotionCode2        |recordType             |errorMessage|errorCode|
      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_479                           |RF6     |                            |MVS      |25 CENTS FOR 12 MONTHS|SSP ACCOUNT RECORD     |Missing Customer Code|10000|
      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_481                           |RF6     |                            |MVS      |25 CENTS FOR 12 MONTHS|SSP FALL TURN ON RECORD|Missing Customer Code|10000|
  |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_493                           |RF6     |                            |MVS      |25 CENTS FOR 12 MONTHS|SSP FALL TURN ON RECORD|Invalid Request: Missing conditional parameters-SSP Participant Code|2000|


  @SSPValidations21 @Phase1 @NegativeFlow
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
      |testCondition                                                         |planCode|promotionCode               |planCode2|promotionCode2        |recordType             |errorMessage|errorCode|
   |SSP_VALIDATION_PREMISES_CODE_MISSING_TC_492                           |RF6     |                            |MVS      |25 CENTS FOR 12 MONTHS|SSP FALL TURN ON RECORD|Invalid Request: Missing conditional parameters-SSP Participant Code|2000|

  @SSPValidations3 @Phase1 @NegativeFlow
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
      |testCondition                                                         |planCode|promotionCode               |recordType        |recordType2                             |planCode2|promotionCode2|testCondition2                               |errorMessage|
      #|SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_482                           |MVS     |25 CENTS FOR 12 MONTHS      |SSP ACCOUNT RECORD|SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD|MVS      |              |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_482_2|Invalid Request: Missing conditional parameters-Customer Code|
      |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_494                    |MVS     |25 CENTS FOR 12 MONTHS      |SSP ACCOUNT RECORD|SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD|MVS      |              |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_494_2|Invalid Request: Missing conditional parameters - SSP Participant Code|

  @SSPValidations4 @Phase1 @NegativeFlow
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
      |testCondition                                                         |planCode|promotionCode               |recordType        |recordType2                             |planCode2|promotionCode2|testCondition2                               |errorMessage|errorCode|
      #|SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_483                           |MVS     |25 CENTS FOR 12 MONTHS      |SSP ACCOUNT RECORD|SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD|MVS      |              |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_483_2|Missing Customer Code|10000|
      |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_495                           |MVS     |25 CENTS FOR 12 MONTHS      |SSP ACCOUNT RECORD|SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD|MVS      |              |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_495_2|Invalid Request: Missing conditional parameters-SSP Participant Code|2000|


  @SSPValidations5 @Phase1 @NegativeFlow
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
    And response should have ErrorCode 2000 and ErrorMessage "Invalid Request: Missing conditional parameters-Premises Code"

    Examples:
      |testCondition                                                         |planCode|promotionCode                      |recordType                                  |
      |SSP_VALIDATION_PREMISES_CODE_MISSING_TC_484                           |CGB     |COM FIX 10 DOLLARS FOR 12 MONTHS                            |SSP ACCOUNT RECORD |


  @SSPValidations6 @Phase1 @NegativeFlow
  Scenario Outline: SaveEnrollment Api SSPValidations -Verify different errors returned due to SSPValidations based on <testCondition>
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 10000 and ErrorMessage "Missing Premises Code"

    Examples:
      |testCondition                                                         |planCode|promotionCode                      |
      |SSP_VALIDATION_PREMISES_CODE_MISSING_TC_485                           |CGB     |COM FIX 10 DOLLARS FOR 12 MONTHS   |


  @SSPValidations7 @Phase1 @NegativeFlow
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
    And response should have ErrorCode 2000 and ErrorMessage "Invalid Request: Missing conditional parameters-Customer Code"


    Examples:
      |testCondition                                                         |planCode|promotionCode                      |recordType             |
      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_486                           |CGB     |COM FIX 10 DOLLARS FOR 12 MONTHS   |SSP FALL TURN ON RECORD|



  @SSPValidations8 @Phase1 @NegativeFlow
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
    And a request is made to the Save Enrollment API for completion for "<testCondition>" with "<planCode2>" and "<promotionCode2>"
    And response should have ErrorCode 10000 and ErrorMessage "Missing Customer Code"

    Examples:
      |testCondition                                                         |planCode|promotionCode                      |recordType             |planCode2|promotionCode2            |
      |SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_487                           |CGB     |COM FIX 10 DOLLARS FOR 12 MONTHS   |SSP FALL TURN ON RECORD|CVS      |COM 25 CENTS FOR 12 MONTHS|

  @SSPValidations9 @Phase1 @NegativeFlow
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
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the Save Enrollment API for completion for "<testCondition>" with "<planCode2>" and "<promotionCode2>"
    And response should have ErrorCode 0 and ErrorMessage ""
#    Then a request is made to the SearchAccountsApi for "<testCondition>"
#    And response should have ErrorCode 0 and ErrorMessage ""
#    And response should have "recordType" as "<recordType2>"
#    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "<testCondition2>"
#    And response should have ErrorCode 2000 and ErrorMessage "Invalid Request: Missing conditional parameters-Premises Code"

    Examples:
      |testCondition                                                         |planCode|promotionCode                       |recordType        |recordType2                             |planCode2|promotionCode2|testCondition2                               |
      |SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488                           |CF6     |COM FIX 5 DOLLARS FOR 6 MONTHS      |SSP ACCOUNT RECORD|SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD|CVS      |              |SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488_2|

  @SSPValidations10 @Phase1 @NegativeFlow
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
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for completion for "<testCondition>" with "<planCode2>" and "<promotionCode2>"
    And response should have ErrorCode 10000 and ErrorMessage "<errorMessage>"
    Examples:
      |testCondition                                                         |planCode|promotionCode                    |planCode2|promotionCode2        |recordType             |errorMessage|
      |SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_491                    |CVS     |COM 25 CENTS FOR 12 MONTHS   |CVS      |                      |SSP ACCOUNT RECORD|Missing SSP Participant Code|

  @SSPValidations12 @Phase1 @NegativeFlow
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
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for completion for "<testCondition>" with "<planCode2>" and "<promotionCode2>"
    And response should have ErrorCode 10000 and ErrorMessage "<errorMessage>"


    Examples:
      |testCondition                                                         |planCode|promotionCode               |recordType                                |errorMessage|planCode2|promotionCode2|
      |SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496             |RF6     |                            |SSP ACCOUNT RECORD                        |Invalid Request: Missing conditional parameters-Payment Confirmation Number|MVS||


  @SSPValidations13 @Phase1 @NegativeFlow
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
      |testCondition                                                         |planCode|promotionCode               |recordType                                |errorMessage|planCode2|promotionCode2|errorCode|
      |SSP_VALIDATION_INVALID_SSP_RESULT_TC_497B            |RF6     |                            |SSP ACCOUNT RECORD                        |Parameter Value should be null -SSP Result|MVS||2200            |
      |SSP_VALIDATION_INVALID_SSP_CODE_TC_497C            |RF6     |                            |SSP ACCOUNT RECORD                        |Parameter Value should be null-SSP Participant Code|MVS||2200            |


  @SaveEnrollmentGetPrepayPlansRequotePositive @HappyFlow
  Scenario Outline: GetPrepayPlansRequote API – returns quotes for <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then the response should contain the expected plans
    Then the response plans should contains a "<planCode>" plan
    Then a request is made to get Marketer Reference Data
    And a request is made to the SaveEnrollment Api for prepay with "<planCode>" planCode for "<testCondition>" condition
    Then verify response code of "SaveEnrollment" Api is 200
    Then a prepay transaction is returned from searchAccounts api for "<testCondition>"
    And a request is made to the GetPrepayPlansRequote Api for "<testCondition>" condition
    Then verify response code of "GetPrepayPlansRequote" Api is 200
    Then the response should contain the expected prepay plans and "<planCode>" planCode
    Then a request is made to get Marketer Reference Data
    And a request is made to the SaveEnrollment Api for prepay with "<planCode>" after requote for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""

    Examples:
      | planCode | testCondition                            |
      #| PRP      | GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_429|
      | PGB      | GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_430|