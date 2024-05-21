package com.advantal.adminRoleModuleService.requestpayload;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ModuleRequestPayload {

	@NotEmpty(message = "Module id can't be null or empty !!")
	private Long id;// moduleId
	
	@NotNull(message = "Module action can't be null !!")
	private Short moduleAction;
	
	@NotEmpty(message = "Module name can't be null or blank !!")
	private String moduleName;
	
	@NotEmpty(message = "Module parent name can't be null or blank !!")
	private String parentModuleName;//
	
	@NotEmpty(message = "Module code can't be null or blank !!")
	private String moduleCode;//

	@NotNull(message = "Add action can't be null !!")
	private Short addAction;

	@NotNull(message = "Update action can't be null !!")
	private Short updateAction;

	@NotNull(message = "Delete action can't be null !!")
	private Short deleteAction;

	@NotNull(message = "View action can't be null !!")
	private Short viewAction;

	@NotNull(message = "Download action can't be null !!")
	private Short downloadAction;

}
