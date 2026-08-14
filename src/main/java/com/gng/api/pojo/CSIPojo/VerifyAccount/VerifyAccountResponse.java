package com.gng.api.pojo.CSIPojo.VerifyAccount;

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
public class VerifyAccountResponse {

    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private DataBlock data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataBlock {

        private String customerCode;
        private String premisesCode;
        private String accountStatus;
        private String billingStreetNumber;
        private String billingStreetPreDirection;
        private String billingStreetName;
        private String billingStreetSuffix;
        private String billingStreetPostDirection;
        private String billingUnitType;
        private String billingUnitNumber;
        private String billingCity;
        private String billingState;
        private String billingStateCode;
        private String billingZip;
        private String billingZipCode;
        private String billingPoBox;
        private String billDeliveryConfirmDate;
        private String corrDeliveryConfirmDate;
        private String billPresType;
        private String correspondencePreference;
        private String billDeliveryOption;
        private String correspondenceDeliveryOption;
        private String emailAddress;
    }
}