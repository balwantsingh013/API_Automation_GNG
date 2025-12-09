Feature: Verify SearchAccounts Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @SearchAccountsWithInvalidRequestID @turnOn @Phase1 @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify SearchAccounts Api with invalid requestID for "<testCondition>" condition
    When a request is made to the SearchAccounts Api with "<testCondition>" condition
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                 | errorCode | errorMessage         |
      | NULL_REQUEST_ID_TC42      | 10001     | Missing Request ID   |
      | LONG_REQUEST_ID_TC43      | 10002     | Invalid Request ID   |
      | DUPLICATE_REQUEST_ID_TC44 | 10003     | Duplicate Request ID |

  @SearchAccountsInvalidLoginID @turnOn @Phase1 @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid loginID "<loginID>"TC45_TC48
    When a request is made to the SearchAccounts Api with "<loginID>"TC45_TC48
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | loginID                                      | errorCode | errorMessage                |
      | NULL_LOGIN_ID_TC45                           | 10112     | Invalid or missing Login ID |
      | MAX_LENGTH_LOGIN_ID_TC46                     | 10112     | Invalid or missing Login ID |
      | ALPHANUMERIC_LOGIN_ID_TC47                   | 10112     | Invalid or missing Login ID |
      | INVALID_LOGIN_ID_NOT_PRESENT_USER_TABLE_TC48 | 2000      | Invalid Login ID            |

  @SearchAccountsInvalidCustomerCode @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid customerCode "<customerCode>"TC49
    When a request is made to the SearchAccounts Api with "<customerCode>"TC49
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | customerCode                  | errorCode | errorMessage                |
      | MAX_LENGTH_CUSTOMER_CODE_TC49 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesCode @turnOn @Phase1 @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesCode "<premisesCode>"TC50
    When a request is made to the SearchAccounts Api with "<premisesCode>"TC50
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesCode                  | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_CODE_TC50 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidTransactionType @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid transactionType "<transactionType>"TC51_TC52
    When a request is made to the SearchAccounts Api with "<transactionType>"TC51_TC52
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | transactionType               | errorCode | errorMessage                        |
      | MISSING_TRANSACTION_TYPE_TC51 | 10113     | Invalid or missing Transaction Type |
      | INVALID_TRANSACTION_TYPE_TC52 | 10113     | Invalid or missing Transaction Type |

  @SearchAccountsInvalidBusinessName @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid businessName "<businessName>"TC53
    When a request is made to the SearchAccounts Api with "<businessName>"TC53
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | businessName                  | errorCode | errorMessage                |
      | MAX_LENGTH_BUSINESS_NAME_TC53 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidLastName @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid lastName "<lastName>"TC54
    When a request is made to the SearchAccounts Api with "<lastName>"TC54
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | lastName                  | errorCode | errorMessage                |
      | MAX_LENGTH_LAST_NAME_TC54 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidFirstName @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid customerFirstName "<customerFirstName>"TC55
    When a request is made to the SearchAccounts Api with "<customerFirstName>"TC55
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | customerFirstName          | errorCode | errorMessage                |
      | MAX_LENGTH_FIRST_NAME_TC55 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidSocialSecurityNumber @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid socialSecurityNumber "<socialSecurityNumber>"TC56_TC57
    When a request is made to the SearchAccounts Api with "<socialSecurityNumber>"TC56_TC57
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | socialSecurityNumber                      | errorCode | errorMessage                |
      | NOT_ENCRYPTED_SOCIAL_SECURITY_NUMBER_TC56 | 10115     | Invalid search parameter(s) |
      | ENCRYPTED_SOCIAL_SECURITY_NUMBER_TC57     | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidFederalTaxID @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid federalTaxID "<federalTaxID>"TC58_TC59
    When a request is made to the SearchAccounts Api with "<federalTaxID>"TC58_TC59
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | federalTaxID                      | errorCode | errorMessage                |
      | NOT_ENCRYPTED_FEDERAL_TAX_ID_TC58 | 10115     | Invalid search parameter(s) |
      | ENCRYPTED_FEDERAL_TAX_ID_TC59     | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPhoneNumber  @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid phoneNumber "<phoneNumber>"TC60
    When a request is made to the SearchAccounts Api with "<phoneNumber>"TC60
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | phoneNumber                  | errorCode | errorMessage                |
      | MAX_LENGTH_PHONE_NUMBER_TC60 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidAGLCAccountNumber @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid aglcAccountNumber "<aglcAccountNumber>"TC61
    When a request is made to the SearchAccounts Api with "<aglcAccountNumber>"TC61
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | aglcAccountNumber                   | errorCode | errorMessage                |
      | MAX_LENGTH_AGLC_ACCOUNT_NUMBER_TC61 | 10115     | Invalid search parameter(s) |


  @SearchAccountsInvalidPremisesStreetNumber @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid premisesStreetNumber "<premisesStreetNumber>"TC62
    When a request is made to the SearchAccounts Api with "<premisesStreetNumber>"TC62
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetNumber                   | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_STREET_NUMBER_TC62 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesStreetPreDirection @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesStreetPreDirection "<premisesStreetPreDirection>"TC63
    When a request is made to the SearchAccounts Api with "<premisesStreetPreDirection>"TC63
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetPreDirection                    | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_STREET_PRE_DIRECTION_TC63 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesStreetName @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid premisesStreetPreDirection "<premisesStreetName>"TC64
    When a request is made to the SearchAccounts Api with "<premisesStreetName>"TC64
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetName                   | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_STREET_NAME_TC64 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesStreetSuffix @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid premisesStreetSuffix "<premisesStreetSuffix>"TC65
    When a request is made to the SearchAccounts Api with "<premisesStreetSuffix>"TC65
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetSuffix                   | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_STREET_SUFFIX_TC65 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesStreetPostDirection @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid premisesStreetPostDirection "<premisesStreetPostDirection>"TC66
    When a request is made to the SearchAccounts Api with "<premisesStreetPostDirection>"TC66
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetPostDirection                    | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_STREET_POST_DIRECTION_TC66 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesUnitType @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid premisesUnitType "<premisesUnitType>"TC67
    When a request is made to the SearchAccounts Api with "<premisesUnitType>"TC67
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesUnitType                   | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_UNIT_TYPE_TC67 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesUnitNumber @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid premisesUnitNumber "<premisesUnitNumber>"TC68
    When a request is made to the SearchAccounts Api with "<premisesUnitNumber>"TC68
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesUnitNumber                   | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_UNIT_NUMBER_TC68 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesCity @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid premisesCity "<premisesCity>"TC69
    When a request is made to the SearchAccounts Api with "<premisesCity>"TC69
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesCity                  | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_CITY_TC69 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesStateCode @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid premisesStateCode "<premisesStateCode>"TC70
    When a request is made to the SearchAccounts Api with "<premisesStateCode>"TC70 and "<invalidStateCode>" invalid state code
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>" with Invalid State code "<invalidStateCode>"
    Examples:
      | premisesStateCode                   | errorCode |  errorMessage                      | invalidStateCode |
      | MAX_LENGTH_PREMISES_STATE_CODE_TC70 | 10115     | Invalid PremisesStateCode provided | KJNY             |

  @SearchAccountsInvalidPremisesZipCode @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code for invalid premisesZipCode "<testCondition>" condition
    When a request is made to the SearchAccounts Api with "<invalidPremisesZipCode>" invalid premises zip code for "<testCondition>" condition
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>" with Invalid premise zip code "<invalidPremisesZipCode>"
    Examples:
      | invalidPremisesZipCode | testCondition                     | errorCode | errorMessage                    |
      | 301235                | MAX_LENGTH_PREMISES_ZIP_CODE_TC71 | 10115     | Invalid PremisesZipCode provided |

  @SearchAccountsInvalidSearchParameters @turnOn @Phase1  @NegativeFlow
  Scenario Outline: SearchAccounts Api - Verify response code with invalid search parameters for "<testCondition>" condition
    When a request is made to the SearchAccounts Api with invalid search parameters for "<testCondition>" condition
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
       | testCondition                                                                                           | errorCode | errorMessage                                                                                                                                                                                        |
       | ALL_SEARCH_PARAMETERS_MISSING_TC72                                                                      | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
       | ONLY_PREMISES_CODE_PROVIDED_TC73                                                                        | 1001      | Invalid Request: Invalid required search field combination - customerCode required.                                                                                                                 |
       | ONLY_CUSTOMER_CODE_PROVIDED_TC74                                                                        | 1001      | Invalid Request: Invalid required search field combination - premisesCode required.                                                                                                                 |
       | ONLY_CUSTOMER_FIRST_NAME_PROVIDED_TC75                                                                  | 1001      | Invalid Request: Invalid required search field combination - customerLastName required.                                                                                                             |
       | ONLY_CUSTOMER_LAST_NAME_PROVIDED_TC76                                                                   | 1001      | Invalid Request: Invalid required search field combination - premisesZipCode required.                                                                                                              |
       | SSN_PROVIDED_FEDERAL_TAX_ID_PROVIDE_TC77                                                                | 10114     | Only Social Security Number or Federal Tax ID field can be provided                                                                                                                                 |
       | ONLY_PREMISES_STREET_NUMBER_PROVIDED_TC78                                                               | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | ONLY_PREMISES_STREET_PRE_DIRECTION_PROVIDED_TC79                                                        | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | ONLY_PREMISES_STREET_SUFFIX_PROVIDED_TC80                                                               | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC81                               | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_STREET_NUMBER_PREMISES_STREET_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC82         | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_STREET_NUMBER_PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC83                | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_STREET_NUMBER_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC84        | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_STREET_PRE_DIRECTION_PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC85         | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_STREET_PRE_DIRECTION_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC86 | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_STREET_SUFFIX_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC87        | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_STREET_NUMBER_SUFFIX_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC88                  | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_STREET_NUMBER_SUFFIX_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC89                 | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_PRE_DIRECTION_SUFFIX_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC90                 | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_STREET_NUMBER_PRE_DIRECTION_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC91          | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_STREET_NUMBER_SUFFIX_PRE_DIRECTION_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC92   | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_STREET_NAME_PROVIDED_CITY_ZIP_STATE_CODE_MISSING_TC93                                          | 1001      | Invalid Request: Invalid required search field combination - premisesCity required.                                                                                                                 |
       | PREMISES_CITY_PROVIDED_STREET_NAME_ZIP_STATE_CODE_MISSING_TC94                                          | 1001      | Invalid Request: Invalid required search field combination - premisesStreetName required.                                                                                                           |
       | PREMISES_STATE_CODE_PROVIDED_STREET_NAME_ZIP_CITY_MISSING_TC95                                          | 1000      | Invalid Request: Invalid required search field combination                                                                                                                                          |
       | PREMISES_ZIP_CODE_PROVIDED_STREET_NAME_ZIP_STATE_MISSING_TC96                                           | 1001      | Invalid Request: Invalid required search field combination - premisesStreetName required.                                                                                                           |
       | PREMISES_STREET_NAME_CITY_PROVIDED_ZIP_STATE_MISSING_TC97                                               | 1001      | Invalid Request: Invalid required search field combination - premisesStateCode required.                                                                                                            |
       | PREMISES_STREET_NAME_STATE_PROVIDED_ZIP_CITY_MISSING_TC98                                               | 1001      | Invalid Request: Invalid required search field combination - premisesCity required.                                                                                                                 |
       | PREMISES_STREET_NAME_ZIP_PROVIDED_STATE_CITY_MISSING_TC99                                               | 1001      | Invalid Request: Invalid required search field combination - premisesCity required.                                                                                                                 |
       | PREMISES_STATE_CITY_PROVIDED_STREET_NAME_ZIP_MISSING_TC100                                              | 1001      | Invalid Request: Invalid required search field combination - premisesStreetName required.                                                                                                          |
       | PREMISES_STATE_CITY_ZIP_PROVIDED_STREET_NAME_STATE_MISSING_TC101                                        | 1001      | Invalid Request: Invalid required search field combination - premisesStreetName required.                                                                                                         |
       | PREMISES_STATE_ZIP_PROVIDED_STREET_NAME_CITY_MISSING_TC102                                              | 1001      | Invalid Request: Invalid required search field combination - premisesStreetName required.                                                                                                         |
       | PREMISES_STATE_STREET_NAME_CITY_PROVIDED_ZIP_MISSING_TC103                                              | 1001      | Invalid Request: Invalid required search field combination - premisesZipCode required.                                                                                                            |
       | PREMISES_STATE_STREET_NAME_PROVIDED_CITY_MISSING_TC104                                                  | 1001      | Invalid Request: Invalid required search field combination - premisesCity required.                                                                                                                |
       | PREMISES_ZIP_STREET_NAME_CITY_PROVIDED_STATE_MISSING_TC105                                              | 1001      | Invalid Request: Invalid required search field combination - premisesStateCode required.                                                                                                          |
       | PREMISES_ZIP_STATE_CITY_PROVIDED_STREET_NAME_MISSING_TC106                                              | 1001      | Invalid Request: Invalid required search field combination - premisesStreetName required.                                                                                                         |


  @SearchAccountsCustomerCodeAndPremisesCodeNotInTable @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- SearchAccountsApiTurnOn- Verify response when Customer Code and premises code are passed that do not exist in the database TC107
    When a request is made to the SearchAccounts Api with customer code "5555555" and premises code "55555" that do not exist in database TC_107
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 0

  @SearchAccountsReturned30RecordsWhenExceedsPSTOValue @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when returned records exceeds the PSTO value TC108
    When a request is made to the SearchAccounts Api with customer business name as "BUSINESS" returned records exceeds the PSTO value TC_108
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 30

  @SearchAccountsAccountNumberSearchBTypeNoSSP @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when Account of B Type and No SSP is searched TC109
    When a request is made to the SearchAccounts Api for Account Number with B Type No SSP "N" TC_109
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 1
    And response should have "accountStatus" as "A"
    And response should have "recordType" as "BANNER RECORD"
    And response should have "sspStatusIndicator" as "false"

  @SearchAccountsAccountNumberSearchETypeNoSSP @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when an Account Number is searched for E Type No SSP TC110
    When a request is made to the SearchAccounts Api for Account Number with E Type No SSP "N" TC_110
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 1
    And response should have "recordType" as "ENROLLMENT RECORD"
    And response should have "sspStatusIndicator" as "false"

  @SearchAccountsSSPBasedOnTheProvidedLastNameAndZipCode @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response for search accounts api when last name and zip are passed for SSP account with status indicator true TC111
    When a request is made to the SearchAccounts Api with SSP "Y" TC_111
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "SSP ACCOUNT RECORD"
    And response should have "transactionType" as "TNON"
    And response should have "sspStatusIndicator" as "true"

  @withFirstLastNameZipETypeSSP @turnOn @Phase1 @HappyFlow
  Scenario: SearchAccountsApiTurnOn- Verify response code SSP Based with First Name & Last Name & Zip - E Type  TC112
    When a request is made to the GetEligiblePlansAndOffers Api for "SSP_FALL_TURN_ON_SEARCH_TC_112" condition
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "SSP_FALL_TURN_ON_SEARCH_TC_112"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And a request is made to get Marketer Reference Data
    And a request is made to the Save Enrollment API for the "SSP_FALL_TURN_ON_SEARCH_TC_112" with "MVS" and "25 CENTS FOR 12 MONTHS"
    And response should have ErrorCode 0 and ErrorMessage ""
    Then a request is made to the SearchAccountsApi for "SSP_FALL_TURN_ON_SEARCH_TC_112"
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "SSP FALL TURN ON RECORD"
    And response should have "transactionType" as "TNON"
    And response should have "sspStatusIndicator" as "true"

  @SearchAccountswithAGLCAccountNumberETypeNoSSP @turnOn @Phase1 @HappyFlow
  Scenario: SearchAccountsApiTurnOn- Verify response code with AGLC Account Number E Type No SSP TC_114
    When a request is made to the SearchAccounts Api with AGLC Account Number E Type No SSP TC_114
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And response should have "enrollmentState" as "DEPOSIT BILLED"
    And response should have "sspStatusIndicator" as "false"

  @SearchAccountsWithCustomerDataETypeSSP @turnOn @Phase1 @HappyFlow
  Scenario: SearchAccountsApiTurnOn- Verify response code with  Customer Data E Type SSP TC_115
    When a request is made to the SearchAccounts Api with Customer Data E Type SSP TC_115
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD"
    And response should have "transactionType" as "TNON"
    And response should have "sspStatusIndicator" as "true"

  @SearchAccountsWithStreetNameAndCityAndStateCodeAndZipCode @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when Street Name, City, State Code And Zip Code are passed in Search accounts api TC_116
    When a request is made to the SearchAccounts Api with Street Name And City And State Code And Zip Code TC_116
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @SearchAccountsWithNumberAndPreDirAndSuffixAndPostDirAndStreetNameAndCityAndStateCodeAndZipCode  @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when Street Number, PreDir, Suffix, PostDir, Street Name, City, State Code And Zip Code are passed in Search accounts api TC_117
    When a request is made to the SearchAccounts Api with Number And PreDir And Suffix And PostDir And Street Name And City And State Code And Zip Code TC_117
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @SearchAccounRecordsBasedOnTheProvidedPhoneNumber @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario Outline: SearchAccountsApiTurnOn- Verify response when a search is performed <testCondition>
    When a request is made to the SearchAccounts Api "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should return numberOfMatches as 1
    And response should have "recordType" as "BANNER RECORD"
    Examples:
    |testCondition                      |
    |SEARCH_BASED_ON_PHONE_BI_A_TC_119_1|
    |SEARCH_BASED_ON_PHONE_BI_I_TC_119_2|
    |SEARCH_BASED_ON_PHONE_BU_A_TC_119_3|
    |SEARCH_BASED_ON_PHONE_BU_I_TC_119_4|

  @SearchAccountsBusinessName @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify if the Records are retrieved Based on Business Name in Search Accounts API TC 120
    When a request is made to the SearchAccounts Api with Business Name TC_120
    Then verify response code of "SearchAccounts" Api is 200
    And response should return numberOfMatches as 1
    And response should have "recordType" as "BANNER RECORD"

  @SearchAccountsWildcardSearchName @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when a Wildcard Search is performed with firstname and lastname TC_121_1
    When a request is made to the SearchAccounts Api with Wildcard Search TC_121_1
    Then verify response code of "SearchAccounts" Api is 200
    And response should return numberOfMatches as 1
    And response should have "recordType" as "BANNER RECORD"

  @SearchAccountsWildcardSearchCity @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when a Wildcard Search is performed with city TC_121_2
    When a request is made to the SearchAccounts Api with Wildcard Search TC_121_2
    Then verify response code of "SearchAccounts" Api is 200
    And response should return numberOfMatches as 30

  @SearchAccountsPartialPayment @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response for Accounts with Partial Payment TC_121a
    When a request is made to the SearchAccounts Api with Account details that have Partial Payment TC_121a
    Then verify response code of "SearchAccounts" Api is 200
    And response should return numberOfMatches as 1
    And response should have "pastDueAmount" to "exist"
    And response should have "paymentAmount" to "exist"
    And response should have "paymentConfirmationNumber" to "not empty"

