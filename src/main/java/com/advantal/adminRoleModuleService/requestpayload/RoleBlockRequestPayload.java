package com.advantal.adminRoleModuleService.requestpayload;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RoleBlockRequestPayload {
	
	@NotNull(message = "Role id can't be null !!")
	private Long id;
	
	@NotNull(message = "Status can't be null !!")
	private Short status;
	
}
