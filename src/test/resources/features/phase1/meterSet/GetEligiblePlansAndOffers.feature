Feature: Verify MeterSet GetEligiblePlansAndOffers API

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetEligiblePlansAndOffersMeterSetNegative @NegativeFlow @Phase1
  Scenario Outline: Verify meter set GetEligiblePlansAndOffers with invalid parameters "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api negative meterSet for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | testCondition                                                      | errorCode | errorMessage                                                    |
   # | MS_GE_MISSING_TRANSACTION_TYPE_TC_006                              | 10000 | Missing Transaction Type                                            |
    #| MS_GE_TRANSACTION_TYPE_MAX_LENGTH_TC_007                           | 10000 | The Transaction Type must be a string with a maximum length of 4    |
    #| MS_GE_TRANSACTION_TYPE_INVALID_TC_008                              | 1000  | Invalid Request: Invalid Transaction Type                           |
    #| MS_GE_AGLC_ACCOUNT_PROVIDED_TC_009                                 | 2200  | Parameter Value should be null-AGLC Account Number                  |


    #| MS_RS_FRAUD_ALERT_INVALID_SSN_TC_010 | 11114     | Identification Verification Required.  Ask customer to mail or fax photo ID, copy of SS card to:  Georgia Natural Gas Attn: Back Office Team PO Box 440667 Kennesaw, GA 30160-9512 FAX: 877-281-5775 Email: customerservice@gng.com |
    #| MS_RS_NO_MATCH_NO_CONFIRM_TC_011     | 11112     | CUSTOMER NOT FOUND,  PLEASE CHECK SPELLING of CUSTOMER NAME and SSN                                                                                                                                   |
      #| MS_RS_NO_RECORD_FOUND_TC_013                 | 11114     | Identification Verification Required.  Ask customer to mail or fax photo ID, copy of SS card to:  Georgia Natural Gas Attn: Back Office Team PO Box 440667 Kennesaw, GA 30160-9512 FAX: 877-281-5775 Email: customerservice@gng.com |
     # | MS_RS_VARIANT_TC_014                 | 11114     | Identification Verification Required.  Ask customer to mail or fax photo ID, copy of SS card to:  Georgia Natural Gas Attn: Back Office Team PO Box 440667 Kennesaw, GA 30160-9512 FAX: 877-281-5775 Email: customerservice@gng.com |
     # | MS_RS_VARIANT_TC_015                 | 11114     | Identification Verification Required.  Ask customer to mail or fax photo ID, copy of SS card to:  Georgia Natural Gas Attn: Back Office Team PO Box 440667 Kennesaw, GA 30160-9512 FAX: 877-281-5775 Email: customerservice@gng.com |
     # | MS_RS_DENIAL_DUE_TC_016                 | 11114     | Identification Verification Required.  Ask customer to mail or fax photo ID, copy of SS card to:  Georgia Natural Gas Attn: Back Office Team PO Box 440667 Kennesaw, GA 30160-9512 FAX: 877-281-5775 Email: customerservice@gng.com |



  @GetEligiblePlansAndOffersMeterSetPositive @HappyFlow @Phase1
  Scenario Outline: Verify meter set GetEligiblePlansAndOffers with valid parameters "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api negative meterSet to seed data for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    Then a request is made to the GetEligiblePlansAndOffers Api negative meterSet for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                        | errorCode | errorMessage |
      #| MS_RS_NO_RECORD_FOUND_TC_013                 | 11114     | Identification Verification Required.  Ask customer to mail or fax photo ID, copy of SS card to:  Georgia Natural Gas Attn: Back Office Team PO Box 440667 Kennesaw, GA 30160-9512 FAX: 877-281-5775 Email: customerservice@gng.com |
     #| MS_RS_DENIAL_DUE_TC_016                 | 11113     |   The customer's enrollment request is denied due to past payment history |



  @GetEligiblePlansAndOffersMeterSetNegative @NegativeFlow @Phase1
  Scenario Outline: Verify meter set GetEligiblePlansAndOffers with invalid parameters "<testCondition>"
    When a request is made to the MeterSet SearchAccounts Api for external cases for "<testCondition>" condition
    Then verify response code of "SearchAccounts" Api is 200
    Then a request is made to the GetEligiblePlansAndOffers Api negative meterSet to seed data for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    Then a request is made to the GetEligiblePlansAndOffers Api negative meterSet for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    Then a request is made to the GetEligiblePlansAndOffers Api negative meterSet for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                        | errorCode | errorMessage |
     | MS_RS_NO_RECORD_FOUND_TC_013                 | 11114     | Identification Verification Required.  Ask customer to mail or fax photo ID, copy of SS card to:  Georgia Natural Gas Attn: Back Office Team PO Box 440667 Kennesaw, GA 30160-9512 FAX: 877-281-5775 Email: customerservice@gng.com |
     #| MS_RS_VARIANT_TC_014                 | 11114     |   Identification Verification Required.  Ask customer to mail or fax photo ID, copy of SS card to:  Georgia Natural Gas Attn: Back Office Team PO Box 440667 Kennesaw, GA 30160-9512 FAX: 877-281-5775 Email: customerservice@gng.com|
