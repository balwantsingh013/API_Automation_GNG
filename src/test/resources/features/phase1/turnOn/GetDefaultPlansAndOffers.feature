Feature: Verify GetDefaultPlansAndOffers Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetDefaultPlansAndOffersPositive @HappyFlow
  Scenario Outline: GetDefaultPlansAndOffersAPi - returns 10 plans for <testCondition>
    When a request is made to the GetDefaultPlansAndOffers Api with "<accountType>" account type "<promotionCode>" promotion code "<enrollmentSource>" enrollment source "<testCondition>" condition
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 10
    And the response should contain the following plans:
    | planCode | planDescription                     | promotion1Code              | promotion1Description                 |
    | MVS      | Variable Select                     | 25 CENTS FOR 12 MONTHS      | $0.25 per therm discount for 12 months|
    | MI       | Market Intro                        |                             |                                       |
    | RGB      | Guaranteed Bill                     | FIX 5 DOLLARS FOR 12 MONTHS | $5 off total bill for 12 months       |
    | 24M      | 24-Month Fixed                      |                             |                                       |
    | 18M      | 18-Month Fixed                      |                             |                                       |
    | GPP      | 12-Month Fixed                      |                             |                                       |
    | RF6      | 6-Month Fixed                       |                             |                                       |
    | MAP      | Market Advantage                    |                             |                                       |
    | PGB      | Pre-Pay Guaranteed Bill             | FIX 10 DOLLARS FOR 12 MONTHS| $10 off total bill for 12 months      |
    | PRP      | Pre-Pay                             |                             |                                       |

    Examples:
    |accountType|promotionCode|enrollmentSource|testCondition                      |
    |RS         |             |MAIL            |GET_DEFAULT_PLANS_AND_OFFERS_TC_122|

  @GetDefaultPlansAndOffersPositive @HappyFlow
  Scenario Outline: GetDefaultPlansAndOffersAPi - returns 5 plans for <testCondition>
    When a request is made to the GetDefaultPlansAndOffers Api with "<accountType>" account type "<promotionCode>" promotion code "<enrollmentSource>" enrollment source "<testCondition>" condition
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 5
    And the response should contain the following plans:
      | planCode | planDescription                     | promotion1Code                    | promotion1Description                 |
      | CVS      | Commercial Variable Select          | COM 25 CENTS FOR 12 MONTHS        | $0.25 per therm discount for 12 months|
      | CMI      | Commercial Market Intro             |                                   |                                       |
      | CGB      | Guaranteed Bill                     | COM 7DOLLARS AND 50 CENT FOR 12MOS| $7.50 off total bill for 12 months    |
      | CFM      | Commercial 12-Month Fixed           | COM FIX 5 CENTS FOR 12 MONTHS     | $0.05 per therm discount for 12 months|
      | CF6      | Commercial 6-Month Fixed            | COM FIX 4 CENTS FOR 6 MONTHS      | $0.04 per therm discount for 6 months |

    Examples:
      |accountType|promotionCode|enrollmentSource|testCondition                      |
      |CM         |             |MAIL            |GET_DEFAULT_PLANS_AND_OFFERS_TC_124|

  @GetDefaultPlansAndOffersPositive @HappyFlow
  Scenario Outline: GetDefaultPlansAndOffersAPi - returns 9 plans for <testCondition> with promotion code and commercial account
    When a request is made to the GetDefaultPlansAndOffers Api with "<accountType>" account type "<promotionCode>" promotion code "<enrollmentSource>" enrollment source "<testCondition>" condition
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 9
    And the response should contain the following plans:
      | planCode | planDescription                     | promotion1Code              | promotion1Description                 |
      | RGB      | Guaranteed Bill                     | FIX 10 DOLLARS FOR 12 MONTHS| $10 off total bill for 12 months      |
      | GPP      | 12-Month Fixed                      | FIX 10 CENTS FOR 12 MONTHS  | $0.10 per therm discount for 12 months|
      | 24M      | 24-Month Fixed                      | FIX 10 CENTS FOR 24 MONTHS  | $0.10 per therm discount for 24 months|
      | RF6      | 6-Month Fixed                       | FIX 10 CENTS FOR 6 MONTHS   | $0.10 per therm discount for 6 months |
      | FIX      | 12-Month Fixed 2                    |                             |                                       |
      | MI       | Market Intro                        | FIX 10 DOLLARS FOR 12 MONTHS| $10 off total bill for 12 months      |
      | MVS      | Variable Select                     | 25 CENTS FOR 12 MONTHS      | $0.25 per therm discount for 12 months|
      | PGB      | Pre-Pay Guaranteed Bill             | FIX 10 DOLLARS FOR 12 MONTHS| $10 off total bill for 12 months      |
      | PRP      | Pre-Pay                             | 15 CENTS FOR 12 MONTHS      | $0.15 per therm discount for 12 months|

    Examples:
      |accountType|promotionCode|enrollmentSource|testCondition                      |
      |CM         |DEALS        |MAIL            |GET_DEFAULT_PLANS_AND_OFFERS_TC_126|

  @GetDefaultPlansAndOffersPositive @HappyFlow
  Scenario Outline: GetDefaultPlansAndOffersAPi - returns 5 plans for <testCondition> with promotion code and commercial account
    When a request is made to the GetDefaultPlansAndOffers Api with "<accountType>" account type "<promotionCode>" promotion code "<enrollmentSource>" enrollment source "<testCondition>" condition
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 5
    And the response should contain the following plans:
      | planCode | planDescription                     | promotion1Code                    | promotion1Description             |
      | CGB      | Guaranteed Bill                     | COM 8DOLLARS AND 50CENT FOR 12 MOS| $8.50 off total bill for 12 months|
      | CFM      | Commercial 12-Month Fixed           | CF 8DOLLARS AND 50CENTS FOR 12 MOS| $8.50 off total bill for 12 months|
      | CF6      | Commercial 6-Month Fixed            | CF 8DOLLARS AND 50CENTS FOR 12 MOS| $8.50 off total bill for 12 months|
      | CVS      | Commercial Variable Select          | COM 8DOLLARS AND 50CENT FOR 12 MOS| $8.50 off total bill for 12 months|
      | CMI      | Commercial Market Intro             | COM 8DOLLARS AND 50CENT FOR 12 MOS| $8.50 off total bill for 12 months|
    Examples:
      |accountType|promotionCode|enrollmentSource|testCondition                      |
      |CM         |SAVE100      |MAIL            |GET_DEFAULT_PLANS_AND_OFFERS_TC_127|

