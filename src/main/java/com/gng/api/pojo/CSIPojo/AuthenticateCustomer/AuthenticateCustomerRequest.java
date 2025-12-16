package com.gng.api.pojo.CSIPojo.AuthenticateCustomer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateCustomerRequest {

    private String requestID;
    private String customerCode;
    private String premisesCode;
    private String customerLastNameBusiness;
    private String customerFirstName;
    private String lastFourSocialSecurityNumber;
    private String federalTaxID;
    private String emailAddress;
    private String phoneNumber;
    private String username;
    private String password;
    private String loginID;
}
