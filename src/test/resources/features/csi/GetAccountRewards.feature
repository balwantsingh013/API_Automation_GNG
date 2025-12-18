Feature: Verify GetAccountRewards Api

  @GetAccountRewards @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetAccountRewards Api for "<testCondition>"
    Then verify response code of "GetAccountRewards" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                | errorCode | errorMessage                 |
      | TC_112__Negative__Missing_Request_ID         | 10001     | Missing Request ID           |
      | TC_113__Negative__Invalid_Request_ID_Length  | 10002     | Invalid Request ID           |
      | TC_114__Negative__Duplicate_Request_ID       | 10003     | Duplicate Request ID         |
      | TC_115__Negative__Missing_customerCode       | 10011     | Missing Customer Code        |
      | TC_116__Negative__Invalid_customerCode_Length| 10015     | Invalid Customer Code Format |
      | TC_117__Negative__Invalid_customerCode_Format| 10015     | Invalid Customer Code Format |
      | TC_118__Negative__Invalid_customerCode       | 40015     | Invalid Account Number       |
      | TC_119__Negative__Missing_premisesCode       | 10013     | Missing Premises Code        |
      | TC_120__Negative__Invalid_premisesCode_Length| 10005     | Invalid Premises Code Format |
      | TC_121__Negative__Invalid_premisesCode_Format| 10005     | Invalid Premises Code Format |
      | TC_122__Negative__Invalid_premisesCode       | 40015     | Invalid Account Number       |

  @GetAccountRewardsPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetAccountRewards Api for "<testCondition>"
    Then verify response code of "GetAccountRewards" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have the rewards status as "<status>"

    Examples:
      | testCondition                                               | errorCode | errorMessage | status   |
      | TC_123__Positive__Active_Rewards                            | 0         |              | ACTIVE   |
      | TC_124__Positive__Pending_Rewards                           | 0         |              | PENDING  |
      | TC_125__Positive__Active_and_Pending_Rewards                | 0         |              | MIXED    |
      | TC_126__Positive__No_Rewards                                | 0         |              | NONE     |
      | TC_127__Positive__Refer_A_Friend_Rewards                    | 0         |              | REFERRED |
      | TC_128__Positive__LoginID_Saved                             | 0         |              | SAVED    |
