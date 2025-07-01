package com.gng.api.pojo.AccountsPojo.SearchAccounts;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchAccountsResponse {
	private SearchData data;
	private boolean success;
	private int errorCode;
	private String errorMessage;
	private String requestID;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class SearchData {
		private int numberOfMatches;
		private List<Account> accounts;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Account {
		private String recordType;
		private int transactionID;
		private String customerCode;
		private String premisesCode;
		private String customerFirstName;
		private String customerMiddleName;
		private String customerLastName;
		private String customerBusinessName;
		private String creditCheckBusinessName;
		private String customerType;
		private String lastFourSocialSecurityNumber;
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
		private String billingAddressLine3;
		private String billingCity;
		private String billingStateCode;
		private String billingZipCode;
		private String billingCountyCode;
		private String billingNationCode;
		private String accountStatus;
		private String homePhoneNumber;
		private String homePhoneType;
		private String workPhoneNumber;
		private String workPhoneExtension;
		private String workPhoneType;
		private String transactionType;
		private String enrollmentState;
		private String enrollmentStatusDate;
		private String sspStatusIndicator;
		private String sspParticipantCode;
		private Double pastDueAmount;
		private Double badDebtAmount;
		private double paymentAmount;
		private String paymentConfirmationNumber;
		private boolean prepayPlanIndicator;
		private boolean payInAdvanceIndicator;
		private Double prepayEstimateOriginalAmount;
		private Double prepayOneTimeWelcomeCredit;
		private Double prepayEstimateAmountDue;
		private Double prepayPaymentReceived;
		private Double prepayEstimateBalance;
		private String prepayEstimateStatus;
		private String prepayCustomerPayByDate;
		private String prepaySystemPayByDate;
		private String aglcAccountNumber;
		private String rewards;
		private Double earlyTerminationCharge;
		private String aglcDeliveryPoolGroup;
		private String greenerLife;
		private Double priceCeiling;
		private String planExpirationDate;
		private String masterAccount;
		private String meteredAccount;
		private String sonpAccount;
		private String waiveETCACR;
		private Double unappliedDepositAmount;
		private String priceProtectionGuarantee;
		private String turnOffAllowed;
		private double reconnectDepositAmount;
		private String currentPricePlanCode;
		private String currentPricePlanDescription;
		private Double pricePerTherm;
		private String activeDiscounts;
	}
}
