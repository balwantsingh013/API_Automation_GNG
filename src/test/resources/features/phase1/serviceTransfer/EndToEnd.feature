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
    |testCondition           |accountType|setEmail     |etcExists     |
    |ACN_RS_REMAINS_ON_TIER_1_TC_251|RS         |false        | null         |
#    |ACN_RS_REMAINS_ON_TIER_1_NACN_TC_252|RS         |false        | null         |
#    |ACN_RS_TC_253                       |RS         |false        | null         |
#      |NACN_RS_TC_254                       |RS         |false        | null         |
#      |ACN_RS_TC_255                      |RS         |false        | null         |
#    |NACN_RS_TC_256                      |RS         |false        | null         |
#    |NACN_SR_TC_258                      |RS         |false        | null         |