#  @SearchAccountsFullPayment @Phase1 @HappyFlow
#  Scenario: SearchAccountsApiTurnOn- Verify response code Enrollment Records with Full Payment TC_121b
#    When a request is made to the SearchAccounts Api with Full Payment TC_121b
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should return numberOfMatches as 1

  @SearchAccountsNoPayment @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response for Accounts with No Payment TC_121c
    When a request is made to the SearchAccounts Api with Account details that have No Payment TC_121c
    Then verify response code of "SearchAccounts" Api is 200
    And response should return numberOfMatches as 1
    And response should have "paymentAmount" to "not exist"
    And response should have "paymentConfirmationNumber" to "empty"

#  @SearchAccountsMultiplePayment @Phase1 @HappyFlow
#  Scenario: SearchAccountsApiTurnOn- Verify response code Enrollment Records with Multiple Payment TC_121d
#    When a request is made to the SearchAccounts Api with Multiple Payment TC_121d
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should return numberOfMatches as 0

  @SearchAccountsSSPParticipantCode @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for SSP Participant Code <TestCondition>
    When a request is made to the SearchAccounts Api with an SSP Participant Code for "<TestCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 1
    And response should have "sspStatusIndicator" flag as "<sspStatusIndicator>"
    And response should have "sspParticipantCode" to "<sspParticipantCode>"

    Examples:
    |TestCondition                   |sspParticipantCode       |sspStatusIndicator|
    |ACTIVE_UZBSSPP_STATUS_TC121E_1  |not empty                |true              |
    |INACTIVE_UZBSSPP_STATUS_TC121E_2|null                     |false             |

  @SearchAccountsNotInSSPParticipantParentTable @turnOn @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response for search account API when customer code is not in SSP Participant Parent table TC_121E_3
    When a request is made to the SearchAccounts Api with account that does not exist in SSP Participant parent table TC_112e_3
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 1
    And response should have "sspStatusIndicator" flag as "false"
    And response should have "sspParticipantCode" to "null"

  @SearchAccountsTurnOnValidSSN @turnOn @Phase1 @HappyFlow
  Scenario: SearchAccountsApiTurnOn- Verify the Search accounts api returns matching enrollment record in SSP when provided with SSN TC113
    When a request is made to the SearchAccounts Api TurnOn with a valid SSN
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And response should have "sspStatusIndicator" as "false"