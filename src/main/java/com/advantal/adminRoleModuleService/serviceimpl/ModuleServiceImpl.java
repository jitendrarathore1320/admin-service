package com.advantal.adminRoleModuleService.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

import com.advantal.adminRoleModuleService.models.Module;
import com.advantal.adminRoleModuleService.repository.ModuleRepository;
import com.advantal.adminRoleModuleService.requestpayload.ModuleRequest;
import com.advantal.adminRoleModuleService.responsepayload.ModuleResponsePayload;
import com.advantal.adminRoleModuleService.service.ModuleService;
import com.advantal.adminRoleModuleService.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ModuleServiceImpl implements ModuleService {

	@Autowired
	private ModuleRepository moduleRepository;


	@Override
	public Map<String, Object> addModule(ModuleRequest moduleRequest) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Module module = new Module();
				BeanUtils.copyProperties(moduleRequest, module);
				module.setUuId(UUID.randomUUID().toString());
				module.setStatus(Constant.ONE);
				module.setModuleAction(Constant.ZERO);
				module.setEntryDate(new Date());
				module.setAddAction(Constant.ZERO);
				module.setUpdateAction(Constant.ZERO);
				module.setDeleteAction(Constant.ZERO);
				module.setViewAction(Constant.ZERO);
				module.setDownloadAction(Constant.ZERO);
				module = moduleRepository.save(module);
				ModuleResponsePayload moduleResponsePayload = new ModuleResponsePayload();
				BeanUtils.copyProperties(module, moduleResponsePayload);
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.MODULE_ADDED_SUCCESS_MESSAGE);
				map.put(Constant.DATA, moduleResponsePayload);
				log.info("Module registered successfully! status - {}", moduleResponsePayload);
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getAllModule() {
		Map<String, Object> map = new HashMap<>();
		try {
			List<Module> moduleList = new ArrayList<Module>();
			List<ModuleResponsePayload> moduleResponsePayloadList = new ArrayList<ModuleResponsePayload>();
			moduleList = moduleRepository.findAllByStatus(Constant.ONE);
			if (!moduleList.isEmpty()) {
				for (Module module : moduleList) {
					ModuleResponsePayload moduleResponsePayload = new ModuleResponsePayload();
					BeanUtils.copyProperties(module, moduleResponsePayload);
					moduleResponsePayloadList.add(moduleResponsePayload);
				}
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.MODULE_LIST_FOUND_MESSAGE);
				map.put(Constant.DATA, moduleResponsePayloadList);
				log.info("Modules list found! status - {}", moduleResponsePayloadList);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.MODULE_LIST_EMPTY_MESSAGE);
				map.put(Constant.DATA, moduleResponsePayloadList);
				log.info("Modules list empty! status - {}", moduleResponsePayloadList);
			}
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

}
