package com.gng.api.pojo.CSIPojo.ReportPayment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportPaymentResponse {

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

        private Boolean paCreated;
        private Double paAmount;
        private String paDueDate;
    }
}
