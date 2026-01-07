Feature: Verify GetAccountInfo Api

  @GetAccountInfo @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetAccountInfo Api for "<testCondition>"
    Then verify response code of "GetAccountInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                           | errorCode | errorMessage                  |
#      | TC_185__Negative__Missing_Request_ID                    | 10001     | Missing Request ID            |
#      | TC_186__Negative__Invalid_Request_ID__Length            | 10002     | Invalid Request ID            |
#      | TC_187__Negative__Duplicate_Request_ID                  | 10003     | Duplicate Request ID          |
#      | TC_188__Negative__Invalid_customerCode_Length           | 10015     | Invalid Customer Code Format  |
#      | TC_189__Negative__Invalid_customerCode_Format           | 10015     | Invalid Customer Code Format  |
#      | TC_190__Negative__Invalid_premisesCode_Length           | 10005     | Invalid Premises Code Format  |
#      | TC_191__Negative__Invalid_premisesCode_Format           | 10005     | Invalid Premises Code Format  |
#      | TC_192__Negative__Invalid_customerCode                  | 40015     | Invalid Account Number        |
#      | TC_193__Negative__Invalid_premisesCode                  | 40015     | Invalid Account Number        |

  @GetAccountInfoPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetAccountInfo Api for "<testCondition>"
    Then verify response code of "GetAccountInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have success as "<success>"

    Examples:
      | testCondition                          | errorCode | errorMessage | success |
      | TC_194__Positive__Account_Info_Returned| 0         |              | true    |
