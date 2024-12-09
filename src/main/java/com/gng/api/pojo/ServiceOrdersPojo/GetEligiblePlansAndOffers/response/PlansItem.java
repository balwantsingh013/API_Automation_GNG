package com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlansItem{
	private Object prepayEstimateAmountDue;
	private String prepaySystemPayByDate;
	private String prepayOneTimeWelcomeCreditMessage;
	private String promotion2MarketingMessage;
	private Object prepayEstimateOriginalAmount;
	private int signUpCharge;
	private int newPrepayGuaranteedBillAmount;
	private boolean payInAdvanceIndicator;
	private Object highCustomerServiceCharge;
	private String offerTerms;
	private Object newPrepayAmountDue;
	private int cancelFee;
	private String promotion2Description;
	private String promotion1Terms;
	private boolean promotion1VisaIndicator;
	private Object prepayOneTimeWelcomeCredit;
	private String guaranteedBillPlanErrorCode;
	private Object serviceCharge;
	private Object planDuration;
	private String promotion2Code;
	private String marketingTerms;
	private Object lowCustomerServiceCharge;
	private String externalTerms;
	private Object depositAmount;
	private String prepayCustomerPayByDate;
	private Object newPrepayMinimumPaymentRequired;
	private String promotion1Description;
	private String promotion1Code;
	private boolean promotion1TransferIndicator;
	private int priceProtectionFee;
	private Object newPrepayAmount;
	private String guaranteedBillPlanResponseMessage;
	private int priceCeiling;
	private String promotion1MarketingMessage;
	private Object prepayPlanIndicator;
	private boolean restrictedIndicator;
	private String planCode;
	private String promotion2Terms;
	private boolean promotion2VisaIndicator;
	private Object thermPrice;
	private boolean promotion2TransferIndicator;
	private List<EnrollmentStatusItem> enrollmentStatus;
	private int sortOrder;
	private Object prepayEstimatedConsumption;
	private String planDescription;
	private Object guaranteedBillPlanQuote;
}