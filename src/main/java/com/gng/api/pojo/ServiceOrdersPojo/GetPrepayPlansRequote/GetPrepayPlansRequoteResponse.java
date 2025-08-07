package com.gng.api.pojo.ServiceOrdersPojo.GetPrepayPlansRequote;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gng.api.pojo.shared.PlanType;
import com.gng.api.util.YesNoBooleanDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPrepayPlansRequoteResponse {
    private DataResult data;
    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataResult {
        private String transactionID;
        private String customerCode;
        private String premisesCode;
        private String additionalInformation;
        private String customerType;
        private ArrayList<Plan> plans;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnrollmentStatus {
        private String code;
        private String label;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Plan implements PlanType {
        private String planCode;
        private String planDescription;
        private int sortOrder;
        private double thermPrice;
        private Integer planDuration;
        private double serviceCharge;
        private int signUpCharge;
        private int priceCeiling;
        private int priceProtectionFee;
        private int cancelFee;
        private double highCustomerServiceCharge;
        private double lowCustomerServiceCharge;
        private String marketingTerms;
        private String offerTerms;
        private String externalTerms;
        @JsonDeserialize(using = YesNoBooleanDeserializer.class)
        private Boolean restrictedIndicator;
        private Object prepayPlanIndicator;
        private Object prepayEstimateAmountDue;
        private Object prepayEstimatedConsumption;
        private String prepayCustomerPayByDate;
        private String prepaySystemPayByDate;
        private Object prepayOneTimeWelcomeCredit;
        private String prepayOneTimeWelcomeCreditMessage;
        private Object prepayEstimateOriginalAmount;
        private Object newPrepayAmount;
        private Object newPrepayAmountDue;
        private Object newPrepayMinimumPaymentRequired;
        @JsonDeserialize(using = YesNoBooleanDeserializer.class)
        private Boolean payInAdvanceIndicator;
        private Object guaranteedBillPlanQuote;
        private Integer newPrepayGuaranteedBillAmount;
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
        private Object promotion2TransferIndicator;
        private Object promotion2VisaIndicator;
        private ArrayList<EnrollmentStatus> enrollmentStatus;
        private Object depositAmount;

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