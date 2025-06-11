Feature: Verify SaveUnenrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @HappyFlow @SaveUnenrollment @123
  Scenario Outline: SaveUnenrollmentApi - Verify SaveUnenrollment Api positive flow <testCondition>
    When a request is made to the SaveUnenrollment Api for account with "<pricePlan>" plan "<accountType>" type with forwardingAddressIs "<forwardingAddressIs>" with type "<addressType>" and turnoffreason "<testCondition>" and setEmail "<setEmail>" with etcExists "<etcExists>"
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
    |pricePlan|accountType|forwardingAddressIs|addressType|testCondition                    |setEmail   |etcExists     |
    |MVS      |RS         |CA                 |not present|SEASONAL_OR_HEAT_ONLY_TC207      |false      |false         |
    |MI       |RS         |NA                 |S          |MOVING_OUTSIDE_AGLC_TC208        |true       |false         |
    |MAP      |RS         |NA                 |R          |OTHERS_MILITARY_TC209            |true       |false         |
    |CSV      |RS         |NA                 |P          |REAP_REALTOR_INSPECTION_TC210    |false      |false         |
    |TRD      |RS         |CA                 |not present|OTHER_FINANCIAL_SITUATION_TC215  |true       |false         |
    |MVS      |SR         |CA                 |not present|MOVING_NOT_STAYING_WITH_GNG_TC216|false      |false         |
    |MVS      |RS         |CA                 |not present|OTHER_REGULATED_PROVIDER_TC219   |false      |false         |
    |CCV      |CM         |CA                 |not present|SEASONAL_OR_HEAT_ONLY_TC223      |false      |false         |
    |VML      |RS         |CA                 |not present|OTHER_DECEASED_TC222             |false      |false         |
    |CGB      |CM         |CA                 |not present|OTHER_FINANCIAL_SITUATION_TC225  |false      |true          |
    |CMI      |CM         |NA                 |S          |REAP_REALTOR_INSPECTION_TC227    |true       |false         |
    |CSV      |CM         |CA                 |not present|MOVING_SERVICE_TRANSFER_TC228    |false      |false         |
    |CGB      |CM         |CA                 |not present|MOVING_NOT_STAYING_WITH_GNG_TC229|false      |true          |
    |CVS      |CM         |CA                 |not present|MOVING_OUTSIDE_AGLC_TC230        |false      |false         |
    |CMI      |CM         |NA                 |S          |MOVING_SERVICE_TRANSFER_TC232    |true       |false         |

  @SaveUnenrollmentWithInvalidRequestID @NegativeFlow @SaveUnenrollment
  Scenario Outline: Verify SaveUnenrollment Api with invalid requestID "<requestID>"
    When a request is made to the SaveUnenrollment Api with "<requestID>"
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID            | errorCode | errorMessage         |
      | EMPTY_REQUEST_ID     | 10001     | Missing Request ID   |
      | DUPLICATE_REQUEST_ID | 10003     | Duplicate Request ID |
      | LONG_REQUEST_ID      | 10002     | Invalid Request ID   |