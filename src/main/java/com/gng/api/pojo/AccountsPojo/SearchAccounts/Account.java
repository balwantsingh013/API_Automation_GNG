package com.gng.api.pojo.AccountsPojo.SearchAccounts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {
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
    private BigDecimal pastDueAmount;
    private BigDecimal badDebtAmount;
    private BigDecimal paymentAmount;
    private String paymentConfirmationNumber;
    private boolean prepayPlanIndicator;
    private boolean payInAdvanceIndicator;
    private BigDecimal prepayEstimateOriginalAmount;
    private BigDecimal prepayOneTimeWelcomeCredit;
    private BigDecimal prepayEstimateAmountDue;
    private BigDecimal prepayPaymentReceived;
    private BigDecimal prepayEstimateBalance;
    private String prepayEstimateStatus;
    private String prepayCustomerPayByDate;
    private String prepaySystemPayByDate;
    private String aglcAccountNumber;
    private BigDecimal earlyTerminationCharge;
    private String aglcDeliveryPoolGroup;
    private String greenerLife;
    private BigDecimal priceCeiling;
    private String planExpirationDate;
    private String masterAccount;
    private String meteredAccount;
    private String sonpAccount;
    private String waiveETCACR;
    private BigDecimal unappliedDepositAmount;
    private String priceProtectionGuarantee;
    private String turnOffAllowed;
    private BigDecimal reconnectDepositAmount;
    private String currentPricePlanCode;
    private String currentPricePlanDescription;
    private BigDecimal pricePerTherm;
    //private String activeDiscounts;
    //private List<Reward> rewards;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> rewards = new ArrayList<>();


    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<ActiveDiscount> activeDiscounts = new ArrayList<>();

}
