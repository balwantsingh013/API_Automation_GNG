Feature: Verify GetEligiblePlansAndOffers Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetEligiblePlansAndOffersRSPositive @GetEligiblePlansAndOffersPositive @HappyFlow
  Scenario Outline: GetEligiblePlansAndOffersAPI - returns <numberOfMatches> plans for <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then the response should contain the expected plans

    Examples:
      | testCondition                        |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_318 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_319 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_320 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_322 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_323 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_324 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_325 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_326 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_328 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_329 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_330 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_331 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_332 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_333 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_334 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_335 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_337 |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_338 |
      #| GET_ELIGIBLE_PLANS_AND_OFFERS_TC_338a|
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_338e|

  @GetEligiblePlansAndOffersPromotionCodePositive @HappyFlow
  Scenario Outline: GetEligiblePlansAndOffersAPI - returns <numberOfMatches> plans for <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api with "<promotionCode>" promotionCode for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then the response should contain the expected plans

    Examples:
      | testCondition                        | promotionCode |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_321 | AAA           |
      | GET_ELIGIBLE_PLANS_AND_OFFERS_TC_327 | AAA           |

  @GetEligiblePlansAndOffersCommercialPositive @GetEligiblePlansAndOffersPositive @Phase1 @HappyFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with customer type commercial for <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api with customer type commercial for "<testCondition>"
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then the response should contain the expected plans

  Examples:
    |testCondition                                              |
    |COMMERCIAL_CREDIT_CHECK_YES_TC_339                         |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_340          |
    |COMMERCIAL_CREDIT_CHECK_SKIP_NEW_ENROLLMENT_TC_341         |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_342          |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_343          |
    |COMMERCIAL_CREDIT_CHECK_SERV_TRANSFER_NEW_ENROLLMENT_TC_344|
#    |COMMERCIAL_CREDIT_CHECK_MULT_NEW_ENROLLMENT_TC_345         |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_346          |
#    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_347          |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_348          |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_349          |
    |COMMERCIAL_CREDIT_CHECK_YES_INCL_ENROLLMENT_TC_350         |
    |COMMERCIAL_CREDIT_CHECK_YES_CRDS_ENROLLMENT_TC_350B        |
    |COMMERCIAL_CREDIT_CHECK_YES_CRDS_ENROLLMENT_TC_350E        |

  @GetEligiblePlansAndOffersWithInvalidRequestIDTNON @Phase1 @NegativeFlow
  Scenario Outline: GetEligiblePlansAndOffersApi- Verify GetEligiblePlansAndOffers Api with invalid requestID <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api with "<testCondition>"TC155_157
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                   | errorCode | errorMessage     |
      | NULL_REQUEST_ID_TC_156      | 10001     | Missing Request ID   |
      | DUPLICATE_REQUEST_ID_TC_155 | 10003     | Duplicate Request ID |
      | LONG_REQUEST_ID_TC_157      | 10002     | Invalid Request ID   |


  @GetEligiblePlansAndOffersInvalidLoginIDTNON @Phase1  @NegativeFlow
  Scenario Outline: GetEligiblePlansAndOffersApi- Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api with login "<testCondition>" ID TC158_160b
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                      | errorCode | errorMessage                                              |
      | INVALID_LOGIN_ID_TC_160      | 2000      | Invalid Login ID                                          |
      | NULL_LOGIN_ID_TC_158         | 10000     | Missing Login ID                                          |
      | NON_NUMERIC_LOGIN_ID_TC_160B | 2000      | Invalid Login ID                                          |
      | ALPHANUMERIC_LOGIN_ID_TC_160A| 2000      | Invalid Login ID                                          |
      | MAX_LENGTH_LOGIN_ID_TC_159   | 10000     | The Login ID must be a string with a maximum length of 30 |

  @GetEligiblePlansAndOffersInvalidTransactionIDTNON @Phase1  @NegativeFlow
  Scenario Outline: GetEligiblePlansAndOffersApi- Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api with transaction "<testCondition>" ID TC161_162
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                    | errorCode | errorMessage                                                   |
      | NULL_TRANSACTION_ID_INCL_ENROLLMENT_STATE_TC_161 | 2000      | Invalid Request: Missing conditional parameters-Transaction ID |
      | NULL_TRANSACTION_ID_CRDS_ENROLLMENT_STATE_TC_162 | 2000      | Invalid Request: Missing conditional parameters-Transaction ID |

  @GetEligiblePlansAndOffersInvalidCustomerCodeTNON @Phase1  @NegativeFlow
  Scenario Outline: GetEligiblePlansAndOffersApi- Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api with customer "<testCondition>" code TC163_164
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                             | errorCode | errorMessage                                                  |
      | NULL_CUSTOMER_CODE_INCL_ENROLLMENT_STATE_TC_163 | 2000      | Invalid Request: Missing conditional parameters-Customer Code |
      | NULL_CUSTOMER_CODE_CRDS_ENROLLMENT_STATE_TC_164 | 2000      | Invalid Request: Missing conditional parameters-Customer Code |

  @GetEligiblePlansAndOffersInvalidPremisesCodeAndEnrollmentStateTNON @Phase1  @NegativeFlow
  Scenario Outline: GetEligiblePlansAndOffersApi- Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api with premises "<testCondition>" code TC165_167
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                              | errorCode | errorMessage                                                                                                                      |
      | NULL_PREMISES_CODE_INCL_ENROLLMENT_STATE_TC_165  | 2000      | Invalid Request: Missing conditional parameters-Premises Code                                                                     |
      | NULL_PREMISES_CODE_CRDS_ENROLLMENT_STATE_TC_166  | 2000      | Invalid Request: Missing conditional parameters-Premises Code                                                                     |
      | VALID_PREMISES_CODE_NULL_ENROLLMENT_STATE_TC_167 | 2000      | Invalid Request: Invalid Parameter Combination - For new enrollment Transaction ID,Customer Code and Premises Code should be null |
    |ENROLLMENT_STATE_CUST_CODE_PREM_CODE_NULL_TC_168  |2000      | Invalid Request: Invalid Parameter Combination - For new enrollment Transaction ID,Customer Code and Premises Code should be null |
    |ENROLLMENT_STATE_TRAN_ID_PREM_CODE_NULL_TC_169    |2000      | Invalid Request: Invalid Parameter Combination - For new enrollment Transaction ID,Customer Code and Premises Code should be null |
