package com.gng.api.pojo.Users.GetUserRoles;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class GetUserRolesRequest {
	private String loginID;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private String requestID;
	private String password;
}