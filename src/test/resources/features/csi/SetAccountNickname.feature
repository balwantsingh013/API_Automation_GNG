Feature: Verify SetAccountNickname Api

  @SetAccountNickname @NegativeFlow @CSI
  Scenario Outline: Verify the response for SetAccountNickname API for "<testCondition>"
    When a request is made to SetAccountNickname Api for "<testCondition>"
    Then verify response code of "SetAccountNickname" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                           | errorCode | errorMessage                          |
      | Missing_request_id_TC_1                                 | 10001     | Missing Request ID                    |
      | Length_of_request_id_larger_than_32_TC_2                | 10002     | Invalid Request ID                    |
      | Duplicate_request_id_TC_3                               | 10003     | Duplicate Request ID                  |
      | Missing_customerCode_TC_4                               | 10011     | Missing Customer Code                 |
      | CustomerCode_length_greater_than_9_TC_5                 | 10015     | Invalid Customer Code Format          |
      | CustomerCode_not_a_string_TC_6                          | 10015     | Invalid Customer Code Format          |
      | CustomerCode_not_found_in_UCBCUST_TC_7                  | 40015     | Invalid Account Number                |
      | Missing_premisesCode_TC_8                               | 10013     | Missing Premisses Code                |
      | PremisesCode_length_greater_than_7_TC_9                 | 10005     | Invalid Premises Code Format          |
      | PremisesCode_not_a_string_TC_10                         | 10005     | Invalid Premises Code Format          |
      | PremisesCode_not_found_in_UCBPREM_TC_11                 | 40015     | Invalid Account Number                |
      | Nickname_not_allowed_for_new_account_TC_12              | 40215     | Nickname not allowed for new account |
      | Nickname_already_exists_for_account_TC_13               | 40217     | Nickname already exists for the account |
      | Nickname_missing_for_existing_account_TC_14             | 40219     | Nickname does not exist in the request |

  @SetAccountNicknamePositive @HappyFlow @CSI
  Scenario Outline: Verify the response for SetAccountNickname API for "<testCondition>"
    When a request is made to SetAccountNickname Api for "<testCondition>"
    Then verify response code of "SetAccountNickname" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should confirm nickname status as "<status>"

    Examples:
      | testCondition                                | errorCode | status   |errorMessage|
      | Valid_account_with_new_nickname_TC_15        | 0         | UPDATED  |            |
