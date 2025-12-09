Feature: Verify MeterSet SearchAccounts API

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @MeterSet @MS_SA @Positive @Phase2 @meterSet
  Scenario Outline: MeterSet SearchAccounts - Verify "<testCondition>" returns success
    When a request is made to the MeterSet SearchAccounts Api for "<testCondition>" condition
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "accountStatus" as "A"
    And response should have "recordType" as "BANNER RECORD"

    Examples:
      | testCondition                                     |
      | MS_SA_LAST_NAME_AND_ZIP_POSITIVE_TC001            |
      | MS_SA_FIRST_NAME_LAST_NAME_AND_ZIP_POSITIVE_TC002 |
