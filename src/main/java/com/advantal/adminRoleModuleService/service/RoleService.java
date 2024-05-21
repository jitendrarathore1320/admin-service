package com.advantal.adminRoleModuleService.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.advantal.adminRoleModuleService.models.Role;
import com.advantal.adminRoleModuleService.requestpayload.RoleBlockRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.RoleRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.SearchRequestPayload;

public interface RoleService {

	Map<String, Object> addRole(RoleRequestPayload roleRequestPayload);

//	Map<String, Object> getAllRole(Integer pageIndex, Integer pageSize, String searchText);

	Map<String, Object> blockRole(RoleBlockRequestPayload roleBlockRequestPayload);

	Map<String, Object> getAllRole(Integer pageIndex, Integer pageSize, String searchText, String status);

	List<Role> getAllRoleDetails(String searchText);	


}
