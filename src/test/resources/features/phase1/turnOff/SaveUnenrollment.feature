Feature: Verify SaveUnenrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @HappyFlow @SaveUnenrollment @tcabcd
  Scenario Outline: Verify SaveUnenrollment Api Positive test cases
    When a request is made to the SaveUnenrollment Api for account with "<pricePlan>" plan "<accountType>" type with forwardingAddressIs "<forwardingAddressIs>" with type "<addressType>" and turnoffreason "<turnoffreason>" and setEmail "<setEmail>" with etcExists "<etcExists>"
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Examples:
    |pricePlan|accountType|forwardingAddressIs|addressType|turnoffreason                    |setEmail|etcExists     |
    |MVS      |RS         |CA                 |not present|Seasonal or Heat Only            |No      |false         |
    |MI       |RS         |NA                 |S          |Moving Outside AGLC              |Yes     |false         |
    |MAP      |RS         |NA                 |R          |Others Military                  |Yes     |false         |
    |CSV      |RS         |NA                 |P          |REAP Realtor Inspection          |No      |false         |
    |PRP      |RS         |NA                 |S          |Household Account Change         |Yes     |false         |
    |TRD      |RS         |CA                 |not present|Other financial situation        |Yes     |false         |
    |MVS      |SR         |CA                 |not present|Moving Not Staying with GNG      |No      |false         |
    |MVS      |RS         |CA                 |not present|Other Regulated Provider         |No      |false         |
    |CCV      |CM         |CA                 |not present|Seasonal or Heat Only            |No      |false         |
    |VML      |RS         |CA                 |not present|Other Deceased                   |No      |false         |
    |CGB      |CM         |CA                 |not present|Other financial situation        |No      |true          |
    |CMI      |CM         |NA                 |S          |REAP Realtor Inspection          |Yes     |false         |
    |CSV      |CM         |CA                 |not present|Moving Service Transfer          |No      |false         |
    |CGB      |CM         |CA                 |not present|Moving Not Staying with GNG      |No      |true          |
    |CVS      |CM         |CA                 |not present|Moving Outside AGLC              |No      |false         |
    |CMI      |CM         |NA                 |S          |Moving Service Transfer          |Yes     |false         |

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