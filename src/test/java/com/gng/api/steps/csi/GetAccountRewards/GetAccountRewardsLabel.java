package com.gng.api.steps.csi.GetAccountRewards;

public enum GetAccountRewardsLabel {

    get_account_rewards,
    get_account_rewards_mandatory,

    // Negative test conditions
    TC_112__Negative__Missing_Request_ID,
    TC_113__Negative__Invalid_Request_ID_Length,
    TC_114__Negative__Duplicate_Request_ID,
    TC_115__Negative__Missing_customerCode,
    TC_116__Negative__Invalid_customerCode_Length,
    TC_117__Negative__Invalid_customerCode_Format,
    TC_118__Negative__Invalid_customerCode,
    TC_119__Negative__Missing_premisesCode,
    TC_120__Negative__Invalid_premisesCode_Length,
    TC_121__Negative__Invalid_premisesCode_Format,
    TC_122__Negative__Invalid_premisesCode,

    // Positive test conditions
    TC_123__Positive__Active_Rewards,
    TC_124__Positive__Pending_Rewards,
    TC_125__Positive__Active_and_Pending_Rewards,
    TC_126__Positive__No_Rewards,
    TC_127__Positive__Refer_A_Friend_Rewards,
    TC_128__Positive__LoginID_Saved
}
