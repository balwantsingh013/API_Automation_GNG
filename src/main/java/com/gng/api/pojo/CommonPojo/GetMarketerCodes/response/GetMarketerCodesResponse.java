package com.gng.api.pojo.CommonPojo.GetMarketerCodes.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMarketerCodesResponse {

    private DataResult data;
    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataResult {
        private List<Marketer> marketers;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Marketer {
        private String marketerCode;
        private String marketerFullName;
    }
}
