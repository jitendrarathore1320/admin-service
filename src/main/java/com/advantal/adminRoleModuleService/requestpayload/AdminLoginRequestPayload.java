package com.advantal.adminRoleModuleService.requestpayload;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor()
public class AdminLoginRequestPayload {

	@NotEmpty(message = "Email can't be empty !!")
	private String email;

	@NotEmpty(message = "Password can't be empty !!")
	private String password;
}
