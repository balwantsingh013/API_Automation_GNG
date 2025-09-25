Feature: Verify SaveUnenrollment ServiceTransfer Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response


  @SaveUnenrollmentWithInvalidParameters @NegativeFlow @SaveUnenrollment @ServiceTransfer @Phase1
  Scenario Outline: Verify SaveUnenrollment ServiceTransfer with invalid parameters "<testCondition>"
    Given a request is made to get Marketer Reference Data
    When a request is made to the SaveUnenrollment ServiceTransfer Api with invalid parameters for "<testCondition>" condition
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                   | errorCode | errorMessage                                                                                                                               |
    # Request ID
      | REQUEST_ID_EMPTY_NEGATIVE_TC97                  | 10001     | Missing Request ID                                                                                                                         |
      | REQUEST_ID_MAX_LENGTH_NEGATIVE_TC98             | 10002     | Invalid Request ID                                                                                                                         |
      | REQUEST_ID_DUPLICATE_NEGATIVE_TC99              | 10003     | Duplicate Request ID                                                                                                                       |
    # Login ID
      | LOGIN_ID_EMPTY_NEGATIVE_TC100                   | 10000     | Missing Login ID                                                                                                                           |
      | LOGIN_ID_MAX_LENGTH_NEGATIVE_TC101              | 10000     | The Login ID must be a string with a maximum length of 30                                                                                  |
      | LOGIN_ID_INVALID_FORMAT_NEGATIVE_TC102          | 2000      | Invalid Login ID                                                                                                                           |
      | LOGIN_ID_NOT_EXISTS_NEGATIVE_TC103              | 2000      | Invalid Login ID                                                                                                                           |
    # Transaction Type
      | TRANSACTION_TYPE_EMPTY_NEGATIVE_TC104           | 10000     | Missing Transaction Type                                                                                                                   |
      | TRANSACTION_TYPE_MAX_LENGTH_NEGATIVE_TC105      | 10000     | The Transaction Type must be a string with a maximum length of 4                                                                           |
      | TRANSACTION_TYPE_INVALID_NEGATIVE_TC106         | 1000      | Invalid Request: Invalid Transaction Type                                                                                                  |
    # Customer Code
      | CUSTOMER_CODE_EMPTY_NEGATIVE_TC107              | 10000     | Missing Customer Code                                                                                                                      |
      | CUSTOMER_CODE_MAX_LENGTH_NEGATIVE_TC108         | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.customerCode \| LineNumber: 0 \| BytePositionInLine: 111.|
      | CUSTOMER_CODE_NON_INT_NEGATIVE_TC109            | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.customerCode \| LineNumber: 0 \| BytePositionInLine: 96. |
      | CUSTOMER_CODE_NOT_EXISTS_NEGATIVE_TC110         | 2000      | Invalid Request: Invalid Customer Code                                                                                                     |
    # Premises Code
      | PREMISES_CODE_EMPTY_NEGATIVE_TC111              | 10000     | Missing Premises Code                                                                                                                      |
      | PREMISES_CODE_MAX_LENGTH_NEGATIVE_TC112         | 10000     | The Premises Code must be a numeric string with a maximum length of 7                                                                      |
      | PREMISES_CODE_NOT_EXISTS_NEGATIVE_TC113         | 2000      | Invalid Request: Invalid Premises Code                                                                                                     |
         # AGLC Account
    | AGLC_ACCOUNT_EMPTY_NEGATIVE_TC115               | 10000     | Missing AGLC Account Number                                                                                                                |
    | AGLC_ACCOUNT_MAX_LENGTH_NEGATIVE_TC116          | 10000     | The AGLC Account Number must be a numeric string with a maximum length of 20                                                               |
  # Forwarding Address selector
    | FWD_ADDRESS_EMPTY_NEGATIVE_TC117                | 10000     | Missing Forwarding Address Is                                                                                                              |
    | FWD_ADDRESS_MAX_LENGTH_NEGATIVE_TC118           | 10000     | The Forwarding Address Is must have a maximum length of 2                                                                                  |
    | FWD_ADDRESS_INVALID_VALUE_NEGATIVE_TC119        | 2000      | Invalid Request: Invalid Forwarding Address Option                                                                                         |
  # Forwarding Address Type
    | FWD_ADDRESS_TYPE_INVALID_NEGATIVE_TC120         | 10000      | Invalid Request: Invalid Forwarding Address Type                                                                                           |
    | FWD_ADDRESS_TYPE_EMPTY_NEGATIVE_TC121           | 10000     | Missing Forwarding Address Type                                                                                                            |
    | FWD_ADDRESS_TYPE_INVALID_VALUE_NEGATIVE_TC122   | 10000     | The Forwarding Address Type must have a maximum length of 1                                                                                |
  # Address fields
    | FWD_ADD_STR_NUM_MAX_LENGTH_NEGATIVE_TC123       | 10000     | The Forwarding Address Street Number must have a maximum length of 12                                                                      |
    | FWD_ADD_STR_PRE_DIR_MAX_LENGTH_NEGATIVE_TC124   | 10000     | The Forwarding Address Street Pre Direction must have a maximum length of 2                                                                |
    | FWD_ADD_STR_NAME_EMPTY_NEGATIVE_TC125           | 10000     | Missing Forwarding Address Street Name                                                                                                     |
    | FWD_ADD_STR_NAME_MAX_LENGTH_NEGATIVE_TC126      | 10000     | The Forwarding Address Street Name must have a maximum length of 30                                                                        |
    | FWD_ADD_STR_SFX_MAX_LENGTH_NEGATIVE_TC127       | 10000     | The Forwarding Address Street Suffix must have a maximum length of 6                                                                       |
    | FWD_ADD_STR_POST_DIR_MAX_LENGTH_NEGATIVE_TC128  | 10000     | The Forwarding Address Street Post Direction must have a maximum length of 2                                                               |
                                                                                          |



  @SaveUnenrollmentWithSearchInvalidParameters @NegativeFlow @SaveUnenrollment @ServiceTransfer @Phase1
  Scenario Outline: Verify SaveUnenrollment ServiceTransfer invalid with prior GetEligiblePlansAndOffers "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to get Marketer Reference Data
    When a request is made to the SaveUnenrollment ServiceTransfer Api with invalid parameters for "<testCondition>" condition
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                   | errorCode | errorMessage                     |
      | ACCOUNT_COMBINATION_INVALID_NEGATIVE_TC114      | 2000      | Invalid Request: Invalid Account |
