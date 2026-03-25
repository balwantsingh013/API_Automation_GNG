package com.gng.api.pojo.CSIPojo.GetPaymentArrangementInfo;

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
public class GetPaymentArrangementInfoResponse {

    private Boolean success;
    private Integer errorCode;
    private String errorMessage;
    private String requestID;
    private DataObject data;

    @lombok.Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataObject {

        private Integer paNumber;
        private String paTypeCode;
        private Double paTotalAmount;
        private String paDateCreated;
        private Integer numberOfInstallments;
        private List<PaInstallment> paInstallments;
    }

    @lombok.Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PaInstallment {

        private Double amountDue;
        private Double balance;
        private String dateDue;
        private String datePaid;
    }
}
