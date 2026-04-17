Feature: Verify GetBillingInfo Api

  @GetBillingInfo @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetBillingInfo Api for "<testCondition>"
    Then verify response code of "GetBillingInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                                    | errorCode | errorMessage                  | success | data |
#      | TC_001__Negative__Missing_Request_ID                                             | 10001     | Missing Request ID            | false   | null |
#      | TC_002__Negative__Invalid_Request_ID_Length                                      | 10002     | Invalid Request ID            | false   | null |
#      | TC_003__Negative__Duplicate_Request_ID                                           | 10003     | Duplicate Request ID          | false   | null |
#      | TC_004__Negative__Missing_customerCode                                           | 10011     | Missing Customer Code         | false   | null |
#      | TC_005__Negative__Invalid_customerCode_Length                                    | 10015     | Invalid Customer Code Format  | false   | null |
#      | TC_006__Negative__Invalid_customerCode_Format                                    | 10015     | Invalid Customer Code Format  | false   | null |
#      | TC_007__Negative__Missing_premisesCode                                           | 10013     | Missing Premises Code         | false   | null |
#      | TC_008__Negative__Invalid_premisesCode_Length                                    | 10005     | Invalid Premises Code Format  | false   | null |
#      | TC_009__Negative__Invalid_premisesCode_Format                                    | 10005     | Invalid Premises Code Format  | false   | null |
#      | TC_010__Negative__Invalid_Account_Number__Invalid_Account__                      | 40015     | Invalid Account Number        | false   | null |


  @GetBillingInfoPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetBillingInfo Api for "<testCondition>"
    Then verify response code of "GetBillingInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                                                           | errorCode | errorMessage | success | data    |
#      | TC_11__Positive__Valid_Account_with_No_Billing_History                                                  | 0         |              | true    | null    |
#      | TC_12__Positive__Valid_Account_with_1_Month_Billing_History                                                  | 0         |              | true    | null    |
#      | TC_13__Positive__Valid_Account_with_More_Than_1_Month_Billing_History                                   | 0         |              | true    | notNull |
#      | TC_14__Positive__Billing_Info_Bill_Date_Format                                                          | 0         |              | true    | notNull |
#      | TC_15__Positive__Billing_Info_Bill_From_Date_Format                                                     | 0         |              | true    | notNull |
#      | TC_16__Positive__Billing_Info_Bill_To_Date_Format                                                       | 0         |              | true    | notNull |
#      | TC_17__Positive__Billing_Info_Past_Due_Amount_Format__Past_Due_Amount_Greater_Than_0__                  | 0         |              | true    | notNull |
#      | TC_18__Positive__Billing_Info_Past_Due_Amount_Format__Past_Due_Amount_Equals_0__                        | 0         |              | true    | notNull |
#      | TC_19__Positive__Billing_Info_Past_Due_Date_Format                                                      | 0         |              | true    | notNull |
#      | TC_20__Positive__Billing_Info_Previous_Bill_Amount_Format__Previous_Bill_Amount_Greater_Than_0__        | 0         |              | true    | notNull |
#      | TC_21__Positive__Billing_Info_Previous_Bill_Amount_Format__Previous_Bill_Amount_Equals_0__              | 0         |              | true    | notNull |
#      | TC_22__Positive__Billing_Info_Payments_Applied_Amount_Format__Payments_Applied_Amount_Greater_Than_0__  | 0         |              | true    | notNull |
#      | TC_23__Positive__Billing_Info_Payments_Applied_Amount_Format__Payments_Applied_Amount_Equals_0__        | 0         |              | true    | notNull |
#      | TC_24__Positive__Balance_Brought_Forward_Greater_Than_0_Format                                          | 0         |              | true    | notNull |
#      | TC_25__Positive__Balance_Brought_Forward_Equals_0_Format                                                | 0         |              | true    | notNull |
#      | TC_26__Positive__Balance_Brought_Forward_Less_Than_0_Format                                             | 0         |              | true    | notNull |
#      | TC_27__Positive__Current_Charges_Format                                                                 | 0         |              | true    | notNull |
#      | TC_28__Positive__Total_Amount_Due_Format                                                                | 0         |              | true    | notNull |
#      | TC_29__Positive__Bill_Due_Date_Format                                                                   | 0         |              | true    | notNull |
#      | TC_30__Positive__Payments_Since_Last_Bill_Greater_Than_0_Format                                         | 0         |              | true    | notNull |
#      | TC_31__Positive__Payments_Since_Last_Bill_Equals_0_Format                                               | 0         |              | true    | notNull |
#      | TC_32__Positive__Current_Balance_Format                                                                 | 0         |              | true    | notNull |
#      | TC_33__Positive__Budget_Billing_Amount_not_Budget_Billing_Account_Format                                | 0         |              | true    | notNull |
#      | TC_34__Positive__Budget_Billing_TotalVariance_not_Budget_Billing_Account_Format                         | 0         |              | true    | notNull |
#      | TC_35__Positive__Budget_Billing_Start_Date_not_Budget_Billing_Account_Format                            | 0         |              | true    | notNull |
#      | TC_36__Positive__Budget_Billing_Amount_Budget_Billing_Account_Format                                    | 0         |              | true    | notNull |
#      | TC_37__Positive__Budget_Billing_TotalVariance_Budget_Billing_Account_Format                             | 0         |              | true    | notNull |
#      | TC_38__Positive__Budget_Billing_Start_Date_Budget_Billing_Account_Format                                | 0         |              | true    | notNull |
#      | TC_39__Positive__Bill_Line_Items_Code_Format                                                            | 0         |              | true    | notNull |
#      | TC_40__Positive__Bill_Line_Items_Description_Format                                                     | 0         |              | true    | notNull |
#      | TC_41__Positive__Bill_Line_Items_Amount_Format                                                          | 0         |              | true    | notNull |
#      | TC_42__Positive__Bill_Line_Items_Category_Format                                                        | 0         |              | true    | notNull |
      | TC_43__Positive__Bill_Line_Items_Reward_Order                                                           | 0         |              | true    | notNull |
      | TC_44__Positive__Bill_Line_Items_Base_Charge_Order                                                      | 0         |              | true    | notNull |
      | TC_45__Positive__Bill_Line_Items_Customer_Service_Charge_Order                                          | 0         |              | true    | notNull |
      | TC_46__Positive__Bill_Line_Items_Natural_Gas_Charge_Order                                               | 0         |              | true    | notNull |
      | TC_47__Positive__Bill_Line_Items_Bill_Guarantee_Order                                                   | 0         |              | true    | notNull |
      | TC_48__Positive__Bill_Line_Items_Interstate_Pipeline_Capacity_Charge_Order                              | 0         |              | true    | notNull |
      | TC_49__Positive__Bill_Line_Items_Promotional_Discount_Charge_Order                                      | 0         |              | true    | notNull |
      | TC_50__Positive__Bill_Line_Items_Miscellaneous_Charge_Order                                             | 0         |              | true    | notNull |
      | TC_51__Positive__Bill_Line_Items_Miscellaneous_Credit_Order                                             | 0         |              | true    | notNull |
      | TC_52__Positive__Bill_Line_Items_Taxes_Order                                                            | 0         |              | true    | notNull |