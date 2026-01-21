package com.gng.api.steps.csi.GetAccountRewards;

public enum GetAccountRewardsLabel {

    get_account_rewards,
    get_account_rewards_mandatory,

    // Negative test conditions
    TC_124__Negative__Missing_Request_ID,
    TC_125__Negative__Invalid_Request_ID_Length,
    TC_126__Negative__Duplicate_Request_ID,
    TC_127__Negative__Missing_customerCode,
    TC_128__Negative__Invalid_customerCode_Length,
    TC_129__Negative__Invalid_customerCode_Format,
    TC_130__Negative__Invalid_Account_Number,
    TC_131__Negative__Missing_premisesCode,
    TC_132__Negative__Invalid_premisesCode_Length,
    TC_133__Negative__Invalid_premisesCode_Format,

    // Positive test conditions
    TC_134__Positive__Rewards_Response_Returned_with_Active_Rewards,
    TC_135__Positive__Rewards_Response_Returned_with_Pending_Rewards,
    TC_136__Positive__Rewards_Response_Returned_with_Active_and_Pending_Rewards,
    TC_137__Positive__Rewards_Response_Returned_with_No_Rewards,
    TC_138__Positive__Active_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards,
    TC_139__Positive__Pending_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards
}
