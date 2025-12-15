package com.gng.api.constants;
import lombok.Getter;

public class GlobalEnums {

    @Getter
    public enum MarketingPromotionCodes {
        PROMOTION_CODE_COMMERCIAL("TURNON100"),
        PROMOTION_CODE_FOR_EXISTING_CUSTOMER("RENEW12"),
        EXPIRED_MARKETING_PROMOTION_CODE("VIPJUL17"),
        PROMOTION_CODE_RESIDENTIAL("APARTMENT SPECIAL"),
        PROMOTION_CODE_GREEN_LIFE("GREEN125"),
        PROMOTION_CODE_DEALS("DEALS"),
        PROMOTION_CODE_FIX_10_DOLLARS_FOR_12_MONTHS("FIX 10 DOLLARS FOR 12 MONTHS");

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
        INVALID_PASSWORD("dummy$01"),
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
        INVALID_ZIP_9_DIGIT("1234-12345"),
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
        INVALID_PREMISES_CODE("0"),
        INVALID_ENROLLMENT_STATE("SEMT"),
        INVALID_CURRENT_MARKETER_CODE("JUMP");

        private final String value;

        InvalidValues(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum Notes {
        TESTING("TESTING, 1, 2, 3");

        private final String value;

        Notes(String value) {
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
        INVALID("TURN"),
        METER_SET("SETM");

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

        TC_1__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the ValidateUsername request, the web method will return success = false, ErrorCode = 10001, ErrorMessage = ‘Missing Request ID’"),
        TC_2__Negative__Invalid_Request_ID__Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the ValidateUsername request, the web method will return success = false, ErrorCode = 10002, ErrorMessage = ‘Invalid Request ID’"),
        TC_3__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the ValidateUsername request, the web method will return success = false, ErrorCode = 10003, ErrorMessage = ‘Duplicate Request ID’"),
        TC_4__Negative__Missing_Username("Verify that if the 'username' parameter is missing in the ValidateUsername request, the web method will return success = false, ErrorCode = 10349, ErrorMessage = ‘Missing Username’"),
        TC_5__Negative__Invalid_Username_format__Length___Too_Short____("Verify that if the provided 'username' parameter length is smaller than 5 characters in the ValidateUsername request, the web method will return success = false, ErrorCode = 10351, ErrorMessage = ‘Invalid Username Length’"),
        TC_6__Negative__Invalid_Username_format__Length___Too_Long____("Verify that if the provided 'username' parameter length is larger than 15 characters in the ValidateUsername request, the web method will return success = false, ErrorCode = 10351, ErrorMessage = ‘Invalid Username Length’"),
        TC_7__Negative__Invalid_Username_format__Alphanumeric("Verify that if the provided 'username' parameter is not alphanumeric in the ValidateUsername request, the web method will return success = false, ErrorCode = 10353, ErrorMessage = ‘Invalid Username Format’"),
        TC_8__Positive__Username_Available("Verify that in the ValidateUsername method request if the provided 'username' does exist in MariaDb 'users' table and active value of 1 and deleted 0 and domain_id is NOT 2, the web method will return success = true, ErrorCode = 0, and usernameStatus = 'AVAILABLE'"),
        TC_9__Positive__Username_Available("Verify that in the ValidateUsername method request if the provided 'username' does not exist in MariaDb, the web method will return success = true, ErrorCode = 0, and usernameStatus = 'AVAILABLE'"),
        TC_10__Positive__Username_Active__users_table("Verify that in the ValidateUsername method request if the provided 'username' does exist in MariaDb 'users' table and active value of 1 and deleted 0 and domain_id is 2, the web method will return success = true, ErrorCode = 0, and usernameStatus = 'ACTIVE' "),
        TC_11__Positive__Username_Active__custadv_pending_registrations_table("Verify that in the ValidateUsername method request if the provided 'username' does exist in MariaDb 'custadv_pending_registrations' table, the web method will return success = true, ErrorCode = 0, and usernameStatus = 'ACTIVE' "),
        TC_12__Positive__Username_Inactive("Verify that in the ValidateUsername method request if the provided 'username' does exist in MariaDb and is 'Inactive', the web method will return success = true, ErrorCode = 0, and usernameStatus = 'INACTIVE' "),
        TC_13__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the UpdatePassword method request, the web method will return success = false, ErrorCode = 10001, ErrorMessage = ‘Missing Request ID’"),
        TC_14__Negative__Invalid_Request_ID__Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the UpdatePassword method request, the web method will return success = false, ErrorCode = 10002, ErrorMessage = ‘Invalid Request ID’"),
        TC_15__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the UpdatePassword method request, the web method will return success = false, ErrorCode = 10003, ErrorMessage = ‘Duplicate Request ID’"),
        TC_16__Negative__Missing_Username("Verify that if the 'username' parameter is missing in the UpdatePassword method request, the web method will return success = false, ErrorCode = 10349, ErrorMessage = ‘Missing Username’"),
        TC_17__Negative__Invalid_Username_format__Length___Too_Short____("Verify that if the provided 'username' parameter length is smaller than 5 characters in the UpdatePassword method request, the web method will return success = false, ErrorCode = 10351, ErrorMessage = ‘Invalid Username Length’"),
        TC_18__Negative__Invalid_Username_format__Length___Too_Long____("Verify that if the provided 'username' parameter length is larger than 15 characters in the UpdatePassword method request, the web method will return success = false, ErrorCode = 10351, ErrorMessage = ‘Invalid Username Length’"),
        TC_19__Negative__Invalid_Username_format__Alphanumeric("Verify that if the provided 'username' parameter is not alphanumeric in the UpdatePassword method request, the web method will return success = false, ErrorCode = 10353, ErrorMessage = ‘Invalid Username Format’"),
        TC_20__Negative__Invalid_Username__Not_Found("Verify that if the provided 'username' parameter does not exist and is used in the UpdatePassword method request, the web method will return success = false, ErrorCode = 10357, ErrorMessage = ‘Username Not Found’"),
        TC_21__Negative__Invalid_Username__Inactive("Verify that if the provided 'username' parameter value is inactive and is used in the UpdatePassword method request, the web method will return success = false, ErrorCode = 10355, ErrorMessage = ‘Inactive Username’"),
        TC_22__Negative__Missing_Password("Verify that if the provided 'password' parameter value is missing in the UpdatePassword method request, the web method will return success = false, ErrorCode = 10113, ErrorMessage = ‘Missing Password’"),
        TC_23__Negative__Invalid_Password_Format__Length___Too_Short____("Verify that if the provided 'password' parameter value length is shorter than 8 characters in the UpdatePassword method request, the web method will return success = false, ErrorCode = 10157, ErrorMessage = ‘Invalid Password Format’"),
        TC_24__Negative__Invalid_Password_Format__Length___Too_Long____("Verify that if the provided 'password' parameter value length is longer than 64 characters in the UpdatePassword method request, the web method will return success = false, ErrorCode = 10157, ErrorMessage = ‘Invalid Password Format’"),
        TC_25__Negative__Invalid_Password__Reused_Password("Verify that if the provided 'password' parameter value matches the current password in the UpdatePassword method request, the web method will return success = false, ErrorCode = 10359, ErrorMessage = ‘Invalid Password’"),
        TC_26__Positive__Password__Updated("Verify that if valid 'username' and 'password' parameters are provided in the UpdatePassword method request, the web method will return success = true, ErrorCode = 0"),

        TC_53__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the SetAccountNickname method request, the web method will return success = false, ErrorCode = 10001, ErrorMessage = ‘Missing Request ID’"),
        TC_54__Negative__Invalid_Request_ID__Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the SetAccountNickname method request, the web method will return success = false, ErrorCode = 10002, ErrorMessage = ‘Invalid Request ID’"),
        TC_55__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the SetAccountNickname method request, the web method will return success = false, ErrorCode = 10003, ErrorMessage = ‘Duplicate Request ID’"),
        TC_56__Negative__Missing_customerCode("Verify that if the 'customerCode' value is missing in the SetAccountNickname method request, the web method will return success = false, ErrorCode = 10011, ErrorMessage = ‘Missing Customer Code’"),
        TC_57__Negative__Invalid_customerCode_Length("Verify that if the 'customerCode' length is larger than 9 numeric characters in the SetAccountNickname method request, the web method will return success = false, ErrorCode = 10015, ErrorMessage = ‘Invalid Customer Code Format’"),
        TC_58__Negative__Invalid_customerCode_Format("Verify that if the 'customerCode' value is not a string in the SetAccountNickname method request, the web method will return success = false, ErrorCode = 10015, ErrorMessage = ‘Invalid Customer Code Format’"),
        TC_59__Negative__Invalid_customerCode("Verify that if the provided 'customerCode' in the SetAccountNickname method request doesn't exist in the UCBCUST table in Banner, the web method will return success = false, ErrorCode = 40015, ErrorMessage = ‘Invalid Account Number’"),
        TC_60__Negative__Missing_premisesCode("Verify that if the 'premisesCode' value is missing in the SetAccountNickname method request, the web method will return success = false, ErrorCode = 10013, ErrorMessage = ‘Missing Premisses Code’"),
        TC_61__Negative__Invalid_premisesCode_Length("Verify that if the 'premisesCode' length is larger than 7 numeric characters in the SetAccountNickname method request, the web method will return success = false, ErrorCode = 10005, ErrorMessage = ‘Invalid Premises Code Format’"),
        TC_62__Negative__Invalid_premisesCode_Format("Verify that if the 'premisesCode' value is not a string in the SetAccountNickname method request, the web method will return success = false, ErrorCode = 10005, ErrorMessage = ‘Invalid Premises Code Format’"),
        TC_63__Negative__Invalid_premisesCode("Verify that if the provided 'premisesCode' in the SetAccountNickname method request doesn't exist in the UCBPREM table in Banner, the web method will return success = false, ErrorCode = 40015, ErrorMessage = ‘Invalid Account Number’"),
        TC_64__Negative__Nickname_Not_Allowed("Verify that if the 'customerCode' and 'premisesCode' for a Banner account are in a 'New' status and a 'nickname' parameter is provided, the web method will return success = false, ErrorCode = 40215, ErrorMessage = ‘Nickname not allowed for new account’"),
        TC_65__Negative__Nickname_Already_Exists("Verify that if the 'customerCode' and 'premisesCode' for a Banner account are NOT in a 'New' status and a nickname is already set, the web method will return success = false, ErrorCode = 40217, ErrorMessage = ‘Nickname already exists for the account’"),
        TC_66__Negative__Nickname_Missing("Verify that if the 'customerCode' and 'premisesCode' for a Banner account are NOT in a 'New' status and no nickname is provided, the web method will return success = false, ErrorCode = 40219, ErrorMessage = ‘Nickname does not exist in the request’"),
        TC_67__Positive__Nickname_Set("Verify that if all required fields are set in the SetAccountNickname method request for a Banner account NOT in a 'New' status where a nickname is set, the web method will return success = true, ErrorCode = 0 and a new nickname is associated to the Banner account");


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
        CANCEL_PREPAY("CP"),
        CREDIT_FREEZE("BADC"),
        PENDING_REVIEW("PVER");

        private final String value;

        EnrollMentStatus(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum EnrollMentState {
        UDCS("UDCS"),
        CRDS("CRDS"),
        INCL("INCL"),
        PENDING_REVIEW("PVER"),
        BAD_CREDIT("BADC");

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
        GPP("GPP"),
        TRD("TRD"),
        VML("VML"),
        GB6("GB6"),
        CGB("CGB"),
        CVS("CVS"),
        CSV("CSV"),
        CCV("CCV"),
        M18("18M"),
        RF6("RF6"),
        MI("MI"),
        CMI("CMI"),
        CFM("CFM"),
        M24("24M"),
        B24("24B"),
        FIX("FIX");

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

    @Getter
    public enum BillingPlan {
        BUDGET("B");
        private final String value;
        BillingPlan(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum CreditScoreStatus {
        STATUS_TEXT("TEXT"),
        REFUSE("REFU"),
        NUMBER("NUMR"),
        ACNL("ACNL");

        private final String value;
        CreditScoreStatus(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum CreditScoreText {
        MATCH_CODE_B("NO RECORD FOUND. MATCH CODE B."),
        VERIFY_ID("NO RECORD FOUND.NO EMCS.VERIFY ID."),
        NOT_APPLICABLE("NOT APPLICABLE"),
        MATCH_CODE_C("MATCH CODE C.");

        private final String value;
        CreditScoreText(String value) {
            this.value = value;
        }
    }
}
