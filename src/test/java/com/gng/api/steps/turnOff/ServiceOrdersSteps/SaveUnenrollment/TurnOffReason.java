package com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment;

import lombok.Getter;

@Getter
public enum TurnOffReason {

    SEASONAL_OR_HEAT_ONLY("Seasonal or Heat Only", ""),
    MOVING_OUTSIDE_AGLC("Moving", "Outside AGLC Territory/Outside Georgia"),
    OTHERS_MILITARY("Other", "Military"),
    OTHERS_MILITARY_ETC_WAIVED("Other", "Military - ETC Waived"),
    REAP_REALTOR_INSPECTION("REAP/Realtor Inspection", ""),
    HOUSEHOLD_ACCOUNT_CHANGE("Household Account Change", ""),
    OTHER_FINANCIAL_SITUATION("Other", "Financial Situation"),
    MOVING_NOT_STAYING_WITH_GNG("Moving", "Within Pool Group but Not Staying with GNG"),
    OTHER_REGULATED_PROVIDER("Other", "Regulated Provider"),
    OTHER_DECEASED("Other", "Deceased"),
    MOVING_SERVICE_TRANSFER("Moving", "Service Transfer");

    private final String reason;
    private final String subReason;

    // Constructor
    TurnOffReason(String reason, String subReason) {
        this.reason = reason;
        this.subReason = subReason;
    }

    public static TurnOffReason fromString(String reasonString) {
        // Normalize the input string: Convert to uppercase and replace spaces with underscores
        String normalizedReason = reasonString.toUpperCase().replace(" ", "_");

        // Iterate through the enum values and compare with the normalized string
        for (TurnOffReason reason : TurnOffReason.values()) {
            if (reason.name().equals(normalizedReason)) {
                return reason;
            }
        }

        // If no match is found, throw an exception
        throw new IllegalArgumentException("Unknown reason: " + reasonString);
    }
}

