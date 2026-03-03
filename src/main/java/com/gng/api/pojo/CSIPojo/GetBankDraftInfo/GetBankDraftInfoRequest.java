package com.gng.api.pojo.CSIPojo.GetBankDraftInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBankDraftInfoRequest {

    private String requestID;
    private String loginID;
    private String customerCode;
    private String premisesCode;

}
