package com.advantal.adminRoleModuleService.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advantal.adminRoleModuleService.models.Download;
import com.advantal.adminRoleModuleService.repository.DownloadRepository;
import com.advantal.adminRoleModuleService.responsepayload.DownloadResponse;
import com.advantal.adminRoleModuleService.service.DownloadService;
import com.advantal.adminRoleModuleService.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service
public class DownloadServiceImpl implements DownloadService {

	@Autowired
	private DownloadRepository downloadRepository;

	@Override
	public Map<String, Object> incrementDownloadCounts(String deviceType) {
		Map<String, Object> map = new HashMap<>();
		try {
			Integer count = 0;
			int iosCount = 0;
			if (deviceType != null && !deviceType.isBlank()) {
				Download download = downloadRepository.findByDeviceType(deviceType);
				if (download != null) {
					if (download.getDeviceType().equalsIgnoreCase(Constant.ANDROID)) {
						count = download.getDownload();
						count++;
						download.setDownload(count);
					} else if (deviceType.equalsIgnoreCase(Constant.IOS)) {
						count = download.getDownload();
						count++;
						download.setDownload(count);
						
					}
					downloadRepository.save(download);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.APP_DOWNLOAD_SUCCESSFULLY);
					log.info("App Download  successfully! status - {}", Constant.OK);
				} else {
					Download download1 = new Download();
					if (deviceType.equalsIgnoreCase(Constant.ANDROID)) {
						count = 1;
						download1.setDownload(count);
						download1.setDeviceType(deviceType);
					} else if (deviceType.equalsIgnoreCase(Constant.IOS)) {
						count = 1;
						download1.setDownload(count);
						download1.setDeviceType(deviceType);
					}
					downloadRepository.save(download1);
					map.put("download", download1);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.APP_DOWNLOAD_SUCCESSFULLY);
					log.info("App Download  successfully! status - {}", Constant.OK);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
				log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			}

		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getDownloadList() {
		Map<String, Object> map = new HashMap<>();
		try {
			List<Download> downloadList = downloadRepository.findAll();
			if (downloadList != null && !downloadList.isEmpty()) {
				map.put("downloadList", downloadList);
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_FOUND);
				log.info(Constant.DATA_FOUND + " ! status - {}", Constant.OK);
			} else {
				map.put("downloadList", downloadList);
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND);
				log.info(Constant.DATA_NOT_FOUND + " ! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}
}
