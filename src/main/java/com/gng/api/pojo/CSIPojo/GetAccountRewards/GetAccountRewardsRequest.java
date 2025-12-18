package com.gng.api.pojo.CSIPojo.GetAccountRewards;

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
public class GetAccountRewardsRequest {

    private String requestID;
    private String customerCode;
    private String premisesCode;
    private String userID;
    private String loginID;   // Optional field for CSR/user identification
}
