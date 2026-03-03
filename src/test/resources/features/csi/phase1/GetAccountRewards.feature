Feature: Verify GetAccountRewards Api

  @GetAccountRewards @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetAccountRewards Api for "<testCondition>"
    Then verify response code of "GetAccountRewards" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                | errorCode | errorMessage                 |
      | TC_129__Negative__Missing_Request_ID         | 10001     | Missing Request ID           |
      | TC_130__Negative__Invalid_Request_ID_Length  | 10002     | Invalid Request ID           |
      | TC_131__Negative__Duplicate_Request_ID       | 10003     | Duplicate Request ID         |
      | TC_132__Negative__Missing_customerCode       | 10011     | Missing Customer Code        |
      | TC_133__Negative__Invalid_customerCode_Length| 10015     | Invalid Customer Code Format |
      | TC_134__Negative__Invalid_customerCode_Format| 10015     | Invalid Customer Code Format |
      | TC_135__Negative__Invalid_Account_Number     | 40015     | Invalid Account Number       |
      | TC_136__Negative__Missing_premisesCode       | 10013     | Missing Premises Code        |
      | TC_137__Negative__Invalid_premisesCode_Length| 10005     | Invalid Premises Code Format |
      | TC_138__Negative__Invalid_premisesCode_Format| 10005     | Invalid Premises Code Format |

  @GetAccountRewardsPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetAccountRewards Api for "<testCondition>"
    Then verify response code of "GetAccountRewards" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And verify the reward details in database for "<testCondition>"

    Examples:
      | testCondition                                               | errorCode | errorMessage |
      | TC_139__Positive__Rewards_Response_Returned_with_Active_Rewards                            | 0         |              |
      | TC_140__Positive__Rewards_Response_Returned_with_Pending_Rewards                         | 0         |              |
      | TC_141__Positive__Rewards_Response_Returned_with_Active_and_Pending_Rewards               | 0         |              |
      | TC_142__Positive__Rewards_Response_Returned_with_No_Rewards                               | 0         |              |
      | TC_143__Positive__Active_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards                    | 0         |              |
      | TC_144__Positive__Pending_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards                    | 0         |              |
