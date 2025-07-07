Feature: Verify ResetPassword Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @ResetPasswordUZRPSTOTable @Phase1 @DBValidation
  Scenario: ResetPassword Api - TC-20 - To validate UZRPSTO_PARM_NAME Expire Day Value in DB
    When a request is made to the validate UZRPSTO_PARM_NAME value in DB TC20

  @ResetPasswordWithInvalidRequestID @Phase1 @NegativeFlow
  Scenario Outline: ResetPassword Api - Verify ResetPassword Api with invalid requestID "<requestID>"
    When a request is made to the ResetPassword Api with "<requestID>"TC21_TC23
    Then verify response code of "ResetPassword" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID                 | errorCode | errorMessage         |
      | WITHOUT_REQUEST_ID_TC21   | 10001     | Missing Request ID   |
      | NULL_REQUEST_ID_TC21A     | 10001     | Missing Request ID   |
      | DUPLICATE_REQUEST_ID_TC22 | 10003     | Duplicate Request ID |
      | LONG_REQUEST_ID_TC23      | 10002     | Invalid Request ID   |

  @ResetPasswordWithInvalidLoginID @Phase1 @NegativeFlow
  Scenario Outline: ResetPassword Api - Verify ResetPassword Api with invalid loginID "<loginID>"
    When a request is made to the ResetPassword Api with "<loginID>"TC24_TC28
    Then verify response code of "ResetPassword" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | loginID                                 | errorCode | errorMessage              |
      | WITHOUT_LOGIN_ID_AND_OLD_PASSWORD_TC24  | 10110     | Invalid Login Credentials |
      | NULL_LOGIN_ID_AND_OLD_PASSWORD_TC24A    | 10110     | Invalid Login Credentials |
      | WITHOUT_LOGIN_ID_TC25                   | 10110     | Invalid Login Credentials |
      | NULL_LOGIN_ID_TC25A                     | 10110     | Invalid Login Credentials |
      | MAX_LENGTH_LOGIN_ID_TC26                | 10110     | Invalid Login Credentials |
      | SPECIAL_CHAR_LOGIN_ID_TC27              | 10110     | Invalid Login Credentials |
      | INVALID_LOGIN_ID_NOT_IN_USER_TABLE_TC28 | 10110     | Invalid Login Credentials |


  @ResetPasswordInvalidOldPassword @Phase1  @NegativeFlow
  Scenario Outline: ResetPassword Api - Verify response code for invalid OldPassword "<oldPassword>"
    Given update the locked indicator to "N" and failed logins to 0
    When a request is made to the ResetPassword Api with "<oldPassword>"TC29_TC31
    Then verify response code of "ResetPassword" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | oldPassword                                                | errorCode | errorMessage                             |
      | WITHOUT_OLD_PASSWORD_TC29                                  | 10110     | Invalid Login Credentials                |
      | NULL_OLD_PASSWORD_TC29A                                    | 10110     | Invalid Login Credentials                |
      | UNENCRYPTED_OLD_PASSWORD_TC30                              | 10110     | Invalid Login Credentials                |
      | ENCRYPTED_OLD_PASSWORD_MORE_THAN_10_CHAR_TC31              | 2000      |  The password doesn't match the Login ID |
      | ENCRYPTED_OLD_PASSWORD_LESS_THAN_7_CHAR_TC31A              | 2000      |  The password doesn't match the Login ID |
      | ENCRYPTED_OLD_PASSWORD_WITH_8_CHAR_WITH_SPECIAL_CHAR_TC31B | 2000      |  The password doesn't match the Login ID |

  @ResetPasswordInvalidNewPassword @Phase1  @NegativeFlow
  Scenario Outline: ResetPassword Api - Verify response code for invalid NewPassword "<newPassword>"
    Given update the locked indicator to "N" and failed logins to 0
    When a request is made to the ResetPassword Api with "<newPassword>"TC32_TC35
    Then verify response code of "ResetPassword" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | newPassword                                                | errorCode | errorMessage                                                                                                |
      | WITHOUT_NEW_PASSWORD_TC32                                  | 10110     | Invalid Login Credentials                                                                                   |
      | NULL_NEW_PASSWORD_TC32A                                    | 10110     | Invalid Login Credentials                                                                                   |
      | UNENCRYPTED_NEW_PASSWORD_TC33                              | 10110     | Invalid Login Credentials                                                                                   |
      | ENCRYPTED_NEW_PASSWORD_MORE_THAN_10_CHAR_TC34              | 11117     | New password must be 7 to 10 characters. It must contain a combination of alphabetic and numeric characters |
      | ENCRYPTED_NEW_PASSWORD_LESS_THAN_7_CHAR_TC34A              | 11117     | New password must be 7 to 10 characters. It must contain a combination of alphabetic and numeric characters |
      | ENCRYPTED_OLD_PASSWORD_WITH_8_CHAR_WITH_SPECIAL_CHAR_TC34B | 11117     | New password must be 7 to 10 characters. It must contain a combination of alphabetic and numeric characters |
      | OLD_PASSWORD_NEW_PASSWORD_SAME_TC35                        | 10110     | Invalid Login Credentials                                                                                   |

  @ResetPasswordOldPasswordMismatchWithDB @Phase1 @DBValidation
  Scenario Outline: ResetPassword Api - TC-36-37 - Validate the case where old password doesn't match with LoginID "<oldPassword>"
    When a request is made to validate oldPassword "<oldPassword>" doesn't match with LoginID TC36
    Then verify response code of "ResetPassword" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | oldPassword                          | errorCode | errorMessage                                                           |
      | INCORRECT_PASSWORD_UNBLOCK_USER_TC36 | 2000      | The password doesn't match the Login ID                                |
      | INCORRECT_PASSWORD_BLOCK_USER_TC37   | 2000      | The password doesn't match the Login ID. The Login ID has been locked. |

  @ResetPasswordForNotExpiredPassword @Phase1 @HappyFlow
  Scenario: ResetPassword Api - Verify ResetPassword Api with not expired password TC39
    Given update the failed login count to 1
    When a request is made to the ResetPassword Api with not expired password
    Then verify response code of "ResetPassword" Api is 200
    And verify if the failed login count is updated to 0
    And a request is made to the ResetPassword Api to set the old password again
    Then verify response code of "ResetPassword" Api is 200
    And verify if the failed login count is updated to 0

  @ResetPasswordForExpiredPassword @Phase1 @HappyFlow
  Scenario: ResetPassword Api - Verify ResetPassword Api with expired password TC40
    Given update the failed login count to 2
    When a request is made to the ResetPassword Api with expired password
    Then verify response code of "ResetPassword" Api is 200
    And verify if the failed login count is updated to 0
    And a request is made to the ResetPassword Api to set the old password again
    Then verify response code of "ResetPassword" Api is 200
    And verify if the failed login count is updated to 0

  @ResetPasswordForLockedOutAccount @Phase1 @NegativeFlow
  Scenario: ResetPassword Api - Verify ResetPassword Api for locked out account TC38
    Given update the locked indicator to "Y" and failed logins to 4
    When a request is made to the ResetPassword Api with locked out account details
    And response should have ErrorCode 2000 and ErrorMessage "Locked out Login ID"
    Then update the locked indicator to "Y" and failed logins to 0