|ENROLLMENT_STATE_TRAN_ID_CUST_CODE_NULL_TC_170    |2000      | Invalid Request: Invalid Parameter Combination - For new enrollment Transaction ID,Customer Code and Premises Code should be null |
    |ENROLLMENT_STATE_PREM_CODE_NULL_TC_171            |2000      | Invalid Request: Invalid Parameter Combination - For new enrollment Transaction ID,Customer Code and Premises Code should be null |
    |ENROLLMENT_STATE_CUST_CODE_NULL_TC_172            |2000      | Invalid Request: Invalid Parameter Combination - For new enrollment Transaction ID,Customer Code and Premises Code should be null |
    |ENROLLMENT_STATE_TRAN_ID_NULL_TC_173              |2000      | Invalid Request: Invalid Parameter Combination - For new enrollment Transaction ID,Customer Code and Premises Code should be null |
    |INVALID_ENROLLMENT_STATE_TC_174                   |2000      |Invalid Request: Invalid Enrollment State                                                                                          |

  @GetEligiblePlansAndOffersInvalidTransactionId @Phase1 @NegativeFlow
  Scenario Outline: GetEligiblePlansAndOffersApi- Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "<testCondition>"
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                              | errorCode | errorMessage                                   |planCode|promotionCode|
      | INVALID_TRANSACTION_ID_ENROLLMENT_STATE_INCL_TC_175  | 2000      | Invalid Request: Invalid Transaction ID|MVS     |25 CENTS FOR 12 MONTHS        |
      | INVALID_TRANSACTION_ID_ENROLLMENT_STATE_CRDS_TC_176  | 2000      | Invalid Request: Invalid Transaction ID|VML     |        |

  @GetEligiblePlansAndOffersInvalidCustomerCode @Phase1 @NegativeFlow
  Scenario Outline: GetEligiblePlansAndOffersApi- Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "<testCondition>"
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                       | errorCode | errorMessage                                   |planCode|promotionCode|
      | INVALID_CUSTOMER_CODE_ENROLLMENT_STATE_INCL_TC_177  | 2000      | Invalid Request: Invalid Customer Code|MVS     |25 CENTS FOR 12 MONTHS        |
      | INVALID_CUSTOMER_CODE_ENROLLMENT_STATE_CRDS_TC_178 | 2000      | Invalid Request: Invalid Customer Code|VML     |        |
      | INVALID_LENGTH_CUSTOMER_CODE_ENROLLMENT_STATE_INCL_TC_186  | 10000      | The Customer Code must be an integer with a maximum length of 9|MVS     |25 CENTS FOR 12 MONTHS        |
      | INVALID_CUSTOMER_CODE_LESS_THAN_0_ENROLLMENT_STATE_INCL_TC_186A  | 10000      | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.customerCode [PIPE] LineNumber: 0 [PIPE] BytePositionInLine: 351. |MVS     |25 CENTS FOR 12 MONTHS        |
      | INVALID_CUSTOMER_CODE_EMPTY_ENROLLMENT_STATE_INCL_TC_186B  | 10000      | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.customerCode [PIPE] LineNumber: 0 [PIPE] BytePositionInLine: 350. |MVS     |25 CENTS FOR 12 MONTHS        |
      | INVALID_CUSTOMER_CODE_ENROLLMENT_STATE_INCL_TC_187  | 2000      | Invalid Request: Invalid Customer Code|MVS     |25 CENTS FOR 12 MONTHS        |

  @GetEligiblePlansAndOffersInvalidPremisesCode @Phase1 @NegativeFlow
  Scenario Outline: GetEligiblePlansAndOffersApi- Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "<testCondition>"
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                       | errorCode | errorMessage                                   |planCode|promotionCode|
      | INVALID_PREMISES_CODE_ENROLLMENT_STATE_INCL_TC_179  | 2000      |Invalid Request: Invalid Premises Code|MVS     |25 CENTS FOR 12 MONTHS        |
      | INVALID_PREMISES_CODE_ENROLLMENT_STATE_CRDS_TC_180 | 2000      |Invalid Request: Invalid Premises Code|VML     |        |
      | INVALID_PREMISES_CODE_LENGTH_ENROLLMENT_STATE_INCL_TC_188  | 10000      |The Premises Code must be a numeric string with a maximum length of 7|MVS     |25 CENTS FOR 12 MONTHS        |
      | INVALID_PREMISES_CODE_NON_NUMERIC_ENROLLMENT_STATE_INCL_TC_188A  | 10000      |The Premises Code must be a numeric string with a maximum length of 7|MVS     |25 CENTS FOR 12 MONTHS        |
      | INVALID_PREMISES_CODE_ENROLLMENT_STATE_INCL_TC_189  |  2000      |Invalid Request: Invalid Premises Code|MVS     |25 CENTS FOR 12 MONTHS        |

  @GetEligiblePlansAndOffersInvalidCombinationOfCustPremCode @Phase1 @NegativeFlow
  Scenario Outline: GetEligiblePlansAndOffersApi- Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "<testCondition>" with "<planCode>" and "<promotionCode>"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "<testCondition>"
    And response should have ErrorCode 0 and ErrorMessage ""
    And a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment "<testCondition>"
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                                      | errorCode | errorMessage                                   |planCode|promotionCode|
      | INVALID_COMBINATION_OF_CUSTOMER_AND_PREMISES_CODE_INCL_TC_181      | 2000      |Invalid Request: Invalid Account|MVS     |25 CENTS FOR 12 MONTHS        |
      | INVALID_COMBINATION_OF_CUSTOMER_AND_PREMISES_CODE_CRDS_TC_182      | 2000      |Invalid Request: Invalid Account|VML     |        |

  @GetEligiblePlansAndOffersInvalidWorkPhoneNumberTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<WorkPhoneNumber>"
    When a request is made to the GetEligiblePlansAndOffers Api with workPhone "<WorkPhoneNumber>" Number284_286
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | WorkPhoneNumber                                   | errorCode | errorMessage                                                     |
      | MIN_LENGTH_WORK_PHONE_NUMBER_TC_284                      | 2000      | Invalid Request: Invalid Work Phone Number                       |
      | ALPHANUMERIC_WORK_PHONE_NUMBER_TC_285                    | 2000      | Invalid Request: Invalid Work Phone Number                       |
      | NULL_WORK_PHONE_NUMBER_WITH_VALID_WORK_PHONE_TYPE_TC_286 | 2000     | Invalid Request: Missing conditional parameters-Work Phone Number|


  @GetEligiblePlansAndOffersCommercialNegative @Phase1 @NegativeFlow
  Scenario Outline: GetEligiblePlansAndOffersApi- Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers for a "<testCondition>"
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                   | errorCode | errorMessage                                                     |
      | NO_MATCHING_DATA_COMMERCIAL_TC_361              | 11116      |No match found. Please see the list of similar businesses found |
