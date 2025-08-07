package com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request;

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
public class GetEligiblePlansAndOffersRequest{
	private Object billingUnitNumber;
	private Object homePhoneNumber;
	private Object aglcAccountNumber;
	private Object billingUnitType;
	private Object billingAddressLine2;
	private Object additionalEnrollmentData;
	private Object premisesUnitType;
	private Object generationCode;
	private Object premisesStreetPostDirection;
	private Object homePhoneType;
	private String customerType;
	private Object emailAddress;
	private Object billingStateCode;
	private Object commercialCreditCheckBusinessBIN;
	private Object workPhoneNumber;
	private String tenantLandlord;
	private String enrollmentSource;
	private String creditCheckOption;
	private Object workPhoneType;
	private String loginID;
	private String premisesStateCode;
	private Object federalTaxID;
	private Object customerPEWCPreferences;
	private Object billingZipCode;
	private Object billingRuralRoute;
	private String premisesStreetName;
	private String requestID;
	private String customerFirstName;
	private Object premisesStreetPreDirection;
	private Object enrollmentState;
	private Object billingCountyCode;
	private Object initialCreditCheckCustomerCode;
	private String premisesZipCode;
	private Object workPhoneExtension;
	private Object customerBusinessName;
	private Object premisesUnitNumber;
	private String socialSecurityNumber;
	private Object billingStreetPreDirection;
	private Object sspParticipantCode;
	private Object customerCode;
	private boolean confirmCreditCheck;
	private Object premisesCode;
	private Object transactionID;
	private String premisesCity;
	private boolean callerIDNotAvailable;
	private Object customerMiddleName;
	private Object billingStreetSuffix;
	private Object creditCheckBusinessName;
	private String referralCode;
	private Object billingRuralRouteNumber;
	private String aglcServiceLocationID;
	private String customerLastName;
	private Object callerID;
	private Object billingStreetNumber;
	private String acnStatusIndicator;
	private Object billingStreetName;
	private String marketingPromotionCode;
	private String premisesStreetNumber;
	private Object billingStreetPostDirection;
	private String transactionType;
	private Object billingPOBox;
	private Object separateBillingAddress;
	private String authorizedBy;
	private String premisesCountyCode;
	private String premisesStreetSuffix;
	private Object billingAddressType;
	private boolean seasonalSavingsProgramIndicator;
	private Object billingCity;

}