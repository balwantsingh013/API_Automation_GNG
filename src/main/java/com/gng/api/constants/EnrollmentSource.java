package com.gng.api.constants;

import lombok.Getter;

@Getter
public enum EnrollmentSource {
    ALL_CONNECT("ALLCONNECT"),
    CIM_BUILDER_TURN_ON("CIMBUILDERTURNON"),
    CORRESPONDENCE("CORRESPONDENCE"),
    EMAIL("EMAIL"),
    ENERGY_SHOP("ENERGYSHOP"),
    FAX("FAX"),
    GEORGIA_GAS_SAVINGS("GEORGIAGASSAVINGS"),
    GNG_HUB("GNGHUB"),
    MAIL("MAIL"),
    MOOVE_GURU("MOOVEGURU"),
    ONE_SOURCE("ONESOURCE"),
    PHONE_CALL("PHONECALL"),
    VIV_INT("VIVINT"),
    WEB("WEB");

    private final String value;

    EnrollmentSource(String value) {
        this.value = value;
    }
}