|VALID_DATA_REENTERED_COMMERCIAL_TC_362           |0           |                                                                |
|CREDIT_CHECK_BUSINESS_BIN_NOT_NULL_TC_262A       |0           |                                                                |


  @GetEligiblePlansAndOffersFraudAlertInvalidSSN @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                     | errorCode | errorMessage                                                          |
      #| FRAUD_ALERT_INVALID_SSN_TC_351 | 11114      | Identification Verification Required. Ask customer to mail or fax photo ID, copy of ss card to: Georgia Natural Gas Attention: Consumer Relations PO Box 78760 Atlanta GA 30357 Fax: 404 685 - 4117|
#|NO_MATCH_FOUND_IN_EXPERIAN_TC_353|11112      |CUSTOMER NOT FOUND,  PLEASE CHECK SPELLING of CUSTOMER NAME and SSN|
#|NO_MATCH_INVALID_NAME_CONTINUE_ENROLLMENT_TC_354A   |0          |                                                                       |
#|NO_MATCH_PLAN_CODE_B_CONTINUE_ENROLLMENT_TC_354   |0          |                                                                       |
#|NO_MATCH_INVALID_NAME_CONTINUE_ENROLLMENT_TC_354B  |11114      |Identification Verification Required. Ask customer to mail or fax photo ID, copy of ss card to: Georgia Natural Gas Attention: Consumer Relations PO Box 78760 Atlanta GA 30357 Fax: 404 685 - 4117|
#|DECEASED_OR_NON_ISSUED_CUSTOMER_TC_355             |11112      |CUSTOMER NOT FOUND,  PLEASE CHECK SPELLING of CUSTOMER NAME and SSN|
    #|DECEASED_OR_NON_ISSUED_CONFIRM_CREDIT_CHECK_CUSTOMER_TC_355A|11115|SSN on deceased or non-issued list. Do not inform customer reason for denial, follow scripted Denial statement. DO NOT override denial.|
      #|NO_MATCH_FOUND_IN_EXPERIAN_TC_356|11112      |CUSTOMER NOT FOUND,  PLEASE CHECK SPELLING of CUSTOMER NAME and SSN|
#|NO_MATCH_INVALID_NAME_CONTINUE_ENROLLMENT_TC_356A|11114      |Identification Verification Required. Ask customer to mail or fax photo ID, copy of ss card to: Georgia Natural Gas Attention: Consumer Relations PO Box 78760 Atlanta GA 30357 Fax: 404 685 - 4117|
#|GET_ELIGIBLE_PLANSA_AND_OFFERS_FROZEN_ACCOUNT_357|11113        |Credit file blocked by consumer.  Inform customer to contact Experian regarding the credit block at 888-397-3742.  DO NOT override denial.|
#|ENROLLMENT_DENIED_DUE_TO_NO_PAYMENT_TC_358         |3000       |The customer's enrollment request is denied due to past payment history|
#|ENROLLMENT_DENIED_AS_CREDIT_CHECK_NOT_AUTHORIZED_TC_360               |3000       |The customer's enrollment request is denied                           |
|LOW_CREDIT_SCORE_FOR_SSP_ENROLLMENT_TC_338B        |2100       |WARNING: This customer does not meet the required credit criteria to participate in the Seasonal Savings Plan|

  @GetEligiblePlansAndOffersInvalidWorkPhoneTypeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<WorkPhoneType>"
    When a request is made to the GetEligiblePlansAndOffers Api with WorkPhone "<WorkPhoneType>" Type287_290
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | WorkPhoneType                                     | errorCode | errorMessage                                                          |
      | NULL_WORK_PHONE_TYPE_WITH_VALID_WORK_PHONE_NUMBER_TC_287 | 2000      | Invalid Request: Missing conditional parameters-Work Phone Type       |
      | MAX_LENGTH_WORK_EXTENSION_TYPE_TC_288                    | 10000     | The Work Phone Extension must be a string with a maximum length of 4. |
      | WORK_PHONE_TYPE_PROVIDED_MAX_1_CHAR_TC_289               | 10000     | The Work Phone Type must be a string with a maximum length of 1.      |
      | INVALID_WORK_PHONE_TYPE_VALUE_TC_290                     | 2000      | Invalid Request: Invalid Work Phone Type                              |

  @GetEligiblePlansAndOffersInvalidHomePhoneNumberTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<HomePhoneNumber>"
    When a request is made to the GetEligiblePlansAndOffers Api with HomePhone "<HomePhoneNumber>" Number291_293
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | HomePhoneNumber                                   | errorCode | errorMessage                                                      |
      #| HOME_PHONE_NUMBER_NOT_10_DIGIT_TC_291                      | 2000      | Invalid Request: Invalid Home Phone Number                        |
      #| ALPHANUMERIC_HOME_PHONE_NUMBER_TC_292                    | 2000      | Invalid Request: Invalid Home Phone Number                        |
      | NULL_HOME_PHONE_NUMBER_WITH_VALID_HOME_PHONE_TYPE_TC_293 | 2000      | Invalid Request: Missing conditional parameters-Home Phone Number |


  @GetEligiblePlansAndOffersInvalidHomePhoneTypeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<HomePhoneType>"
    When a request is made to the GetEligiblePlansAndOffers Api with HomePhone "<HomePhoneType>" Type294_297
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | HomePhoneType                                         | errorCode | errorMessage                                                     |
      | NULL_HOME_PHONE_TYPE_WITH_VALID_HOME_PHONE_NUMBER_294 | 2000      | Invalid Request: Missing conditional parameters-Home Phone Type  |
      | HOME_PHONE_TYPE_PROVIDED_MAX_1_CHAR_296               | 10000     | The Home Phone Type must be a string with a maximum length of 1. |
      | INVALID_HOME_PHONE_TYPE_VALUE_297                     | 2000      | Invalid Request: Invalid Home Phone Type                        |

  @GetEligiblePlansAndOffersInvalidAcnStatusIndicatorTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<AcnStatusIndicator>"
    When a request is made to the GetEligiblePlansAndOffers Api with acnStatus "<AcnStatusIndicator>" IndicatorTC298_307
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | AcnStatusIndicator                                                        | errorCode | errorMessage                                                          |
#      | EMPTY_ACN_STATUS_INDICATOR_WITH_TENANT_LANDLORD                           | 10000     | Invalid or missing ACN Status Indicator                           |
   #   | INVALID_ACN_STATUS_INDICATOR_VALUE_NOT_PRESENT_IN_TABLE_TC_299                   | 2000      | Invalid Request: Invalid ACN Status Indicator                         |
