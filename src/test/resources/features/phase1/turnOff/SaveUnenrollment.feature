Feature: Verify SaveUnenrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @HappyFlow @SaveUnenrollment @Phase1
  Scenario Outline: SaveUnenrollmentApi - Verify SaveUnenrollment Api positive flow <testCondition>
    Given a request is made to get Marketer Reference Data
    When a request is made to the SaveUnenrollment Api for account with "<pricePlan>" plan "<accountType>" type with forwardingAddressIs "<forwardingAddressIs>" with type "<addressType>" and turnoffreason "<testCondition>" and setEmail "<setEmail>" with etcExists "<etcExists>"
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then perform database validation
    Examples:
    |pricePlan|accountType|forwardingAddressIs         |addressType              |testCondition                                  |setEmail   |etcExists     |
    |MVS      |RS         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |SEASONAL_OR_HEAT_ONLY_TC207                    |false      |false         |
    |MI       |RS         |NEW_ADDRESS                 |ADDRESS_TYPE_STREET      |MOVING_OUTSIDE_AGLC_TC208                      |true       |false         |
    |MAP      |RS         |NEW_ADDRESS                 |ADDRESS_TYPE_RURAL       |OTHER_MILITARY_TC209                           |true       |false         |
    |CSV      |RS         |NEW_ADDRESS                 |ADDRESS_TYPE_POBOX       |REAP_REALTOR_INSPECTION_TC210                  |false      |false         |
    |TRD      |RS         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |OTHER_FINANCIAL_SITUATION_TC215                |true       |false         |
    |MVS      |SR         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |MOVING_NOT_STAYING_WITH_GNG_TC216              |false      |false         |
    |MVS      |RS         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |OTHER_REGULATED_PROVIDER_TC219                 |false      |false         |
    |CCV      |CM         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |SEASONAL_OR_HEAT_ONLY_TC223                    |false      |false         |
    |VML      |RS         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |OTHER_DECEASED_TC222                           |false      |false         |
    |CGB      |CM         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |OTHER_FINANCIAL_SITUATION_TC225                |false      |true          |
    |CMI      |CM         |NEW_ADDRESS                 |ADDRESS_TYPE_STREET      |REAP_REALTOR_INSPECTION_TC227                  |true       |false         |
    |CSV      |CM         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |MOVING_SERVICE_TRANSFER_TC228                  |false      |false         |
    |CGB      |CM         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |MOVING_NOT_STAYING_WITH_GNG_TC229              |false      |true          |
    |CVS      |CM         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |MOVING_OUTSIDE_AGLC_TC230                      |false      |false         |
    |CMI      |CM         |NEW_ADDRESS                 |ADDRESS_TYPE_STREET      |MOVING_SERVICE_TRANSFER_TC232                  |true       |false         |
    |GPP      |SR         |NEW_ADDRESS                 |ADDRESS_TYPE_STREET      |MOVING_OUTSIDE_ETC_WAIVED_TC211                |false      |true          |
    |RGB      |SR         |NEW_ADDRESS                 |ADDRESS_TYPE_STREET      |MOVING_SERVICE_TRANSFER_ETC_WAIVED_TC_213      |true       |true          |
    |18M      |RS         |NEW_ADDRESS                 |ADDRESS_TYPE_RURAL       |OTHER_MILITARY_ETC_WAIVED_TC_217               |false      |true          |
    |24M      |RS         |NEW_ADDRESS                 |ADDRESS_TYPE_POBOX       |MOVING_OUTSIDE_AGLC_ETC_WAIVED_TC_218          |false      |true          |
    |GPP      |RS         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |OTHER_DECEASED_ETC_WAIVED_TC_220               |false      |true          |
    |RF6      |RS         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |OTHER_REGULATED_PROVIDER_ETC_WAIVED_TC212      |true       |true          |
    |VML      |RS         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |OTHER_RENOVATION_ELECTRIC_CONVERSION_TC221     |false      |false         |
    |CFM      |CM         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |OTHER_RENOVATION_ELECTRIC_CONVERSION_TC224     |false      |true          |
    |CF6      |CM         |CURRENT_ADDRESS             |ADDRESS_TYPE_NOT_PRESENT |OTHER_MILITARY_ETC_WAIVED_TC_231               |false      |true          |
    |PRP      |RS         |NEW_ADDRESS                 |ADDRESS_TYPE_STREET      |HOUSEHOLD_ACCOUNT_CHANGE_TC_214                |true       |false         |
    |INX      |CM         |NEW_ADDRESS                 |ADDRESS_TYPE_STREET      |MOVING_OUTSIDE_POOL_GROUP_TC_226               |false      |false         |

  @SaveUnenrollmentWithInvalidRequestAndLoginID @NegativeFlow @SaveUnenrollment @Phase1
  Scenario Outline: Verify SaveUnenrollment Api with invalid request and login id for "<testCondition>"
    Given a request is made to get Marketer Reference Data
    When a request is made to the SaveUnenrollment Api with "<testCondition>"
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                         | errorCode | errorMessage                                              |
      | REQUEST_ID_EMPTY_NEGATIVE_TC106       | 10001     | Missing Request ID                                        |
      | REQUEST_ID_MAX_LENGTH_NEGATIVE_TC107  | 10002     | Invalid Request ID                                        |
      | REQUEST_ID_DUPLICATE_NEGATIVE_TC108   | 10003     | Duplicate Request ID                                      |
      | LOGIN_ID_EMPTY_NEGATIVE_TC109         | 10000     | Missing Login ID                                          |
      | LOGIN_ID_MAX_LENGTH_NEGATIVE_TC110    | 10000     | The Login ID must be a string with a maximum length of 30 |
      | LOGIN_ID_INVALID_ALPHA_NEGATIVE_TC111 | 2000      | Invalid Login ID                                          |
      | LOGIN_ID_NOT_EXISTS_NEGATIVE_TC112    | 2000      | Invalid Login ID                                          |

  @SaveUnenrollmentWithInvalidParameters @NegativeFlow @SaveUnenrollment @Phase1
  Scenario Outline: Verify SaveUnenrollment Api with invalid request and login id for "<testCondition>"
    Given a request is made to get Marketer Reference Data
    When a request is made to the SaveUnenrollment Api with invalid parameters for "<testCondition>" condition
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                            | errorCode | errorMessage                                                                                                                               |
      | TRANSACTION_TYPE_EMPTY_NEGATIVE_TC113                                    | 10000     | Missing Transaction Type                                                                                                                   |
      | TRANSACTION_TYPE_MAX_LENGTH_NEGATIVE_TC114                               | 10000     | The Transaction Type must be a string with a maximum length of 4                                                                           |
      | TRANSACTION_TYPE_INVALID_NEGATIVE_TC115                                  | 1000      | Invalid Request: Invalid Transaction Type                                                                                                  |
      | CUSTOMER_CODE_EMPTY_NEGATIVE_TC116                                       | 10000     | Missing Customer Code                                                                                                                      |
      | CUSTOMER_CODE_MAX_LENGTH_NEGATIVE_TC117                                  | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.customerCode \| LineNumber: 0 \| BytePositionInLine: 111.|
      | CUSTOMER_CODE_NON_INT_NEGATIVE_TC118                                     | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.customerCode \| LineNumber: 0 \| BytePositionInLine: 96. |
      | CUSTOMER_CODE_NOT_EXISTS_NEGATIVE_TC119                                  | 2000      | Invalid Request: Invalid Customer Code                                                                                                     |
      | PREMISES_CODE_EMPTY_NEGATIVE_TC120                                       | 10000     | Missing Premises Code                                                                                                                      |
      | PREMISES_CODE_MAX_LENGTH_NEGATIVE_TC121                                  | 10000     | The Premises Code must be a numeric string with a maximum length of 7                                                                      |
      | PREMISES_CODE_NOT_EXISTS_NEGATIVE_TC122                                  | 2000      | Invalid Request: Invalid Premises Code                                                                                                     |
      | AGLC_ACCOUNT_EMPTY_NEGATIVE_TC124                                        | 10000     | Missing AGLC Account Number                                                                                                                |
      | AGLC_ACCOUNT_MAX_LENGTH_NEGATIVE_TC125                                   | 10000     | The AGLC Account Number must be a numeric string with a maximum length of 20                                                               |
      | FWD_ADDRESS_EMPTY_NEGATIVE_TC129                                         | 10000     | Missing Forwarding Address Is                                                                                                              |
      | FWD_ADDRESS_MAX_LENGTH_NEGATIVE_TC130                                    | 10000     | The Forwarding Address Is must have a maximum length of 2                                                                                  |
      | FWD_ADDRESS_INVALID_VALUE_NEGATIVE_TC131                                 | 2000      | Invalid Request: Invalid Forwarding Address Option                                                                                         |
      | FWD_ADDRESS_TYPE_EMPTY_NEGATIVE_TC132                                    | 10000     | Missing Forwarding Address Type                                                                                                            |
      | FWD_ADDRESS_TYPE_MAX_LENGTH_NEGATIVE_TC133                               | 10000     | The Forwarding Address Type must have a maximum length of 1                                                                                |
      | FWD_ADDRESS_TYPE_INVALID_VALUE_NEGATIVE_TC134                            | 2000      | Invalid Request: Invalid Forwarding Address Type                                                                                           |
      | FWD_ADD_STR_NUM_MAX_LENGTH_NEGATIVE_TC135                                | 10000     | The Forwarding Address Street Number must have a maximum length of 12                                                                      |
      | FWD_ADD_STR_PRE_DIR_MAX_LENGTH_NEGATIVE_TC136                            | 10000     | The Forwarding Address Street Pre Direction must have a maximum length of 2                                                                |
      | FWD_ADD_STR_NAME_EMPTY_NEGATIVE_TC137                                    | 10000     | Missing Forwarding Address Street Name                                                                                                     |
      | FWD_ADD_STR_NAME_MAX_LENGTH_NEGATIVE_TC138                               | 10000     | The Forwarding Address Street Name must have a maximum length of 30                                                                        |
      | FWD_ADD_STR_SFX_MAX_LENGTH_NEGATIVE_TC139                                | 10000     | The Forwarding Address Street Suffix must have a maximum length of 6                                                                       |
      | FWD_ADD_STR_POST_DIR_MAX_LENGTH_NEGATIVE_TC140                           | 10000     | The Forwarding Address Street Post Direction must have a maximum length of 2                                                               |
      | FWD_ADD_UNIT_TYPE_MAX_LENGTH_NEGATIVE_TC141                              | 10000     | The Forwarding Address Unit Type must have a maximum length of 6                                                                           |
      | FWD_ADD_UNIT_NUM_MAX_LENGTH_NEGATIVE_TC142                               | 10000     | The Forwarding Address Unit Number must have a maximum length of 6                                                                         |
      | FWD_ADD_RURAL_ROUTE_MAX_LENGTH_NEGATIVE_TC143                            | 10000     | The Forwarding Address Rural Route must have a maximum length of 20                                                                        |
      | FWD_ADD_PO_BOX_MAX_LENGTH_NEGATIVE_TC144                                 | 10000     | The Forwarding Address PO Box must have a maximum length of 10                                                                             |
      | FWD_ADD_LINE2_MAX_LENGTH_NEGATIVE_TC145                                  | 10000     | Forwarding Address Line 2 must have a maximum length of 30                                                                                 |
      | FWD_ADD_CITY_EMPTY_NEGATIVE_TC146                                        | 10000     | Missing Forwarding Address City                                                                                                            |
      | FWD_ADD_CITY_MAX_LENGTH_NEGATIVE_TC147                                   | 10000     | The Forwarding Address City must have a maximum length of 20                                                                               |
      | FWD_ADD_STATE_CODE_EMPTY_NEGATIVE_TC148                                  | 10000     | Missing Forwarding Address State Code                                                                                                      |
      | FWD_ADD_STATE_CODE_MAX_LENGTH_NEGATIVE_TC149                             | 10000     | The Forwarding Address State Code must have a maximum length of 3                                                                          |
      | FWD_ADD_STATE_CODE_INVALID_NEGATIVE_TC150                                | 2000      | Invalid Request: Invalid Forwarding State Code                                                                                             |
      | FWD_ADD_ZIP_CODE_EMPTY_NEGATIVE_TC151                                    | 10000     | Missing Forwarding Address Zip Code                                                                                                        |
      | FWD_ADD_ZIP_CODE_MAX_LENGTH_NEGATIVE_TC152                               | 10000     | The Forwarding Address Zip Code must have a maximum length of 10                                                                           |
      | FWD_ADD_ZIP_CODE_INVALID_NEGATIVE_TC153                                  | 2000      | Invalid Request: Invalid Forwarding Zip Code                                                                                               |
      | FWD_ADD_ZIP_CODE_INVALID_FORMAT_NEGATIVE_TC154                           | 2000      | Invalid Request: Invalid Forwarding Zip Code                                                                                               |
      | TURN_OFF_REASON_EMPTY_NEGATIVE_TC155                                     | 10000     | Missing Turn Off Reason                                                                                                                    |
      | TURN_OFF_REASON_MAX_LENGTH_NEGATIVE_TC156                                | 10000     | Turn Off Reason must have a maximum length of 100                                                                                          |
      | TURN_OFF_REASON_INVALID_NEGATIVE_TC157                                   | 2000      | Invalid Request: Invalid Turn Off Reason                                                                                                   |
      | TURN_OFF_SUB_REASON_EMPTY_NEGATIVE_TC158                                 | 10000     | Missing Turn Off Sub Reason                                                                                                                |
      | TURN_OFF_SUB_REASON_MAX_LENGTH_NEGATIVE_TC159                            | 10000     | Turn Off Sub Reason must have a maximum length of 100                                                                                      |
      | TURN_OFF_SUB_REASON_EMPTY_MOVING_NEGATIVE_TC160                          | 10000     | Missing Turn Off Sub Reason                                                                                                                |
      | TURN_OFF_SUB_REASON_MAX_LENGTH_MOVING_NEGATIVE_TC161                     | 10000     | Turn Off Sub Reason must have a maximum length of 100                                                                                      |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC162                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC163                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC164                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC165                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC166                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC167                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC168                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC169                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC170                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC171                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC172                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC173                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC174                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC175                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC176                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC177                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC178                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC179                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC180                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC181                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC182                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC183                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_NO_ETC_TC184                                 | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC185                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC186                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC187                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC188                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC189                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC190                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | TURN_OFF_SUB_REASON_INVALID_WITH_ETC_TC191                               | 2000      | Invalid Request: Invalid Turn Off Sub Reason                                                                                               |
      | EMAIL_MAX_LENGTH_NEGATIVE_TC192                                          | 10000     | The Email Address must be a string with a maximum length of 100.                                                                           |
      | EMAIL_INVALID_FORMAT_NEGATIVE_TC193                                      | 2000      | Invalid Request: Invalid Email address                                                                                                     |
      | REQ_TURN_OFF_DATE_EMPTY_NEGATIVE_TC194                                   | 10000     | Missing Requested Turn Off Date                                                                                                            |
      | REQ_TURN_OFF_DATE_INVALID_FORMAT_NEGATIVE_TC195                          | 10000     | The Requested Turn Off Date must be a date in the format of yyyyMMdd                                                                       |
      | AGLC_SVC_ORDER_NUM_EMPTY_NEGATIVE_TC196                                  | 10000     | Missing AGLC Service Order Number                                                                                                          |
      | AGLC_SVC_ORDER_NUM_MAX_LENGTH_NEGATIVE_TC197                             | 10000     | The AGLC Service Order Number must be a numeric string with a maximum length of 9                                                          |
      | AGLC_SVC_ORDER_NUM_NON_NUMERIC_NEGATIVE_TC198                            | 10000     | The AGLC Service Order Number must be a numeric string with a maximum length of 9                                                          |
      | ETC_EXISTS_INVALID_NEGATIVE_TC199                                        | 10000     | The JSON value could not be converted to System.Nullable`1[System.Boolean]. Path: $.etcExists \| LineNumber: 0 \| BytePositionInLine: 662. |
      | ETC_EXISTS_EMPTY_NEGATIVE_TC200                                          | 10000     | Missing Etc Exists flag                                                                                                                    |
      | TURN_OFF_SUB_REASON_NOT_NULL_SEASONAL_ETC_TRUE_NEGATIVE_TC201            | 2200      | Parameter Value should be null -Turn Off Sub Reason                                                                                        |
      | TURN_OFF_SUB_REASON_NOT_NULL_SEASONAL_ETC_FALSE_NEGATIVE_TC202           | 2200      | Parameter Value should be null -Turn Off Sub Reason                                                                                        |
      | TURN_OFF_SUB_REASON_NOT_NULL_REAP_ETC_TRUE_NEGATIVE_TC203                | 2200      | Parameter Value should be null -Turn Off Sub Reason                                                                                        |
      | TURN_OFF_SUB_REASON_NOT_NULL_REAP_ETC_FALSE_NEGATIVE_TC204               | 2200      | Parameter Value should be null -Turn Off Sub Reason                                                                                        |
      | TURN_OFF_SUB_REASON_NOT_NULL_HOUSEHOLD_CHANGE_ETC_TRUE_NEGATIVE_TC205    | 2200      | Parameter Value should be null -Turn Off Sub Reason                                                                                        |
      | TURN_OFF_SUB_REASON_NOT_NULL_HOUSEHOLD_CHANGE_ETC_FALSE_NEGATIVE_TC206   | 2200      | Parameter Value should be null -Turn Off Sub Reason                                                                                        |



  @SaveUnenrollmentWithSearchInvalidParameters @NegativeFlow @SaveUnenrollment @Phase1
  Scenario Outline: Verify SaveUnenrollment Api with invalid request and login id for "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to get Marketer Reference Data
    When a request is made to the SaveUnenrollment Api with invalid parameters for "<testCondition>" condition
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                 | errorCode | errorMessage                                                                 |
      | ACCOUNT_COMBINATION_INVALID_NEGATIVE_TC123    | 2000      | Invalid Request: Invalid Account                                             |