package com.gng.api.pojo.AccountsPojo.getAccountInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAccountInfoRequest {
    private String requestID;
    private Object customerCode;
    private Object premisesCode;
    private String serviceNumber;
    private String noteTypeCode;
    private String noteText;
    private String origin;
    private String expirationDate;
    private String suspenseDate;
    private String userIDRemind;
}
