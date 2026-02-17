package com.gng.api.pojo.CSIPojo.GetBankDraftInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBankDraftInfoResponse {

    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private BankDraftData data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BankDraftData {
        private String bankDraftStatus;
        private String bankDraftRoutingNumber;
        private String bankDraftAccountNumber;
        private String bankDraftAccountType;
        private String bankName;
    }
}
