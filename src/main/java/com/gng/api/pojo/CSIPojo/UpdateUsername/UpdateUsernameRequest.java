package com.gng.api.pojo.CSIPojo.UpdateUsername;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUsernameRequest {

    private String requestID;
    private String username;
    private String password;
    private String customerCode;
    private String premisesCode;
}
