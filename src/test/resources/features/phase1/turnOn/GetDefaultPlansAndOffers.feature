Feature: Verify GetDefaultPlansAndOffers Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetDefaultPlansAndOffersPositive @HappyFlow @turnOn @Phase1
  Scenario Outline: GetDefaultPlansAndOffersAPi - returns <numberOfMatches> plans for <testCondition>
    When a request is made to the GetDefaultPlansAndOffers Api with "<customerType>" customer type "<promotionCode>" promotion code "<enrollmentSource>" enrollment source "<testCondition>" condition
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as <numberOfMatches>
    Then the response should contain the expected default plans

    Examples:
      | customerType | promotionCode | enrollmentSource   | testCondition                         | numberOfMatches |
      | RESIDENTIAL  |               | MAIL               | GET_DEFAULT_PLANS_AND_OFFERS_TC_122   | 10              |
      | COMMERCIAL   |               | MAIL               | GET_DEFAULT_PLANS_AND_OFFERS_TC_124   | 5               |
      | RESIDENTIAL  | DEALS         | MAIL               | GET_DEFAULT_PLANS_AND_OFFERS_TC_126   | 9               |
      | COMMERCIAL   | SAVE100       | FAX                | GET_DEFAULT_PLANS_AND_OFFERS_TC_127   | 5               |
      | RESIDENTIAL  | AAA           | WEB                | GET_DEFAULT_PLANS_AND_OFFERS_TC_151_1 | 9               |
      | COMMERCIAL   | AAA           | WEB                | GET_DEFAULT_PLANS_AND_OFFERS_TC_151_2 | 5               |
      | RESIDENTIAL  | AAA           | WEB                | GET_DEFAULT_PLANS_AND_OFFERS_TC_152_1 | 9               |
      | RESIDENTIAL  | AAA           | WEB                | GET_DEFAULT_PLANS_AND_OFFERS_TC_152_2 | 9               |
      | RESIDENTIAL  |               | ALLCONNECT         | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_1 | 10              |
      | RESIDENTIAL  |               | CIM_BUILDER_TURN_ON| GET_DEFAULT_PLANS_AND_OFFERS_TC_153_2 | 10              |
      | RESIDENTIAL  |               | CORRESPONDENCE     | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_3 | 10              |
      | RESIDENTIAL  |               | EMAIL              | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_4 | 10              |
      | RESIDENTIAL  |               | ENERGYSHOP         | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_5 | 10              |
      | RESIDENTIAL  |               | FAX                | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_6 | 10              |
      | RESIDENTIAL  |               | GEORGIAGASSAVINGS  | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_7 | 10              |
      | RESIDENTIAL  |               | GNGHUB             | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_8 | 10              |
      | RESIDENTIAL  |               | MAIL               | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_9 | 10              |
      | RESIDENTIAL  |               | MOOVEGURU          | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_10| 10              |
      | RESIDENTIAL  |               | ONESOURCE          | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_11| 10              |
      | RESIDENTIAL  |               | PHONECALL          | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_12| 10              |
      | RESIDENTIAL  |               | VIVINT             | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_13| 10              |
      | RESIDENTIAL  |               | WEB                | GET_DEFAULT_PLANS_AND_OFFERS_TC_153_14| 10              |
      | RESIDENTIAL  |               | MAIL               | GET_DEFAULT_PLANS_AND_OFFERS_TC_154   | 10              |


  @GetDefaultPlansAndOffersNegativeInvalidRequestId  @turnOn @Phase1 @NegativeFlow
  Scenario Outline: GetDefaultPlansAndOffers Api - Verify GetDefaultPlansAndOffers Api with invalid requestID for "<testCondition>" condition
    When a request is made to the GetDefaultPlansAndOffers Api with an invalid requestID for "<testCondition>"
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                           | errorCode | errorMessage         |
      | GET_DEFAULT_PLANS_AND_OFFERS_MISSING_REQUEST_ID_TC_129  | 10001     | Missing Request ID   |
      | GET_DEFAULT_PLANS_AND_OFFERS_INVALID_REQUEST_ID_TC_130  | 10002     | Invalid Request ID   |
      | GET_DEFAULT_PLANS_AND_OFFERS_DUPLICATE_REQUEST_ID_TC_128| 10003     | Duplicate Request ID |

  @GetDefaultPlansAndOffersNegativeInvalidLoginId  @turnOn @Phase1 @NegativeFlow
  Scenario Outline: GetDefaultPlansAndOffers Api - Verify GetDefaultPlansAndOffers Api with invalid loginId for "<testCondition>" condition
    When a request is made to the GetDefaultPlansAndOffers Api with an invalid loginId for "<testCondition>"
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                           | errorCode | errorMessage              |
      | GET_DEFAULT_PLANS_AND_OFFERS_MISSING_LOGIN_ID_TC_131    | 10112     |Invalid or missing Login ID|
      | GET_DEFAULT_PLANS_AND_OFFERS_INVALID_LOGIN_ID_TC_132    | 10112     |Invalid or missing Login ID|
      | GET_DEFAULT_PLANS_AND_OFFERS_NON_ALPHANUMERIC_ID_TC_133 | 2000      | Invalid Login ID          |


  @GetDefaultPlansAndOffersNegativeInvalidCustomerType @turnOn @Phase1 @NegativeFlow
  Scenario Outline: GetDefaultPlansAndOffers Api - Verify GetDefaultPlansAndOffers Api with invalid customerType for "<testCondition>" condition
    When a request is made to the GetDefaultPlansAndOffers Api with an invalid customerType for "<testCondition>"
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                   | errorCode | errorMessage                        |
      | GET_DEFAULT_PLANS_AND_OFFERS_MISSING_CUSTOMER_TYPE_ID_TC_134    | 10118     |Invalid or missing Customer Type     |
      | GET_DEFAULT_PLANS_AND_OFFERS_INVALID_LENGTH_CUSTOMER_TYPE_TC_135| 1000     |Invalid Request: Invalid Customer Type|
      | GET_DEFAULT_PLANS_AND_OFFERS_INVALID_CUSTOMER_TYPE_TC_136       | 1000     |Invalid Request: Invalid Customer Type|


  @GetDefaultPlansAndOffersNegativeInvalidTransactionType @turnOn @Phase1 @NegativeFlow
  Scenario Outline: GetDefaultPlansAndOffers Api - Verify GetDefaultPlansAndOffers Api with invalid transactionType for "<testCondition>" condition
    When a request is made to the GetDefaultPlansAndOffers Api with an invalid transactionType for "<testCondition>"
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                       | errorCode | errorMessage                      |
      | GET_DEFAULT_PLANS_AND_OFFERS_MISSING_TRANSACTION_TYPE_TC_137        | 10113     |Invalid or missing Transaction Type|
      | GET_DEFAULT_PLANS_AND_OFFERS_INVALID_LENGTH_TRANSACTION_TYPE_TC_138 | 10113     |Invalid or missing Transaction Type|
      | GET_DEFAULT_PLANS_AND_OFFERS_INVALID_TRANSACTION_TYPE_TC_139        |10113      |Invalid or missing Transaction Type|


  @GetDefaultPlansAndOffersNegativeInvalidEnrollmentSource @turnOn @Phase1 @NegativeFlow
  Scenario Outline: GetDefaultPlansAndOffers Api - Verify GetDefaultPlansAndOffers Api with invalid enrollmentSource for "<testCondition>" condition
    When a request is made to the GetDefaultPlansAndOffers Api with an invalid enrollmentSource for "<testCondition>"
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                         | errorCode | errorMessage                             |
      | GET_DEFAULT_PLANS_AND_OFFERS_MISSING_ENROLLMENT_SOURCE_TC_140         | 10117     |Invalid or missing Enrollment Source      |
      | GET_DEFAULT_PLANS_AND_OFFERS_INVALID_LENGTH_ENROLLMENT_SOURCE_TC_141  | 1000      |Invalid Request: Invalid Enrollment Source|
      | GET_DEFAULT_PLANS_AND_OFFERS_INVALID_ENROLLMENT_SOURCE_TC_142         |1000       |Invalid Request: Invalid Enrollment Source|

  @InvalidMarketingPromotionCodes @turnOn @Phase1 @NegativeFlow
  Scenario Outline: GetDefaultPlansAndOffers Api - Verify GetDefaultPlansAndOffers Api with invalid enrollmentSource for "<testCondition>" condition
    When a request is made to the GetDefaultPlansAndOffers Api with an invalid promotionCode for "<testCondition>"
    Then verify response code of "GetDefaultPlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                           | errorCode | errorMessage                                         |
      | INVALID_MARKETING_PROMOTION_CODE_TC_143                 | 10119     |Invalid Marketing Promotion Code                      |
      |INVALID_PROMOTION_CODE_FOR_ALLCONNECT_TC_144_1           |2100       |Invalid promotion code                                |
      |INVALID_PROMOTION_CODE_FOR_ENERGYSHOP_TC_144_2           |2100       |Invalid promotion code                                |
      |INVALID_PROMOTION_CODE_FOR_GEORGIAGASSAVINGS_TC_144_3    |2100       |Invalid promotion code                                |
      |INVALID_PROMOTION_CODE_FOR_ONESOURCE_TC_144_4            |2100       |Invalid promotion code                                |
      |INVALID_PROMOTION_CODE_FOR_TC_145                        |1000       |Invalid Request: Invalid Promotion Code               |
      |INVALID_PROMOTION_CODE_FOR_CM_CUSTOMER_TC_146            |2100       |Promotion code is valid for Residential Customers only|
      |INVALID_PROMOTION_CODE_FOR_NEW_CUSTOMER_TC_147           |2100       |Promotion code is valid for Existing Customers only   |
      |EXPIRED_PROMOTION_CODE_TC_148                            |2100       |Promotion code is expired                             |
      |INVALID_PROMOTION_CODE_FOR_RS_CUSTOMER_TC_149            |2100       |Promotion code is valid for Commercial Customers only |
      |INVALID_PROMOTION_CODE_FOR_CHANNEL_SOURCE_TC_150         |2100       |Promotion code is valid for Web only                  |