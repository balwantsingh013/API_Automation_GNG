package com.gng.api.pojo.AccountsPojo.SearchAccounts;

import lombok.Data;

@Data
public class SearchAccountsRequest{
	private String customerBusinessName;
	private Object premisesUnitNumber;
	private String loginID;
	private Object premisesStateCode;
	private Object federalTaxID;
	private Object aglcAccountNumber;
	private String socialSecurityNumber;
	private Object customerCode;
	private Object premisesUnitType;
	private Object premisesStreetNumber;
	private Object premisesCode;
	private Object premisesStreetPostDirection;
	private Object premisesCity;
	private String transactionType;
	private Object phoneNumber;
	private Object premisesStreetName;
	private String requestID;
	private String customerFirstName;
	private Object premisesStreetPreDirection;
	private String customerLastName;
	private Object premisesStreetSuffix;
	private String premisesZipCode;
}