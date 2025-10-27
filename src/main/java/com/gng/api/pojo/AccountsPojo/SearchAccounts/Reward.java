package com.gng.api.pojo.AccountsPojo.SearchAccounts;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reward {
    private Integer activeRewards;
    private Integer pendingRewards;
    private String  reward;
    private String  status;
    private String  occurrences;
    private String  restrictions;

}

