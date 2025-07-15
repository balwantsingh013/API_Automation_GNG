Feature: Verify GetDefaultPlansAndOffers Api

@GetDefaultPlansAndOffersPositive @HappyFlow
Scenario Outline: GetDefaultPlansAndOffersAPi - returns 9 plans for <testCondition>
When a request is made to the GetDefaultPlansAndOffers Api with "<accountType>" account type "<promotionCode>" promotion code "<testCondition>" condition
Then verify response code of "GetDefaultPlansAndOffers" Api is 200
And response should have ErrorCode 0 and ErrorMessage ""
And response should return numberOfMatches as 10
And the response should contain the following plans:
| planCode | planDescription                     |
| MVS      | Variable Select                     |
| MI       | Market Intro                        |
| RGB      | Guaranteed Bill                     |
| 24M      | 24-Month Fixed                      |
| 18M      | 18-Month Fixed                      |
| GPP      | 12-Month Fixed                      |
| RF6      | 6-Month Fixed                       |
| MAP      | Market Advantage                    |
| PGB      | Pre-Pay Guaranteed Bill             |
| PRP      | Pre-Pay                             |

Examples:
|accountType|PromotionCode|testCondition                      |
|RS         |             |GET_DEFAULT_PLANS_AND_OFFERS_TC_122|

