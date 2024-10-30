package com.gng.api.pojo.createaccountnote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountNoteResponse {
    private Data data;
    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private String noteSequenceNumber;
    }
}