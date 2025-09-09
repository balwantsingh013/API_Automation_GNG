package com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaveEnrollmentRequest  {
    private String requestID;
    private String loginID;
    private Object transactionID;
    private String transactionType;
    private Object customerCode;
    private String premisesCode;
    private String planCode;
    private String promotionCode;
    private String enrollmentStatus;
    private String paymentConfirmationNumber;
    private String billingPlan;
    private Object estimatedBudgetAmount;
    private String customerRequestedServiceDate;
    private String seasonalSavingsProgramResult;
    private Boolean splitConnectionFeeIndicator;
    private String aglcAccountNumber;
    private String aglcServiceOrderNumber;
    private String requestedTurnOnDate;
    private String entranceInstructions;
    private String specialInstructions;
    private String notes;
    private Object sspParticipantCode;
    private String currentMarketerCode;
    private Object marketerReferenceData;
}