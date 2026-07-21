package com.gng.api.steps.csi.ConfirmPaperlessEnrollment;



public enum ConfirmPaperlessEnrollmentLabel {



    confirm_paperless_enrollment,



    TC_120__Negative__Missing_Request_ID_,

    TC_121__Negative__Invalid_Request_ID_Length_,

    TC_122__Negative__Duplicate_Request_ID_,

    TC_123__Negative__Missing_Token_,

    TC_124__Negative__Token_Validation_Failed_,

    TC_125__Negative__Token_Not_Found__No_Stored_Token_Record_,

    TC_126__Negative__Token_Expired__Stored_Expiry_Passed_,

    TC_127__Negative__Token_Expired__Passive_Email_Change_Invalidation_,

    TC_128__Negative__Token_Already_Used_,

    TC_129__Negative__Token_Account_Mismatch_,

    TC_130__Negative__Token_Not_Found__No_Active_Pending_PPER_,

    TC_131__Negative__Paperless_Preference_Update_Failed__ACTIVE_,

    TC_132__Negative__Paperless_Preference_Update_Failed__NEW_,

    TC_133__Negative__Confirmation_Notification_Failure_Rollback_,



    TC_134__Positive__ConfirmPaperlessEnrollment_accountType_Format__ACTIVE_,

    TC_135__Positive__ConfirmPaperlessEnrollment_accountType_Format__NEW_,

    TC_136__Positive__billDeliveryOptionStatus_Format__CONFIRMED_,

    TC_137__Positive__billDeliveryOptionStatus_Format__NO_CHANGE_,

    TC_138__Positive__corrDeliveryOptionStatus_Format__CONFIRMED_,

    TC_139__Positive__corrDeliveryOptionStatus_Format__NO_CHANGE_,

    TC_140__Positive__confirmationDateTime_Format_,

    TC_141__Positive__Active_Account_Bill_Confirmation_,

    TC_142__Positive__New_Account_Bill_Confirmation_,

    TC_143__Positive__Active_Account_Both_Channels_Confirmation_,

    TC_144__Positive__New_Account_Both_Channels_Confirmation_,

    TC_145__Positive__Active_Account_Correspondence_Confirmation_,

    TC_146__Positive__New_Account_Correspondence_Confirmation_,

    TC_147__Positive__Account_Transitions_from_NEW_to_ACTIVE_Before_Confirmation_,

    TC_148__Positive__Current_Aggregated_PPER_State_Confirmed_,

    TC_149__Positive__No_Email_on_File_Does_Not_Prevent_Success_,

    TC_150__Positive__Confirmation_Email_Failure_Does_Not_Prevent_Success_,

    TC_151__Positive__Token_Marked_Used_and_PPER_Archived_After_Successful_Active_Finalization_,

    TC_152__Positive__Token_Marked_Used_and_PPER_Archived_After_Successful_New_Finalization_,

    TC_153__Positive__New_Account_Finalization_Uses_Confirmation_Dates_Rather_Than_Final_Preferences_

}

