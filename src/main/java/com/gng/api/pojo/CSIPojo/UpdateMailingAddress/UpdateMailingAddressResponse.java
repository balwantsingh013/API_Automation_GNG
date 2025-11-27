package com.gng.api.pojo.CSIPojo.UpdateMailingAddress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMailingAddressResponse {

    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;
    private String data;
}
