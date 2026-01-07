Feature: Verify UpdateAccountNickname Api

  @UpdateAccountNickname @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateAccountNickname Api for "<testCondition>"
    Then verify response code of "UpdateAccountNickname" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                         | errorCode | errorMessage                                |
      | TC_60__Negative__Missing_Request_ID                   | 10001     | Missing Request ID                          |
      | TC_61__Negative__Invalid_Request_ID__Length           | 10002     | Invalid Request ID                          |
      | TC_62__Negative__Duplicate_Request_ID                 | 10003     | Duplicate Request ID                        |
      | TC_63__Negative__Missing_CustomerCode                 | 10011     | Missing Customer Code                       |
      | TC_64__Negative__Invalid_CustomerCode_Length          | 10015     | Invalid Customer Code Format                |

      | TC_65__Negative__Invalid_CustomerCode_Format__Not_String | 10015  | Invalid Customer Code Format                |
      | TC_66__Negative__Invalid_CustomerCode__Not_Found      | 40015     | Invalid Account Number                      |
      | TC_67__Negative__Missing_PremisesCode                 | 10013     | Missing Premises Code                       |
      | TC_68__Negative__Invalid_PremisesCode_Length          | 10005     | Invalid Premises Code Format                |
      | TC_69__Negative__Invalid_PremisesCode_Format__Not_String | 10005  | Invalid Premises Code Format                |

      | TC_70__Negative__Nickname_Not_Allowed_For_New_Account | 40215     | Nickname not allowed for new account        |
      | TC_71__Negative__Nickname_Already_Exists              | 40217     | Nickname already exists for the account     |
      | TC_72__Negative__Nickname_Missing                     | 40219     | Nickname does not exist in the request      |

  @UpdateAccountNicknamePositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateAccountNickname Api for "<testCondition>"
    Then verify response code of "UpdateAccountNickname" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                     | errorCode | errorMessage |
      | TC_73__Positive__Nickname_Set__Active | 0         |              |
      | TC_74__Positive__Nickname_Set__Final | 0         |              |
      | TC_75__Positive__Nickname_Set__Inactive | 0         |              |
      | TC_76__Positive__Nickname_Updated__Active | 0         |              |
      | TC_77__Positive__Nickname_Updated__Final | 0         |              |
      | TC_78__Positive__Nickname_Updated__Inactive | 0         |              |
      | TC_79__Positive__Nickname_Removed__Active | 0         |              |
      | TC_80__Positive__Nickname_Removed__Final | 0         |              |
      | TC_81__Positive__Nickname_Removed__Inactive | 0         |              |


  @UpdateAccountNicknamePositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdateAccountNickname Api for "<testCondition>"
    Then verify response code of "UpdateAccountNickname" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And verify if login id is saved

    Examples:
      | testCondition                     | errorCode | errorMessage |
      | TC_82__Positive__LoginID_Saved    | 0         |              |
