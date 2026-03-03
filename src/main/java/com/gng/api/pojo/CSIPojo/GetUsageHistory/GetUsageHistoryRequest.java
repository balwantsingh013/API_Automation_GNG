package com.gng.api.pojo.CSIPojo.GetUsageHistory;

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
public class GetUsageHistoryRequest {

    private String requestID;
    private String loginID;
    private String customerCode;
    private String premisesCode;
    private Object numberOfMonths; // Accepts numeric or string values based on test conditions
}
