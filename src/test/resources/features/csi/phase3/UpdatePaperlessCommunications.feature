Feature: Verify UpdatePaperlessCommunications Api

  @UpdatePaperlessCommunications @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdatePaperlessCommunications Api for "<testCondition>"
    Then verify response code of "UpdatePaperlessCommunications" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                                    | errorCode | errorMessage                                                      | success | data |
#      | TC_53__Negative__Missing_Request_ID_                                            | 10001     | Missing Request ID                              | false   | null |
#      | TC_54__Negative__Invalid_Request_ID_Length_                                     | 10002     | Invalid Request ID                              | false   | null |
#      | TC_55__Negative__Duplicate_Request_ID_                                          | 10003     | Duplicate Request ID                            | false   | null |
#      | TC_56__Negative__Missing_customerCode_                                          | 10011     | Missing Customer Code                           | false   | null |
#      | TC_57__Negative__Invalid_customerCode_Length_                                   | 10015     | Invalid Customer Code Format                    | false   | null |
#      | TC_58__Negative__Invalid_customerCode_Format_                                   | 10015     | Invalid Customer Code Format                    | false   | null |
#      | TC_59__Negative__Missing_premisesCode_                                          | 10013     | Missing Premises Code                           | false   | null |
#      | TC_60__Negative__Invalid_premisesCode_Length_                                   | 10005     | Invalid Premises Code Format                    | false   | null |
#      | TC_61__Negative__Invalid_premisesCode_Format_                                   | 10005     | Invalid Premises Code Format                    | false   | null |
#      | TC_62__Negative__Invalid_Account_Number__Invalid_Account_                       | 40015     | Invalid Account Number                          | false   | null |
#      | TC_63__Negative__Missing_Both_Preference_Values_                                | 10427     | Paperless Preference Flag Required              | false   | null |
#      | TC_64__Negative__Invalid_Bill_Preference_Value_                                 | 10425     | Invalid Bill Preference Value                   | false   | null |
#      | TC_65__Negative__Invalid_Correspondence_Preference_Value_                       | 10419     | Invalid Correspondence Preference Value         | false   | null |
#      | TC_66__Negative__Mixed_Enrollment_and_Unenrollment_                             | 40283     | Mixed Enrollment and Unenrollment Not Permitted | false   | null |
#      | TC_67__Negative__Missing_Email_for_Enrollment_                                  | 10053     | Missing Email Address                           | false   | null |
#      | TC_68__Negative__Invalid_Email_Format_                                          | 10065     | Invalid Email Address Format                    | false   | null |
#      | TC_69__Negative__Bill_Enrollment_Ineligible__Not_Fiserv_                        | 40287     |                                                 | false   | null |
#      | TC_70__Negative__Bill_Enrollment_Ineligible__Fiserv_                            | 40287     |                                                 | false   | null |
#      | TC_71__Negative__Correspondence_Enrollment_Ineligible_                          | 40289     |                                                 | false   | null |
#      | TC_72__Negative__Both_Channels_Enrollment_Ineligible_                           | 40291     |                                                 | false   | null |
#      | TC_73__Negative__Atomic_Eligibility_Failure_                                    | 40291     | Email Address is Not Present;Email Address is Not Present         | false   | null |
#      | TC_74__Negative__Unenrollment_Not_Applicable__Not_Enrolled_                     | 40285     | Unenrollment Not Applicable                     | false   | null |
#      | TC_75__Negative__Fiserv_Bill_Unenrollment_Not_Allowed_                          | 40285     | Unenrollment Not Applicable                     | false   | null |
#      | TC_76__Negative__Cannot_Unenroll_Initiated_State_                               | 40285     | Unenrollment Not Applicable                     | false   | null |
#      | TC_77__Negative__Enrollment_Email_Failure_Rollback__ACTIVE_                     | 40281     | Confirmation Email Delivery Failed              | false   | null |
#      | TC_78__Negative__Enrollment_Email_Failure_Rollback__NEW_                        | 40281     | Confirmation Email Delivery Failed              | false   | null |
#      | TC_79__Negative__Email_Ignored_for_P_Requests__Not_Enrolled_                    | 40285     | Unenrollment Not Applicable                     | false   | null |

  # Single-request scenarios: each row fetches fresh DB data (no pending token / correct enrolled state).
  @UpdatePaperlessCommunicationsPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to UpdatePaperlessCommunications Api for "<testCondition>"
    Then verify response code of "UpdatePaperlessCommunications" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                                                                                          | errorCode | errorMessage | success | data    |
