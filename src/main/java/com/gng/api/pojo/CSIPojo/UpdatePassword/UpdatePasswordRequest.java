package com.gng.api.pojo.CSIPojo.UpdatePassword;

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
public class UpdatePasswordRequest {

    private String requestID;
    private String username;
    private Object password;
}
