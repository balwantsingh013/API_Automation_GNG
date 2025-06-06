Feature: Verify GetUserRoles Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

    @DBValidation
  Scenario: GetUserRoles Api - TC_01 Ensure the entry for UZBPSTO_OBJECT = SPK_WEB_API is present in the UZBPSTO table
    When we query the database for validate UZBPSTO_OBJECT value

  @DBValidation
  Scenario: GetUserRoles Api - TC_02 Ensure the entry for UZRPSTO_PARM_NAME = FAILED_LOGINS_TO_LOCK is present in the UZRPSTO table with value 4
    When we query the database for validate UZRPSTO_PARM_NAME value


  @GetUserRoleWithInvalidRequestID @Phase1 @NegativeFlow
  Scenario Outline: GetUserRoles Api - Verify Api Response with invalid requestID "<requestID>"
    When a request is made to the GetUserRoles Api with "<requestID>"TC3_TC5
    Then verify response code of "GetUserRoles" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID                | errorCode | errorMessage         |
      | NULL_REQUEST_ID_TC3      | 10001     | Missing Request ID   |
      | NO_REQUEST_ID_TC3A       | 10001     | Missing Request ID   |
      | DUPLICATE_REQUEST_ID_TC4 | 10003     | Duplicate Request ID |
      | LONG_REQUEST_ID_TC5      | 10002     | Invalid Request ID   |

  @GetUserRolesInvalidLoginID @Phase1  @NegativeFlow
  Scenario Outline: GetUserRoles Api - Verify response code for invalid loginID "<loginID>"
    When a request is made to the GetUserRoles Api with "<loginID>"TC6_TC9
    Then verify response code of "GetUserRole" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | loginID                           | errorCode | errorMessage              |
      | WITHOUT_LOGIN_ID_AND_PASSWORD_TC6 | 10110     | Invalid Login Credentials |
      | NULL_LOGIN_ID_AND_PASSWORD_TC6A   | 10110     | Invalid Login Credentials |
      | WITHOUT_LOGIN_ID_TC7              | 10110     | Invalid Login Credentials |
      | NULL_LOGIN_ID_TC7A                | 10110     | Invalid Login Credentials |
      | LOGIN_ID_MORE_THAN_30_CHAR_TC8    | 10110     | Invalid Login Credentials |
      | LOGIN_ID_WITH_SPECIAL_CHAR_TC9    | 10110     | Invalid Login Credentials |


  @GetUserRolesInvalidPassword @Phase1  @NegativeFlow
  Scenario Outline: GetUserRoles Api - Verify response code for invalid Password "<password>"
    When a request is made to the GetUserRoles Api with "<password>" TC10_TC12
    Then verify response code of "GetUserRole" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | password                                               | errorCode | errorMessage              |
      | WITHOUT_PASSWORD_FIELD_TC10                            | 10110     | Invalid Login Credentials |
      | NULL_PASSWORD_TC10A                                    | 10110     | Invalid Login Credentials |
      | UNENCRYPTED_PASSWORD_TC11                              | 10110     | Invalid Login Credentials |
      | ENCRYPTED_PASSWORD_MORE_THAN_10_CHAR_TC12              | 10110     | Invalid Login Credentials |
      | ENCRYPTED_PASSWORD_LESS_THAN_7_CHAR_TC12A              | 10110     | Invalid Login Credentials |
      | ENCRYPTED_PASSWORD_WITH_8_CHAR_WITH_SPECIAL_CHAR_TC12B | 10110     | Invalid Login Credentials |


  @GetUserRolesInvalidTestConditionRespUserTable @Phase1  @NegativeFlow
  Scenario Outline: GetUserRoles Api - Verify response code for invalid testCondition "<testCondition>"
    When a request is made to the GetUserRoles Api with "<testCondition>"TC13_TC14
    Then verify response code of "GetUserRole" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                             | errorCode | errorMessage                             |
      | INVALID_LOGIN_ID_TC13                     | 2000      | Invalid Login ID                         |
      | PASSWORD_MISMATCH_WITH_LOGIN_ID_TC14      | 2000      | The password doesn't match the Login ID |


  @GetUserRolesInvalidPasswordInDB @Phase1  @NegativeFlow
  Scenario: GetUserRoles Api - TC15 - Verify response code for invalid Password
    When a request is made to the GetUserRoles Api with TC15
    Then verify response code of "GetUserRole" Api is 200
    And response should have ErrorCode 2000 and ErrorMessage "The password doesn't match the Login ID. The Login ID has been locked."


  @GetUserRolesExpiredPasswordInDB @Phase1  @NegativeFlow
  Scenario: GetUserRoles Api - TC16 - Verify response code for invalid Password
    When a request is made to the GetUserRoles Api with TC16
    Then verify response code of "GetUserRole" Api is 200
    And response should have ErrorCode 2010 and ErrorMessage "Expired password"


  @GetUserRolesLockedOutLoginIDInDB @Phase1  @NegativeFlow
  Scenario: GetUserRoles Api - TC17 - Verify response code for invalid Password
    When a request is made to the GetUserRoles Api with TC17
    Then verify response code of "GetUserRole" Api is 200
    And response should have ErrorCode 2000 and ErrorMessage "Locked out Login ID"


  @GetUserRolesSuccessfulResponseWithUserRolesIDInDB @Phase1  @NegativeFlow
  Scenario: GetUserRoles Api  TC19 - Verify response code for invalid Password
    When a request is made to the GetUserRoles Api with TC19
    Then verify response code of "GetUserRole" Api is 200
