Feature: Verify SearchAccounts Api


  @SearchAccounts987 @NegativeFlow @CSI @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                     | errorCode | errorMessage                                                   |
      | TC_191__Negative__Missing_Request_ID                              | 10001     | Missing Request ID                                             |
      | TC_192__Negative__Invalid_Request_ID_Length                       | 10002     | Invalid Request ID                                             |
      | TC_193__Negative__Duplicate_Request_ID                            | 10003     | Duplicate Request ID                                           |
      | TC_194__Negative__Invalid_customerCode_Length                     | 10015     | Invalid Customer Code Format                                   |
      | TC_195__Negative__Invalid_customerCode_Format         | 10015     | Invalid Customer Code Format                                   |
      | TC_196__Negative__Invalid_premisesCode_Length                     | 10005     | Invalid Premises Code Format                                   |
      | TC_197__Negative__Invalid_premisesCode_Format         | 10005     | Invalid Premises Code Format                                   |
      | TC_198__Negative__Invalid_Last_or_Business_Name_Length            | 10119     | Invalid Last or Business Name Format                           |
      | TC_199__Negative__Invalid_First_Name_Length                       | 10121     | Invalid First Name Format                                      |
      | TC_200__Negative__Invalid_Last_Four_SSN_Length_Too_Long           | 10063     | Invalid Last Four SSN Format                                   |
      | TC_201__Negative__Invalid_Last_Four_SSN_Length_Too_Short          | 10063     | Invalid Last Four SSN Format                                   |
      | TC_202__Negative__Invalid_Federal_Tax_ID_Length_Too_Long          | 10069     | Invalid Federal Tax ID Format                                  |
      | TC_203__Negative__Invalid_Federal_Tax_ID_Length_Too_Short         | 10069     | Invalid Federal Tax ID Format                                  |
      | TC_204__Negative__Invalid_Email_Address_Format                    | 10065     | Invalid Email Address Format                                   |
      | TC_205__Negative__Invalid_Phone_Number_Length_Too_Long            | 10009     | Invalid Phone Number Format                                    |
      | TC_206__Negative__Invalid_Phone_Number_Length_Too_Short           | 10009     | Invalid Phone Number Format                                    |
      | TC_207__Negative__Invalid_Username_Format                         | 10353     | Invalid Username Format                                        |
      | TC_208__Negative__Inactive_Username                               | 10355     | Inactive Username                                              |
      | TC_209__Negative__Invalid_Password_Format_Length_Too_Short        | 10157     | Invalid Password Length. The password must be between 8 and 64 characters. |
      | TC_210__Negative__Invalid_Password_Format_Length_Too_Long         | 10157     | Invalid Password Length. The password must be between 8 and 64 characters. |
      | TC_211__Negative__Last4SSN_____Password                                | 10377     | Insufficient Search Criteria                                   |
      | TC_212__Negative__Last4SSN_____Username                                | 10377     | Insufficient Search Criteria                                   |
      | TC_213__Negative__Last4SSN_____CustomerCode_____PremisesCode              | 10377     | Insufficient Search Criteria                                   |
      | TC_214__Negative__Last4SSN_____FederalTaxID_Number                           | 10377     | Insufficient Search Criteria                                   |
      | TC_215__Negative__Password_____FederalTaxID_Number                           | 10377     | Insufficient Search Criteria                                   |
      | TC_216__Negative__CustomerLastName_____EmailAddress                   | 10377     | Insufficient Search Criteria                                   |
      | TC_217__Negative__CustomerLastName_____PhoneNumber                    | 10377     | Insufficient Search Criteria                                   |
      | TC_218__Negative__CustomerLastName_____Username                       | 10377     | Insufficient Search Criteria                                   |
      | TC_219__Negative__CustomerLastName_____CustomerCode_____PremisesCode      | 10377     | Insufficient Search Criteria                                   |
      | TC_220__Negative__EmailAddress_____PhoneNumber                        | 10377     | Insufficient Search Criteria                                   |
      | TC_221__Negative__EmailAddress_____Username                           | 10377     | Insufficient Search Criteria                                   |
      | TC_222__Negative__EmailAddress_____CustomerCode_____PremisesCode          | 10377     | Insufficient Search Criteria                                   |
      | TC_223__Negative__EmailAddress_____FederalTaxID_Number                       | 10377     | Insufficient Search Criteria                                   |
      | TC_224__Negative__PhoneNumber_____Username                            | 10377     | Insufficient Search Criteria                                   |
      | TC_225__Negative__PhoneNumber_____CustomerCode_____PremisesCode           | 10377     | Insufficient Search Criteria                                   |
      | TC_226__Negative__PhoneNumber_____FederalTaxID_Number                        | 10377     | Insufficient Search Criteria                                   |
      | TC_227__Negative__Username_____CustomerCode_____PremisesCode              | 10377     | Insufficient Search Criteria                                   |
      | TC_228__Negative__Username_____FederalTaxID_Number                           | 10377     | Insufficient Search Criteria                                   |
      | TC_229__Negative__CustomerCode_____PremisesCode_____FederalTaxID_Number          | 10377     | Insufficient Search Criteria                                   |
