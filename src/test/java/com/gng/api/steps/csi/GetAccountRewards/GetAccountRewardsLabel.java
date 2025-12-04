package com.gng.api.steps.csi.GetAccountRewards;

public enum GetAccountRewardsLabel {

    get_account_rewards,
    get_account_rewards_mandatory,

    TC_107__Negative__Missing_Request_ID,
    TC_108__Negative__Invalid_Request_ID_Length,
    TC_109__Negative__Duplicate_Request_ID,
    TC_110__Negative__Missing_customerCode,
    TC_111__Negative__Invalid_customerCode_Length,
    TC_112__Negative__Invalid_customerCode_Format,
    TC_113__Negative__Invalid_customerCode,
    TC_114__Negative__Missing_premisesCode,
    TC_115__Negative__Invalid_premisesCode_Length,
    TC_116__Negative__Invalid_premisesCode_Format,
    TC_117__Negative__Invalid_premisesCode,

    TC_118__Positive__Rewards_Response_Returned_with_Active_Rewards,
    TC_119__Positive__Rewards_Response_Returned_with_Pending_Rewards,
    TC_120__Positive__Rewards_Response_Returned_with_Active_and_Pending_Rewards,
    TC_121__Positive__Rewards_Response_Returned_with_NO_Rewards,
    TC_122__Positive__Rewards_Response_Returned_with_Refer__a__Friend_Reward
}
