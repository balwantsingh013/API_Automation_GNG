package com.gng.api.constants;
import lombok.Getter;

public class GlobalEnums {

    @Getter
    public enum MarketingPromotionCodes {
        PROMOTION_CODE_COMMERCIAL("TURNON100"),
        PROMOTION_CODE_FOR_EXISTING_CUSTOMER("RENEW12"),
        EXPIRED_MARKETING_PROMOTION_CODE("VIPJUL17"),
        PROMOTION_CODE_RESIDENTIAL("APARTMENT SPECIAL"),
        PROMOTION_CODE_GREEN_LIFE("GREEN125");

        private final String value;

        MarketingPromotionCodes(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum BillingUnitType {
        KEY("KEY");

        private final String value;

        BillingUnitType(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum AddressType {
        STREET("S"),
        RURAL("R"),
        POBOX("P");

        private final String value;

        AddressType(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum ForwardingAddressType {
        CURRENT_ADDRESS("CA"),
        NEW_ADDRESS("NA"),
        INVALID("Z");

        private final String value;

        ForwardingAddressType(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum TenantOrLandlord {
        TENANT("T"),
        LANDLORD("L");

        private final String value;

        TenantOrLandlord(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum ACNorNACN {
        ACN("ACN"),
        NACN("NACN");

        private final String value;

        ACNorNACN(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum InvalidValues {
        DUPLICATE_REQUEST_ID("123"),
        INVALID_LOGIN_ID("FAKE"),
        TRANSACTION_TYPE_WITH_WHITESPACE("DF HJ"),
        INVALID_CUSTOMER_TYPE("AA"),
        INNVALID_MARKETING_PROMOTION_CODE("AAA"),
        INVALID_ADDRESS_TYPE("C"),
        INVALID_STREET_PREDIRECTION("WN"),
        INVALID_STREET_SUFFIX("CIRC"),
        INVALID_STREET_POST_DIRECTION("WS"),
        INVALID_BILLING_ADDRESS_TYPE("C"),
        INVALID_BILLING_PRE_DIRECTION("EN"),
        INVALID_BILLING_UNIT_TYPE("KE"),
        INVALID_BILLING_STATE_CODE("BEE"),
        INVALID_PREMISES_COUNTY_CODE("T"),
        INVALID_ZIP_CODE("31111"),
        INVALID_PREMISE_STATE_CODE("YU"),
        INVALID_GENERATION_CODE("AB"),
        INVALID_CREDIT_CHECK_OPTION("MAYBE"),
        INVALID_INITIAL_CREDIT_CHECK_CUSTOMER_CODE("1234588"),
        INVALID_WORK_PHONE_TYPE("C"),
        INVALID_HOME_PHONE_TYPE("B"),
        INVALID_TENANT_LANDLORD("P"),
        DUPLICATE_MARKETER_REFERENCE_NUMBER("175481815862"),
        INVALID_ACCOUNT_NOTE("BOGUS"),
        INVALID_TRANSACTION_TYPE("TOBO"),
        INVALID_CUSTOMER_CODE("0"),
        INVALID_PREMISES_CODE("0");

        private final String value;

        InvalidValues(String value) {
            this.value = value;
        }
    }

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
        TRANSFER("TRAN"),
        MKSW("MKSW"),
        INVALID("TURN");

        private final String value;

        TransactionType(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum ScenarioDescriptions {
        Missing_request_id_TC_106("Verify that if the 'requestID' parameter is missing in the SaveUnenrollment method request, the web method will return success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID' "),
        Invalid_length_of_request_id_TC_107("Verify that if the provided 'requestID' paramter length is larger than 32 characters in the SaveUnenrollment method request, the web method will return success = false, ErrorCode = 10002, ErrorMessage = ‘Invalid Request ID’"),
        Duplicate_request_id_TC_108("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the SaveUnenrollment method request, the web method will return success = false, ErrorCode = 10003, ErrorMessage = ‘Duplicate Request ID’"),
        TC_423("Verify that the SaveEnrollment method returns a successful response (success = true) and completes the residential new Turn On enrollment by updating the enrollment status to 'UDCS' and saving the provided information in the relevant Banner tables [per technical design] when the input includes valid values for all the mandatory details, such as transactionID returned from the getEligiblePlansAndOffers; transactionType = TNON; corresponding customerCode and premisesCode; planCode is not VML/PRP/PBG; promotionCode is not null; enrollmentStatus = CE (Complete Enrollment); paymentConfirmationNumber is null; billingPlan = 'R' (Regular); estimatedBudgetAmount is null; customerRequestedServiceDate = <YYYYMMDD>; seasonalSavingsProgramResult is null; splitConnectionFeeIndicator = true; aglcAccountNumber is not null; aglcServiceOrderNumber is not null; notes is not null."),
        GET_DEFAULT_PLANS_AND_OFFERS_TC_122("Verify the GetDefaultPlansAndOffers method for a residential turn-on (no Promotion Code) returns applicable price plans/details and associated offers/details."),

        Missing_request_id_TC_1("Verify that if the 'requestID' parameter is missing in the ValidateUsername request, the web method will return success = false, ErrorCode = 10001, ErrorMessage = ‘Missing Request ID’"),
        Length_of_request_id_larger_than_32_TC_2("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the ValidateUsername request, the web method will return success = false, ErrorCode = 10002, ErrorMessage = ‘Invalid Request ID’"),
        Duplicate_request_id_TC_3("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the ValidateUsername request, the web method will return success = false, ErrorCode = 10003, ErrorMessage = ‘Duplicate Request ID’"),
        Missing_username_TC_4("Verify that if the 'username' parameter is missing in the ValidateUsername request, the web method will return success = false, ErrorCode = 10349, ErrorMessage = ‘Missing Username’"),
        Length_of_username_smaller_than_5_TC_5("Verify that if the provided 'username' parameter length is smaller than 5 characters in the ValidateUsername request, the web method will return success = false, ErrorCode = 10351, ErrorMessage = ‘Invalid Username Length’"),
        Length_of_username_greater_than_15_TC_6("Verify that if the provided 'username' parameter length is larger than 15 characters in the ValidateUsername request, the web method will return success = false, ErrorCode = 10351, ErrorMessage = ‘Invalid Username Length’"),
        Username_not_alphanumeric_TC_7("Verify that if the provided 'username' parameter is not alphanumeric in the ValidateUsername request, the web method will return success = false, ErrorCode = 10353, ErrorMessage = ‘Invalid Username Format’"),
        Username_does_not_exist_TC_8("Verify that in the ValidateUsername method the provided 'username' does not exist in MariaDb, the web method will return success = true, ErrorCode = 0, and usernameStatus = 'AVAILABLE'"),
        Active_username_exists_TC_9("Verify that in the ValidateUsername request the provided 'username' does exist in MariaDb and is 'Active', the web method will return success = true, ErrorCode = 0, and usernameStatus = 'ACTIVE'"),
        Inactive_username_exists_TC_10("Verify that in the ValidateUsername request the provided 'username' does exist in MariaDb and is 'Inactive', the web method will return success = true, ErrorCode = 0, and usernameStatus = 'INACTIVE'");

        private final String value;

        ScenarioDescriptions(String value) {
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
        CIM_BUILDER_TURN_ON("CIM BUILDER TURN ON"),
        CORRESPONDENCE("CORRESPONDENCE"),
        EMAIL("EMAIL"),
        ENERGYSHOP("ENERGYSHOP"),
        FAX("FAX"),
        GEORGIAGASSAVINGS("GEORGIAGASSAVINGS"),
        GNGHUB("GNGHUB"),
        MAIL("MAIL"),
        MOOVEGURU("MOOVEGURU"),
        ONESOURCE("ONESOURCE"),
        PHONECALL("PHONE CALL"),
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
        AAA("AAA"),
        ADVANTAGE("ADVANTAGE");

        private final String value;

        PromotionCode(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum WorkPhoneType {
        HOME("home"),
        BUSINESS("BUSINESS");

        private final String value;

        WorkPhoneType(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum HomePhoneType {
        MOBILE("M"),
        LANDLINE("L");

        private final String value;

        HomePhoneType(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum EnrollMentStatus {
        COMPLETE("CE"),
        DEPOSIT_PAID("DP"),
        BILL_DEPOSIT("BD"),
        PAYMENT_COMPLETE("PC"),
        SAVE_INCOMPLETE("SI"),
        DEPOSIT_REQUIRED("DR"),
        PREPAY_REQUIRED("PR"),
        REFUSED_PREPAY("RP"),
        REFUSED_DEPOSIT("RD"),
        SAVE_FOR_FALL_SSP("SF"),
        DEPOSIT_BILLED("DB"),
        CANCEL_PREPAY("CP");

        private final String value;

        EnrollMentStatus(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum EnrollMentState {
        UDCS("UDCS"),
        CRDS("CRDS"),
        INCL("INCL");

        private final String value;

        EnrollMentState(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum PlanCode {
        PRP("PRP"),
        PGB("PGB"),
        RGB("RGB"),
        MVS("MVS"),
        GPP("GPP");

        private final String value;
        PlanCode(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum AccountStatus {
        ACTIVE("A"),
        INACTIVE("I"),
        NOT_ACTIVE_YET("N"),
        FINAL_ACCOUNT("F");

        private final String value;
        AccountStatus(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum PlanTypeIndicator {
        VARIABLE_SELECT("V"),
        GUARANTEED_BILL("G"),
        DEFAULTED("F");

        private final String value;
        PlanTypeIndicator(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum SeasonSavingsProgramResult {
        ENROLLED("ENROLLED");

        private final String value;
        SeasonSavingsProgramResult(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum CurrentMarketerCode {
        FIRE("FIRE");

        private final String value;
        CurrentMarketerCode(String value) {
            this.value = value;
        }
    }
}
