package com.advantal.adminRoleModuleService.responsepayload;

import java.util.List;

import lombok.Data;

@Data
public class RoleResponsePayload {

	private Long id;

	private String roleName;
	
	private String roleDescription;

	private Short status;

	private String entryDate;

	private String updationDate;

	private List<RoleModuleMappingResponse> roleModuleMappingResponseList;

}
