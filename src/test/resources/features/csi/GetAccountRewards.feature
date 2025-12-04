Feature: Verify GetAccountRewards Api

  @GetAccountRewards @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetAccountRewards Api for "<testCondition>"
    Then verify response code of "GetAccountRewards" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                        | errorCode | errorMessage                 |
      | TC_107__Negative__Missing_Request_ID                                 | 10001     | Missing Request ID           |
      | TC_108__Negative__Invalid_Request_ID_Length                          | 10002     | Invalid Request ID           |
      | TC_109__Negative__Duplicate_Request_ID                               | 10003     | Duplicate Request ID         |
      | TC_110__Negative__Missing_customerCode                               | 10011     | Missing Customer Code        |
      | TC_111__Negative__Invalid_customerCode_Length                        | 10015     | Invalid Customer Code Format |
      | TC_112__Negative__Invalid_customerCode_Format                        | 10015     | Invalid Customer Code Format |
      | TC_113__Negative__Invalid_customerCode                               | 40015     | Invalid Account Number       |
      | TC_114__Negative__Missing_premisesCode                               | 10013     | Missing Premisses Code       |
      | TC_115__Negative__Invalid_premisesCode_Length                        | 10005     | Invalid Premises Code Format |
      | TC_116__Negative__Invalid_premisesCode_Format                        | 10005     | Invalid Premises Code Format |
      | TC_117__Negative__Invalid_premisesCode                               | 40015     | Invalid Account Number       |

  @GetAccountRewardsPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetAccountRewards Api for "<testCondition>"
    Then verify response code of "GetAccountRewards" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have the rewards status as "<status>"

    Examples:
      | testCondition                                                             | errorCode | errorMessage | status   |
      | TC_118__Positive__Rewards_Response_Returned_with_Active_Rewards           | 0         |              | ACTIVE   |
      | TC_119__Positive__Rewards_Response_Returned_with_Pending_Rewards          | 0         |              | PENDING  |
      | TC_120__Positive__Rewards_Response_Returned_with_Active_and_Pending_Rewards| 0        |              | MIXED    |
      | TC_121__Positive__Rewards_Response_Returned_with_NO_Rewards               | 0         |              | NONE     |
      | TC_122__Positive__Rewards_Response_Returned_with_Refer__a__Friend_Reward  | 0         |              | REFERRED |
