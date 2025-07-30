package com.gng.api.constants;
import lombok.Getter;

public class GlobalEnums {

    @Getter
    public enum CustomerType {
        RESIDENTIAL("RS"),
        COMMERCIAL("CM"),
        SENIOR("SR"),
        INVALID("IN");

        private final String value;

        CustomerType(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum TransactionType {
        TURN_ON("TNON"),
        TURN_OFF("TOFF"),
        MKSW("MKSW");

        private final String value;

        TransactionType(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum CreditCheckOption {
        YES("Yes"),
        NO("No"),
        SERVICE_TRANSFER("ServTransfer"),
        COMMERCIAL_CREDIT_CHECK_REQUIRED("Comm"),
        MULTIPLE_PREMISES_OWNER("Mult"),
        CREDIT_CHECK_NOT_REQUIRED("Skip"),
        ACN_LANDLORD("ACN Landlord");

        private final String value;

        CreditCheckOption(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum EnrollmentSource {
        ALLCONNECT("ALLCONNECT"),
        CIM_BUILDER_TURN_ON("CMITURNON"),
        CORRESPONDENCE("CORRSP"),
        EMAIL("EMAIL"),
        ENERGYSHOP("ENERGYSHOP"),
        FAX("FAX"),
        GEORGIAGASSAVINGS("GAGASSAVE"),
        GNGHUB("GNGHUB"),
        MAIL("MAIL"),
        MOOVEGURU("MOOVEGURU"),
        ONESOURCE("ONESOURCE"),
        PHONECALL("PHONECALL"),
        VIVINT("VIVINT"),
        WEB("WEB");

        private final String value;

        EnrollmentSource(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum PromotionCode {
        DEALS("DEALS"),
        SAVE100("SAVE100"),
        AAA("AAA");

        private final String value;

        PromotionCode(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum EnrollMentStatus {
        COMPLETE("CE"),
        DEPOSIT_PAID("DP"),
        BAD_DEBT("BD"),
        PC("PC");

        private final String value;

        EnrollMentStatus(String value) {
            this.value = value;
        }
    }
}
