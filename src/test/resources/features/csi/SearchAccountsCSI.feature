Feature: Verify SearchAccounts Api

#
#
#
#  SELECT * FROM ucbprem
#  WHERE ucbprem_code='8462'
  @SearchAccounts @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                     | errorCode | errorMessage                                                   |
      | TC_189__Negative__Missing_Request_ID                              | 10001     | Missing Request ID                                             |
      | TC_190__Negative__Invalid_Request_ID_Length                       | 10002     | Invalid Request ID                                             |
      | TC_191__Negative__Duplicate_Request_ID                            | 10003     | Duplicate Request ID                                           |
      | TC_192__Negative__Invalid_customerCode_Length                     | 10015     | Invalid Customer Code Format                                   |
      | TC_193__Negative__Invalid_customerCode_Format__Not_String         | 10015     | Invalid Customer Code Format                                   |
      | TC_194__Negative__Invalid_premisesCode_Length                     | 10005     | Invalid Premises Code Format                                   |
      | TC_195__Negative__Invalid_premisesCode_Format__Not_String         | 10005     | Invalid Premises Code Format                                   |
      | TC_196__Negative__Invalid_Last_or_Business_Name_Length            | 10119     | Invalid Last or Business Name Format                           |
      | TC_197__Negative__Invalid_First_Name_Length                       | 10121     | Invalid First Name Format                                      |
      | TC_198__Negative__Invalid_Last_Four_SSN_Length_Too_Long           | 10063     | Invalid Last Four SSN Format                                   |
      | TC_199__Negative__Invalid_Last_Four_SSN_Length_Too_Short          | 10063     | Invalid Last Four SSN Format                                   |
      | TC_200__Negative__Invalid_Federal_Tax_ID_Length_Too_Long          | 10069     | Invalid Federal Tax ID Format                                  |
      | TC_201__Negative__Invalid_Federal_Tax_ID_Length_Too_Short         | 10069     | Invalid Federal Tax ID Format                                  |
      | TC_202__Negative__Invalid_Email_Address_Format                    | 10065     | Invalid Email Address Format                                   |
      | TC_203__Negative__Invalid_Phone_Number_Length_Too_Long            | 10009     | Invalid Phone Number Format                                    |
      | TC_204__Negative__Invalid_Phone_Number_Length_Too_Short           | 10009     | Invalid Phone Number Format                                    |
      | TC_205__Negative__Invalid_Username_Format                         | 10353     | Invalid Username Format                                        |
      | TC_206__Negative__Inactive_Username                               | 10355     | Inactive Username                                              |
      | TC_207__Negative__Invalid_Password_Format_Length_Too_Short        | 10157     | Invalid Password Length. The password must be between 8 and 64 characters. |
      | TC_208__Negative__Invalid_Password_Format_Length_Too_Long         | 10157     | Invalid Password Length. The password must be between 8 and 64 characters. |
      | TC_209__Negative__Last4SSN_Password                                | 10377     | Insufficient Search Criteria                                   |
      | TC_210__Negative__Last4SSN_Username                                | 10377     | Insufficient Search Criteria                                   |
      | TC_211__Negative__Last4SSN_CustomerCode_PremisesCode              | 10377     | Insufficient Search Criteria                                   |
      | TC_212__Negative__Last4SSN_FederalTaxID                           | 10377     | Insufficient Search Criteria                                   |
      | TC_213__Negative__Password_FederalTaxID                           | 10377     | Insufficient Search Criteria                                   |
      | TC_214__Negative__CustomerLastName_EmailAddress                   | 10377     | Insufficient Search Criteria                                   |
      | TC_215__Negative__CustomerLastName_PhoneNumber                    | 10377     | Insufficient Search Criteria                                   |
      | TC_216__Negative__CustomerLastName_Username                       | 10377     | Insufficient Search Criteria                                   |
      | TC_217__Negative__CustomerLastName_CustomerCode_PremisesCode      | 10377     | Insufficient Search Criteria                                   |
      | TC_218__Negative__BusinessName_EmailAddress                       | 10377     | Insufficient Search Criteria                                   |
      | TC_219__Negative__BusinessName_PhoneNumber                        | 10377     | Insufficient Search Criteria                                   |
      | TC_220__Negative__BusinessName_Username                           | 10377     | Insufficient Search Criteria                                   |
      | TC_221__Negative__BusinessName_CustomerCode_PremisesCode          | 10377     | Insufficient Search Criteria                                   |
      | TC_222__Negative__EmailAddress_PhoneNumber                        | 10377     | Insufficient Search Criteria                                   |
      | TC_223__Negative__EmailAddress_Username                           | 10377     | Insufficient Search Criteria                                   |
      | TC_224__Negative__EmailAddress_CustomerCode_PremisesCode          | 10377     | Insufficient Search Criteria                                   |
      | TC_225__Negative__EmailAddress_FederalTaxID                       | 10377     | Insufficient Search Criteria                                   |
      | TC_226__Negative__PhoneNumber_Username                            | 10377     | Insufficient Search Criteria                                   |
      | TC_227__Negative__PhoneNumber_CustomerCode_PremisesCode           | 10377     | Insufficient Search Criteria                                   |
      | TC_228__Negative__PhoneNumber_FederalTaxID                        | 10377     | Insufficient Search Criteria                                   |
      | TC_229__Negative__Username_CustomerCode_PremisesCode              | 10377     | Insufficient Search Criteria                                   |
      | TC_230__Negative__Username_FederalTaxID                           | 10377     | Insufficient Search Criteria                                   |
      | TC_231__Negative__CustomerCode_PremisesCode_FederalTaxID          | 10377     | Insufficient Search Criteria                                   |
      | TC_232__Negative__Too_Many_Matches                                | 10391     | Too Many Matches                                               |
      | TC_233__Positive__Last4SSN_CustomerLastName                       | 0         |                                                                |
      | TC_234__Positive__Last4SSN_EmailAddress                           | 0         |                                                                |
      | TC_235__Positive__Last4SSN_PhoneNumber                            | 0         |                                                                |
      | TC_236__Positive__Password_Username                               | 0         |                                                                |
      | TC_237__Positive__Password_CustomerLastNameBusiness               | 0         |                                                                |
      | TC_238__Positive__Password_CustomerCode_PremisesCode              | 0         |                                                                |
      | TC_239__Positive__Password_EmailAddress                           | 0         |                                                                |
      | TC_240__Positive__Password_PhoneNumber                            | 0         |                                                                |
      | TC_241__Positive__BusinessName_FederalTaxID                       | 0         |                                                                |
      | TC_242__Positive__AccountStatus_Active                            | 0         |                                                                |
      | TC_243__Positive__AccountStatus_Final                             | 0         |                                                                |
      | TC_244__Positive__AccountStatus_Inactive                          | 0         |                                                                |
      | TC_245__Positive__AccountStatus_New                               | 0         |                                                                |
      | TC_246__Positive__Username                                        | 0         |                                                                |
      | TC_247__Positive__No_Username                                     | 0         |                                                                |
      | TC_248__Positive__Nickname                                        | 0         |                                                                |
      | TC_249__Positive__No_Nickname                                     | 0         |                                                                |
      | TC_250__Positive__FirstName                                       | 0         |                                                                |
      | TC_251__Positive__No_FirstName                                    | 0         |                                                                |
      | TC_252__Positive__Residential                                     | 0         |                                                                |
      | TC_253__Positive__Commercial                                      | 0         |                                                                |
      | TC_254__Positive__Industrial                                      | 0         |                                                                |
      | TC_255__Positive__Agriculture                                     | 0         |                                                                |
      | TC_256__Positive__MultiFamily                                     | 0         |                                                                |
      | TC_257__Positive__Seasonal                                       | 0         |                                                                |
      | TC_258__Positive__SeniorCitizen                                  | 0         |                                                                |
      | TC_259__Positive__PremisesAddress_StreetNumber                   | 0         |                                                                |
      | TC_260__Positive__PremisesAddress_No_StreetNumber                | 0         |                                                                |
      | TC_261__Positive__PremisesAddress_StreetPreDirection             | 0         |                                                                |
      | TC_262__Positive__PremisesAddress_No_StreetPreDirection          | 0         |                                                                |
      | TC_263__Positive__PremisesAddress_StreetName                     | 0         |                                                                |
      | TC_264__Positive__PremisesAddress_No_StreetName                  | 0         |                                                                |
      | TC_265__Positive__PremisesAddress_StreetSuffix                   | 0         |                                                                |
      | TC_266__Positive__PremisesAddress_No_StreetSuffix                | 0         |                                                                |
      | TC_267__Positive__PremisesAddress_StreetPostDirection            | 0         |                                                                |
      | TC_268__Positive__PremisesAddress_No_StreetPostDirection          | 0         |                                                                |
      | TC_269__Positive__PremisesAddress_UnitType                        | 0         |                                                                |
      | TC_270__Positive__PremisesAddress_No_UnitType                     | 0         |                                                                |
      | TC_271__Positive__PremisesAddress_UnitNumber                      | 0         |                                                                |
      | TC_272__Positive__PremisesAddress_No_UnitNumber                   | 0         |                                                                |
      | TC_273__Positive__PremisesAddress_City                            | 0         |                                                                |
      | TC_274__Positive__PremisesAddress_No_City                         | 0         |                                                                |
      | TC_275__Positive__PremisesAddress_State                           | 0         |                                                                |
      | TC_276__Positive__PremisesAddress_No_State                        | 0         |                                                                |
      | TC_277__Positive__PremisesAddress_ZipCode                         | 0         |                                                                |
      | TC_278__Positive__PremisesAddress_No_ZipCode                      | 0         |                                                                |