#
#
     #| MS_RS_CREDIT_FREEZE_TC_015                 | 11113     |   Credit file blocked by consumer.  Inform customer to contact Experian regarding the credit block at 888-397-3742.  DO NOT override denial.|
     # | MS_RS_DENIAL_DUE_TC_016                 | 11113     |   The customer's enrollment request is denied due to past payment history |





  @GetEligiblePlansAndOffersTransferMeterSetNegativeCustomerFile @NegativeFlow @Phase1
  Scenario Outline: Verify meter set GetEligiblePlansAndOffers with invalid parameters "<testCondition>"
    #When a request is made to the GetEligiblePlansAndOffers Api negative meterSet to seed data for "<testCondition>" condition

    When a request is made to the GetEligiblePlansAndOffers Api negative meterSet for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                           | errorCode | errorMessage                                                    |
      | MS_RS_DENIAL_DUE_TC_016                 | 11113     |   The customer's enrollment request is denied due to past payment history |
      #| MS_RS_CUSTOMER_NO_AUTH_TC_017                 | 3000     |The customer's enrollment request is denied |

      | MS_RS_CREDIT_FREEZE_TC_015                 | 11113     |   Credit file blocked by consumer.  Inform customer to contact Experian regarding the credit block at 888-397-3742.  DO NOT override denial.|


  @GetEligiblePlansAndOffersMeterSetPositive @HappyFLow @Phase1
  Scenario Outline: Verify meter set GetEligiblePlansAndOffers with valid parameters "<testCondition>"
    #When a request is made to the GetEligiblePlansAndOffers Api negative meterSet to seed data for "<testCondition>" condition
    When a request is made to the GetEligiblePlansAndOffers Api meterSet for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the meter set response should contain the expected plans
    And verify the response does not contain disallowed plans for "<testCondition>" condition


    Examples:
      | testCondition                          |

      # 7 instead of 9 plans returned ftd
      #| MS_GE_TIER_1_NE_TC_018                 |
      #| MS_GE_TIER_2_NE_TC_019                 |
     # | MS_GE_TIER_9_NE_TC_020                 |
      #| MS_GE_RS_COMM_VS_150_TC_021            |

  @GetEligiblePlansAndOffersMeterSetWithSaveEnrollmentPositive @HappyFLow @Phase1
  Scenario Outline: Verify meter set GetEligiblePlansAndOffers and SaveEnrollment and SearchAccounts with valid parameters "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api from customer file meterSet to seed data for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the meter set response should contain the expected plans
    Then a request is made to the SaveEnrollment Api for external meterSet calls with valid parameters for "<testCondition>" condition
    And verify response code of "SaveEnrollment" Api is 200
    Then a request is made to the MeterSet SearchAccounts Api from GetEligiblePlansAndOffers API for external cases for "<testCondition>" condition
    And verify response code of "SearchAccounts" Api is 200
    And response should return numberOfMatches as 1
    And response should have "recordType" as "ENROLLMENT RECORD"
    Then a request is made to the GetEligiblePlansAndOffers Api meterSet for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the meter set response should contain the expected plans
    And verify the response does not contain disallowed plans for "<testCondition>" condition

    Examples:
      | testCondition               |

      #| MS_GE_RS_COMM_VS_150_TC_021            |

       #| MS_GE_RS_ACN_LAND_BYPASS_CREDIT_TC_022| worked add uzbenro select * from uzbenro t where t.uzbenro_cust_code = 6125044
       #        where UZBENRO_CRED_SCORE_TEXT = '' and UZBENRO_CRED_SCORE_Status = 'ACNL'

      #| MS_RS_MULTIPLE_PREM_TC_024  | worked

  @GetEligiblePlansAndOffersMeterSetWithSaveEnrollmentTwicePositive @HappyFLow @Phase1
  Scenario Outline: Verify meter set GetEligiblePlansAndOffers and SaveEnrollment and SearchAccounts for a complete enrollment with valid parameters "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api from customer file meterSet to seed data for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the meter set response should contain the expected plans
    And verify the response does not contain disallowed plans for "<testCondition>" condition
    Then a request is made to the SaveEnrollment Api for external meterSet calls with valid parameters for "<testCondition>" condition
    And verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the MeterSet SearchAccounts Api from GetEligiblePlansAndOffers API for external cases for "<testCondition>" condition
    And verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 1
    And response should have "recordType" as "ENROLLMENT RECORD"
    Then a request is made to the GetEligiblePlansAndOffers Api meterSet for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the meter set response should contain the expected plans
    And verify the response does not contain disallowed plans for "<testCondition>" condition
    Then a request is made to the SaveEnrollment Api for external meterSet second calls with valid parameters for "<testCondition>" condition
    And verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

    Examples:
      | testCondition                             |
      #|MS_GE_RS_INCL_TIER_5_TC_023  |
     # |MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025  |





