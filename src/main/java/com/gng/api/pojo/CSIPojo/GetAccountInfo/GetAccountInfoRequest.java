package com.gng.api.pojo.CSIPojo.GetAccountInfo;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetAccountInfoRequest {

    private String requestID;
    private String customerCode;
    private String premisesCode;
    private String loginID;
}
