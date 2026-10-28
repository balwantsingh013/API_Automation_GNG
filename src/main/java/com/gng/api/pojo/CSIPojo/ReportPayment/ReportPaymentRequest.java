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
public class ReportPaymentRequest {

    private String requestID;
    private String loginID;
    private String customerCode;
    private String premisesCode;
    private String paymentDate;
    private String paymentAmount;
    private String paymentType;
    private String confirmationNumber;
    private String origin;
}
