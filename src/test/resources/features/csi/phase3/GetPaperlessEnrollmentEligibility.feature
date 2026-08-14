Feature: Verify GetPaperlessEnrollmentEligibility Api

  @GetPaperlessEnrollmentEligibility @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetPaperlessEnrollmentEligibility Api for "<testCondition>"
    Then verify response code of "GetPaperlessEnrollmentEligibility" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                         | errorCode | errorMessage                  | success | data |
      | TC_154__Negative__Missing_Request_ID_                                 | 10001     | Missing Request ID            | false   | null |
      | TC_155__Negative__Invalid_Request_ID_Length_                          | 10002     | Invalid Request ID            | false   | null |
      | TC_156__Negative__Duplicate_Request_ID_                               | 10003     | Duplicate Request ID          | false   | null |
      | TC_157__Negative__Missing_customerCode_                               | 10011     | Missing Customer Code         | false   | null |
      | TC_158__Negative__Invalid_customerCode_Length_                        | 10015     | Invalid Customer Code Format  | false   | null |
      | TC_159__Negative__Invalid_customerCode_Format_                        | 10015     | Invalid Customer Code Format  | false   | null |
      | TC_160__Negative__Missing_premisesCode_                               | 10013     | Missing Premises Code         | false   | null |
      | TC_161__Negative__Invalid_premisesCode_Length_                        | 10005     | Invalid Premises Code Format  | false   | null |
      | TC_162__Negative__Invalid_premisesCode_Format_                        | 10005     | Invalid Premises Code Format  | false   | null |
      | TC_163__Negative__Invalid_Account_Number__Invalid_Account_            | 40015     | Invalid Account Number        | false   | null |

  @GetPaperlessEnrollmentEligibilityPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetPaperlessEnrollmentEligibility Api for "<testCondition>"
    Then verify response code of "GetPaperlessEnrollmentEligibility" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have success as "true"
    And the response should have data as "notNull"
    And validate GetPaperlessEnrollmentEligibility response for "<testCondition>"

    Examples:
      | testCondition                                      | errorCode | errorMessage | success | data    |
      | TC_164__Positive__Corr_Delivery_Option_Format_     | 0         |              | true    | notNull |
      | TC_165__Positive__Bill_Delivery_Option_Format_     | 0         |              | true    | notNull |
      | TC_166__Positive__Corr_Eligible_Format_            | 0         |              | true    | notNull |
      | TC_167__Positive__Bill_Eligible_Format_            | 0         |              | true    | notNull |
      | TC_168__Positive__Corr_Reason_Code_Format_         | 0         |              | true    | notNull |
      | TC_169__Positive__Bill_Reason_Code_Format_         | 0         |              | true    | notNull |
      | TC_170__Positive__Corr_Reason_Desc_Format_         | 0         |              | true    | notNull |
      | TC_171__Positive__Bill_Reason_Desc_Format_         | 0         |              | true    | notNull |
      | TC_172__Positive__Correspondence_NULL_Defaults_to_P_ | 0       |              | true    | notNull |
      | TC_173__Positive__Correspondence_P_Mapping_        | 0         |              | true    | notNull |
      | TC_174__Positive__Correspondence_E_Mapping_        | 0         |              | true    | notNull |
      | TC_175__Positive__Bill_NULL_Defaults_to_P_         | 0         |              | true    | notNull |
      | TC_176__Positive__Bill_P_Mapping_                  | 0         |              | true    | notNull |
      | TC_177__Positive__Bill_E_Mapping_                  | 0         |              | true    | notNull |
      | TC_178__Positive__Bill_F_Mapping_                  | 0         |              | true    | notNull |
      | TC_179__Positive__Corr_Ineligible_No_Email_        | 0         |              | true    | notNull |
      | TC_180__Positive__Bill_Ineligible_No_Email_        | 0         |              | true    | notNull |
      | TC_181__Positive__Corr_Reason_Code_1_              | 0         |              | true    | notNull |
      | TC_181A__Positive__Corr_Reason_Desc_1_             | 0         |              | true    | notNull |
      | TC_182__Positive__Bill_Reason_Code_1_              | 0         |              | true    | notNull |
      | TC_182A__Positive__Bill_Reason_Desc_1_             | 0         |              | true    | notNull |
      | TC_183__Positive__Corr_Eligible_                   | 0         |              | true    | notNull |
      | TC_184__Positive__Corr_Ineligible_Enrolled_        | 0         |              | true    | notNull |
      | TC_185__Positive__Bill_Eligible_                   | 0         |              | true    | notNull |
      | TC_186__Positive__Bill_Ineligible_Enrolled_        | 0         |              | true    | notNull |
      | TC_187__Positive__Bill_Ineligible_Fiserv_          | 0         |              | true    | notNull |
      | TC_188__Positive__Corr_Reason_Code_2_              | 0         |              | true    | notNull |
      | TC_188A__Positive__Corr_Reason_Desc_2_             | 0         |              | true    | notNull |
      | TC_189__Positive__Bill_Reason_Code_2_              | 0         |              | true    | notNull |
      | TC_189A__Positive__Bill_Reason_Desc_2_             | 0         |              | true    | notNull |
      | TC_190__Positive__Bill_Reason_Code_3_              | 0         |              | true    | notNull |
      | TC_190A__Positive__Bill_Reason_Desc_3_             | 0         |              | true    | notNull |
      | TC_191__Positive__Corr_Reason_Description_         | 0         |              | true    | notNull |
      | TC_192__Positive__Bill_Reason_Description_         | 0         |              | true    | notNull |
      | TC_193__Positive__Corr_Reason_Description_NULL_    | 0         |              | true    | notNull |
      | TC_194__Positive__Bill_Reason_Description_NULL_    | 0         |              | true    | notNull |
      | TC_195__Positive__Corr_PPER_I_                     | 0         |              | true    | notNull |
      | TC_196__Positive__Corr_PPER_Eligible_              | 0         |              | true    | notNull |
      | TC_197__Positive__Corr_PPER_Reason_Null_           | 0         |              | true    | notNull |
      | TC_197A__Positive__Corr_PPER_Reason_Desc_Null_     | 0         |              | true    | notNull |
      | TC_198__Positive__Bill_PPER_I_                     | 0         |              | true    | notNull |
      | TC_199__Positive__Bill_PPER_Eligible_              | 0         |              | true    | notNull |
      | TC_200__Positive__Bill_PPER_Reason_Null_           | 0         |              | true    | notNull |
      | TC_200A__Positive__Bill_PPER_Reason_Desc_Null_     | 0         |              | true    | notNull |
      | TC_201__Positive__Corr_Override_Precedence_        | 0         |              | true    | notNull |
      | TC_201A__Positive__Corr_Override_Eligible_         | 0         |              | true    | notNull |
      | TC_202__Positive__Bill_Override_Precedence_        | 0         |              | true    | notNull |
      | TC_202A__Positive__Bill_Override_Eligible_         | 0         |              | true    | notNull |
      | TC_203__Positive__New_Unconfirmed_Bill_            | 0         |              | true    | notNull |
      | TC_204__Positive__New_Confirmed_Bill_              | 0         |              | true    | notNull |
      | TC_205__Positive__New_Paper_Bill_                  | 0         |              | true    | notNull |
