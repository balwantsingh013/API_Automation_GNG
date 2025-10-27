package com.gng.api.pojo.AccountsPojo.getAccountInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAccountInfoResponse {
    private Data data;
    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private String customerCode;
        private String premisesCode;
        private String custFirstName;
        private String custMiddleName;
        private String custLastNameBus;
        private String accountStatus;
        private String rateSchedule;
        private String currentPlanType;
        private String lastFourSSN;
        private String premStreetNum;
        private String premStreetPreDir;
        private String premStreetName;
        private String premStreetSuffix;
        private String premStreetPostDir;
        private String premUnitType;
        private String premUnitNum;
        private String premCity;
        private String premState;
        private String premZip;
        private String billingStreetNum;
        private String billingStreetPreDir;
        private String billingStreetName;
        private String billingStreetSuffix;
        private String billingStreetPostDir;
        private String billingUnitType;
        private String billingUnitNum;
        private String billingCity;
        private String billingState;
        private String billingZip;
        private String billedBalance;
        private String pastDueAmount;
        private String pastDueDate;
        private String billPrintDate;
        private String billEndAmount;
        private String billDueDate;
        private String lastPaymentAmount;
        private String lastPaymentDate;
        private String discLetterDate;
        private String discLetterAmount;
        private String activePAInd;
        private String recurringCCInd;
        private String bankDraftInd;
        private String activeBudgetInd;
        private String badDebtInd;
        private String activeWarrantyInd;
        private String lastDefaultPADate;
        private String lastSONPDate;
        private String lastPreCollDate;
    }

}
