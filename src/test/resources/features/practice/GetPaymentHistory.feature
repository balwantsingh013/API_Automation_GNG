Feature: Practice - Verify GetPaymentHistory Api (CSI phase2 style)

  @GetPaymentHistoryPractice @HappyFlow @Practice @CSI
  Scenario Outline: Practice GetPaymentHistory positive flow for "<testCondition>"
    When a practice request is made to GetPaymentHistory Api for "<testCondition>"
    Then verify response code of "GetPaymentHistory" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And payment history response should match database for customer and premises code

    Examples:
      | testCondition                           | errorCode | errorMessage |
      | TC_141__Positive__Payment_Date_Format   | 0         |              |

  @GetPaymentHistoryPractice @NegativeFlow @Practice @CSI
  Scenario Outline: Practice GetPaymentHistory negative flow for "<testCondition>"
    When a practice request is made to GetPaymentHistory Api for "<testCondition>"
    Then verify response code of "GetPaymentHistory" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                 | errorCode | errorMessage                 |
      | TC_126__Negative__Missing_Request_ID          | 10001     | Missing Request ID           |
      | TC_130__Negative__Invalid_customerCode_Length | 10015     | Invalid Customer Code Format |
