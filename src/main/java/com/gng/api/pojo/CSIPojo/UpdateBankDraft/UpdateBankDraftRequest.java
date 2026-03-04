package com.gng.api.pojo.CSIPojo.UpdateBankDraft;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBankDraftRequest {

    private String requestID;
    private String loginID;
    private String customerCode;
    private String premisesCode;

    private String bankDraftAccountType;
    private String bankDraftRoutingNumber;
    private String bankDraftAccountNumber;
}
