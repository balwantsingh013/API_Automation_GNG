package com.gng.api.pages.AccountsApiPages.GetAccountInfoPage;

import lombok.Getter;

@Getter
public enum GetAccountInfoLabels {
    HAPPY_FLOW("GetAccountInfo_HappyFlow"),
    MISSING_REQUEST_ID("GetAccountInfo_MissingRequestId"),
    MISSING_PREM_CODE("GetAccountInfo_MissingPremCode"),
    MISSING_CUSTOMER_CODE("GetAccountInfo_MissingCustomerCode"),
    INVALID_PREM_CODE_LENGTH("GetAccountInfo_InvalidPremCodeLength"),
    INVALID_CUSTOMER_CODE_LENGTH("GetAccountInfo_InvalidCustomerCodeLength"),
    NONEXISTENT_CUST_PREM_CODE("GetAccountInfo_NonExistentCustomerPremCode");

    private final String label;

    GetAccountInfoLabels(String label) {
        this.label = label;
    }

}
