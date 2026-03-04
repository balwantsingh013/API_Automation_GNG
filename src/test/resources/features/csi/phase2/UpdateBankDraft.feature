Feature: Verify UpdateBankDraft Api

  @UpdateBankDraft @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateBankDraft Api for "<testCondition>"
    Then verify response code of "UpdateBankDraft" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And response should have data as null
#    And response should have success as false

    Examples:
      | testCondition                                        | errorCode | errorMessage                      |
#      | TC_50__Negative__Missing_Request_ID                  | 10001     | Missing Request ID                |
#      | TC_51__Negative__Invalid_Request_ID_Length           | 10002     | Invalid Request ID                |
#      | TC_52__Negative__Duplicate_Request_ID                | 10003     | Duplicate Request ID              |
#      | TC_53__Negative__Missing_customerCode                | 10011     | Missing Customer Code             |
#      | TC_54__Negative__Invalid_customerCode_Length         | 10015     | Invalid Customer Code Format      |
#      | TC_55__Negative__Invalid_customerCode_Format         | 10015     | Invalid Customer Code Format      |
#      | TC_56__Negative__Missing_premisesCode                | 10013     | Missing Premises Code             |
#      | TC_57__Negative__Invalid_premisesCode_Length         | 10005     | Invalid Premises Code Format      |
#      | TC_58__Negative__Invalid_premisesCode_Format         | 10005     | Invalid Premises Code Format      |
#      | TC_59__Negative__Invalid_Account_Number              | 40015     | Invalid Account Number            |
#      | TC_60__Negative__Missing_Bank_Account_Type           | 10379     | Missing Bank Account Type         |
#      | TC_61__Negative__Invalid_Bank_Account_Type_Format    | 10381     | Invalid Bank Account Type Format  |
#      | TC_62__Negative__Missing_Routing_Number              | 10383     | Missing Routing Number            |
#      | TC_63__Negative__Invalid_Routing_Number_Format       | 10385     | Invalid Routing Number Format     |
#      | TC_64__Negative__Invalid_Routing_Number_Length       | 10385     | Invalid Routing Number Format     |
#      | TC_65__Negative__Invalid_Routing_Number              | 40261     | Invalid Routing Number            |
#      | TC_66__Negative__Missing_Bank_Account_Number         | 10387     | Missing Bank Account Number       |
#      | TC_67__Negative__Invalid_Bank_Account_Number_Format  | 10389     | Invalid Bank Account Number Format|
#      | TC_68__Negative__Invalid_Bank_Account_Number_Length  | 10389     | Invalid Bank Account Number Format|
#      | TC_69__Negative__No_Active_ABD_Configured            | 40259     | No active Automatic Bank Draft found |
#      | TC_70__Negative__Invalid_Service_Number              | 40043     | Invalid Service Number            |
#      | TC_71__Negative__Active_Payment_Arrangement          | 40265     | Active Payment Arrangement found  |
#      | TC_72__Negative__Account_Ineligible_Future_Payment   | 40267     | Account ineligible for Bank Draft update |
#      | TC_73__Negative__No_Update_Required                  | 40269     | No update required                |
#
#
  @UpdateBankDraftPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateBankDraft Api for "<testCondition>"
    Then verify response code of "UpdateBankDraft" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And response should have success as true
#    And response should have data as notNull
#    And the response should return the associated bankName for the provided Routing Number
#    And the Banner database should be updated with the provided bank draft information

    Examples:
      | testCondition                                           | errorCode | errorMessage |
#      | TC_74__Positive__Active_Checking_Account_Updated        | 0         |              |
      | TC_75__Positive__Active_Savings_Account_Updated         | 0         |              |
#      | TC_76__Positive__Prenotification_Checking_Updated       | 0         |              |
#      | TC_77__Positive__Prenotification_Savings_Updated        | 0         |              |
#      | TC_78__Positive__Login_ID_Saved                         | 0         |              |
