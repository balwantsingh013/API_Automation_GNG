Feature: Verify SearchAccounts ServiceTransfer Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response


  @SearchAccountsServiceTransfer @Phase1 @NegativeFlow
  Scenario Outline: SearchAccountsApiServiceTransfer Api - Verify the "<testCondition>" for searchAccounts API
    When a request is made to the SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                          | errorCode | errorMessage                                                                                                                                                                     |
      | NO_SEARCH_PARAMETERS_PROVIDED_TC_1                     | 10116     | At least one of the following is required: GNG Account Number, Business Name, First and Last Name (with Zip Code), Social Security Number, AGLC Account Number or Premise Address.|
      | MISSING_REQUEST_ID_TC_2                                | 10001     | Missing Request ID                                                                                                                                                                |
      | INVALID_REQUEST_ID_LENGTH_TC_3                         | 10002     | Invalid Request ID                                                                                                                                                                |
      | DUPLICATE_REQUEST_ID_TC_4                              | 10003     | Duplicate Request ID                                                                                                                                                              |
      | MISSING_LOGIN_ID_TC_5                                  | 10112     | Invalid or missing Login ID                                                                                                                                                       |
      | INVALID_LOGIN_ID_LENGTH_TC_6                           | 10112     | Invalid or missing Login ID                                                                                                                                                       |
      | INVALID_LOGIN_ID_FORMAT_TC_7                           | 10112     | Invalid or missing Login ID                                                                                                                                                       |
      | INVALID_LOGIN_ID_TC_8                                  | 2000      | Invalid Login ID                                                                                                                                                                  |
      | MISSING_CUSTOMER_CODE_TC_9                             | 1001      | Invalid Request: Invalid required search field combination - customerCode required.                                                                                               |
      | MISSING_PREMISES_CODE_TC_10                            | 1001      | Invalid Request: Invalid required search field combination - premisesCode required.                                                                                               |
      | INVALID_CUSTOMER_CODE_LENGTH_TC_11                     | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | INVALID_PREMISES_CODE_LENGTH_TC_12                     | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | MISSING_TRANSACTION_TYPE_TC_13                         | 10113     | Invalid or missing Transaction Type                                                                                                                                               |
      | INVALID_TRANSACTION_TYPE_TC_14                         | 10113     | Invalid or missing Transaction Type                                                                                                                                               |
      | INVALID_TRANSACTION_TYPE_LENGTH_TC_15                  | 10113     | Invalid or missing Transaction Type                                                                                                                                               |
      | INVALID_BUSINESS_NAME_LENGTH_TC_16                     | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | INVALID_CUSTOMER_LAST_NAME_LENGTH_TC_17                | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | MISSING_PREMISES_ZIP_CODE_TC_19                        | 1001      | Invalid Request: Invalid required search field combination - premisesZipCode required.                                                                                            |
      | MISSING_TRANSACTION_TYPE_TC_20                         | 10113     | Invalid or missing Transaction Type                                                                                                                                               |
      | INVALID_FIRST_NAME_LENGTH_TC_21                        | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | UNENCRYPTED_SSN_TC_22                                  | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | INVALID_SSN_LENGTH_TC_23                               | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | TAX_ID_NOT_ALLOWED_TC_24                               | 2200      | Parameter Value should be null-Federal Tax ID                                                                                                                                     |
      | SSN_AND_TAX_ID_PROVIDED_TC_25                          | 2200      | Parameter Value should be null-Federal Tax ID                                                                                                                                     |
      | PHONE_NOT_ALLOWED_TC_26                                | 2200      | Parameter Value should be null-Phone Number                                                                                                                                       |
      | INVALID_AGLC_ACCOUNT_NO_LENGTH_TC_27                   | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | NON_NUMERIC_AGLC_ACCOUNT_NO_TC_28                      | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | INVALID_STREET_NUMBER_LENGTH_TC_29                     | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | INVALID_STREET_PRE_DIR_LENGTH_TC_30                    | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | INVALID_STREET_NAME_LENGTH_TC_31                       | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | MISSING_PREMISES_STREET_NAME_TC_32                     | 1001      | Invalid Request: Invalid required search field combination - premisesStreetName required.                                                                                         |
      | INVALID_STREET_SUFFIX_LENGTH_TC_33                     | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | INVALID_PREMISES_STREET_POST_DIRECTION_LENGTH_TC_34    | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | INVALID_UNIT_TYPE_LENGTH_TC_35                         | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | PREMISES_UNIT_TYPE_MISSING_TC_36                       | 1001      | Invalid Request: Invalid required search field combination - premisesUnitType required.                                                                                           |
      | INVALID_PREMISES_UNIT_NUMBER_FORMAT_TC_37              | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | MISSING_UNIT_NUMBER_TC_38                              | 1001      | Invalid Request: Invalid required search field combination - premisesUnitNumber required.                                                                                         |
      | INVALID_PREMISES_CITY_LENGTH_TC_39                     | 10115     | Invalid search parameter(s)                                                                                                                                                       |
      | MISSING_PREMISES_CITY_TC_40                            | 1001      | Invalid Request: Invalid required search field combination - premisesCity required.                                                                                               |
      | INVALID_STATE_CODE_LENGTH_TC_41                        | 10115     | Invalid PremisesStateCode provided                                                                                                                                                |
      | MISSING_STATE_CODE_TC_42                               | 1001      | Invalid Request: Invalid required search field combination - premisesStateCode required.                                                                                          |
      | INVALID_ZIP_CODE_LENGTH_TC_43                          | 10115     | Invalid PremisesZipCode provided                                                                                                                                                  |
      | INVALID_ZIP_CODE_LENGTH_LESS_THAN_5_TC_44              | 10115     | Invalid PremisesZipCode provided                                                                                                                                                  |
      | MISSING_ZIP_CODE_TC_45                                 | 1001      | Invalid Request: Invalid required search field combination - premisesZipCode required.                                                                                            |

  @SearchAccountsServiceTransfer1 @Phase1 @HappyFlow
  Scenario Outline: SearchAccountsApiServiceTransfer Api - Verify the results for <testCondition>
    When a request is made to the SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And response should return numberOfMatches as <noOfRecords>

    Examples:
    |testCondition                                   |errorCode|errorMessage|noOfRecords|
