Feature: Verify SearchAccounts Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @SearchAccountsWithInvalidRequestID @Phase1 @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify SearchAccounts Api with invalid requestID "<requestID>"TC42_TC44
    When a request is made to the SearchAccounts Api with "<requestID>"TC42_TC44
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID                 | errorCode | errorMessage         |
      | NULL_REQUEST_ID_TC42      | 10001     | Missing Request ID   |
      | DUPLICATE_REQUEST_ID_TC43 | 10003     | Duplicate Request ID |
      | LONG_REQUEST_ID_TC44      | 10002     | Invalid Request ID   |

  @SearchAccountsInvalidLoginID @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid loginID "<loginID>"TC45_TC48
    When a request is made to the SearchAccounts Api with "<loginID>"TC45_TC48
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | loginID                                      | errorCode | errorMessage                |
      | NULL_LOGIN_ID_TC45                           | 10112     | Invalid or missing Login ID |
      #| ALPHANUMERIC_LOGIN_ID_TC47                   | 10112     | Invalid or missing Login ID |
      | MAX_LENGTH_LOGIN_ID_TC46                     | 10112     | Invalid or missing Login ID |
      | INVALID_LOGIN_ID_NOT_PRESENT_USER_TABLE_TC48 | 2000      | Invalid Login ID            |

  @SearchAccountsInvalidCustomerCode @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid customerCode "<customerCode>"TC49
    When a request is made to the SearchAccounts Api with "<customerCode>"TC49
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | customerCode                  | errorCode | errorMessage                |
      | MAX_LENGTH_CUSTOMER_CODE_TC49 | 10115     | Invalid search parameter(s) |

#  @SearchAccountsInvalidPremisesCode @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesCode "<premisesCode>"TC49
#    When a request is made to the SearchAccounts Api with "<premisesCode>"TC49
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | premisesCode                  | errorCode | errorMessage                |
#      | MAX_LENGTH_PREMISES_CODE_TC50 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidTransactionType @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid transactionType "<transactionType>"TC51_TC52
    When a request is made to the SearchAccounts Api with "<transactionType>"TC51_TC52
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | transactionType               | errorCode | errorMessage                        |
      | MISSING_TRANSACTION_TYPE_TC51 | 10113     | Invalid or missing Transaction Type |
      | INVALID_TRANSACTION_TYPE_TC52 | 10113     | Invalid or missing Transaction Type |

  @SearchAccountsInvalidBusinessName @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid businessName "<businessName>"TC53
    When a request is made to the SearchAccounts Api with "<businessName>"TC53
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | businessName                  | errorCode | errorMessage                |
      | MAX_LENGTH_BUSINESS_NAME_TC53 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidLastName @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid lastName "<lastName>"TC54
    When a request is made to the SearchAccounts Api with "<lastName>"TC54
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | lastName                  | errorCode | errorMessage                |
      | MAX_LENGTH_LAST_NAME_TC54 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidFirstName @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid customerFirstName "<customerFirstName>"TC55
    When a request is made to the SearchAccounts Api with "<customerFirstName>"TC55
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | customerFirstName          | errorCode | errorMessage                |
      | MAX_LENGTH_FIRST_NAME_TC55 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidSocialSecurityNumber @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid socialSecurityNumber "<socialSecurityNumber>"TC56_TC57
    When a request is made to the SearchAccounts Api with "<socialSecurityNumber>"TC56_TC57
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | socialSecurityNumber                      | errorCode | errorMessage                |
      | NOT_ENCRYPTED_SOCIAL_SECURITY_NUMBER_TC56 | 10115     | Invalid search parameter(s) |
      | ENCRYPTED_SOCIAL_SECURITY_NUMBER_TC57     | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidFederalTaxID @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid federalTaxID "<federalTaxID>"TC58_TC59
    When a request is made to the SearchAccounts Api with "<federalTaxID>"TC58_TC59
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | federalTaxID                      | errorCode | errorMessage                |
      | NOT_ENCRYPTED_FEDERAL_TAX_ID_TC58 | 10115     | Invalid search parameter(s) |
      | ENCRYPTED_FEDERAL_TAX_ID_TC59     | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPhoneNumber  @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid phoneNumber "<phoneNumber>"TC60
    When a request is made to the SearchAccounts Api with "<phoneNumber>"TC60
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | phoneNumber                  | errorCode | errorMessage                |
      | MAX_LENGTH_PHONE_NUMBER_TC60 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidAGLCAccountNumber  @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid aglcAccountNumber "<aglcAccountNumber>"TC61
    When a request is made to the SearchAccounts Api with "<aglcAccountNumber>"TC61
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | aglcAccountNumber                   | errorCode | errorMessage                |
      | MAX_LENGTH_AGLC_ACCOUNT_NUMBER_TC61 | 10115     | Invalid search parameter(s) |


  @SearchAccountsInvalidPremisesStreetNumber  @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesStreetNumber "<premisesStreetNumber>"TC62
    When a request is made to the SearchAccounts Api with "<premisesStreetNumber>"TC62
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetNumber                   | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_STREET_NUMBER_TC62 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesStreetPreDirection  @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesStreetPreDirection "<premisesStreetPreDirection>"TC63
    When a request is made to the SearchAccounts Api with "<premisesStreetPreDirection>"TC63
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetPreDirection                    | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_STREET_PRE_DIRECTION_TC63 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesStreetName @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesStreetPreDirection "<premisesStreetName>"TC64
    When a request is made to the SearchAccounts Api with "<premisesStreetName>"TC64
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetName                   | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_STREET_NAME_TC64 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesStreetSuffix @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesStreetSuffix "<premisesStreetSuffix>"TC65
    When a request is made to the SearchAccounts Api with "<premisesStreetSuffix>"TC65
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetSuffix                   | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_STREET_SUFFIX_TC65 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesStreetPostDirection @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesStreetPostDirection "<premisesStreetPostDirection>"TC66
    When a request is made to the SearchAccounts Api with "<premisesStreetPostDirection>"TC66
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesStreetPostDirection                    | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_STREET_POST_DIRECTION_TC66 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesUnitType @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesUnitType "<premisesUnitType>"TC67
    When a request is made to the SearchAccounts Api with "<premisesUnitType>"TC67
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesUnitType                   | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_UNIT_TYPE_TC67 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesUnitNumber @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesUnitNumber "<premisesUnitNumber>"TC68
    When a request is made to the SearchAccounts Api with "<premisesUnitNumber>"TC68
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesUnitNumber                   | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_UNIT_NUMBER_TC68 | 10115     | Invalid search parameter(s) |

  @SearchAccountsInvalidPremisesCity @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesCity "<premisesCity>"TC69
    When a request is made to the SearchAccounts Api with "<premisesCity>"TC69
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | premisesCity                  | errorCode | errorMessage                |
      | MAX_LENGTH_PREMISES_CITY_TC69 | 10115     | Invalid search parameter(s) |

