package com.advantal.adminRoleModuleService.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordPolicyRequestPayload {

	@NotEmpty(message = "Title can't be empty !!")
	@NotNull(message = "Title can't be null !!")
	private String title;

	@NotEmpty(message = "Description can't be empty !!")
	@NotNull(message = "Description can't be null !!")
	private String description;
	
	@NotEmpty(message = "policy_expiration_days can't be empty !!")
	@NotNull(message = "policy_expiration_days can't be null !!")
	private String policy_expiration_days;

}
