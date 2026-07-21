Feature: Verify GetAccountInfo Api

#  @GetAccountInfo @NegativeFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#
#    Examples:
#      | testCondition                                           | errorCode | errorMessage                  |
#      | TC_142__Negative__Missing_Request_ID                    | 10001     | Missing Request ID            |
#      | TC_143__Negative__Invalid_Request_ID__Length            | 10002     | Invalid Request ID            |
#      | TC_144__Negative__Duplicate_Request_ID                  | 10003     | Duplicate Request ID          |
#      | TC_145__Negative__Invalid_customerCode_Length           | 10015     | Invalid Customer Code Format  |
#      | TC_146__Negative__Invalid_customerCode_Format           | 10015     | Invalid Customer Code Format  |
#      | TC_147__Negative__Invalid_premisesCode_Length           | 10005     | Invalid Premises Code Format  |
#      | TC_148__Negative__Invalid_premisesCode_Format           | 10005     | Invalid Premises Code Format  |
#      | TC_149__Negative__Invalid_Account_Number                | 40015     | Invalid Account Number        |
#
#
#  @GetAccountInfoPositive @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should have email existence as "<emailShouldExist>"
#
#
#    Examples:
#      | testCondition                                                  | errorCode | errorMessage | success | emailShouldExist |
#      | TC_150__Positive__Account_Info_Returned___Email_Address____    | 0         |              | true    | true             |
#      | TC_151__Positive__Account_Info_Returned___No_Email_Address____ | 0         |              | true    | false            |
#
#
#  @GetAccountInfoPositive @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should have partner promotions indicator as "<partnerIndicator>"
#
#    Examples:
#      | testCondition                                                              | errorCode | errorMessage | success | partnerIndicator |
#      | TC_152__Positive__Account_Info_Returned___Partner_Promotions_Indicator__equals_Y____ | 0 | | true | Y |
#      | TC_153__Positive__Account_Info_Returned___Partner_Promotions_Indicator__equals_N____ | 0 | | true | N |
#      | TC_154__Positive__Account_Info_Returned___Partner_Promotions_Indicator_is_null____   | 0 | | true | N |
#
#  @GetAccountInfoPositive @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should have marketing offers indicator as "<marketingIndicator>"
#
#    Examples:
#      | testCondition                                                              | errorCode | errorMessage | success | marketingIndicator |
#      | TC_155__Positive__Account_Info_Returned___Marketing_Offers_Indicator__equals_Y____ | 0 | | true | Y |
#      | TC_156__Positive__Account_Info_Returned___Marketing_Offers_Indicator__equals_N____ | 0 | | true | N |
#      | TC_157__Positive__Account_Info_Returned___Marketing_Offers_Indicator_is_null____   | 0 | | true | N |
#
#  @GetAccountInfoPositive12 @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should have account billing reminder as "<billingReminder>"
#
#    Examples:
#      | testCondition                                                              | errorCode | errorMessage | success | billingReminder |
#      | TC_158__Positive__Account_Info_Returned___Account__and_Billing_Reminder__equals_Y____ | 0 | | true | Y |
#      | TC_159__Positive__Account_Info_Returned___Account__and_Billing_Reminder__equals_N____ | 0 | | true | N |
#      | TC_160__Positive__Account_Info_Returned___Account__and_Billing_Reminder_is_null____   | 0 | | true | N |
#
#
#  @GetAccountInfoPositive @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should have phone number existence as "<phoneShouldExist>"
#
#    Examples:
#      | testCondition                                                   | errorCode | errorMessage | success | phoneShouldExist |
#      | TC_161__Positive__Account_Info_Returned___Primary_Phone_is_Home____ | 0         |              | true    | true             |
#      | TC_162__Positive__Account_Info_Returned___Primary_Phone_is_Work____ | 0         |              | true    | true             |
#      | TC_163__Positive__Account_Info_Returned___Primary_Phone_is_both_Home_and_Work____ | 0         |              | true    | true             |
#      | TC_164__Positive__Account_Info_Returned___No_Phone_Number____       | 0         |              | true    | false            |
#
#
#  @GetAccountInfoPositive @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should validate greener life rate as "<rateShouldExist>"
#    Examples:
#      | testCondition                                                   | errorCode | errorMessage | success | rateShouldExist |
#      | TC_165__Positive__Account_Info_Returned___Greener_Life_Rate____    | 0         |              | true    | true            |
#      | TC_166__Positive__Account_Info_Returned___No_Greener_Life_Rate____ | 0         |              | true    | false           |
#
#  @GetAccountInfoPositive @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should have bill delivery option as "<billOption>"
#
#    Examples:
#      | testCondition                                                              | errorCode | errorMessage | success | billOption |
#      | TC_167__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Paper____    | 0 | | true | P |
#      | TC_168__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Electronic____ | 0 | | true | E |
#      | TC_169__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Fiserv_E__Bill____ | 0 | | true | F |
#      | TC_170__Positive__Account_Info_Returned___Bill_Delivery_Option_is_null____     | 0 | | true | P |
#
#
#  @GetAccountInfoPositive @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should have correspondence delivery option as "<corrOption>"
#
#    Examples:
#      | testCondition                                                              | errorCode | errorMessage | success | corrOption |
#      | TC_171__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_Paper____    | 0 | | true | P |
#      | TC_172__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_Electronic____ | 0 | | true | E |
#      | TC_173__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_null____     | 0 | | true | P |
#
#  @GetAccountInfoPositive @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should have price plan count as "<planCountType>"
#
#    Examples:
#      | testCondition                                                   | errorCode | errorMessage | success | planCountType |
#      | TC_174__Positive__Account_Info_Returned___Single_Price_Plan____    | 0         |              | true    | single        |
#      | TC_175__Positive__Account_Info_Returned___Multiple_Price_Plans____ | 0         |              | true    | multiple      |
#
#  @GetAccountInfoPositive @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should validate guaranteed bill plan for "<testCondition>"
#
#    Examples:
#      | testCondition                                                                                     | errorCode | errorMessage | success |
#      | TC_176__Positive__Account_Info_Returned___No_Guaranteed_Bill_Plan____                              | 0         |              | true    |
#      | TC_177__Positive__Account_Info_Returned___Guaranteed_Bill_Plan____                                 | 0         |              | true    |
#
#
#  @GetAccountInfoPositive @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should validate price protection guarantee plan for "<testCondition>"
#
#    Examples:
#      | testCondition                                                                                     | errorCode | errorMessage | success |
#      | TC_178__Positive__Account_Info_Returned___No_Price_Protection_Guarantee_Plan____                   | 0         |              | true    |
#      | TC_179__Positive__Account_Info_Returned___Price_Protection_Guarantee_Plan____                      | 0         |              | true    |
#
#  @GetAccountInfo @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should validate rollover plan indicator for "<testCondition>"
#
#    Examples:
#      | testCondition                                                                                     | errorCode | errorMessage | success |
#      | TC_180__Positive__Account_Info_Returned___No_Rollover___ACR______Plans____                             | 0         |              | true    |
#      | TC_181__Positive__Account_Info_Returned___Rollover___ACR______Plans____                                | 0         |              | true    |
#
#  @GetAccountInfoPositive @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should validate restricted plan indicator for "<testCondition>"
#
#    Examples:
#      | testCondition                                                                                     | errorCode | errorMessage | success |
#      | TC_182__Positive__Account_Info_Returned___Non__Restricted_Plans____                                | 0         |              | true    |
#
#  @GetAccountInfoPositive @HappyFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#    And the response should have success as "<success>"
#    And response should validate discounts for "<testCondition>"
#
#    Examples:
#      | testCondition                                                                                     | errorCode | errorMessage | success |
#      | TC_183__Positive__Account_Info_Returned___Single_Discount____                                      | 0         |              | true    |
#      | TC_184__Positive__Account_Info_Returned___Multiple_Discounts____                                   | 0         |              | true    |
#      | TC_185__Positive__Account_Info_Returned___No_Discounts____                                         | 0         |              | true    |
#      | TC_186__Positive__Account_Info_Returned___Transferable_Discount____                               | 0         |              | true    |
#      | TC_187__Positive__Account_Info_Returned___Non__Transferable_Discount____                          | 0         |              | true    |

  @GetAccountInfoPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetAccountInfo Api for "<testCondition>"
    Then verify response code of "GetAccountInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have success as "<success>"
    And validate GetAccountInfo price plan response for "<testCondition>"

    Examples:
      | testCondition                                                                                     | errorCode | errorMessage | success |
      | TC_203__Positive__Account_Info_Returned___Renewal_Indicator_Format____                              | 0         |              | true    |
      | TC_204__Positive__Account_Info_Returned___Renewal_Indicator_equals_Y____                            | 0         |              | true    |
      | TC_205__Positive__Account_Info_Returned___Renewal_Indicator_equals_N____                            | 0         |              | true    |
      | TC_206__Positive__Account_Info_Returned___Renewal_Indicator_equals_Dash____                         | 0         |              | true    |
      | TC_207__Positive__Account_Info_Returned___Guaranteed_Plan_Pricing_Model____                         | 0         |              | true    |
      | TC_208__Positive__Account_Info_Returned___Non__Guaranteed_Plan_Pricing_Model____                  | 0         |              | true    |
      | TC_209__Positive__Account_Info_Returned___Plan_Description_Format____                               | 0         |              | true    |
      | TC_210__Positive__Account_Info_Returned___Plan_Description_Value____                                | 0         |              | true    |
