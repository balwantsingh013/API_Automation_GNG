Feature: Verify SaveUnenrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @HappyFlow @SaveUnenrollment
  Scenario Outline: <TestId> Verify SaveUnenrollment Api Positive test case
    When a request is made to the SaveUnenrollment Api for account with "<pricePlan>" plan "<accountType>" type with forwardingAddressIs "<forwardingAddressIs>" with type "<addressType>" and turnoffreason "<turnoffreason>" and setEmail "<setEmail>" with etcExists "<etcExists>"
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
    |TestId|pricePlan|accountType|forwardingAddressIs|addressType|turnoffreason                    |setEmail|etcExists        |
    |TC-207|MVS      |RS         |CA                 |not present|Seasonal or Heat Only            |false      |false         |
    |TC-208|MI       |RS         |NA                 |S          |Moving Outside AGLC              |true       |false         |
    |TC-209|MAP      |RS         |NA                 |R          |Others Military                  |true       |false         |
    |TC-210|CSV      |RS         |NA                 |P          |REAP Realtor Inspection          |false      |false         |
    |TC-214|PRP      |RS         |NA                 |S          |Household Account Change         |true       |false         |
    |TC-215|TRD      |RS         |CA                 |not present|Other financial situation        |true       |false         |
    |TC-216|MVS      |SR         |CA                 |not present|Moving Not Staying with GNG      |false      |false         |
    |TC-219|MVS      |RS         |CA                 |not present|Other Regulated Provider         |false      |false         |
    |TC-223|CCV      |CM         |CA                 |not present|Seasonal or Heat Only            |false      |false         |
    |TC-222|VML      |RS         |CA                 |not present|Other Deceased                   |false      |false         |
    |TC-225|CGB      |CM         |CA                 |not present|Other financial situation        |false      |true          |
    |TC-227|CMI      |CM         |NA                 |S          |REAP Realtor Inspection          |true       |false         |
    |TC-228|CSV      |CM         |CA                 |not present|Moving Service Transfer          |false      |false         |
    |TC-229|CGB      |CM         |CA                 |not present|Moving Not Staying with GNG      |false      |true          |
    |TC-230|CVS      |CM         |CA                 |not present|Moving Outside AGLC              |false      |false         |
    |TC-232|CMI      |CM         |NA                 |S          |Moving Service Transfer          |true       |false         |

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