#    |INVALID_LAST_NAME_ZIP_COMBINATION_TC_18         |0        |            |0          |
#    |INVALID_CUSTOMER_PREMISES_CODE_COMBINATION_TC_46|0        |            |0          |
#    |INVALID_RS_LAST_NAME_ZIP_COMBINATION_TC_47      |0        |            |0          |
#    |VALID_LAST_NAME_ZIP_COMBINATION_TC_48           |0        |            |30         |
#    |NO_MATCHING_CUSTOMER_BUSINESS_NAME_TC_49        |0        |            |0          |
    |VALID_CUSTOMER_BUSINESS_NAME_TC_50              |0        |            |1          |


  @SearchAccountsServiceTransfer2 @Phase1 @HappyFlow
  Scenario Outline: SearchAccountsApiServiceTransfer Api - Verify the no results are returned for <testCondition>
    When a request is made to the SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And response should return numberOfMatches as <noOfRecords>
    And response should have "accountStatus" as "<accountStatus>"
    And response should have "sonpAccount" flag as "<isSONPAccount>"
    And response should have "unappliedDepositAmount" as "<unappliedDeposit>"
    And response should have "pastDueAmount" as "<pastDueAmount>"
    And response should have "badDebtAmount" as "<badDebtAmount>"
    And response should have "aglcAccountNumber" as "<aglcAccountNumber>"

    Examples:
      |testCondition                                   |errorCode|errorMessage|noOfRecords|accountStatus|isSONPAccount|unappliedDeposit|pastDueAmount|badDebtAmount|aglcAccountNumber|
 # |CUST_PREM_CODE_INACTIVE_UNAPPLIED_DEPOSIT_TC_51 |0        |            |1          |I            |false        |25               |0|0|0000                                    |
