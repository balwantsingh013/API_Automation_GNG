package com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveUnenrollmentResponse {

    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private DataResponse data;


    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataResponse {
        private String accountNumber;
        private int marketerReferenceData;
    }
}

