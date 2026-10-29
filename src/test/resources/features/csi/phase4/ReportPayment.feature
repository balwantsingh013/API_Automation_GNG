Feature: Verify ReportPayment Api

  @ReportPayment @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to ReportPayment Api for "<testCondition>"
    Then verify response code of "ReportPayment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And response success is "<success>" and data is "<data>"

    Examples:
      | testCondition                                                                    | errorCode | errorMessage                                      | success | data |
      | TC_1__Negative__Missing_Request_ID__ReportPayment_                         | 10001     | Missing Request ID                                | false   | null |
      | TC_2__Negative__Invalid_Request_ID_Length__ReportPayment_                  | 10002     | Invalid Request ID                                | false   | null |
      | TC_3__Negative__Duplicate_Request_ID__ReportPayment_                       | 10003     | Duplicate Request ID                              | false   | null |
      | TC_4__Negative__Missing_customerCode__ReportPayment_                       | 10011     | Missing Customer Code                             | false   | null |
      | TC_5__Negative__Invalid_customerCode_Length__ReportPayment_                | 10015     | Invalid Customer Code Format                      | false   | null |
      | TC_6__Negative__Invalid_customerCode_Format__ReportPayment_                | 10015     | Invalid Customer Code Format                      | false   | null |
      | TC_7__Negative__Missing_premisesCode__ReportPayment_                       | 10013     | Missing Premises Code                             | false   | null |
      | TC_8__Negative__Invalid_premisesCode_Length__ReportPayment_                | 10005     | Invalid Premises Code Format                      | false   | null |
      | TC_9__Negative__Invalid_premisesCode_Format__ReportPayment_                | 10005     | Invalid Premises Code Format                      | false   | null |
      | TC_10__Negative__Invalid_Account_Number__Invalid_Account__ReportPayment_   | 40015     | Invalid Account Number                            | false   | null |
      | TC_11__Negative__Missing_Payment_Date_                                     | 11119     | Missing Payment Date                              | false   | null |
      | TC_12__Negative__Invalid_Payment_Date_Format_                              | 11121     | Invalid Payment Date Format                       | false   | null |
      | TC_13__Negative__Missing_Payment_Amount_                                   | 10515     | Missing Payment Amount                            | false   | null |
      | TC_14__Negative__Invalid_Payment_Amount_Format_NonNumeric_                 | 10519     | Invalid Payment Amount                            | false   | null |
      | TC_15__Negative__Invalid_Payment_Amount_Format_DecimalPlaces_              | 10519     | Invalid Payment Amount                            | false   | null |
      | TC_16__Negative__Missing_Payment_Type_                                     | 11123     | Missing Payment Type                              | false   | null |
      | TC_17__Negative__Invalid_Payment_Type_Format_                              | 11125     | Invalid Payment Type Format                       | false   | null |
      | TC_18__Negative__Invalid_Payment_Confirmation_Number_Format_               | 10313     | Invalid Payment Confirmation Number Format        | false   | null |
      | TC_19__Negative__Missing_Origin_                                           | 10029     | Missing Origin                                    | false   | null |
      | TC_20__Negative__Invalid_Origin_                                           | 10085     | Invalid Origin                                    | false   | null |
      | TC_21__Negative__Open_SONP_Service_Order_                                  | 40323     | Account Ineligible for Payment Notification       | false   | null |
      | TC_22__Negative__SONP_Credit_History_Entry_                                | 40323     | Account Ineligible for Payment Notification       | false   | null |
      | TC_23__Negative__Unresolved_SONP_Transaction_                              | 40323     | Account Ineligible for Payment Notification       | false   | null |
      | TC_24__Negative__Past_Due_Balance_Exceeds_Threshold_                       | 40323     | Account Ineligible for Payment Notification       | false   | null |
      | TC_25__Negative__Invalid_Payment_Type_                                     | 40325     | Invalid Payment Type                              | false   | null |
      | TC_26__Negative__Invalid_Payment_Date_Future_Date_                         | 40327     | Invalid Payment Date                              | false   | null |
      | TC_27__Negative__Invalid_Payment_Date_Outside_Allowed_Backdate_Window_     | 40327     | Invalid Payment Date                              | false   | null |
      | TC_28__Negative__Payment_Amount_Equals_Zero_                               | 40307     | Payment amount must be positive                   | false   | null |
      | TC_29__Negative__Payment_Amount_Less_Than_Zero_                            | 40307     | Payment amount must be positive                   | false   | null |
      | TC_30__Negative__Payment_Amount_Exceeds_Maximum_Allowed_                   | 40329     | Payment Amount Exceeds Maximum Allowed            | false   | null |
      | TC_31__Negative__WS_Missing_Confirmation_Number_                           | 40173     | Missing Payment Confirmation Number               | false   | null |
      | TC_32__Negative__PH_Missing_Confirmation_Number_                           | 40173     | Missing Payment Confirmation Number               | false   | null |
      | TC_33__Negative__WI_Missing_Confirmation_Number_                           | 40173     | Missing Payment Confirmation Number               | false   | null |

  @ReportPaymentPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to ReportPayment Api for "<testCondition>"
    Then verify response code of "ReportPayment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And response success is "<success>" and data is "<data>"
    And ReportPayment paCreated is "<paCreated>" paAmount is "<paAmount>" and paDueDate is "<paDueDate>"

    Examples:
      | testCondition                                                              | errorCode | errorMessage | success | data    | paCreated | paAmount      | paDueDate |
      | TC_34__Positive__OT_Payment_Type_Without_Confirmation_Number_              | 0         |              | true    | notNull | false     | null          | null      |
      | TC_35__Positive__Payment_Amount_Less_Than_Past_Due_Balance_                | 0         |              | true    | notNull | false     | null          | null      |
      | TC_36__Positive__No_Recent_PREC_                                           | 0         |              | true    | notNull | false     | null          | null      |
      | TC_37__Positive__Net_Past_Due_Balance_Zero_or_Negative_                    | 0         |              | true    | notNull | false     | null          | null      |
      | TC_38__Positive__Existing_Payment_Arrangement_                             | 0         |              | true    | notNull | false     | null          | null      |
      | TC_39__Positive__Active_Bank_Draft_                                        | 0         |              | true    | notNull | false     | null          | null      |
      | TC_40__Positive__Active_Budget_Billing_                                    | 0         |              | true    | notNull | false     | null          | null      |
      | TC_41__Positive__Home_Warranty_                                            | 0         |              | true    | notNull | false     | null          | null      |
      | TC_42__Positive__Recurring_Credit_Card_                                    | 0         |              | true    | notNull | false     | null          | null      |
      | TC_43__Positive__MPAY_Defaulted_Payment_Arrangement_History_               | 0         |              | true    | notNull | false     | null          | null      |
      | TC_44__Positive__Delinquency_Stage_Update_Rule_                            | 0         |              | true    | notNull | true      | notNull       | notNull   |
      | TC_45__Positive__Hold_Payment_Arrangement_Created_                         | 0         |              | true    | notNull | true      | notNull       | notNull   |
      | TC_46__Positive__Hold_Response_Amount_                                     | 0         |              | true    | notNull | true      | matchesPastDue | notNull  |
      | TC_47__Positive__Hold_Response_Due_Date_                                   | 0         |              | true    | notNull | true      | notNull       | notNull   |
      | TC_48__Positive__Payment_Arrangement_Enrollment_Note_Created_              | 0         |              | true    | notNull | true      | notNull       | notNull   |