#      |CUST_PREM_CODE_INACTIVE_PAST_DUE_TC_52 |0        |            |1          |I            |false        |0               |199.37                     |0|0000                                    |
#      |RS_INACTIVE_BANKRUPCY_TC_53 |0        |            |1          |I            |false        |0               |0                     |0|BANK                                    |
#      |RS_INACTIVE_BAD_DEBT_BALANCE_TC_54|0        |            |1          |I            |true        |0               |0                     |277.22             |0000                                    |
#      |RS_INACTIVE_BAD_DEBT_BALANCE_TC_55|0        |            |1          |I            |true        |0               |0                     |277.22             |0000                                    |
#    |CUST_PREM_CODE_NEW_UNAPPLIED_DEPOSIT_TC_57 |0        |            |1          |N            |false        |25               |0|0|0000                                         |
#     |RS_NEW_BANKRUPCY_TC_58 |0        |            |1          |N            |false        |0               |0                     |0|BANK                                    |
#    |CUST_PREM_CODE_FINAL_UNAPPLIED_DEPOSIT_TC_60 |0        |            |1          |F            |false        |250               |0|0|0000                                       |
#      |CUST_PREM_CODE_ACTIVE_PAST_DUE_TC_61 |0        |            |1          |A            |false        |0               |81.28                     |0|0000                       |
#   |CUST_PREM_CODE_ACTIVE_SONP_PAST_DUE_TC_68 |0        |            |1          |A            |true        |0               |336.98|0|0000                                       |
    |CUST_PREM_CODE_SONP_UNAPPLIEDDEPOSIT_TC_70 |0        |            |1          |A            |true        |25|359.35               |0|0000                                       |


  @SearchAccountsServiceTransfer3 @Phase1 @HappyFlow
  Scenario Outline: SearchAccountsApiServiceTransfer Api - Verify the no results are returned for <testCondition>
    When a request is made to the SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And response should return numberOfMatches as <noOfRecords>
    And response should have "accountStatus" as "<accountStatus>"
    And response should have "sonpAccount" flag as "<isSONPAccount>"
    And response should have "unappliedDepositAmount" as "<unappliedDeposit>"
    And response should have "pastDueAmount" as "<pastDueAmount>"
    And response should have "badDebtAmount" as "<badDebtAmount>"
    And response should have "aglcAccountNumber" as "<aglcAccountNumber>"
    And response should have "rewards" as "<rewards>"

    Examples:
      |testCondition                                   |errorCode|errorMessage|noOfRecords|accountStatus|isSONPAccount|unappliedDeposit|pastDueAmount|badDebtAmount|aglcAccountNumber|rewards|
    #|FINAL_NO_SONP_ACTIVE_PENDING_REWARDS_TC_59|0        |            |1          |F            |false        |0               |198.95                    |0|0000                      |true   |
