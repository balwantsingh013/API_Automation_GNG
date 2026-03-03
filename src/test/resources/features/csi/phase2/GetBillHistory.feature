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
#      | TC_11__Negative__Missing_Request_ID             | 10001     | Missing Request ID                           |
#      | TC_12__Negative__Invalid_Request_ID_Length      | 10002     | Invalid Request ID                           |
#      | TC_13__Negative__Duplicate_Request_ID           | 10003     | Duplicate Request ID                         |
#      | TC_14__Negative__Missing_customerCode           | 10011     | Missing Customer Code                        |
#      | TC_15__Negative__Invalid_customerCode_Length    | 10015     | Invalid Customer Code Format                 |
#      | TC_16__Negative__Invalid_customerCode_Format    | 10015     | Invalid Customer Code Format                 |
#      | TC_17__Negative__Missing_premisesCode           | 10013     | Missing Premises Code                        |
#      | TC_18__Negative__Invalid_premisesCode_Length    | 10005     | Invalid Premises Code Format                 |
#      | TC_19__Negative__Invalid_premisesCode_Format    | 10005     | Invalid Premises Code Format                 |
#      | TC_20__Negative__Invalid_Account_Number         | 40015     | Invalid Account Number                       |
#      | TC_21__Negative__Missing_Number_of_Months       | 10393     | Missing Number of Months                     |
      | TC_22__Negative__Invalid_Number_of_Months_Format_NotANumber | 10000 | The JSON value could not be converted to System.Int32. Path: $.numberOfMonths [PIPE] LineNumber: 0 [PIPE] BytePositionInLine: |
#      | TC_23__Negative__Invalid_Number_of_Months_Zero  | 10395     | Invalid Number of Months                     |
#      | TC_24__Negative__Invalid_Number_of_Months_Negative | 10395  | Invalid Number of Months                     |
#      | TC_25__Negative__Invalid_Number_of_Months_Length | 10405    | Number of Months exceeds maximum allowed                     |
#      | TC_26__Negative__New_Account_Not_Allowed        | 40271     | Operation Not Allowed for New Account        |

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
#      | TC_27__Positive__Usage_History_BillDate_Format           | 0         |              | notNull |
#      | TC_28__Positive__Usage_History_UsageFromDate_Format      | 0         |              | notNull |
#      | TC_29__Positive__Usage_History_UsageToDate_Format        | 0         |              | notNull |
#      | TC_30__Positive__Usage_History_DaysOfService_Format      | 0         |              | notNull |
#      | TC_31__Positive__Usage_History_HeatingDegreeDays_Format     | 0         |              | notNull |
#      | TC_32__Positive__Usage_History_TotalBilledConsumption_Format | 0        |              | notNull |
#      | TC_33__Positive__Usage_History_BalanceBroughtForward_Format  | 0        |              | notNull |
#      | TC_34__Positive__Usage_History_GasServiceCharges_Format      | 0        |              | notNull |
#      | TC_35__Positive__Usage_History_OtherCharges_Format           | 0        |              | notNull |
#      | TC_36__Positive__Usage_History_PromotionalDiscounts_Format   | 0        |              | notNull |
#      | TC_37__Positive__Usage_History_Taxes_Format                  | 0        |              | notNull |
#      | TC_38__Positive__Usage_History_BudgetBillingAmount_Format    | 0        |              | notNull |
#      | TC_39__Positive__Usage_History_NotBudgetBillingAccount       | 0        |              | notNull |
#      | TC_40__Positive__Usage_History_TotalBillAmount_Format        | 0        |              | notNull |
#      | TC_41__Positive__Usage_History_BillHistoryTransactionNumber_Format | 0 |              | notNull |
#      | TC_42__Positive__No_Usage_History_Active                           | 0 |              | notNull |
#      | TC_43__Positive__No_Usage_History_Final                            | 0 |              | notNull |
#      | TC_44__Positive__No_Usage_History_Inactive                         | 0 |              | notNull |
#      | TC_45__Positive__Valid_NumberOfMonths_BillHistory_Too_Old          | 0 |              | notNull |
#      | TC_46__Positive__Usage_History_Less_Than_Requested_Months          | 0 |              | notNull |
#      | TC_47__Positive__Usage_History_Equals_Requested_Months             | 0 |              | notNull |
#      | TC_48__Positive__Usage_History_Greater_Than_Requested_Months       | 0 |              | notNull |
