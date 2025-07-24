package com.gng.api.pojo.CommonPojo.GetReasonsForLeaving;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetReasonsForLeavingRequest {
    private String requestID;
    private String loginID;
    private boolean etcExists;
    private String transactionType;
}