|ACTIVE_SONP_WITH_REWARDS_TC_71            |0        |            |1          |A            |true         |250             |0                         |0|0000                      |true   |


  @SearchAccountsServiceTransfer4 @Phase1 @HappyFlow
  Scenario Outline: SearchAccountsApiServiceTransfer Api - Verify the no results are returned for <testCondition>
    When a request is made to the SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And response should return numberOfMatches as <noOfRecords>
    And response should have "accountStatus" as "<accountStatus>"
    And response should have "sonpAccount" flag as "<isSONPAccount>"
    And response should have "unappliedDepositAmount" as "<unappliedDeposit>"
    And response should have "pastDueAmount" as "<pastDueAmount>"
    And response should have "badDebtAmount" as "<badDebtAmount>"
    And response should have "aglcAccountNumber" as "<aglcAccountNumber>"
    And response should have "currentPricePlanCode" as "<currentPricePlanCode>"
    And response should have "currentPricePlanDescription" as "<currentPricePlanDescription>"

    Examples:
      |testCondition                                   |errorCode|errorMessage|noOfRecords|accountStatus|isSONPAccount|unappliedDeposit|pastDueAmount|badDebtAmount|aglcAccountNumber|currentPricePlanCode|currentPricePlanDescription|
      |CUST_PREM_CODE_ACTIVE_PAST_DUE_REWARDS_TC_63 |0        |            |1          |A            |false        |250              |0                     |0|0000               |CCV                   |CVS with Price Pro Guarantee|

  @SearchAccountsServiceTransfer5 @Phase1 @HappyFlow
  Scenario Outline: SearchAccountsApiServiceTransfer Api - Verify the no results are returned for <testCondition>
    When a request is made to the SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And response should return numberOfMatches as <noOfRecords>
    And response should have "accountStatus" as "<accountStatus>"
    And response should have "sonpAccount" flag as "<isSONPAccount>"
    And response should have "unappliedDepositAmount" as "<unappliedDeposit>"
    And response should have "badDebtAmount" as "<badDebtAmount>"
    And response should have "currentPricePlanCode" as "<currentPricePlanCode>"
    And response should have "currentPricePlanDescription" as "<currentPricePlanDescription>"
    And response should have "currentPricePlanDescription" as "<currentPricePlanDescription>"
    And response should have "activeDiscounts" as "<activeDiscounts>"


    Examples:
      |testCondition                                   |errorCode|errorMessage|noOfRecords|accountStatus|isSONPAccount|unappliedDeposit|badDebtAmount|currentPricePlanCode|currentPricePlanDescription|activeDiscounts|
    #|CUST_PREM_CODE_ACTIVE_UNAPPLIED_DEPOSIT_TC_64 |0        |            |1          |A            |false        |0               |0                                      |RGB                                        |Residential Guaranteed Bill|No Restrictions|
    #|DISCOUNTS_WITH_RESTRICTIONS_TC_65             |0        |            |1          |A            |false        |0               |0                                |MVS                                        |Variable Select|Restrictions|
      #|TRANSFERABLE_DISCOUNTS_TC_66             |0        |            |1          |A            |false        |0               |0                                |18M                                        |18-Month Fixed|This offer is Transferable except to the Guaranteed Bill Plan|
        #|MULTIPLE_DISCOUNTS_TC_67             |0        |            |1          |A            |false        |0               |0                                |MVS                                        |Variable Select|This offer is Transferable except to the Guaranteed Bill Plan|
    #|SONP_DISCOUNT_NO_RESTRICTION_TC_72 |0        |            |1          |A            |true        |25               |0                                      |RGB                                        |Residential Guaranteed Bill|No Restrictions|
    #|SONP_DISCOUNT_WITH_RESTRICTIONS_TC_73 |0        |            |1          |A            |true        |0               |0                                      |PRP                                        |Pre-Pay|Restrictions|
      #|TRANSFERABLE_DISCOUNTS_SONP_TC_74             |0        |            |1          |A            |true        |0               |0                                |MVS                                        |Variable Select|This offer is Transferable except to the Guaranteed Bill Plan|
        |MULTIPLE_DISCOUNTS_TC_SONP_TC_75             |0        |            |1          |A            |true        |0               |0                                |MVS                                        |Variable Select|This offer is Transferable except to the Guaranteed Bill Plan|


  @SearchAccountsServiceTransfer6 @Phase1 @HappyFlow
  Scenario Outline: SearchAccountsApiServiceTransfer Api - Verify the no results are returned for <testCondition>
    When a request is made to the SearchAccounts Api for "<testCondition>"
    Then verify response code of "SearchAccounts" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And response should return numberOfMatches as <noOfRecords>
    And response should have "accountStatus" as "<accountStatus>"
    And response should have "sonpAccount" flag as "<isSONPAccount>"
    And response should have "unappliedDepositAmount" as "<unappliedDeposit>"
    And response should have "pastDueAmount" as "<pastDueAmount>"
    And response should have "badDebtAmount" as "<badDebtAmount>"
    And response should have "aglcAccountNumber" as "<aglcAccountNumber>"
    And response should have "greenerLife" as "<greenerLife>"

 Examples:
      |testCondition                                   |errorCode|errorMessage|noOfRecords|accountStatus|isSONPAccount|unappliedDeposit|pastDueAmount|badDebtAmount|aglcAccountNumber|greenerLife|
# |CUST_PREM_CODE_ACTIVE_GREENER_LIFE_SONP_TC_69 |0        |            |1          |A            |true        |0               |48.97|0|0000                                    |true       |
      |CUST_PREM_CODE_ACTIVE_GREENER_LIFE_NO_SONP_TC_62 |0        |            |1          |A            |false        |0               |0|0|0000                                    |true       |
