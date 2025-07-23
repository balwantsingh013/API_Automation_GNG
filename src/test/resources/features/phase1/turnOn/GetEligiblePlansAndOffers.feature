Feature: Verify GetEligiblePlansAndOffers Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetEligiblePlansAndOffersWithCustomerTypeResidentialCreditCheckAsYesAndPromotionCodeAsNullTC318UC39 @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with customer type residential credit check as yes and promotion code as null TC 318 UC 39
    When  a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes and promotion code as null TC_318_UC 39
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should return numberOfMatches as 12
    And the response should contain the following plans:
      | planCode | planDescription                   |
      | MVS      | Variable Select                   |
      |CSV       |MVS with Price Protection Guarantee|
      | MI       | Market Intro                      |
      | RGB      | Guaranteed Bill                   |
      | GPP      | 12-Month Fixed                    |
      | 24M      | 24-Month Fixed                    |
      | 18M      | 18-Month Fixed                    |
      | RF6      | 6-Month Fixed                     |
      | TRD      | Volume Discount                   |
      | PGB      | Pre-Pay Guaranteed Bill           |
      | PRP      | Pre-Pay                           |

  @GetEligiblePlansAndOffersWithCustomerTypeResidentialCreditCheckAsYesWithNoPromotionCodeTC319UC44 @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code TC 319 UC 44
    When a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code  TC_319_UC 44
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should return numberOfMatches as 12
    And the response should contain the following plans:
      | planCode | planDescription                     |
      | MVS      | Variable Select                     |
      | CSV      | MVS with Price Protection Guarantee |
      | MI       | Market Intro                        |
      | RGB      | Guaranteed Bill                     |
      | GPP      | 12-Month Fixed                      |
      | 24M      | 24-Month Fixed                      |
      | 18M      | 18-Month Fixed                      |
      | RF6      | 6-Month Fixed                       |
      | TRD      | Volume Discount                     |
      | PGB      | Pre-Pay Guaranteed Bill             |
      | PRP      | Pre-Pay                             |

  @GetEligiblePlansAndOffersWithCustomerTypeResidentialCreditCheckAsYesWithNoPromotionCodeTC320UC46 @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code TC 320 UC 46
    When a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes yes with no promotion code  TC_320_UC 46
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should return numberOfMatches as 12
    And the response should contain the following plans:
      | planCode | planDescription                     |
      | MVS      | Variable Select                     |
      | CSV      | MVS with Price Protection Guarantee |
      | MI       | Market Intro                        |
      | RGB      | Guaranteed Bill                     |
      | GPP      | 12-Month Fixed                      |
      | 24M      | 24-Month Fixed                      |
      | 18M      | 18-Month Fixed                      |
      | RF6      | 6-Month Fixed                       |
      | TRD      | Volume Discount                     |
      | PGB      | Pre-Pay Guaranteed Bill             |
      | PRP      | Pre-Pay                             |

  @GetEligiblePlansAndOffersWithCustomerTypeResidentialCreditCheckAsYesWithPromotionCodeTC321UC64 @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with customer type residential credit check as yes with  promotion code TC 321 UC 64
    When a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with  promotion code  TC_321_UC 64
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should return numberOfMatches as 10
    And the response should contain the following plans:
      | planCode | planDescription                     |
      | MVS      | Variable Select                     |
      | CSV      | MVS with Price Protection Guarantee |
      | MI       | Market Intro                        |
      | RGB      | Guaranteed Bill                     |
      | GPP      | 12-Month Fixed                      |
      | 24M      | 24-Month Fixed                      |
      | 18M      | 18-Month Fixed                      |
      | RF6      | 6-Month Fixed                       |
      | PGB      | Pre-Pay Guaranteed Bill             |
      | PRP      | Pre-Pay                             |

  @GetEligiblePlansAndOffersWithCustomerTypeResidentialCreditCheckAsYesWithPromotionCodeTC322UC45 @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code TC 322 UC 45
    When a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code  TC_322_UC 45
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should return numberOfMatches as 8
    And the response should contain the following plans:
      | planCode | planDescription                     |
      | MVS      | Variable Select                     |
      | CSV      | MVS with Price Protection Guarantee |
      | MI       | Market Intro                        |
      | RGB      | Guaranteed Bill                     |
      | TRD      | Volume Discount                     |
      | PGB      | Pre-Pay Guaranteed Bill             |
      | PRP      | Pre-Pay                             |

  @GetEligiblePlansAndOffersWithCustomerTypeResidentialCreditCheckAsYesWithPromotionCodeTC323UCNA @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code TC 323 UC NA
    When a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code  TC_323_UC NA
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should return numberOfMatches as 12
    And the response should contain the following plans:
      | planCode | planDescription                     |
      | MVS      | Variable Select                     |
      | CSV      | MVS with Price Protection Guarantee |
      | MI       | Market Intro                        |
      | RGB      | Guaranteed Bill                     |
      | GPP      | 12-Month Fixed                      |
      | 24M      | 24-Month Fixed                      |
      | 18M      | 18-Month Fixed                      |
      | RF6      | 6-Month Fixed                       |
      | TRD      | Volume Discount                     |
      | PGB      | Pre-Pay Guaranteed Bill             |
      | PRP      | Pre-Pay                             |

  @GetEligiblePlansAndOffersWithCustomerTypeResidentialCreditCheckAsYesWithPromotionCodeTC324UCNA @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code TC 324 UC NA
    When a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code  TC_324_UC NA
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should return numberOfMatches as 3
    And the response should contain the following plans:
      | planCode | planDescription         |
      | VML      | Variable Market Limited |
      | PGB      | Pre-Pay Guaranteed Bill |
      | PRP      | Pre-Pay                 |

  @GetEligiblePlansAndOffersWithCustomerTypeResidentialCreditCheckAsServiceTransferWithNoPromotionCodeTC325UC55 @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with customer type residential credit check as Service Transfer with no promotion code TC 325 UC 55
    When a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as Service Transfer with no promotion code  TC_325_UC 55
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should return numberOfMatches as 13
    And the response should contain the following plans:
      | planCode | planDescription                     |
      | MVS      | Variable Select                     |
      | CSV      | MVS with Price Protection Guarantee |
      | MI       | Market Intro                        |
      | RGB      | Guaranteed Bill                     |
      | GPP      | 12-Month Fixed                      |
      | 24M      | 24-Month Fixed                      |
      | 18M      | 18-Month Fixed                      |
      | RF6      | 6-Month Fixed                       |
      | TRD      | Volume Discount                     |
      | PGB      | Pre-Pay Guaranteed Bill             |
      | PRP      | Pre-Pay                             |
      | VML      | Variable Market Limited             |

  @GetEligiblePlansAndOffersWithCustomerTypeResidentialCreditCheckAsCommWithNoPromotionCodeTC326UC56 @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with customer type residential credit check as Comm with no promotion code TC 326 UC 56
    When a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as Comm with no promotion code  TC_326_UC 56
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should return numberOfMatches as 12
    And the response should contain the following plans:
      | planCode | planDescription                     |
      | MVS      | Variable Select                     |
      | CSV      | MVS with Price Protection Guarantee |
      | MI       | Market Intro                        |
      | RGB      | Guaranteed Bill                     |
      | GPP      | 12-Month Fixed                      |
      | 24M      | 24-Month Fixed                      |
      | 18M      | 18-Month Fixed                      |
      | RF6      | 6-Month Fixed                       |
      | TRD      | Volume Discount                     |
      | PGB      | Pre-Pay Guaranteed Bill             |
      | PRP      | Pre-Pay                             |

  @GetEligiblePlansAndOffersWithCustomerTypeResidentialCreditCheckAsYesWithPromotionCodeTC327UC40  @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with customer type residential credit check as yes with  promotion code TC 327 UC 40
    When a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with  promotion code  TC_327_UC 40
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should return numberOfMatches as 8
    And the response should contain the following plans:
      | planCode | planDescription                     |
      | MVS      | Variable Select                     |
      | CSV      | MVS with Price Protection Guarantee |
      | MI       | Market Intro                        |
      | RGB      | Guaranteed Bill                     |
      | GPP      | 12-Month Fixed                      |
      | 24M      | 24-Month Fixed                      |
      | 18M      | 18-Month Fixed                      |
      | RF6      | 6-Month Fixed                       |

  @GetEligiblePlansAndOffersWithCustomerTypeResidentialCreditCheckAsYesWithPromotionCodeTC328UCNA @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code TC 328 UC NA
    When a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code  TC_328_UC NA
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should return numberOfMatches as 8
    And the response should contain the following plans:
      | planCode | planDescription                     |
      | MVS      | Variable Select                     |
      | CSV      | MVS with Price Protection Guarantee |
      | MI       | Market Intro                        |
      | RGB      | Guaranteed Bill                     |
      | GPP      | 12-Month Fixed                      |
      | 24M      | 24-Month Fixed                      |
      | 18M      | 18-Month Fixed                      |
      | RF6      | 6-Month Fixed                       |

  @GetEligiblePlansAndOffersWithCustomerTypeCommercialPositive @Phase1 @HappyFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with customer type commercial for <testCondition>
    When a request is made to the GetEligiblePlansAndOffers Api with customer type commercial for "<testCondition>"
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And the response should contain the expected plans for "<testCondition>" condition

  Examples:
    |testCondition                                              |
    |COMMERCIAL_CREDIT_CHECK_YES_TC_339                         |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_340          |
    |COMMERCIAL_CREDIT_CHECK_SKIP_NEW_ENROLLMENT_TC_341         |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_342          |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_343          |
    |COMMERCIAL_CREDIT_CHECK_SERV_TRANSFER_NEW_ENROLLMENT_TC_344|
    |COMMERCIAL_CREDIT_CHECK_MULT_NEW_ENROLLMENT_TC_345         |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_346          |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_347          |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_348          |
    |COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_349          |
    |COMMERCIAL_CREDIT_CHECK_YES_INCL_ENROLLMENT_TC_350         |
    |COMMERCIAL_CREDIT_CHECK_YES_CRDS_ENROLLMENT_TC_350B        |
    |COMMERCIAL_CREDIT_CHECK_YES_CRDS_ENROLLMENT_TC_350E        |


  @GetEligiblePlansAndOffersWithInvalidRequestIDTNON @Phase1 @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid requestID "<requestID>"TC155_157
    When a request is made to the GetEligiblePlansAndOffers Api with "<requestID>"TC155_157
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID            | errorCode | errorMessage         |
      | NULL_REQUEST_ID      | 10001     | Missing Request ID   |
      | DUPLICATE_REQUEST_ID | 10003     | Duplicate Request ID |
      | LONG_REQUEST_ID      | 10002     | Invalid Request ID   |


  @GetEligiblePlansAndOffersInvalidLoginIDTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<loginID>"
    When a request is made to the GetEligiblePlansAndOffers Api with login "<loginID>" ID TC158_160b
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | loginID               | errorCode | errorMessage                                              |
      | INVALID_LOGIN_ID      | 2000      | Invalid Login ID                                          |
      | NULL_LOGIN_ID         | 10000     | Missing Login ID                                          |
      | NON_NUMERIC_LOGIN_ID  | 2000      | Invalid Login ID                                          |
      | ALPHANUMERIC_LOGIN_ID | 2000      | Invalid Login ID                                          |
      | MAX_LENGTH_LOGIN_ID   | 10000     | The Login ID must be a string with a maximum length of 30 |

  @GetEligiblePlansAndOffersInvalidTransactionIDTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<transactionID>"
    When a request is made to the GetEligiblePlansAndOffers Api with transaction "<transactionID>" ID TC161_162
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | transactionID                             | errorCode | errorMessage                                                   |
      | NULL_TRANSACTION_ID_INCL_ENROLLMENT_STATE | 2000      | Invalid Request: Missing conditional parameters-Transaction ID |
      | NULL_TRANSACTION_ID_CRDS_ENROLLMENT_STATE | 2000      | Invalid Request: Missing conditional parameters-Transaction ID |

  @GetEligiblePlansAndOffersInvalidCustomerCodeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<customerCode>"
    When a request is made to the GetEligiblePlansAndOffers Api with customer "<customerCode>" code TC163_164
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | customerCode                             | errorCode | errorMessage                                                  |
      | NULL_CUSTOMER_CODE_INCL_ENROLLMENT_STATE | 2000      | Invalid Request: Missing conditional parameters-Customer Code |
      | NULL_CUSTOMER_CODE_CRDS_ENROLLMENT_STATE | 2000      | Invalid Request: Missing conditional parameters-Customer Code |

  @GetEligiblePlansAndOffersInvalidPremisesCodeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<premisesCode>"
    When a request is made to the GetEligiblePlansAndOffers Api with premises "<premisesCode>" code TC165_167
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesCode                              | errorCode | errorMessage                                                                                                                      |
      | NULL_PREMISES_CODE_INCL_ENROLLMENT_STATE  | 2000      | Invalid Request: Missing conditional parameters-Premises Code                                                                     |
      | NULL_PREMISES_CODE_CRDS_ENROLLMENT_STATE  | 2000      | Invalid Request: Missing conditional parameters-Premises Code                                                                     |
      | VALID_PREMISES_CODE_NULL_ENROLLMENT_STATE | 2000      | Invalid Request: Invalid Parameter Combination - For new enrollment Transaction ID,Customer Code and Premises Code should be null |

  @GetEligiblePlansAndOffersInvalidEnrollmentStateTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<enrollmentState>"
    When a request is made to the GetEligiblePlansAndOffers Api with enrollment "<enrollmentState>" state TC168_182
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | enrollmentState                           | errorCode | errorMessage                                                                                                                      |
      | NULL_PREMISES_CODE_INCL_ENROLLMENT_STATE  | 2000      | Invalid Request: Missing conditional parameters-Premises Code                                                                     |
      | NULL_PREMISES_CODE_CRDS_ENROLLMENT_STATE  | 2000      | Invalid Request: Missing conditional parameters-Premises Code                                                                     |
      | VALID_PREMISES_CODE_NULL_ENROLLMENT_STATE | 2000      | Invalid Request: Invalid Parameter Combination - For new enrollment Transaction ID,Customer Code and Premises Code should be null |


  @GetEligiblePlansAndOffersWithInvalidTestConditionTNON @Phase1 @NegativeFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with invalid test condition null Authorised Type
    When a request is made to the GetEligiblePlansAndOffers Api with null Authorised Type
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode 2000 and ErrorMessage "Invalid Request: Missing conditional parameters-Authorized By"


  @GetEligiblePlansAndOffersWithInvalidReferralCodeTNON @Phase1 @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid test condition "<referralCode>" code Type
    When a request is made to the GetEligiblePlansAndOffers Api with "<referralCode>" code Type
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | referralCode                                    | errorCode | errorMessage                                                                                                                                                 |
      | MAX_REFERRAL_CODE                               | 10000     | The Referral Code must be a string with a maximum length of 9.                                                                                               |
      | NONNUMERIC_REFERRAL_CODE                        | 11115      | Invalid Request: Invalid Referral Code                                                                                                                       |
      | ALPHANUMERIC_REFERRAL_CODE                      | 2000      | Invalid Request: Invalid Referral Code                                                                                                                       |
      | VALID_REFERRAL_CODE_WITH_MISSING_MARKETING_CODE | 1000      | Invalid Request: Missing Promotion Code -Please ask the customer for a promotion code. If they do not have one, enter the appropriate default promotion code |

  @GetEligiblePlansAndOffersWithInvalidPremisesStreetTypeTNON @Phase1 @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid test condition "<premisesStreetNumber>"  Type
    When a request is made to the GetEligiblePlansAndOffers Api with "<premisesStreetNumber>"  Type
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetNumber       | errorCode | errorMessage                                                             |
      | MAX_PREMISES_STREET_NUMBER | 10000     | The Premises Street Number must be a string with a maximum length of 12. |

  @GetEligiblePlansAndOffersWithInvalidPremisesStreetPreDirectionTNON  @Phase1 @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid test condition "<premisesStreetPreDirection>" test cases 243
    When a request is made to the GetEligiblePlansAndOffers Api with "<premisesStreetPreDirection>" test cases 243
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetPreDirection                         | errorCode | errorMessage                                                                  |
      | MAX_PREMISES_STREET_PRE_DIRECTION                  | 10000     | The Premises Street PreDirection must be a string with a maximum length of 2. |
      | INVALID_PREMISES_STREET_PRE_DIRECTION_EQUAL_TO_TWO | 2000      | Invalid Request: Invalid Premises Street Pre Direction                        |

  @GetEligiblePlansAndOffersWithInvalidPremisesStreetNameTNON  @Phase1 @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid test condition "<premisesStreetName>" test cases 244
    When a request is made to the GetEligiblePlansAndOffers Api with "<premisesStreetName>" test cases 244_245
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetName        | errorCode | errorMessage                                                           |
      | MAX_PREMISES_STREET_NAME  | 10000     | The Premises Street Name must be a string with a maximum length of 30. |
      | NULL_PREMISES_STREET_NAME | 10000     | Invalid or missing Premises Street Name                                |

  @GetEligiblePlansAndOffersWithInvalidPremisesStreetSuffixTNON  @Phase1 @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid test condition "<premisesStreetSuffix>" test cases 246_246a
    When a request is made to the GetEligiblePlansAndOffers Api with "<premisesStreetSuffix>" test cases 246_246a
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetSuffix           | errorCode | errorMessage                                                             |
      | MAX_PREMISES_STREET_SUFFIX     | 10000     | The Premises Street Suffix  must be a string with a maximum length of 6. |
      | INVALID_PREMISES_STREET_SUFFIX | 2000      | Invalid Request: Invalid Premises Street Suffix                          |

  @GetEligiblePlansAndOffersWithInvalidPremisesStreetPostDirectionTNON  @Phase1 @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid test condition "<premisesStreetPostDirection>" test cases 247_247a
    When a request is made to the GetEligiblePlansAndOffers Api with "<premisesStreetPostDirection>" test cases 247_247a
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetPostDirection                         | errorCode | errorMessage                                                                   |
      | MAX_PREMISES_STREET_POST_DIRECTION                  | 10000     | The Premises Street PostDirection must be a string with a maximum length of 2. |
      | INVALID_PREMISES_STREET_POST_DIRECTION_EQUAL_TO_TWO | 2000      | Invalid Request: Invalid Premises Street Post Direction                        |

  @GetEligiblePlansAndOffersWithInvalidPremisesUnitTypeTNON  @Phase1 @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid test condition "<premisesUnitType>" test cases 248_248a
    When a request is made to the GetEligiblePlansAndOffers Api with "<premisesUnitType>" test cases 248_248a
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesUnitType           | errorCode | errorMessage                                                        |
      | MAX_PREMISES_UNIT_TYPE     | 10000     | The Premises Unit Type must be a string with a maximum length of 6. |
      | INVALID_PREMISES_UNIT_TYPE | 2000      | Invalid Request: Invalid Premises Unit Type                         |

  @GetEligiblePlansAndOffersWithInvalidPremisesUnitNumberTNON  @Phase1 @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid test condition "<premisesUnitNumber>" test cases 249
    When a request is made to the GetEligiblePlansAndOffers Api with "<premisesUnitNumber>" test cases 249
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesUnitNumber       | errorCode | errorMessage                                                          |
      | MAX_PREMISES_UNIT_NUMBER | 10000     | The Premises Unit Number must be a string with a maximum length of 6. |


  @GetEligiblePlansAndOffersWithInvalidPremisesCityTNON  @Phase1 @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid test condition "<premisesCity>" test cases 250_251
    When a request is made to the GetEligiblePlansAndOffers Api with "<premisesCity>" test cases 250_251
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesCity       | errorCode | errorMessage                                                    |
      | MAX_PREMISES_CITY  | 10000     | The Premises City must be a string with a maximum length of 20. |
      | NULL_PREMISES_CITY | 10000     | Invalid or missing Premises City                                |

  @GetEligiblePlansAndOffersInvalidPremisesStateCodeTNON  @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<PremisesStateCode>"252_253
    When a request is made to the GetEligiblePlansAndOffers Api with premises "<PremisesStateCode>" Statecode 252_253
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | PremisesStateCode                        | errorCode | errorMessage                                                         |
      | EMPTY_PREMISES_STATE_CODE                | 10000     | The Premises State Code must be a string with a maximum length of 3. |
      | MAX_LENGTH_PREMISES_STATE_CODE           | 10000     | The Premises State Code must be a string with a maximum length of 3. |
      | INVALID_PREMISES_STATE_CODE_EQUAL_TO_TWO | 2000      | Invalid Request: Invalid Premises State Code                         |

  @GetEligiblePlansAndOffersInvalidPremisesZipCodeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<PremisesZipCode>"254_255c
    When a request is made to the GetEligiblePlansAndOffers Api with premises "<PremisesZipCode>" Zipcode254_255c
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | PremisesZipCode                         | errorCode | errorMessage                                                       |
      | EMPTY_PREMISES_ZIP_CODE                 | 2000      | Invalid Request: Invalid Premises Zip Code                         |
      | INVALID_PREMISES_ZIP_CODE_RANDOM_STRING | 2000      | Invalid Request: Invalid Premises Zip Code                         |
      | MAX_LENGTH_PREMISES_ZIP_CODE            | 10000     | The Premises ZipCode must be a string with a maximum length of 10. |
      | LOWERCASE_PREMISES_ZIP_CODE             | 2000      | Invalid Request: Invalid Premises Zip Code                         |
      | INVALID_PREMISES_ZIP_FORMAT_CODE        | 2000      | Invalid Request: Invalid Premises Zip Code                         |
      | NUMERIC_PREMISES_ZIP_CODE               | 2000      | Invalid Request: Invalid Premises Zip Code                         |


  @GetEligiblePlansAndOffersInvalidPremisesCountyCodeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<PremisesCountyCode>"
    When a request is made to the GetEligiblePlansAndOffers Api with premises "<PremisesCountyCode>" Countycode256_257
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | PremisesCountyCode                | errorCode | errorMessage                                  |
      | EMPTY_PREMISES_COUNTY_CODE        | 2000      | Invalid Request: Invalid Premises County Code |
      | MIN_LENGTH_PREMISES_COUNTY_CODE   | 2000      | Invalid Request: Invalid Premises County Code |
      | ALPHANUMERIC_PREMISES_COUNTY_CODE | 2000      | Invalid Request: Invalid Premises County Code |


  @GetEligiblePlansAndOffersInvalidSeparateBillingAddressTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<SeparateBillingAddress>"
    When a request is made to the GetEligiblePlansAndOffers Api with separateBilling "<SeparateBillingAddress>" Address258_283b
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | SeparateBillingAddress                                                                         | errorCode | errorMessage                                                                                                                     |
      | EMPTY_SEPARATE_BILLING_ADDRESS                                                                 | 10000     | The JSON value could not be converted to System.Boolean. Path: $.separateBillingAddress  LineNumber: 21  BytePositionInLine: 32. |
      | MIN_LENGTH_SEPARATE_BILLING_ADDRESS                                                            | 10000     | The JSON value could not be converted to System.Boolean. Path: $.separateBillingAddress  LineNumber: 21  BytePositionInLine: 33. |
      | SEPARATE_BILLING_ADDRESS_BILLING_ADD_TYPE_PROVIDED                                             | 10000     | The Billing Address Type must be a string with a maximum length of 1.                                                            |
      | SEPARATE_BILLING_ADDRESS_BILLING_ADD_TYPE_MISSING                                              | 2000      | Invalid Request: Missing conditional parameters-Billing Address Type                                                             |
      | SEPARATE_BILLING_ADDRESS_WITH_INVALID_BILLING_ADD_TYPE                                         | 2000      | Invalid Request: Invalid Billing Address Type                                                                                    |
      | INVALID_SEPARATE_BILLING_ADDRESS_WITH_VALID_BILLING_ADD_TYPE                                   | 10000     | The Billing Street Name must be a string with a maximum length of 30.                                                            |
      | VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_STREET_PRE_DIRECTION                        | 10000     | The Billing Street PreDirection must be a string with a maximum length of 2.                                                     |
      | VALID_BILLING_ADDRESS_TYPE_WITH_INVALID_BILLING_STREET_PRE_DIRECTION                           | 2000      | Invalid Request: Invalid Billing Street Pre Direction                                                                            |
      | VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_STREET_SUFFIX                               | 10000     | The Billing Street Suffix must be a string with a maximum length of 6.                                                           |
      | VALID_BILLING_ADDRESS_TYPE_WITH_INVALID_BILLING_STREET_SUFFIX                                  | 2000      | Invalid Request: Invalid Billing Street Suffix                                                                                   |
      | VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_STREET_POST_DIRECTION                       | 10000     | The Billing Street PostDirection must be a string with a maximum length of 2.                                                    |
      | VALID_BILLING_ADDRESS_TYPE_WITH_INVALID_BILLING_STREET_POST_DIRECTION                          | 2000      | Invalid Request: Invalid Billing Street Post Direction                                                                           |
      | VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_UNIT_TYPE                                   | 10000     | The Billing Unit Type must be a string with a maximum length of 6.                                                               |
      | VALID_BILLING_ADDRESS_TYPE_WITH_INVALID_BILLING_UNIT_TYPE                                      | 2000      | Invalid Request: Invalid Billing Unit Type                                                                                       |
      | VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_UNIT_NUMBER                                 | 10000     | The Billing Unit Number must be a string with a maximum length of 6.                                                             |
      | VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_RURAL_ROUTE                                 | 10000     | The Billing Rural Route must be a string with a maximum length of 20.                                                            |
      | VALID_BILLING_ADDRESS_TYPE_WITH_EMPTY_BILLING_RURAL_ROUTE                                      | 2000      | Invalid Request: Missing conditional parameters-Customer PEWC Preference                                                                                   |
      | VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_RURAL_ROUTE_NUMBER                          | 10000     | The Billing Rural Route Number must be a string with a maximum length of 10.                                                     |
      | VALID_BILLING_ADDRESS_TYPE_WITH_NULL_BILLING_RURAL_ROUTE_NUMBER                                | 2000      | Invalid Request: Missing conditional parameters-Billing Rural Route Number                                                                           |
      | VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_PO_BOX                                      | 10000     | The Billing PO Box must be a string with a maximum length of 10.                                                                 |
      | VALID_BILLING_ADDRESS_TYPE_WITH_NULL_BILLING_PO_BOX                                            | 2000      | Invalid Request: Missing conditional parameters-Billing PO Box                                                                                 |
      | VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_ADDRESS_LINE2                               | 10000     | The Billing Address Line 2 must be a string with a maximum length of 30.                                                         |
      | VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_ADDRESS_LINE2_WITH_BILLING_RURAL_ROUTE      | 10000     | The Billing Address Line 2 must be a string with a maximum length of 30.                                                         |
      | VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_ADDRESS_LINE2_WITH_BILLING_PO_BOX           | 10000     | The Billing Address Line 2 must be a string with a maximum length of 30.                                                         |
      | VALID_BILLING_ADDRESS_TYPE_S_WITH_MAX_LENGTH_BILLING_CITY                                      | 10000     | The Billing City must be a string with a maximum length of 20.                                                                   |
      | VALID_BILLING_ADDRESS_TYPE_R_WITH_MAX_LENGTH_BILLING_CITY_WITH_BILLING_RURAL_ROUTE             | 10000     | The Billing City must be a string with a maximum length of 20.                                                                   |
      | VALID_BILLING_ADDRESS_TYPE_P_WITH_MAX_LENGTH_BILLING_CITY_WITH_BILLING_PO_BOX                  | 10000     | The Billing City must be a string with a maximum length of 20.                                                                   |
      | VALID_BILLING_ADDRESS_TYPE_R_WITH_MAX_LENGTH_BILLING_STATE_CODE_WITH_BILLING_RURAL_ROUTE       | 10000     | The Billing State Code must be a string with a maximum length of 3.                                                              |
      | VALID_BILLING_ADDRESS_TYPE_P_WITH_MAX_LENGTH_BILLING_STATE_CODE_WITH_BILLING_PO_BOX            | 10000     | The Billing State Code must be a string with a maximum length of 3.                                                              |
      | VALID_BILLING_ADDRESS_TYPE_S_WITH_INVALID_BILLING_STATE_CODE_NOT_PRESENT_IN_TABLE              | 2000      | Invalid Request: Invalid Billing State Code                                                                                      |
      | VALID_BILLING_ADDRESS_TYPE_R_WITH_INVALID_BILLING_STATE_CODE_NOT_PRESENT_IN_TABLE              | 2000      | Invalid Request: Invalid Billing State Code                                                                                      |
      | VALID_BILLING_ADDRESS_TYPE_P_WITH_INVALID_BILLING_STATE_CODE_NOT_PRESENT_IN_TABLE              | 2000      | Invalid Request: Invalid Billing State Code                                                                                      |
      | VALID_BILLING_ADDRESS_TYPE_S_WITH_INVALID_BILLING_ZIP_CODE                                     | 2000      | Invalid Request: Invalid Billing Zip Code                                                                                        |
      | VALID_BILLING_ADDRESS_TYPE_S_WITH_MIN_LENGTH_BILLING_ZIP_CODE                                  | 2000      | Invalid Request: Invalid Billing Zip Code                                                                                        |
      | VALID_BILLING_ADDRESS_TYPE_R_WITH_INVALID_BILLING_ZIP_CODE                                     | 2000      | Invalid Request: Invalid Billing Zip Code                                                                                        |
      | VALID_BILLING_ADDRESS_TYPE_R_WITH_MIN_LENGTH_BILLING_ZIP_CODE                                  | 2000      | Invalid Request: Invalid Billing Zip Code                                                                                        |
      | VALID_BILLING_ADDRESS_TYPE_P_WITH_INVALID_BILLING_ZIP_CODE                                     | 2000      | Invalid Request: Invalid Billing Address Type                                                                  |
      | VALID_BILLING_ADDRESS_TYPE_P_WITH_MIN_LENGTH_BILLING_ZIP_CODE                                  | 2000      | Invalid Request: Invalid Billing Zip Code                                                                                        |
      | VALID_BILLING_ADDRESS_TYPE_S_WITH_BILLING_ZIP_CODE_MISSING_281c                                     | 2000      | Invalid Request: Invalid Billing Address Type                                                                |
      | VALID_BILLING_ADDRESS_TYPE_P_WITH_BILLING_ZIP_CODE_MISSING_281e                                | 2000      | Invalid Request: Missing conditional parameters-Billing State Code                                     |
      | VALID_BILLING_ADDRESS_TYPE_S_WITH_MAX_LENGTH_BILLING_COUNTY_CODE                               | 10000     | The Billing County Code must be a string with a maximum length of 5.                                                             |
      | VALID_BILLING_ADDRESS_TYPE_R_WITH_MAX_LENGTH_BILLING_COUNTY_CODE_BILLING_RURAL_ROUTE           | 10000     | The Billing County Code must be a string with a maximum length of 5.                                                             |
      | VALID_BILLING_ADDRESS_TYPE_P_WITH_MAX_LENGTH_BILLING_COUNTY_CODE_WITH_BILLING_PO_BOX           | 10000     | The Billing County Code must be a string with a maximum length of 5.                                                             |
      | VALID_BILLING_ADDRESS_TYPE_S_WITH_BILLING_COUNTY_CODE_NOT_PRESENT_IN_TABLE                     | 2000      | Invalid Request: Invalid Billing County Code                                                                                     |
      | VALID_BILLING_ADDRESS_TYPE_R_WITH_BILLING_COUNTY_CODE_NOT_PRESENT_IN_TABLE                     | 2000      | Invalid Request: Invalid Billing County Code                                                                                     |
      | VALID_BILLING_ADDRESS_TYPE_P_WITH_BILLING_COUNTY_CODE_NOT_PRESENT_IN_TABLE_WITH_BILLING_PO_BOX | 2000      | Invalid Request: Invalid Billing County Code                                                                                     |

  @GetEligiblePlansAndOffersInvalidWorkPhoneNumberTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<WorkPhoneNumber>"
    When a request is made to the GetEligiblePlansAndOffers Api with workPhone "<WorkPhoneNumber>" Number284_286
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | WorkPhoneNumber                                   | errorCode | errorMessage                                                     |
      | MIN_LENGTH_WORK_PHONE_NUMBER                      | 2000      | Invalid Request: Invalid Work Phone Number                       |
      | ALPHANUMERIC_WORK_PHONE_NUMBER                    | 2000      | Invalid Request: Invalid Work Phone Number                       |
      | NULL_WORK_PHONE_NUMBER_WITH_VALID_WORK_PHONE_TYPE | 10000     | The Work Phone Type must be a string with a maximum length of 1. |


  @GetEligiblePlansAndOffersInvalidWorkPhoneTypeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<WorkPhoneType>"
    When a request is made to the GetEligiblePlansAndOffers Api with WorkPhone "<WorkPhoneType>" Type287_290
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | WorkPhoneType                                     | errorCode | errorMessage                                                          |
      | NULL_WORK_PHONE_TYPE_WITH_VALID_WORK_PHONE_NUMBER | 2000      | Invalid Request: Missing conditional parameters-Work Phone Type       |
      | MAX_LENGTH_WORK_EXTENSION_TYPE                    | 10000     | The Work Phone Extension must be a string with a maximum length of 4. |
      | WORK_PHONE_TYPE_PROVIDED_MAX_1_CHAR               | 10000     | The Work Phone Type must be a string with a maximum length of 1.      |
      | INVALID_WORK_PHONE_TYPE_VALUE                     | 2000      | Invalid Request: Invalid Work Phone Type                              |

  @GetEligiblePlansAndOffersInvalidHomePhoneNumberTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<HomePhoneNumber>"
    When a request is made to the GetEligiblePlansAndOffers Api with HomePhone "<HomePhoneNumber>" Number291_293
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | HomePhoneNumber                                   | errorCode | errorMessage                                                      |
      | MIN_LENGTH_HOME_PHONE_NUMBER                      | 2000      | Invalid Request: Invalid Home Phone Number                        |
      | ALPHANUMERIC_HOME_PHONE_NUMBER                    | 2000      | Invalid Request: Invalid Home Phone Number                        |
      | NULL_HOME_PHONE_NUMBER_WITH_VALID_HOME_PHONE_TYPE | 2000      | Invalid Request: Missing conditional parameters-Home Phone Number |


  @GetEligiblePlansAndOffersInvalidHomePhoneTypeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<HomePhoneType>"
    When a request is made to the GetEligiblePlansAndOffers Api with HomePhone "<HomePhoneType>" Type294_297
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | HomePhoneType                                         | errorCode | errorMessage                                                     |
      | NULL_HOME_PHONE_TYPE_WITH_VALID_HOME_PHONE_NUMBER_294 | 2000      | Invalid Request: Missing conditional parameters-Home Phone Type  |
      | HOME_PHONE_TYPE_PROVIDED_MAX_1_CHAR_296               | 10000     | The Home Phone Type must be a string with a maximum length of 1. |
      | INVALID_HOME_PHONE_TYPE_VALUE_297                     | 2000      | Invalid Request: Invalid Work Phone Type                         |

  @GetEligiblePlansAndOffersInvalidAcnStatusIndicatorTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<AcnStatusIndicator>"
    When a request is made to the GetEligiblePlansAndOffers Api with acnStatus "<AcnStatusIndicator>" IndicatorTC298_307
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | AcnStatusIndicator                                                        | errorCode | errorMessage                                                          |
      | EMPTY_ACN_STATUS_INDICATOR_WITH_TENANT_LANDLORD                           | 10000     | Invalid or missing ACN Status Indicator                           |
      | INVALID_ACN_STATUS_INDICATOR_VALUE_NOT_PRESENT_IN_TABLE                   | 2000      | Invalid Request: Invalid ACN Status Indicator                         |
      | MAX_LENGTH_ACN_STATUS_INDICATOR                                           | 10000     | The ACN Status Indicator must be a string with a maximum length of 4. |
      | VALID_ACN_STATUS_INDICATOR_WITH_MAX_LENGTH_TENANT_LANDLORD                | 10000     | The Tenant/Landlord must be a string with a maximum length of 1.      |
      | VALID_ACN_STATUS_INDICATOR_WITH_NULL_TENANT_LANDLORD                      | 10000     | Invalid or missing Tenant/Landlord Indicator                          |
      | VALID_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_L_WITH_INVALID_USER_ROLE | 2000      | Invalid Request: Invalid ACN Status for user role                     |
      | VALID_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_T_WITH_INVALID_USER_ROLE | 2000      | Invalid Request: Invalid ACN Status for user role                     |
      | NULL_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_L_WITH_INVALID_USER_ROLE  | 10000     | Invalid or missing ACN Status Indicator                               |
      | NULL_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_T_WITH_INVALID_USER_ROLE  | 10000     | Invalid or missing ACN Status Indicator                     |

  @GetEligiblePlansAndOffersInvalidCustomerPEWCPreferencesTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<CustomerPEWCPreferences>"
    When a request is made to the GetEligiblePlansAndOffers Api with CustomerPEWC "<CustomerPEWCPreferences>" PreferencesTC308_310
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | CustomerPEWCPreferences                                   | errorCode | errorMessage                                                                                                                      |
      | CUSTOMER_PEWC_PREFRENCES_VALUE_GOOD_WITH_OTHER_PARAM_NULL | 10000     | The JSON value could not be converted to System.Boolean. Path: $.customerPEWCPreferences  LineNumber: 63  BytePositionInLine: 37. |
      | CUSTOMER_PEWC_PREFRENCES_VALUE_TRUE_WITH_OTHER_PARAM_NULL | 2000      | Invalid Request: Missing conditional parameters-Customer PEWC Preference                                                          |

  @GetEligiblePlansAndOffersInvalidCreditCheckOptionTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<CreditCheckOption>"
    When a request is made to the GetEligiblePlansAndOffers Api with credit "<CreditCheckOption>" CheckoptionTC310_312
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | CreditCheckOption              | errorCode | errorMessage                                                          |
      | EMPTY_CREDIT_CHECK_OPTION      | 10000     | Invalid or missing Credit Check Option                                |
      | INVALID_CREDIT_CHECK_OPTION    | 2000      | Invalid Request: Invalid Credit Check Option                          |
      | MAX_LENGTH_CREDIT_CHECK_OPTION | 10000     | The Credit Check Option must be a string with a maximum length of 32. |

  @GetEligiblePlansAndOffersInitialCreditCheckCustomerCodeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<InitialCreditCheckCustomerCode>"
    When a request is made to the GetEligiblePlansAndOffers Api with InitialCreditCheck "<InitialCreditCheckCustomerCode>" CustomerCodeTC313_317
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | InitialCreditCheckCustomerCode                                    | errorCode | errorMessage                                                                                                                                              |
      | EMPTY_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_WITH_CREDIT_CHECK_OPTION_315 | 2000      | Invalid Request: Missing conditional parameters-Initial Credit Check Cust Code                                                                            |
      | MAX_LENGTH_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_313                     | 10000     | The Initial Credit Check Customer Code must be an integer with a maximum length of 9                                                                   |
      | NONNUMERIC_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_314                     | 10000     | The JSON value could not be converted to System.Nullable`1[System.Int32]. Path: $.initialCreditCheckCustomerCode  LineNumber: 65  BytePositionInLine: 47. |
      | INVALID_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_NOT_PRESENT_IN_TABLE_316   | 2000      | Invalid or missing Initial Credit Check Customer Code                                                                                                                  |
      | INVALID_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_WITHOUT_CREDIT_SCORE_317   | 10000      | Unable to locate a credit score within 3 months for the Customer Code provided - 5908691                                                                  |


  @GetEligiblePlansAndOffersInvalidTransactionTypeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<transactionType>"Type
    When a request is made to the GetEligiblePlansAndOffers Api with  transaction "<transactionType>" Type
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | transactionType                      | errorCode | errorMessage                                                     |
      | EMPTY_TRANSACTION_TYPE               | 10000     | Missing Transaction Type                                         |
      | NUMERIC_TRANSACTION_TYPE             | 10000     | The Transaction Type must be a string with a maximum length of 4 |
      | UPPERCASE_TRANSACTION_TYPE           | 10000     | The Transaction Type must be a string with a maximum length of 4 |
      | ALPHANUMERIC_TRANSACTION_TYPE        | 1000      | Invalid Request: Invalid Transaction Type                        |
      | WHITESPACE_CONTAINS_TRANSACTION_TYPE | 10000     | The Transaction Type must be a string with a maximum length of 4 |

  @GetEligiblePlansAndOffersInvalidCustomerTypeTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify SaveEnrollment Api with invalid "<customerTYPE>" type
    When a request is made to the GetEligiblePlansAndOffers Api with  customer "<customerTYPE>" Type
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | customerTYPE             | errorCode | errorMessage                                                   |
      | EMPTY_CUSTOMER_TYPE      | 10000     | Invalid or missing Customer Type                              |
      | MIN_LENGTH_CUSTOMER_TYPE | 10000     | The Customer Type must be a string with a maximum length of 2 |
      | SPL_CHAR_CUSTOMER_TYPE   | 10000     | The Customer Type must be a string with a maximum length of 2 |
      | MAX_LENGTH_CUSTOMER_TYPE | 10000     | The Customer Type must be a string with a maximum length of 2 |

  @GetEligiblePlansAndOffersInvalidEnrollmentSourcesTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<enrollmentSources>"
    When a request is made to the GetEligiblePlansAndOffers Api with enrollment "<enrollmentSources>" Sources
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | enrollmentSources             | errorCode | errorMessage                                                        |
      | EMPTY_ENROLLMENT_SOURCES      | 10000     | Invalid or missing Enrollment Source                                |
      | NUMERIC_ENROLLMENT_SOURCES    | 1000      | Invalid Request: Invalid Enrollment Source                          |
      | SPL_CHAR_ENROLLMENT_SOURCES   | 1000      | Invalid Request: Invalid Enrollment Source                          |
      | MAX_LENGTH_ENROLLMENT_SOURCES | 10000     | The Enrollment Source must be a string with a maximum length of 35. |

  @GetEligiblePlansAndOffersInvalidCustomerLastNameTNON @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<CustomerLastName>"
    When a request is made to the GetEligiblePlansAndOffers Api with customer "<CustomerLastName>" LastName
    Then verify response code of "GetEligiblePlansAndOffers" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | CustomerLastName         | errorCode | errorMessage                                                       |
      | EMPTY_CUSTOMER_LAST_NAME | 2000      | Invalid Request: Missing conditional parameters-Customer Last Name |


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