#      | MAX_LENGTH_ACN_STATUS_INDICATOR_TC_298                                           | 10000     | The ACN Status Indicator must be a string with a maximum length of 4. |
      #| VALID_ACN_STATUS_INDICATOR_WITH_MAX_LENGTH_TENANT_LANDLORD_TC_301                | 10000     | The Tenant/Landlord must be a string with a maximum length of 1.      |
#|INVALID_TENANT_LANDLORD_INDICATOR_TC_302                                   |2000       |Invalid Request: Invalid Tenant/Landlord Indicator                              |
      #| VALID_ACN_STATUS_INDICATOR_WITH_NULL_TENANT_LANDLORD_TC_303                      | 10000     | Invalid or missing Tenant/Landlord Indicator                          |
      | VALID_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_L_WITH_INVALID_USER_ROLE_TC_304 | 2000      | Invalid Request: Invalid ACN Status for user role                     |
     | VALID_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_T_WITH_INVALID_USER_ROLE_TC_305 | 2000      | Invalid Request: Invalid ACN Status for user role                     |
    |NULL_ACN_STATUS_INDICATOR_TC_300A                                                 |10000      |Invalid or missing ACN Status Indicator                                |
          |NULL_ACN_STATUS_INDICATOR_TC_300B                                                 |10000      |Invalid or missing ACN Status Indicator                                |
#      | NULL_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_L_WITH_INVALID_USER_ROLE_TC_306  | 10000     | Invalid or missing ACN Status Indicator                               |
#      | NULL_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_T_WITH_INVALID_USER_ROLE_TC_307  | 10000     | Invalid or missing ACN Status Indicator                     |

  @GetEligiblePlansAndOffersInvalidCustomerPEWCPreferencesTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<CustomerPEWCPreferences>"
    When a request is made to the GetEligiblePlansAndOffers Api with CustomerPEWC "<CustomerPEWCPreferences>" PreferencesTC308_310
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | CustomerPEWCPreferences                                   | errorCode | errorMessage                                                                                                                      |
      | CUSTOMER_PEWC_PREFRENCES_VALUE_GOOD_WITH_OTHER_PARAM_NULL_TC_308 | 10000     |The JSON value could not be converted to System.Boolean. Path: $.customerPEWCPreferences [PIPE] LineNumber: 0 [PIPE] BytePositionInLine: 174.|
      | CUSTOMER_PEWC_PREFRENCES_VALUE_TRUE_WITH_OTHER_PARAM_NULL_TC_309 | 2000      | Invalid Request: Missing conditional parameters-Customer PEWC Preference                                                          |

  @GetEligiblePlansAndOffersInvalidCreditCheckOptionTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<CreditCheckOption>"
    When a request is made to the GetEligiblePlansAndOffers Api with credit "<CreditCheckOption>" CheckoptionTC310_312
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | CreditCheckOption              | errorCode | errorMessage                                                          |
      | EMPTY_CREDIT_CHECK_OPTION_TC_312      | 10000     | Invalid or missing Credit Check Option                                |
      | INVALID_CREDIT_CHECK_OPTION_TC_311    | 2000      | Invalid Request: Invalid Credit Check Option                          |
      | MAX_LENGTH_CREDIT_CHECK_OPTION_TC_310 | 10000     | The Credit Check Option must be a string with a maximum length of 32. |

  @GetEligiblePlansAndOffersInitialCreditCheckCustomerCodeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<InitialCreditCheckCustomerCode>"
    When a request is made to the GetEligiblePlansAndOffers Api with InitialCreditCheck "<InitialCreditCheckCustomerCode>" CustomerCodeTC313_317
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | InitialCreditCheckCustomerCode                                    | errorCode | errorMessage                                                                                                                                              |
      | EMPTY_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_WITH_CREDIT_CHECK_OPTION_315 | 2000      | Invalid Request: Missing conditional parameters-Initial Credit Check Cust Code                                                                            |
      | MAX_LENGTH_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_313                     | 10000     | The Initial Credit Check Customer Code must be an integer with a maximum length of 9                                                                   |
      | NONNUMERIC_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_314                     | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.initialCreditCheckCustomerCode [PIPE] LineNumber: 0 [PIPE] BytePositionInLine: 304.|
      | INVALID_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_NOT_PRESENT_IN_TABLE_316   | 2000      | Invalid or missing Initial Credit Check Customer Code                                                                                                                  |
      | INVALID_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_WITHOUT_CREDIT_SCORE_317   | 2100      | Unable to locate a credit score within 3 months for the Customer Code provided - 5908691                                                                  |


  @GetEligiblePlansAndOffersInvalidTransactionTypeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<testCondition>"Type
    When a request is made to the GetEligiblePlansAndOffers Api with  transaction "<testCondition>" Type
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                      | errorCode | errorMessage                                                     |
      |NULL_TANSACTION_TYPE_TC_183         |10000     | Missing Transaction Type                                         |
