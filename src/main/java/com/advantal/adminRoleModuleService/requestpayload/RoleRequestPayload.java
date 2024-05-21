package com.advantal.adminRoleModuleService.requestpayload;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RoleRequestPayload {
	
	@NotNull(message = "Role id can't be null !!")
	private Long id;

	@NotEmpty(message = "Role name can't be empty !!")
	private String roleName;
	
	@NotEmpty(message = "Role description can't be empty !!")
	private String roleDescription;
		
	private List<ModuleRequestPayload> moduleRequestList;
	
}
