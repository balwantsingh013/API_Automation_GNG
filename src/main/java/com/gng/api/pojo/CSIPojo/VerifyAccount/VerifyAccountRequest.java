package com.gng.api.pojo.CSIPojo.VerifyAccount;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Preferences PEW VerifyAccount inner request (stringified under broker {@code Request}).
 * Postman: actions=VerifyAccount, Module=PEW, acctSearchType=email.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyAccountRequest {

    private String acctSearchType;
    private String actions;
    private String custCode;
    private String premCode;
    private String emailAddress;

    @JsonProperty("Module")
    private String module;

    private String authenticationToken;
    private String requestID;
}
