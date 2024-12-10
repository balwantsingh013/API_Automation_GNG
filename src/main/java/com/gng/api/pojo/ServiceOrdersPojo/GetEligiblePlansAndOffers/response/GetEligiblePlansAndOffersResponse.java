package com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEligiblePlansAndOffersResponse{
	private Result data;
	private boolean success;
	private String requestID;
	private String errorMessage;
	private int errorCode;
}