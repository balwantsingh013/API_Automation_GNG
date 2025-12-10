package com.gng.api.pojo.CommonPojo.GetMarketerCodes.request;

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

public class GetMarketerCodesRequest {
    private String requestID;
    private String loginID;
}
