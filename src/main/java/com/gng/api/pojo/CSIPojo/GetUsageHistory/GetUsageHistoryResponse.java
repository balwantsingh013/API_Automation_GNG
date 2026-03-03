package com.gng.api.pojo.CSIPojo.GetUsageHistory;

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
public class GetUsageHistoryResponse {

    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private DataBlock data; // MUST be named "data" to match JSON

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataBlock {
        private Integer numberOfMatches;
        private List<UsageHistory> usageHistory;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UsageHistory {

        private String serviceNumber;
        private String billDate;
        private String usageFromDate;
        private String usageToDate;

        private Double averageDailyActualConsumption;
        private Double averageDailyBilledConsumption;
        private Double totalBilledConsumption;

        private Integer daysOfService;
        private Double reading;

        private String readTypeCode;
        private String readDate;

        private Double averageTemperature;
        private Double heatingDegreeDays;

        private Long billHistoryTransactionNumber;
    }
}
