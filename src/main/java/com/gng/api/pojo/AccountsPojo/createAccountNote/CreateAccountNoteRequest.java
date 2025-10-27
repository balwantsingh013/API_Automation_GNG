package com.gng.api.pojo.AccountsPojo.createAccountNote;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateAccountNoteRequest {
    private String requestID;
    private String customerCode;
    private String premisesCode;
    private String serviceNumber;
    private String noteTypeCode;
    private String noteText;
    private String origin;
    private String expirationDate;
    private String suspenseDate;
    private String userIDRemind;
}
