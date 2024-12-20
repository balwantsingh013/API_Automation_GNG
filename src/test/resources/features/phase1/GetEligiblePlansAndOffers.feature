Feature: Verify GetEligiblePlansAndOffers Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @GetEligiblePlansAndOffersWithValidData @Phase1 @HappyFlow
  Scenario: Verify GetEligiblePlansAndOffers Api with valid data
    When a request is made to the GetEligiblePlansAndOffers Api
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>

  @GetEligiblePlansAndOffersWithInvalidRequestID @Phase1 @NegativeFlow
  Scenario Outline: Verify GetEligiblePlansAndOffers Api with invalid requestID "<requestID>"
    When a request is made to the GetEligiblePlansAndOffers Api with "<requestID>"
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID                | errorCode | errorMessage           |
      | EMPTY_REQUEST_ID         | 10001     | Missing Request ID     |
      | DUPLICATE_REQUEST_ID     | 10003     | Duplicate Request ID   |
      | LONG_REQUEST_ID          | 10002     | Invalid Request ID     |
      | UNICODE_CHARS_REQUEST_ID | 10007     | Unsupported Characters |

  @GetEligiblePlansAndOffersInvalidLoginID @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<loginID>"
    When a request is made to the GetEligiblePlansAndOffers Api with login "<loginID>" ID
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | loginID                | errorCode | errorMessage                                              |
      | MIN_LENGTH_LOGIN_ID    | 2000      | Invalid Login ID                                          |
      | SPECIAL_CHARS_LOGIN_ID | 2000      | Invalid Login ID                                          |
      | EMPTY_LOGIN_ID         | 10000     | Missing Login ID                                          |
      | UPPERCASE_LOGIN_ID     | 2000      | Invalid Login ID                                          |
      | ALPHANUMERIC_LOGIN_ID  | 2000      | Invalid Login ID                                          |
      | MAX_LENGTH_LOGIN_ID    | 10000     | The Login ID must be a string with a maximum length of 30 |

  @GetEligiblePlansAndOffersInvalidTransactionType @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<transactionType>"Type
    When a request is made to the GetEligiblePlansAndOffers Api with  transaction "<transactionType>" Type
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | transactionType                      | errorCode | errorMessage                                                     |
      | EMPTY_TRANSACTION_TYPE               | 10000     | Missing Transaction Type                                         |
      | NUMERIC_TRANSACTION_TYPE             | 10000     | The Transaction Type must be a string with a maximum length of 4 |
      | UPPERCASE_TRANSACTION_TYPE           | 10000     | The Transaction Type must be a string with a maximum length of 4 |
      | ALPHANUMERIC_TRANSACTION_TYPE        | 1000      | Invalid Request: Invalid Transaction Type                        |
#      | MAX_LENGTH_TRANSACTION_TYPE          | 1000      | Invalid Request: Invalid Transaction Type                        |
      | WHITESPACE_CONTAINS_TRANSACTION_TYPE | 10000     | The Transaction Type must be a string with a maximum length of 4 |

  @GetEligiblePlansAndOffersInvalidCustomerTYPE @Phase1  @NegativeFlow
  Scenario Outline: Verify SaveEnrollment Api with invalid "<customerTYPE>" type
    When a request is made to the GetEligiblePlansAndOffers Api with  customer "<customerTYPE>" Type
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | customerTYPE             | errorCode | errorMessage                                                    |
      | EMPTY_CUSTOMER_TYPE      | 10000     | Invalid or missing Customer Type  |
      | MIN_LENGTH_CUSTOMER_TYPE | 10000     | The Customer Type must be a string with a maximum length of 2.|
      | NUMERIC_CUSTOMER_TYPE    | 2000      | Invalid Request: Invalid Customer Type                          |
      | SPL_CHAR_CUSTOMER_TYPE   | 10000     | The Customer Type must be a string with a maximum length of 2.  |
      | MAX_LENGTH_CUSTOMER_TYPE | 10000      | The Customer Type must be a string with a maximum length of 2.  |

  @GetEligiblePlansAndOffersInvalidEnrollmentSources @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<enrollmentSources>"
    When a request is made to the GetEligiblePlansAndOffers Api with enrollment "<enrollmentSources>" Sources
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | enrollmentSources             | errorCode | errorMessage                                                        |
      | EMPTY_ENROLLMENT_SOURCES      | 10000     | Invalid or missing Enrollment Source                                |
      | NUMERIC_ENROLLMENT_SOURCES    | 1000      | Invalid Request: Invalid Enrollment Source                          |
      | SPL_CHAR_ENROLLMENT_SOURCES   | 1000      | Invalid Request: Invalid Enrollment Source                          |
      | MAX_LENGTH_ENROLLMENT_SOURCES | 10000     | The Enrollment Source must be a string with a maximum length of 35. |

  @GetEligiblePlansAndOffersInvalidCustomerLastName @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<CustomerLastName>"
    When a request is made to the GetEligiblePlansAndOffers Api with customer "<CustomerLastName>" LastName
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | CustomerLastName         | errorCode | errorMessage                                                       |
      | EMPTY_CUSTOMER_LAST_NAME | 2000      | Invalid Request: Missing conditional parameters-Customer Last Name |

  @GetEligiblePlansAndOffersInvalidCustomerFirstName @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<CustomerFirstName>"
    When a request is made to the GetEligiblePlansAndOffers Api with customer "<CustomerFirstName>" FirstName
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | CustomerFirstName         | errorCode | errorMessage                                                       |
      | EMPTY_CUSTOMER_FIRST_NAME | 10000     | Invalid Request: Missing conditional parameters-Customer Last Name |

  @GetEligiblePlansAndOffersInvalidSeasonalSavingsProgramIndicator @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid  "<SeasonalSavingsProgramIndicator> "
    When a request is made to the GetEligiblePlansAndOffers Api with seasonal savings program "<SeasonalSavingsProgramIndicator>" Indicator
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | SeasonalSavingsProgramIndicator         | errorCode | errorMessage                                   |
      | NULL_SEASONAL_SAVINGS_PROGRAM_INDICATOR | 10000     | Seasonal Saving Program Indicator Not Provided |

  @GetEligiblePlansAndOffersInvalidPremisesStreetName @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<PremisesStreetName>"
    When a request is made to the GetEligiblePlansAndOffers Api with premises "<PremisesStreetName>" StreetName
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | PremisesStreetName              | errorCode | errorMessage                                                           |
      | EMPTY_PREMISES_STREET_NAME      | 10000     | Invalid or missing Premises Street Name                                |
