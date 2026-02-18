package com.gng.api.steps.csi.GetAccountRewards;

public enum GetAccountRewardsLabel {

    get_account_rewards,
    get_account_rewards_mandatory,

    // Negative test conditions
    TC_129__Negative__Missing_Request_ID,
    TC_130__Negative__Invalid_Request_ID_Length,
    TC_131__Negative__Duplicate_Request_ID,
    TC_132__Negative__Missing_customerCode,
    TC_133__Negative__Invalid_customerCode_Length,
    TC_134__Negative__Invalid_customerCode_Format,
    TC_135__Negative__Invalid_Account_Number,
    TC_136__Negative__Missing_premisesCode,
    TC_137__Negative__Invalid_premisesCode_Length,
    TC_138__Negative__Invalid_premisesCode_Format,

    // Positive test conditions
    TC_139__Positive__Rewards_Response_Returned_with_Active_Rewards,
    TC_140__Positive__Rewards_Response_Returned_with_Pending_Rewards,
    TC_141__Positive__Rewards_Response_Returned_with_Active_and_Pending_Rewards,
    TC_142__Positive__Rewards_Response_Returned_with_No_Rewards,
    TC_143__Positive__Active_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards,
    TC_144__Positive__Pending_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards
}
