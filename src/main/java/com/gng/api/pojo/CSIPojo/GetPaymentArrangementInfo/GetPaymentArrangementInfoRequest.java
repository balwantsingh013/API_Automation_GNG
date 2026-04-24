package com.gng.api.pojo.CSIPojo.GetPaymentArrangementInfo;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPaymentArrangementInfoRequest {

    private String requestID;
    private String loginID;
    private String customerCode;
    private String premisesCode;
}
