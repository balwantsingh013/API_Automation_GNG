package com.gng.api.pojo.CommonPojo.GetMerketerReferenceData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMarketerReferenceDataResponse {
    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private GetMarketerReferenceDataResponse.Data data;


    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private long marketerReferenceData;
    }
}
