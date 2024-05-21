package com.advantal.adminRoleModuleService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advantal.adminRoleModuleService.models.Download;
import com.advantal.adminRoleModuleService.responsepayload.DownloadResponse;
import com.advantal.adminRoleModuleService.service.DownloadService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class DownloadController {

	@Autowired
	private DownloadService downloadService;

	@GetMapping("/download_count")
	public Map<String, Object> incrementDownloadCount(@RequestParam(required = true) String deviceType) {
		return downloadService.incrementDownloadCounts(deviceType);
	}

	@GetMapping("/downloads")
	public Map<String, Object> getDownloadList() {
		return downloadService.getDownloadList();
	}
}
