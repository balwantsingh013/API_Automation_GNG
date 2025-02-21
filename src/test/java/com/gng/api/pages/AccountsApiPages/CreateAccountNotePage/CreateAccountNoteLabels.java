package com.gng.api.pages.AccountsApiPages.CreateAccountNotePage;

import lombok.Getter;

@Getter
public enum CreateAccountNoteLabels {
    HAPPY_FLOW("CreateAccountNote_HappyFlow"),
    MISSING_REQUEST_ID("CreateAccountNote_MissingRequestId"),
    NULL_CUSTOMER_CODE("CreateAccountNote_NullCustomerCode"),
    NULL_NOTE_TYPE_CODE("CreateAccountNote_NullTypeCode"),
    NULL_NOTE_TEXT("CreateAccountNote_NullNoteText"),
    NULL_ORIGIN("CreateAccountNote_NullOrigin"),
    INVALID_PREM_CODE_LENGTH("CreateAccountNote_InvalidPremCodeLength"),
    INVALID_CUSTOMER_CODE_LENGTH("CreateAccountNote_InvalidCustomerCodeLength"),
    INVALID_EXPIRATION_DATE("CreateAccountNote_InvalidExpirationDate"),
    NONEXISTENT_SERVICE_NO_PREM_CODE("CreateAccountNote_NonExistentServiceNoPremCode"),
    INVALID_SERVICE_NO_FORMAT("CreateAccountNote_InvalidServiceNoFormat"),
    NONEXISTENT_NOTE_TYPE("CreateAccountNote_NonExistentNoteType"),
    NONEXISTENT_CUST_PREM_CODE("GetAccountInfo_NonExistentCustomerPremCode");

    private final String label;

    CreateAccountNoteLabels(String label) {
        this.label = label;
    }

}
