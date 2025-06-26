package com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment;

import lombok.Getter;

@Getter
public enum TurnOffReason {

    SEASONAL_OR_HEAT_ONLY("Seasonal or Heat Only", ""),
    MOVING_OUTSIDE_AGLC("Moving", "Outside AGLC Territory/Outside Georgia"),
    OTHER_MILITARY("Other", "Military"),
    OTHER_MILITARY_ETC_WAIVED("Other", "Military - ETC Waived"),
    REAP_REALTOR_INSPECTION("REAP/Realtor Inspection", ""),
    HOUSEHOLD_ACCOUNT_CHANGE("Household Account Change", ""),
    OTHER_FINANCIAL_SITUATION("Other", "Financial Situation"),
    MOVING_NOT_STAYING_WITH_GNG("Moving", "Within Pool Group but Not Staying with GNG"),
    OTHER_REGULATED_PROVIDER("Other", "Regulated Provider"),
    OTHER_DECEASED("Other", "Deceased"),
    MOVING_SERVICE_TRANSFER("Moving", "Service Transfer"),
    MOVING_OUTSIDE_POOL_GROUP_ETC_WAIVED("Moving","Outside Pool (Delivery) Group - ETC Waived"),
    MOVING_SERVICE_TRANSFER_ETC_WAIVED("Moving","Service Transfer - ETC Waived"),
    MOVING_OUTSIDE_AGLC_TERRITORY_ETC_WAIVED("Moving","Outside AGLC Territory/Outside Georgia – ETC Waived"),
    OTHER_DECEASED_ETC_WAIVED("Other", "Deceased - ETC Waived");

    private final String reason;
    private final String subReason;

    TurnOffReason(String reason, String subReason) {
        this.reason = reason;
        this.subReason = subReason;
    }
}

