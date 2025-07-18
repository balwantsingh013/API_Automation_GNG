Feature: Verify GetDefaultPlansAndOffers Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetDefaultPlansAndOffersPositive @HappyFlow
  Scenario Outline: GetDefaultPlansAndOffersAPi - returns <numberOfMatches> plans for <testCondition>
    When a request is made to the GetDefaultPlansAndOffers Api with "<customerType>" customer type "<promotionCode>" promotion code "<enrollmentSource>" enrollment source "<testCondition>" condition
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as <numberOfMatches>
    And the response should contain the expected plans for "<testCondition>"

        #?? TC 152_2 transactionType = "MKSW" -> 0 plans i ftd ex., 9 results actually being returned?
        #?? TC 151_3 should it be in the negative flow?
    Examples:
      | customerType | promotionCode | enrollmentSource  | testCondition                         | numberOfMatches |
      | RESIDENTIAL |               | MAIL               | GET_DEFAULT_PLANS_AND_OFFERS_TC_122   | 10              |
      | COMMERCIAL  |               | MAIL               | GET_DEFAULT_PLANS_AND_OFFERS_TC_124   | 5               |
      | RESIDENTIAL | DEALS         | MAIL               | GET_DEFAULT_PLANS_AND_OFFERS_TC_126   | 9               |
      | COMMERCIAL  | SAVE100       | FAX                | GET_DEFAULT_PLANS_AND_OFFERS_TC_127   | 5               |
      | RESIDENTIAL | AAA           | WEB                | GET_DEFAULT_PLANS_AND_OFFERS_TC_151_1 | 9               |
      | COMMERCIAL  | AAA           | WEB                | GET_DEFAULT_PLANS_AND_OFFERS_TC_151_2 | 5               |
      ##| INVALID     | AAA           | WEB                | GET_DEFAULT_PLANS_AND_OFFERS_TC_151_3 | 0               |
      | RESIDENTIAL | AAA           | WEB                | GET_DEFAULT_PLANS_AND_OFFERS_TC_152_1 | 9               |
      | RESIDENTIAL | AAA           | WEB                | GET_DEFAULT_PLANS_AND_OFFERS_TC_152_2 | 9               |
      | RESIDENTIAL |               | ALL_CONNECT        | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_1 | 10              |
      | RESIDENTIAL |               | CIM_BUILDER_TURN_ON| GET_DEFAULT_PLANS_AND_OFFERS_TC_153_2 | 10              |
      | RESIDENTIAL |               | CORRESPONDENCE     | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_3 | 10              |
      | RESIDENTIAL |               | EMAIL              | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_4 | 10              |
      | RESIDENTIAL |               | ENERGY_SHOP        | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_5 | 10              |
      | RESIDENTIAL |               | FAX                | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_6 | 10              |
      | RESIDENTIAL |               | GEORGIA_GAS_SAVINGS| GET_DEFAULT_PLANS_AND_OFFERS_TC_153_7 | 10              |
      | RESIDENTIAL |               | GNG_HUB            | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_8 | 10              |
      | RESIDENTIAL |               | MAIL               | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_9 | 10              |
      | RESIDENTIAL |               | MOOVE_GURU         | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_10| 10              |
      | RESIDENTIAL |               | ONE_SOURCE         | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_11| 10              |
      | RESIDENTIAL |               | PHONE_CALL         | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_12| 10              |
      | RESIDENTIAL |               | VIV_INT            | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_13| 10              |
      | RESIDENTIAL |               | WEB                | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_14| 10              |
      | RESIDENTIAL |               | MAIL               | GET_DEFAULT_PLANS_AND_OFFERS_TC_154   | 10              |