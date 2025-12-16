package com.gng.api.pojo.CSIPojo.UpdateUsername;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUsernameResponse {

    private String requestID;
    private boolean success;
    private int errorCode;
    private String errorMessage;
    private UpdateUsernameRecord data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUsernameRecord {
        private String action;
    }
}
