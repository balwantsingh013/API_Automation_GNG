Feature: Verify UpdateAccountNickname Api

  @UpdateAccountNickname @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateAccountNickname Api for "<testCondition>"
    Then verify response code of "UpdateAccountNickname" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                   | errorCode | errorMessage                               |
      | TC_53__Negative__Missing_Request_ID             | 10001     | Missing Request ID                         |
      | TC_54__Negative__Invalid_Request_ID__Length     | 10002     | Invalid Request ID                         |
      | TC_55__Negative__Duplicate_Request_ID           | 10003     | Duplicate Request ID                       |
      | TC_56__Negative__Missing_customerCode           | 10011     | Missing Customer Code                      |
      | TC_57__Negative__Invalid_customerCode_Length    | 10015     | Invalid Customer Code Format               |
      | TC_58__Negative__Invalid_customerCode_Format    | 10015     | Invalid Customer Code Format               |
      | TC_59__Negative__Invalid_customerCode           | 40015     | Invalid Account Number                     |
      | TC_60__Negative__Missing_premisesCode           | 10013     | Missing Premisses Code                     |
      | TC_61__Negative__Invalid_premisesCode_Length    | 10005     | Invalid Premises Code Format               |
      | TC_62__Negative__Invalid_premisesCode_Format    | 10005     | Invalid Premises Code Format               |
      | TC_63__Negative__Invalid_premisesCode           | 40015     | Invalid Account Number                     |
      | TC_64__Negative__Nickname_Not_Allowed           | 40215     | Nickname not allowed for new account       |
      | TC_65__Negative__Nickname_Already_Exists        | 40217     | Nickname already exists for the account    |
      | TC_66__Negative__Nickname_Missing               | 40219     | Nickname does not exist in the request     |

  @UpdateAccountNicknamePositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateAccountNickname Api for "<testCondition>"
    Then verify response code of "UpdateAccountNickname" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should confirm nickname status as "<status>"

    Examples:
      | testCondition                        | errorCode | status   | errorMessage |
      | TC_67__Positive__Nickname_Set        | 0         | UPDATED  |              |
