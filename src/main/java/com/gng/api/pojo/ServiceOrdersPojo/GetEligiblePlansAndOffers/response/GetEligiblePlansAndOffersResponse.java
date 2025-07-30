package com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gng.api.pojo.shared.PlanType;
import com.gng.api.util.YesNoBooleanDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEligiblePlansAndOffersResponse {

	private DataResult data;
	private boolean success;
	private String requestID;
	private String errorMessage;
	private int errorCode;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class DataResult {
		private String transactionID;
		private String customerCode;
		private String premisesCode;
		private String additionalInformation;
		private String enrollmentState;
		private String loginID;
		private String customerType;
		private String transactionType;
		private String enrollmentSource;
		private String marketingPromotionCode;
		private String callerID;
		private boolean callerIDNotAvailable;
		private boolean seasonalSavingsProgramIndicator;
		private String customerBusinessName;
		private String creditCheckBusinessName;
		private String customerLastName;
		private String generationCode;
		private String customerMiddleName;
		private String customerFirstName;
		private String emailAddress;
		private String aglcAccountNumber;
		private String aglcServiceLocationID;
		private String authorizedBy;
		private String referralCode;
		private String premisesStreetNumber;
		private String premisesStreetPreDirection;
		private String premisesStreetName;
		private String premisesStreetSuffix;
		private String premisesStreetPostDirection;
		private String premisesUnitType;
		private String premisesUnitNumber;
		private String premisesCity;
		private String premisesStateCode;
		private String premisesZipCode;
		private String premisesCountyCode;
		private String billingStreetNumber;
		private String billingStreetPreDirection;
		private String billingStreetName;
		private String billingStreetSuffix;
		private String billingStreetPostDirection;
		private String billingUnitType;
		private String billingUnitNumber;
		private String billingAddressLine2;
		private String billingCity;
		private String billingStateCode;
		private String billingZipCode;
		private String billingCountyCode;
		private String workPhoneNumber;
		private String workPhoneExtension;
		private String workPhoneType;
		private String homePhoneNumber;
		private String homePhoneType;
		private String acnStatusIndicator;
		private String tenantLandlord;
		private boolean customerPEWCPreferences;
		private String creditCheckOption;
		private int availableSplitConnectionFeeInstallments;
		private int numberOfMatches;
		private ArrayList<Plan> plans;
		private Object similarBusinesses;
		private Object sspParticipantCode;
		private String earliestPossibleTurnOnDate;
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
	public static class Plan implements PlanType{
		private String planCode;
		private String planDescription;
		private int sortOrder;
		private double thermPrice;
		private int planDuration;
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
		private int newPrepayGuaranteedBillAmount;
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