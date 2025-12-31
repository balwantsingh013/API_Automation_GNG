Feature: Verify UpdateUsername Api

  @UpdateUsername @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition>"
    Then verify response code of "UpdateUsername" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have success as "<success>"

    Examples:
      | testCondition                                                        | errorCode | errorMessage                               | success |
      | TC_28__Negative__Missing_Request_ID                                  | 10001     | Missing Request ID                         | false   |
      | TC_29__Negative__Invalid_Request_ID__Length                          | 10002     | Invalid Request ID                         | false   |
      | TC_30__Negative__Duplicate_Request_ID                                | 10003     | Duplicate Request ID                       | false   |
      | TC_31__Negative__Missing_Username                                    | 10349     | Missing Username                           | false   |
      | TC_32__Negative__Invalid_Username__Inactive                          | 10355     | Inactive Username                          | false   |
      | TC_33__Negative__Invalid_Username_format__Length___Too_Short____     | 10351     | Invalid Username Length                    | false   |
      | TC_34__Negative__Invalid_Username_format__Length___Too_Long____      | 10351     | Invalid Username Length                    | false   |
      | TC_35__Negative__Invalid_Username_format__Alphanumeric               | 10353     | Invalid Username Format                    | false   |
      | TC_36__Negative__Invalid_Username__Not_Found                         | 10357     | Username Not Found                         | false   |
      | TC_37__Negative__Missing_Password                                    | 10113     | Missing Password                           | false   |
      | TC_38__Negative__Invalid_Password_Format__Length___Too_Short____     | 10157     | Invalid Password Length. The password must be between 8 and 64 characters. | false |
      | TC_39__Negative__Invalid_Password_Format__Length___Too_Long____      | 10157     | Invalid Password Length. The password must be between 8 and 64 characters. | false |
      | TC_40__Negative__Invalid_Password_Format___Not_A_String____          | 10157     | Invalid Password Format                    | false   |
      | TC_41__Negative__Invalid_Password_Format__Policy_Requirement___s____ | 10157     | Invalid Password Format                    | false   |
      | TC_42__Negative__Account_username_already_exists                     | 10361     | The account is already linked to a username| false   |
      | TC_43__Negative__Password_does_not_match_username                    | 10363     | The password doesn’t match the username    | false   |
      | TC_44__Negative__Missing_customerCode                                | 10011     | Missing Customer Code                      | false   |
      | TC_45__Negative__Invalid_customerCode_Length                         | 10015     | Invalid Customer Code Format               | false   |
      | TC_46__Negative__Invalid_customerCode_Format                         | 10015     | Invalid Customer Code Format               | false   |
      | TC_47__Negative__Invalid_customerCode                                | 40015     | Invalid Account Number                     | false   |
      | TC_48__Negative__Missing_premisesCode                                | 10013     | Missing Premises Code                      | false   |
      | TC_49__Negative__Invalid_premisesCode_Length                         | 10005     | Invalid Premises Code Format               | false   |
      | TC_50__Negative__Invalid_premisesCode_Format                         | 10005     | Invalid Premises Code Format               | false   |
      | TC_51__Negative__Invalid_premisesCode                                | 40015     | Invalid Account Number                     | false   |

  @UpdateUsernamePositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateUsername Api for "<testCondition>"
    Then verify response code of "UpdateUsername" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have success as "<success>"

    Examples:
      | testCondition                                | errorCode | errorMessage | success |
      | TC_52__Positive__Username_Available          | 0         | null         | true    |
      | TC_53__Positive__Username_Active             | 0         |              | true    |
