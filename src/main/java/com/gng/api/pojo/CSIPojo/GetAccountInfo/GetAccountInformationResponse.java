package com.gng.api.pojo.CSIPojo.GetAccountInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAccountInformationResponse {

    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private DataResponse data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataResponse {
        private String emailAddress;
        private String partnerPromotionsIndicator;
        private String marketingOffersIndicator;
        private String accountBillingReminder;
        private String phoneNumber;
        private String phoneType;
        private String greenerLifeRate;
        private String billDeliveryOption;
        private String correspondenceDeliveryOption;
        private String planRenewalWindowIndicator;
        private List<PricePlan> pricePlans;
        private List<Discount> discounts;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PricePlan {
        private Integer serviceNumber;
        private String planEffectiveDate;
        private String planExpirationDate;
        private String planCode;
        private Double planThermPrice;
        private String planRateCode;
        private Double planETC;
        private Double planGBPAmount;
        private Double planServiceCharge;
        private Double priceProtectionGuaranteeFee;
        private Double priceProtectionGuaranteeCeiling;
        private String rolloverPlanIndicator;
        private String restrictedPlanIndicator;
        private String planDescription;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Discount {
        private String discountCode;
        private String discountType;
        private String discountStartDate;
        private String discountEndDate;
        private String discountRate;
        private String discountDescription;
        private String discountTransferabilityIndicator;
    }
}
