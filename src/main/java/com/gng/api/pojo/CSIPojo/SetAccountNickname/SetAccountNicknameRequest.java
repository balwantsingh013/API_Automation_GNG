package com.gng.api.pojo.CSIPojo.SetAccountNickname;

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
public class SetAccountNicknameRequest {

    private String requestID;      // Globally unique identifier for the request
    private String customerCode;   // Up to 9-digit numeric customer code
    private String premisesCode;   // Up to 7-digit numeric premises code
    private String nickname;       // Optional nickname (max 20 characters)
}
