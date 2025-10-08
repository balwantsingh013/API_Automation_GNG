Feature: Verify SearchAccounts ServiceTransfer Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response


  @SearchAccountsServiceTransfer @Phase1 @NegativeFlow
  Scenario Outline: SearchAccountsApiServiceTransfer Api - Verify the "<testCondition>" for searchAccounts API
    When a request is made to the SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                         | errorCode | errorMessage                                                                                                                                                                     |
      |SEARCH_ACCOUNTS_NO_SEARCH_PARAMETERS_PROVIDED_TC_1     |10116      |At least one of the following is required: GNG Account Number, Business Name, First and Last Name (with Zip Code), Social Security Number, AGLC Account Number or Premise Address.|
      |SEARCH_ACCOUNTS_MISSING_REQUEST_ID_TC_2                |10001      |Missing Request ID                                                                                                                                                                |
      |SEARCH_ACCOUNTS_INVALID_REQUEST_ID_LENGTH_TC_3         |10002      |Invalid Request ID                                                                                                                                                                |
      |SEARCH_ACCOUNTS_DUPLICATE_REQUEST_ID_TC_4              |10003      |Duplicate Request ID                                                                                                                                                              |
      |SEARCH_ACCOUNTS_MISSING_LOGIN_ID_TC_5                  |10112      |Invalid or missing Login ID                                                                                                                                                       |
      |SEARCH_ACCOUNTS_INVALID_LOGIN_ID_LENGTH_TC_6           |10112      |Invalid or missing Login ID                                                                                                                                                       |
      |SEARCH_ACCOUNTS_INVALID_LOGIN_ID_FORMAT_TC_7           |10112      |Invalid or missing Login ID                                                                                                                                                       |
      |SEARCH_ACCOUNTS_INVALID_LOGIN_ID_TC_8                  |2000       |Invalid Login ID                                                                                                                                                                  |
      |SEARCH_ACCOUNTS_MISSING_CUSTOMER_CODE_TC_9             |1001       |Invalid Request: Invalid required search field combination - customerCode required.                                                                                               |
      |SEARCH_ACCOUNTS_MISSING_PREMISES_CODE_TC_10            |1001       |Invalid Request: Invalid required search field combination - premisesCode required.                                                                                               |
      |SEARCH_ACCOUNTS_INVALID_CUSTOMER_CODE_LENGTH_TC_11     |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |SEARCH_ACCOUNTS_INVALID_PREMISES_CODE_LENGTH_TC_12     |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |SEARCH_ACCOUNTS_MISSING_TRANSACTION_TYPE_TC_13         |10113      |Invalid or missing Transaction Type                                                                                                                                               |
      |SEARCH_ACCOUNTS_INVALID_TRRANSACTION_TYPE_TC_14        |10113      |Invalid or missing Transaction Type                                                                                                                                               |
      |SEARCH_ACCOUNTS_INVALID_TRANSACTION_TYPR_LENGTH_TC_15  |10113      |Invalid or missing Transaction Type                                                                                                                                               |
      |SEARCH_ACCOUNTS_INVALID_BUSINESS_NAME_LENGTH_TC_16     |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |SEARCH_ACCOUNTS_INVALID_CUSTOMER_LAST_NAME_LENGTH_TC_17|10115      |Invalid search parameter(s)                                                                                                                                                       |
      |SEARCH_ACCOUNTS_MISSING_PREMISES_ZIP_CODE_TC_19        |1001       |Invalid Request: Invalid required search field combination - premisesZipCode required.                                                                                            |
      |SEARCH_ACCOUNTS_MISSING_TRANSACTION_TYPE_TC_20         |10113      |Invalid or missing Transaction Type                                                                                                                                               |
      |SEARCH_ACCOUNTS_INVALID_FIRST_NAME_LENGTH_TC_21        |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |SEARCH_ACCOUNTS_UNENCRYPTED_SSN_TC_22                  |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |SEARCH_ACCOUNTS_INVALID_SSN_LENGTH_TC_23               |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |SEARCH_ACCOUNTS_TAX_ID_NOT_ALLOWED_TC_24               |2200       |Parameter Value should be null-Federal Tax ID                                                                                                                                     |
      |SEARCH_ACCOUNTS_SSN_AND_TAX_ID_PROVIDED_TC_25          |2200       |Parameter Value should be null-Federal Tax ID                                                                                                                                     |
      |SEARCH_ACCOUNTS_PHONE_NOT_ALLOWED_TC_26                |2200       |Parameter Value should be null-Phone Number                                                                                                                                       |
      |SEARCH_ACCOUNTS_INVALID_AGLC_ACCOUNT_NO_LENGTH_TC_27   |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |SEARCH_ACCOUNTS_NON_NUMERIC_AGLC_ACCOUNT_NO_TC_28      |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |SEARCH_ACCOUNTS_INVALID_STREET_NUMBER_LENGTH_TC_29     |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |SEARCH_ACCOUNTS_INVALID_STREET_PRE_DIR_LENGTH_TC_30    |10115      |Invalid search parameter(s)                                                                                                                                                       |
      |SEARCH_ACCOUNTS_INVALID_STREET_NAME_LENGTH_TC_31       |10115      |Invalid search parameter(s)                                                                                                                                                       |