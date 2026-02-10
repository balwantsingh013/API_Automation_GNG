package com.gng.api.pojo.CSIPojo.GetAccountRewards;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetAccountRewardsResponse {

    private String requestID;
    private boolean success;
    private int errorCode;
    private String errorMessage;
    private DataBlock data; // <-- MUST be named "data" to match JSON

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataBlock {
        private int activeRewardsCount;
        private int pendingRewardsCount;
        private List<Reward> rewards;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Reward {
        private int rewardID;
        private String rewardName;
        private String rewardTypeCode;
        private String rewardTypeDescription;
        private String status;
        private String rewardEstablishedDate;
        private String rewardInitiatedDate;
        private Integer remainingOccurrences;
        private int totalOccurrences;
        private String restrictions;
        private String referenceCustomerCode;
        private String referencePremisesCode;
    }
}
