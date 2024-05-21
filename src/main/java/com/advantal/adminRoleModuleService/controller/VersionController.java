package com.advantal.adminRoleModuleService.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.advantal.adminRoleModuleService.models.Version;
import com.advantal.adminRoleModuleService.service.VersionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class VersionController {

	@Autowired
	private VersionService versionService;

	@PostMapping("/version")
	public Map<String, Object> createVersion(@RequestParam(required = true) String version) {
		return versionService.createVersion(version);
	}

	
	
}
