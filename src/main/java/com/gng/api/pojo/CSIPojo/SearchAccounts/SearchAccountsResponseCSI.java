package com.gng.api.pojo.CSIPojo.SearchAccounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchAccountsResponseCSI {
    private String requestID;
    private boolean success;
    private int errorCode;
    private String errorMessage;
    private int numberOfMatches;
    private List<com.gng.api.pojo.CSIPojo.AuthenticateCustomer.AuthenticateCustomerResponse.Account> accounts;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Account {
        private String customerCode;
        private String premisesCode;
        private String accountStatus;
        private String username;
        private String nickname;
        private String customerFirstName;
        private String customerLastNameBusiness;
        private String customerType;
        private String premisesStreetNumber;
        private String premisesStreetPreDirection;
        private String premisesStreetName;
        private String premisesStreetSuffix;
        private String premisesStreetPostDirection;
        private String premisesUnitType;
        private String premisesUnitNumber;
        private String premisesCity;
        private String premisesStateCode;
        private String premisesZipCode;
    }
}
