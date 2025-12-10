Feature: Verify MarketSwitch GetMarketerCodes API

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetMarketerCodesMarketerSwitchNegative @NegativeFlow @Phase2
  Scenario Outline: Verify market switch GetMarketerCodes with invalid parameters "<testCondition>"
    When a request is made to the GetMarketerCodes Api negative marketerSwitch for "<testCondition>" condition
    Then verify response code of "GetMarketerCodes" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                                 | errorCode | errorMessage                                               |
      | GM_MRK_SWT_DUPLICATE_REQUEST_ID_TC_59                         | 10003     | Duplicate Request ID                                       |
      | GM_MRK_SWT_MISSING_REQUEST_ID_TC_60                           | 10001     | Missing Request ID                                         |
      | GM_MRK_SWT_MAX_LENGTH_REQUEST_ID_TC_61                        | 10002     | Invalid Request ID                                         |
      | GM_MRK_SWT_MISSING_LOGIN_ID_TC_62                             | 10000     | Missing Login ID                                           |
      | GM_MRK_SWT_MAX_LENGTH_LOGIN_ID_TC_63                          | 10000      | The Login ID must be a string with a maximum length of 30 |
      | GM_MRK_SWT_NOT_ALPHA_NUM_LOGIN_ID_TC_64                       | 2000      | Invalid Login ID                                           |
      | GM_MRK_SWT_INVALID_LOGIN_ID_TC_65                             | 2000      | Invalid Login ID                                           |


