package com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment;

import lombok.Getter;

@Getter
public enum TurnOffReason {

    SEASONAL_OR_HEAT_ONLY("Seasonal or Heat Only", ""),
    SEASONAL_OR_HEAT_SERVICE_TRANSFER("Seasonal or Heat Only", "Service Transfer"),
    SEASONAL_OR_HEAT_SERVICE_TERRITORY("Seasonal or Heat Only", "Outside AGLC Territory/Outside Georgia"),

    OTHER_MILITARY("Other", "Military"),
    OTHER_MILITARY_ETC_WAIVED("Other", "Military - ETC Waived"),
    REAP_REALTOR_INSPECTION("REAP/Realtor Inspection", ""),
    REAP_REALTOR_INSPECTION_SERVICE_TRANSFER("REAP/Realtor Inspection", "Service transfer"),
    REAP_REALTOR_INSPECTION_TERRITORY("REAP/Realtor Inspection", "Outside AGLC Territory/Outside Georgia"),
    HOUSEHOLD_ACCOUNT_CHANGE("Household Account Change", ""),
    HOUSEHOLD_ACCOUNT_SERVICE_TRANSFER("Household Account Change", "Service Transfer"),
    HOUSEHOLD_ACCOUNT_TERRITORY("Household Account Change", "Outside AGLC Territory/Outside Georgia"),


    //Other
    OTHER_FINANCIAL_SITUATION("Other", "Financial Situation"),
    OTHER_REGULATED_PROVIDER("Other", "Regulated Provider"),
    OTHER_DECEASED("Other", "Deceased"),
    OTHER_OUTSIDE_AGLC_TERRITORY_ETC_WAIVED("Other","Outside AGLC Territory/Outside Georgia – ETC Waived"),
    OTHER_DECEASED_ETC_WAIVED("Other", "Deceased - ETC Waived"),
    OTHER_REGULATED_PROVIDER_ETC_WAIVED("Other", "Regulated Provider - ETC Waived"),
    OTHER_RENOVATION_ELECTRIC_CONVERSION("Other", "Renovation/Electric Conversion"),
    OTHER_OUTSIDE_TERRITORY("Other", "Outside AGLC Territory/Outside Georgia"),
    OTHER_OUTSIDE_POOL_GROUP("Other", "Outside Pool (Delivery) Group"),
    OTHER_SERVICE_TRANSFER("Other", "Service Transfer"),
    OTHER_WITHIN_POOL_NOT_GNG("Other", "Within Pool Group but Not Staying with GNG"),


    //Moving
    MOVING_NOT_STAYING_WITH_GNG("Moving", "Within Pool Group but Not Staying with GNG"),
    MOVING_OUTSIDE_AGLC("Moving", "Outside AGLC Territory/Outside Georgia"),
    MOVING_SERVICE_TRANSFER("Moving", "Service Transfer"),
    MOVING_OUTSIDE_POOL_GROUP_ETC_WAIVED("Moving","Outside Pool (Delivery) Group - ETC Waived"),
    MOVING_SERVICE_TRANSFER_ETC_WAIVED("Moving","Service Transfer - ETC Waived"),
    MOVING_OUTSIDE_AGLC_TERRITORY_ETC_WAIVED("Moving","Outside AGLC Territory/Outside Georgia – ETC Waived"),
    MOVING_OUTSIDE_POOL_GROUP("Moving","Outside Pool (Delivery) Group"),
    MOVING_RENOVATION_ELECTRIC("Moving","Renovation/Electric Conversion\""),
    MOVING_REGULATED_PROVIDER("Moving", "Regulated Provider"),
    MOVING_MILITARY("Moving", "Military"),
    MOVING_DECEASED("Moving", "Deceased"),
    MOVING_FINANCIAL_SITUATION("Moving", "Financial Situation"),

    INVALID("INVALID-REASON", "Invalid Sub Reason");


    private final String reason;
    private final String subReason;

    TurnOffReason(String reason, String subReason) {
        this.reason = reason;
        this.subReason = subReason;
    }
}

