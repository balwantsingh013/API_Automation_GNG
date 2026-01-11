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

        TC_1__Negative__Missing_Request_ID("Verify that if the requestID parameter is missing in the ValidateUsername endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_2__Negative__Invalid_Request_ID__Length("Verify that if the provided requestID parameter length is larger than 32 characters in the ValidateUsername endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_3__Negative__Duplicate_Request_ID("Verify that if the provided requestID parameter already exists within Transaction Manager while executing the ValidateUsername endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_4__Negative__Missing_Username("Verify that if the username parameter is missing in the ValidateUsername endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10349, ErrorMessage = 'Missing Username'"),
        TC_5__Negative__Invalid_Username_format__Length___Too_Short____("Verify that if the provided username parameter length is smaller than 5 characters in the ValidateUsername endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10351, ErrorMessage = 'Invalid Username Length'"),
        TC_6__Negative__Invalid_Username_format__Length___Too_Long____("Verify that if the provided username parameter length is larger than 15 characters in the ValidateUsername endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10351, ErrorMessage = 'Invalid Username Length'"),
        TC_7__Negative__Invalid_Username_format__Alphanumeric("Verify that if the provided username parameter is not alphanumeric in the ValidateUsername endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10353, ErrorMessage = 'Invalid Username Format'"),
        TC_8__Positive__Username_Available("Verify that in the ValidateUsername endpoint request if the provided username does exist in MariaDb users table with active = 1, deleted = 0, and domain_id is NOT 2, the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'AVAILABLE'"),
        TC_9__Positive__Username_Available("Verify that in the ValidateUsername endpoint request if the provided username does not exist in MariaDb, the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'AVAILABLE'"),
        TC_10__Positive__Username_Active__users_table("Verify that in the ValidateUsername endpoint request if the provided username does exist in MariaDb users table with active = 1, deleted = 0, and domain_id = 2, the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'ACTIVE'"),
        TC_11__Positive__Username_Inactive("Verify that in the ValidateUsername endpoint request if the provided username does exist in MariaDb users table with active = 0 and domain_id = 2, the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'INACTIVE'"),

        TC_12__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_13__Negative__Invalid_Request_ID__Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_14__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_15__Negative__Missing_Username("Verify that if the 'username' parameter is missing in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10349, ErrorMessage = 'Missing Username'"),
        TC_16__Negative__Invalid_Username_format__Length___Too_Short____("Verify that if the provided 'username' parameter length is smaller than 5 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10351, ErrorMessage = 'Invalid Username Length'"),
        TC_17__Negative__Invalid_Username_format__Length___Too_Long____("Verify that if the provided 'username' parameter length is larger than 15 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10351, ErrorMessage = 'Invalid Username Length'"),
        TC_18__Negative__Invalid_Username_format__Alphanumeric("Verify that if the provided 'username' parameter is not alphanumeric in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10353, ErrorMessage = 'Invalid Username Format'"),
        TC_19__Negative__Invalid_Username__Not_Found("Verify that if the provided 'username' parameter is well‑formed but does not exist in MariaDb 'users' table where domain_id = 2, the endpoint response will return success = false, ErrorCode = 10357, ErrorMessage = 'Username Not Found'"),
        TC_20__Negative__Invalid_Username__Inactive("Verify that if the provided 'username' exists in MariaDb 'users' table but is inactive (active = 0 OR deleted = 1) and domain_id = 2, the endpoint response will return success = false, ErrorCode = 10355, ErrorMessage = 'Inactive Username'"),
        TC_21__Negative__Missing_Password("Verify that if the 'password' parameter is missing in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10113, ErrorMessage = 'Missing Password'"),
        TC_22__Negative__Invalid_Password_Format__Length___Too_Short____("Verify that if the provided 'password' parameter length is shorter than 8 characters, the endpoint response will return success = false, ErrorCode = 10157, ErrorMessage = 'Invalid Password Length. The password must be between 8 and 64 characters.'"),
        TC_23__Negative__Invalid_Password_Format__Length___Too_Long____("Verify that if the provided 'password' parameter length is longer than 64 characters, the endpoint response will return success = false, ErrorCode = 10157, ErrorMessage = 'Invalid Password Length. The password must be between 8 and 64 characters.'"),
        TC_24__Negative__Invalid_Password__Reused_Password("Verify that if the provided 'password' matches the existing password hash in the users table, the endpoint response will return success = false, ErrorCode = 10359, ErrorMessage = 'Invalid Password. The provided password matches the existing one.'"),
        TC_25__Positive__Password__Updated("Verify that if valid 'username' and 'password' parameters are provided, the endpoint response will return success = true, ErrorCode = 0"),

        TC_26__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_27__Negative__Invalid_Request_ID__Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_28__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_29__Negative__Missing_Username("Verify that if the 'username' parameter is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10349, ErrorMessage = 'Missing Username'"),
        TC_30__Negative__Invalid_Username__Inactive("Verify that if the provided 'username' parameter value does exist in MariaDb 'users' table with active value of 0 and domain_id = 2 and is used in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10355, ErrorMessage = 'Inactive Username'"),
        TC_31__Negative__Invalid_Username_format__Length___Too_Short____("Verify that if the provided 'username' parameter length is smaller than 5 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10351, ErrorMessage = 'Invalid Username Length'"),
        TC_32__Negative__Invalid_Username_format__Length___Too_Long____("Verify that if the provided 'username' parameter length is larger than 15 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10351, ErrorMessage = 'Invalid Username Length'"),
        TC_33__Negative__Invalid_Username_format__Alphanumeric("Verify that if the provided 'username' parameter is not alphanumeric in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10353, ErrorMessage = 'Invalid Username Format'"),
        TC_34__Negative__Missing_Password("Verify that if the provided 'password' parameter value is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10113, ErrorMessage = 'Missing Password'"),
        TC_35__Negative__Invalid_Password_Format__Length___Too_Short____("Verify that if the provided 'password' parameter value length is shorter than 8 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10157, ErrorMessage = 'Invalid Password Format'"),
        TC_36__Negative__Invalid_Password_Format__Length___Too_Long____("Verify that if the provided 'password' parameter value length is longer than 64 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10157, ErrorMessage = 'Invalid Password Format'"),
        TC_37__Negative__Account_username_already_exists("Verify that if the provided 'customerCode', 'premisesCode' and 'username' in the UpdateUsername endpoint request and the account is already linked to a username, the endpoint response will return success = false, action = null, ErrorCode = 10361, ErrorMessage = 'The account is already linked to a username'"),
        TC_38__Negative__Invalid_credentials___Password____("Verify that if the provided 'password' and 'username' are in the UpdateUsername endpoint request and the password does not match what is in the database where username exists with domain_id 2, the endpoint response will return success = false, action = null, ErrorCode = 10363, ErrorMessage = 'Invalid Credentials'"),
        TC_39__Negative__Invalid_credentials___Username_does_not_exist____("Verify that if the provided 'password' and 'username' are in the UpdateUsername endpoint request and the username does not exist in the MariaDb database where domain_id is 2, the endpoint response will return success = false, action = null, ErrorCode = 10363, ErrorMessage = 'Invalid Credentials'"),
        TC_40__Negative__Invalid_credentials___Username_Inactive____("Verify that if the provided 'password' and 'username' are in the UpdateUsername endpoint request and username does exist in MariaDb 'users' table with active value of 0 and domain_id = 2, the endpoint response will return success = false, action = null, ErrorCode = 10363, ErrorMessage = 'Invalid Credentials'"),
        TC_41__Negative__Missing_customerCode("Verify that if the 'customerCode' value is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10011, ErrorMessage = 'Missing Customer Code'"),
        TC_42__Negative__Invalid_customerCode_Length("Verify that if the 'customerCode' length is larger than 9 numeric characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_43__Negative__Invalid_customerCode_Format("Verify that if the 'customerCode' value is not a string in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_44__Negative__Invalid_Account_number("Verify that if the provided combination of 'customerCode' and 'premisesCode' in the UpdateUsername endpoint request doesn't exist in the UCBCUST table in Banner, the endpoint response will return success = false, action = null, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_45__Negative__Missing_premisesCode("Verify that if the 'premisesCode' value is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10013, ErrorMessage = 'Missing Premises Code'"),
        TC_46__Negative__Invalid_premisesCode_Length("Verify that if the 'premisesCode' length is larger than 7 numeric characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_47__Negative__Invalid_premisesCode_Format("Verify that if the 'premisesCode' value is not a string in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_48__Positive__Username_Available___Banner_Active____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is AVAILABLE in MariaDb using an Account Number ('customer code' + 'premises code') for a Banner account in an 'Active' status, the endpoint response will return success = true, ErrorCode = 0, action = 'CREATED' and a new username is created and associated to the Banner account"),
        TC_49__Positive__Username_Available___Banner_New____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is AVAILABLE in MariaDb using an Account Number ('customer code' + 'premises code') for a Banner account in a 'New' status, the endpoint response will return success = true, ErrorCode = 0, action = 'CREATED' and a new username is created and associated to the Banner account"),
        TC_50__Positive__Username_Available___Banner_Final____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is AVAILABLE in MariaDb using an Account Number ('customer code' + 'premises code') for a Banner account in a 'Final' status, the endpoint response will return success = true, ErrorCode = 0, action = 'CREATED' and a new username is created and associated to the Banner account"),
        TC_51__Positive__Username_Active___Banner_Active____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is ACTIVE in MariaDb using an Account Number ('customer code' + 'premises code') for a Banner account in an 'Active' status, the endpoint response will return success = true, ErrorCode = 0, action = 'UPDATED' and the existing username is associated to the Banner account"),
        TC_52__Positive__Username_Active___Banner_Final____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is ACTIVE in MariaDb using an Account Number ('customer code' + 'premises code') for a Banner account in an 'Final' status, the endpoint response will return success = true, ErrorCode = 0, action = 'UPDATED' and the existing username is associated to the Banner account"),
        TC_53__Positive__Username_Active___Banner_Inactive____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is ACTIVE in MariaDb using an Account Number ('customer code' + 'premises code') for a Banner account in an 'Inactive' status, the endpoint response will return success = true, ErrorCode = 0, action = 'UPDATED' and the existing username is associated to the Banner account"),

        TC_54__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_55__Negative__Invalid_Request_ID__Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_56__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_57__Negative__Missing_CustomerCode("Verify that if the 'customerCode' value is missing in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10011, ErrorMessage = 'Missing Customer Code'"),
        TC_58__Negative__Invalid_CustomerCode_Length("Verify that if the 'customerCode' length is larger than 9 numeric characters in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_59__Negative__Invalid_CustomerCode_Format__Not_String("Verify that if the 'customerCode' value is not a string in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_60__Negative__Invalid_Account_number("Verify that if the provided combination of 'customerCode' and 'premisesCode' in the UpdateAccountNickname endpoint request doesn't exist in the UCBCUST table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_61__Negative__Missing_PremisesCode("Verify that if the 'premisesCode' value is missing in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10013, ErrorMessage = 'Missing Premises Code'"),
        TC_62__Negative__Invalid_PremisesCode_Length("Verify that if the 'premisesCode' length is larger than 7 numeric characters in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_63__Negative__Invalid_PremisesCode_Format__Not_String("Verify that if the 'premisesCode' value is not a string in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_64__Negative__Nickname_Not_Allowed_For_New_Account("Verify that if the 'customerCode' and 'premisesCode' for a Banner account in a 'New' status and a 'nickname' parameter is provided in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 40215, ErrorMessage = 'Nickname not allowed for new account'"),
        TC_65__Negative__Nickname_Already_Exists("Verify that if the 'customerCode' and 'premisesCode' for a Banner account in an 'Active' status where a nickname is set for the Banner account (UCRACCT.UCRACCT_NICK_NAME) and a 'nickname' parameter is provided in the UpdateAccountNickname endpoint request that matches the existing nickname, the endpoint response will return success = false, ErrorCode = 40217, ErrorMessage = 'Nickname already exists for the account'"),
        TC_66__Negative__Nickname_Already_Exists("Verify that if the 'customerCode' and 'premisesCode' for a Banner account in a 'Final' status where a nickname is set for the Banner account (UCRACCT.UCRACCT_NICK_NAME) and a 'nickname' parameter is provided in the UpdateAccountNickname endpoint request that matches the existing nickname, the endpoint response will return success = false, ErrorCode = 40217, ErrorMessage = 'Nickname already exists for the account'"),
        TC_67__Negative__Nickname_Already_Exists("Verify that if the 'customerCode' and 'premisesCode' for a Banner account in an 'Inactive' status where a nickname is set for the Banner account (UCRACCT.UCRACCT_NICK_NAME) and a 'nickname' parameter is provided in the UpdateAccountNickname endpoint request that matches the existing nickname, the endpoint response will return success = false, ErrorCode = 40217, ErrorMessage = 'Nickname already exists for the account'"),
        TC_68__Negative__Nickname_Missing("Verify that if the customerCode and premisesCode for a Banner account NOT in a New status where a nickname is not set for the Banner account (UCRACCT.UCRACCT_NICK_NAME) and a nickname parameter is NOT provided in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 40219, ErrorMessage = 'Nickname does not exist in the request'"),
        TC_69__Positive__Nickname_Set__Active("Verify that if all required fields are set in and a value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in an Active status where an existing nickname is NOT set, the endpoint response will return success = true, ErrorCode = 0 and the provided nickname is associated to the Banner account"),
        TC_70__Positive__Nickname_Set__Final("Verify that if all required fields are set in and a value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in a Final status where an existing nickname is NOT set, the endpoint response will return success = true, ErrorCode = 0 and the provided nickname is associated to the Banner account"),
        TC_71__Positive__Nickname_Set__Inactive("Verify that if all required fields are set in and a value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in an Inactive status where an existing nickname is NOT set, the endpoint response will return success = true, ErrorCode = 0 and the provided nickname is associated to the Banner account"),
        TC_72__Positive__Nickname_Updated__Active("Verify that if all required fields are set in and a value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in an Active status where an existing nickname is set, the endpoint response will return success = true, ErrorCode = 0 and a new nickname is associated to the Banner account"),
        TC_73__Positive__Nickname_Updated__Final("Verify that if all required fields are set in and a value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in a Final status where an existing nickname is set, the endpoint response will return success = true, ErrorCode = 0 and a new nickname is associated to the Banner account"),
        TC_74__Positive__Nickname_Updated__Inactive("Verify that if all required fields are set in and a value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in an Inactive status where an existing nickname is set, the endpoint response will return success = true, ErrorCode = 0 and a new nickname is associated to the Banner account"),
        TC_75__Positive__Nickname_Removed__Active("Verify that if all required fields are set in and an EMPTY value ('') for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in an Active status where an existing nickname is set, the endpoint response will return success = true, ErrorCode = 0 and the nickname that is associated to the Banner account is set to EMPTY ('')"),
        TC_76__Positive__Nickname_Removed__Final("Verify that if all required fields are set in and an EMPTY value ('') for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in a Final status where an existing nickname is set, the endpoint response will return success = true, ErrorCode = 0 and the nickname that is associated to the Banner account is set to EMPTY ('')"),
        TC_77__Positive__Nickname_Removed__Inactive("Verify that if all required fields are set in and an EMPTY value ('') for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in an Inactive status where an existing nickname is set, the endpoint response will return success = true, ErrorCode = 0 and the nickname that is associated to the Banner account is set to EMPTY ('')"),
        TC_78__Positive__LoginID_Saved("Verify that if a value is provided for the optional loginID parameter in the UpdateAccountNickname endpoint request and the rest of the request is well formed the endpoint response will return success = true, ErrorCode = 0, and loginID value is saved/stored in the GZRAPIL_LOGIN_ID field in the GZRAPIL table"),


        TC_79__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_80__Negative__Invalid_Request_ID_Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_81__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_82__Negative__Missing_customerCode("Verify that if the 'customerCode' value is missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10011, ErrorMessage = 'Missing Customer Code'"),
        TC_83__Negative__Invalid_customerCode_Length("Verify that if the 'customerCode' length is larger than 9 numeric characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_84__Negative__Invalid_customerCode_Format("Verify that if the 'customerCode' value is not a string in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_85__Negative__Invalid_Account_number("Verify that if the provided combination of 'customerCode' and 'premisesCode' in the UpdateMailingAddress endpoint request doesn't exist in the UCBCUST table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_86__Negative__Missing_premisesCode("Verify that if the 'premisesCode' value is missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10013, ErrorMessage = 'Missing Premises Code'"),
        TC_87__Negative__Invalid_premisesCode_Length("Verify that if the 'premisesCode' length is larger than 7 numeric characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_88__Negative__Invalid_premisesCode_Format("Verify that if the 'premisesCode' value is not a string in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_89__Negative__Invalid_Address_Fields_Missing("Verify that if streetName AND ruralRoute AND poBox are all missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10365, ErrorMessage = 'Invalid Address Fields'"),
        TC_90__Negative__Invalid_Address_Fields_Too_Many("Verify that if a combination of 2 or more of streetName, ruralRoute and/or poBox are provided in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10365, ErrorMessage = 'Invalid Address Fields'"),
        TC_91__Negative__Invalid_Street_Number_Length("Verify that if the 'streetNumber' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 12 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10129, errorMessage = 'Invalid Street Number Format'"),
        TC_92__Negative__Invalid_Street_Pre_Direction_Length("Verify that if the 'streetPreDirection' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 2 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10131, errorMessage = 'Invalid Street Pre-Direction Format'"),
        TC_93__Negative__Invalid_Street_Pre_Direction("Verify that if the 'streetPreDirection' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the value does not exist in the Banner reference table, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 40227, errorMessage = 'Invalid Street Pre-Direction'"),
        TC_94__Negative__Invalid_Street_Name_Length("Verify that if the 'streetName' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 30 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10133, errorMessage = 'Invalid Street Name Format'"),
        TC_95__Negative__Missing_StreetName("Verify that if the 'streetName' parameter is NOT provided in the UpdateMailingAddress endpoint request, and the remainder of the request is well formed with the appropriate/required data for a street address (NOT a Rural Route or PO Box address), the endpoint response will return success = false, ErrorCode = 10365, ErrorMessage = 'Invalid Address Fields'"),
        TC_96__Negative__Invalid_StreetSuffix_Length("Verify that if the 'streetSuffix' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 6 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10135, errorMessage = 'Invalid Street Suffix Format'"),
        TC_97__Negative__Invalid_StreetSuffix("Verify that if the 'streetSuffix' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the value does not exist in the Banner reference table, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 40221, errorMessage = 'Invalid Street Suffix'"),
        TC_98__Negative__Invalid_Street_Post_Direction_Length("Verify that if the 'streetPostDirection' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 2 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10137, errorMessage = 'Invalid Street Post-Direction Format'"),
        TC_99__Negative__Invalid_Street_Post_Direction("Verify that if the 'streetPostDirection' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the value does not exist in the Banner reference table, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 40229, errorMessage = 'Invalid Street Post-Direction'"),
        TC_100__Negative__Invalid_Unit_Type_Format("Verify that if the 'unitType' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 6 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10135, errorMessage = 'Invalid Unit Type Format'"),
        TC_101__Negative__Invalid_Unit_Type("Verify that if the 'unitType' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the value does not exist in the Banner reference table, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 40223, errorMessage = 'Invalid Street Unit Type'"),
        TC_102__Negative__Invalid_Unit_Number_Format("Verify that if the 'unitNumber' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 6 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10135, errorMessage = 'Invalid Unit Number Format'"),
        TC_103__Negative__Invalid_City_Length("Verify that if the 'city' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 20 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10135, errorMessage = 'Invalid Unit Type Format'"),
        TC_104__Negative__Missing_City("Verify that if the 'city' parameter is NOT provided in the UpdateMailingAddress endpoint request, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, ErrorCode = 10089, ErrorMessage = 'Missing City'"),
        TC_105__Negative__Invalid_Zip_Code_Format_Length("Verify that if the 'zipCode' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 10 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10147, errorMessage = 'Invalid Zip Code Format'"),
        TC_106__Negative__Missing_Zip_Code("Verify that if the 'zipCode' parameter is NOT provided in the UpdateMailingAddress endpoint request, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, ErrorCode = 10091, ErrorMessage = 'Missing Zip Code'"),
        TC_107__Negative__Invalid_Zip_Code("Verify that if the 'zipCode' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the value does not exist in the Banner reference table, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 40225, errorMessage = 'Invalid Zip Code'"),
        TC_108__Negative__Invalid_City_and_Zip_Code_Combination("Verify that if the 'zipCode' and 'city' parameters are provided in the UpdateMailingAddress endpoint request is populated, the values are well-formed/correct but the values do not represent a valid combination and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 40237, errorMessage = 'Invalid City and Zip combination'"),
        TC_109__Negative__Invalid_Delivery_Point_Format("Verify that if the 'deliveryPoint' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 2 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10367, errorMessage = 'Invalid Delivery Point Format'"),
        TC_110__Negative__Invalid_Carrier_Route_Length("Verify that if the 'carrierRoute' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 4 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10369, errorMessage = 'Invalid Carrier Route Format'"),
        TC_111__Negative__Invalid_Attention_To_Length("Verify that if the 'attentionTo' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 30 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10373, errorMessage = 'Invalid Attention To Format'"),
        TC_112__Negative__Invalid_Additional_Address_Line_Length("Verify that if the 'additionalAddressLine' parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 30 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10373, errorMessage = 'Invalid Attention To Format'"),
        TC_113__Positive__Valid_Street_Address___Minimum_parameters_or_No_Existing_Address____("Verify that if a well formed request with the minimum required parameters for a Street-type address is provided in the UpdateMailingAddress endpoint request where NO ADDRESS exists, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and a new row is added to the Banner table UCRADDR with all of the data from the fields provided in request, the current date is set in UCRADDR_FROM_DATE, no value is set for UCRADDR_TO_DATE and UCRADDR_STATUS_IND is set to 'A'"),
        TC_114__Positive__Valid_Street_Address___Maximum_parameters_or_No_Existing_Address____("Verify that if a well formed request with the maximum required parameters for a Street-type address is provided in the UpdateMailingAddress endpoint request where NO ADDRESS exists, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and a new row is added to the Banner table UCRADDR with all of the data from the fields provided in request, the current date is set in UCRADDR_FROM_DATE, no value is set for UCRADDR_TO_DATE and UCRADDR_STATUS_IND is set to 'A'"),
        TC_115__Positive__Valid_Street_Address___Mixed_parameters_or_No_Existing_Address____("Verify that if a well formed request with a mix of valid parameters, including the minimum required parameters, for a Street-type address is provided in the UpdateMailingAddress endpoint request where NO ADDRESS exists, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and a new row is added to the Banner table UCRADDR with all of the data from the fields provided in request, the current date is set in UCRADDR_FROM_DATE, no value is set for UCRADDR_TO_DATE and UCRADDR_STATUS_IND is set to 'A'"),
        TC_116__Positive__Valid_Street_Address___Minimum_parameters_or_Existing_Address_or_Same_Day____("Verify that if a well formed request with the minimum required parameters for a Street-type address is provided in the UpdateMailingAddress endpoint request where a matching address exists AND the start date is the same as the current date, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and the existing row is updated with the values provided in the request. UCRADDR_FROM_DATE, UCRADDR_TO_DATE and UCRADDR_STATUS_IND remain unmodified"),
        TC_117__Positive__Valid_Street_Address___Maximum_parameters_or_Existing_Address_or_Same_Day____("Verify that if a well formed request with the maximum required parameters for a Street-type address is provided in the UpdateMailingAddress endpoint request where a matching address exists AND the start date is the same as the current date, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and the existing row is updated with the values provided in the request. UCRADDR_FROM_DATE, UCRADDR_TO_DATE and UCRADDR_STATUS_IND remain unmodified"),
        TC_118__Positive__Valid_Street_Address___Mixed_parameters_or_Existing_Address_or_Same_Day____("Verify that if a well formed request with a mix of valid parameters, including the minimum required parameters, for a Street-type address is provided in the UpdateMailingAddress endpoint request where a matching address exists AND the start date is the same as the current date, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and the existing row is updated with the values provided in the request. UCRADDR_FROM_DATE, UCRADDR_TO_DATE and UCRADDR_STATUS_IND remain unmodified"),
        TC_119__Positive__Valid_Street_Address___Minimum_parameters_or_Existing_Address_or_Different_Day____("Verify that if a well formed request with the minimum required parameters for a Street-type address is provided in the UpdateMailingAddress endpoint request where a matching address exists AND the start date is AFTER the current date, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and a new row is added with the values provided in the request. UCRADDR_FROM_DATE for the new row is set to the current date and UCRADDR_TO_DATE is left blank. UCRADDR_TO_DATE on the existing row is set to current date - 1. UCRADDR_STATUS_IND for the new row is set to 'A' and UCRADDR_STATUS_IND for the existing row is set to 'I'"),
        TC_120__Positive__Valid_Street_Address___Maximum_parameters_or_Existing_Address_or_Different_Day____("Verify that if a well formed request with the maximum required parameters for a Street-type address is provided in the UpdateMailingAddress endpoint request where a matching address exists AND the start date is AFTER the current date, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and a new row is added with the values provided in the request. UCRADDR_FROM_DATE for the new row is set to the current date and UCRADDR_TO_DATE is left blank. UCRADDR_TO_DATE on the existing row is set to current date - 1. UCRADDR_STATUS_IND for the new row is set to 'A' and UCRADDR_STATUS_IND for the existing row is set to 'I'"),
        TC_121__Positive__Valid_Street_Address___Mixed_parameters_or_Existing_Address_or_Different_Day____("Verify that if a well formed request with a mix of valid parameters, including the minimum required parameters, for a Street-type address is provided in the UpdateMailingAddress endpoint request where a matching address exists AND the start date is AFTER the current date, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and a new row is added with the values provided in the request. UCRADDR_FROM_DATE for the new row is set to the current date and UCRADDR_TO_DATE is left blank. UCRADDR_TO_DATE on the existing row is set to current date - 1. UCRADDR_STATUS_IND for the new row is set to 'A' and UCRADDR_STATUS_IND for the existing row is set to 'I'"),
        TC_122__Positive__Valid_PO_Box_Address("Verify that if the request is well formed with the appropriate/required data for a PO Box-type address is provided in the UpdateMailingAddress endpoint request, the endpoint response will return success = true, errorCode = 0, errorMessage = ''"),
        TC_123__Positive__Valid_Rural_Route_Address("Verify that if the request is well formed with the appropriate/required data for a Rural Route-type address is provided in the UpdateMailingAddress endpoint request, the endpoint response will return success = true, errorCode = 0, errorMessage = ''"),
        TC_124__Positive__LoginID_Saved("Verify that if a value is provided for the optional 'loginID' parameter in the UpdateMailingAddress endpoint request and the rest of the request is well formed the endpoint response will return success = true, ErrorCode = 0, and loginID value is saved/stored in the GZRAPIL_LOGIN_ID field in the GZRAPIL table"),

        TC_125__Negative__Missing_Request_ID("Verify that if the 'requestID' parameter is missing in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10001, and ErrorMessage = 'Missing Request ID'"),
        TC_126__Negative__Invalid_Request_ID_Length("Verify that if the provided 'requestID' parameter length is larger than 32 characters in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10002, and ErrorMessage = 'Invalid Request ID'"),
        TC_127__Negative__Duplicate_Request_ID("Verify that if the provided 'requestID' parameter already exists within Transaction Manager while executing the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10003, and ErrorMessage = 'Duplicate Request ID'"),
        TC_128__Negative__Missing_customerCode("Verify that if the 'customerCode' value is missing in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10011, and ErrorMessage = 'Missing Customer Code'"),
        TC_129__Negative__Invalid_customerCode_Length("Verify that if the 'customerCode' length is larger than 9 numeric characters in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10015, and ErrorMessage = 'Invalid Customer Code Format'"),
        TC_130__Negative__Invalid_customerCode_Format("Verify that if the 'customerCode' value is not a string in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10015, and ErrorMessage = 'Invalid Customer Code Format'"),
        TC_131__Negative__Invalid_Account_number("Verify that if the provided combination of 'customerCode' and 'premisesCode' in the GetAccountRewards endpoint request does not exist in the UCBCUST table in Banner, the endpoint response will return data = null, success = false, ErrorCode = 40015, and ErrorMessage = 'Invalid Account Number'"),
        TC_132__Negative__Missing_premisesCode("Verify that if the 'premisesCode' value is missing in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10013, and ErrorMessage = 'Missing Premises Code'"),
        TC_133__Negative__Invalid_premisesCode_Length("Verify that if the 'premisesCode' length is larger than 7 numeric characters in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10005, and ErrorMessage = 'Invalid Premises Code Format'"),
        TC_134__Negative__Invalid_premisesCode_Format("Verify that if the 'premisesCode' value is not a string in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10005, and ErrorMessage = 'Invalid Premises Code Format'"),
        TC_135__Positive__Rewards_Response_Returned_with_Active_Rewards("Verify that when all required fields are provided, the GetAccountRewards endpoint returns success = true, ErrorCode = 0, and a response containing Active reward(s) where Active rewards count > 0, Pending rewards count = 0, and the active rewards array includes rewardID, rewardName, rewardTypeCode, rewardTypeDescription, status, rewardEstablishedDate (optional), rewardInitiatedDate (optional), remainingOccurrences, totalOccurrences, restrictions (optional), referenceCustomerCode (optional), and referencePremisesCode (optional)"),
        TC_136__Positive__Rewards_Response_Returned_with_Pending_Rewards("Verify that when all required fields are provided, the GetAccountRewards endpoint returns success = true, ErrorCode = 0, and a response containing Pending reward(s) where Active rewards count = 0, Pending rewards count > 0, and the pending rewards array includes rewardID, rewardName, rewardTypeCode, rewardTypeDescription, status, rewardEstablishedDate (optional), rewardInitiatedDate (optional), remainingOccurrences (9999), totalOccurrences, restrictions (optional), referenceCustomerCode (optional), and referencePremisesCode (optional)"),
        TC_137__Positive__Rewards_Response_Returned_with_Active_and_Pending_Rewards("Verify that when all required fields are provided, the GetAccountRewards endpoint returns success = true, ErrorCode = 0, and a response containing both Active and Pending reward(s), including counts for each and arrays containing all required reward fields for both active and pending rewards"),
        TC_138__Positive__Rewards_Response_Returned_with_No_Rewards("Verify that when all required fields are provided, the GetAccountRewards endpoint returns success = true, ErrorCode = 0, and a response indicating Active rewards count = 0, Pending rewards count = 0, and an empty rewards array"),
        TC_139__Positive__Active_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards("Verify that when all required fields are provided, the GetAccountRewards endpoint returns success = true, ErrorCode = 0, and a response containing Active reward(s) where at least one reward is a refer‑a‑friend reward including referenceCustomerCode and referencePremisesCode, with Pending rewards count = 0"),
        TC_140__Positive__Pending_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards("Verify that when all required fields are provided, the GetAccountRewards endpoint returns success = true, ErrorCode = 0, and a response containing Pending reward(s) where at least one reward is a refer‑a‑friend reward including referenceCustomerCode and referencePremisesCode, with Active rewards count = 0"),

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
