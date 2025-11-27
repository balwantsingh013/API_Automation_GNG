Feature: Verify ValidateUserName Api

  @ValidateUserName @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to validateUsername Api for "<testCondition>"
    Then verify response code of "ValidateUserName" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                        | errorCode | errorMessage            |
      | TC_1__Negative__Missing_Request_ID                                   | 10001     | Missing Request ID      |
      | TC_2__Negative__Invalid_Request_ID__Length                           | 10002     | Invalid Request ID      |
      | TC_3__Negative__Duplicate_Request_ID                                 | 10003     | Duplicate Request ID    |
      | TC_4__Negative__Missing_Username                                     | 10349     | Missing Username        |
      | TC_5__Negative__Invalid_Username_format__Length___Too_Short____      | 10351     | Invalid Username Length |
      | TC_6__Negative__Invalid_Username_format__Length___Too_Long____       | 10351     | Invalid Username Length |
      | TC_7__Negative__Invalid_Username_format__Alphanumeric                | 10353     | Invalid Username Format |

  @ValidateUserNamePositve @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to validateUsername Api for "<testCondition>"
    Then verify response code of "ValidateUserName" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have the username status as "<status>"

    Examples:
      | testCondition                                                        | errorCode | errorMessage | status    |
      | TC_9__Positive__Username_Available                                   | 0         |              | AVAILABLE |
      | TC_8__Positive__Username_Available                                   | 0         |              | AVAILABLE |
      | TC_10__Positive__Username_Active__users_table                        | 0         |              | ACTIVE    |
      | TC_11__Positive__Username_Active__custadv_pending_registrations_table| 0         |              | ACTIVE    |
      | TC_12__Positive__Username_Inactive                                   | 0         |              | INACTIVE  |
