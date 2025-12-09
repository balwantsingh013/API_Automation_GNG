Feature: Verify SaveUnenrollment ServiceTransfer Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @SaveUnenrollmentWithInvalidParameters @NegativeFlow @SaveUnenrollment @serviceTransfer @Phase1
  Scenario Outline: Verify SaveUnenrollment ServiceTransfer with invalid parameters "<testCondition>"
    Given a request is made to get Marketer Reference Data
    When a request is made to the SaveUnenrollment ServiceTransfer Api with invalid parameters for "<testCondition>" condition
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                    | errorCode | errorMessage                                                                                                                                               |
      # Request ID
      | REQUEST_ID_EMPTY_NEGATIVE_TC97                                   | 10001     | Missing Request ID                                                                                                                                         |
      | REQUEST_ID_MAX_LENGTH_NEGATIVE_TC98                              | 10002     | Invalid Request ID                                                                                                                                         |
      | REQUEST_ID_DUPLICATE_NEGATIVE_TC99                               | 10003     | Duplicate Request ID                                                                                                                                       |
      # Login ID
      | LOGIN_ID_EMPTY_NEGATIVE_TC100                                    | 10000     | Missing Login ID                                                                                                                                           |
      | LOGIN_ID_MAX_LENGTH_NEGATIVE_TC101                               | 10000     | The Login ID must be a string with a maximum length of 30                                                                                                  |
      | LOGIN_ID_INVALID_FORMAT_NEGATIVE_TC102                           | 2000      | Invalid Login ID                                                                                                                                           |
      | LOGIN_ID_NOT_EXISTS_NEGATIVE_TC103                               | 2000      | Invalid Login ID                                                                                                                                           |
      # Transaction Type
      | TRANSACTION_TYPE_EMPTY_NEGATIVE_TC104                            | 10000     | Missing Transaction Type                                                                                                                                   |
      | TRANSACTION_TYPE_MAX_LENGTH_NEGATIVE_TC105                       | 10000     | The Transaction Type must be a string with a maximum length of 4                                                                                           |
      | TRANSACTION_TYPE_INVALID_NEGATIVE_TC106                          | 1000      | Invalid Request: Invalid Transaction Type                                                                                                                  |
      # Customer Code
      | CUSTOMER_CODE_EMPTY_NEGATIVE_TC107                               | 10000     | Missing Customer Code                                                                                                                                      |
      | CUSTOMER_CODE_MAX_LENGTH_NEGATIVE_TC108                          | 10000     | The Customer Code must be an integer with a maximum length of 9                                                                                            |
      | CUSTOMER_CODE_NON_INT_NEGATIVE_TC109                             | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.customerCode \| LineNumber: 0 \| BytePositionInLine: 96.                 |
      | CUSTOMER_CODE_NOT_EXISTS_NEGATIVE_TC110                          | 2000      | Invalid Request: Invalid Customer Code                                                                                                                     |
      # Premises Code
      | PREMISES_CODE_EMPTY_NEGATIVE_TC111                               | 10000     | Missing Premises Code                                                                                                                                      |
      | PREMISES_CODE_MAX_LENGTH_NEGATIVE_TC112                          | 10000     | The Premises Code must be a numeric string with a maximum length of 7                                                                                      |
      | PREMISES_CODE_NOT_EXISTS_NEGATIVE_TC113                          | 2000      | Invalid Request: Invalid Premises Code                                                                                                                     |
      # AGLC Account
      | AGLC_ACCOUNT_EMPTY_NEGATIVE_TC115                                | 10000     | Missing AGLC Account Number                                                                                                                                |
      | AGLC_ACCOUNT_MAX_LENGTH_NEGATIVE_TC116                           | 10000     | The AGLC Account Number must be a numeric string with a maximum length of 20                                                                               |
      # Forwarding Address selector
      | FWD_ADDRESS_EMPTY_NEGATIVE_TC117                                 | 10000     | Missing Forwarding Address Is                                                                                                                              |
      | FWD_ADDRESS_MAX_LENGTH_NEGATIVE_TC118                            | 10000     | The Forwarding Address Is must have a maximum length of 2                                                                                                  |
      | FWD_ADDRESS_INVALID_VALUE_NEGATIVE_TC119                         | 2000      | Invalid Request: Invalid Forwarding Address Option                                                                                                         |
      # Forwarding Address Type
      | FWD_ADDRESS_TYPE_INVALID_NEGATIVE_TC120                          | 10000     | Missing Forwarding Address Type                                                                                                                            |
      | FWD_ADDRESS_TYPE_EMPTY_NEGATIVE_TC121                            | 10000     | Missing Forwarding Address Type                                                                                                                            |
      | FWD_ADDRESS_TYPE_INVALID_VALUE_NEGATIVE_TC122                    | 10000     | The Forwarding Address Type must have a maximum length of 1                                                                                                |
      # Address fields
      | FWD_ADD_STR_NUM_MAX_LENGTH_NEGATIVE_TC123                        | 10000     | The Forwarding Address Street Number must have a maximum length of 12                                                                                      |
      | FWD_ADD_STR_PRE_DIR_MAX_LENGTH_NEGATIVE_TC124                    | 10000     | The Forwarding Address Street Pre Direction must have a maximum length of 2                                                                                |
      | FWD_ADD_STR_NAME_EMPTY_NEGATIVE_TC125                            | 10000     | Missing Forwarding Address Street Name                                                                                                                     |
      | FWD_ADD_STR_NAME_MAX_LENGTH_NEGATIVE_TC126                       | 10000     | The Forwarding Address Street Name must have a maximum length of 30                                                                                        |
      | FWD_ADD_STR_SFX_MAX_LENGTH_NEGATIVE_TC127                        | 10000     | The Forwarding Address Street Suffix must have a maximum length of 6                                                                                       |
      | FWD_ADD_STR_POST_DIR_MAX_LENGTH_NEGATIVE_TC128                   | 10000     | The Forwarding Address Street Post Direction must have a maximum length of 2                                                                               |
      | FWD_ADD_UNIT_TYPE_MAX_LENGTH_NEGATIVE_TC129                      | 10000     | The Forwarding Address Unit Type must have a maximum length of 6                                                                                           |
      | FWD_ADD_UNIT_NUM_MAX_LENGTH_NEGATIVE_TC130                       | 10000     | The Forwarding Address Unit Number must have a maximum length of 6                                                                                         |
      | FWD_ADD_RURAL_ROUTE_MAX_LENGTH_NEGATIVE_TC131                    | 10000     | The Forwarding Address Rural Route must have a maximum length of 20                                                                                        |
      | FWD_ADD_PO_BOX_MAX_LENGTH_NEGATIVE_TC132                         | 10000     | The Forwarding Address PO Box must have a maximum length of 10                                                                                             |
      | FWD_ADD_LINE2_MAX_LENGTH_NEGATIVE_TC133                          | 10000     | Forwarding Address Line 2 must have a maximum length of 30                                                                                                 |
      | FWD_ADD_CITY_EMPTY_NEGATIVE_TC134                                | 10000     | Missing Forwarding Address City                                                                                                                            |
      | FWD_ADD_CITY_MAX_LENGTH_NEGATIVE_TC135                           | 10000     | The Forwarding Address City must have a maximum length of 20                                                                                               |
      | FWD_ADD_STATE_CODE_EMPTY_NEGATIVE_TC136                          | 10000     | Missing Forwarding Address State Code                                                                                                                      |
      | FWD_ADD_STATE_CODE_MAX_LENGTH_NEGATIVE_TC137                     | 10000     | The Forwarding Address State Code must have a maximum length of 3                                                                                          |
      | FWD_ADD_STATE_CODE_INVALID_NEGATIVE_TC138                        | 2000      | Invalid Request: Invalid Forwarding State Code                                                                                                             |
      | FWD_ADD_ZIP_CODE_EMPTY_NEGATIVE_TC139                            | 10000     | Missing Forwarding Address Zip Code                                                                                                                        |
      | FWD_ADD_ZIP_CODE_MAX_LENGTH_NEGATIVE_TC140                       | 10000     | The Forwarding Address Zip Code must have a maximum length of 10                                                                                           |
      | FWD_ADD_ZIP_CODE_INVALID_NEGATIVE_TC141                          | 2000      | Invalid Request: Invalid Forwarding Zip Code                                                                                                               |
      | FWD_ADD_ZIP_CODE_INVALID_FORMAT_NEGATIVE_TC142                   | 2000      | Invalid Request: Invalid Forwarding Zip Code                                                                                                               |
      # Turn Off Reason/Sub Reason (generic validation)
      | TURN_OFF_REASON_EMPTY_NEGATIVE_TC143                             | 2000      | Invalid Request: Missing conditional parameters-Turn Off Reason                                                                                            |
      | TURN_OFF_REASON_MAX_LENGTH_NEGATIVE_TC144                        | 10000     | Turn Off Reason must have a maximum length of 100                                                                                                          |
      | TURN_OFF_REASON_INVALID_NEGATIVE_TC145                           | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_SUB_REASON_EMPTY_NEGATIVE_TC146                         | 10000     | Missing Turn Off Sub Reason                                                                                                                                |
      | TURN_OFF_SUB_REASON_MAX_LENGTH_NEGATIVE_TC147                    | 10000     | Turn Off Sub Reason must have a maximum length of 100                                                                                                      |
      | TURN_OFF_SUB_REASON_EMPTY_MOVING_NEGATIVE_TC148                  | 10000     | Missing Turn Off Sub Reason                                                                                                                                |
      | TURN_OFF_SUB_REASON_MAX_LENGTH_MOVING_NEGATIVE_TC149             | 10000     | Turn Off Sub Reason must have a maximum length of 100                                                                                                      |
      # Invalid-for-service-transfer combinations
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC150      | 2200      | Parameter Value should be null -Turn Off Reason                                                                                                            |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC151      | 2200      | Parameter Value should be null -Turn Off Reason                                                                                                            |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC152      | 2200      | Parameter Value should be null -Turn Off Reason                                                                                                            |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC153      | 2200      | Parameter Value should be null -Turn Off Reason                                                                                                            |
      | TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC154  | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                                              |
      | TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC155  | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                                              |
      | TURN_OFF_SUB_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC156  | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                                              |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC157      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC158      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC159      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC160      | 2200      | Parameter Value should be null -Turn Off Reason                                                                                                            |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC161      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC162      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC163      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC164      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      # ---- Invalid turnOffReason for Service Transfer (should be null for ST) ----
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC165      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC166      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC167      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC168      | 2200      | Parameter Value should be null -Turn Off Reason                                                                                                            |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC169      | 2200      | Parameter Value should be null -Turn Off Reason                                                                                                            |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC170      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC171      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC172      | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                                   |
      | TURN_OFF_REASON_INVALID_FOR_SERVICE_TRANSFER_NEGATIVE_TC173      | 2200      | Parameter Value should be null -ETC Exists                                                                                                                 |
      # ---- Missing/invalid sub reason for Service Transfer ----
      | TURN_OFF_SUB_REASON_MISSING_FOR_SERVICE_TRANSFER_NEGATIVE_TC174A | 2200      | Parameter Value should be null -ETC Exists                                                                                                                 |
      | TURN_OFF_SUB_REASON_MISSING_FOR_SERVICE_TRANSFER_NEGATIVE_TC174B | 10000     | Missing Turn Off Sub Reason                                                                                                                                |
      # ---- Email validations ----
      | EMAIL_ADDRESS_INVALID_LENGTH_NEGATIVE_TC175A                     | 10000     | The Email Address must be a string with a maximum length of 100.                                                                                           |
      | EMAIL_ADDRESS_INVALID_FORMAT_NEGATIVE_TC175B                     | 2200      | Parameter Value should be null -Email Address                                                                                                              |
      # ---- Requested Turn Off Date ----
      | REQUESTED_TURNOFF_DATE_MISSING_NEGATIVE_TC176                    | 10000     | Missing Requested Turn Off Date                                                                                                                            |
      | REQUESTED_TURNOFF_DATE_INVALID_NEGATIVE_TC177                    | 10000     | The Requested Turn Off Date must be a date in the format of yyyyMMdd                                                                                       |
      # ---- AGLC Service Order Number ----
      | AGLC_SERVICE_ORDER_NUMBER_MISSING_NEGATIVE_TC178                 | 10000     | Missing AGLC Service Order Number                                                                                                                          |
      | AGLC_SERVICE_ORDER_NUMBER_INVALID_LENGTH_MAX_NEGATIVE_TC179      | 10000     | The AGLC Service Order Number must be a numeric string with a maximum length of 9                                                                          |
      | AGLC_SERVICE_ORDER_NUMBER_INVALID_LENGTH_MIN_NEGATIVE_TC180      | 10000     | The AGLC Service Order Number must be a numeric string with a maximum length of 9                                                                          |
      # ---- etcExists / MRD ----
      | ETC_EXISTS_MISSING_NEGATIVE_TC181                                | 10000     | The JSON value could not be converted to System.Nullable`1[System.Boolean]. Path: $.etcExists \| LineNumber: 0 \| BytePositionInLine: 717.                 |
      | ETC_MISSING_NEGATIVE_TC182                                       | 10000     | The JSON value could not be converted to System.Nullable`1[System.Boolean]. Path: $.etcExists \| LineNumber: 0 \| BytePositionInLine: 375.                 |
      | MRD_INVALID_VALUE_NEGATIVE_TC183                                 | 1000      | Invalid Request: Missing required parameters - Marketer Reference Number                                                                                    |
      | MRD_LENGTH_MAX_NEGATIVE_TC184                                    | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.marketerReferenceData \| LineNumber: 0 \| BytePositionInLine: 647.      |
      | MRD_LENGTH_MIN_NEGATIVE_TC185                                    | 10000     | The Marketer Reference Data must be a number with 12 digits                                                                                                 |
      | MRD_DUPLICATE_NEGATIVE_TC186                                     | 10000     | The Marketer Reference Data must be a number with 12 digits                                                                                                 |
      | MRD_NOT_NUMERIC_NEGATIVE_TC187                                   | 2000      | Invalid Request: Invalid Marketer Reference Number - Duplicate                                                                                              |

  @SaveUnenrollmentWithSearchInvalidParameters @NegativeFlow @SaveUnenrollment @serviceTransfer @Phase1
  Scenario Outline: Verify SaveUnenrollment ServiceTransfer invalid with prior GetEligiblePlansAndOffers "<testCondition>"
    Then a request is made to get Marketer Reference Data
    When a request is made to the SaveUnenrollment ServiceTransfer Api with invalid parameters for "<testCondition>" condition
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                              | errorCode | errorMessage                       |
      | ACCOUNT_COMBINATION_INVALID_NEGATIVE_TC114 | 2000      | Invalid Request: Invalid Account   |
