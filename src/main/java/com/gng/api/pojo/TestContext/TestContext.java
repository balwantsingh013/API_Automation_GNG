package com.gng.api.pojo.TestContext;

import com.gng.api.pages.CreateAccountNoteApiPage;
import com.gng.api.pages.GetAccountInfoApiPage;
import com.gng.api.pojo.accountinfo.GetAccountInfoResponse;
import com.gng.api.pojo.createaccountnote.CreateAccountNoteResponse;
import io.restassured.response.Response;
import lombok.Data;

@Data
public class TestContext {
    private Response response;
    private String authToken;
    private String customerCode;
    private String premisesCode;
    private String noteSequenceNumber;
    private String requestId;
    private String serviceNumber;
    private String noteTypeCode;
    private String noteText;
    private String origin;
    private String expirationDate;
    private String suspenseDate;
    private String userIDRemind;
    private GetAccountInfoApiPage getAccountInfoApiPage;
    private GetAccountInfoResponse getAccountInfoResponse;
    private CreateAccountNoteApiPage createAccountNoteApiPage;
    private CreateAccountNoteResponse createAccountNoteResponse;
}
