Feature: Verify GetPaymentHistory Api

  @GetPaymentHistory @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetPaymentHistory Api for "<testCondition>"
    Then verify response code of "GetPaymentHistory" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And response should have data as null
    And response should have success as false

    Examples:
      | testCondition                                      | errorCode | errorMessage                               |
      | TC_126__Negative__Missing_Request_ID               | 10001     | Missing Request ID                          |
      | TC_127__Negative__Invalid_Request_ID_Length        | 10002     | Invalid Request ID                          |
      | TC_128__Negative__Duplicate_Request_ID             | 10003     | Duplicate Request ID                        |
      | TC_129__Negative__Missing_customerCode             | 10011     | Missing Customer Code                       |
      | TC_130__Negative__Invalid_customerCode_Length      | 10015     | Invalid Customer Code Format                |
      | TC_131__Negative__Invalid_customerCode_Format      | 10015     | Invalid Customer Code Format                |
      | TC_132__Negative__Missing_premisesCode             | 10013     | Missing Premises Code                       |
      | TC_133__Negative__Invalid_premisesCode_Length      | 10005     | Invalid Premises Code Format                |
      | TC_134__Negative__Invalid_premisesCode_Format      | 10005     | Invalid Premises Code Format                |
      | TC_135__Negative__Invalid_Account_Number           | 40015     | Invalid Account Number                      |
      | TC_136__Negative__Missing_Number_of_Months         | 10393     | Missing Number of Months                    |
      | TC_137__Negative__Invalid_Number_of_Months_Format__Not_a_Number | 10000 | Framework error for data type conversion |
      | TC_138__Negative__Invalid_Number_of_Months_Format__Zero_Value   | 10395 | Invalid Number of Months                  |
      | TC_139__Negative__Invalid_Number_of_Months_Format__Negative_Number | 10395 | Invalid Number of Months              |
      | TC_140__Negative__Invalid_Number_of_Months_Length  | 10405     | Number of Months exceeds maximum allowed    |


  @GetPaymentHistoryPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetPaymentHistory Api for "<testCondition>"
    Then verify response code of "GetPaymentHistory" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And response should have success as true
    And response should have data as notNull

    Examples:
      | testCondition                                                         | errorCode | errorMessage |
      | TC_141__Positive__Payment_Date_Format                                 | 0         |              |
      | TC_142__Positive__Payment_Amount_Format                               | 0         |              |
      | TC_143__Positive__Payment_Code_Format                                 | 0         |              |
      | TC_144__Positive__Payment_Description_Format                          | 0         |              |
      | TC_145__Positive__No_Payment_History_New                              | 0         |              |
      | TC_146__Positive__No_Payment_History_Active_Final_Inactive            | 0         |              |
      | TC_147__Positive__Valid_NumberOfMonths_PaymentHistory_Too_Old         | 0         |              |
      | TC_148__Positive__Valid_NumberOfMonths_Less_Than_Requested            | 0         |              |
      | TC_149__Positive__Valid_NumberOfMonths_Equals_Requested               | 0         |              |
      | TC_150__Positive__Valid_NumberOfMonths_Greater_Than_Requested         | 0         |              |
      | TC_151__Positive__Posted_Reversal_Payment                             | 0         |              |
      | TC_152__Positive__Not_Posted_Reversal_Payment                         | 0         |              |
      | TC_153__Positive__Posted_Payments                                     | 0         |              |
      | TC_154__Positive__Pending_Payments                                    | 0         |              |
      | TC_155__Positive__Posted_and_Pending_Payments                         | 0         |              |
