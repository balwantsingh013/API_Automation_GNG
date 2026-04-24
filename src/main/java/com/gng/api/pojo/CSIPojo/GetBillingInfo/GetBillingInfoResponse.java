package com.gng.api.pojo.CSIPojo.GetBillingInfo;

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
public class GetBillingInfoResponse {

    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private DataBlock data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataBlock {

        private String billDate;
        private String billFromDate;
        private String billToDate;

        private Double pastDueAmount;
        private String pastDueDate;

        private Double previousBillAmount;
        private Double paymentsApplied;
        private Double balanceBroughtForward;
        private Double currentCharges;
        private Double totalAmountDue;

        private String billDueDate;

        private Double paymentsSinceLastBill;
        private Double currentBalance;

        private Double budgetBillingAmount;
        private Double budgetBillingTotalVariance;
        private String budgetBillingStartDate;

        private List<BillLineItem> billLineItems;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BillLineItem {

        private String code;
        private String description;
        private Double amount;
        private String category;
    }
}