Feature: Verify AuthenticateCustomer Api

  @AuthenticateCustomer @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to AuthenticateCustomer Api for "<testCondition>"
    Then verify response code of "AuthenticateCustomer" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                        | errorCode | errorMessage                                                                 |
      | TC_129__Negative__Missing_Request_ID                                 | 10001     | Missing Request ID                                                           |
      | TC_130__Negative__Invalid_Request_ID__Length                         | 10002     | Invalid Request ID                                                           |
      | TC_131__Negative__Duplicate_Request_ID                               | 10003     | Duplicate Request ID                                                         |
      | TC_132__Negative__Invalid_customerCode_Length                        | 10015     | Invalid Customer Code Format                                                 |
      | TC_133__Negative__Invalid_customerCode_Format                        | 10015     | Invalid Customer Code Format                                                 |
      | TC_134__Negative__Invalid_premisesCode_Length                        | 10005     | Invalid Premises Code Format                                                 |
      | TC_135__Negative__Invalid_premisesCode_Format                        | 10005     | Invalid Premises Code Format                                                 |
      | TC_136__Negative__Invalid_Last_or_Business_Name_Length               | 10119     | Invalid Last or Business Name Format                                         |
      | TC_137__Negative__Invalid_First_Name_Length                          | 10121     | Invalid First Name Format                                                    |
      | TC_138__Negative__Invalid_Last_Four_SSN_Length___Too_Long____        | 10065     | Invalid Last Four SSN Format                                                 |
      | TC_139__Negative__Invalid_Last_Four_SSN_Length___Too_Short____       | 10065     | Invalid Last Four SSN Format                                                 |
      | TC_140__Negative__Invalid_Federal_Tax_ID_Length___Too_Long____       | 10125     | Invalid Federal Tax ID Format                                                |
      | TC_141__Negative__Invalid_Federal_Tax_ID_Length___Too_Short____      | 10125     | Invalid Federal Tax ID Format                                                |
      | TC_142__Negative__Invalid_Email_Address_Format                       | 10067     | Invalid Email Address Format                                                 |
      | TC_143__Negative__Invalid_Phone_Number_Length___Too_Long____         | 10009     | Invalid Phone Number Format                                                  |
      | TC_144__Negative__Invalid_Phone_Number_Length___Too_Short____        | 10009     | Invalid Phone Number Format                                                  |
      | TC_145__Negative__Invalid_Username_Format                            | 10353     | Invalid Username Format                                                      |
      | TC_146__Negative__Inactive_Username                                  | 10355     | Inactive Username                                                            |
      | TC_147__Negative__Invalid_Password_Format_Length___Too_Short____     | 10157     | Invalid Password Length. The password must be between 8 and 64 characters.   |
      | TC_148__Negative__Invalid_Password_Format_Length___Too_Long____      | 10157     | Invalid Password Length. The password must be between 8 and 64 characters.   |
      | TC_149__Negative__Insufficient_Search_Criteria                       | 10377     | Insufficient Search Criteria                                                 |
      | TC_150__Negative__Last_4_SSN_____Password                            | 10377     | Insufficient Search Criteria                                                 |
      | TC_151__Negative__Last_4_SSN_____Username                            | 10377     | Insufficient Search Criteria                                                 |
      | TC_152__Negative__Last_4_SSN_____Customer_Code_____Premises_Code     | 10377     | Insufficient Search Criteria                                                 |
      | TC_153__Negative__Last_4_SSN_____Federal_Tax_ID_Number               | 10377     | Insufficient Search Criteria                                                 |
      | TC_154__Negative__Password_____Federal_Tax_ID_Number                 | 10377     | Insufficient Search Criteria                                                 |
      | TC_155__Negative__Customer_Last_Name_____Email_Address               | 10377     | Insufficient Search Criteria                                                 |
      | TC_156__Negative__Customer_Last_Name_____Phone_Number                | 10377     | Insufficient Search Criteria                                                 |
      | TC_157__Negative__Customer_Last_Name_____Username                    | 10377     | Insufficient Search Criteria                                                 |
      | TC_158__Negative__Customer_Last_Name_____Customer_Code_____Premises_Code | 10377 | Insufficient Search Criteria                                                 |
      | TC_159__Negative__Business_Name_____Email_Address                    | 10377     | Insufficient Search Criteria                                                 |
      | TC_160__Negative__Business_Name_____Phone_Number                     | 10377     | Insufficient Search Criteria                                                 |
      | TC_161__Negative__Business_Name_____Username                         | 10377     | Insufficient Search Criteria                                                 |
      | TC_162__Negative__Business_Name_____Customer_Code_____Premises_Code  | 10377     | Insufficient Search Criteria                                                 |
      | TC_163__Negative__Email_Address_____Phone_Number                     | 10377     | Insufficient Search Criteria                                                 |
      | TC_164__Negative__Email_Address_____Username                         | 10377     | Insufficient Search Criteria                                                 |
      | TC_165__Negative__Email_Address_____Customer_Code_____Premises_Code  | 10377     | Insufficient Search Criteria                                                 |
      | TC_166__Negative__Email_Address_____Federal_Tax_ID_Number            | 10377     | Insufficient Search Criteria                                                 |
      | TC_167__Negative__Phone_Number_____Username                          | 10377     | Insufficient Search Criteria                                                 |
      | TC_168__Negative__Phone_Number_____Customer_Code_____Premises_Code   | 10377     | Insufficient Search Criteria                                                 |
      | TC_169__Negative__Phone_Number_____Federal_Tax_ID_Number             | 10377     | Insufficient Search Criteria                                                 |
      | TC_170__Negative__Username_____Customer_Code_____Premises_Code       | 10377     | Insufficient Search Criteria                                                 |
      | TC_171__Negative__Username_____Federal_Tax_ID_Number                 | 10377     | Insufficient Search Criteria                                                 |
      | TC_172__Negative__Customer_Code_____Premises_Code_____Federal_Tax_ID_Number | 10377 | Insufficient Search Criteria                                                 |
      | TC_173__Negative__Too_Many_Matches                                  | 10391     | Too Many Matches                                                             |

  @AuthenticateCustomerPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to AuthenticateCustomer Api for "<testCondition>"
    Then verify response code of "AuthenticateCustomer" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have success as "<success>"

    Examples:
      | testCondition                                                        | errorCode | errorMessage | success |
      | TC_174__Positive__Last_4_SSN_____Customer_Last_Name                  | 0         |              | true    |
      | TC_175__Positive__Last_4_SSN_____Email_Address                       | 0         |              | true    |
      | TC_176__Positive__Last_4_SSN_____Phone_Number                        | 0         |              | true    |
      | TC_177__Positive__Password_____Username                              | 0         |              | true    |
      | TC_178__Positive__Password_____Customer_Last_Name                    | 0         |              | true    |
      | TC_179__Positive__Password_____Business_Name                         | 0         |              | true    |
      | TC_180__Positive__Password_____Customer_Code_____Premises_Code       | 0         |              | true    |
      | TC_181__Positive__Password_____Email_Address                         | 0         |              | true    |
      | TC_182__Positive__Password_____Phone_Number                          | 0         |              | true    |
      | TC_183__Positive__Business_Name_____Federal_Tax_ID                   | 0         |              | true    |
      | TC_184__Positive__Login_ID_Saved                                     | 0         |              | true    |