#      | TC_230__Negative__Too_Many_Matches                                | 10391     | Too Many Matches                                               |
      | TC_231__Positive__Last4SSN_____CustomerLastName                       | 0         |                                                                |
      | TC_232__Positive__Last4SSN_____EmailAddress                           | 0         |                                                                |
      | TC_233__Positive__Last4SSN_____PhoneNumber                            | 0         |                                                                |
      | TC_234__Positive__Password_____Username                               | 0         |                                                                |
      | TC_235__Positive__Password_____CustomerLastNameBusiness               | 0         |                                                                |
      | TC_236__Positive__Password_____CustomerCode_____PremisesCode              | 0         |                                                                |
      | TC_237__Positive__Password_____EmailAddress                           | 0         |                                                                |
      | TC_238__Positive__Password_____PhoneNumber                            | 0         |                                                                |
      | TC_239__Positive__BusinessName_____FederalTaxID                       | 0         |                                                                |

  @SearchAccounts @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|
      |TC_240__Positive__AccountStatus_Active| 0  |                 |TC_47__Positive__Username_Available___Banner_Active____|

  @SearchAccounts @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|
      |TC_241__Positive__AccountStatus_Final| 0  |                 |TC_49__Positive__Username_Available___Banner_Final____|

  @SearchAccounts321 @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|
      |TC_242__Positive__AccountStatus_Inactive| 0  |                 |TC_50__Positive__Username_Available___Banner_Inactive____|

  @SearchAccounts @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|
      |TC_243__Positive__AccountStatus_New| 0  |                 |TC_48__Positive__Username_Available___Banner_New____|


  @SearchAccounts987 @NegativeFlow @CSI @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                     | errorCode | errorMessage                                                   |
      | TC_244__Positive__Username                                        | 0         |                                                                |
      | TC_245__Positive__No_Username                                     | 0         |                                                                |
      | TC_246__Positive__Nickname                                        | 0         |                                                                |
      | TC_247__Positive__No_Nickname                                     | 0         |                                                                |
      | TC_248__Positive__FirstName                                       | 0         |                                                                |
      | TC_249__Positive__No_FirstName                                    | 0         |                                                                |
      | TC_250__Positive__Residential                                     | 0         |                                                                |
      | TC_251__Positive__Commercial                                      | 0         |                                                                |
      | TC_252__Positive__Industrial                                      | 0         |                                                                |
      | TC_253__Positive__Agriculture                                     | 0         |                                                                |
      | TC_254__Positive__MultiFamily                                     | 0         |                                                                |
      | TC_255__Positive__Seasonal                                       | 0         |                                                                |
      | TC_256__Positive__SeniorCitizen                                  | 0         |                                                                |
      | TC_257__Positive__PremisesAddress_StreetNumber                   | 0         |                                                                |
      | TC_258__Positive__PremisesAddress_No_StreetNumber                | 0         |                                                                |
      | TC_259__Positive__PremisesAddress_StreetPreDirection             | 0         |                                                                |
      | TC_260__Positive__PremisesAddress_No_StreetPreDirection          | 0         |                                                                |
      | TC_261__Positive__PremisesAddress_StreetName                     | 0         |                                                                |
      | TC_262__Positive__PremisesAddress_StreetSuffix                   | 0         |                                                                |
      | TC_263__Positive__PremisesAddress_No_StreetSuffix                | 0         |                                                                |
      | TC_264__Positive__PremisesAddress_StreetPostDirection            | 0         |                                                                |
      | TC_265__Positive__PremisesAddress_No_StreetPostDirection          | 0         |                                                                |
      | TC_266__Positive__PremisesAddress_UnitType                        | 0         |                                                                |
      | TC_267__Positive__PremisesAddress_No_UnitType                     | 0         |                                                                |
      | TC_268__Positive__PremisesAddress_UnitNumber                      | 0         |                                                                |
      | TC_269__Positive__PremisesAddress_No_UnitNumber                   | 0         |                                                                |
      | TC_270__Positive__PremisesAddress_City                            | 0         |                                                                |
      | TC_271__Positive__PremisesAddress_No_City                         | 0         |                                                                |
      | TC_272__Positive__PremisesAddress_State                           | 0         |                                                                |
      | TC_273__Positive__PremisesAddress_No_State                        | 0         |                                                                |
      | TC_274__Positive__PremisesAddress_ZipCode                         | 0         |                                                                |
      | TC_275__Positive__PremisesAddress_No_ZipCode                      | 0         |                                                                |




  @SearchAccountsSorting @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to UpdateUsername Api for "<testCondition2>"
    When a request is made to UpdateUsername Api for "<testCondition3>"
    When a request is made to UpdateUsername Api for "<testCondition4>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And accounts should be sorted by accountStatus in order A, F, N, I
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|testCondition2|testCondition3|testCondition4|
    |TC_276__Positive__Search_Order__Active_______Final_______New_______Inactive| 0  |                 |TC_47__Positive__Username_Available___Banner_Active____|TC_48__Positive__Username_Available___Banner_New____|TC_49__Positive__Username_Available___Banner_Final____|TC_50__Positive__Username_Available___Banner_Inactive____|

  @SearchAccountsSorting2 @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to UpdateUsername Api for "<testCondition2>"
    When a request is made to UpdateUsername Api for "<testCondition3>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And accounts should be sorted by accountStatus in order A, F, N, I
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|testCondition2|testCondition3|
      |TC_277__Positive__Search_Order__Active_______Final_______New| 0  |                 |TC_47__Positive__Username_Available___Banner_Active____|TC_48__Positive__Username_Available___Banner_New____|TC_49__Positive__Username_Available___Banner_Final____|


  @SearchAccountsSorting3 @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to UpdateUsername Api for "<testCondition2>"
    When a request is made to UpdateUsername Api for "<testCondition3>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And accounts should be sorted by accountStatus in order A, F, N, I
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|testCondition2|testCondition3|
      |TC_278__Positive__Search_Order__Active_______Final_______Inactive| 0  |                 |TC_47__Positive__Username_Available___Banner_Active____|TC_49__Positive__Username_Available___Banner_Final____|TC_50__Positive__Username_Available___Banner_Inactive____|

  @SearchAccountsSorting4 @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to UpdateUsername Api for "<testCondition2>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And accounts should be sorted by accountStatus in order A, F, N, I
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|testCondition2|
      |TC_279__Positive__Search_Order__Active_______Final| 0  |                 |TC_47__Positive__Username_Available___Banner_Active____|TC_49__Positive__Username_Available___Banner_Final____|


  @SearchAccountsSorting5 @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to UpdateUsername Api for "<testCondition2>"
    When a request is made to UpdateUsername Api for "<testCondition3>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And accounts should be sorted by accountStatus in order A, F, N, I
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|testCondition2|testCondition3|
      |TC_280__Positive__Search_Order__Active_______New_______Inactive| 0  |                 |TC_47__Positive__Username_Available___Banner_Active____|TC_48__Positive__Username_Available___Banner_New____|TC_50__Positive__Username_Available___Banner_Inactive____|


  @SearchAccountsSorting6 @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to UpdateUsername Api for "<testCondition2>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And accounts should be sorted by accountStatus in order A, F, N, I
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|testCondition2|
      |TC_281__Positive__Search_Order__Active_______New| 0  |                 |TC_47__Positive__Username_Available___Banner_Active____|TC_48__Positive__Username_Available___Banner_New____|


  @SearchAccountsSorting7 @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to UpdateUsername Api for "<testCondition2>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And accounts should be sorted by accountStatus in order A, F, N, I
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|testCondition2|
      |TC_282__Positive__Search_Order__Active_______Inactive| 0  |                 |TC_47__Positive__Username_Available___Banner_Active____|TC_50__Positive__Username_Available___Banner_Inactive____|



  @SearchAccountsSorting8 @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to UpdateUsername Api for "<testCondition2>"
    When a request is made to UpdateUsername Api for "<testCondition3>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And accounts should be sorted by accountStatus in order A, F, N, I
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|testCondition2|testCondition3|
      |TC_283__Positive__Search_Order__Final_______New_______Inactive| 0  |                 |TC_48__Positive__Username_Available___Banner_New____|TC_50__Positive__Username_Available___Banner_Inactive____|TC_49__Positive__Username_Available___Banner_Final____|


  @SearchAccountsSorting9 @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to UpdateUsername Api for "<testCondition2>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And accounts should be sorted by accountStatus in order A, F, N, I
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|testCondition2|
      |TC_284__Positive__Search_Order__Final_______New| 0  |                 |TC_48__Positive__Username_Available___Banner_New____|TC_49__Positive__Username_Available___Banner_Final____|


  @SearchAccountsSorting10 @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to UpdateUsername Api for "<testCondition2>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And accounts should be sorted by accountStatus in order A, F, N, I
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|testCondition2|
      |TC_285__Positive__Search_Order__Final_______Inactive| 0  |                 |TC_50__Positive__Username_Available___Banner_Inactive____|TC_49__Positive__Username_Available___Banner_Final____|


  @SearchAccountsSorting11 @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition1>"
    When a request is made to UpdateUsername Api for "<testCondition2>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And accounts should be sorted by accountStatus in order A, F, N, I
    And performs rollback operation

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |testCondition1|testCondition2|
      |TC_286__Positive__Search_Order__New_______Inactive| 0  |                 |TC_50__Positive__Username_Available___Banner_Inactive____|TC_48__Positive__Username_Available___Banner_New____|


  @SearchAccountsSorting12 @NegativeFlow @CSI  @searchAccountsCSI
  Scenario Outline: "<testCondition>"
    When a request is made to SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And accounts should be alphabetically sorted by customerFirstName when status and last name match

    Examples:
      | testCondition                                                     | errorCode | errorMessage    |
      |TC_287__Positive__Search_Order__Active_Name_order| 0  |                 |
      |TC_288__Positive__Search_Order__Final_Name_order| 0  |                 |
      |TC_289__Positive__Search_Order__New_Name_order| 0  |                 |
      |TC_290__Positive__Search_Order__Inactive_Name_order| 0  |                 |

