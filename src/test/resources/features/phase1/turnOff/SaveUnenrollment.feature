Feature: Verify SaveUnenrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @HappyFlow @SaveUnenrollment @test
  Scenario Outline: SaveUnenrollmentApi - Verify SaveUnenrollment Api positive flow <testCondition>
    Given a request is made to get Marketer Reference Data
    When a request is made to the SaveUnenrollment Api for account with "<pricePlan>" plan "<accountType>" type with forwardingAddressIs "<forwardingAddressIs>" with type "<addressType>" and turnoffreason "<testCondition>" and setEmail "<setEmail>" with etcExists "<etcExists>"
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
    |pricePlan|accountType|forwardingAddressIs|addressType|testCondition                                  |setEmail   |etcExists     |
    |MVS      |RS         |CA                 |not present|SEASONAL_OR_HEAT_ONLY_TC207                    |false      |false         |
    |MI       |RS         |NA                 |S          |MOVING_OUTSIDE_AGLC_TC208                      |true       |false         |
    |MAP      |RS         |NA                 |R          |OTHER_MILITARY_TC209                           |true       |false         |
    |CSV      |RS         |NA                 |P          |REAP_REALTOR_INSPECTION_TC210                  |false      |false         |
    |TRD      |RS         |CA                 |not present|OTHER_FINANCIAL_SITUATION_TC215                |true       |false         |
    |MVS      |SR         |CA                 |not present|MOVING_NOT_STAYING_WITH_GNG_TC216              |false      |false         |
    |MVS      |RS         |CA                 |not present|OTHER_REGULATED_PROVIDER_TC219                 |false      |false         |
    |CCV      |CM         |CA                 |not present|SEASONAL_OR_HEAT_ONLY_TC223                    |false      |false         |
    |VML      |RS         |CA                 |not present|OTHER_DECEASED_TC222                           |false      |false         |
    |CGB      |CM         |CA                 |not present|OTHER_FINANCIAL_SITUATION_TC225                |false      |true          |
    |CMI      |CM         |NA                 |S          |REAP_REALTOR_INSPECTION_TC227                  |true       |false         |
    |CSV      |CM         |CA                 |not present|MOVING_SERVICE_TRANSFER_TC228                  |false      |false         |
    |CGB      |CM         |CA                 |not present|MOVING_NOT_STAYING_WITH_GNG_TC229              |false      |true          |
    |CVS      |CM         |CA                 |not present|MOVING_OUTSIDE_AGLC_TC230                      |false      |false         |
    |CMI      |CM         |NA                 |S          |MOVING_SERVICE_TRANSFER_TC232                  |true       |false         |
    |GPP      |SR         |NA                 |S          |MOVING_OUTSIDE_ETC_WAIVED_TC211                |false      |true          |
    |RGB      |SR         |NA                 |S          |MOVING_SERVICE_TRANSFER_ETC_WAIVED_TC_213      |true       |true          |
    |18M      |RS         |NA                 |R          |OTHER_MILITARY_ETC_WAIVED_TC_217               |false      |true          |
    |24M      |RS         |NA                 |P          |MOVING_OUTSIDE_AGLC_ETC_WAIVED_TC_218          |false      |true          |
    |GPP      |RS         |CA                 |not present|OTHER_DECEASED_ETC_WAIVED_TC_220               |false      |true          |
    |RF6      |RS         |CA                 |not present|OTHER_REGULATED_PROVIDER_ETC_WAIVED_TC212      |true       |true          |
    |VML      |RS         |CA                 |not present|OTHER_RENOVATION_ELECTRIC_CONVERSION_TC221     |false      |false         |
    |CFM      |CM         |CA                 |not present|OTHER_RENOVATION_ELECTRIC_CONVERSION_TC224     |false      |true          |
    |CF6      |CM         |CA                 |not present|OTHER_MILITARY_ETC_WAIVED_TC_231               |false      |true          |
    |PRP      |RS         |NA                 |S          |HOUSEHOLD_ACCOUNT_CHANGE_TC_214                |true       |false         |
    |INX      |CM         |NA                 |S          |MOVING_OUTSIDE_POOL_GROUP_TC_226               |false      |false         |

  @SaveUnenrollmentWithInvalidRequestID @NegativeFlow @SaveUnenrollment
  Scenario Outline: Verify SaveUnenrollment Api with invalid requestID "<requestID>"
    Given a request is made to get Marketer Reference Data
    When a request is made to the SaveUnenrollment Api with "<requestID>"
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID            | errorCode | errorMessage         |
      | EMPTY_REQUEST_ID     | 10001     | Missing Request ID   |
      | DUPLICATE_REQUEST_ID | 10003     | Duplicate Request ID |
      | LONG_REQUEST_ID      | 10002     | Invalid Request ID   |