#      | EMPTY_TRANSACTION_TYPE               | 10000     | Missing Transaction Type                                         |
#      | NUMERIC_TRANSACTION_TYPE             | 10000     | The Transaction Type must be a string with a maximum length of 4 |
#      | MAX_LENGTH_VALIDATION_TRANSACTION_TYPE_TC_184           | 10000     | The Transaction Type must be a string with a maximum length of 4 |
#      | ALPHANUMERIC_TRANSACTION_TYPE        | 1000      | Invalid Request: Invalid Transaction Type                        |
#      | WHITESPACE_CONTAINS_TRANSACTION_TYPE | 10000     | The Transaction Type must be a string with a maximum length of 4 |
  |INVALID_TRANSACTION_TYPE_TC_185                          |1000       |Invalid Request: Invalid Transaction Type                        |

  @GetEligiblePlansAndOffersInvalidCustomerTypeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api with  customer "<testCondition>" Type
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition             | errorCode | errorMessage                                                   |
#      | EMPTY_CUSTOMER_TYPE      | 10000     | Invalid or missing Customer Type                              |
      | NULL_CUSTOMER_TYPE_TC_190     | 10000     | Invalid or missing Customer Type                              |
#      | MIN_LENGTH_CUSTOMER_TYPE | 10000     | The Customer Type must be a string with a maximum length of 2 |
#      | SPL_CHAR_CUSTOMER_TYPE   | 10000     | The Customer Type must be a string with a maximum length of 2 |
      | MAX_LENGTH_CUSTOMER_TYPE_TC_191 | 10000     | The Customer Type must be a string with a maximum length of 2 |
    |INVALID_VALUE_CUSTOMER_TYPE_TC_192|1000     | Invalid Request: Invalid Customer Type|

  @GetEligiblePlansAndOffersInvalidEnrollmentSourcesTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api with enrollment "<testCondition>" Sources
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition             | errorCode | errorMessage                                                        |
#      | EMPTY_ENROLLMENT_SOURCES      | 10000     | Invalid or missing Enrollment Source                                |
#      | NUMERIC_ENROLLMENT_SOURCES    | 1000      | Invalid Request: Invalid Enrollment Source                          |
#      | SPL_CHAR_ENROLLMENT_SOURCES   | 1000      | Invalid Request: Invalid Enrollment Source                          |
      | MAX_LENGTH_ENROLLMENT_SOURCES_TC_194 | 10000     | The Enrollment Source must be a string with a maximum length of 35. |
    |NULL_ENROLLMENT_SOURCES_TC_193 | 10000     | Invalid or missing Enrollment Source                                |
