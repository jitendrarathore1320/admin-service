package com.advantal.adminRoleModuleService.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advantal.adminRoleModuleService.models.Version;
import com.advantal.adminRoleModuleService.repository.VersionRepository;
import com.advantal.adminRoleModuleService.service.VersionService;
import com.advantal.adminRoleModuleService.utils.Constant;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class VersionServiceImpl implements VersionService {

	@Autowired
	private VersionRepository versionRepository;

	@Override
	public Map<String, Object> createVersion(String buildVersion) {
		Map<String, Object> map = new HashMap<>();
		try {
			if (buildVersion != null && !buildVersion.isBlank()) {
				List<Version> list = versionRepository.findAll();
				if (!list.isEmpty()) {
					Version version = list.get(0);
					if (!version.getVersion().equals(buildVersion)) {
						version.setVersion(buildVersion);
						version.setUpdationAt(new Date());
						versionRepository.save(version);
						map.put("version", version);
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.VERSION_UPDATE_SUCESSFULLY_MESSAGE);
						log.info("Version updated successfully! status - {}", Constant.OK);
					} else {
						map.put("version", version);
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.VERSION_ALREADY_EXISTS);
						log.info("Version already exists! No changes made. status - {}", Constant.OK);
					}
				} else {
					Version version = new Version();
					version.setVersion(buildVersion);
					version.setUpdationAt(new Date());
					versionRepository.save(version);
					map.put("version", version);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.VERSION_CREATE_SUCESSFULLY_MESSAGE);
					log.info("Version created successfully! status - {}", Constant.OK);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
				log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception occurred: {}", e.getMessage(), e);
		}
		return map;
	}

}
