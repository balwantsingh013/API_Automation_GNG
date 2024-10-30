package com.gng.api.constants;

/**
 * Label value should be same as payload filename(if used)
 * ex: For Payload file: CustomerEnroll.json the label used should be: CustomerEnroll
 */
public final class ApiLabel {

    public static final String GETACCOUNTINFO_HAPPYFLOW_API = "GetAccountInfo_HappyFlow";
    public static final String GETACCOUNTINFO_MISSINGREQUESTID_API = "GetAccountInfo_MissingRequestId";
    public static final String GETACCOUNTINFO_MISSINGPREMCODE_API = "GetAccountInfo_MissingPremCode";
    public static final String GETACCOUNTINFO_MISSINGCUSTOMERCODE_API = "GetAccountInfo_MissingCustomerCode";
    public static final String GETACCOUNTINFO_INVALIDPREMCODELENGTH_API = "GetAccountInfo_InvalidPremCodeLength";
    public static final String GETACCOUNTINFO_INVALIDCUSTOMERCODELENGTH_API = "GetAccountInfo_InvalidCustomerCodeLength";
    public static final String GETACCOUNTINFO_NONEXISTENT_CUSTPREMCODE_API = "GetAccountInfo_NonExistentCustomerPremCode";
    public static final String CREATEACCOUNTNOTE_HAPPYFLOW_API = "CreateAccountNote_HappyFlow";
    public static final String CREATEACCOUNTNOTE_MISSINGREQUESTID_API = "CreateAccountNote_MissingRequestId";
    public static final String CREATEACCOUNTNOTE_NULLCUSTOMERCODE_API = "CreateAccountNote_NullCustomerCode";
    public static final String CREATEACCOUNTNOTE_NULLNOTETYPECODE_API = "CreateAccountNote_NullTypeCode";
    public static final String CREATEACCOUNTNOTE_NULLNOTETEXT_API = "CreateAccountNote_NullNoteText";
    public static final String CREATEACCOUNTNOTE_NULLORIGIN_API = "CreateAccountNote_NullOrigin";
    public static final String CREATEACCOUNTNOTE_NONEXISTENT_SERVICENOPREMCODE_API = "CreateAccountNote_NonExistentServiceNoPremCode";
    public static final String CREATEACCOUNTNOTE_INVALID_SERVICENOFORMAT_API = "CreateAccountNote_InvalidServiceNoFormat";
    public static final String CREATEACCOUNTNOTE_NONEXISTENT_NOTETYPE_API = "CreateAccountNote_NonExistentNoteType";
    public static final String CREATEACCOUNTNOTE_INVALIDPREMCODELENGTH_API = "CreateAccountNote_InvalidPremCodeLength";
    public static final String CREATEACCOUNTNOTE_INVALIDCUSTOMERCODELENGTH_API = "CreateAccountNote_InvalidCustomerCodeLength";
    public static final String CREATEACCOUNTNOTE_INVALIDEXPIRATIONDATE_API = "CreateAccountNote_InvalidExpirationDate";

    private ApiLabel() {
    }

}
