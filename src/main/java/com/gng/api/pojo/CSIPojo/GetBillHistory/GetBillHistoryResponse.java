package com.gng.api.pojo.CSIPojo.GetBillHistory;

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
public class GetBillHistoryResponse {

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
        private List<BillHistory> billHistory;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BillHistory {

        private String billDate;
        private String billFromDate;
        private String billToDate;

        private Integer daysOfService;
        private Integer heatingDegreeDays;

        private Double totalBilledConsumption;
        private Double balanceBroughtForward;
        private Double gasServiceCharges;
        private Double otherCharges;
        private Double promotionalDiscounts;
        private Double taxes;
        private Double budgetBillingAmount;
        private Double totalBillAmount;

        private Long billHistoryTransactionNumber;
    }
}
