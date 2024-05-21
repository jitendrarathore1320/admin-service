package com.advantal.adminRoleModuleService.service;

import java.util.List;
import java.util.Map;

import com.advantal.adminRoleModuleService.models.Download;
import com.advantal.adminRoleModuleService.responsepayload.DownloadResponse;

public interface DownloadService {
//	Map<String, Object> getDownloadCount(String deviceType);

//	public interface DownloadService {
//	Map<String, Object> incrementDownloadCounts(String deviceType);

//	Map<String, Object> getDownloadCount();

	// List<DownloadInfo> getDownloadInfoList();
	//DownloadResponse getDownloadResponse();

//	DownloadResponse getDownloadResponse(String deviceType);

//	Map<String, Object> incrementDownloadCounts(String deviceType);

	Map<String, Object>  getDownloadList();

	Map<String, Object> incrementDownloadCounts(String deviceType);

	//List<Download> getDownloaList();
}