|   INVALID_VALUE_ENROLLMENT_SOURCE_195|1000     | Invalid Request: Invalid Enrollment Source |


  @GetEligiblePlansAndOffersInvalidMarketingPromotionCodeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api with enrollment "<testCondition>" Sources
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition             | errorCode | errorMessage                                                        |
#      |   INVALID_LENGTH_MARKETING_PROMOTION_CODE_196|10000     |The Marketing Promotion Code must be a string with a maximum length of 45.|
#      |   INVALID_MARKETING_PROMOTION_CODE_FOR_ENROLLMENT_SOURCE_TC_197|2100     |Invalid promotion code|
#|INVALID_MARKETING_PROMOTION_CODE_TC_198|1000     |Invalid Request: Invalid Promotion Code|
#        |   INVALID_MARKETING_PROMOTION_CODE_FOR_RS_CUST_TYPE_TC_199|2100     |Promotion code is valid for Commercial Customers only|
#  |INVALID_MARKETING_PROMOTION_CODE_FOR_NEW_CUSTOMERS_TC_200|2100|Promotion code is valid for Existing Customers only|
#    |EXPIRED_MARKETING_PROMOTION_CODE_TC_201                  |2100|Promotion code is expired                        |
#|   INVALID_MARKETING_PROMOTION_CODE_FOR_CM_CUST_TYPE_TC_202|2100     |Promotion code is valid for Residential Customers only|
# |   INVALID_MARKETING_PROMOTION_CODE_FOR_ENROLLMENT_SOURCE_TC_203|2100     |Promotion code is valid for Web only|
#|INVALID_CUSTOMER_BUSINESS_NAME_LENGTH_TC_212|10000|The Customer Business Name must be a string with a maximum length of 60.|
#|CUSTOMER_BUSINESS_NAME_NULL_TC_213|2000|Invalid Request: Missing conditional parameters-Customer Business Name|
#|CUSTOMER_BUSINESS_NAME_COMM_CREDIT_CHECK_214|2000|Invalid Request: Missing conditional parameters-Customer Business Name|
#|CREDIT_CHECK_BUSINESS_NAME_LENGTH_VALIDATION_TC_215|10000|The CreditCheck Business Name must be a string with a maximum length of 60.|
#    |CUSTOMER_BUSINESS_NAME_NULL_TC_215A                |2000|Invalid Request: Missing conditional parameters-Customer Business Name|
#      |INVALID_LENGTH_CUSTOMER_LAST_NAME_TC_216|10000     |The Customer Last Name must be a string with a maximum length of 60.|
#|GENERATION_CODE_LENGTH_VALIDATION_TC_218|10000|The Generation Code must be a string with a maximum length of 3.|
#|INVALID_GENERATION_CODE_TC_219|2000    |Invalid Request: Invalid Generation Code                             |
#|CUSTOMER_MIDDLE_NAME_LENGTH_VALIDATION_TC_220|10000|The Customer Middle Name must be a string with a maximum length of 15.|
#|CUSTOMER_FIRST_NAME_LENGTH_VALIDATION_TC_221|10000|The Customer First Name must be a string with a maximum length of 15.|
#|CUSTOMER_FIRST_AND_MIDDLE_NAME_INVALID_LENGTH_TC_222|10000|The Customer First Name must be a string with a maximum length of 15.|
#|SSN_INVALID_LENGTH_TC_223  |2000       |Invalid Request: Invalid SSN                                        |
#|NON_NUMERIC_SSN_TC_224     |2000       |Invalid Request: Invalid SSN                                        |
#|FEDERAL_TAX_ID_INVALID_LENGTH_TC_225|10000|Invalid Federal Tax ID                                           |
# |NON_NUMERIC_FEDERAL_TAX_ID_TC_226|10000|Invalid Federal Tax ID                                           |
#    |FEDERAL_TAX_ID_NULL_TC_227 |2000       |Invalid Request: Missing conditional parameters-Federal Tax ID      |
#|FEDERAL_TAX_ID_MISSING_FOR_CREDIT_CHECK_COMM_TC_228|2000|Invalid Request: Missing conditional parameters-Federal Tax ID|
#|EMAIL_ADDRESS_LENGTH_VALIDATION_TC_229|10000|The EmailAddress field is not a valid e-mail address.          |
#|INVALID_EMAIL_FORMAT_TC_230|2000       |Invalid Request: Invalid Email address                              |
#|AGLC_ACCOUNT_NUMBER_LENGTH_VALIDATION_TC_231|10000|The AGLC Account Number must be a numeric string with a maximum length of 20|
#|AGLC_ACCOUNT_NUMBER_NON_NUMERIC_TC_232|10000|The AGLC Account Number must be a numeric string with a maximum length of 20|
#|AGLC_SERVICE_LOCATION_ID_NULL_TC_233|10000|Missing AGLC Service Location ID     |
#  |AGLC_SERVICE_LOCATION_ID_LENGTH_VALIDATION_TC_234|10000|The field AglcServiceLocationID must have a maximum length of 9.|
#|NON_NUMERIC_AGLC_SERVICE_LOCATION_ID_TC_235|10000|The JSON value could not be converted to System.Nullable`1[System.Int64]. Path: $.aglcServiceLocationID [PIPE] LineNumber: 0 [PIPE] BytePositionInLine: 474.|
#|AUTHORIZED_BY_LENGTH_VALIDATION_TC_236|10000|The Authorized By must be a string with a maximum length of 90. |
#|AUTHORIZED_BY_NULL_TC_237  |2000       |Invalid Request: Missing conditional parameters-Authorized By        |
#|REFERRAL_CODE_LENGTH_VALIDATION_TC_238|10000|The Referral Code must be a string with a maximum length of 9.  |
#|REFERRAL_CODE_BEGINNING_WITH_A_LETTER_TC_239|2000|Invalid Request: Invalid Referral Code                     |
#|REFERRAL_CODE_ALPHANUMERIC_TC_240|2000|Invalid Request: Invalid Referral Code                     |
#|MARKETING_PROMOTION_CODE_MISSING_TC_241|1000|Invalid Request: Missing Promotion Code -Please ask the customer for a promotion code. If they do not have one, enter the appropriate default promotion code|
#|PREMISES_STREET_NUMBER_LENGTH_VALIDATION_TC_242|10000|The Premises Street Number must be a string with a maximum length of 12.|
#|PREMISES_STREET_PRE_DIRECTION_LENGTH_VALIDATION_TC_243|10000|The Premises Street PreDirection must be a string with a maximum length of 2.|
#|INVALID_PREMISES_STREET_PRE_DIRECTION_TC_243_2|2000|Invalid Request: Invalid Premises Street Pre Direction   |
#|PREMISES_STREET_NAME_LENGTH_VALIDATION_TC_244|10000|The Premises Street Name must be a string with a maximum length of 30.|
#|NULL_PREMISES_STREET_NAME_TC_245|10000|Invalid or missing Premises Street Name|
#|PREMISES_STREET_SUFFIX_LENGTH_VALIDATION_TC_246|10000|The Premises Street Suffix  must be a string with a maximum length of 6.|
#|INVALID_PREMISES_STREET_SUFFIX_TC_246A|2000|Invalid Request: Invalid Premises Street Suffix                  |
#|PREMISES_STREET_POST_DIRECTION_LENGTH_VALIDATION_TC_247|10000|The Premises Street PostDirection must be a string with a maximum length of 2.|
#|INVALID_PREMISES_STREET_POST_DIRECTION_TC_247A|2000|Invalid Request: Invalid Premises Street Post Direction  |
#|PREMISES_UNIT_TYPE_LENGTH_VALIDATION_TC_248|10000|The Premises Unit Type must be a string with a maximum length of 6.|
#|INVALID_PREMISES_UNIT_TYPE_TC_248A|2000|Invalid Request: Invalid Premises Unit Type                          |
#|PREMISES_UNIT_NUMBER_LENGTH_VALIDATION_TC_249|10000|The Premises Unit Number must be a string with a maximum length of 6.|
#|PREMISES_CITY_LENGTH_VALIDATION_TC_250|10000|The Premises City must be a string with a maximum length of 20.|
#|PREMISES_CITY_NULL_TC_251  |10000      |Invalid or missing Premises City                                     |
#|PREMISES_STATE_CODE_LENGTH_VALIDATION_TC_252|10000|The Premises State Code must be a string with a maximum length of 3.|
#|NULL_PREMISES_STATE_CODE_TC_253|10000  |Invalid or missing Premises State Code                               |
#    |INVALID_PREMISES_STATE_CODE_TC_253_2|2000|Invalid Request: Invalid Premises State Code                       |
# | NULL_PREMISES_ZIP_CODE_TC_255|10000   |Invalid or missing Premises Zip Code                                 |
#  |PREMISES_ZIP_CODE_NOT_A_NUMERIC_VALUE_OF_LENGTH_5_TC_255A|2000|Invalid Request: Invalid Premises Zip Code    |
#  |PREMISES_ZIP_CODE_LENGTH_VALIDATION_TC_254               |10000|The Premises ZipCode must be a string with a maximum length of 10.|
#  |INVALID_PREMISES_ZIP_FORMAT_TC_255B                      |2000 |Invalid Request: Invalid Premises Zip Code                        |
#  |NON_NUMERIC_PREMISES_ZIP_CODE_TC_255_2                   |2000 |Invalid Request: Invalid Premises Zip Code                        |
#  |INVALID_PREMISES_ZIP_CODE_TC_255C                        |2000 |Invalid Request: Invalid Premises Zip Code                        |
#|PREMISES_COUNTY_CODE_LENGTH_VALIDATION_TC_256|10000|The Premises County Code must be a string with a maximum length of 5.|
#    |INVALID_PREMISES_COUNTY_CODE_TC_256A         |2000 |Invalid Request: Invalid Premises County Code                        |
#    |NULL_PREMISES_COUNTY_CODE_TC_257             |10000|Invalid or missing Premises County Code                               |
#|INVALID_BOOLEAN_VALUE_SEPARATE_BILLING_ADDRESS_TC_258|10000|The JSON value could not be converted to System.Nullable`1[System.Boolean]. Path: $.separateBillingAddress [PIPE] LineNumber: 0 [PIPE] BytePositionInLine: 612.|
#|EMPTY_SEPARATE_BILLING_ADDRESS_TC_259                |10000|The JSON value could not be converted to System.Nullable`1[System.Boolean]. Path: $.separateBillingAddress [PIPE] LineNumber: 0 [PIPE] BytePositionInLine: 611.|
#|BILLING_ADDRESS_TYPE_LENGTH_VALIDATION_TC_260|10000|The Billing Address Type must be a string with a maximum length of 1.|
#|NULL_BILLING_ADDRESS_TYPE_TC_261|2000 |Invalid Request: Missing conditional parameters-Billing Address Type|
#|INVALID_BILLING_ADDRESS_TYPE_TC_262|2000|Invalid Request: Invalid Billing Address Type                       |
#|BILLING_STREET_NAME_LENGTH_VALIDATION_TC_263| 10000     | The Billing Street Name must be a string with a maximum length of 30.                                                            |
#|BILLING_STREET_NUMBER_LENGTH_VALIDATION_TC_264|10000|The Billing Street Number must be a string with a maximum length of 12.|
#|BILLING_STREET_PREDIRECTION_LENGTH_VALIDATION_TC_265|10000|The Billing Street PreDirection must be a string with a maximum length of 2.|
#|INVALID_BILLING_STREET_PREDIRECTION_TC_266|2000|Invalid Request: Invalid Billing Street Pre Direction          |
#|BILLING_STREET_SUFFIX_LENGTH_VALIDATION_TC_267|10000|The Billing Street Suffix must be a string with a maximum length of 6.|
#|INVALID_BILLING_STREET_SUFFIX_TC_268|2000|Invalid Request: Invalid Billing Street Suffix                     |
#|BILLING_STREET_POST_DIRECTION_LENGTH_VALIDATION_TC_269|10000|The Billing Street PostDirection must be a string with a maximum length of 2.|
#|INVALID_BILLING_STREET_POST_DIRECTION_TC_270|2000|Invalid Request: Invalid Billing Street Post Direction     |
#|BILLING_UNIT_TYPE_LENGTH_VALIDATION_TC_271|10000|The Billing Unit Type must be a string with a maximum length of 6.|
#|INVALID_BILLING_UNIT_TYPE_TC_272|2000  |Invalid Request: Invalid Billing Unit Type                            |
#|BILLING_UNIT_NUMBER_LENGTH_VALIDATION_TC_273|10000|The Billing Unit Number must be a string with a maximum length of 6.|
#|BILLING_RURAL_ROUTE_LENGTH_VALIDATION_TC_274|10000|The Billing Rural Route must be a string with a maximum length of 20.|
#|NULL_BILLING_RURAL_ROUTE_TC_274A|2000|Invalid Request: Invalid Billing Rural Route                         |
#|BILLING_RURAL_ROUTE_NUMBER_LENGTH_VALIDATION_TC_275|10000|The Billing Rural Route Number must be a string with a maximum length of 10.|
#|NULL_BILLING_RURAL_ROUTE_NUMBER_TC_275A|2000|Invalid Request: Missing conditional parameters-Billing Rural Route Number|
#|BILLING_PO_BOX_LENGTH_VALIDATION_TC_276|10000|The Billing PO Box must be a string with a maximum length of 10.|
#|NULL_BILLING_PO_BOX_TC_276A|2000       |Invalid Request: Invalid Billing PO Box Number                      |
#|BILLING_ADDRESS_LINE_LENGTH_VALIDATION_TC_277|10000|The Billing Address Line 2 must be a string with a maximum length of 30.|
#      |BILLING_ADDRESS_LINE_LENGTH_VALIDATION_FOR_RURAL_TC_277A|10000|The Billing Address Line 2 must be a string with a maximum length of 30.|
#|BILLLING_ADDRESS_LINE_LENGTH_VALIDATION_FOR_POBOX_TC_277B|10000|The Billing Address Line 2 must be a string with a maximum length of 30.|
#|BILLING_CITY_LENGTH_VALIDATION_TC_278                    |10000|The Billing City must be a string with a maximum length of 20.          |
#|BILLING_CITY_LENGTH_VALIDATION_RURAL_TC_278A|10000|The Billing City must be a string with a maximum length of 20.          |
#|BILLING_CITY_LENGTH_VALIDATION_POBOX_TC_278B|10000|The Billing City must be a string with a maximum length of 20.          |
#|BILLING_STATE_CODE_LENGTH_VALIDATION_TC_279 |10000|The Billing State Code must be a string with a maximum length of 3.|
#|Billing_STATE_CODE_LENGTH_VALIDATION_RURAL_TC_279A|10000|The Billing State Code must be a string with a maximum length of 3.|
#|BILLING_STATE_CODE_LENGTH_VALIDATION_POBOX_TC_279B|10000|The Billing State Code must be a string with a maximum length of 3.|
#|INVALID_BILLING_STATE_CODE_TC_280                 |2000|Invalid Request: Invalid Billing State Code|
#|INVALID_BILLING_STATE_CODE_RURAL_TC_280A|2000|Invalid Request: Invalid Billing State Code|
#|INVALID_BILLING_STATE_CODE_POBOX_TC_280B|2000|Invalid Request: Invalid Billing State Code|
#|INVALID_BILLING_ZIP_CODE_TC_281_1         |2000|Invalid Request: Invalid Billing Zip Code  |
#      |INVALID_BILLING_ZIP_CODE_TC_281_2         |2000|Invalid Request: Invalid Billing Zip Code  |
#|INVALID_BILLING_ZIP_CODE_RURAL_TC_281A_2|2000|Invalid Request: Invalid Billing Zip Code  |
#|INVALID_BILLING_ZIP_CODE_RURAL_TC_281A_1|2000|Invalid Request: Invalid Billing Zip Code  |
#|INVALID_BILLING_ZIP_CODE_POBOX_TC_281B_1|2000|Invalid Request: Invalid Billing Zip Code  |
#      |INVALID_BILLING_ZIP_CODE_POBOX_TC_281B_2|2000|Invalid Request: Invalid Billing Zip Code  |
#|NULL_BILLING_ZIP_CODE_TC_281C|2000     |Invalid Request: Missing conditional parameters-Billing Zip Code                      |
#      |NULL_BILLING_ZIP_CODE_RURAL_TC_281D|2000     |Invalid Request: Missing conditional parameters-Billing Zip Code                      |
#|NULL_BILLING_ZIP_CODE_POBOX_TC_281E|2000     |Invalid Request: Missing conditional parameters-Billing Zip Code                      |
#|BILLING_COUNTY_CODE_LENGTH_VALIDATION_TC_282|10000|The Billing County Code must be a string with a maximum length of 5.|
#|BILLING_COUNTY_CODE_RURAL_LENGTH_VALIDATION_TC_282A|10000|The Billing County Code must be a string with a maximum length of 5.|
#|BILLING_COUNTY_CODE_POBOX_LENGTH_VALIDATION_TC_282B|10000|The Billing County Code must be a string with a maximum length of 5.|
#|INVALID_BILLING_COUNTY_CODE_TC_283|2000|Invalid Request: Invalid Billing County Code                         |
#|INVALID_BILLING_COUNTY_CODE_RURAL_TC_283A|2000|Invalid Request: Invalid Billing County Code                         |
|INVALID_BILLING_POBOX_COUNTY_CODE_TC_283B|2000|Invalid Request: Invalid Billing County Code                         |

  @GetEligiblePlansAndOffersInvalidCallerIdAndCallerIdNotAvailableOptionCombinationTNON @Phase1 @NegativeFlow
  Scenario Outline: Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api with enrollment "<testCondition>" Sources
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition             | errorCode | errorMessage                                                        |
      |   INVALID_LENGTH_CALLER_ID_TC_204|10000     |The Caller ID must be a string with a maximum length of 10.|
      |   NON_NUMERIC_CALLER_ID_TC_205|2000     |Invalid Request: Invalid Caller ID|
  |CALLER_ID_MISSING_TC_206   |2000       |Invalid Request: Missing conditional parameters-Caller ID            |
