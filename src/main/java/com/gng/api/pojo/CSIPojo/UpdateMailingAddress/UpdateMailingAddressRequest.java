package com.gng.api.pojo.CSIPojo.UpdateMailingAddress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMailingAddressRequest {

    private String requestID;
    private String customerCode;
    private String premisesCode;

    private String streetNumber;
    private String streetPreDirection;
    private String streetName;
    private String streetSuffix;
    private String streetPostDirection;

    private String unitType;
    private String unitNumber;

    private String poBox;
    private String ruralRoute;

    private String city;
    private String countyCode;
    private String zipCode;

    private String deliveryPoint;
    private String carrierRoute;
    private String attentionTo;
    private String additionalAddressLine;

    private String loginID;
}
