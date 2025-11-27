package com.gng.api.pojo.CSIPojo.UpdateMailingAddress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for UpdateMailingAddress API.
 * Covers all fields mentioned in the test conditions (TC_68–TC_106).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMailingAddressRequest {

    private String requestID;              // Unique request identifier
    private String customerCode;           // Customer code (numeric, up to 9 chars)
    private String premisesCode;           // Premises code (numeric, up to 7 chars)

    // Address fields
    private String streetNumber;           // Street number (<= 12 chars)
    private String streetPreDirection;     // Street pre-direction (<= 2 chars, must exist in reference table)
    private String streetName;             // Street name (<= 30 chars, required for street-type address)
    private String streetSuffix;           // Street suffix (<= 6 chars, must exist in reference table)
    private String streetPostDirection;    // Street post-direction (<= 2 chars, must exist in reference table)

    // Unit fields
    private String unitType;               // Unit type (<= 6 chars, must exist in reference table)
    private String unitNumber;             // Unit number (<= 6 chars)

    // Alternative address types
    private String poBox;                  // PO Box identifier
    private String ruralRoute;             // Rural Route identifier

    // Location fields
    private String city;                   // City (<= 20 chars, required)
    private String county;                 // County code (must exist in reference table)
    private String zipCode;                // Zip code (<= 10 chars, must exist in reference table)

    // Additional fields
    private String deliveryPoint;          // Delivery point (<= 2 chars)
    private String carrierRoute;           // Carrier route (<= 4 chars)
    private String attentionTo;            // Attention-to line (<= 30 chars)
    private String additionalAddressLine;  // Additional address line (<= 30 chars)
}
