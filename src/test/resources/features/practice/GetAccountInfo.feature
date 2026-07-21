Feature: Practice - Verify GetAccountInfo Api (CSI phase1 style)

  @GetAccountInfoPractice @HappyFlow @Practice @CSI
  Scenario Outline: Practice GetAccountInfo positive flow for "<testCondition>"
    When a practice request is made to GetAccountInfo Api for "<testCondition>"
    Then verify response code of "GetAccountInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have success as "<success>"
    And response should have email existence as "<emailShouldExist>"

    Examples:
      | testCondition                                                  | errorCode | errorMessage | success | emailShouldExist |
      | TC_150__Positive__Account_Info_Returned___Email_Address____    | 0         |              | true    | true             |
      | TC_151__Positive__Account_Info_Returned___No_Email_Address____ | 0         |              | true    | false            |

  @GetAccountInfoPractice @HappyFlow @Practice @CSI
  Scenario Outline: Practice GetAccountInfo positive flow - partner promotions for "<testCondition>"
    When a practice request is made to GetAccountInfo Api for "<testCondition>"
    Then verify response code of "GetAccountInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have success as "<success>"
    And response should have partner promotions indicator as "<partnerIndicator>"

    Examples:
      | testCondition                                                                        | errorCode | errorMessage | success | partnerIndicator |
      | TC_152__Positive__Account_Info_Returned___Partner_Promotions_Indicator__equals_Y____ | 0         |              | true    | Y                |

  @GetAccountInfoPractice @NegativeFlow @Practice @CSI
  Scenario Outline: Practice GetAccountInfo negative flow for "<testCondition>"
    When a practice request is made to GetAccountInfo Api for "<testCondition>"
    Then verify response code of "GetAccountInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                 | errorCode | errorMessage                 |
      | TC_142__Negative__Missing_Request_ID          | 10001     | Missing Request ID           |
      | TC_145__Negative__Invalid_customerCode_Length | 10015     | Invalid Customer Code Format |
