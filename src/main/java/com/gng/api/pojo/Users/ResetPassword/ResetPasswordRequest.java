package com.gng.api.pojo.Users.ResetPassword;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetPasswordRequest{
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private String loginID;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private String requestID;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private String oldPassword;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private String newPassword;
}