#  @SearchAccountsInvalidPremisesStateCode @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesStateCode "<premisesStateCode>"TC70
#    When a request is made to the SearchAccounts Api with "<premisesStateCode>"TC70
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | premisesStateCode                   | errorCode | errorMessage                |
#      | MAX_LENGTH_PREMISES_STATE_CODE_TC70 | 10115     | Invalid search parameter(s) |

#  @SearchAccountsInvalidPremisesZipCode @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesZipCode "<premisesZipCode>"TC71
#    When a request is made to the SearchAccounts Api with "<premisesZipCode>"TC71
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | premisesZipCode                   | errorCode | errorMessage                |
#      | MAX_LENGTH_PREMISES_ZIP_CODE_TC71 | 10115     | Invalid search parameter(s) |

#  @SearchAccountsInvalidCustomerCode @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid customerCode "<customerCode>"TC73
#    When a request is made to the SearchAccounts Api with "<customerCode>"TC73
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | customerCode                                      | errorCode | errorMessage                                                                                                                                                                                        |
#      | CUSTOMER_CODE_PROVIDED_PREMISES_CODE_MISSING_TC73 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsInvalidPremisesCode @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesCode "<premisesCode>"TC74
#    When a request is made to the SearchAccounts Api with "<premisesCode>"TC74
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | premisesCode                                      | errorCode | errorMessage                                                                                                                                                                                        |
#      | CUSTOMER_CODE_MISSING_PREMISES_CODE_PROVIDED_TC74 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsInvalidCustomerLastName @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid customerLastName "<customerLastName>"TC75
#    When a request is made to the SearchAccounts Api with "<customerLastName>"TC75
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | customerLastName                                             | errorCode | errorMessage                                                                                                                                                                                        |
#      | CUSTOMER_FIRST_NAME_PROVIDED_CUSTOMER_LAST_NAME_MISSING_TC75 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsInvalidPremisesZipCode @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesZipCode "<premisesZipCode>"TC76
#    When a request is made to the SearchAccounts Api with "<premisesZipCode>"TC76
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | premisesZipCode                                       | errorCode | errorMessage                                                                                                                                                                                        |
#      | CUSTOMER_LAST_NAME_PROVIDED_PREMISES_ZIP_MISSING_TC76 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

  @SearchAccountsInvalidSSNAndFederalTaxID @Phase1  @NegativeFlow
  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid SSNAndFederalTaxID "<SSNAndFederalTaxID>"TC77
    When a request is made to the SearchAccounts Api with "<SSNAndFederalTaxID>"TC77
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | SSNAndFederalTaxID                       | errorCode | errorMessage                                                        |
      | SSN_PROVIDED_FEDERAL_TAX_ID_PROVIDE_TC77 | 10114     | Only Social Security Number or Federal Tax ID field can be provided |

