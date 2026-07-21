Feature: Practice - Verify UpdatePaperlessCommunications Api (CSI phase3 style)

  # TC_53 - Negative - Missing Request ID
  # Verify that if the "requestID" parameter is missing in the UpdatePaperlessCommunications
  # endpoint request, the endpoint response will return data = null, success = false,
  # ErrorCode = 10001, ErrorMessage = 'Missing Request ID'

  @UpdatePaperlessCommunicationsPractice @NegativeFlow @Practice @CSI
  Scenario Outline: Practice UpdatePaperlessCommunications negative flow - missing request ID for "<testCondition>"
    When a practice request is made to UpdatePaperlessCommunications Api for "<testCondition>"
    Then verify response code of "UpdatePaperlessCommunications" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                            | errorCode | errorMessage       |
      | TC_53__Negative__Missing_Request_ID_     | 10001     | Missing Request ID |
