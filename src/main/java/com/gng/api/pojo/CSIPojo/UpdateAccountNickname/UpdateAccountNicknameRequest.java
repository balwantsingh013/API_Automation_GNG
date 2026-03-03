package com.gng.api.pojo.CSIPojo.UpdateAccountNickname;

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
public class UpdateAccountNicknameRequest {

    private String requestID;    // Required, String(32)
    private String customerCode; // Required, String(9)
    private String premisesCode; // Required, String(7)
    private String nickname;     // Optional, String(20)
    private String loginID;      // Optional, String(30)
}
