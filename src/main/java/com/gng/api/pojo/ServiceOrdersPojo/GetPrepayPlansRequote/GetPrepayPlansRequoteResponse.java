package com.gng.api.pojo.ServiceOrdersPojo.GetPrepayPlansRequote;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPrepayPlansRequoteResponse {
    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private Object data; // Replace with actual data type if needed
}

