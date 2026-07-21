Feature: Verify VerifyAccount Api

  @VerifyAccount @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to VerifyAccount Api for "<testCondition>"
    Then verify response code of "VerifyAccount" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have success as "true"
    And the response should have data as "notNull"
    And validate VerifyAccount response for "<testCondition>"

    Examples:
      | testCondition                                                    | errorCode | errorMessage | success | data    |
      | TC_206__Positive__Billing_Street_Number_Value_                   | 0         |              | true    | notNull |
      | TC_207__Positive__Billing_Street_Number_Format_                  | 0         |              | true    | notNull |
      | TC_208__Positive__Billing_PreDir_Value_                          | 0         |              | true    | notNull |
      | TC_209__Positive__Billing_PreDir_Format_                         | 0         |              | true    | notNull |
      | TC_210__Positive__Billing_Street_Name_Value_                     | 0         |              | true    | notNull |
      | TC_211__Positive__Billing_Street_Name_Format_                    | 0         |              | true    | notNull |
      | TC_212__Positive__Billing_City_Value_                            | 0         |              | true    | notNull |
      | TC_213__Positive__Billing_City_Format_                           | 0         |              | true    | notNull |
      | TC_214__Positive__Billing_State_Value_                           | 0         |              | true    | notNull |
      | TC_215__Positive__Billing_State_Format_                          | 0         |              | true    | notNull |
      | TC_216__Positive__Billing_ZIP_Value_                             | 0         |              | true    | notNull |
      | TC_217__Positive__Billing_ZIP_Format_                            | 0         |              | true    | notNull |
      | TC_218__Positive__Billing_PO_Box_Value_                          | 0         |              | true    | notNull |
      | TC_219__Positive__Billing_PO_Box_Format_                         | 0         |              | true    | notNull |
      | TC_220__Positive__Bill_Delivery_Confirmation_Date_Value_         | 0         |              | true    | notNull |
      | TC_221__Positive__Bill_Delivery_Confirmation_Date_Format_        | 0         |              | true    | notNull |
      | TC_222__Positive__Corr_Delivery_Confirmation_Date_Value_         | 0         |              | true    | notNull |
      | TC_223__Positive__Corr_Delivery_Confirmation_Date_Format_        | 0         |              | true    | notNull |
      | TC_224__Positive__Bill_Delivery_Preference_Confirmed_            | 0         |              | true    | notNull |
      | TC_225__Positive__Correspondence_Delivery_Preference_Confirmed_  | 0         |              | true    | notNull |
      | TC_226__Positive__Bill_Delivery_Preference_Initiated_            | 0         |              | true    | notNull |
      | TC_227__Positive__Correspondence_Delivery_Preference_Initiated_  | 0         |              | true    | notNull |
      | TC_228__Positive__Bill_Delivery_Preference_Paper_                | 0         |              | true    | notNull |
      | TC_229__Positive__Correspondence_Delivery_Preference_Paper_      | 0         |              | true    | notNull |
      | TC_230__Positive__Bill_Delivery_Preference_Expired_              | 0         |              | true    | notNull |
      | TC_231__Positive__Correspondence_Delivery_Preference_Expired_    | 0         |              | true    | notNull |
