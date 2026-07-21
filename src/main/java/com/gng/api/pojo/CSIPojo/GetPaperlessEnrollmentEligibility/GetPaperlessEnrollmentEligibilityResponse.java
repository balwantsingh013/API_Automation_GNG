package com.gng.api.pojo.CSIPojo.GetPaperlessEnrollmentEligibility;

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
public class GetPaperlessEnrollmentEligibilityResponse {

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

        private String billDeliveryOption;
        private String correspondenceDeliveryOption;
        private Boolean paperlessBillEligible;
        private Boolean paperlessCorrespondenceEligible;
        private Integer paperlessBillIneligReasonCode;
        private Integer paperlessCorrIneligReasonCode;
        private String paperlessBillIneligReasonDesc;
        private String paperlessCorrIneligReasonDesc;
    }
}
