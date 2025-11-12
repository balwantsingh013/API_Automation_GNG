Feature: Verify End to End flow for ServiceTransfer

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @ServiceTransferE2E @HappyFlow @Phase2
  Scenario Outline: Verify end to end flow for ServiceTransfer for "<testCondition>"
  When customer code, premises code and transaction id is fetched from db for "<testCondition>"
    Then request is made to the SearchAccountsApi for "<testCondition>"
    And a request is made to get Marketer Reference Data
    And a request is made to the SaveUnenrollment Api for account with "<pricePlan>" plan "<accountType>" type with forwardingAddressIs "<forwardingAddressIs>" with type "<addressType>" and turnoffreason "<testCondition>" and setEmail "<setEmail>" with etcExists "<etcExists>"


    Examples:
    |testCondition|
    |ACN_RS_REMAINS_ON_TIER_1|
