Feature: Verify GetPaymentArrangementInfo Api

  @GetPaymentArrangementInfo @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetPaymentArrangementInfo Api for "<testCondition>"
    Then verify response code of "GetPaymentArrangementInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                      | errorCode | errorMessage                     |
      | TC_156__Negative__Missing_Request_ID               | 10001     | Missing Request ID               |
      | TC_157__Negative__Invalid_Request_ID_Length        | 10002     | Invalid Request ID               |
      | TC_158__Negative__Duplicate_Request_ID             | 10003     | Duplicate Request ID             |
      | TC_159__Negative__Missing_customerCode             | 10011     | Missing Customer Code            |
      | TC_160__Negative__Invalid_customerCode_Length      | 10015     | Invalid Customer Code Format     |
      | TC_161__Negative__Invalid_customerCode_Format      | 10015     | Invalid Customer Code Format     |
      | TC_162__Negative__Missing_premisesCode             | 10013     | Missing Premises Code            |
      | TC_163__Negative__Invalid_premisesCode_Length      | 10005     | Invalid Premises Code Format     |
      | TC_164__Negative__Invalid_premisesCode_Format      | 10005     | Invalid Premises Code Format     |
      | TC_165__Negative__Invalid_Account_Number           | 40015     | Invalid Account Number           |


  @GetPaymentArrangementInfoPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetPaymentArrangementInfo Api for "<testCondition>"
    Then verify response code of "GetPaymentArrangementInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                     | errorCode | errorMessage |
      | TC_166__Positive__No_Payment_Arrangement                          | 0         |              |
      | TC_167__Positive__Has_Inactive_Payment_Arrangement                | 0         |              |
      | TC_168__Positive__Has_Active_Payment_Arrangement                  | 0         |              |


  @GetPaymentArrangementInfoPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetPaymentArrangementInfo Api for "<testCondition>"
    Then verify response code of "GetPaymentArrangementInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And GetPaymentArrangementInfo response should match database for customer and premises code

    Examples:
      | testCondition                                                     | errorCode | errorMessage |
      | TC_169__Positive__Payment_Arrangement_Number_Format               | 0         |              |
      | TC_170__Positive__Payment_Arrangement_Type_Code_Format            | 0         |              |
      | TC_171__Positive__Payment_Arrangement_Total_Amount_Format         | 0         |              |
      | TC_172__Positive__Payment_Arrangement_Date_Created_Format         | 0         |              |
      | TC_173__Positive__Payment_Arrangement_Number_Of_Installments_Format | 0       |              |
      | TC_174__Positive__Payment_Arrangement_Amount_Due_Format           | 0         |              |
      | TC_175__Positive__Payment_Arrangement_Balance_Format              | 0         |              |
      | TC_176__Positive__Payment_Arrangement_Date_Due_Format             | 0         |              |
      | TC_177__Positive__Payment_Arrangement_Date_Paid_Format            | 0         |              |
      | TC_178__Positive__Payment_Arrangement_No_Of_Installments_Greater_than_1 | 0         |              |