#      | SPL_CHAR_PREMISES_STREET_NAME   | 11115     | No Data in the Experian response                                       |
#      | MAX_LENGTH_PREMISES_STREET_NAME | 10000     | The Premises Street Name must be a string with a maximum length of 30. |


  @GetEligiblePlansAndOffersInvalidPremisesCity @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<PremisesCity>"
    When a request is made to the GetEligiblePlansAndOffers Api with premises "<PremisesCity>" City
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | PremisesCity        | errorCode | errorMessage                     |
      | EMPTY_PREMISES_CITY | 10000     | Invalid or missing Premises City |

  @GetEligiblePlansAndOffersInvalidPremisesZipCode @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<PremisesZipCode>"
    When a request is made to the GetEligiblePlansAndOffers Api with premises "<PremisesZipCode>" Zipcode
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | PremisesZipCode                | errorCode | errorMessage                                                       |
      | EMPTY_PREMISES_ZIP_CODE        | 2000      | Invalid Request: Invalid Premises Zip Code                         |
      | SPL_CHAR_PREMISES_ZIP_CODE     | 2000      | Invalid Request: Invalid Premises Zip Code                         |
      | MAX_LENGTH_PREMISES_ZIP_CODE   | 10000     | The Premises ZipCode must be a string with a maximum length of 10. |
      | MIN_LENGTH_PREMISES_ZIP_CODE   | 2000      | Invalid Request: Invalid Premises Zip Code                         |
      | ALPHANUMERIC_PREMISES_ZIP_CODE | 2000      | Invalid Request: Invalid Premises Zip Code                         |

  @GetEligiblePlansAndOffersInvalidPremisesCountyCode @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<PremisesCountyCode>"
    When a request is made to the GetEligiblePlansAndOffers Api with premises "<PremisesCountyCode>" Countycode
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | PremisesCountyCode                | errorCode | errorMessage                                                          |
      | EMPTY_PREMISES_COUNTY_CODE        | 2000      | Invalid Request: Invalid Premises County Code                         |
      | SPL_CHAR_PREMISES_COUNTY_CODE     | 2000      | Invalid Request: Invalid Premises County Code                         |
      | MAX_LENGTH_PREMISES_COUNTY_CODE   | 10000     | The Premises County Code must be a string with a maximum length of 5. |
      | ALPHANUMERIC_PREMISES_COUNTY_CODE | 2000      | Invalid Request: Invalid Premises County Code                         |

  @GetEligiblePlansAndOffersInvalidPremisesStateCode @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<PremisesStateCode>"
    When a request is made to the GetEligiblePlansAndOffers Api with premises "<PremisesStateCode>" Statecode
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | PremisesStateCode                | errorCode | errorMessage                                                         |
      | EMPTY_PREMISES_STATE_CODE        | 10000     | The Premises State Code must be a string with a maximum length of 3. |
      | SPL_CHAR_PREMISES_STATE_CODE     | 10000     | The Premises State Code must be a string with a maximum length of 3. |
      | MAX_LENGTH_PREMISES_STATE_CODE   | 10000     | The Premises State Code must be a string with a maximum length of 3. |
      | ALPHANUMERIC_PREMISES_STATE_CODE | 2000      | Invalid Request: Invalid Premises State Code                         |

  @GetEligiblePlansAndOffersInvalidCreditCheckOption @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<CreditCheckOption>"
    When a request is made to the GetEligiblePlansAndOffers Api with credit "<CreditCheckOption>" Checkoption
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | CreditCheckOption                | errorCode | errorMessage                                                          |
      | EMPTY_CREDIT_CHECK_OPTION        | 2000      | Invalid Request: Invalid Credit Check Option                               |
      | SPL_CHAR_CREDIT_CHECK_OPTION     | 2000      | Invalid Request: Invalid Credit Check Option                          |
      | MAX_LENGTH_CREDIT_CHECK_OPTION   | 2000     | Invalid Request: Invalid Credit Check Option |
      | NUMERIC_CREDIT_CHECK_OPTION      | 2000      | Invalid Request: Invalid Credit Check Option                          |
      | ALPHANUMERIC_CREDIT_CHECK_OPTION | 2000      | Invalid Request: Invalid Credit Check Option                          |


  @GetEligiblePlansAndOffersInvalidTenantLandlord @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<TenantLandlord>"
    When a request is made to the GetEligiblePlansAndOffers Api with tenant "<TenantLandlord>" Landlord
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | TenantLandlord             | errorCode | errorMessage                                                      |
      | EMPTY_TENANT_LANDLORD      | 10000     | The Tenant/Landlord must be a string with a maximum length of 1. |
      | SPL_CHAR_TENANT_LANDLORD   | 10000     | The Tenant/Landlord must be a string with a maximum length of 1. |
      | MAX_LENGTH_TENANT_LANDLORD | 10000     | The Tenant/Landlord must be a string with a maximum length of 1.  |
      | LOWERCASE_TENANT_LANDLORD  | 10000     | The Tenant/Landlord must be a string with a maximum length of 1.  |
      | NUMERIC_TENANT_LANDLORD    | 10000     | The Tenant/Landlord must be a string with a maximum length of 1. |


  @GetEligiblePlansAndOffersInvalidSeparateBillingAddress @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<SeparateBillingAddress>"
    When a request is made to the GetEligiblePlansAndOffers Api with separateBilling "<SeparateBillingAddress>" Address
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | SeparateBillingAddress        | errorCode | errorMessage                                |
      | NULL_SEPARATE_BILLING_ADDRESS | 10000     | Invalid or missing Separate Billing Address |


  @GetEligiblePlansAndOffersInvalidAcnStatusIndicator @Phase1  @NegativeFlow
  Scenario Outline: Verify response code for invalid "<AcnStatusIndicator>"
    When a request is made to the GetEligiblePlansAndOffers Api with acnStatus "<AcnStatusIndicator>" Indicator
    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | AcnStatusIndicator                | errorCode | errorMessage                                                          |
      | EMPTY_ACN_STATUS_INDICATOR        | 10000     | The ACN Status Indicator must be a string with a maximum length of 4. |
      | SPL_CHAR_ACN_STATUS_INDICATOR     | 10000     | The ACN Status Indicator must be a string with a maximum length of 4. |
      | MAX_LENGTH_ACN_STATUS_INDICATOR   | 2000      | Invalid Request: Invalid ACN Status Indicator                         |
      | ALPHANUMERIC_ACN_STATUS_INDICATOR | 2000      | Invalid Request: Invalid ACN Status Indicator                         |

#  @GetEligiblePlansAndOffersInvalidAglcServiceLocationID @Phase1  @NegativeFlow
#  Scenario Outline: Verify response code for invalid "<AglcServiceLocationID>"
#    When a request is made to the GetEligiblePlansAndOffers Api with aglcService "<AglcServiceLocationID>" LocationID
#    Then verify response code of "GetEligiblePlansAndOffers" Api is <200>
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | AglcServiceLocationID                 | errorCode | errorMessage                                                         |
#      | EMPTY_AGLC_SERVICE_LOCATION_ID        | 10000     | Invalid or missing Premises State Code                               |
#      | SPL_CHAR_AGLC_SERVICE_LOCATION_ID     | 10000     | The Premises State Code must be a string with a maximum length of 3. |
#      | MAX_LENGTH_AGLC_SERVICE_LOCATION_ID   | 10000     | The Premises State Code must be a string with a maximum length of 3. |
#      | ALPHANUMERIC_AGLC_SERVICE_LOCATION_ID | 2000      | Invalid Request: Invalid Premises State Code                         |
