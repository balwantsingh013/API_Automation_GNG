Feature: Verify ConfirmPaperlessEnrollment Api
  @ConfirmPaperlessEnrollment @NegativeFlow @CSI

  Scenario Outline: "<testCondition>"

    When a request is made to ConfirmPaperlessEnrollment Api for "<testCondition>"

    Then verify response code of "ConfirmPaperlessEnrollment" Api is 200

    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:

      | testCondition                                                              | errorCode | errorMessage                          | success | data |

#      | TC_120__Negative__Missing_Request_ID_                                      | 10001     | Missing Request ID                    | false   | null |
#
#      | TC_121__Negative__Invalid_Request_ID_Length_                               | 10002     | Invalid Request ID                    | false   | null |
#
#      | TC_122__Negative__Duplicate_Request_ID_                                    | 10003     | Duplicate Request ID                  | false   | null |
#
#      | TC_123__Negative__Missing_Token_                                           | 10407     | Missing Token                         | false   | null |
#
#      | TC_124__Negative__Token_Validation_Failed_                                 | 10409     | Token Validation Failed               | false   | null |
#
#      | TC_125__Negative__Token_Not_Found__No_Stored_Token_Record_                 | 10411     | Token Not Found                       | false   | null |
#
#      | TC_126__Negative__Token_Expired__Stored_Expiry_Passed_                     | 10413     | Token Expired                         | false   | null |
#
#      | TC_127__Negative__Token_Expired__Passive_Email_Change_Invalidation_        | 10413     | Token Expired                         | false   | null |
#
#      | TC_128__Negative__Token_Already_Used_                                      | 10415     | Token Already Used                    | false   | null |
#
#      | TC_129__Negative__Token_Account_Mismatch_                                  | 10417     | Token Account Mismatch                | false   | null |
#
#      | TC_130__Negative__Token_Not_Found__No_Active_Pending_PPER_                 | 10411     | Token Not Found                       | false   | null |

      # TC_131/132: GZBEMCP FOR UPDATE lock — UAT often returns HTTP 504 instead of 40293.
      | TC_131__Negative__Paperless_Preference_Update_Failed__ACTIVE_              | 40293     | Paperless Preference Update Failed    | false   | null |
      | TC_132__Negative__Paperless_Preference_Update_Failed__NEW_                 | 40293     | Paperless Preference Update Failed    | false   | null |
      # TC_133: same NEW_TEST_EMAIL_ADDR clear as Update TC_77/78.
      # UAT Confirm returns 40321 Customer Email Creation Failed (FTD still lists 40281).
#      | TC_133__Negative__Confirmation_Notification_Failure_Rollback_             | 40321     | Customer Email Creation Failed        | false   | null |

#  @ConfirmPaperlessEnrollmentPositive @HappyFlow @CSI
#
#  Scenario Outline: "<testCondition>"
#
#    When a request is made to ConfirmPaperlessEnrollment Api for "<testCondition>"
#
#    Then verify response code of "ConfirmPaperlessEnrollment" Api is 200
#
#    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
#
#    Examples:
#
#      | testCondition                                                                                              | errorCode | errorMessage | success | data    |
#
#      | TC_134__Positive__ConfirmPaperlessEnrollment_accountType_Format__ACTIVE_                                 | 0         |              | true    | notNull |
#
#      | TC_135__Positive__ConfirmPaperlessEnrollment_accountType_Format__NEW_                                    | 0         |              | true    | notNull |
#
#      | TC_136__Positive__billDeliveryOptionStatus_Format__CONFIRMED_                                             | 0         |              | true    | notNull |
#
#      | TC_137__Positive__billDeliveryOptionStatus_Format__NO_CHANGE_                                            | 0         |              | true    | notNull |
#
#      | TC_138__Positive__corrDeliveryOptionStatus_Format__CONFIRMED_                                              | 0         |              | true    | notNull |
#
#      | TC_139__Positive__corrDeliveryOptionStatus_Format__NO_CHANGE_                                            | 0         |              | true    | notNull |
#
#      | TC_140__Positive__confirmationDateTime_Format_                                                           | 0         |              | true    | notNull |
#
#      | TC_141__Positive__Active_Account_Bill_Confirmation_                                                      | 0         |              | true    | notNull |
#
#      | TC_142__Positive__New_Account_Bill_Confirmation_                                                         | 0         |              | true    | notNull |
#
#      | TC_143__Positive__Active_Account_Both_Channels_Confirmation_                                             | 0         |              | true    | notNull |
#
#      | TC_144__Positive__New_Account_Both_Channels_Confirmation_                                                | 0         |              | true    | notNull |
#
#      | TC_145__Positive__Active_Account_Correspondence_Confirmation_                                            | 0         |              | true    | notNull |
#
#      | TC_146__Positive__New_Account_Correspondence_Confirmation_                                               | 0         |              | true    | notNull |
#
#      | TC_147__Positive__Account_Transitions_from_NEW_to_ACTIVE_Before_Confirmation_                            | 0         |              | true    | notNull |
#
#      | TC_148__Positive__Current_Aggregated_PPER_State_Confirmed_                                               | 0         |              | true    | notNull |
#
#      | TC_149__Positive__No_Email_on_File_Does_Not_Prevent_Success_                                             | 0         |              | true    | notNull |
#
#      | TC_150__Positive__Confirmation_Email_Failure_Does_Not_Prevent_Success_                                   | 0         |              | true    | notNull |
#
#      | TC_151__Positive__Token_Marked_Used_and_PPER_Archived_After_Successful_Active_Finalization_              | 0         |              | true    | notNull |
#
#      | TC_152__Positive__Token_Marked_Used_and_PPER_Archived_After_Successful_New_Finalization_                 | 0         |              | true    | notNull |
#
#      | TC_153__Positive__New_Account_Finalization_Uses_Confirmation_Dates_Rather_Than_Final_Preferences_      | 0         |              | true    | notNull |
#
