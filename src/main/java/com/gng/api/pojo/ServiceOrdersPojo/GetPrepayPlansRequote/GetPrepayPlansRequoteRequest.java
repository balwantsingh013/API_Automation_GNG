package com.gng.api.pojo.ServiceOrdersPojo.GetPrepayPlansRequote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPrepayPlansRequoteRequest {
    private String requestID;
    private String loginID;
    private String transactionID;
    private String transactionType;
}

