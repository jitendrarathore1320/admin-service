package com.advantal.adminRoleModuleService.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor()
public class LoginRequestPayload {

	@NotEmpty(message = "Email can't be empty !!")
	private String email;

	@NotNull(message = "OTP can't be null !!")
	private String otp;
}
