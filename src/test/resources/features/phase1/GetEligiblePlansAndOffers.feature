Feature: Verify GetEligiblePlansAndOffers Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetEligiblePlansAndOffersWithValidData @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with valid data
    When a request is made to the GetEligiblePlansAndOffers Api
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>