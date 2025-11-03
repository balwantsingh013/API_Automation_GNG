Feature: Verify ValidateUserName Api

  @ValidateUserName @NegativeFlow @CSI
  Scenario Outline: Verify the response for ValidateUserName API for "<testCondition>"
    When a request is made to validateUsername Api for "<testCondition>"
    Then verify response code of "ValidateUserName" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                              | errorCode | errorMessage                                              |
      | Missing_request_id_TC_1                    | 10001     | Missing Request ID                                        |
      | Length_of_request_id_larger_than_32_TC_2   | 10002     | Invalid Request ID                                        |
      | Duplicate_request_id_TC_3                  | 10003     | Duplicate Request ID                                      |
      | Missing_username_TC_4                      | 10349     | Missing Username                                          |
      | Length_of_username_smaller_than_5_TC_5     | 10351     | Invalid Username Length                                   |
      | Length_of_username_greater_than_15_TC_6    | 10351     | Invalid Username Length                                   |
      | Username_not_alphanumeric_TC_7             | 10353     | Invalid Username Format                                   |

  @ValidateUserNamePositve @HappyFlow @CSI
  Scenario Outline: Verify the response for ValidateUserName API for "<testCondition>"
    When a request is made to validateUsername Api for "<testCondition>"
    Then verify response code of "ValidateUserName" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have the username status as "<status>"

    Examples:
      | testCondition                               | errorCode | errorMessage | status    |
      | Username_does_not_exist_in_mariadb_TC_8     | 0         |              | AVAILABLE |
      | Active_username_exists_in_mariadb_TC_9      | 0         |              | ACTIVE    |
      | Active_username_exists_in_mariadb_TC_9_2    | 0         |              | ACTIVE    |
      | Inactive_username_exists_in_mariadb_TC_10   | 0         |              | INACTIVE  |
