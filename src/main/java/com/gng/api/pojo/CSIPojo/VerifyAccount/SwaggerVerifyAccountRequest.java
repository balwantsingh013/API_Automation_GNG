package com.gng.api.pojo.CSIPojo.VerifyAccount;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** WAPI Swagger {@code POST /Accounts/VerifyAccount} request (FTD05 TC_206–227). */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwaggerVerifyAccountRequest {

    private String requestID;
    private String verifyType;
    private String customerCode;
    private String premisesCode;
    private String emailAddress;
    private String origin;
    private String loginID;
}
