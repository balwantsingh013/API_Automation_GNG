package com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment;

import lombok.Getter;

@Getter
public enum TurnOffReason {

    POSITIVE_RESIDENTIAL_TURN_OFF_UC28_TC207("Seasonal or Heat Only", ""),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC44_TC223("Seasonal or Heat Only", ""),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC29_TC208("Moving", "Outside AGLC Territory/Outside Georgia"),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC51_TC230("Moving", "Outside AGLC Territory/Outside Georgia"),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC30_TC209("Other", "Military"),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC35_TC214("Other", "Military - ETC Waived"),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC31_TC210("REAP/Realtor Inspection", ""),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC48_TC227("REAP/Realtor Inspection", ""),
    HOUSEHOLD_ACCOUNT_CHANGE("Household Account Change", ""),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC36_TC215("Other", "Financial Situation"),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC37_TC216("Moving", "Within Pool Group but Not Staying with GNG"),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC50_TC229("Moving", "Within Pool Group but Not Staying with GNG"),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC40_TC219("Other", "Regulated Provider"),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC43_TC222("Other", "Deceased"),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC49_TC228("Moving", "Service Transfer"),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC46_TC225("Other", "Financial Situation"),
    POSITIVE_RESIDENTIAL_TURN_OFF_UC53_TC232("Other", "Financial Situation");

    private final String reason;
    private final String subReason;

    // Constructor
    TurnOffReason(String reason, String subReason) {
        this.reason = reason;
        this.subReason = subReason;
    }

}

