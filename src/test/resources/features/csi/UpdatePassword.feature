Feature: Verify UpdatePassword Api

  @UpdatePassword @NegativeFlow @CSI
  Scenario Outline: Verify the response for UpdatePassword API for "<testCondition>"
    When a request is made to UpdatePassword Api for "<testCondition>"
    Then verify response code of "UpdatePassword" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                              | errorCode | errorMessage               |
      | Missing_Request_id_TC_1                                    | 10001     | Missing Request ID         |
      | Length_of_Request_id_larger_than_32_TC_2                   | 10002     | Invalid Request ID         |
      | Duplicate_Request_id_TC_3                                  | 10003     | Duplicate Request ID       |
      | Missing_Username_TC_4                                      | 10349     | Missing Username           |
      | Length_of_Username_smaller_than_5_TC_5                     | 10351     | Invalid Username Length    |
      | Length_of_Username_greater_than_15_TC_6                    | 10351     | Invalid Username Length    |
      | Username_not_Alphanumeric_TC_7                             | 10353     | Invalid Username Format    |
      | Username_not_found_TC_8                                    | 10357     | Username Not Found         |
      | Inactive_username_TC_9                                     | 10355     | Inactive Username          |
      | Missing_password_TC_10                                     | 10113     | Missing Password           |
      | Password_shorter_than_8_characters_TC_11                   | 10157     | Invalid Password Format    |
      | Password_longer_than_64_characters_TC_12                   | 10157     | Invalid Password Format    |
      | Password_not_a_string_TC_13                                | 10157     | Invalid Password Format    |
      | Password_does_not_conform_to_policy_TC_14                  | 10157     | Invalid Password Format    |
      | Password_matches_current_password_TC_15                    | 10359     | Invalid Password           |
#
  @UpdatePasswordPositive @HappyFlow @CSI
  Scenario Outline: Verify the response for UpdatePassword API for "<testCondition>"
    When a request is made to UpdatePassword Api for "<testCondition>"
    Then verify response code of "UpdatePassword" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                          | errorCode |errorMessage|
      | Valid_username_and_password_TC_16      | 0         |            |
