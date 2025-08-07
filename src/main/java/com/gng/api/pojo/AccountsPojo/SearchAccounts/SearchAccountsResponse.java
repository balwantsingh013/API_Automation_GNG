package com.gng.api.pojo.AccountsPojo.SearchAccounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchAccountsResponse {
	private SearchAccountsData data;
	private boolean success;
	private int errorCode;
	private String errorMessage;
	private String requestID;
}
