package com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEligiblePlansAndOffersResponse{
	private DataResult data;
	private boolean success;
	private String requestID;
	private String errorMessage;
	private int errorCode;
}