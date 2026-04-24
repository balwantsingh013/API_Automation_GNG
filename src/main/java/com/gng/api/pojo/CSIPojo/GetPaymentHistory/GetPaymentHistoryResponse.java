package com.gng.api.pojo.CSIPojo.GetPaymentHistory;

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
public class GetPaymentHistoryResponse {

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
        private List<PaymentHistory> paymentHistory;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PaymentHistory {

        private String paymentDate;          // String(8) YYYYMMDD
        private Double paymentAmount;        // Number(10,2)
        private String paymentCode;          // String(4)
        private String paymentDescription;   // String(35)
    }
}
