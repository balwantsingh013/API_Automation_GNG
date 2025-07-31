package com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gng.api.pojo.shared.PlanType;
import com.gng.api.util.YesNoBooleanDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDefaultPlansAndOffersResponse {

    private DataResult data;
    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataResult {
        private int numberOfMatches;
        private String additionalInformation;
        private List<Plan> plans;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Plan implements PlanType{
        private String planCode;
        private String planDescription;
        private int sortOrder;
        private double thermPrice;
        private Integer planDuration;
        private double serviceCharge;
        private double signUpCharge;
        private double priceCeiling;
        private double priceProtectionFee;
        private double cancelFee;
        private Double highCustomerServiceCharge;
        private Double lowCustomerServiceCharge;
        private String marketingTerms;
        private String offerTerms;
        private String externalTerms;
        @JsonDeserialize(using = YesNoBooleanDeserializer.class)
        private Boolean restrictedIndicator;
        @JsonDeserialize(using = YesNoBooleanDeserializer.class)
        private Boolean prepayPlanIndicator;
        private Double prepayEstimateAmountDue;
        private Double prepayEstimatedConsumption;
        private String prepayCustomerPayByDate;
        private String prepaySystemPayByDate;
        private Double prepayOneTimeWelcomeCredit;
        private String prepayOneTimeWelcomeCreditMessage;
        private Double prepayEstimateOriginalAmount;
        private Double newPrepayAmount;
        private Double newPrepayAmountDue;
        private Double newPrepayMinimumPaymentRequired;
        @JsonDeserialize(using = YesNoBooleanDeserializer.class)
        private Boolean payInAdvanceIndicator;
        private Double guaranteedBillPlanQuote;
        private Double newPrepayGuaranteedBillAmount;
        private String guaranteedBillPlanErrorCode;
        private String guaranteedBillPlanResponseMessage;
        private String promotion1Code;
        private String promotion1Description;
        private String promotion1Terms;
        private String promotion1MarketingMessage;
        @JsonDeserialize(using = YesNoBooleanDeserializer.class)
        private Boolean promotion1TransferIndicator;
        @JsonDeserialize(using = YesNoBooleanDeserializer.class)
        private Boolean promotion1VisaIndicator;
        private String promotion2Code;
        private String promotion2Description;
        private String promotion2Terms;
        private String promotion2MarketingMessage;
        @JsonDeserialize(using = YesNoBooleanDeserializer.class)
        private Boolean promotion2TransferIndicator;
        @JsonDeserialize(using = YesNoBooleanDeserializer.class)
        private Boolean promotion2VisaIndicator;
        private String enrollmentStatus;
        private Double depositAmount;
        @Override
        public String getPlanCode() {
            return planCode;
        }
        @Override
        public String getPlanDescription() {
            return planDescription;
        }
        @Override
        public String getPromotion1Code() {
            return promotion1Code;
        }
        @Override
        public String getPromotion1Description() {
            return promotion1Description;
        }
    }
}
