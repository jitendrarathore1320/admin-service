package com.advantal.adminRoleModuleService.service;

import java.util.Map;

import com.advantal.adminRoleModuleService.requestpayload.ModuleRequest;

public interface ModuleService {

	Map<String, Object> addModule(ModuleRequest moduleRequest);

	Map<String, Object> getAllModule();


}
