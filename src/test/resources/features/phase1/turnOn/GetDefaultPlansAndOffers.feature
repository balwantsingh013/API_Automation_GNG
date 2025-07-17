Feature: Verify GetDefaultPlansAndOffers Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetDefaultPlansAndOffersPositive @HappyFlow
  Scenario Outline: GetDefaultPlansAndOffersAPi - returns <numberOfMatches> plans for <testCondition>
    When a request is made to the GetDefaultPlansAndOffers Api with "<accountType>" account type "<promotionCode>" promotion code "<enrollmentSource>" enrollment source "<testCondition>" condition
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as <numberOfMatches>
    And the response should contain the expected plans for "<testCondition>"

    Examples:
      | accountType | promotionCode | enrollmentSource | testCondition                      | numberOfMatches |
      | RS          |               | MAIL             | GET_DEFAULT_PLANS_AND_OFFERS_TC_122| 10              |
      | CM          |               | MAIL             | GET_DEFAULT_PLANS_AND_OFFERS_TC_124| 5               |
      | RS          | DEALS         | MAIL             | GET_DEFAULT_PLANS_AND_OFFERS_TC_126| 9               |
      | CM          | SAVE100       | FAX              | GET_DEFAULT_PLANS_AND_OFFERS_TC_127| 5               |
