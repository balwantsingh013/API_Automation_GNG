package com.gng.api.pojo.CSIPojo.UpdateBankDraft;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBankDraftResponse {

    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private UpdateBankDraftData data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateBankDraftData {
        private String bankName;
    }
}