#  @SearchAccountsInvalidPremisesStreetName @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid premisesStreetName "<premisesStreetName>"TC78_TC92
#    When a request is made to the SearchAccounts Api with "<premisesStreetName>"TC78_TC92
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | premisesStreetName                                                                                      | errorCode | errorMessage                                                                                                                                                                                        |
      #| PREMISES_STREET_NUMBER_PROVIDED_PREMISES_STREET_NAME_MISSING_TC78                                       | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC79                                | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC80                                       | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC81                               | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_NUMBER_PREMISES_STREET_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC82         | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_NUMBER_PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC83                | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_NUMBER_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC84        | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_PRE_DIRECTION_PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC85         | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_PRE_DIRECTION_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC86 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_SUFFIX_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC87        | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_NUMBER_SUFFIX_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC88                  | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_NUMBER_SUFFIX_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC89                 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_PRE_DIRECTION_SUFFIX_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC90                 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_NUMBER_PRE_DIRECTION_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC91          | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |
      #| PREMISES_STREET_NUMBER_SUFFIX_PRE_DIRECTION_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC92   | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingCityAndZip @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingAddressFieldsCityAndZip "<MissingCityAndZip>"TC93
#    When a request is made to the SearchAccounts Api with "<MissingCityAndZip>"TC93
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingCityAndZip                                              | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_STREET_NAME_PROVIDED_CITY_ZIP_STATE_CODE_MISSING_TC93 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingAddressFieldsZipAndState @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingAddressFieldsZipAndState "<MissingAddressFieldsZipAndState>"TC94
#    When a request is made to the SearchAccounts Api with "<MissingAddressFieldsZipAndState>"TC94
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingAddressFieldsZipAndState                                | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_CITY_PROVIDED_STREET_NAME_ZIP_STATE_CODE_MISSING_TC94 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingZipStreetNameAndCity @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingZipStreetNameAndCity "<MissingZipStreetNameAndCity>"TC95
#    When a request is made to the SearchAccounts Api with "<MissingZipStreetNameAndCity>"TC95
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingZipStreetNameAndCity                                    | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_STATE_CODE_PROVIDED_STREET_NAME_ZIP_CITY_MISSING_TC95 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingStreetNameStateAndCity @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingStreetNameStateAndCity "<MissingStreetNameStateAndCity>"TC96
#    When a request is made to the SearchAccounts Api with "<MissingStreetNameStateAndCity>"TC96
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingStreetNameStateAndCity                                 | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_ZIP_CODE_PROVIDED_STREET_NAME_ZIP_STATE_MISSING_TC96 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingStateAndZipCode @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingStateAndZipCode "<MissingStateAndZipCode>"TC97
#    When a request is made to the SearchAccounts Api with "<MissingStateAndZipCode>"TC97
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingStateAndZipCode                                    | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_STREET_NAME_CITY_PROVIDED_ZIP_STATE_MISSING_TC97 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingCityAndZipCode @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingCityAndZipCode "<MissingCityAndZipCode>"TC98
#    When a request is made to the SearchAccounts Api with "<MissingCityAndZipCode>"TC98
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingCityAndZipCode                                     | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_STREET_NAME_STATE_PROVIDED_ZIP_CITY_MISSING_TC98 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingStateAndCity @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingStateAndCity "<MissingStateAndCity>"TC99
#    When a request is made to the SearchAccounts Api with "<MissingStateAndCity>"TC99
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingStateAndCity                                       | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_STREET_NAME_ZIP_PROVIDED_STATE_CITY_MISSING_TC99 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingStreetNameAndZip @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingStreetNameAndZip "<MissingStreetNameAndZip>"TC100
#    When a request is made to the SearchAccounts Api with "<MissingStreetNameAndZip>"TC100
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingStreetNameAndZip                                    | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_STATE_CITY_PROVIDED_STREET_NAME_ZIP_MISSING_TC100 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingStreetNameAndState @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingStreetNameAndState "<MissingStreetNameAndState>"TC101
#    When a request is made to the SearchAccounts Api with "<MissingStreetNameAndState>"TC101
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingStreetNameAndState                                        | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_STATE_CITY_ZIP_PROVIDED_STREET_NAME_STATE_MISSING_TC101 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingStreetNameAndCity @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingStreetNameAndCity "<MissingStreetNameAndCity>"TC102
#    When a request is made to the SearchAccounts Api with "<MissingStreetNameAndCity>"TC102
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingStreetNameAndCity                                   | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_STATE_ZIP_PROVIDED_STREET_NAME_CITY_MISSING_TC102 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingZipCode @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingZipCode "<MissingZipCode>"TC103
#    When a request is made to the SearchAccounts Api with "<MissingZipCode>"TC103
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingZipCode                                             | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_STATE_STREET_NAME_CITY_PROVIDED_ZIP_MISSING_TC103 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingCityAddress @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingCityAddress "<MissingCityAddress>"TC104
#    When a request is made to the SearchAccounts Api with "<MissingCityAddress>"TC104
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingCityAddress                                     | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_STATE_STREET_NAME_PROVIDED_CITY_MISSING_TC104 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingStateCode @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingStateCode "<MissingStateCode>"TC105
#    When a request is made to the SearchAccounts Api with "<MissingStateCode>"TC105
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingStateCode                                           | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_ZIP_STREET_NAME_CITY_PROVIDED_STATE_MISSING_TC105 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

