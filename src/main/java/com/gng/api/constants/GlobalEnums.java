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

        TC_1__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the ValidateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = ‘Missing Request ID’"),
        TC_2__Negative__Invalid_Request_ID__Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the ValidateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = ‘Invalid Request ID’"),
        TC_3__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the ValidateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = ‘Duplicate Request ID’"),
        TC_4__Negative__Missing_Username("Verify that if the 'username' parameter is missing in the ValidateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10349, ErrorMessage = ‘Missing Username’"),
        TC_5__Negative__Invalid_Username_format__Length___Too_Short____("Verify that if the provided 'username' parameter length is smaller than 5 characters in the ValidateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10351, ErrorMessage = ‘Invalid Username Length’"),
        TC_6__Negative__Invalid_Username_format__Length___Too_Long____("Verify that if the provided 'username' parameter length is larger than 15 characters in the ValidateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10351, ErrorMessage = ‘Invalid Username Length’"),
        TC_7__Negative__Invalid_Username_format__Alphanumeric("Verify that if the provided 'username' parameter is not alphanumeric in the ValidateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10353, ErrorMessage = ‘Invalid Username Format’"),
        TC_8__Positive__Username_Available("Verify that in the ValidateUsername endpoint request if the provided 'username' does exist in MariaDb 'users' table and active value of 1 and deleted 0 and domain_id is NOT 2, the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'AVAILABLE'"),
        TC_9__Positive__Username_Available("Verify that in the ValidateUsername endpoint request if the provided 'username' does not exist in MariaDb, the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'AVAILABLE'"),
        TC_10__Positive__Username_Active__users_table("Verify that in the ValidateUsername endpoint request if the provided 'username' does exist in MariaDb 'users' table and active value of 1 and deleted 0 and domain_id is 2, the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'ACTIVE' "),
        TC_11__Positive__Username_Active__custadv_pending_registrations_table("Verify that in the ValidateUsername endpoint request if the provided 'username' does exist in MariaDb 'custadv_pending_registrations' table, the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'ACTIVE' "),
        TC_11__Positive__Username_Inactive("Verify that in the ValidateUsername endpoint request if the provided 'username' does exist in MariaDb and is 'Inactive', the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'INACTIVE' "),
        TC_13__Positive__LoginID_Saved("Verify that if a value is provided for the optional 'loginID' parameter in the ValidateUsername endpoint request and the rest of the request is well formed the endpoint response will return success = true, ErrorCode = 0, and loginID value is saved/stored in the database."),
        TC_12__Negative__Data_null_for_failure("Verify that if ValidateUsername endpoint request fails due to any error response will return success = false, ErrorCode != 0, and data= null"),

        TC_14__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = ‘Missing Request ID’"),
        TC_15__Negative__Invalid_Request_ID__Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = ‘Invalid Request ID’"),
        TC_16__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = ‘Duplicate Request ID’"),
        TC_17__Negative__Missing_Username("Verify that if the 'username' parameter is missing in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10349, ErrorMessage = ‘Missing Username’"),
        TC_18__Negative__Invalid_Username_format__Length___Too_Short____("Verify that if the provided 'username' parameter length is smaller than 5 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10351, ErrorMessage = ‘Invalid Username Length’"),
        TC_19__Negative__Invalid_Username_format__Length___Too_Long____("Verify that if the provided 'username' parameter length is larger than 15 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10351, ErrorMessage = ‘Invalid Username Length’"),
        TC_20__Negative__Invalid_Username_format__Alphanumeric("Verify that if the provided 'username' parameter is not alphanumeric in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10353, ErrorMessage = ‘Invalid Username Format’"),
        TC_21__Negative__Invalid_Username__Not_Found("Verify that if the provided 'username' parameter does not exist and is used in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10357, ErrorMessage = ‘Username Not Found’"),
        TC_22__Negative__Invalid_Username__Inactive("Verify that if the provided 'username' parameter value is inactive and is used in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10355, ErrorMessage = ‘Inactive Username’"),
        TC_23__Negative__Missing_Password("Verify that if the provided 'password' parameter value is missing in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10113, ErrorMessage = ‘Missing Password’"),
        TC_24__Negative__Invalid_Password_Format__Length___Too_Short____("Verify that if the provided 'password' parameter value length is shorter than 8 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10157, ErrorMessage = ‘Invalid Password Length. The password must be between 8 and 64 characters.’"),
        TC_25__Negative__Invalid_Password_Format__Length___Too_Long____("Verify that if the provided 'password' parameter value length is longer than 64 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10157, ErrorMessage = ‘Invalid Password Length. The password must be between 8 and 64 characters.’"),
        TC_26__Negative__Invalid_Password__Reused_Password("Verify that if the provided 'password' parameter value matches the current password in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10359, ErrorMessage = ‘Invalid Password’"),
        TC_27__Positive__Password__Updated("Verify that if valid 'username' and 'password' parameters are provided in the UpdatePassword endpoint request, the endpoint response will return success = true, ErrorCode = 0"),
        TC_28__Positive__LoginID_Saved("Verify that if a value is provided for the optional 'loginID' parameter in the UpdatePassword endpoint request and the rest of the request is well formed the endpoint response will return success = true, ErrorCode = 0, and loginID value is saved/stored in the database."),

        TC_29__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = ‘Missing Request ID’"),
        TC_30__Negative__Invalid_Request_ID__Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = ‘Invalid Request ID’"),
        TC_31__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = ‘Duplicate Request ID’"),
        TC_32__Negative__Missing_Username("Verify that if the 'username' parameter is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10349, ErrorMessage = ‘Missing Username’"),
        TC_33__Negative__Invalid_Username__Inactive("Verify that if the provided 'username' parameter value is inactive and is used in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10355, ErrorMessage = ‘Inactive Username’"),
        TC_34__Negative__Invalid_Username_format__Length___Too_Short____("Verify that if the provided 'username' parameter length is smaller than 5 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10351, ErrorMessage = ‘Invalid Username Length’"),
        TC_35__Negative__Invalid_Username_format__Length___Too_Long____("Verify that if the provided 'username' parameter length is larger than 15 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10351, ErrorMessage = ‘Invalid Username Length’"),
        TC_36__Negative__Invalid_Username_format__Alphanumeric("Verify that if the provided 'username' parameter is not alphanumeric in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10353, ErrorMessage = ‘Invalid Username Format’"),
        TC_37__Negative__Invalid_Username__Not_Found("Verify that if the provided 'username' parameter does not exist in MariaDb and is used in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10357, ErrorMessage = ‘Username Not Found’"),
        TC_38__Negative__Missing_Password("Verify that if the provided 'password' parameter value is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10113, ErrorMessage = ‘Missing Password’"),
        TC_39__Negative__Invalid_Password_Format__Length___Too_Short____("Verify that if the provided 'password' parameter value length is shorter than 8 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10157, ErrorMessage = ‘Invalid Password Format’"),
        TC_40__Negative__Invalid_Password_Format__Length___Too_Long____("Verify that if the provided 'password' parameter value length is longer than 64 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10157, ErrorMessage = ‘Invalid Password Format’"),
        TC_41__Negative__Invalid_Password_Format__Not_String("Verify that if the provided 'password' parameter value is not a string in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10157, ErrorMessage = ‘Invalid Password Format’"),
        TC_42__Negative__Invalid_Password_Format__Policy_Violation("Verify that if the provided 'password' parameter value does not conform to the password policy(s) in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10157, ErrorMessage = ‘Invalid Password Format’"),
        TC_43__Negative__Account_Already_Linked("Verify that if the provided 'customerCode', 'premisesCode' and 'username' in the UpdateUsername endpoint request and the account is already linked to a username, the endpoint response will return success = false, ErrorCode = 10361, ErrorMessage = ‘The account is already linked to a username’"),
        TC_44__Negative__Password_Mismatch("Verify that if the provided 'password' and 'username' are in the UpdateUsername endpoint request and the password does not match what is in the database, the endpoint response will return success = false, ErrorCode = 10363, ErrorMessage = ‘The password doesn’t match the username’"),
        TC_45__Negative__Missing_CustomerCode("Verify that if the 'customerCode' value is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10011, ErrorMessage = ‘Missing Customer Code’"),
        TC_46__Negative__Invalid_CustomerCode_Format__Too_Long("Verify that if the 'customerCode' length is larger than 9 numeric characters in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = ‘Invalid Customer Code Format’"),
        TC_47__Negative__Invalid_CustomerCode_Format__Not_String("Verify that if the 'customerCode' value is not a string in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = ‘Invalid Customer Code Format’"),
        TC_48__Negative__Invalid_CustomerCode__Not_Found("Verify that if the provided 'customerCode' in the UpdateUsername endpoint request doesn't exist in the UCBCUST table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = ‘Invalid Account Number’"),
        TC_49__Negative__Missing_PremisesCode("Verify that if the 'premisesCode' value is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10013, ErrorMessage = ‘Missing Premises Code’"),
        TC_50__Negative__Invalid_PremisesCode_Format__Too_Long("Verify that if the 'premisesCode' length is larger than 7 numeric characters in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = ‘Invalid Premises Code Format’"),
        TC_51__Negative__Invalid_PremisesCode_Format__Not_String("Verify that if the 'premisesCode' value is not a string in the UpdateUsername endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = ‘Invalid Premises Code Format’"),
        TC_52__Negative__Invalid_PremisesCode__Not_Found("Verify that if the provided 'premisesCode' in the UpdateUsername endpoint request doesn't exist in the UCBPREM table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = ‘Invalid Account Number’"),
        TC_53__Positive__Username_Available("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is AVAILABLE in MariaDb, the endpoint response will return success = true, ErrorCode = 0 and a new username is created and associated to the Banner account"),
        TC_54__Positive__Username_Active("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is ACTIVE in MariaDb, the endpoint response will return success = true, ErrorCode = 0 and the existing username is associated to the Banner account"),
        TC_55__Positive__LoginID_Saved("Verify that if a value is provided for the optional 'loginID' parameter in the UpdateUsername endpoint request and the rest of the request is well formed the endpoint response will return success = true, ErrorCode = 0, and loginID value is saved/stored in the database"),

        TC_56__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = ‘Missing Request ID’"),
        TC_57__Negative__Invalid_Request_ID__Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = ‘Invalid Request ID’"),
        TC_58__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = ‘Duplicate Request ID’"),
        TC_59__Negative__Missing_CustomerCode("Verify that if the 'customerCode' value is missing in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10011, ErrorMessage = ‘Missing Customer Code’"),
        TC_60__Negative__Invalid_CustomerCode_Length("Verify that if the 'customerCode' length is larger than 9 numeric characters in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = ‘Invalid Customer Code Format’"),
        TC_61__Negative__Invalid_CustomerCode_Format__Not_String("Verify that if the 'customerCode' value is not a string in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = ‘Invalid Customer Code Format’"),
        TC_62__Negative__Invalid_CustomerCode__Not_Found("Verify that if the provided 'customerCode' in the UpdateAccountNickname endpoint request doesn't exist in the UCBCUST table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = ‘Invalid Account Number’"),
        TC_63__Negative__Missing_PremisesCode("Verify that if the 'premisesCode' value is missing in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10013, ErrorMessage = ‘Missing Premises Code’"),
        TC_64__Negative__Invalid_PremisesCode_Length("Verify that if the 'premisesCode' length is larger than 7 numeric characters in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = ‘Invalid Premises Code Format’"),
        TC_65__Negative__Invalid_PremisesCode_Format__Not_String("Verify that if the 'premisesCode' value is not a string in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = ‘Invalid Premises Code Format’"),
        TC_66__Negative__Invalid_PremisesCode__Not_Found("Verify that if the provided 'premisesCode' in the UpdateAccountNickname endpoint request doesn't exist in the UCBPREM table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = ‘Invalid Account Number’"),
        TC_67__Negative__Nickname_Not_Allowed_For_New_Account("Verify that if the 'customerCode' and 'premisesCode' for a Banner account are in a 'New' status and a 'nickname' parameter is provided in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 40215, ErrorMessage = ‘Nickname not allowed for new account’"),
        TC_68__Negative__Nickname_Already_Exists("Verify that if the 'customerCode' and 'premisesCode' for a Banner account are NOT in a 'New' status and a nickname is already set in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 40217, ErrorMessage = ‘Nickname already exists for the account’"),
        TC_69__Negative__Nickname_Missing("Verify that if the 'customerCode' and 'premisesCode' for a Banner account are NOT in a 'New' status and no nickname is provided in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 40219, ErrorMessage = ‘Nickname does not exist in the request’"),
        TC_70__Positive__Nickname_Updated("Verify that if all required fields are set in the UpdateAccountNickname endpoint request for a Banner account NOT in a 'New' status where a nickname is set, the endpoint response will return success = true, ErrorCode = 0 and a new nickname is associated to the Banner account"),
        TC_71__Positive__LoginID_Saved("Verify that if a value is provided for the optional 'loginID' parameter in the UpdateAccountNickname endpoint request and the rest of the request is well formed the endpoint response will return success = true, ErrorCode = 0, and loginID value is saved/stored in the database"),

        TC_72__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_73__Negative__Invalid_Request_ID_Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_74__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_75__Negative__Missing_customerCode("Verify that if the 'customerCode' value is missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10011, ErrorMessage = 'Missing Customer Code'"),
        TC_76__Negative__Invalid_customerCode_Length("Verify that if the 'customerCode' length is larger than 9 numeric characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_77__Negative__Invalid_customerCode_Format("Verify that if the 'customerCode' value is not a string in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_78__Negative__Invalid_customerCode("Verify that if the provided 'customerCode' in the UpdateMailingAddress endpoint request doesn't exist in the UCBCUST table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_79__Negative__Missing_premisesCode("Verify that if the 'premisesCode' value is missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10013, ErrorMessage = 'Missing Premises Code'"),
        TC_80__Negative__Invalid_premisesCode_Length("Verify that if the 'premisesCode' length is larger than 7 numeric characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_81__Negative__Invalid_premisesCode_Format("Verify that if the 'premisesCode' value is not a string in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_82__Negative__Invalid_premisesCode("Verify that if the provided 'premisesCode' in the UpdateMailingAddress endpoint request doesn't exist in the UCBPREM table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_83__Negative__Invalid_Address_Fields_Missing("Verify that if streetName AND ruralRoute AND poBox are all missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10365, ErrorMessage = 'Invalid Address Fields'"),
        TC_84__Negative__Invalid_Address_Fields_Too_Many("Verify that if a combination of 2 or more of streetName, ruralRoute AND/OR poBox are provided in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10365, ErrorMessage = 'Invalid Address Fields'"),
        TC_85__Negative__Invalid_Street_Number_Length("Verify that if the 'streetNumber' parameter length is larger than 12 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10129, ErrorMessage = 'Invalid Street Number Format'"),
        TC_86__Negative__Invalid_Street_Pre_Direction_Length("Verify that if the 'streetPreDirection' parameter length is larger than 2 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10131, ErrorMessage = 'Invalid Street Pre-Direction Format'"),
        TC_87__Negative__Invalid_Street_Pre_Direction("Verify that if the 'streetPreDirection' parameter value does not exist in the Banner reference table in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 40227, ErrorMessage = 'Invalid Street Pre-Direction'"),
        TC_88__Negative__Invalid_Street_Name_Length("Verify that if the 'streetName' parameter length is larger than 30 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10133, ErrorMessage = 'Invalid Street Name Format'"),
        TC_89__Negative__Missing_StreetName("Verify that if the 'streetName' parameter is not provided in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10365, ErrorMessage = 'Invalid Address Fields'"),
        TC_90__Negative__Invalid_StreetSuffix_Length("Verify that if the 'streetSuffix' parameter length is larger than 6 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10135, ErrorMessage = 'Invalid Street Suffix Format'"),
        TC_91__Negative__Invalid_StreetSuffix("Verify that if the 'streetSuffix' parameter value does not exist in the Banner reference table in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 40221, ErrorMessage = 'Invalid Street Suffix'"),
        TC_92__Negative__Invalid_Street_Post_Direction_Length("Verify that if the 'streetPostDirection' parameter length is larger than 2 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10137, ErrorMessage = 'Invalid Street Post-Direction Format'"),
        TC_93__Negative__Invalid_Street_Post_Direction("Verify that if the 'streetPostDirection' parameter value does not exist in the Banner reference table in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 40229, ErrorMessage = 'Invalid Street Post-Direction'"),
        TC_94__Negative__Invalid_Unit_Type_Format("Verify that if the 'unitType' parameter length is larger than 6 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10139, ErrorMessage = 'Invalid Unit Type Format'"),
        TC_95__Negative__Missing_Unit_Type("Verify that if the 'unitType' parameter is missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 40223, ErrorMessage = 'Invalid Street Unit Type'"),
        TC_96__Negative__Invalid_Unit_Number_Format("Verify that if the 'unitNumber' parameter length is larger than 6 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10141, ErrorMessage = 'Invalid Unit Number Format'"),
        TC_97__Negative__Invalid_City_Length("Verify that if the 'city' parameter length is larger than 20 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10143, ErrorMessage = 'Invalid City Format'"),
        TC_98__Negative__Missing_City("Verify that if the 'city' parameter is missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10089, ErrorMessage = 'Missing City'"),
        TC_99__Negative__Invalid_Zip_Code_Format_Length("Verify that if the 'zipCode' parameter length is larger than 10 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10147, ErrorMessage = 'Invalid Zip Code Format'"),
        TC_100__Negative__Missing_Zip_Code("Verify that if the 'zipCode' parameter is missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10091, ErrorMessage = 'Missing Zip Code'"),
        TC_101__Negative__Invalid_Zip_Code("Verify that if the 'zipCode' parameter value does not exist in the Banner reference table in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 40225, ErrorMessage = 'Invalid Zip Code'"),
        TC_102__Negative__Invalid_City_and_Zip_Code_Combination("Verify that if the 'city' and 'zipCode' values are well-formed but do not represent a valid combination in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 40237, ErrorMessage = 'Invalid City and Zip combination'"),
        TC_103__Negative__Invalid_County_Code("Verify that if the 'countyCode' parameter value does not exist in the Banner reference table in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 40231, ErrorMessage = 'Invalid County Code'"),
        TC_104__Negative__Invalid_Delivery_Point_Format("Verify that if the 'deliveryPoint' parameter length is larger than 2 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10367, ErrorMessage = 'Invalid Delivery Point Format'"),
        TC_105__Negative__Invalid_Carrier_Route_Length("Verify that if the 'carrierRoute' parameter length is larger than 4 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10369, ErrorMessage = 'Invalid Carrier Route Format'"),
        TC_106__Negative__Invalid_Attention_To_Length("Verify that if the 'attentionTo' parameter length is larger than 30 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10373, ErrorMessage = 'Invalid Attention To Format'"),
        TC_107__Negative__Invalid_Additional_Address_Line_Length("Verify that if the 'additionalAddressLine' parameter length is larger than 30 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10375, ErrorMessage = 'Invalid Additional Address Line Format'"),
        TC_108__Positive__Valid_Street_Address("Verify that if the request is well formed with the appropriate/required data for a Street-type address in the UpdateMailingAddress endpoint request, the endpoint response will return success = true, ErrorCode = 0, and a valid street address is saved"),
        TC_109__Positive__Valid_PO_Box_Address("Verify that if the request is well formed with the appropriate/required data for a PO Box-type address in the UpdateMailingAddress endpoint request, the endpoint response will return success = true, ErrorCode = 0, and a valid PO Box address is saved"),
        TC_110__Positive__Valid_Rural_Route_Address("Verify that if the request is well formed with the appropriate/required data for a Rural Route-type address in the UpdateMailingAddress endpoint request, the endpoint response will return success = true, ErrorCode = 0, and a valid rural route address is saved"),
        TC_111__Positive__LoginID_Saved("Verify that if a value is provided for the optional 'loginID' parameter in the UpdateMailingAddress endpoint request and the rest of the request is well formed, the endpoint response will return success = true, ErrorCode = 0, and loginID value is saved/stored in the database"),

        TC_112__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the GetAccountRewards endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_113__Negative__Invalid_Request_ID_Length("Verify that if the 'requestID' parameter length is larger than 32 characters in the GetAccountRewards endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_114__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the GetAccountRewards endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_115__Negative__Missing_customerCode("Verify that if the 'customerCode' value is missing in the GetAccountRewards endpoint request, the endpoint response will return success = false, ErrorCode = 10011, ErrorMessage = 'Missing Customer Code'"),
        TC_116__Negative__Invalid_customerCode_Length("Verify that if the 'customerCode' length is larger than 9 numeric characters in the GetAccountRewards endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_117__Negative__Invalid_customerCode_Format("Verify that if the 'customerCode' value is not a string in the GetAccountRewards endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_118__Negative__Invalid_customerCode("Verify that if the provided 'customerCode' in the GetAccountRewards endpoint request doesn't exist in the UCBCUST table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_119__Negative__Missing_premisesCode("Verify that if the 'premisesCode' value is missing in the GetAccountRewards endpoint request, the endpoint response will return success = false, ErrorCode = 10013, ErrorMessage = 'Missing Premises Code'"),
        TC_120__Negative__Invalid_premisesCode_Length("Verify that if the 'premisesCode' length is larger than 7 numeric characters in the GetAccountRewards endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_121__Negative__Invalid_premisesCode_Format("Verify that if the 'premisesCode' value is not a string in the GetAccountRewards endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_122__Negative__Invalid_premisesCode("Verify that if the provided 'premisesCode' in the GetAccountRewards endpoint request doesn't exist in the UCBPREM table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_123__Positive__Active_Rewards("Verify that if all required fields are set in the GetAccountRewards endpoint request, the endpoint response will return success = true, ErrorCode = 0, and a response will be populated with the details of the account’s rewards for an account with active reward(s)"),
        TC_124__Positive__Pending_Rewards("Verify that if all required fields are set in the GetAccountRewards endpoint request, the endpoint response will return success = true, ErrorCode = 0, and a response will be populated with the details of the account’s rewards for an account with pending reward(s)"),
        TC_125__Positive__Active_and_Pending_Rewards("Verify that if all required fields are set in the GetAccountRewards endpoint request, the endpoint response will return success = true, ErrorCode = 0, and a response will be populated with the details of the account’s rewards for an account with both active and pending reward(s)"),
        TC_126__Positive__No_Rewards("Verify that if all required fields are set in the GetAccountRewards endpoint request, the endpoint response will return success = true, ErrorCode = 0, and a response will be returned for an account with NO pending/active reward(s)"),
        TC_127__Positive__Refer_A_Friend_Rewards("Verify that if all required fields are set in the GetAccountRewards endpoint request, the endpoint response will return success = true, ErrorCode = 0, and a response will be populated with the details of the account’s rewards for an account with pending/active reward(s) and at least one of the rewards is a refer-a-friend reward with the corresponding referring Customer and Premises Codes values"),
        TC_128__Positive__LoginID_Saved("Verify that if a value is provided for the optional 'loginID' parameter in the GetAccountRewards endpoint request and the rest of the request is well formed, the endpoint response will return success = true, ErrorCode = 0, and loginID value is saved/stored in the database"),


        TC_129__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_130__Negative__Invalid_Request_ID_Length("Verify that if the 'requestID' parameter length is larger than 32 characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_131__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_132__Negative__Invalid_customerCode_Length("Verify that if the 'customerCode' length is larger than 9 numeric characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_133__Negative__Invalid_customerCode_Format("Verify that if the 'customerCode' value is not a string in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_134__Negative__Invalid_premisesCode_Length("Verify that if the 'premisesCode' length is larger than 7 numeric characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_135__Negative__Invalid_premisesCode_Format("Verify that if the 'premisesCode' value is not a string in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_136__Negative__Invalid_Last_or_Business_Name_Length("Verify that if the 'customerLastNameBusiness' length is larger than 60 characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10119, ErrorMessage = 'Invalid Last or Business Name Format'"),
        TC_137__Negative__Invalid_First_Name_Length("Verify that if the 'customerFirstName' length is larger than 15 characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10121, ErrorMessage = 'Invalid First Name Format'"),
        TC_138__Negative__Invalid_Last_Four_SSN_Length_Too_Long("Verify that if the 'lastFourSocialSecurityNumber' length is larger than 4 characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10065, ErrorMessage = 'Invalid Last Four SSN Format'"),
        TC_139__Negative__Invalid_Last_Four_SSN_Length_Too_Short("Verify that if the 'lastFourSocialSecurityNumber' length is smaller than 4 characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10065, ErrorMessage = 'Invalid Last Four SSN Format'"),
        TC_140__Negative__Invalid_Federal_Tax_ID_Length_Too_Long("Verify that if the 'federalTaxID' length is larger than 9 characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10125, ErrorMessage = 'Invalid Federal Tax ID Format'"),
        TC_141__Negative__Invalid_Federal_Tax_ID_Length_Too_Short("Verify that if the 'federalTaxID' length is smaller than 9 characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10125, ErrorMessage = 'Invalid Federal Tax ID Format'"),
        TC_142__Negative__Invalid_Email_Address_Format("Verify that if the 'emailAddress' format is not {identifier}@{domain name}.{domain extension} in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10067, ErrorMessage = 'Invalid Email Address Format'"),
        TC_143__Negative__Invalid_Phone_Number_Length_Too_Long("Verify that if the 'phoneNumber' length is larger than 10 characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10009, ErrorMessage = 'Invalid Phone Number Format'"),
        TC_144__Negative__Invalid_Phone_Number_Length_Too_Short("Verify that if the 'phoneNumber' length is smaller than 10 characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10009, ErrorMessage = 'Invalid Phone Number Format'"),
        TC_145__Negative__Invalid_Username_Format("Verify that if 'username' contains non-alphanumeric characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10353, ErrorMessage = 'Invalid Username Format'"),
        TC_146__Negative__Inactive_Username("Verify that if the provided 'username' exists in MariaDb and is inactive in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10355, ErrorMessage = 'Inactive Username'"),
        TC_147__Negative__Invalid_Password_Length_Too_Short("Verify that if the 'password' parameter length is shorter than 8 characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10157, ErrorMessage = 'Invalid Password Length. The password must be between 8 and 64 characters.'"),
        TC_148__Negative__Invalid_Password_Length_Too_Long("Verify that if the 'password' parameter length is longer than 64 characters in the AuthenticateCustomer endpoint request, the endpoint response will return success = false, ErrorCode = 10157, ErrorMessage = 'Invalid Password Length. The password must be between 8 and 64 characters.'"),
        TC_149__Negative__Insufficient_Search_Criteria("Verify that if the AuthenticateCustomer endpoint request does not contain the minimum required fields for a valid search, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_150__Negative__Last_4_SSN_and_Password("Verify that if the AuthenticateCustomer endpoint request contains lastFourSocialSecurityNumber + password, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_151__Negative__Last_4_SSN_and_Username("Verify that if the AuthenticateCustomer endpoint request contains lastFourSocialSecurityNumber + username, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_152__Negative__Last_4_SSN_and_CustomerCode_and_PremisesCode("Verify that if the AuthenticateCustomer endpoint request contains lastFourSocialSecurityNumber + customerCode + premisesCode, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_153__Negative__Last_4_SSN_and_Federal_Tax_ID("Verify that if the AuthenticateCustomer endpoint request contains lastFourSocialSecurityNumber + federalTaxID, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_154__Negative__Password_and_Federal_Tax_ID("Verify that if the AuthenticateCustomer endpoint request contains password + federalTaxID, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_155__Negative__CustomerLastNameBusiness_and_EmailAddress("Verify that if the AuthenticateCustomer endpoint request contains customerLastNameBusiness + emailAddress, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_156__Negative__CustomerLastNameBusiness_and_PhoneNumber("Verify that if the AuthenticateCustomer endpoint request contains customerLastNameBusiness + phoneNumber, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_157__Negative__CustomerLastNameBusiness_and_Username("Verify that if the AuthenticateCustomer endpoint request contains customerLastNameBusiness + username, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_158__Negative__CustomerLastNameBusiness_and_CustomerCode_and_PremisesCode("Verify that if the AuthenticateCustomer endpoint request contains customerLastNameBusiness + customerCode + premisesCode, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_159__Negative__BusinessName_and_EmailAddress("Verify that if the AuthenticateCustomer endpoint request contains businessName + emailAddress, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_160__Negative__BusinessName_and_PhoneNumber("Verify that if the AuthenticateCustomer endpoint request contains businessName + phoneNumber, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_161__Negative__BusinessName_and_Username("Verify that if the AuthenticateCustomer endpoint request contains businessName + username, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_162__Negative__BusinessName_and_CustomerCode_and_PremisesCode("Verify that if the AuthenticateCustomer endpoint request contains businessName + customerCode + premisesCode, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_163__Negative__EmailAddress_and_PhoneNumber("Verify that if the AuthenticateCustomer endpoint request contains emailAddress + phoneNumber, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_164__Negative__EmailAddress_and_Username("Verify that if the AuthenticateCustomer endpoint request contains emailAddress + username, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_165__Negative__EmailAddress_and_CustomerCode_and_PremisesCode("Verify that if the AuthenticateCustomer endpoint request contains emailAddress + customerCode + premisesCode, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_166__Negative__EmailAddress_and_Federal_Tax_ID("Verify that if the AuthenticateCustomer endpoint request contains emailAddress + federalTaxID, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_167__Negative__PhoneNumber_and_Username("Verify that if the AuthenticateCustomer endpoint request contains phoneNumber + username, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_168__Negative__PhoneNumber_and_CustomerCode_and_PremisesCode("Verify that if the AuthenticateCustomer endpoint request contains phoneNumber + customerCode + premisesCode, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_169__Negative__PhoneNumber_and_Federal_Tax_ID("Verify that if the AuthenticateCustomer endpoint request contains phoneNumber + federalTaxID, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_170__Negative__Username_and_CustomerCode_and_PremisesCode("Verify that if the AuthenticateCustomer endpoint request contains username + customerCode + premisesCode, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_171__Negative__Username_and_Federal_Tax_ID("Verify that if the AuthenticateCustomer endpoint request contains username + federalTaxID, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_172__Negative__CustomerCode_and_PremisesCode_and_Federal_Tax_ID("Verify that if the AuthenticateCustomer endpoint request contains customerCode + premisesCode + federalTaxID, the endpoint response will return success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_173__Negative__Too_Many_Matches("Verify that if the AuthenticateCustomer endpoint request contains the minimum required fields for a valid search but the number of search results exceeds the pre-defined SEARCH_LIMIT, the endpoint response will return success = false, ErrorCode = 10391, ErrorMessage = 'Too Many Matches'"),
        TC_174__Positive__Last_4_SSN_and_Customer_Last_Name("Verify that if valid lastFourSocialSecurityNumber + customerLastNameBusiness are provided in the AuthenticateCustomer endpoint request, the endpoint response will return success = true, ErrorCode = 0"),
        TC_175__Positive__Last_4_SSN_and_EmailAddress("Verify that if valid lastFourSocialSecurityNumber + emailAddress are provided in the AuthenticateCustomer endpoint request, the endpoint response will return success = true, ErrorCode = 0"),
        TC_176__Positive__Last_4_SSN_and_PhoneNumber("Verify that if valid lastFourSocialSecurityNumber + phoneNumber are provided in the AuthenticateCustomer endpoint request, the endpoint response will return success = true, ErrorCode = 0"),
        TC_177__Positive__Password_and_Username("Verify that if valid password + username are provided in the AuthenticateCustomer endpoint request, the endpoint response will return success = true, ErrorCode = 0"),
        TC_178__Positive__Password_and_Customer_Last_Name("Verify that if valid password + customerLastNameBusiness are provided in the AuthenticateCustomer endpoint request, the endpoint response will return success = true, ErrorCode = 0"),
        TC_179__Positive__Password_and_BusinessName("Verify that if valid password + businessName are provided in the AuthenticateCustomer endpoint request, the endpoint response will return success = true, ErrorCode = 0"),
        TC_180__Positive__Password_and_CustomerCode_and_PremisesCode("Verify that if valid password + customerCode + premisesCode are provided in the AuthenticateCustomer endpoint request, the endpoint response will return success = true, ErrorCode = 0"),
        TC_181__Positive__Password_and_EmailAddress("Verify that if valid password + emailAddress are provided in the AuthenticateCustomer endpoint request, the endpoint response will return success = true, ErrorCode = 0"),
        TC_182__Positive__Password_and_PhoneNumber("Verify that if valid password + phoneNumber are provided in the AuthenticateCustomer endpoint request, the endpoint response will return success = true, ErrorCode = 0"),
        TC_183__Positive__BusinessName_and_Federal_Tax_ID("Verify that if valid businessName + federalTaxID are provided in the AuthenticateCustomer endpoint request, the endpoint response will return success = true, ErrorCode = 0"),
        TC_184__Positive__LoginID_Saved("Verify that if a value is provided for the optional 'loginID' parameter in the AuthenticateCustomer endpoint request and the rest of the request is well formed, the endpoint response will return success = true, ErrorCode = 0, and loginID value is saved/stored in the database"),

        TC_185__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the GetAccountInfo request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_186__Negative__Invalid_Request_ID__Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the GetAccountInfo request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_187__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the GetAccountInfo request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_188__Negative__Invalid_customerCode_Length("Verify that if the 'customerCode' length is larger than 9 numeric characters in the GetAccountInfo request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_189__Negative__Invalid_customerCode_Format("Verify that if the 'customerCode' value is not a string in the GetAccountInfo request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_190__Negative__Invalid_premisesCode_Length("Verify that if the 'premisesCode' length is larger than 7 numeric characters in the GetAccountInfo request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_191__Negative__Invalid_premisesCode_Format("Verify that if the 'premisesCode' value is not a string in the GetAccountInfo request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_192__Negative__Invalid_customerCode("Verify that if the provided 'customerCode' in the UpdateUsername request doesn't exist in the UCBCUST table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_193__Negative__Invalid_premisesCode("Verify that if the provided 'premisesCode' in the UpdateUsername request doesn't exist in the UCBPREM table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_194__Positive__Account_Info_Returned("Verify that if all required fields are set in the GetAccountInfo request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response payload parameters contain all of the available data for the test account used as expected."),
        TC_195__Positive__Login_ID_Saved("Verify that if a value is provided for the optional 'loginID' parameter in the GetAccountInfo request and the rest of the request is well formed, the endpoint response will return success = true, ErrorCode = 0, and loginID value is saved/stored in the database.");


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
