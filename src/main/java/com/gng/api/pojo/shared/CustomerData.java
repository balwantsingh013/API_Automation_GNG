package com.gng.api.pojo.shared;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerData {
    private String loginID;
    private String customerType;
    private String customerLastName;
    private String customerMiddleName;
    private String customerFirstName;
    private String aglcAccountNumber;
    private String aglcServiceLocationID;
    private String premisesStreetPreDirection;
    private String premisesStreetNumber;
    private String premisesStreetName;
    private String premisesStreetSuffix;
    private String premisesUnitType;
    private String premisesUnitNumber;
    private String premisesCity;
    private String premisesStateCode;
    private String premisesZipCode;
    private String premisesCountyCode;
    private String separateBillingAddress;
    private String acnStatusIndicator;
    private String customerPEWCPreferences;
    private String creditCheckOption;
    private boolean confirmCreditCheck;
    private String tenantLandlord;
    private String premisesStreetPostDirection;
    private String socialSecurityNumber;
    private String phoneNumber;
    private String marketingPromotionCode;
    private String federalTaxId;

    public boolean getConfirmCreditCheck() {
    return confirmCreditCheck;
    }
}
