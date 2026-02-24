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
        TC_4__Negative__Missing_Username("Verify that if the username parameter is missing in the ValidateUsername endpoint request, the endpoint response will return data = null, success = false, errorCode = 10349, errorMessage = 'Missing Username'"),
        TC_5__Negative__Invalid_Username_format__Length___Too_Short____("Verify that if the provided username parameter length is smaller than 5 characters in the ValidateUsername endpoint request, the endpoint response will return data = null, success = false, errorCode = 10351, errorMessage = 'Invalid Username Length'"),
        TC_6__Negative__Invalid_Username_format__Length___Too_Long____("Verify that if the provided username parameter length is larger than 15 characters in the ValidateUsername endpoint request, the endpoint response will return data = null, success = false, errorCode = 10351, errorMessage = 'Invalid Username Length'"),
        TC_7__Negative__Invalid_Username_format__Alphanumeric("Verify that if the provided username parameter is not alphanumeric in the ValidateUsername endpoint request, the endpoint response will return data = null, success = false, errorCode = 10353, errorMessage = 'Invalid Username Format'"),
        TC_8__Positive__Username_Available("Verify that in the ValidateUsername endpoint request if the provided username does exist in MariaDb users table and active value of 1 and domain_id is NOT 2, the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'AVAILABLE'"),
        TC_9__Positive__Username_Available("Verify that in the ValidateUsername endpoint request if the provided username does not exist in MariaDb, the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'AVAILABLE'"),
        TC_10__Positive__Username_Active__users_table("Verify that in the ValidateUsername endpoint request if the provided username does exist in MariaDb users table and active value of 1 and domain_id is 2, the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'ACTIVE'"),
        TC_11__Positive__Username_Inactive("Verify that in the ValidateUsername endpoint request if the provided username does exist in MariaDb users table and active value of 0 and domain_id is 2, the endpoint response will return success = true, ErrorCode = 0, and usernameStatus = 'INACTIVE'"),

        TC_12__Negative__Missing_Request_ID("Verify that if the requestID parameter is missing in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_13__Negative__Invalid_Request_ID__Length("Verify that if the provided requestID parameter length is larger than 32 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_14__Negative__Duplicate_Request_ID("Verify that if the provided requestID parameter already exists within Transaction Manager while executing the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_15__Negative__Missing_Username("Verify that if the username parameter is missing in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10349, ErrorMessage = 'Missing Username'"),
        TC_16__Negative__Invalid_Username_format__Length___Too_Short____("Verify that if the provided username parameter length is smaller than 5 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10351, ErrorMessage = 'Invalid Username Length'"),
        TC_17__Negative__Invalid_Username_format__Length___Too_Long____("Verify that if the provided username parameter length is larger than 15 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10351, ErrorMessage = 'Invalid Username Length'"),
        TC_18__Negative__Invalid_Username_format__Alphanumeric("Verify that if the provided username parameter is not alphanumeric in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10353, ErrorMessage = 'Invalid Username Format'"),
        TC_19__Negative__Invalid_Username__Not_Found("Verify that if the provided username parameter is otherwise well formed and used in the UpdatePassword endpoint request but username does not exist in MariaDb users table where domain_id = 2, the endpoint response will return success = false, ErrorCode = 10357, ErrorMessage = 'Username Not Found'"),
        TC_20__Negative__Invalid_Username__Inactive("Verify that if the provided username parameter value does exist in MariaDb users table and active value of 0 and domain_id is 2 and is used in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10355, ErrorMessage = 'Inactive Username'"),
        TC_21__Negative__Missing_Password("Verify that if the password parameter value is missing in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10113, ErrorMessage = 'Missing Password'"),
        TC_22__Negative__Invalid_Password_Format__Length___Too_Short____("Verify that if the provided password parameter value length is shorter than 8 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10157, ErrorMessage = 'Invalid Password Length. The password must be between 8 and 64 characters.'"),
        TC_23__Negative__Invalid_Password_Format__Length___Too_Long____("Verify that if the provided password parameter value length is longer than 64 characters in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10157, ErrorMessage = 'Invalid Password Length. The password must be between 8 and 64 characters.'"),
        TC_24__Negative__Invalid_Password__Reused_Password("Verify that if the provided password parameter value (hashed value) matches the current password (the existing hash in the password column in the users table) in the UpdatePassword endpoint request, the endpoint response will return success = false, ErrorCode = 10359, ErrorMessage = 'Invalid Password'"),
        TC_25__Positive__Password__Updated("Verify that if valid username and password parameters are provided in the UpdatePassword endpoint request, the endpoint response will return success = true, ErrorCode = 0"),

        TC_26__Negative__Missing_Request_ID("Verify that if the requestID parameter is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_27__Negative__Invalid_Request_ID__Length("Verify that if the provided requestID parameter length is larger than 32 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_28__Negative__Duplicate_Request_ID("Verify that if the provided requestID parameter already exists within Transaction Manager while executing the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_29__Negative__Missing_Username("Verify that if the username parameter is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, errorCode = 10349, errorMessage = 'Missing Username'"),
        TC_30__Negative__Invalid_Username__Inactive("Verify that if the provided username parameter value does exist in MariaDb users table and active value of 0 and is used in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, errorCode = 10355, errorMessage = 'Inactive Username'"),
        TC_31__Negative__Invalid_Username_format__Length___Too_Short____("Verify that if the provided username parameter length is smaller than 5 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, errorCode = 10351, errorMessage = 'Invalid Username Length'"),
        TC_32__Negative__Invalid_Username_format__Length___Too_Long____("Verify that if the provided username parameter length is larger than 15 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, errorCode = 10351, errorMessage = 'Invalid Username Length'"),
        TC_33__Negative__Invalid_Username_format__Alphanumeric("Verify that if the provided username parameter is not alphanumeric in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, errorCode = 10353, errorMessage = 'Invalid Username Format'"),
        TC_34__Negative__Missing_Password("Verify that if the password parameter value is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, errorCode = 10113, errorMessage = 'Missing Password'"),
        TC_35__Negative__Invalid_Password_Format__Length___Too_Short____("Verify that if the provided password parameter value length is shorter than 8 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, errorCode = 10157, errorMessage = 'Invalid Password Format'"),
        TC_36__Negative__Invalid_Password_Format__Length___Too_Long____("Verify that if the provided password parameter value length is longer than 64 characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, errorCode = 10157, errorMessage = 'Invalid Password Format'"),
        TC_37__Negative__Account_username_already_linked("Verify that if the provided combination of customer code, premises code and username in the UpdateUsername endpoint request where the account is already linked to any username in Banner, the endpoint response will return success = false, action = null, errorCode = 10361, errorMessage = 'The account is already linked to a username'"),
        TC_38__Negative__Invalid_credentials___Password____("Verify that if the provided password and username are in the UpdateUsername endpoint request and the password does not match what is in the database where username exists, the endpoint response will return success = false, action = null, errorCode = 10363, errorMessage = 'Invalid Credentials'"),
        TC_39__Negative__Invalid_credentials___Username_Inactive____("Verify that if the provided password and username are in the UpdateUsername endpoint request and username exists in MariaDb users table with active value of 0, the endpoint response will return success = false, action = null, errorCode = 10363, errorMessage = 'Invalid Credentials'"),
        TC_40__Negative__Missing_customerCode("Verify that if the customerCode value is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, errorCode = 10011, errorMessage = 'Missing Customer Code'"),
        TC_41__Negative__Invalid_customerCode_Length("Verify that if the customerCode length is larger than 9 numeric characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_42__Negative__Invalid_customerCode_Format("Verify that if the customerCode value is not a string in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_43__Negative__Invalid_Account_Number("Verify that if the provided combination of customerCode and premisesCode in the UpdateUsername endpoint request does not exist in the UCBCUST table in Banner, the endpoint response will return success = false, action = null, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_44__Negative__Missing_premisesCode("Verify that if the premisesCode value is missing in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, errorCode = 10013, errorMessage = 'Missing Premises Code'"),
        TC_45__Negative__Invalid_premisesCode_Length("Verify that if the premisesCode length is larger than 7 numeric characters in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_46__Negative__Invalid_premisesCode_Format("Verify that if the premisesCode value is not a string in the UpdateUsername endpoint request, the endpoint response will return success = false, action = null, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_47__Positive__Username_Available___Banner_Active____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is AVAILABLE in MariaDb using an Account Number for a Banner account in an Active status, the endpoint response will return success = true, ErrorCode = 0, action = 'CREATED'"),
        TC_48__Positive__Username_Available___Banner_New____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is AVAILABLE in MariaDb using an Account Number for a Banner account in a New status, the endpoint response will return success = true, ErrorCode = 0, action = 'CREATED'"),
        TC_49__Positive__Username_Available___Banner_Final____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is AVAILABLE in MariaDb using an Account Number for a Banner account in a Final status, the endpoint response will return success = true, ErrorCode = 0, action = 'CREATED'"),
        TC_50__Positive__Username_Available___Banner_Inactive____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is AVAILABLE in MariaDb using an Account Number for a Banner account in an Inactive status, the endpoint response will return success = true, ErrorCode = 0, action = 'CREATED'"),
        TC_51__Positive__Username_Active___Banner_Active____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is ACTIVE in MariaDb using an Account Number for a Banner account in an Active status, the endpoint response will return success = true, ErrorCode = 0, action = 'UPDATED'"),
        TC_52__Positive__Username_Active___Banner_New____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is ACTIVE in MariaDb using an Account Number for a Banner account in a New status, the endpoint response will return success = true, ErrorCode = 0, action = 'UPDATED'"),
        TC_53__Positive__Username_Active___Banner_Final____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is ACTIVE in MariaDb using an Account Number for a Banner account in a Final status, the endpoint response will return success = true, ErrorCode = 0, action = 'UPDATED'"),
        TC_54__Positive__Username_Active___Banner_Inactive____("Verify that if all required fields are set in the UpdateUsername endpoint request and the username is ACTIVE in MariaDb using an Account Number for a Banner account in an Inactive status, the endpoint response will return success = true, ErrorCode = 0, action = 'UPDATED'"),

        TC_55__Negative__Missing_Request_ID("Verify that if the requestID parameter is missing in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_56__Negative__Invalid_Request_ID__Length("Verify that if the provided requestID parameter length is larger than 32 characters in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_57__Negative__Duplicate_Request_ID("Verify that if the provided requestID parameter already exists within Transaction Manager while executing the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_58__Negative__Missing_CustomerCode("Verify that if the customerCode value is missing in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, errorCode = 10011, errorMessage = 'Missing Customer Code'"),
        TC_59__Negative__Invalid_CustomerCode_Length("Verify that if the customerCode length is larger than 9 numeric characters in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_60__Negative__Invalid_CustomerCode_Format("Verify that if the customerCode value is not a numeric string in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_61__Negative__Invalid_Account_Number("Verify that if the provided combination of customerCode and premisesCode in the UpdateAccountNickname endpoint request does not exist in the UCBCUST table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_62__Negative__Missing_PremisesCode("Verify that if the premisesCode value is missing in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, errorCode = 10013, errorMessage = 'Missing Premises Code'"),
        TC_63__Negative__Invalid_PremisesCode_Length("Verify that if the premisesCode length is larger than 7 numeric characters in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_64__Negative__Invalid_PremisesCode_Format("Verify that if the premisesCode value is not a numeric string in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_65__Negative__Nickname_Not_Allowed_For_New_Account("Verify that if the customerCode and premisesCode for a Banner account in a New status and a nickname parameter is provided in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 40215, ErrorMessage = 'Nickname not allowed for new account'"),
        TC_66__Negative__Nickname_Already_Exists__Active("Verify that if the customerCode and premisesCode for a Banner account in an Active status where a nickname is set and a nickname parameter is provided that matches the existing nickname, the endpoint response will return success = false, ErrorCode = 40217, ErrorMessage = 'Nickname already exists for the account'"),
        TC_67__Negative__Nickname_Already_Exists__Final("Verify that if the customerCode and premisesCode for a Banner account in a Final status where a nickname is set and a nickname parameter is provided that matches the existing nickname, the endpoint response will return success = false, ErrorCode = 40217, ErrorMessage = 'Nickname already exists for the account'"),
        TC_68__Negative__Nickname_Already_Exists__Inactive("Verify that if the customerCode and premisesCode for a Banner account in an Inactive status where a nickname is set and a nickname parameter is provided that matches the existing nickname, the endpoint response will return success = false, ErrorCode = 40217, ErrorMessage = 'Nickname already exists for the account'"),
        TC_69__Negative__Nickname_Missing("Verify that if the customerCode and premisesCode for a Banner account not in a New status where a nickname is not set and a nickname parameter is not provided in the UpdateAccountNickname endpoint request, the endpoint response will return success = false, ErrorCode = 40219, ErrorMessage = 'Nickname does not exist in the request'"),
        TC_70__Positive__Nickname_Set__Active("Verify that if all required fields are set and a value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in an Active status where an existing nickname is not set, the endpoint response will return success = true, ErrorCode = 0 and the provided nickname is associated to the Banner account"),
        TC_71__Positive__Nickname_Set__Final("Verify that if all required fields are set and a value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in a Final status where an existing nickname is not set, the endpoint response will return success = true, ErrorCode = 0 and the provided nickname is associated to the Banner account"),
        TC_72__Positive__Nickname_Set__Inactive("Verify that if all required fields are set and a value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in an Inactive status where an existing nickname is not set, the endpoint response will return success = true, ErrorCode = 0 and the provided nickname is associated to the Banner account"),
        TC_73__Positive__Nickname_Updated__Active("Verify that if all required fields are set and a value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in an Active status where an existing nickname is set, the endpoint response will return success = true, ErrorCode = 0 and a new nickname is associated to the Banner account"),
        TC_74__Positive__Nickname_Updated__Final("Verify that if all required fields are set and a value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in a Final status where an existing nickname is set, the endpoint response will return success = true, ErrorCode = 0 and a new nickname is associated to the Banner account"),
        TC_75__Positive__Nickname_Updated__Inactive("Verify that if all required fields are set and a value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in an Inactive status where an existing nickname is set, the endpoint response will return success = true, ErrorCode = 0 and a new nickname is associated to the Banner account"),
        TC_76__Positive__Nickname_Removed__Active("Verify that if all required fields are set and an EMPTY value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in an Active status where an existing nickname is set, the endpoint response will return success = true, ErrorCode = 0 and the nickname associated to the Banner account is set to EMPTY"),
        TC_77__Positive__Nickname_Removed__Final("Verify that if all required fields are set and an EMPTY value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in a Final status where an existing nickname is set, the endpoint response will return success = true, ErrorCode = 0 and the nickname associated to the Banner account is set to EMPTY"),
        TC_78__Positive__Nickname_Removed__Inactive("Verify that if all required fields are set and an EMPTY value for the nickname parameter is provided in the UpdateAccountNickname endpoint request for a Banner account in an Inactive status where an existing nickname is set, the endpoint response will return success = true, ErrorCode = 0 and the nickname associated to the Banner account is set to EMPTY"),
        TC_79__Positive__LoginID_Saved("Verify that if a value is provided for the optional loginID parameter in the UpdateAccountNickname endpoint request and the rest of the request is well formed, the endpoint response will return success = true, ErrorCode = 0 and the loginID value is saved in the GZRAPIL_LOGIN_ID field in the GZRAPIL table"),

        TC_80__Negative__Missing_Request_ID("Verify that if the requestID parameter is missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_81__Negative__Invalid_Request_ID_Length("Verify that if the provided requestID parameter length is larger than 32 characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_82__Negative__Duplicate_Request_ID("Verify that if the provided requestID parameter already exists within Transaction Manager while executing the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_83__Negative__Missing_customerCode("Verify that if the customerCode value is missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, errorCode = 10011, errorMessage = 'Missing Customer Code'"),
        TC_84__Negative__Invalid_customerCode_Length("Verify that if the customerCode length is larger than 9 numeric characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_85__Negative__Invalid_customerCode_Format("Verify that if the customerCode value is not a numeric string in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_86__Negative__Invalid_Account_Number("Verify that if the provided combination of customerCode and premisesCode in the UpdateMailingAddress endpoint request does not exist in the UCBCUST table in Banner, the endpoint response will return success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_87__Negative__Missing_premisesCode("Verify that if the premisesCode value is missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, errorCode = 10013, errorMessage = 'Missing Premises Code'"),
        TC_88__Negative__Invalid_premisesCode_Length("Verify that if the premisesCode length is larger than 7 numeric characters in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_89__Negative__Invalid_premisesCode_Format("Verify that if the premisesCode value is not a numeric string in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_90__Negative__Invalid_Address_Fields_Missing("Verify that if streetName and ruralRoute and poBox are all missing in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10365, ErrorMessage = 'Invalid Address Fields'"),
        TC_91__Negative__Invalid_Address_Fields_Too_Many("Verify that if a combination of two or more of streetName, ruralRoute and/or poBox are provided in the UpdateMailingAddress endpoint request, the endpoint response will return success = false, ErrorCode = 10365, ErrorMessage = 'Invalid Address Fields'"),
        TC_92__Negative__Invalid_Street_Number_Length("Verify that if the streetNumber parameter is provided and populated in the UpdateMailingAddress endpoint request but the length is larger than 12 characters, the endpoint response will return success = false, errorCode = 10146, errorMessage = 'Invalid Street Number Format'"),
        TC_93__Negative__Invalid_Street_Pre_Direction_Length("Verify that if the streetPreDirection parameter is provided and populated in the UpdateMailingAddress endpoint request but the length is larger than 2 characters, the endpoint response will return success = false, errorCode = 10148, errorMessage = 'Invalid Street Pre-Direction Format'"),
        TC_94__Negative__Invalid_Street_Pre_Direction("Verify that if the streetPreDirection parameter is provided and populated in the UpdateMailingAddress endpoint request but the value does not exist in the Banner reference table, the endpoint response will return success = false, errorCode = 40227, errorMessage = 'Invalid Street Pre-Direction'"),
        TC_95__Negative__Invalid_Street_Name_Length("Verify that if the streetName parameter is provided and populated in the UpdateMailingAddress endpoint request but the length is larger than 30 characters, the endpoint response will return success = false, errorCode = 10133, errorMessage = 'Invalid Street Name Format'"),
        TC_96__Negative__Missing_StreetName("Verify that if the streetName parameter is not provided in the UpdateMailingAddress endpoint request and the remainder of the request is well formed for a street address, the endpoint response will return success = false, ErrorCode = 10365, ErrorMessage = 'Invalid Address Fields'"),
        TC_97__Negative__Invalid_StreetSuffix_Length("Verify that if the streetSuffix parameter is provided and populated in the UpdateMailingAddress endpoint request but the length is larger than 6 characters, the endpoint response will return success = false, errorCode = 10149, errorMessage = 'Invalid Street Suffix Format'"),
        TC_98__Negative__Invalid_StreetSuffix("Verify that if the streetSuffix parameter is provided and populated in the UpdateMailingAddress endpoint request but the value does not exist in the Banner reference table, the endpoint response will return success = false, errorCode = 40221, errorMessage = 'Invalid Street Suffix'"),
        TC_99__Negative__Invalid_Street_Post_Direction_Length("Verify that if the streetPostDirection parameter is provided and populated in the UpdateMailingAddress endpoint request but the length is larger than 2 characters, the endpoint response will return success = false, errorCode = 10150, errorMessage = 'Invalid Street Post-Direction Format'"),
        TC_100__Negative__Invalid_Street_Post_Direction("Verify that if the streetPostDirection parameter is provided and populated in the UpdateMailingAddress endpoint request but the value does not exist in the Banner reference table, the endpoint response will return success = false, errorCode = 40229, errorMessage = 'Invalid Street Post-Direction'"),
        TC_101__Negative__Invalid_Unit_Type_Format("Verify that if the unitType parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 6 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10151, errorMessage = 'Invalid Unit Type Format'"),
        TC_102__Negative__Invalid_Unit_Type("Verify that if the unitType parameter is provided in the UpdateMailingAddress endpoint request is populated, but the value does not exist in the Banner reference table, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 40223, errorMessage = 'Invalid Street Unit Type'"),
        TC_103__Negative__Invalid_Unit_Number_Format("Verify that if the unitNumber parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 6 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10135, errorMessage = 'Invalid Unit Number Format'"),
        TC_104__Negative__Invalid_City_Length("Verify that if the city parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 20 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10135, errorMessage = 'Invalid Unit Type Format'"),
        TC_105__Negative__Missing_City("Verify that if the city parameter is NOT provided in the UpdateMailingAddress endpoint request, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, ErrorCode = 10089, ErrorMessage = 'Missing City'"),
        TC_106__Negative__Invalid_Zip_Code_Format_Length("Verify that if the zipCode parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 10 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10147, errorMessage = 'Invalid Zip Code Format'"),
        TC_107__Negative__Missing_Zip_Code("Verify that if the zipCode parameter is NOT provided in the UpdateMailingAddress endpoint request, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, ErrorCode = 10091, ErrorMessage = 'Missing Zip Code'"),
        TC_108__Negative__Invalid_Zip_Code("Verify that if the zipCode parameter is provided in the UpdateMailingAddress endpoint request is populated, but the value does not exist in the Banner reference table, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 40225, errorMessage = 'Invalid Zip Code'"),
        TC_109__Negative__Invalid_City_and_Zip_Code_Combination("Verify that if the zipCode and City parameters are provided in the UpdateMailingAddress endpoint request is populated, the values are well-formed/correct but the values do not represent a valid combination and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 40237, errorMessage = 'Invalid City and Zip combination'"),
        TC_110__Negative__Invalid_Delivery_Point_Format("Verify that if the deliveryPoint parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 2 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10367, errorMessage = 'Invalid Delivery Point Format'"),
        TC_111__Negative__Invalid_Carrier_Route_Length("Verify that if the carrierRoute parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 4 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10369, errorMessage = 'Invalid Carrier Route Format'"),
        TC_112__Negative__Invalid_Attention_To_Length("Verify that if the attentionTo parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 30 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10373, errorMessage = 'Invalid Attention To Format'"),
        TC_113__Negative__Invalid_Additional_Address_Line_Length("Verify that if the additionalAddressLine parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 30 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10373, errorMessage = 'Invalid Attention To Format'"),
        TC_114__Negative__Invalid_Rural_Route_Length("Verify that if the ruralRoute parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 30 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10255, errorMessage = 'Invalid Rural Route Format'"),
        TC_115__Negative__Invalid_PO_Box_Length("Verify that if the poBox parameter is provided in the UpdateMailingAddress endpoint request is populated, but the length is larger than 23 characters, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10259, errorMessage = 'Invalid PO Box Format'"),
        TC_116__Negative__Invalid_PO_Box_Format("Verify that if the poBox parameter is provided in the UpdateMailingAddress endpoint request is populated, but is not a number, and the remainder of the request is well formed with the appropriate/required data, the endpoint response will return success = false, errorCode = 10259, errorMessage = 'Invalid PO Box Format'"),
        TC_117__Positive__Valid_Street_Address___Minimum_parameters_and_No_Existing_Address____("Verify that if a well formed request with the minimum required parameters for a Street-type address is provided in the UpdateMailingAddress endpoint request where no address exists, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and a new row is added to the Banner table UCRADDR with all of the data from the fields provided in request, the current date is set in UCRADDR_FROM_DATE, no value is set for UCRADDR_TO_DATE and UCRADDR_STATUS_IND is set to 'A'"),
        TC_118__Positive__Valid_Street_Address___Maximum_parameters_and_No_Existing_Address____("Verify that if a well formed request with the maximum required parameters for a Street-type address is provided in the UpdateMailingAddress endpoint request where no address exists, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and a new row is added to the Banner table UCRADDR with all of the data from the fields provided in request, the current date is set in UCRADDR_FROM_DATE, no value is set for UCRADDR_TO_DATE and UCRADDR_STATUS_IND is set to 'A'"),
        TC_119__Positive__Valid_Street_Address___Mixed_parameters_and_No_Existing_Address____("Verify that if a well formed request with a mix of valid parameters, including the minimum required parameters, for a Street-type address is provided in the UpdateMailingAddress endpoint request where NO ADDRESS exists, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and a new row is added to the Banner table UCRADDR with all of the data from the fields provided in request, the current date is set in UCRADDR_FROM_DATE, no value is set for UCRADDR_TO_DATE and UCRADDR_STATUS_IND is set to 'A'"),
        TC_120__Positive__Valid_Street_Address___Minimum_parameters_and_Existing_Address_and_Same_Day____("Verify that if a well formed request with the minimum required parameters for a Street-type address is provided in the UpdateMailingAddress endpoint request where a matching address exists AND the start date is the same as the current date, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and the existing row is updated with the values provided in the request. UCRADDR_FROM_DATE, UCRADDR_TO_DATE and UCRADDR_STATUS_IND remain unmodified"),
        TC_121__Positive__Valid_Street_Address___Maximum_parameters_and_Existing_Address_and_Same_Day____("Verify that if a well formed request with the maximum required parameters for a Street-type address is provided in the UpdateMailingAddress endpoint request where a matching address exists AND the start date is the same as the current date, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and the existing row is updated with the values provided in the request. UCRADDR_FROM_DATE, UCRADDR_TO_DATE and UCRADDR_STATUS_IND remain unmodified"),
        TC_122__Positive__Valid_Street_Address___Mixed_parameters_and_Existing_Address_and_Same_Day____("Verify that if a well formed request with a mix of valid parameters, including the minimum required parameters, for a Street-type address is provided in the UpdateMailingAddress endpoint request where a matching address exists AND the start date is the same as the current date, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and the existing row is updated with the values provided in the request. UCRADDR_FROM_DATE, UCRADDR_TO_DATE and UCRADDR_STATUS_IND remain unmodified"),
        TC_123__Positive__Valid_Street_Address___Minimum_parameters_and_Existing_Address_and_Different_Day____("Verify that if a well formed request with the minimum required parameters for a Street-type address is provided in the UpdateMailingAddress endpoint request where a matching address exists AND the start date is AFTER the current date, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and a new row is added with the values provided in the request. UCRADDR_FROM_DATE for the new row is set to the current date and UCRADDR_TO_DATE is left blank. UCRADDR_TO_DATE on the existing row is set to current date - 1. UCRADDR_STATUS_IND for the new row is set to 'A' and UCRADDR_STATUS_IND for the existing row is set to 'I'"),
        TC_124__Positive__Valid_Street_Address___Maximum_parameters_and_Existing_Address_and_Different_Day____("Verify that if a well formed request with the maximum required parameters for a Street-type address is provided in the UpdateMailingAddress endpoint request where a matching address exists AND the start date is AFTER the current date, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and a new row is added with the values provided in the request. UCRADDR_FROM_DATE for the new row is set to the current date and UCRADDR_TO_DATE is left blank. UCRADDR_TO_DATE on the existing row is set to current date - 1. UCRADDR_STATUS_IND for the new row is set to 'A' and UCRADDR_STATUS_IND for the existing row is set to 'I'"),
        TC_125__Positive__Valid_Street_Address___Mixed_parameters_and_Existing_Address_and_Different_Day____("Verify that if a well formed request with a mix of valid parameters, including the minimum required parameters, for a Street-type address is provided in the UpdateMailingAddress endpoint request where a matching address exists AND the start date is AFTER the current date, the endpoint response will return success = true, errorCode = 0, errorMessage = '', and a new row is added with the values provided in the request. UCRADDR_FROM_DATE for the new row is set to the current date and UCRADDR_TO_DATE is left blank. UCRADDR_TO_DATE on the existing row is set to current date - 1. UCRADDR_STATUS_IND for the new row is set to 'A' and UCRADDR_STATUS_IND for the existing row is set to 'I'"),
        TC_126__Positive__Valid_PO_Box_Address("Verify that if the request is well formed with the appropriate/required data for a PO Box-type address is provided in the UpdateMailingAddress endpoint request, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the value for the PO Box number provided is prefixed with 'PO BOX' followed by the number and saved in UCRADDR.UCRADDR_STREET_LINE2."),
        TC_127__Positive__Valid_Rural_Route_Address("Verify that if the request is well formed with the appropriate/required data for a Rural Route-type address is provided in the UpdateMailingAddress endpoint request, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the value for the Rural Route provided is saved in UCRADDR.UCRADDR_STREET_LINE2 as it was entered."),
        TC_128__Positive__LoginID_Saved("Verify that if a value is provided for the optional loginID parameter in the UpdateMailingAddress endpoint request and the rest of the request is well formed the endpoint response will return success = true, ErrorCode = 0, and loginID value is saved/stored in the GZRAPIL_LOGIN_ID field in the GZRAPIL table"),

        TC_129__Negative__Missing_Request_ID("Verify that if the requestID parameter is missing in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_130__Negative__Invalid_Request_ID_Length("Verify that if the provided requestID parameter length is larger than 32 characters in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_131__Negative__Duplicate_Request_ID("Verify that if the provided requestID parameter already exists within Transaction Manager while executing the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_132__Negative__Missing_customerCode("Verify that if the customerCode value is missing in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, errorCode = 10011, errorMessage = 'Missing Customer Code'"),
        TC_133__Negative__Invalid_customerCode_Length("Verify that if the customerCode length is larger than 9 numeric characters in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_134__Negative__Invalid_customerCode_Format("Verify that if the customerCode value is not a numeric string in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_135__Negative__Invalid_Account_Number("Verify that if the provided combination of customerCode and premisesCode in the GetAccountRewards endpoint request doesn't exist in the UCBCUST table in Banner, the endpoint response will return data = null, success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_136__Negative__Missing_premisesCode("Verify that if the premisesCode value is missing in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, errorCode = 10013, errorMessage = 'Missing Premises Code'"),
        TC_137__Negative__Invalid_premisesCode_Length("Verify that if the premisesCode length is larger than 7 numeric characters in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_138__Negative__Invalid_premisesCode_Format("Verify that if the premisesCode value is not a numeric string in the GetAccountRewards endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_139__Positive__Rewards_Response_Returned_with_Active_Rewards("Verify that if all required fields are set in the GetAccountRewards endpoint request, the endpoint response will return success = true, ErrorCode = 0 and a response will be populated with the details of the account’s rewards for an account with Active reward(s). The response should contain three reward sections: 1. The total number of Active rewards greater than 0. 2. The total number of Pending rewards equals 0. 3. An array of the active reward(s) that contains: rewardID, rewardName, rewardTypeCode, rewardTypeDescription, status, rewardEstablishedDate, rewardInitiatedDate, remainingOccurrences, totalOccurrences, restrictions, referenceCustomerCode, referencePremisesCode"),
        TC_140__Positive__Rewards_Response_Returned_with_Pending_Rewards("Verify that if all required fields are set in the GetAccountRewards endpoint request, the endpoint response will return success = true, ErrorCode = 0 and a response will be populated with the details of the account’s rewards for an account with Pending reward(s). The response should contain three reward sections: 1. The total number of Active rewards equals 0. 2. The total number of Pending rewards greater than 0. 3. An array of the pending reward(s) that contains: rewardID, rewardName, rewardTypeCode, rewardTypeDescription, status, rewardEstablishedDate, rewardInitiatedDate, remainingOccurrences, totalOccurrences, restrictions, referenceCustomerCode, referencePremisesCode"),
        TC_141__Positive__Rewards_Response_Returned_with_Active_and_Pending_Rewards("Verify that if all required fields are set in the GetAccountRewards endpoint request, the endpoint response will return success = true, ErrorCode = 0 and a response will be populated with the details of the account’s rewards for an account with both active and pending reward(s). The response should contain: 1. The total number of Active rewards and the total number of Pending rewards. 2. An array of the active reward(s) with all required fields. 3. An array of the pending reward(s) with all required fields"),
        TC_142__Positive__Rewards_Response_Returned_with_No_Rewards("Verify that if all required fields are set in the GetAccountRewards endpoint request, the endpoint response will return success = true, ErrorCode = 0 and a response will be returned for an account with no pending or active rewards. The response should contain: 1. Active rewards count = 0. 2. Pending rewards count = 0. 3. An empty array of rewards"),
        TC_143__Positive__Active_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards("Verify that if all required fields are set in the GetAccountRewards endpoint request, the endpoint response will return success = true, ErrorCode = 0 and a response will be populated for an account with active reward(s) including at least one refer-a-friend reward with referenceCustomerCode and referencePremisesCode. The response should contain: 1. Total Active rewards. 2. Pending rewards count = 0. 3. An array of active reward(s) with all required fields including referenceCustomerCode and referencePremisesCode"),
        TC_144__Positive__Pending_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards("Verify that if all required fields are set in the GetAccountRewards endpoint request, the endpoint response will return success = true, ErrorCode = 0 and a response will be populated for an account with pending reward(s) including at least one refer-a-friend reward with referenceCustomerCode and referencePremisesCode. The response should contain: 1. Active rewards count = 0. 2. Total Pending rewards. 3. An array of pending reward(s) with all required fields including referenceCustomerCode and referencePremisesCode"),

        TC_142__Negative__Missing_Request_ID("Verify that if the requestID parameter is missing in the GetAccountInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_143__Negative__Invalid_Request_ID__Length("Verify that if the provided requestID parameter length is larger than 32 characters in the GetAccountInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_144__Negative__Duplicate_Request_ID("Verify that if the provided requestID parameter already exists within Transaction Manager while executing the GetAccountInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_145__Negative__Invalid_customerCode_Length("Verify that if the customerCode length is larger than 9 numeric characters in the GetAccountInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_146__Negative__Invalid_customerCode_Format("Verify that if the customerCode value is not a numeric string in the GetAccountInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_147__Negative__Invalid_premisesCode_Length("Verify that if the premisesCode length is larger than 7 numeric characters in the GetAccountInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_148__Negative__Invalid_premisesCode_Format("Verify that if the premisesCode value is not a numeric string in the GetAccountInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_149__Negative__Invalid_Account_Number("Verify that if the provided combination of customerCode and premisesCode in the GetAccountInfo endpoint request doesn't exist in the UCBCUST table in Banner, the endpoint response will return data = null, success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_150__Positive__Account_Info_Returned___Email_Address____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has an email address, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes the expected data in emailAddress parameter of the response"),
        TC_151__Positive__Account_Info_Returned___No_Email_Address____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has no email address, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes null in emailAddress parameter of the response"),
        TC_152__Positive__Account_Info_Returned___Partner_Promotions_Indicator__equals_Y____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has indicated they want to receive Partner Promotions, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'Y' in partnerPromotionsIndicator parameter of the response"),
        TC_153__Positive__Account_Info_Returned___Partner_Promotions_Indicator__equals_N____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has indicated they do not want to receive Partner Promotions, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'N' in partnerPromotionsIndicator parameter of the response"),
        TC_154__Positive__Account_Info_Returned___Partner_Promotions_Indicator_is_null____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided have not indicated either way regarding receiving Partner Promotions, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'N' in partnerPromotionsIndicator parameter of the response"),
        TC_155__Positive__Account_Info_Returned___Marketing_Offers_Indicator__equals_Y____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has indicated they want to receive Marketing Offers Promotions, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'Y' in marketingOffersIndicator parameter of the response"),
        TC_156__Positive__Account_Info_Returned___Marketing_Offers_Indicator__equals_N____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has indicated they do not want to receive Partner Marketing Offers, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'N' in marketingOffersIndicator parameter of the response"),
        TC_157__Positive__Account_Info_Returned___Marketing_Offers_Indicator_is_null____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided have not indicated either way regarding receiving Partner Marketing Offers, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'N' in marketingOffersIndicator parameter of the response"),
        TC_158__Positive__Account_Info_Returned___Account__and_Billing_Reminder__equals_Y____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has indicated they want to receive Account & Billing Reminders, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'Y' in accountBillingReminder parameter of the response"),
        TC_159__Positive__Account_Info_Returned___Account__and_Billing_Reminder__equals_N____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has indicated they do not want to receive Account & Billing Reminders, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'N' in accountBillingReminder parameter of the response"),
        TC_160__Positive__Account_Info_Returned___Account__and_Billing_Reminder_is_null____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided have not indicated either way regarding receiving Account & Billing Reminders, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'N' in accountBillingReminder parameter of the response"),
        TC_161__Positive__Account_Info_Returned___Primary_Phone_is_Home____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided only has a Home Phone Number designated as Primary, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes the expected Home Phone Number in the phoneNumber parameter of the response"),
        TC_162__Positive__Account_Info_Returned___Primary_Phone_is_Work____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided only has a Business Phone Number designated as Primary, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes the expected Business Phone Number in the phoneNumber parameter of the response"),
        TC_163__Positive__Account_Info_Returned___Primary_Phone_is_both_Home_and_Work____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has both a Home and Business Phone Number both designated as Primary, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes the expected Home Phone Number in the phoneNumber parameter of the response"),
        TC_164__Positive__Account_Info_Returned___No_Phone_Number____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has no Phone Number set, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes null in the phoneNumber parameter of the response"),
        TC_165__Positive__Account_Info_Returned___Greener_Life_Rate____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has Greener Life Rate, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes the expected Rate Plan in the greenerLifeRate parameter of the response"),
        TC_166__Positive__Account_Info_Returned___No_Greener_Life_Rate____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has no Greener Life Rate, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes null in the greenerLifeRate parameter of the response"),
        TC_167__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Paper____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has indicated they want Paper Billing, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'P' in the billDeliveryOption parameter of the response"),
        TC_168__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Electronic____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has indicated they want Electronic Billing, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'E' in the billDeliveryOption parameter of the response"),
        TC_169__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Fiserv_E__Bill____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has indicated they want Fiserv E-Bill Billing, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'F' in the billDeliveryOption parameter of the response"),
        TC_170__Positive__Account_Info_Returned___Bill_Delivery_Option_is_null____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has not indicated a billing preference, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'P' in the billDeliveryOption parameter of the response"),
        TC_171__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_Paper____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has indicated they want Paper Correspondence, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'P' in the correspondenceDeliveryOption parameter of the response"),
        TC_172__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_Electronic____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has indicated they want Electronic Correspondence, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'E' in the correspondenceDeliveryOption parameter of the response"),
        TC_173__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_null____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has not indicated a correspondence preference, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes 'P' in the correspondenceDeliveryOption parameter of the response"),
        TC_174__Positive__Account_Info_Returned___Single_Price_Plan____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has only one Price Plan available to them, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes only the one price plan in the pricePlans array parameter of the response"),
        TC_175__Positive__Account_Info_Returned___Multiple_Price_Plans____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has multiple Price Plans available to them, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and includes all expected price plans in the pricePlans array parameter of the response"),
        TC_176__Positive__Account_Info_Returned___No_Guaranteed_Bill_Plan____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has no Guaranteed Bill Price Plan available to them, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and for the non-Guaranteed Plan(s) returned in the pricePlans array parameter, the planGBPAmount has no data and the planThermPrice and planServiceCharge parameters have data in the response"),
        TC_177__Positive__Account_Info_Returned___Guaranteed_Bill_Plan____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has a Guaranteed Bill Price Plan available to them, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and for the Guaranteed Plan(s) returned in the pricePlans array parameters the planGBPAmount parameter contains the expected data and the planThermPrice and planServiceCharge parameters have no data in the response"),
        TC_178__Positive__Account_Info_Returned___No_Price_Protection_Guarantee_Plan____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has no Price Protection Guarantee (PPG) Price Plan(s) available to them, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and for the non-PPG Plan(s) returned in the pricePlans array parameter, the priceProtectionGuaranteeFee and priceProtectionGuaranteeCeiling parameters have no data in the response"),
        TC_179__Positive__Account_Info_Returned___Price_Protection_Guarantee_Plan____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has Price Protection Guarantee (PPG) Price Plan(s) available to them, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and for the PPG Plan(s) returned in the pricePlans array parameter, the priceProtectionGuaranteeFee and priceProtectionGuaranteeCeiling parameters have the expected data in the response"),
        TC_180__Positive__Account_Info_Returned___No_Rollover___ACR______Plans____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has no Rollover (ACR) Price Plan(s) available to them, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and for the non-ACR Plan(s) returned in the pricePlans array parameter, the rolloverPlanIndicator parameter is set to 'N' in the response"),
        TC_181__Positive__Account_Info_Returned___Rollover___ACR______Plans____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has Rollover (ACR) Price Plan(s) available to them, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and for the ACR Plan(s) returned in the pricePlans array parameter, the rolloverPlanIndicator parameter is set to 'Y' in the response"),
        TC_182__Positive__Account_Info_Returned___Non__Restricted_Plans____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has no Restricted Price Plan(s) available to them, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and for the non-Restricted Plan(s) returned in the pricePlans array parameter, the restrictedPlanIndicator parameter is set to 'N' in the response"),
        TC_183__Positive__Account_Info_Returned___Single_Discount____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has a Single Discount, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and only one Discount is returned in the Discounts array in the response"),
        TC_184__Positive__Account_Info_Returned___Multiple_Discounts____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has Multiple Discounts, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and all expected Discounts are returned in the Discounts array in the response"),
        TC_185__Positive__Account_Info_Returned___No_Discounts____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has No Discounts, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and no discounts are returned in the Discounts array (an empty array is returned) in the response"),
        TC_186__Positive__Account_Info_Returned___Transferable_Discount____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has a Discount(s) that is Transferrable, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and Discounts that are returned in the Discounts array that are transferrable have the parameter discountTransferabilityIndicator = 'Y' in the response"),
        TC_187__Positive__Account_Info_Returned___Non__Transferable_Discount____("Verify that if all required parameters (requestID, customerCode, premisesCode) are set in the GetAccountInfo endpoint request, and the account provided has a Discount(s) that is Not Transferrable, the endpoint response will return success = true, errorCode = 0, errorMessage = '' and the response payload parameters contain all of the available data for the test account used and Discounts that are returned in the Discounts array that are not transferrable have the parameter discountTransferabilityIndicator = 'N' in the response"),

        TC_191__Negative__Missing_Request_ID("Verify that if the requestID parameter is missing in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_192__Negative__Invalid_Request_ID_Length("Verify that if the provided requestID parameter length is larger than 32 characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_193__Negative__Duplicate_Request_ID("Verify that if the provided requestID parameter already exists within Transaction Manager while executing the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_194__Negative__Invalid_customerCode_Length("Verify that if the customerCode length is larger than 9 numeric characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_195__Negative__Invalid_customerCode_Format("Verify that if the customerCode value is not a numeric string in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_196__Negative__Invalid_premisesCode_Length("Verify that if the premisesCode length is larger than 7 numeric characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_197__Negative__Invalid_premisesCode_Format("Verify that if the premisesCode value is not a numeric string in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_198__Negative__Invalid_Last_or_Business_Name_Length("Verify that if the customerLastNameBusiness length is larger than 60 characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10119, ErrorMessage = 'Invalid Last or Business Name Format'"),
        TC_199__Negative__Invalid_First_Name_Length("Verify that if the customerFirstName length is larger than 15 characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10121, ErrorMessage = 'Invalid First Name Format'"),
        TC_200__Negative__Invalid_Last_Four_SSN_Length_Too_Long("Verify that if the lastFourSocialSecurityNumber length is larger than 4 characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10063, ErrorMessage = 'Invalid Last Four SSN Format'"),
        TC_201__Negative__Invalid_Last_Four_SSN_Length_Too_Short("Verify that if the lastFourSocialSecurityNumber length is smaller than 4 characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10063, ErrorMessage = 'Invalid Last Four SSN Format'"),
        TC_202__Negative__Invalid_Federal_Tax_ID_Length_Too_Long("Verify that if the federalTaxID length is larger than 9 characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10069, ErrorMessage = 'Invalid Federal Tax ID Format'"),
        TC_203__Negative__Invalid_Federal_Tax_ID_Length_Too_Short("Verify that if the federalTaxID length is smaller than 9 characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10069, ErrorMessage = 'Invalid Federal Tax ID Format'"),
        TC_204__Negative__Invalid_Email_Address_Format("Verify that if the emailAddress format is not {identifier}@{domain name}.{domain extension} in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10065, ErrorMessage = 'Invalid Email Address Format'"),
        TC_205__Negative__Invalid_Phone_Number_Length_Too_Long("Verify that if the phoneNumber length is larger than 10 characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10009, ErrorMessage = 'Invalid Phone Number Format'"),
        TC_206__Negative__Invalid_Phone_Number_Length_Too_Short("Verify that if the phoneNumber length is smaller than 10 characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10009, ErrorMessage = 'Invalid Phone Number Format'"),
        TC_207__Negative__Invalid_Username_Format("Verify that if username contains non-alphanumeric characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10353, ErrorMessage = 'Invalid Username Format'"),
        TC_208__Negative__Inactive_Username("Verify that in the SearchAccounts request if the provided username does exist in MariaDb and is Inactive, the endpoint response will return data = null, success = false, ErrorCode = 10355, ErrorMessage = 'Inactive Username'"),
        TC_209__Negative__Invalid_Password_Format_Length_Too_Short("Verify that if the provided password parameter value length is shorter than 8 characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10157, ErrorMessage = 'Invalid Password Length. The password must be between 8 and 64 characters.'"),
        TC_210__Negative__Invalid_Password_Format_Length_Too_Long("Verify that if the provided password parameter value length is longer than 64 characters in the SearchAccounts endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10157, ErrorMessage = 'Invalid Password Length. The password must be between 8 and 64 characters.'"),
        TC_211__Negative__Last4SSN_____Password("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (lastFourSocialSecurityNumber + password), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_212__Negative__Last4SSN_____Username("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (lastFourSocialSecurityNumber + username), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_213__Negative__Last4SSN_____CustomerCode_____PremisesCode("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (lastFourSocialSecurityNumber + CustomerCode + premisesCode), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_214__Negative__Last4SSN_____FederalTaxID_Number("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (lastFourSocialSecurityNumber + federalTaxID), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_215__Negative__Password_____FederalTaxID_Number("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (password + federalTaxID), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_216__Negative__CustomerLastName_____EmailAddress("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (customerLastNameBusiness + emailAddress), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_217__Negative__CustomerLastName_____PhoneNumber("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (customerLastNameBusiness + phoneNumber), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_218__Negative__CustomerLastName_____Username("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (customerLastNameBusiness + username), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_219__Negative__CustomerLastName_____CustomerCode_____PremisesCode("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (customerLastNameBusiness + CustomerCode + premisesCode), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_220__Negative__EmailAddress_____PhoneNumber("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (emailAddress + phoneNumber), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_221__Negative__EmailAddress_____Username("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (emailAddress + username), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_222__Negative__EmailAddress_____CustomerCode_____PremisesCode("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (emailAddress + CustomerCode + premisesCode), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_223__Negative__EmailAddress_____FederalTaxID_Number("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (emailAddress + federalTaxID), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_224__Negative__PhoneNumber_____Username("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (phoneNumber + username), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_225__Negative__PhoneNumber_____CustomerCode_____PremisesCode("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (phoneNumber + CustomerCode + premisesCode), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_226__Negative__PhoneNumber_____FederalTaxID_Number("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (phoneNumber + federalTaxID), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_227__Negative__Username_____CustomerCode_____PremisesCode("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (username + CustomerCode + premisesCode), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_228__Negative__Username_____FederalTaxID_Number("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (username + federalTaxID), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_229__Negative__CustomerCode_____PremisesCode_____FederalTaxID_Number("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but an invalid combination of parameters is used (CustomerCode + premisesCode + federalTaxID), the endpoint response will return data = null, success = false, ErrorCode = 10377, ErrorMessage = 'Insufficient Search Criteria'"),
        TC_230__Negative__Too_Many_Matches("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search but the number of search results exceeds the pre-defined SEARCH_LIMIT (2000 records), the endpoint response will return data = null, success = false, ErrorCode = 10391, ErrorMessage = 'Too Many Matches'"),
        TC_231__Positive__Last4SSN_____CustomerLastName("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using lastFourSocialSecurityNumber + 1 additional identification (customerLastNameBusiness), the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results."),
        TC_232__Positive__Last4SSN_____EmailAddress("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using lastFourSocialSecurityNumber + 1 additional identification (emailAddress), the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results."),
        TC_233__Positive__Last4SSN_____PhoneNumber("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using lastFourSocialSecurityNumber + 1 additional identification (phoneNumber), the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results."),
        TC_234__Positive__Password_____Username("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using password + 1 additional identification (username), the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results."),
        TC_235__Positive__Password_____CustomerLastNameBusiness("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using password + 1 additional identification (customerLastNameBusiness), the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results."),
        TC_236__Positive__Password_____CustomerCode_____PremisesCode("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using password + 1 additional identification (customerCode+premisesCode), the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results."),
        TC_237__Positive__Password_____EmailAddress("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using password + 1 additional identification (emailAddress), the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results."),
        TC_238__Positive__Password_____PhoneNumber("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using password + 1 additional identification (phoneNumber), the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results."),
        TC_239__Positive__BusinessName_____FederalTaxID("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using customerLastNameBusiness + federalTaxID, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results."),
        TC_240__Positive__AccountStatus_Active("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations for an Active Account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the accountStatus parameter in the response is set to 'A'."),
        TC_241__Positive__AccountStatus_Final("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations for a Final Account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the accountStatus parameter in the response is set to 'F'."),
        TC_242__Positive__AccountStatus_Inactive("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations for an Inactive Account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the accountStatus parameter in the response is set to 'I'."),
        TC_243__Positive__AccountStatus_New("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations for a New Account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the accountStatus parameter in the response is set to 'N'."),
        TC_244__Positive__Username("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where a username is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the username parameter in the response is set to the expected value."),
        TC_245__Positive__No_Username("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where a username is NOT set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the username parameter in the response is an empty string."),
        TC_246__Positive__Nickname("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where a nickname is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the nickname parameter in the response is set to the expected value."),
        TC_247__Positive__No_Nickname("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where a nickname is NOT set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the nickname parameter in the response is an empty string."),
        TC_248__Positive__FirstName("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where a First Name is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the firstName parameter in the response is set to the expected value."),
        TC_249__Positive__No_FirstName("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where a First Name is NOT set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the firstName parameter in the response is an empty string."),
        TC_250__Positive__Residential("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations for a Residential Account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the customerType parameter in the response is set to 'RS'."),
        TC_251__Positive__Commercial("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations for a Commercial Account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the customerType parameter in the response is set to 'CM'."),
        TC_252__Positive__Industrial("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations for an Industrial Account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the customerType parameter in the response is set to 'IN'."),
        TC_253__Positive__Agriculture("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations for an Agriculture Account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the customerType parameter in the response is set to 'AG'."),
        TC_254__Positive__MultiFamily("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations for a Multi-Family Account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the customerType parameter in the response is set to 'MF'."),
        TC_255__Positive__Seasonal("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations for a Seasonal Account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the customerType parameter in the response is set to 'SE'."),
        TC_256__Positive__SeniorCitizen("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations for a Senior Citizen Account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the customerType parameter in the response is set to 'SR'."),
        TC_257__Positive__PremisesAddress_StreetNumber("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Street Number is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesStreetNumber in the response is set to the expected value."),
        TC_258__Positive__PremisesAddress_No_StreetNumber("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Street Number is NOT set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesStreetNumber in the response is set to an empty string."),
        TC_259__Positive__PremisesAddress_StreetPreDirection("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Street Pre-Direction is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesStreetPreDirection in the response is set to the expected value."),
        TC_260__Positive__PremisesAddress_No_StreetPreDirection("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Street Pre-Direction is NOT set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesStreetPreDirection in the response is set to an empty string."),
        TC_261__Positive__PremisesAddress_StreetName("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Street Name is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesStreetName in the response is set to the expected value."),
        TC_262__Positive__PremisesAddress_StreetSuffix("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Street Suffix is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesStreetSuffix in the response is set to the expected value."),
        TC_263__Positive__PremisesAddress_No_StreetSuffix("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Street Suffix is NOT set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesStreetSuffix in the response is set to an empty string."),
        TC_264__Positive__PremisesAddress_StreetPostDirection("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Street Post-Direction is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesStreetPostDirection in the response is set to the expected value."),
        TC_265__Positive__PremisesAddress_No_StreetPostDirection("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Street Post-Direction is NOT set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesStreetPostDirection in the response is set to an empty string."),
        TC_266__Positive__PremisesAddress_UnitType("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Unit Type is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesUnitType in the response is set to the expected value."),
        TC_267__Positive__PremisesAddress_No_UnitType("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Unit Type is NOT set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesUnitType in the response is set to an empty string."),
        TC_268__Positive__PremisesAddress_UnitNumber("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Unit Number is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesUnitNumber in the response is set to the expected value."),
        TC_269__Positive__PremisesAddress_No_UnitNumber("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Unit Number is NOT set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesUnitNumber in the response is set to an empty string."),
        TC_270__Positive__PremisesAddress_City("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the City is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesCity in the response is set to the expected value."),
        TC_271__Positive__PremisesAddress_No_City("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the City is NOT set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesCity in the response is set to an empty string."),
        TC_272__Positive__PremisesAddress_State("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the State is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesState in the response is set to the expected value."),
        TC_273__Positive__PremisesAddress_No_State("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the State is NOT set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesState in the response is set to an empty string."),
        TC_274__Positive__PremisesAddress_ZipCode("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Zip Code is set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesZipCode in the response is set to the expected value."),
        TC_275__Positive__PremisesAddress_No_ZipCode("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the Zip Code is NOT set for the account, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' along with the expected response payload with the results and the expected premisesZipCode in the response is set to an empty string."),
        TC_276__Positive__Search_Order__Active_______Final_______New_______Inactive("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the customer has at least one account of EACH Banner Status Active, Final, New AND Inactive accounts, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the order Active -> Final -> New -> Inactive."),
        TC_277__Positive__Search_Order__Active_______Final_______New("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the customer has at least one account of EACH Banner Status Active, Final and New accounts, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the order Active -> Final -> New."),
        TC_278__Positive__Search_Order__Active_______Final_______Inactive("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the customer has at least one account of EACH Banner Status Active, Final and Inactive accounts, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the order Active -> Final -> Inactive."),
        TC_279__Positive__Search_Order__Active_______Final("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the customer has at least one account of EACH Banner Status Active and Final accounts, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the order Active -> Final."),
        TC_280__Positive__Search_Order__Active_______New_______Inactive("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the customer has at least one account of EACH Banner Status Active, New and Inactive accounts, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the order Active -> New -> Inactive."),
        TC_281__Positive__Search_Order__Active_______New("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the customer has at least one account of EACH Banner Status Active and New accounts, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the order Active -> New."),
        TC_282__Positive__Search_Order__Active_______Inactive("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the customer has at least one account of EACH Banner Status Active and Inactive accounts, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the order Active -> Inactive."),
        TC_283__Positive__Search_Order__Final_______New_______Inactive("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the customer has at least one account of EACH Banner Status Final, New and Inactive accounts, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the order Final -> New -> Inactive."),
        TC_284__Positive__Search_Order__Final_______New("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the customer has at least one account of EACH Banner Status Final and New accounts, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the order Final -> New."),
        TC_285__Positive__Search_Order__Final_______Inactive("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the customer has at least one account of EACH Banner Status Final and Inactive accounts, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the order Final -> Inactive."),
        TC_286__Positive__Search_Order__New_______Inactive("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the customer has at least one account of EACH Banner Status New and Inactive accounts, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the order New -> Inactive."),
        TC_287__Positive__Search_Order__Active_Name_order("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the result set has at least two accounts in an Active Banner Status and the Customer Last Name is the same and the Customer First Names are not the same, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the alphabetical order by First Name + Last Name."),
        TC_288__Positive__Search_Order__Final_Name_order("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the result set has at least two accounts in an Final Banner Status and the Customer Last Name is the same and the Customer First Names are not the same, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the alphabetical order by First Name + Last Name."),
        TC_289__Positive__Search_Order__New_Name_order("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the result set has at least two accounts in an New Banner Status and the Customer Last Name is the same and the Customer First Names are not the same, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the alphabetical order by First Name + Last Name."),
        TC_290__Positive__Search_Order__Inactive_Name_order("Verify that if the SearchAccounts endpoint request contains the minimum required fields for a valid search using one of the available combinations where the result set has at least two accounts in an Inactive Banner Status and the Customer Last Name is the same and the Customer First Names are not the same, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = ‘’ along with the expected response payload with the results and the expected accounts are returned in the alphabetical order by First Name + Last Name."),

        TC_01__Negative__Missing_Request_ID("Verify that if the requestID parameter is missing in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_02__Negative__Invalid_Request_ID__Length("Verify that if the provided requestID parameter length is larger than 32 characters in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC_03__Negative__Duplicate_Request_ID("Verify that if the provided requestID parameter already exists within Transaction Manager while executing the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_04__Negative__Missing_customerCode("Verify that if the customerCode value is missing in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10011, ErrorMessage = 'Missing Customer Code'"),
        TC_05__Negative__Invalid_customerCode__Length("Verify that if the customerCode length is larger than 9 numeric characters in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_06__Negative__Invalid_customerCode__Format("Verify that if the customerCode value is not a numeric string in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_07__Negative__Missing_premisesCode("Verify that if the premisesCode value is missing in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10013, ErrorMessage = 'Missing Premises Code'"),
        TC_08__Negative__Invalid_premisesCode__Length("Verify that if the premisesCode length is larger than 7 numeric characters in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_09__Negative__Invalid_premisesCode__Format("Verify that if the premisesCode value is not a numeric string in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_10__Negative__Invalid_Account_Number("Verify that if the provided combination of customerCode and premisesCode in the GetUsageHistory endpoint request doesn't exist in the UCBCUST table in Banner, the endpoint response will return data = null, success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_11__Negative__Missing_Number_of_Months("Verify that if the numberOfMonths parameter is missing in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10393, ErrorMessage = 'Missing Number of Months'"),
        TC_12__Negative__Invalid_Number_of_Months__Format__Not_a_Number("Verify that if the numberOfMonths parameter is not a numerical value in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10395, ErrorMessage = 'Missing Number of Months'"),
        TC_13__Negative__Invalid_Number_of_Months__Format__Zero_Value("Verify that if the numberOfMonths parameter is Zero (0) in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10395, ErrorMessage = 'Missing Number of Months'"),
        TC_14__Negative__Invalid_Number_of_Months__Format__Negative_Number("Verify that if the numberOfMonths parameter is a negative numerical value in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10395, ErrorMessage = 'Invalid Number of Months'"),
        TC_15__Negative__Invalid_Number_of_Months__Length("Verify that if the numberOfMonths parameter is a positive numerical value of three (3) or more digits in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10395, ErrorMessage = 'Invalid Number of Months'"),
        TC_16__Negative__New_Account_Not_Allowed("Verify that if the customerCode and premisesCode for a Banner account in a 'New' status and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 40271, ErrorMessage = 'Operation not allowed for new account'"),
        TC_17__Positive__Usage_History_Service_Number_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters are well formed and provided, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains a serviceNumber in the expected String(1) format"),
        TC_18__Positive__Usage_History_Bill_Date_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters are well formed and provided, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains a billDate in the expected String(8) format"),
        TC_19__Positive__Usage_History_Usage_From_Date_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters are well formed and provided, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains a usageFromDate in the expected String(8) formatted as YYYYMMDD"),
        TC_20__Positive__Usage_History_Usage_To_Date_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters are well formed and provided, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains a usageToDate in the expected String(8) formatted as YYYYMMDD"),
        TC_21__Positive__Usage_History_Average_Daily_Actual_Consumption_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains an averageDailyActualConsumption in the expected Number(12,3) format"),
        TC_22__Positive__Usage_History_Average_Daily_Billed_Consumption_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains an averageDailyBilledConsumption in the expected Number(12,3) format"),
        TC_23__Positive__Usage_History_Total_Billed_Consumption_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains a totalBilledConsumption in the expected Number(12,3) format"),
        TC_24__Positive__Usage_History_Days_Of_Service_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains daysOfService in the expected Number format"),
        TC_25__Positive__Usage_History_Reading_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains a reading in the expected Number(12,3) format"),
        TC_26__Positive__Usage_History_Read_Type_Format__Actual("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history where at least one service period has an 'Actual' meter reading and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains a readTypeCode that equals 'A' corresponding to the 'Actual' meter reading in Banner"),
        TC_27__Positive__Usage_History_Read_Type_Format__Zero_Consumption("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history where at least one service period has a 'Zero Consumption' meter reading and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains a readTypeCode that equals 'Z' corresponding to the 'Zero Consumption' meter reading in Banner"),
        TC_28__Positive__Usage_History_Read_Type_Format__Estimated("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history where at least one service period has an 'Estimated' meter reading and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains a readTypeCode that equals 'E' corresponding to the 'Estimated' meter reading in Banner"),
        TC_29__Positive__Usage_History_Read_Date_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains a readDate in the expected String(8) formatted as YYYYMMDD"),
        TC_30__Positive__Usage_History_Average_Temperature_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains an averageTemperature in the expected Number format"),
        TC_31__Positive__Usage_History_Heating_Degree_Days_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains a heatingDegreeDays in the expected Number format"),
        TC_32__Positive__Usage_History_Bill_History_Transaction_Format("Verify that if the customerCode and premisesCode for a Banner account not in a 'New' status with at least 1 month of usage history and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the usageHistory array contains a billHistoryTransactionNumber in the expected Number(15) format"),
        TC_33__Positive__No_Usage_History_Active("Verify that if the customerCode and premisesCode for a Banner account in an 'Active' status with no months of usage history and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response contains a numberOfMatches value of '0' and an empty usageHistory array"),
        TC_34__Positive__No_Usage_History_Final("Verify that if the customerCode and premisesCode for a Banner account in a 'Final' status with no months of usage history and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response contains a numberOfMatches value of '0' and an empty usageHistory array"),
        TC_35__Positive__No_Usage_History_Inactive("Verify that if the customerCode and premisesCode for a Banner account in an 'Inactive' status with no months of usage history and all other required parameters (requestID, numberOfMonths) are well formed and provided in the GetUsageHistory endpoint request, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response contains a numberOfMatches value of '0' and an empty usageHistory array"),
        TC_36__Positive__Usage_History_Too_Old_Valid_Number_of_Months("Verify that if the customerCode and premisesCode for a Banner account in an 'Active' or 'Final' or 'Inactive' status with at least 1 month of usage history and all other required parameters (requestID) are well formed, and numberOfMonths contains any valid value but the most recent usage history is older than the numberOfMonths value, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response contains a numberOfMatches value of '0' and an empty usageHistory array"),
        TC_37__Positive__Usage_History_Less_Than_Requested_Months("Verify that if the customerCode and premisesCode for a Banner account in an 'Active' or 'Final' or 'Inactive' status with at least 1 month of usage history but fewer months than the numberOfMonths requested and all other required parameters (requestID) are well formed, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response contains a numberOfMatches value equal to the available months of usage history and the usageHistory array contains the required minimum data for each returned month"),
        TC_38__Positive__Usage_History_Equals_Requested_Months("Verify that if the customerCode and premisesCode for a Banner account in an 'Active' or 'Final' or 'Inactive' status with 12 months of usage history and all other required parameters (requestID) are well formed, and numberOfMonths contains 12, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response contains a numberOfMatches value equal to 12 and the usageHistory array contains the required minimum data for each of the 12 months"),
        TC_39__Positive__Usage_History_Greater_Than_Requested_Months("Verify that if the customerCode and premisesCode for a Banner account in an 'Active' or 'Final' or 'Inactive' status with usage history greater than 12 months and all other required parameters (requestID) are well formed, and numberOfMonths contains 12, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response contains a numberOfMatches value of 12 and the usageHistory array contains the required minimum data for the most recent 12 months"),

        TC1__Negative__Missing_Request_ID("Verify that if the requestID parameter is missing in the GetBankDraftInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10001, ErrorMessage = 'Missing Request ID'"),
        TC_2__Negative__Invalid_Request_ID_Length("Verify that if the provided requestID parameter length is larger than 32 characters in the GetBankDraftInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10002, ErrorMessage = 'Invalid Request ID'"),
        TC3__Negative__Duplicate_Request_ID("Verify that if the provided requestID parameter already exists within Transaction Manager while executing the GetBankDraftInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10003, ErrorMessage = 'Duplicate Request ID'"),
        TC_4__Negative__Missing_CustomerCode("Verify that if the customerCode value is missing in the GetBankDraftInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10011, ErrorMessage = 'Missing Customer Code'"),
        TC_5__Negative__Invalid_CustomerCode_Length("Verify that if the customerCode length is larger than 9 numeric characters in the GetBankDraftInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_6__Negative__Invalid_CustomerCode_Format("Verify that if the customerCode value is not a numeric string in the GetBankDraftInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10015, ErrorMessage = 'Invalid Customer Code Format'"),
        TC_7__Negative__Missing_PremisesCode("Verify that if the premisesCode value is missing in the GetBankDraftInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10013, ErrorMessage = 'Missing Premises Code'"),
        TC_8__Negative__Invalid_PremisesCode_Length("Verify that if the premisesCode length is larger than 7 numeric characters in the GetBankDraftInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC_9__Negative__Invalid_PremisesCode_Format("Verify that if the premisesCode value is not a numeric string in the GetBankDraftInfo endpoint request, the endpoint response will return data = null, success = false, ErrorCode = 10005, ErrorMessage = 'Invalid Premises Code Format'"),
        TC10__Negative__Invalid_Account_Number("Verify that if the provided combination of customerCode and premisesCode in the GetBankDraftInfo endpoint request doesn't exist in the UCBCUST table in Banner, the endpoint response will return data = null, success = false, ErrorCode = 40015, ErrorMessage = 'Invalid Account Number'"),
        TC_11__Positive__No_BankDraft_Info("Verify that if the GetBankDraftInfo endpoint request contains the minimum required fields for a valid search (requestID, customerCode, premisesCode) for a Banner Account that does NOT have an Automatic Bank Draft configuration set up, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response payload contains bankDraftStatus = null and all other fields below are returned as ''"),
        TC_12__Positive__BankDraftStatus_Active("Verify that if the GetBankDraftInfo endpoint request contains the minimum required fields for a valid search (requestID, customerCode, premisesCode) for a Banner Account that does have an Automatic Bank Draft configuration in an Active status, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response payload contains bankDraftStatus = ACTIVE and the rest of the response should contain the expected values for the record used"),
        TC_13__Positive__BankDraftStatus_PreNotification("Verify that if the GetBankDraftInfo endpoint request contains the minimum required fields for a valid search (requestID, customerCode, premisesCode) for a Banner Account that does have an Automatic Bank Draft configuration in a Pre-Notification status, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response payload contains bankDraftStatus = PRENOTIFICATION and the rest of the response should contain the expected values for the record used"),
        TC_14__Positive__BankDraftStatus_Canceled("Verify that if the GetBankDraftInfo endpoint request contains the minimum required fields for a valid search (requestID, customerCode, premisesCode) for a Banner Account that does have an Automatic Bank Draft configuration in a Canceled status, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response payload contains bankDraftStatus = CANCELED and the rest of the response should contain the expected values for the record used"),
        TC_15__Positive__BankDraftStatus_Inactive("Verify that if the GetBankDraftInfo endpoint request contains the minimum required fields for a valid search (requestID, customerCode, premisesCode) for a Banner Account that does have an Automatic Bank Draft configuration in an Inactive status, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response payload contains bankDraftStatus = INACTIVE and the rest of the response should contain the expected values for the record used"),
        TC_16__Positive__BankRoutingNumber("Verify that if the GetBankDraftInfo endpoint request contains the minimum required fields for a valid search (requestID, customerCode, premisesCode) for a Banner Account that does have an Automatic Bank Draft configuration and contains a Bank Routing Number, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response payload contains bankDraftRoutingNumber with the expected Bank Routing Number value that is masked except the final four digits and the rest of the response should contain the expected values for the record used"),
        TC_17__Positive__BankAccountNumber("Verify that if the GetBankDraftInfo endpoint request contains the minimum required fields for a valid search (requestID, customerCode, premisesCode) for a Banner Account that does have an Automatic Bank Draft configuration and contains a Bank Account Number, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response payload contains bankDraftAccountNumber with the expected Bank Account Number value that is masked except the final four digits and the rest of the response should contain the expected values for the record used"),
        TC_18__Positive__BankAccountType_Checking("Verify that if the GetBankDraftInfo endpoint request contains the minimum required fields for a valid search (requestID, customerCode, premisesCode) for a Banner Account that does have an Automatic Bank Draft configuration and contains a Bank Account Type of Checking, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response payload contains bankDraftAccountType with the expected value of CHECKING and the rest of the response should contain the expected values for the record used"),
        TC_19__Positive__BankAccountType_Savings("Verify that if the GetBankDraftInfo endpoint request contains the minimum required fields for a valid search (requestID, customerCode, premisesCode) for a Banner Account that does have an Automatic Bank Draft configuration and contains a Bank Account Type of Savings, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response payload contains bankDraftAccountType with the expected value of SAVINGS and the rest of the response should contain the expected values for the record used"),
        TC_20__Positive__BankName("Verify that if the GetBankDraftInfo endpoint request contains the minimum required fields for a valid search (requestID, customerCode, premisesCode) for a Banner Account that does have an Automatic Bank Draft configuration and contains a Bank Name, the endpoint response will return success = true, ErrorCode = 0, ErrorMessage = '' and the response payload contains bankName with the expected Bank Name value and the rest of the response should contain the expected values for the record used");












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
