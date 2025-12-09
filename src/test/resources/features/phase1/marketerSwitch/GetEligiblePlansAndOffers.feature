Feature: Verify MarketSwitch GetEligiblePlansAndOffers API

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetEligiblePlansAndOffersMarketerSwitchNegative @NegativeFlow @Phase2
  Scenario Outline: Verify market switch GetEligiblePlansAndOffers with invalid parameters "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api negative marketerSwitch for "<testCondition>" condition
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Then verify the marketer switch enrollment record for "<testCondition>" condition
    Examples:
      | testCondition                                                       | errorCode | errorMessage                                                                                                                                                                                                                        |
      | GE_MRK_SW_VAL_MISSING_TRANSACTION_TYPE_TC04                         | 10000     | Missing Transaction Type                                                                                                                                                                                                            |
      | GE_MRK_SW_VAL_TRANSACTION_TYPE_MAX_LENGTH_TC05                      | 10000     | The Transaction Type must be a string with a maximum length of 4                                                                                                                                                                    |
      | GE_MRK_SW_VAL_INVALID_TRANSACTION_TYPE_TC06                         | 1000      | Invalid Request: Invalid Transaction Type                                                                                                                                                                                           |
      | GE_MRK_SW_VAL_MISSING_AGLC_ACCOUNT_NUMBER_TC07                      | 10000     | The AGLC Account Number is required for Marketer Switch transaction type                                                                                                                                                            |
      | GE_MRK_SW_VAL_MISSING_AGLC_SERVICE_LOCATION_ID_TC08                 | 10000     | Missing AGLC Service Location ID                                                                                                                                                                                                    |
      | GE_MRK_SW_RS_NEW_CC_YES_INVALID_SSN_FRAUD_ALERT_11114_TC09          | 11114     | Identification Verification Required.  Ask customer to mail or fax photo ID, copy of SS card to:  Georgia Natural Gas Attn: Back Office Team PO Box 440667 Kennesaw, GA 30160-9512 FAX: 877-281-5775 Email: customerservice@gng.com |
      | GE_MRK_SW_RS_NEW_CC_YES_NO_RECORD_CONFIRM_FALSE_11112_TC10          | 11112     | CUSTOMER NOT FOUND,  PLEASE CHECK SPELLING of CUSTOMER NAME and SSN                                                                                                                                                                 |

      # bug 1311 logged not expected result
      | GE_MRK_SW_RS_NEW_CC_YES_UC50_ALT_PATH_TC12                          | 11114     | Identification Verification Required.  Ask customer to mail or fax photo ID, copy of SS card to:  Georgia Natural Gas Attn: Back Office Team PO Box 440667 Kennesaw, GA 30160-9512 FAX: 877-281-5775 Email: customerservice@gng.com |
      | GE_MRK_SW_RS_NEW_CC_YES_UC52_TC13                                   | 11114     | Identification Verification Required.  Ask customer to mail or fax photo ID, copy of ss card to:  Georgia Natural Gas Attention: Consumer Relations PO Box 78760 Atlanta GA 30357 Fax: 404 685 - 4117                               |
      | GE_MRK_SW_RS_NEW_CC_YES_UC63_TC14                                   | 11113     | Credit file blocked by consumer.  Inform customer to contact Experian regarding the credit block at 888-397-3742.  DO NOT override denial.                                                                                          |
      | GE_MRK_SW_RS_NEW_CC_YES_UC47_TC15                                   | 3000      |  The customer's enrollment request is denied due to past payment history.                                                                                                                                                            |
      | GE_MRK_SW_RS_NEW_CC_NO_DENIED_BY_CUSTOMER_TC16                      | 3000      | The customer's enrollment request is denied                                                                                                                                                                                          |
      | GE_MRK_SW_CM_NEW_CC_YES_UC53_TC25                                   | 11116     | No match found.  Please see  the list of similar businesses found                                                                                                                                                                    |

  @GetEligiblePlansAndOffersMarketerSwitchPositive @HappyFLow @Phase2
  Scenario Outline: Verify market switch GetEligiblePlansAndOffers with valid parameters "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api marketerSwitch for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the marketer switch response should contain the expected plans
    And verify the marketer switch response does not contain disallowed plans for "<testCondition>" condition
    Then verify the marketer switch enrollment record for "<testCondition>" condition
    Examples:
    | testCondition                                                       |
    | GE_MRK_SW_RS_NEW_CC_YES_TIER1_VALUE100_CREDIT700_999_SSP_FALSE_TC17 |
    | GE_MRK_SW_RS_NEW_CC_YES_TIER2_VALUE101_CREDIT600_699_SSP_FALSE_TC18 |
    | GE_MRK_SW_RS_NEW_CC_YES_TIER_UC45_TC19                              |
    | GE_MRK_SW_RS_NEW_CC_COMM_CREDIT_OPTION_9998_TC20                    |
    | GE_MRK_SW_RS_NEW_CC_YES_ACN_LANDLORD_BYPASS_CREDIT_TC21             |
    | GE_MRK_SW_CM_NEW_CC_YES_BIN_NULL_NO_MATCH_CONTINUE_TC26             |
    |  GE_MRK_SW_CM_NEW_CC_YES_BIN_NOT_NULL_SELECT_SIMILAR_BUSINESS_TC27  |
   | GE_MRK_SW_CM_NEW_CC_YES_TIER_EXCELLENT_VALUE200_CREDIT50_100_TC28    |
   | GE_MRK_SW_CM_NEW_CC_YES_COMM_DEPOSIT_PROSPECT_UC72_TC29              |
   | GE_MRK_SW_CM_CRDS_CC_YES_COMM_DEPOSIT_PROSPECT_UC72_TC31             |


  @GetEligiblePlansAndOffersMarketerSwitchWithSaveEnrollmentTwicePositive @HappyFLow @Phase2
  Scenario Outline: Verify meter set GetEligiblePlansAndOffers and SaveEnrollment and SearchAccounts for a complete enrollment with valid parameters "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api from customer file marketerSwitch to seed data for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the marketer switch response should contain the expected plans
    Then a request is made to the SaveEnrollment Api for external marketerSwitch calls with valid parameters for "<testCondition>" condition
    And verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the MarketerSwitch SearchAccounts Api from GetEligiblePlansAndOffers API for external cases for "<testCondition>" condition
    And verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 1
    And response should have "recordType" as "ENROLLMENT RECORD"
    Then a request is made to the GetEligiblePlansAndOffers Api second call marketerSwitch for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the marketer switch response should contain the expected plans
    And verify the marketer switch response does not contain disallowed plans for "<testCondition>" condition
    Then a request is made to the SaveEnrollment Api for external marketerSwitch second calls with valid parameters for "<testCondition>" condition
    And verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then verify the marketer switch enrollment record for "<testCondition>" condition

    Examples:
      | testCondition                                         |
      | GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22                     |
      | GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24 |

  @GetEligiblePlansAndOffersMarketerSwitchSecondCallPositive @HappyFLow @Phase2
  Scenario Outline: Verify marketer switch GetEligiblePlansAndOffers with valid parameters "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api from customer file marketerSwitch to seed data for "<testCondition>" condition
    Then a request is made to the GetEligiblePlansAndOffers Api second call marketerSwitch for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the marketer switch response should contain the expected plans
    And verify the marketer switch response does not contain disallowed plans for "<testCondition>" condition
    Examples:
      | testCondition                                             |
      | GE_MRK_SW_RS_NEW_CC_YES_MULTI_PREMISES_MATCH_UC71_TC23    |
      |  GE_MRK_SW_CM_CRDS_CC_YES_COMM_DEPOSIT_PROSPECT_UC73_TC30 |

  @GetEligiblePlansAndOffersTwiceWithSaveEnrollmentPositive @HappyFLow @Phase2
  Scenario Outline: Verify meter set GetEligiblePlansAndOffers and SaveEnrollment and SearchAccounts for a complete enrollment with valid parameters "<testCondition>"
    When a request is made to the GetEligiblePlansAndOffers Api from customer file marketerSwitch to seed data for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the marketer switch response should contain the expected plans
    Then a request is made to the SaveEnrollment Api for external marketerSwitch calls with valid parameters for "<testCondition>" condition
    And verify response code of "SaveEnrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the MarketerSwitch SearchAccounts Api from GetEligiblePlansAndOffers API for external cases for "<testCondition>" condition
    And verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 1
    And response should have "recordType" as "ENROLLMENT RECORD"
    Then a request is made to the GetEligiblePlansAndOffers Api second call marketerSwitch for "<testCondition>" condition
    And verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the marketer switch response should contain the expected plans
    And verify the marketer switch response does not contain disallowed plans for "<testCondition>" condition
    Examples:
      | testCondition                                                               |
      | GE_MRK_SW_CM_CRDS_CC_YES_PROMO_DEPOSIT_REQUIRED_VALUE210_CREDIT0_49_TC32    |
      | GE_MRK_SW_CM_CRDS_CC_YES_DEPOSIT_REQUIRED_BUSINESS_NAME_POPULATED_UC42_TC33 |
