package com.gng.api.pojo.CSIPojo.ValidateUsername;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidateUsernameRequest {

    private String requestID;   // Required, String(32)
    private String username;    // Required, String(15)
    private String loginID;     // Optional, String(30)
}
