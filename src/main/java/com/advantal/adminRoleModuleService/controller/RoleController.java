package com.advantal.adminRoleModuleService.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advantal.adminRoleModuleService.requestpayload.RoleBlockRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.RoleRequestPayload;
import com.advantal.adminRoleModuleService.service.RoleService;
import com.advantal.adminRoleModuleService.utils.Constant;

//import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/role")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@PostMapping("/add_role")
//	@ApiOperation(value = "Add role details !!")
	public Map<String, Object> addRole(@RequestBody @Valid RoleRequestPayload roleRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		if (roleRequestPayload != null) {
			return roleService.addRole(roleRequestPayload);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

	@PostMapping(value = "/get_all_role")
//	@ApiOperation(value = "Get all roles")
	public Map<String, Object> getAllRole(@RequestParam(required = false) Integer pageIndex,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String searchText,
			@RequestParam(required = false) String status) {
		return roleService.getAllRole(pageIndex, pageSize, searchText, status);
	}

	@PostMapping("/block_role")
//	@ApiOperation(value = "To block/unblock role !! send status value, 2 for block and 1 for unblock role")
	public Map<String, Object> blockAdmin(@RequestBody @Valid RoleBlockRequestPayload roleBlockRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		if (roleBlockRequestPayload != null) {
			return roleService.blockRole(roleBlockRequestPayload);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

}
