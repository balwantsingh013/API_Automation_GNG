package com.gng.api.pojo.AccountsPojo.SearchAccounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchAccountsResponse{
	private String billingUnitNumber;
	private String homePhoneNumber;
	private Object prepayEstimateOriginalAmount;
	private String billingUnitType;
	private Object payInAdvanceIndicator;
	private String premisesUnitType;
	private String premisesStreetPostDirection;
	private Object prepayOneTimeWelcomeCredit;
	private String homePhoneType;
	private String customerType;
	private String billingStateCode;
	private String loginID;
	private String premisesStateCode;
	private String billingZipCode;
	private Object prepayPlanIndicator;
	private String premisesStreetName;
	private String requestID;
	private String customerFirstName;
	private String premisesStreetPreDirection;
	private String billingCountyCode;
	private String premisesZipCode;
	private Object prepayEstimateAmountDue;
	private String premisesUnitNumber;
	private int badDebtAmount;
	private String billingStreetPreDirection;
	private String customerCode;
	private Object prepayEstimateBalance;
	private Object pastDueAmount;
	private String premisesCode;
	private Object paymentAmount;
	private Object transactionID;
	private String premisesCity;
	private String customerMiddleName;
	private String billingStreetSuffix;
	private String accountStatus;
	private Object prepayPaymentReceived;
	private String customerLastName;
	private String billingStreetNumber;
	private String recordType;
	private String paymentConfirmationNumber;
	private String billingStreetName;
	private String premisesStreetNumber;
	private String billingStreetPostDirection;
	private String transactionType;
	private String premisesCountyCode;
	private boolean sspStatusIndicator;
	private String lastFourSocialSecurityNumber;
	private String premisesStreetSuffix;
	private String billingCity;
}