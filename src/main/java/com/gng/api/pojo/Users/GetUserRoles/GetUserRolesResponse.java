package com.gng.api.pojo.Users.GetUserRoles;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserRolesResponse{
	private String loginID;
	private String requestID;
	private String password;
}