package com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveEnrollmentRequest  {
    private String requestID;
    private String loginID;
    private int transactionID;
    private String transactionType;
    private int customerCode;
    private String premisesCode;
    private String planCode;
    private String promotionCode;
    private String enrollmentStatus;
    private String paymentConfirmationNumber;
    private String billingPlan;
    private int estimatedBudgetAmount;
    private String customerRequestedServiceDate;
    private String seasonalSavingsProgramResult;
    private boolean splitConnectionFeeIndicator;
    private String aglcAccountNumber;
    private String aglcServiceOrderNumber;
    private String requestedTurnOnDate;
    private String entranceInstructions;
    private String specialInstructions;
    private String notes;
    private int sspParticipantCode;
    private String currentMarketerCode;
    
}