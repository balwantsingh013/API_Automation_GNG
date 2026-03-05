Feature: Verify GetBillHistory Api

  @GetBillHistory @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetBillHistory Api for "<testCondition>"
    Then verify response code of "GetBillHistory" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have data as null
#    And the response should have success as false

    Examples:
      | testCondition                                   | errorCode | errorMessage                                |
#      | TC_60__Negative__Missing_Request_ID             | 10001     | Missing Request ID                           |
#      | TC_61__Negative__Invalid_Request_ID_Length      | 10002     | Invalid Request ID                           |
#      | TC_62__Negative__Duplicate_Request_ID           | 10003     | Duplicate Request ID                         |
#      | TC_63__Negative__Missing_customerCode           | 10011     | Missing Customer Code                        |
#      | TC_64__Negative__Invalid_customerCode_Length    | 10015     | Invalid Customer Code Format                 |
#      | TC_65__Negative__Invalid_customerCode_Format    | 10015     | Invalid Customer Code Format                 |
#      | TC_66__Negative__Missing_premisesCode           | 10013     | Missing Premises Code                        |
#      | TC_67__Negative__Invalid_premisesCode_Length    | 10005     | Invalid Premises Code Format                 |
#      | TC_68__Negative__Invalid_premisesCode_Format    | 10005     | Invalid Premises Code Format                 |
#      | TC_69__Negative__Invalid_Account_Number         | 40015     | Invalid Account Number                       |
#      | TC_70__Negative__Missing_Number_of_Months       | 10393     | Missing Number of Months                     |
#      | TC_71__Negative__Invalid_Number_of_Months_Format_NotANumber | 10000 | The JSON value could not be converted to System.Int32. Path: $.numberOfMonths [PIPE] LineNumber: 0 [PIPE] BytePositionInLine: |
#      | TC_72__Negative__Invalid_Number_of_Months_Zero  | 10395     | Invalid Number of Months                     |
#      | TC_73__Negative__Invalid_Number_of_Months_Negative | 10395  | Invalid Number of Months                     |
#      | TC_74__Negative__Invalid_Number_of_Months_Length | 10405    | Number of Months exceeds maximum allowed                     |

#
  @GetBillHistoryPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetBillHistory Api for "<testCondition>"
    Then verify response code of "GetBillHistory" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as true
#    And the response should have data as "<data>"

    Examples:
      | testCondition                                            | errorCode | errorMessage | data    |
      | TC_75__Positive__Usage_History_BillDate_Format           | 0         |              | notNull |
#      | TC_76__Positive__Usage_History_UsageFromDate_Format      | 0         |              | notNull |
#      | TC_77__Positive__Usage_History_UsageToDate_Format        | 0         |              | notNull |
#      | TC_78__Positive__Usage_History_DaysOfService_Format      | 0         |              | notNull |
#      | TC_79__Positive__Usage_History_HeatingDegreeDays_Format     | 0         |              | notNull |
#      | TC_80__Positive__Usage_History_TotalBilledConsumption_Format | 0        |              | notNull |
#      | TC_81__Positive__Usage_History_BalanceBroughtForward_Format  | 0        |              | notNull |
#      | TC_82__Positive__Usage_History_GasServiceCharges_Format      | 0        |              | notNull |
#      | TC_83__Positive__Usage_History_OtherCharges_Format           | 0        |              | notNull |
#      | TC_84__Positive__Usage_History_PromotionalDiscounts_Format   | 0        |              | notNull |
#      | TC_85__Positive__Usage_History_Taxes_Format                  | 0        |              | notNull |
#      | TC_86__Positive__Usage_History_BudgetBillingAmount_Format    | 0        |              | notNull |
#      | TC_87__Positive__Usage_History_NotBudgetBillingAccount       | 0        |              | notNull |
#      | TC_88__Positive__Usage_History_TotalBillAmount_Format        | 0        |              | notNull |
#      | TC_89__Positive__Usage_History_BillHistoryTransactionNumber_Format | 0 |              | notNull |
#      | TC_90__Positive__No_Usage_History_Active                           | 0 |              | notNull |
#      | TC_91__Positive__No_Usage_History_Final                            | 0 |              | notNull |
#      | TC_92__Positive__No_Usage_History_Inactive                         | 0 |              | notNull |
#      | TC_93__Positive__Valid_NumberOfMonths_BillHistory_Too_Old          | 0 |              | notNull |
#      | TC_94__Positive__Usage_History_Less_Than_Requested_Months          | 0 |              | notNull |
#      | TC_95__Positive__Usage_History_Equals_Requested_Months             | 0 |              | notNull |
#      | TC_96__Positive__Usage_History_Greater_Than_Requested_Months       | 0 |              | notNull |
