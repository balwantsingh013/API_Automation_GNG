Feature: Verify GetUserRoles Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetUserRoleWithInvalidRequestID @Phase1 @NegativeFlow
  Scenario Outline: Verify GetUserRoles Api with invalid requestID "<requestID>"TC3_TC5
    When a request is made to the GetUserRoles Api with "<requestID>"TC3_TC5
    Then verify response code of "GetUserRoles" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID                | errorCode | errorMessage         |
      | NULL_REQUEST_ID_TC3      | 10001     | Missing Request ID   |
      | DUPLICATE_REQUEST_ID_TC4 | 10003     | Duplicate Request ID |
      | LONG_REQUEST_ID_TC5      | 10002     | Invalid Request ID   |

  @GetUserRolesInvalidLoginID @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid loginID "<loginID>"TC6_TC9
    When a request is made to the GetUserRoles Api with "<loginID>"TC6_TC9
    Then verify response code of "GetUserRole" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | loginID                        | errorCode | errorMessage              |
      | NULL_LOGIN_ID_AND_PASSWORD_TC6 | 10110     | Invalid Login Credentials |
      | NULL_LOGIN_ID_TC7              | 10110     | Invalid Login Credentials |
      | ALPHANUMERIC_LOGIN_ID_TC8      | 10110     | Invalid Login Credentials |
      | MAX_LENGTH_LOGIN_ID_TC9        | 10110     | Invalid Login Credentials |


  @GetUserRolesInvalidPassword @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid Password  "<password >"TC10_TC12
    When a request is made to the GetUserRoles Api with "<password>"TC10_TC12
    Then verify response code of "GetUserRole" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | password                                        | errorCode | errorMessage              |
      | NULL_PASSWORD_TC10                              | 10110     | Invalid Login Credentials |
      | INVALID_PASSWORD_FORMAT_NOT_ENCRYPTED_TC11      | 10110     | Invalid Login Credentials |
      | INVALID_PASSWORD_FORMAT_ENCRYPTED_10_CHAR_TC12  | 10110     | Invalid Login Credentials |
      | INVALID_PASSWORD_FORMAT_ENCRYPTED_7_CHAR_TC12_1 | 10110     | Invalid Login Credentials |



  @GetUserRolesInvalidTestConditionRespUserTable @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid testCondition "<testCondition>"TC13_TC14
    When a request is made to the GetUserRoles Api with "<testCondition>"TC13_TC14
    Then verify response code of "GetUserRole" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                             | errorCode | errorMessage                             |
      | INVALID_LOGIN_ID_TC13                     | 2000      | Invalid Login ID                         |
      | PASSWORD_MISMATCH_WITH_LOGIN_ID_TC14      | 2000      | The password doesn't match the Login ID |




  @GetUserRolesInvalidPasswordInDB @Phase1  @NegativeFlow
  Scenario: Verify response code for invalid Password TC15
    When a request is made to the GetUserRoles Api with TC15
    Then verify response code of "GetUserRole" Api is 200
    And response should have ErrorCode 2000 and ErrorMessage "The password doesn't match the Login ID. The Login ID has been locked."

  @GetUserRolesExpiredPasswordInDB @Phase1  @NegativeFlow
  Scenario: Verify response code for invalid Password TC16
    When a request is made to the GetUserRoles Api with TC16
    Then verify response code of "GetUserRole" Api is 200
    And response should have ErrorCode 2010 and ErrorMessage "Expired password"


  @GetUserRolesLockedOutLoginIDInDB @Phase1  @NegativeFlow
  Scenario: Verify response code for invalid Password TC17
    When a request is made to the GetUserRoles Api with TC17
    Then verify response code of "GetUserRole" Api is 200
    And response should have ErrorCode 2000 and ErrorMessage "Locked out Login ID"



  @GetUserRolesSuccessfulResponseWithUserRolesIDInDB @Phase1  @NegativeFlow
  Scenario: Verify response code for invalid Password TC19
    When a request is made to the GetUserRoles Api with TC19
    Then verify response code of "GetUserRole" Api is 200
