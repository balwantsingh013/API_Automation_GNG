package com.gng.api.pojo.CommonPojo.GetReasonsForLeaving.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetReasonsForLeavingResponse {
    private DataResult data;
    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
}