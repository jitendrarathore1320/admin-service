package com.advantal.adminRoleModuleService.requestpayload;


import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class ModuleRequest {
	
	@NotEmpty(message = "Module name can't be null or blank !!")
	private String moduleName;
	
	@NotEmpty(message = "Parent module name can't be null or blank !!")
	private String parentModuleName;
	
	@NotEmpty(message = "Module code can't be null or blank !!")
	private String moduleCode;

}
