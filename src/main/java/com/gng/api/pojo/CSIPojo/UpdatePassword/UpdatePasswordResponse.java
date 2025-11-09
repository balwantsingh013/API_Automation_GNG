package com.gng.api.pojo.CSIPojo.UpdatePassword;

import com.gng.api.pojo.CSIPojo.ValidateUsername.ValidateUsernameResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UpdatePasswordResponse {

    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private ValidateUsernameResponse.DataResponse data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataResponse {
        private String usernameStatus;
    }
}
