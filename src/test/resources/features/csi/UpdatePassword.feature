Feature: Verify UpdatePassword Api

#  @UpdatePassword @NegativeFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to UpdatePassword Api for "<testCondition>"
#    Then verify response code of "UpdatePassword" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#
#    Examples:
#      | testCondition                                                    | errorCode | errorMessage                                                              |
#      | TC_13__Negative__Missing_Request_ID                              | 10001     | Missing Request ID                                                        |
#      | TC_14__Negative__Invalid_Request_ID__Length                      | 10002     | Invalid Request ID                                                        |
#      | TC_15__Negative__Duplicate_Request_ID                            | 10003     | Duplicate Request ID                                                      |
#      | TC_16__Negative__Missing_Username                                | 10349     | Missing Username                                                          |
#      | TC_17__Negative__Invalid_Username_format__Length___Too_Short____ | 10351     | Invalid Username Length                                                   |
#      | TC_18__Negative__Invalid_Username_format__Length___Too_Long____  | 10351     | Invalid Username Length                                                   |
#      | TC_19__Negative__Invalid_Username_format__Alphanumeric           | 10353     | Invalid Username Format                                                   |
#      | TC_20__Negative__Invalid_Username__Not_Found                     | 10357     | Username Not Found                                                        |
#      | TC_21__Negative__Invalid_Username__Inactive                      | 10355     | Inactive Username                                                         |
#      | TC_22__Negative__Invalid_Username__Inactive                      | 10355     | Inactive Username                                                         |
#      | TC_23__Negative__Missing_Password                                | 10113     | Missing Password                                                          |
#      | TC_24__Negative__Invalid_Password_Format__Length___Too_Short____ | 10157     | Invalid Password Length. The password must be between 8 and 64 characters.|
#      | TC_25__Negative__Invalid_Password_Format__Length___Too_Long____  | 10157     | Invalid Password Length. The password must be between 8 and 64 characters.|
#      | TC_26__Negative__Invalid_Password__Reused_Password               | 10359     | Invalid Password. The provided password matches the existing one.           |

  @UpdatePasswordPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdatePassword Api for "<testCondition>"
    Then verify response code of "UpdatePassword" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                      | errorCode | errorMessage |
      | TC_27__Positive__Password__Updated | 0         |              |