#      | TC_80__Positive__UpdatePaperlessCommunications_accountType_Format__ACTIVE_                                            | 0         |              | true    | notNull |
#      | TC_81__Positive__UpdatePaperlessCommunications_accountType_Format__NEW_                                               | 0         |              | true    | notNull |
#      | TC_82__Positive__billDeliveryOptionStatus_Format__INITIATED_                                                          | 0         |              | true    | notNull |
#      | TC_83__Positive__billDeliveryOptionStatus_Format__UPDATED_                                                            | 0         |              | true    | notNull |
#      | TC_84__Positive__billDeliveryOptionStatus_Format__NO_CHANGE_                                                          | 0         |              | true    | notNull |
#      | TC_85__Positive__corrDeliveryOptionStatus_Format__INITIATED_                                                          | 0         |              | true    | notNull |
#      | TC_86__Positive__corrDeliveryOptionStatus_Format__UPDATED_                                                            | 0         |              | true    | notNull |
#      | TC_87__Positive__corrDeliveryOptionStatus_Format__NO_CHANGE_                                                          | 0         |              | true    | notNull |
#      | TC_88__Positive__linkExpiryDateTime_Format_                                                                           | 0         |              | true    | notNull |
#      | TC_89__Positive__linkExpiryDateTime_Null_When_No_Enrollment_Processed_                                                | 0         |              | true    | notNull |
#      | TC_90__Positive__linkCreated_Format__true_                                                                            | 0         |              | true    | notNull |
#      | TC_91__Positive__linkCreated_Format__false_                                                                           | 0         |              | true    | notNull |
#      | TC_92__Positive__linkCreated_Null_When_No_Enrollment_Processed_                                                       | 0         |              | true    | notNull |
#      | TC_93__Positive__emailUpdated_Format__false_                                                                          | 0         |              | true    | notNull |
#      | TC_94__Positive__emailUpdated_Format__true_                                                                           | 0         |              | true    | notNull |
#      | TC_95__Positive__Email_Ignored_for_P_Requests__Enrolled_                                                              | 0         |              | true    | notNull |
#      | TC_96__Positive__Active_Account_Bill_Enrollment_Initiated_                                                            | 0         |              | true    | notNull |
#      | TC_97__Positive__New_Account_Bill_Enrollment_Initiated_                                                               | 0         |              | true    | notNull |
#      | TC_98__Positive__Active_Account_Both_Channels_Enrollment_Initiated_                                                   | 0         |              | true    | notNull |
#      | TC_99__Positive__New_Account_Both_Channels_Enrollment_Initiated_                                                      | 0         |              | true    | notNull |
#      | TC_100__Positive__Active_Account_Bill_Unenrollment_                                                                    | 0         |              | true    | notNull |
#      | TC_101__Positive__New_Account_Bill_Unenrollment_                                                                       | 0         |              | true    | notNull |
      | TC_102__Positive__Active_Account_Both_Channels_Unenrollment_                                                           | 0         |              | true    | notNull |
      | TC_103__Positive__New_Account_Both_Channels_Unenrollment_                                                             | 0         |              | true    | notNull |
#      | TC_104__Positive__Active_Account_Reuse_Existing_Valid_Token_                                                          | 0         |              | true    | notNull |
#      | TC_105__Positive__New_Account_Reuse_Existing_Valid_Token_                                                             | 0         |              | true    | notNull |
#      | TC_106__Positive__Active_Account_New_Token_When_Existing_Token_Invalid_                                               | 0         |              | true    | notNull |
#      | TC_107__Positive__New_Account_New_Token_When_Existing_Token_Invalid_                                                  | 0         |              | true    | notNull |
#      | TC_108__Positive__Email_Updated_During_Enrollment_                                                                    | 0         |              | true    | notNull |
#      | TC_109__Positive__New_Token_Required_When_Email_Changes_                                                              | 0         |              | true    | notNull |
#      | TC_110__Positive__Cross_Channel_Aggregation_                                                                          | 0         |              | true    | notNull |
#      | TC_111__Positive__Same_Channel_Update__Last_Value_Wins_                                                               | 0         |              | true    | notNull |
#      | TC_112__Positive__PPER_Cleanup_on_Active_Unenrollment_                                                                | 0         |              | true    | notNull |
#      | TC_113__Positive__Token_Expired_on_New_Account_Unenrollment_                                                          | 0         |              | true    | notNull |
#      | TC_114__Positive__Latest_Aggregated_State_Wins_                                                                       | 0         |              | true    | notNull |
#      | TC_115__Positive__Enrollment_Finalized_on_Confirmation_                                                               | 0         |              | true    | notNull |
#      | TC_116__Positive__Prior_Link_Invalid_After_Email_Change_                                                              | 0         |              | true    | notNull |
#      | TC_117__Positive__Passive_Invalidation_After_External_Email_Change_                                                   | 0         |              | true    | notNull |
