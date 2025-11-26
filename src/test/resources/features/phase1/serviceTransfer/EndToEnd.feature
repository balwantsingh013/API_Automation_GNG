Feature: Verify End to End flow for ServiceTransfer

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @ServiceTransferE2E @HappyFlow @Phase2
  Scenario Outline: Verify end to end flow for ServiceTransfer for "<testCondition>"
    When customer code, premises code and transaction id is fetched from db for "<testCondition>"
    Then a request is made to the SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    When a request is made to the GetMarketerReferenceData Api with valid parameters
    Then verify response code of "GetMarketerReferenceData" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the SaveUnenrollment Api for account with "<accountType>" type and turnoffreason "<testCondition>" and setEmail "<setEmail>" with etcExists "<etcExists>"
    Then a request is made to the GetEligiblePlansAndOffers Api from SearchAccounts response for external cases for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to get Marketer Reference Data
    Then a request is made to the SaveEnrollment Api for serviceTransfer with valid parameters for "<testCondition>" condition
    Then verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

    Examples:
      |testCondition                         |accountType|setEmail|etcExists|
      |ACN_RS_REMAINS_ON_TIER_1_TC_251       |RS         |false   |null     |
      |ACN_RS_REMAINS_ON_TIER_1_NACN_TC_252  |RS         |false   |null     |
      |ACN_RS_TC_253                         |RS         |false   |null     |
      |NACN_RS_TC_254                        |RS         |false   |null     |
      |ACN_RS_TC_255                         |RS         |false   |null     |
      |NACN_RS_TC_256                        |RS         |false   |null     |
      |NACN_SR_TC_258                        |RS         |false   |null     |
      |ACN_RS_TC_259                         |RS         |false   |null     |
      |NACN_RS_TC_260                        |RS         |false   |null     |
      |NACN_SR_TC_262                        |RS         |false   |null     |
      |ACN_RS_TC_263                         |RS         |false   |null     |
      |NACN_RS_TC_264                        |RS         |false   |null     |
      |ACN_RS_TC_265                         |RS         |false   |null     |
      |NACN_RS_TC_266                        |RS         |false   |null     |
      |NACN_RS_TC_267                        |RS         |false   |null     |
      |ACN_RS_TC_268                         |RS         |false   |null     |
      |ACN_RS_TC_269                         |RS         |false   |null     |
      |NACN_RS_TC_270                        |RS         |false   |null     |
      |ACN_RS_TC_271                         |RS         |false   |null     |
      |NACN_RS_TC_273                        |RS         |false   |null     |
      |ACN_RS_TC_274                         |RS         |false   |null     |
      |ACN_RS_TC_275                         |RS         |false   |null     |
      |NACN_RS_TC_276                        |RS         |false   |null     |
      |NACN_RS_TC_277                        |RS         |false   |null     |
      |ACN_RS_TC_278                         |RS         |false   |null     |
      |NACN_RS_TC_279                        |RS         |false   |null     |
      |NACN_RS_TC_280                        |RS         |false   |null     |
      |ACN_CM_TC_281                         |CM         |false   |null     |
      |NACN_CM_TC_282                        |CM         |false   |null     |
      |NACN_CM_TC_283                        |CM         |false   |null     |
      |ACN_CM_TC_284                         |CM         |false   |null     |
      |NACN_RS_TC_285                        |RS         |false   |null     |
      |NACN_RS_TC_286                        |RS         |false   |null     |
      |NACN_CM_TC_287                        |CM         |false   |null     |
      |NACN_CM_TC_288                        |CM         |false   |null     |
      |NACN_RS_TC_289                        |RS         |false   |null     |
      |NACN_RS_TC_290                        |RS         |false   |null     |
      |NACN_RS_TC_291                        |RS         |false   |null     |
      |NACN_RS_TC_292                        |RS         |false   |null     |
      |NACN_RS_TC_293                        |RS         |false   |null     |
      |NACN_RS_TC_294                        |RS         |false   |null     |
      |NACN_RS_TC_295                        |RS         |false   |null     |
      |NACN_RS_TC_296                        |RS         |false   |null     |
      |NACN_RS_TC_297                        |RS         |false   |null     |
      |ACN_RS_TC_298                         |RS         |false   |null     |

  @ServiceTransferE2E2 @HappyFlow @Phase2
  Scenario Outline: Verify end to end flow for ServiceTransfer for "<testCondition>"
    When customer code, premises code and transaction id is fetched from db for "<testCondition>"
    Then a request is made to the SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    When a request is made to the GetMarketerReferenceData Api with valid parameters
    Then verify response code of "GetMarketerReferenceData" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the SaveUnenrollment Api for account with "<accountType>" type and turnoffreason "<testCondition>" and setEmail "<setEmail>" with etcExists "<etcExists>"
    Then a request is made to the GetEligiblePlansAndOffers Api from SearchAccounts response for external cases for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the GetEligiblePlansAndOffers Api from SearchAccounts response for external cases for "<testCondition2>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the GetEligiblePlansAndOffers Api from SearchAccounts response for external cases for "<testCondition3>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the GetEligiblePlansAndOffers Api from SearchAccounts response for external cases for "<testCondition4>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the GetEligiblePlansAndOffers Api from SearchAccounts response for external cases for "<testCondition5>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to get Marketer Reference Data
    Then a request is made to the SaveEnrollment Api for serviceTransfer with valid parameters for "<testCondition>" condition
    Then verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

    Examples:
      |testCondition   |accountType|setEmail|etcExists|testCondition2   |testCondition3   |testCondition4   |testCondition5   |
      |NACN_RS_TC_300  |RS         |false   |null     |NACN_RS_TC_300_2 |NACN_RS_TC_300_3 |NACN_RS_TC_300_4 |NACN_RS_TC_300_5 |
      |NACN_CM_TC_301  |CM         |false   |null     |NACN_CM_TC_301_1 |NACN_CM_TC_301_3 |NACN_CM_TC_301_4 |NACN_CM_TC_301_5 |
