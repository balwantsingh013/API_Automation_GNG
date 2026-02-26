Feature: Verify GetUsageHistory Api

  @GetUsageHistory @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetUsageHistory Api for "<testCondition>"
    Then verify response code of "GetUsageHistory" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                | errorCode | errorMessage                        |
#      | TC_01__Negative__Missing_Request_ID                           | 10001     | Missing Request ID                  |
#      | TC_02__Negative__Invalid_Request_ID__Length                   | 10002     | Invalid Request ID                  |
#      | TC_03__Negative__Duplicate_Request_ID                         | 10003     | Duplicate Request ID                |
#      | TC_04__Negative__Missing_customerCode                         | 10011     | Missing Customer Code               |
#      | TC_05__Negative__Invalid_customerCode__Length                 | 10015     | Invalid Customer Code Format        |
#      | TC_06__Negative__Invalid_customerCode__Format                 | 10015     | Invalid Customer Code Format        |
#      | TC_07__Negative__Missing_premisesCode                         | 10013     | Missing Premises Code               |
#      | TC_08__Negative__Invalid_premisesCode__Length                 | 10005     | Invalid Premises Code Format        |
#      | TC_09__Negative__Invalid_premisesCode__Format                 | 10005     | Invalid Premises Code Format        |
#      | TC_10__Negative__Invalid_Account_Number                      | 40015     | Invalid Account Number              |
#      | TC_11__Negative__Missing_Number_of_Months                    | 10393     | Missing Number of Months            |
#      | TC_12__Negative__Invalid_Number_of_Months__Format__Not_a_Number | 10000 | The JSON value could not be converted to System.Int32. Path: $.numberOfMonths [PIPE] LineNumber: 0 [PIPE] BytePositionInLine: |
#      | TC_13__Negative__Invalid_Number_of_Months__Format__Zero_Value   | 10395 | Invalid Number of Months           |
#      | TC_14__Negative__Invalid_Number_of_Months__Format__Negative_Number | 10395 | Invalid Number of Months        |
#      | TC_15__Negative__Invalid_Number_of_Months__Length            | 10395     | Invalid Number of Months            |

  @GetUsageHistoryPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetUsageHistory Api for "<testCondition>"
    Then verify response code of "GetUsageHistory" Api is 200
#    And the response should have success as true
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have data as "<data>"
    And usage history response should match database for customer and premises code


    Examples:
      | testCondition                                                   | errorCode | errorMessage | data    |
#      | TC_16__Positive__Usage_History_Service_Number_Format            | 0         |              | notNull |
#      | TC_17__Positive__Usage_History_Bill_Date_Format                | 0         |              | notNull |
#      | TC_18__Positive__Usage_History_Usage_From_Date_Format          | 0         |              | notNull |
#      | TC_19__Positive__Usage_History_Usage_To_Date_Format            | 0         |              | notNull |
      | TC_20__Positive__Usage_History_Average_Daily_Actual_Consumption_Format | 0 |          | notNull |
#      | TC_21__Positive__Usage_History_Average_Daily_Billed_Consumption_Format | 0 |          | notNull |
#      | TC_22__Positive__Usage_History_Total_Billed_Consumption_Format | 0         |              | notNull |
#      | TC_23__Positive__Usage_History_Days_Of_Service_Format          | 0         |              | notNull |
#      | TC_24__Positive__Usage_History_Reading_Format                  | 0         |              | notNull |
#      | TC_25__Positive__Usage_History_Read_Type_Format__Actual        | 0         |              | notNull |
#      | TC_26__Positive__Usage_History_Read_Type_Format__Zero_Consumption | 0      |              | notNull |
#      | TC_27__Positive__Usage_History_Read_Type_Format__Estimated     | 0         |              | notNull |
#      | TC_28__Positive__Usage_History_Read_Date_Format                | 0         |              | notNull |
#      | TC_29__Positive__Usage_History_Average_Temperature_Format      | 0         |              | notNull |
#      | TC_30__Positive__Usage_History_Heating_Degree_Days_Format      | 0         |              | notNull |
#      | TC_31__Positive__Usage_History_Bill_History_Transaction_Format | 0         |              | notNull |
##      | TC_32__Positive__No_Usage_History_New                          |0          |              |notNull  |
#      | TC_33__Positive__No_Usage_History_Active                       | 0         |              | notNull |
#      | TC_34__Positive__No_Usage_History_Final                        | 0         |              | notNull |
#      | TC_35__Positive__No_Usage_History_Inactive                     | 0         |              | notNull |
#      | TC_36__Positive__Usage_History_Too_Old_Valid_Number_of_Months  | 0         |              | notNull |
#      | TC_37__Positive__Usage_History_Less_Than_Requested_Months      | 0         |              | notNull |
#      | TC_38__Positive__Usage_History_Equals_Requested_Months         | 0         |              | notNull |
#      | TC_39__Positive__Usage_History_Greater_Than_Requested_Months   | 0         |              | notNull |