#  @SearchAccountsMissingStreetName @Phase1  @NegativeFlow
#  Scenario Outline: SearchAccountsApiTurnOn- Verify response code for invalid MissingStreetName "<MissingStreetName>"TC106
#    When a request is made to the SearchAccounts Api with "<MissingStreetName>"TC106
#    Then verify response code of "SearchAccounts" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    Examples:
#      | MissingStreetName                                          | errorCode | errorMessage                                                                                                                                                                                        |
#      | PREMISES_ZIP_STATE_CITY_PROVIDED_STREET_NAME_MISSING_TC106 | 10116     | At least one of the following is required: GNG Account Number, Business Name, Last Name and Zip Code, Social Security Number, Federal Tax ID, Phone Number, AGLC Account Number or Premises Address |

  @SearchAccountsCustomerCodeAndPremisesCodeNotInTable @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- SearchAccountsApiTurnOn- Verify response when Customer Code and premises code are passed that do not exist in the database TC107
    When a request is made to the SearchAccounts Api with customer code "5555555" and premises code "55555" that do not exist in database TC_107
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 0

  @SearchAccountsReturned30RecordsWhenExceedsPSTOValue @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when returned records exceeds the PSTO value TC108
    When a request is made to the SearchAccounts Api with customer business name as "BUSINESS" returned records exceeds the PSTO value TC_108
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 30

  @SearchAccountsAccountNumberSearchBTypeNoSSP @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when Account of B Type and No SSP is searched TC109
    When a request is made to the SearchAccounts Api for Account Number with B Type No SSP "N" TC_109
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 1
    And response should have "accountStatus" as "A"
    And response should have "recordType" as "BANNER RECORD"
    And response should have "sspStatusIndicator" as "false"

  @SearchAccountsAccountNumberSearchETypeNoSSP @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when an Account Number is searched for E Type No SSP TC110
    When a request is made to the SearchAccounts Api for Account Number with E Type No SSP "N" TC_110
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 1
    And response should have "recordType" as "ENROLLMENT RECORD"
    And response should have "sspStatusIndicator" as "false"

  @SearchAccountsSSPBasedOnTheProvidedLastNameAndZipCode @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response for search accounts api when last name and zip are passed for SSP account with status indicator true TC111
    When a request is made to the SearchAccounts Api with SSP "Y" TC_111
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "SSP ACCOUNT RECORD"
    And response should have "transactionType" as "TNON"
    And response should have "sspStatusIndicator" as "true"

  @withFirstLastNameZipETypeSSP @Phase1 @HappyFlow
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

  @SearchAccountswithAGLCAccountNumberETypeNoSSP @Phase1 @HappyFlow
  Scenario: SearchAccountsApiTurnOn- Verify response code with AGLC Account Number E Type No SSP TC_114
    When a request is made to the SearchAccounts Api with AGLC Account Number E Type No SSP TC_114
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And response should have "enrollmentState" as "DEPOSIT BILLED"
    And response should have "sspStatusIndicator" as "false"

  @SearchAccountsWithCustomerDataETypeSSP @Phase1 @HappyFlow
  Scenario: SearchAccountsApiTurnOn- Verify response code with  Customer Data E Type SSP TC_115
    When a request is made to the SearchAccounts Api with Customer Data E Type SSP TC_115
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD"
    And response should have "transactionType" as "TNON"
    And response should have "sspStatusIndicator" as "true"

  @SearchAccountsWithStreetNameAndCityAndStateCodeAndZipCode @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when Street Name, City, State Code And Zip Code are passed in Search accounts api TC_116
    When a request is made to the SearchAccounts Api with Street Name And City And State Code And Zip Code TC_116
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @SearchAccountsWithNumberAndPreDirAndSuffixAndPostDirAndStreetNameAndCityAndStateCodeAndZipCode  @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when Street Number, PreDir, Suffix, PostDir, Street Name, City, State Code And Zip Code are passed in Search accounts api TC_117
    When a request is made to the SearchAccounts Api with Number And PreDir And Suffix And PostDir And Street Name And City And State Code And Zip Code TC_117
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @SearchAccounRecordsBasedOnTheProvidedPhoneNumber @Phase1 @HappyFlow @SearchAccountsTurnOn
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

  @SearchAccountsBusinessName @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify if the Records are retrieved Based on Business Name in Search Accounts API TC 120
    When a request is made to the SearchAccounts Api with Business Name TC_120
    Then verify response code of "SearchAccounts" Api is 200
    And response should return numberOfMatches as 1
    And response should have "recordType" as "BANNER RECORD"

  @SearchAccountsWildcardSearchName @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when a Wildcard Search is performed with firstname and lastname TC_121_1
    When a request is made to the SearchAccounts Api with Wildcard Search TC_121_1
    Then verify response code of "SearchAccounts" Api is 200
    And response should return numberOfMatches as 1
    And response should have "recordType" as "BANNER RECORD"

  @SearchAccountsWildcardSearchCity @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response when a Wildcard Search is performed with city TC_121_2
    When a request is made to the SearchAccounts Api with Wildcard Search TC_121_2
    Then verify response code of "SearchAccounts" Api is 200
    And response should return numberOfMatches as 30

  @SearchAccountsPartialPayment @Phase1 @HappyFlow @SearchAccountsTurnOn
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

  @SearchAccountsNoPayment @Phase1 @HappyFlow @SearchAccountsTurnOn
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

  @SearchAccountsSSPParticipantCode @Phase1 @HappyFlow @SearchAccountsTurnOn
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

  @SearchAccountsNotInSSPParticipantParentTable @Phase1 @HappyFlow @SearchAccountsTurnOn
  Scenario: SearchAccountsApiTurnOn- Verify response for search account API when customer code is not in SSP Participant Parent table TC_121E_3
    When a request is made to the SearchAccounts Api with account that does not exist in SSP Participant parent table TC_112e_3
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should return numberOfMatches as 1
    And response should have "sspStatusIndicator" flag as "false"
    And response should have "sspParticipantCode" to "null"

  @SearchAccountsTurnOnValidSSN @Phase1 @HappyFlow
  Scenario: SearchAccountsApiTurnOn- Verify the Search accounts api returns matching enrollment record in SSP when provided with SSN TC113
    When a request is made to the SearchAccounts Api TurnOn with a valid SSN
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And response should have "recordType" as "ENROLLMENT RECORD"
    And response should have "sspStatusIndicator" as "false"