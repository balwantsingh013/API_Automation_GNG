Feature: Verify GetAccountInfo Api

#  @GetAccountInfo @NegativeFlow @CSI
#  Scenario Outline: "<testCondition>"
#    When a request is made to GetAccountInfo Api for "<testCondition>"
#    Then verify response code of "GetAccountInfo" Api is 200
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#
#    Examples:
#      | testCondition                                           | errorCode | errorMessage                  |
#      | TC_141__Negative__Missing_Request_ID                    | 10001     | Missing Request ID            |
#      | TC_142__Negative__Invalid_Request_ID__Length            | 10002     | Invalid Request ID            |
#      | TC_143__Negative__Duplicate_Request_ID                  | 10003     | Duplicate Request ID          |
#      | TC_144__Negative__Invalid_customerCode_Length           | 10015     | Invalid Customer Code Format  |
#      | TC_145__Negative__Invalid_customerCode_Format           | 10015     | Invalid Customer Code Format  |
#      | TC_146__Negative__Invalid_premisesCode_Length           | 10005     | Invalid Premises Code Format  |
#      | TC_147__Negative__Invalid_premisesCode_Format           | 10005     | Invalid Premises Code Format  |
#      | TC_148__Negative__Invalid_Account_Number                  | 40015     | Invalid Account Number        |

  @GetAccountInfoPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetAccountInfo Api for "<testCondition>"
    Then verify response code of "GetAccountInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have success as "<success>"

    Examples:
      | testCondition                                                                                     | errorCode | errorMessage | success |
#      | TC_149__Positive__Account_Info_Returned___Email_Address____                                        | 0         |              | true    |
      | TC_150__Positive__Account_Info_Returned___No_Email_Address____                                     | 0         |              | true    |
#      | TC_151__Positive__Account_Info_Returned___Partner_Promotions_Indicator__equals_Y____               | 0         |              | true    |
#      | TC_152__Positive__Account_Info_Returned___Partner_Promotions_Indicator__equals_N____               | 0         |              | true    |
      | TC_153__Positive__Account_Info_Returned___Partner_Promotions_Indicator_is_null____                 | 0         |              | true    |
#      | TC_154__Positive__Account_Info_Returned___Marketing_Offers_Indicator__equals_Y____                 | 0         |              | true    |
#      | TC_155__Positive__Account_Info_Returned___Marketing_Offers_Indicator__equals_N____                 | 0         |              | true    |
      | TC_156__Positive__Account_Info_Returned___Marketing_Offers_Indicator_is_null____                   | 0         |              | true    |
#      | TC_157__Positive__Account_Info_Returned___Account__and_Billing_Reminder__equals_Y____                | 0         |              | true    |
#      | TC_158__Positive__Account_Info_Returned___Account__and_Billing_Reminder__equals_N____                | 0         |              | true    |
      | TC_159__Positive__Account_Info_Returned___Account__and_Billing_Reminder_is_null____                  | 0         |              | true    |
#      | TC_160__Positive__Account_Info_Returned___Primary_Phone_is_Home____                                | 0         |              | true    |
#      | TC_161__Positive__Account_Info_Returned___Primary_Phone_is_Work____                                | 0         |              | true    |
#      | TC_162__Positive__Account_Info_Returned___Primary_Phone_is_both_Home_and_Work____                  | 0         |              | true    |
#      | TC_163__Positive__Account_Info_Returned___No_Phone_Number____                                      | 0         |              | true    |
#      | TC_164__Positive__Account_Info_Returned___Greener_Life_Rate____                                    | 0         |              | true    |
#      | TC_165__Positive__Account_Info_Returned___No_Greener_Life_Rate____                                 | 0         |              | true    |
#      | TC_166__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Paper____                        | 0         |              | true    |
#      | TC_167__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Electronic____                   | 0         |              | true    |
#      | TC_168__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Fiserv_E__Bill____               | 0         |              | true    |
#      | TC_169__Positive__Account_Info_Returned___Bill_Delivery_Option_is_null____                         | 0         |              | true    |
#      | TC_170__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_Paper____              | 0         |              | true    |
#      | TC_171__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_Electronic____         | 0         |              | true    |
#      | TC_172__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_null____               | 0         |              | true    |
#      | TC_173__Positive__Account_Info_Returned___Single_Price_Plan____                                    | 0         |              | true    |
#      | TC_174__Positive__Account_Info_Returned___Multiple_Price_Plans____                                 | 0         |              | true    |
#      | TC_175__Positive__Account_Info_Returned___No_Guaranteed_Bill_Plan____                              | 0         |              | true    |
#      | TC_176__Positive__Account_Info_Returned___Guaranteed_Bill_Plan____                                 | 0         |              | true    |
#      | TC_177__Positive__Account_Info_Returned___No_Price_Protection_Guarantee_Plan____                   | 0         |              | true    |
#      | TC_178__Positive__Account_Info_Returned___Price_Protection_Guarantee_Plan____                      | 0         |              | true    |
#      | TC_179__Positive__Account_Info_Returned___No_Rollover___ACR__Plans____                             | 0         |              | true    |
#      | TC_180__Positive__Account_Info_Returned___Rollover___ACR__Plans____                                | 0         |              | true    |
#      | TC_181__Positive__Account_Info_Returned___Non__Restricted_Plans____                                | 0         |              | true    |
#      | TC_182__Positive__Account_Info_Returned___Restricted_Plans____                                     | 0         |              | true    |
#      | TC_183__Positive__Account_Info_Returned___Single_Discount____                                      | 0         |              | true    |
#      | TC_184__Positive__Account_Info_Returned___Multiple_Discounts____                                   | 0         |              | true    |
#      | TC_185__Positive__Account_Info_Returned___No_Discounts____                                         | 0         |              | true    |
#      | TC_186__Positive__Account_Info_Returned___Transferrable_Discount____                               | 0         |              | true    |
#      | TC_187__Positive__Account_Info_Returned___Non__Transferrable_Discount____                          | 0         |              | true    |
