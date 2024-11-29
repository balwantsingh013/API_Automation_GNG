package com.gng.api.pojo.TestContext;

import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentResponse;
import io.restassured.response.Response;
import lombok.Data;
import com.gng.api.pojo.AccountsPojo.getAccountInfo.GetAccountInfoResponse;
import com.gng.api.pojo.AccountsPojo.createAccountNote.CreateAccountNoteResponse;
import lombok.Getter;
import lombok.Setter;

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

    // Add type-safe getters and setters
    // Use Object type to avoid direct dependency on test classes
    @Getter @Setter
    private Object getAccountInfoApiPage;
    private GetAccountInfoResponse getAccountInfoResponse;
    @Getter @Setter
    private Object createAccountNoteApiPage;
    private CreateAccountNoteResponse createAccountNoteResponse;
    @Getter @Setter
    private Object saveEnrollmentApiPage;
    private SaveEnrollmentResponse saveEnrollmentResponse;
}
