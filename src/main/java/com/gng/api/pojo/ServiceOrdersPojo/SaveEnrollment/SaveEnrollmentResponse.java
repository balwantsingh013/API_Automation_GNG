package com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveEnrollmentResponse {

    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private Data data;


    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private int transactionID;
        private String accountNumber;
        private String setAccountPreferencesURL;
        private String marketerReferenceData;
    }
}

