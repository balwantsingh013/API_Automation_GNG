package com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class GetDefaultPlansAndOffersRequest {
    private String requestID;
    private String loginID;
    private String customerType;
    private String transactionType;
    private String enrollmentSource;
    private String marketingPromotionCode;
}
