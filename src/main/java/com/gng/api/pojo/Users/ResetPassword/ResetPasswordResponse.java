package com.gng.api.pojo.Users.ResetPassword;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordResponse{
	private String loginID;
	private String requestID;
	private String oldPassword;
	private String newPassword;
}