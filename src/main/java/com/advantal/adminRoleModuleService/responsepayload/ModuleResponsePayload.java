package com.advantal.adminRoleModuleService.responsepayload;

import lombok.Data;

@Data
public class ModuleResponsePayload {

	private Long id;

	private String uuId;

	private String parentModuleName;//

	private String moduleName;

	private String moduleCode;//

	private Short addAction;

	private Short updateAction;

	private Short deleteAction;

	private Short viewAction;

	private Short downloadAction;

	private Short status;
}
