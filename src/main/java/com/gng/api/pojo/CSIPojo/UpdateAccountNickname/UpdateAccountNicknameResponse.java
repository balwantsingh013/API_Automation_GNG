package com.gng.api.pojo.CSIPojo.UpdateAccountNickname;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountNicknameResponse {

    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private DataResponse data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataResponse {
        private String nicknameStatus;
    }
}
