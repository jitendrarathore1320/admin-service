package com.advantal.adminRoleModuleService.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advantal.adminRoleModuleService.requestpayload.ModuleRequest;
import com.advantal.adminRoleModuleService.service.ModuleService;
import com.advantal.adminRoleModuleService.utils.Constant;

//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/module")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ModuleController {

	@Autowired
	ModuleService moduleService;

	@PostMapping("/add_module")
//	@ApiOperation(value = "Add module details !!")
	public Map<String, Object> addModule(@RequestBody @Valid ModuleRequest moduleRequest) {
		Map<String, Object> map = new HashMap<>();
		if (moduleRequest != null) {
			return moduleService.addModule(moduleRequest);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST, moduleRequest);
			return map;
		}
	}

	@GetMapping(value = "/get_all_module")
//	@ApiOperation(value = "Get all module")
	public Map<String, Object> getAllModule() {
		return moduleService.getAllModule();
	}

}
