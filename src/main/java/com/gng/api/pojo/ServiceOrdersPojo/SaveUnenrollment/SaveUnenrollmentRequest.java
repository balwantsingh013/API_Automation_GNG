package com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment;

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
public class SaveUnenrollmentRequest {

    private String requestID;
    private String loginID;
    private String transactionType;
    private Object customerCode;
    private String premisesCode;
    private String aglcAccountNumber;
    private String forwardingAddressIs;
    private String forwardingAddressType;
    private String forwardingAddressStreetNumber;
    private String forwardingAddressStreetPreDirection;
    private String forwardingAddressStreetName;
    private String forwardingAddressStreetSuffix;
    private String forwardingAddressStreetPostDirection;
    private String forwardingAddressUnitType;
    private String forwardingAddressUnitNumber;
    private String forwardingAddressRuralRoute;
    private String forwardingAddressPOBox;
    private String forwardingAddressLine2;
    private String forwardingAddressCity;
    private String forwardingAddressStateCode;
    private String forwardingAddressZipCode;
    private String turnOffReason;
    private String turnOffSubReason;
    private String emailAddress;
    private String requestedTurnOffDate;
    private String aglcServiceOrderNumber;
    private boolean etcExists;
    private Long marketerReferenceData;
}
