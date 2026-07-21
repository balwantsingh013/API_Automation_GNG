package com.gng.api.pojo.CSIPojo.GetPaperlessEnrollmentEligibility;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPaperlessEnrollmentEligibilityRequest {

    private String requestID;
    private String loginID;
    private String customerCode;
    private String premisesCode;
}