|INVALID_VALUE_FOR_CALLER_ID_NOT_AVAILABLE_TC_207|10000|The JSON value could not be converted to System.Boolean. Path: $.callerIDNotAvailable [PIPE] LineNumber: 0 [PIPE] BytePositionInLine: 444.|
 |CALLER_ID_NOT_AVAILABLE_TRUE_BUT_VALUE_IS_PROVIDED_TC_208|2000|Invalid Request: Invalid Caller ID Not Available|
      |ADDITIONAL_ENROLLMENT_DATA_LENGTH_VALIDATION_TC_209|10000|The Additional Enrollment Data must be a string with a maximum length of 30.|
|SSP_INDICATOR_NOT_PROVIDED_TC_210|10000|Seasonal Saving Program Indicator Not Provided|
|INVALID_SSP_INDICATOR_VALUE_TC_211|10000|The JSON value could not be converted to System.Nullable`1[System.Boolean]. Path: $.seasonalSavingsProgramIndicator [PIPE] LineNumber: 0 [PIPE] BytePositionInLine: 711.|

  @GetEligiblePlansAndOffersInvalidCustomerLastNameTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api with customer "<testCondition>" LastName
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition         | errorCode | errorMessage                                                       |
      | EMPTY_CUSTOMER_LAST_NAME_TC_217 | 2000      | Invalid Request: Missing conditional parameters-Customer Last Name |


  @GetEligiblePlansAndOffersInvalidTenantLandlordTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<TenantLandlord>"
    When a request is made to the GetEligiblePlansAndOffers Api with tenant "<TenantLandlord>" Landlord
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | TenantLandlord             | errorCode | errorMessage                                                     |
      | EMPTY_TENANT_LANDLORD      | 10000     | The Tenant/Landlord must be a string with a maximum length of 1. |
      | SPL_CHAR_TENANT_LANDLORD   | 10000     | The Tenant/Landlord must be a string with a maximum length of 1. |
      | MAX_LENGTH_TENANT_LANDLORD | 10000     | The Tenant/Landlord must be a string with a maximum length of 1. |
      | LOWERCASE_TENANT_LANDLORD  | 10000     | The Tenant/Landlord must be a string with a maximum length of 1. |
      | NUMERIC_TENANT_LANDLORD    | 10000     | The Tenant/Landlord must be a string with a maximum length of 1. |

  @GetEligiblePlansAndOffersTC25UC53 @Phase1
  Scenario: Verify response code Commercial marketer switch  TC_25
    When a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_25
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 11116 and ErrorMessage "No match found.  Please see  the list of similar businesses found"


  @GetEligiblePlansAndOffersTC26UC54PositiveMKSW @Phase1 @HappyFlow
  Scenario: Verify response code Commercial marketer switch  TC_26
    When a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_26
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200

  @GetEligiblePlansAndOffersTC27UC54aMKSW @Phase1 @HappyFlow
  Scenario: Verify response code Commercial marketer switch  TC_27
    When a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_27
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200

  @GetEligiblePlansAndOffersTC28UCN/AMKSW @Phase1 @HappyFlow
  Scenario: Verify response code Commercial marketer switch  TC_28
    When a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_28
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200

  @GetEligiblePlansAndOffersTC29UC61MKSW @Phase1 @HappyFlow
  Scenario: Verify response code Commercial marketer switch  TC_29
    When a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_29
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200

  @GetEligiblePlansAndOffersTC30UC60MKSW @Phase1 @HappyFlow
  Scenario: Verify response code Commercial marketer switch  TC_30
    When a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_30
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200

  @GetEligiblePlansAndOffersTC31UC69MKSW @Phase1 @HappyFlow
  Scenario: Verify response code Commercial marketer switch  TC_31
    When a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_31
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200

  @GetEligiblePlansAndOffers_RSTC11UC50MKSW @Phase1 @HappyFlow
  Scenario: Verify response code Residential marketer switch  RSTC11UC50
    When a request is made to the GetEligiblePlansAndOffers Api with Residential marketer switch  RSTC11UC50
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200


  @SSPValidations @NegativeFlow @Phase1
  Scenario: GetEligiblePlansAndOffersAPISSPValidations - Verify the SSP validation for SSP_TRUE_NOT_ALLOWED_FOR_ACN_TC_476
    When a request is made to the GetEligiblePlansAndOffers Api for "SSP_TRUE_NOT_ALLOWED_FOR_ACN_TC_476" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 2100 and ErrorMessage "WARNING:Seasonal Savings Program Participation is not allowed for ACN premises"


  @SSPValidations @HappyFlow @Phase1
  Scenario: GetEligiblePlansAndOffersAPISSPValidations - Verify the SSP validation for SSP_FALSE_ALLOWED_FOR_ACN_TC_477
    When a request is made to the GetEligiblePlansAndOffers Api for "SSP_FALSE_ALLOWED_FOR_ACN_TC_477" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
