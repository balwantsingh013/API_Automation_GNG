package com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimilarBusinesses {

    private String businessName;
    private String businessStreet;
    private String businessCity;
    private String businessState;
    private String businessZipCode;
    private String businessPhoneNumber;
    private String businessBIN;

}