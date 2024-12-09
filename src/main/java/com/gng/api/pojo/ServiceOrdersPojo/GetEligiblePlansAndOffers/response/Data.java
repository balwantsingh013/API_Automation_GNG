package com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {
	private String billingUnitNumber;
	private String homePhoneNumber;
	private Object similarBusinesses;
	private String aglcAccountNumber;
	private String billingUnitType;
	private String billingAddressLine2;
	private String premisesUnitType;
	private String generationCode;
	private String premisesStreetPostDirection;
	private String homePhoneType;
	private String customerType;
	private String emailAddress;
	private String billingStateCode;
	private String workPhoneNumber;
	private String tenantLandlord;
	private String enrollmentSource;
	private int numberOfMatches;
	private Object additionalInformation;
	private String creditCheckOption;
	private String workPhoneType;
	private String loginID;
	private String premisesStateCode;
	private boolean customerPEWCPreferences;
	private String billingZipCode;
	private int availableSplitConnectionFeeInstallments;
	private String premisesStreetName;
	private List<PlansItem> plans;
	private String customerFirstName;
	private String premisesStreetPreDirection;
	private String enrollmentState;
	private String billingCountyCode;
	private String premisesZipCode;
	private String workPhoneExtension;
	private String customerBusinessName;
	private String premisesUnitNumber;
	private String billingStreetPreDirection;
	private Object sspParticipantCode;
	private String customerCode;
	private String premisesCode;
	private String transactionID;
	private String premisesCity;
	private boolean callerIDNotAvailable;
	private String customerMiddleName;
	private String billingStreetSuffix;
	private String creditCheckBusinessName;
	private String referralCode;
	private String aglcServiceLocationID;
	private String customerLastName;
	private String callerID;
	private String billingStreetNumber;
	private String acnStatusIndicator;
	private String billingStreetName;
	private String marketingPromotionCode;
	private String premisesStreetNumber;
	private String billingStreetPostDirection;
	private String transactionType;
	private String authorizedBy;
	private String premisesCountyCode;
	private String earliestPossibleTurnOnDate;
	private String premisesStreetSuffix;
	private boolean seasonalSavingsProgramIndicator;
	private String billingCity;
}