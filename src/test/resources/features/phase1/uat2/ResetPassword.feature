Feature: Verify ResetPassword Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @ResetPasswordUZRPSTOTable @Phase1 @DBValidation
  Scenario: TC-20 - ResetPassword Api with Expire Day Value
    When a request is made to the validate UZRPSTO_PARM_NAME value in DB TC20

  @ResetPasswordWithInvalidRequestID @Phase1 @NegativeFlow
  Scenario Outline: Verify ResetPassword Api with invalid requestID "<requestID>" TC21_TC23
    When a request is made to the ResetPassword Api with "<requestID>"TC21_TC23
    Then verify response code of "ResetPassword" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID                 | errorCode | errorMessage         |
      | WITHOUT_REQUEST_ID_TC21      | 10001     | Missing Request ID   |
      | NULL_REQUEST_ID_TC21A      | 10001     | Missing Request ID   |
      | DUPLICATE_REQUEST_ID_TC22 | 10003     | Duplicate Request ID |
      | LONG_REQUEST_ID_TC23      | 10002     | Invalid Request ID   |


  @ResetPasswordWithInvalidLoginID @Phase1 @NegativeFlow
  Scenario Outline: TC24_TC28 - Verify ResetPassword Api with invalid loginID "<loginID>"
    When a request is made to the ResetPassword Api with "<loginID>"TC24_TC28
    Then verify response code of "ResetPassword" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | loginID                                 | errorCode | errorMessage              |
      | WITHOUT_LOGIN_ID_AND_OLD_PASSWORD_TC24     | 10110     | Invalid Login Credentials |
      | NULL_LOGIN_ID_AND_OLD_PASSWORD_TC24A     | 10110     | Invalid Login Credentials |
      | WITHOUT_LOGIN_ID_TC25                      | 10110     | Invalid Login Credentials |
      | NULL_LOGIN_ID_TC25A                     | 10110     | Invalid Login Credentials |
      | MAX_LENGTH_LOGIN_ID_TC26                | 10110     | Invalid Login Credentials |
      | SPECIAL_CHAR_LOGIN_ID_TC27              | 10110     | Invalid Login Credentials |
      | INVALID_LOGIN_ID_NOT_IN_USER_TABLE_TC28 | 10110     | Invalid Login Credentials |


  @ResetPasswordInvalidOldPassword @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid OldPassword "<oldPassword >"TC29_TC31
    When a request is made to the ResetPassword Api with "<oldPassword>"TC29_TC31
    Then verify response code of "ResetPassword" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | oldPassword                                         | errorCode | errorMessage              |
      | NULL_OLD_PASSWORD_TC29                              | 10110     | Invalid Login Credentials |
      | INVALID_OLD_PASSWORD_FORMAT_NOT_ENCRYPTED_TC30      | 10110     | Invalid Login Credentials |
      | INVALID_OLD_PASSWORD_FORMAT_ENCRYPTED_MAX_CHAR_TC31 | 10110     | Invalid Login Credentials |
      | INVALID_PASSWORD_FORMAT_ENCRYPTED_MIN_7_CHAR_TC31_1 | 10110     | Invalid Login Credentials |

  @ResetPasswordInvalidNewPassword @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid NewPassword "<newPassword >"TC32_TC35
    When a request is made to the ResetPassword Api with "<newPassword>"TC32_TC35
    Then verify response code of "ResetPassword" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | newPassword                                         | errorCode | errorMessage              |
      | NULL_NEW_PASSWORD_TC32                              | 10110     | Invalid Login Credentials |
      | INVALID_NEW_PASSWORD_FORMAT_NOT_ENCRYPTED_TC33      | 10110     | Invalid Login Credentials |
      | INVALID_NEW_PASSWORD_FORMAT_ENCRYPTED_MAX_CHAR_TC34 | 10110     | Invalid Login Credentials |
      | INVALID_PASSWORD_FORMAT_ENCRYPTED_MIN_7_CHAR_TC34_1 | 10110     | Invalid Login Credentials |
      | OLD_PASSWORD_NEW_PASSWORD_SAME_TC35                 | 10110     | Invalid Login Credentials |

