package com.advantal.adminRoleModuleService.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.advantal.adminRoleModuleService.models.Module;
import com.advantal.adminRoleModuleService.models.Role;
import com.advantal.adminRoleModuleService.models.RoleModuleMapping;
import com.advantal.adminRoleModuleService.repository.AdminRepository;
import com.advantal.adminRoleModuleService.repository.ModuleRepository;
import com.advantal.adminRoleModuleService.repository.RoleModuleMappingRepository;
import com.advantal.adminRoleModuleService.repository.RoleRepository;
import com.advantal.adminRoleModuleService.requestpayload.ModuleRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.RoleBlockRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.RoleRequestPayload;
import com.advantal.adminRoleModuleService.responsepayload.RoleModuleMappingResponse;
import com.advantal.adminRoleModuleService.responsepayload.RoleResponsePage;
import com.advantal.adminRoleModuleService.responsepayload.RoleResponsePayload;
import com.advantal.adminRoleModuleService.service.RoleService;
import com.advantal.adminRoleModuleService.utils.Constant;
import com.advantal.adminRoleModuleService.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoleServiceimpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private CheckExist checkExist;

	@Autowired
	private RoleModuleMappingRepository roleModuleMappingRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public Map<String, Object> addRole(RoleRequestPayload roleRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		try {
			Role role = new Role();
			Module module = new Module();
//			RoleModuleMapping roleModuleMapping = new RoleModuleMapping();
			if (!roleRequestPayload.getModuleRequestList().isEmpty()) {
				Role oldRole = new Role();
				List<ModuleRequestPayload> moduleRequestList = roleRequestPayload.getModuleRequestList();
				List<RoleModuleMapping> roleModuleMappingList = new ArrayList<>();
				List<RoleModuleMapping> newModuleMappingList = new ArrayList<>();

				/* -------------------- Here perform update role ----------------------- */
				if (roleRequestPayload.getId() != 0) { // here roleId must be valid

					oldRole = roleRepository.findByIdAndStatus(roleRequestPayload.getId(), Constant.ONE);
					if (oldRole != null) {

						/* --- Here updating old role --- */
						log.info("Role found! status - {}", oldRole);
						if (checkExist.isExistRoleNameAndId(roleRequestPayload.getRoleName(),
								roleRequestPayload.getId())) {
							oldRole.setRoleName(roleRequestPayload.getRoleName());
						} else if (!checkExist.isExistRoleName(roleRequestPayload.getRoleName())) {
							oldRole.setRoleName(roleRequestPayload.getRoleName());
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
							map.put(Constant.MESSAGE, Constant.ROLENAME_EXISTS_MESSAGE);
							log.info("Please provide another roleName, as this roleName already exist!! status - {}",
									Constant.CONFLICT);
							return map;
						}
						oldRole.setRoleDescription(roleRequestPayload.getRoleDescription());
						oldRole.setStatus(Constant.ONE);
						oldRole.setUpdationDate(new Date());
						oldRole = roleRepository.save(oldRole);

						/* --- Here updating RoleModuleMapping --- */
						for (ModuleRequestPayload moduleReq : moduleRequestList) {
							if (moduleReq.getId() != null) {
								if (moduleReq.getId() != 0) {
									RoleModuleMapping moduleMapping = roleModuleMappingRepository
											.findByIdAndStatus(moduleReq.getId(), Constant.ONE);
									if (moduleMapping != null) {
										log.info("Role Module Mapping details found! status - {}", moduleMapping);

										if (!moduleReq.getModuleName().isBlank()) {
											moduleMapping.setModuleName(moduleReq.getModuleName());
										} else {
											map.put(Constant.MESSAGE, "Module name can not null or empty!!");
											return map;
										}
//										roleModuleMapping.setRole_id_fk(oldRole.getId());
										moduleMapping.setRoleIdFk(oldRole.getId());
										moduleMapping.setModuleAction(moduleReq.getModuleAction());
										moduleMapping.setModuleCode(moduleReq.getModuleCode());
										moduleMapping.setParentModuleName(moduleReq.getParentModuleName());
//										roleModuleMapping.setModuleId(roleModuleMapping.getModuleId());
										moduleMapping.setAddAction(moduleReq.getAddAction());
										moduleMapping.setUpdateAction(moduleReq.getUpdateAction());
										moduleMapping.setDeleteAction(moduleReq.getDeleteAction());
										moduleMapping.setViewAction(moduleReq.getViewAction());
										moduleMapping.setDownloadAction(moduleReq.getDownloadAction());
										moduleMapping.setUpdationDate(new Date());
										moduleMapping.setStatus(Constant.ONE);

										roleModuleMappingList.add(moduleMapping);
										log.info("Role Module Mapping detail added into List! status - {}",
												roleModuleMappingList);
									} else {
										map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
										map.put(Constant.MESSAGE, Constant.MODULE_NAME_AND_ID_NOT_FOUND_MESSAGE);
										log.info(
												"Given moduleId or moduleName not found into the database! status - {}",
												Constant.NOT_FOUND);
										return map;
									}
								} else {
									RoleModuleMapping newModuleMapping = new RoleModuleMapping();
									if (!moduleReq.getModuleName().isBlank()) {
										newModuleMapping.setModuleName(moduleReq.getModuleName());
									} else {
										map.put(Constant.MESSAGE, "Module name can not null or empty!!");
										return map;
									}
//									roleModuleMapping.setRole_id_fk(oldRole.getId());
									newModuleMapping.setRoleIdFk(oldRole.getId());
									newModuleMapping.setModuleAction(moduleReq.getModuleAction());
									newModuleMapping.setModuleCode(moduleReq.getModuleCode());
									newModuleMapping.setParentModuleName(moduleReq.getParentModuleName());
//									roleModuleMapping.setModuleId(roleModuleMapping.getModuleId());
									newModuleMapping.setAddAction(moduleReq.getAddAction());
									newModuleMapping.setUpdateAction(moduleReq.getUpdateAction());
									newModuleMapping.setDeleteAction(moduleReq.getDeleteAction());
									newModuleMapping.setViewAction(moduleReq.getViewAction());
									newModuleMapping.setDownloadAction(moduleReq.getDownloadAction());
									newModuleMapping.setUpdationDate(new Date());
									newModuleMapping.setStatus(Constant.ONE);

									newModuleMapping = roleModuleMappingRepository.save(newModuleMapping);
									newModuleMappingList.add(newModuleMapping);
									log.info("Created new Module Mapping detail! status - {}", newModuleMapping);
								}
							} else {
								map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
								map.put(Constant.MESSAGE, Constant.MODULE_ID_NOT_FOUND_MESSAGE);
								log.info("Module_id can not blank or null, it should be valid! status - {}",
										Constant.BAD_REQUEST);
								return map;
							}
						}
						roleModuleMappingList = roleModuleMappingRepository.saveAll(roleModuleMappingList);
						log.info("Role Module mapping data saved! status - {}", roleModuleMappingList);

						if (moduleRequestList.size() > roleModuleMappingList.size())
//							roleModuleMappingList.add(newModuleMapping);
							roleModuleMappingList.addAll(newModuleMappingList);

						oldRole.setRoleModuleMappingList(roleModuleMappingList);

						List<RoleModuleMappingResponse> roleModuleMappingResponsesList = new ArrayList<>();
						for (RoleModuleMapping roleModule : roleModuleMappingList) {
							RoleModuleMappingResponse roleModuleMappingResponse = new RoleModuleMappingResponse();
							BeanUtils.copyProperties(roleModule, roleModuleMappingResponse);
							roleModuleMappingResponse
									.setEntryDate(DateUtil.convertDateToStringDateTime(roleModule.getEntryDate()));
							roleModuleMappingResponse.setUpdationDate(DateUtil.convertDateToStringDateTime(new Date()));
							roleModuleMappingResponsesList.add(roleModuleMappingResponse);
						}

						RoleResponsePayload roleResponse = new RoleResponsePayload();
						BeanUtils.copyProperties(oldRole, roleResponse);
						roleResponse.setRoleModuleMappingResponseList(roleModuleMappingResponsesList);
						roleResponse.setEntryDate(DateUtil.convertDateToStringDateTime(oldRole.getEntryDate()));
						roleResponse.setUpdationDate(DateUtil.convertDateToStringDateTime(new Date()));
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.ROLE_UPDATED_SUCCESS_MESSAGE);
						map.put(Constant.DATA, roleResponse);
						log.info("Role updated successfully! status - {}", roleResponse);

					} else {
						map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
						map.put(Constant.MESSAGE, Constant.ROLE_ID_NOT_FOUND_MESSAGE);
						log.info("Given role_id not found into the database! status - {}", Constant.NOT_FOUND);
					}
				}

				/* ------------------- Here creating new user role --------------------- */
				else {
					if (checkExist.isExistRoleName(roleRequestPayload.getRoleName())) {
						map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
						map.put(Constant.MESSAGE, Constant.ROLENAME_EXISTS_MESSAGE);
						log.info("Please provide another roleName, as this roleName already exist!! status - {}",
								Constant.CONFLICT);
						return map;
					} else {

						/* --- Here creating role --- */
						role.setRoleName(roleRequestPayload.getRoleName());
						role.setRoleDescription(roleRequestPayload.getRoleDescription());
						role.setStatus(Constant.ONE);
						role.setEntryDate(new Date());
						role = roleRepository.save(role);

						/* --- Here creating RoleModuleMapping --- */
						for (ModuleRequestPayload moduleReq : moduleRequestList) {
							if (moduleReq.getId() != null) {
								module = moduleRepository.findByIdAndModuleNameAndStatus(moduleReq.getId(),
										moduleReq.getModuleName(), Constant.ONE);
								if (module != null) {
									log.info("Module details found! status - {}", module);

									RoleModuleMapping roleModuleMapping = new RoleModuleMapping();

//									roleModuleMapping.setRole_id_fk(role.getId());
									roleModuleMapping.setRoleIdFk(role.getId());
									roleModuleMapping.setModuleAction(moduleReq.getModuleAction());
									roleModuleMapping.setModuleName(moduleReq.getModuleName());
									roleModuleMapping.setModuleCode(moduleReq.getModuleCode());
									roleModuleMapping.setParentModuleName(moduleReq.getParentModuleName());
//									roleModuleMapping.setModuleId(module.getId());
									roleModuleMapping.setAddAction(moduleReq.getAddAction());
									roleModuleMapping.setUpdateAction(moduleReq.getUpdateAction());
									roleModuleMapping.setDeleteAction(moduleReq.getDeleteAction());
									roleModuleMapping.setViewAction(moduleReq.getViewAction());
									roleModuleMapping.setDownloadAction(moduleReq.getDownloadAction());
									roleModuleMapping.setEntryDate(new Date());
									roleModuleMapping.setStatus(Constant.ONE);

									roleModuleMappingList.add(roleModuleMapping);
									log.info("Module added into List! status - {}", roleModuleMappingList);
								} else {
									map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
									map.put(Constant.MESSAGE, Constant.MODULE_NAME_AND_ID_NOT_FOUND_MESSAGE);
									log.info("Given moduleId or moduleName not found into the database! status - {}",
											Constant.NOT_FOUND);
									return map;
								}
							} else {
								map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
								map.put(Constant.MESSAGE, Constant.MODULE_ID_NOT_FOUND_MESSAGE);
								log.info("Module_id can not blank or null, it should be valid! status - {}",
										Constant.BAD_REQUEST);
								return map;
							}
						}
						roleModuleMappingList = roleModuleMappingRepository.saveAll(roleModuleMappingList);
						log.info("Role Module mapping data saved! status - {}", roleModuleMappingList);

						role.setRoleModuleMappingList(roleModuleMappingList);

						List<RoleModuleMappingResponse> roleModuleMappingResponsesList = new ArrayList<>();

						for (RoleModuleMapping roleModule : roleModuleMappingList) {
							RoleModuleMappingResponse roleModuleMappingResponse = new RoleModuleMappingResponse();
							BeanUtils.copyProperties(roleModule, roleModuleMappingResponse);
							roleModuleMappingResponse
									.setEntryDate(DateUtil.convertDateToStringDateTime(roleModule.getEntryDate()));
							roleModuleMappingResponsesList.add(roleModuleMappingResponse);
						}

						RoleResponsePayload roleResponse = new RoleResponsePayload();
						BeanUtils.copyProperties(role, roleResponse);
						roleResponse.setRoleModuleMappingResponseList(roleModuleMappingResponsesList);
						roleResponse.setEntryDate(DateUtil.convertDateToStringDateTime(role.getEntryDate()));
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.ROLE_ADDED_SUCCESS_MESSAGE);
						map.put(Constant.DATA, roleResponse);
						log.info("Role registration successfully! status - {}", roleResponse);
					}
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.ROLE_ADDED_FAILED_MESSAGE);
				log.info("Role registration failed, because no module selected! status - {}", Constant.BAD_REQUEST);
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

//	@Override
//	public Map<String, Object> addRole(RoleRequestPayload roleRequestPayload) {
//		Map<String, Object> map = new HashMap<>();
//		try {
//			Role role = new Role();
//			Module module = new Module();
//
//			if (!roleRequestPayload.getModuleRequestList().isEmpty()) {
//				Role oldRole = new Role();
//				List<ModuleRequestPayload> moduleRequestList = roleRequestPayload.getModuleRequestList();
//				List<RoleModuleMapping> roleModuleMappingList = new ArrayList<>();
//
//				/* -------------------- Here perform update role ----------------------- */
//				if (roleRequestPayload.getId() != 0) { // here roleId must be valid
//
//					oldRole = roleRepository.findByIdAndStatus(roleRequestPayload.getId(), Constant.ONE);
//					if (oldRole != null) {
//
//						/* --- Here updating old role --- */
//						log.info("Role found! status - {}", oldRole);
//						if (checkExist.isExistRoleNameAndId(roleRequestPayload.getRoleName(),
//								roleRequestPayload.getId())) {
//							oldRole.setRoleName(roleRequestPayload.getRoleName());
//						} else if (!checkExist.isExistRoleName(roleRequestPayload.getRoleName())) {
//							oldRole.setRoleName(roleRequestPayload.getRoleName());
//						} else {
//							map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
//							map.put(Constant.MESSAGE, Constant.ROLENAME_EXISTS_MESSAGE);
//							log.info("Please provide another roleName, as this roleName already exist!! status - {}",
//									Constant.CONFLICT);
//							return map;
//						}
//						oldRole.setRoleDescription(roleRequestPayload.getRoleDescription());
//						oldRole.setStatus(Constant.ONE);
//						oldRole.setUpdationDate(new Date());
//						oldRole = roleRepository.save(oldRole);
//
//						/* --- Here updating RoleModuleMapping --- */
//						for (ModuleRequestPayload moduleReq : moduleRequestList) {
//							if (moduleReq.getId() != null) {
//								RoleModuleMapping roleModuleMapping = roleModuleMappingRepository
//										.findByIdAndStatus(moduleReq.getId(), Constant.ONE);
//								if (roleModuleMapping != null) {
//									log.info("Role Module Mapping details found! status - {}", roleModuleMapping);
//
//									if (!moduleReq.getModuleName().isBlank()) {
//										roleModuleMapping.setModuleName(moduleReq.getModuleName());
//									} else {
//										map.put(Constant.MESSAGE, "Module name can not null or empty!!");
//										return map;
//									}
//									roleModuleMapping.setRole_id_fk(oldRole.getId());
//									roleModuleMapping.setModuleAction(moduleReq.getModuleAction());
//									roleModuleMapping.setModuleId(roleModuleMapping.getModuleId());
//									roleModuleMapping.setAddAction(moduleReq.getAddAction());
//									roleModuleMapping.setUpdateAction(moduleReq.getUpdateAction());
//									roleModuleMapping.setDeleteAction(moduleReq.getDeleteAction());
//									roleModuleMapping.setViewAction(moduleReq.getViewAction());
//									roleModuleMapping.setDownloadAction(moduleReq.getDownloadAction());
//									roleModuleMapping.setEntryDate(new Date());
//									roleModuleMapping.setStatus(Constant.ONE);
//
//									roleModuleMappingList.add(roleModuleMapping);
//									log.info("Role Module Mapping detail added into List! status - {}",
//											roleModuleMappingList);
//								} else {
//									map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
//									map.put(Constant.MESSAGE, Constant.MODULE_NAME_AND_ID_NOT_FOUND_MESSAGE);
//									log.info("Given moduleId or moduleName not found into the database! status - {}",
//											Constant.NOT_FOUND);
//									return map;
//								}
//							} else {
//								map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
//								map.put(Constant.MESSAGE, Constant.MODULE_ID_NOT_FOUND_MESSAGE);
//								log.info("Module_id can not blank or null, it should be valid! status - {}",
//										Constant.BAD_REQUEST);
//								return map;
//							}
//						}
//						roleModuleMappingList = roleModuleMappingRepository.saveAll(roleModuleMappingList);
//						log.info("Role Module mapping data saved! status - {}", roleModuleMappingList);
//
//						oldRole.setRoleModuleMappingList(roleModuleMappingList);
//
//						List<RoleModuleMappingResponse> roleModuleMappingResponsesList = new ArrayList<>();
//
//						for (RoleModuleMapping roleModule : roleModuleMappingList) {
//							RoleModuleMappingResponse roleModuleMappingResponse = new RoleModuleMappingResponse();
//							BeanUtils.copyProperties(roleModule, roleModuleMappingResponse);
//							roleModuleMappingResponse
//									.setEntryDate(DateUtil.convertDateToStringDateTime(roleModule.getEntryDate()));
//							roleModuleMappingResponse.setUpdationDate(DateUtil.convertDateToStringDateTime(new Date()));
//							roleModuleMappingResponsesList.add(roleModuleMappingResponse);
//						}
//
//						RoleResponsePayload roleResponse = new RoleResponsePayload();
//						BeanUtils.copyProperties(oldRole, roleResponse);
//						roleResponse.setRoleModuleMappingResponseList(roleModuleMappingResponsesList);
//						roleResponse.setEntryDate(DateUtil.convertDateToStringDateTime(oldRole.getEntryDate()));
//						roleResponse.setUpdationDate(DateUtil.convertDateToStringDateTime(new Date()));
//						map.put(Constant.RESPONSE_CODE, Constant.OK);
//						map.put(Constant.MESSAGE, Constant.ROLE_UPDATED_SUCCESS_MESSAGE);
//						map.put(Constant.DATA, roleResponse);
//						log.info("Role updated successfully! status - {}", roleResponse);
//
//					} else {
//						map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
//						map.put(Constant.MESSAGE, Constant.ROLE_ID_NOT_FOUND_MESSAGE);
//						log.info("Given role_id not found into the database! status - {}", Constant.NOT_FOUND);
//					}
//				}
//
//				/* ------------------- Here creating new user role --------------------- */
//				else {
//					if (checkExist.isExistRoleName(roleRequestPayload.getRoleName())) {
//						map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
//						map.put(Constant.MESSAGE, Constant.ROLENAME_EXISTS_MESSAGE);
//						log.info("Please provide another roleName, as this roleName already exist!! status - {}",
//								Constant.CONFLICT);
//						return map;
//					} else {
//
//						/* --- Here creating role --- */
//						role.setRoleName(roleRequestPayload.getRoleName());
//						role.setRoleDescription(roleRequestPayload.getRoleDescription());
//						role.setStatus(Constant.ONE);
//						role.setEntryDate(new Date());
//						role = roleRepository.save(role);
//
//						/* --- Here creating RoleModuleMapping --- */
//						for (ModuleRequestPayload moduleReq : moduleRequestList) {
//							if (moduleReq.getId() != null) {
//								module = moduleRepository.findByIdAndModuleNameAndStatus(moduleReq.getId(),
//										moduleReq.getModuleName(), Constant.ONE);
//								if (module != null) {
//									log.info("Module details found! status - {}", module);
//
//									RoleModuleMapping roleModuleMapping = new RoleModuleMapping();
//
//									roleModuleMapping.setRole_id_fk(role.getId());
//									roleModuleMapping.setModuleAction(moduleReq.getModuleAction());
//									roleModuleMapping.setModuleName(moduleReq.getModuleName());
//									roleModuleMapping.setModuleCode(moduleReq.getModuleCode());
//									roleModuleMapping.setParentModuleName(moduleReq.getParentModuleName());
//									roleModuleMapping.setModuleId(module.getId());
//									roleModuleMapping.setAddAction(moduleReq.getAddAction());
//									roleModuleMapping.setUpdateAction(moduleReq.getUpdateAction());
//									roleModuleMapping.setDeleteAction(moduleReq.getDeleteAction());
//									roleModuleMapping.setViewAction(moduleReq.getViewAction());
//									roleModuleMapping.setDownloadAction(moduleReq.getDownloadAction());
//									roleModuleMapping.setEntryDate(new Date());
//									roleModuleMapping.setStatus(Constant.ONE);
//
//									roleModuleMappingList.add(roleModuleMapping);
//									log.info("Module added into List! status - {}", roleModuleMappingList);
//								} else {
//									map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
//									map.put(Constant.MESSAGE, Constant.MODULE_NAME_AND_ID_NOT_FOUND_MESSAGE);
//									log.info("Given moduleId or moduleName not found into the database! status - {}",
//											Constant.NOT_FOUND);
//									return map;
//								}
//							} else {
//								map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
//								map.put(Constant.MESSAGE, Constant.MODULE_ID_NOT_FOUND_MESSAGE);
//								log.info("Module_id can not blank or null, it should be valid! status - {}",
//										Constant.BAD_REQUEST);
//								return map;
//							}
//						}
//						roleModuleMappingList = roleModuleMappingRepository.saveAll(roleModuleMappingList);
//						log.info("Role Module mapping data saved! status - {}", roleModuleMappingList);
//
//						role.setRoleModuleMappingList(roleModuleMappingList);
//
//						List<RoleModuleMappingResponse> roleModuleMappingResponsesList = new ArrayList<>();
//
//						for (RoleModuleMapping roleModule : roleModuleMappingList) {
//							RoleModuleMappingResponse roleModuleMappingResponse = new RoleModuleMappingResponse();
//							BeanUtils.copyProperties(roleModule, roleModuleMappingResponse);
//							roleModuleMappingResponse
//									.setEntryDate(DateUtil.convertDateToStringDateTime(roleModule.getEntryDate()));
//							roleModuleMappingResponsesList.add(roleModuleMappingResponse);
//						}
//
//						RoleResponsePayload roleResponse = new RoleResponsePayload();
//						BeanUtils.copyProperties(role, roleResponse);
//						roleResponse.setRoleModuleMappingResponseList(roleModuleMappingResponsesList);
//						roleResponse.setEntryDate(DateUtil.convertDateToStringDateTime(role.getEntryDate()));
//						map.put(Constant.RESPONSE_CODE, Constant.OK);
//						map.put(Constant.MESSAGE, Constant.ROLE_ADDED_SUCCESS_MESSAGE);
//						map.put(Constant.DATA, roleResponse);
//						log.info("Role registration successfully! status - {}", roleResponse);
//					}
//				}
//			} else {
//				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
//				map.put(Constant.MESSAGE, Constant.ROLE_ADDED_FAILED_MESSAGE);
//				log.info("Role registration failed, because no module selected! status - {}", Constant.BAD_REQUEST);
//			}
//
//		} catch (DataAccessResourceFailureException e) {
//			e.printStackTrace();
//			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
//			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
//			log.error("Exception : " + e.getMessage());
//		} catch (Exception e) {
//			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
//			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
//			log.error("Exception : " + e.getMessage());
//		}
//		return map;
//	}

//	@Override
//	public Map<String, Object> getAllRole(Integer pageIndex, Integer pageSize, String searchText) {
//		Map<String, Object> map = new HashMap<>();
//		try {
//			List<RoleResponsePayload> roleResponseList = new ArrayList<>();
//			RoleResponsePage roleResponsePage = new RoleResponsePage();
//			List<Role> roleList = new ArrayList<>();
//			Page<Role> page = null;
//
//			if (pageSize >= 1) {
//				Pageable pageable = PageRequest.of(pageIndex, pageSize);
//				if (searchText != "" && searchText != null) {
//					if (!searchText.equals("0") && !searchText.equals("1") && !searchText.equals("2")) {
//						page = roleRepository.findByRoleNameContaining(searchText, pageable);
//					} else if (searchText.equals("0") || searchText.equals("1") || searchText.equals("2")) {
//						Short status = Short.parseShort(searchText);
//						page = roleRepository.findByStatus(pageable, status);
//					}
//				} else {
//					page = roleRepository.findAllByStatus(pageable, Constant.ZERO);
//				}
//				if (page != null && page.getContent().size() > Constant.ZERO) {
//					roleList = page.getContent();
//					for (Role role : roleList) {
//						RoleResponsePayload roleResponse = new RoleResponsePayload();
//						List<RoleModuleMappingResponse> moduleResponseList = new ArrayList<>();
//						List<RoleModuleMapping> roleModuleMappingList = role.getRoleModuleMappingList();
//						for (RoleModuleMapping roleModuleMapping : roleModuleMappingList) {
//							RoleModuleMappingResponse roleModuleMappingResponse = new RoleModuleMappingResponse();
//							BeanUtils.copyProperties(roleModuleMapping, roleModuleMappingResponse);
//							moduleResponseList.add(roleModuleMappingResponse);
//						}
//
//						BeanUtils.copyProperties(role, roleResponse);
//						roleResponse.setRoleModuleMappingResponseList(moduleResponseList);
//						roleResponse.setEntryDate(DateUtil.convertDateToStringDateTime(role.getEntryDate()));
//						roleResponse.setUpdationDate(DateUtil.convertDateToStringDateTime(role.getUpdationDate()));
//						roleResponseList.add(roleResponse);
//					}
//					roleResponsePage.setRoleList(roleResponseList);
//					roleResponsePage.setPageIndex(page.getNumber());
//					roleResponsePage.setPageSize(page.getSize());
//					roleResponsePage.setTotalElement(page.getTotalElements());
//					roleResponsePage.setTotalPages(page.getTotalPages());
//					roleResponsePage.setIsFirstPage(page.isFirst());
//					roleResponsePage.setIsLastPage(page.isLast());
//
//					map.put(Constant.RESPONSE_CODE, Constant.OK);
//					map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
//					map.put(Constant.DATA, roleResponsePage);
//					log.info("Record found! status - {}", roleResponsePage);
//
//				} else {
//					map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
//					map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
//					map.put(Constant.DATA, roleResponseList);
//					log.info("Record not found! status - {}", roleResponseList);
//				}
//			} else {
//				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
//				map.put(Constant.MESSAGE, Constant.PAGE_SIZE_MESSAGE);
//				log.info("Page size can't be less then one! status - {}", pageSize);
//			}
//
//		} catch (DataAccessResourceFailureException e) {
//			e.printStackTrace();
//			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
//			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
//			log.error("Exception : " + e.getMessage());
//		} catch (Exception e) {
//			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
//			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
//			log.error("Exception : " + e.getMessage());
//		}
//		return map;
//	}

	@Override
	public Map<String, Object> getAllRole(Integer pageIndex, Integer pageSize, String searchText, String status) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<RoleResponsePayload> roleResponseList = new ArrayList<>();
			RoleResponsePage roleResponsePage = new RoleResponsePage();
			List<Role> roleList = new ArrayList<>();
			Page<Role> page = null;
			Short st = null;
			Pageable pageable = null;

			/* ---- To get list without pagination ---- */
			if (pageIndex == null && pageSize == null && status == null && searchText == null) {
				roleList = roleRepository.findAllByStatus(Constant.ONE);
				if (roleList.size() > Constant.ZERO) {
					for (Role role : roleList) {
						RoleResponsePayload roleResponse = new RoleResponsePayload();
						List<RoleModuleMappingResponse> moduleResponseList = new ArrayList<>();
						List<RoleModuleMapping> roleModuleMappingList = role.getRoleModuleMappingList();
						for (RoleModuleMapping roleModuleMapping : roleModuleMappingList) {
							RoleModuleMappingResponse roleModuleMappingResponse = new RoleModuleMappingResponse();
							BeanUtils.copyProperties(roleModuleMapping, roleModuleMappingResponse);
							moduleResponseList.add(roleModuleMappingResponse);
						}
						BeanUtils.copyProperties(role, roleResponse);
						roleResponse.setRoleModuleMappingResponseList(moduleResponseList);
						roleResponse.setEntryDate(DateUtil.convertDateToStringDateTime(role.getEntryDate()));
						roleResponse.setUpdationDate(DateUtil.convertDateToStringDateTime(role.getUpdationDate()));
						roleResponseList.add(roleResponse);
					}
					roleResponsePage.setRoleList(roleResponseList);

					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
					map.put(Constant.DATA, roleResponsePage);
					log.info("Record found! status - {}", roleResponsePage);
					return map;
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
					map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, roleResponseList);
					log.info("Record not found! status - {}", roleResponseList);
					return map;
				}
			} else if (pageIndex != null && pageSize != null) {
				if (pageSize >= 1) {
					/* ---- To get list with pagination (Filter by status only)---- */
					if (pageIndex != null && pageSize != null && status != null && searchText == null) {
						if (status.equals("0") || status.equals("1") || status.equals("2")) {
							st = Short.parseShort(status);
							pageable = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());
							page = roleRepository.findAllByStatus(pageable, st);
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
							map.put(Constant.MESSAGE, Constant.STATUS_INVALID_MESSAGE);
							log.info("Status value invalid! status - {}", status);
							return map;
						}
					}

					/* To get list with pagination (Get all record by default Active and Blocked) */
					else if (pageIndex != null && pageSize != null && status == null && searchText == null) {
						pageable = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());
						page = roleRepository.findAllRole(pageable);
					}

					/*
					 * To get list with pagination (Filter by RoleName, by default Active and
					 * Blocked)
					 */
					else if (pageIndex != null && pageSize != null && status == null && searchText != null) {
						pageable = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());
						page = roleRepository.findBySearchTextWithPagination(searchText, pageable);
					}

					if (page != null && page.getContent().size() > Constant.ZERO) {
						log.info("Record found! status - {}", roleList);
						roleList = page.getContent();
						for (Role role : roleList) {
							RoleResponsePayload roleResponse = new RoleResponsePayload();
							List<RoleModuleMappingResponse> moduleResponseList = new ArrayList<>();
							List<RoleModuleMapping> roleModuleMappingList = role.getRoleModuleMappingList();
							for (RoleModuleMapping roleModuleMapping : roleModuleMappingList) {
								RoleModuleMappingResponse roleModuleMappingResponse = new RoleModuleMappingResponse();
								BeanUtils.copyProperties(roleModuleMapping, roleModuleMappingResponse);
								moduleResponseList.add(roleModuleMappingResponse);
							}
							BeanUtils.copyProperties(role, roleResponse);
							roleResponse.setRoleModuleMappingResponseList(moduleResponseList);
							roleResponse.setEntryDate(DateUtil.convertDateToStringDateTime(role.getEntryDate()));
							roleResponse.setUpdationDate(DateUtil.convertDateToStringDateTime(role.getUpdationDate()));
							roleResponseList.add(roleResponse);
						}
//						roleResponsePage.setRoleList(roleResponseList);
//						roleResponsePage.setPageIndex(page.getNumber());
//						roleResponsePage.setPageSize(page.getSize());
//						roleResponsePage.setTotalElement(page.getTotalElements());
//						roleResponsePage.setTotalPages(page.getTotalPages());
//						roleResponsePage.setIsFirstPage(page.isFirst());
//						roleResponsePage.setIsLastPage(page.isLast());
//
//						map.put(Constant.RESPONSE_CODE, Constant.OK);
//						map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
//						map.put(Constant.DATA, roleResponsePage);
//						log.info("Record found! status - {}", roleResponsePage);
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
						map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, roleResponseList);
						log.info("Record not found! status - {}", roleResponseList);
					}
				} else {
					if (!searchText.isBlank()) {
						roleList = roleRepository.findBySearchText(searchText);
					} else {
						roleList = roleRepository.findAllRole();
					}
					if (roleList.size() > Constant.ZERO) {
						for (Role role : roleList) {
							RoleResponsePayload roleResponse = new RoleResponsePayload();
							List<RoleModuleMappingResponse> moduleResponseList = new ArrayList<>();
							List<RoleModuleMapping> roleModuleMappingList = role.getRoleModuleMappingList();
							for (RoleModuleMapping roleModuleMapping : roleModuleMappingList) {
								RoleModuleMappingResponse roleModuleMappingResponse = new RoleModuleMappingResponse();
								BeanUtils.copyProperties(roleModuleMapping, roleModuleMappingResponse);
								moduleResponseList.add(roleModuleMappingResponse);
							}
							BeanUtils.copyProperties(role, roleResponse);
							roleResponse.setRoleModuleMappingResponseList(moduleResponseList);
							roleResponse.setEntryDate(DateUtil.convertDateToStringDateTime(role.getEntryDate()));
							roleResponse.setUpdationDate(DateUtil.convertDateToStringDateTime(role.getUpdationDate()));
							roleResponseList.add(roleResponse);
						}
//						roleResponsePage.setRoleList(roleResponseList);
//
//						map.put(Constant.RESPONSE_CODE, Constant.OK);
//						map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
//						map.put(Constant.DATA, roleResponsePage);
//						log.info("Record found! status - {}", roleResponsePage);
//						return map;
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
						map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, roleResponseList);
						log.info("Record not found! status - {}", roleResponseList);
						return map;
					}
//					map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
//					map.put(Constant.MESSAGE, Constant.PAGE_SIZE_MESSAGE);
//					log.info("Page size can't be less then one! status - {}", pageSize);
//					return map;
				}
				roleResponsePage.setRoleList(roleResponseList);
				roleResponsePage.setPageIndex(page!=null ? page.getNumber() : null);
				roleResponsePage.setPageSize(page!=null ? page.getSize() : null);
				roleResponsePage.setTotalElement(page!=null ? page.getTotalElements() : roleResponseList.size());
				roleResponsePage.setTotalPages(page!=null ? page.getTotalPages() : null);
				roleResponsePage.setIsFirstPage(page!=null ? page.isFirst() : null);
				roleResponsePage.setIsLastPage(page!=null ? page.isLast() : null);

				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
				map.put(Constant.DATA, roleResponsePage);
				log.info("Record found! status - {}", roleResponsePage);
//			} else if (pageIndex != null && pageSize != null && searchText != null) {
//				if (!searchText.isBlank()) {
//					roleList = roleRepository.findBySearchText(searchText);
//				} else {
//					roleList = roleRepository.findAllRole();
//				}
//				if (roleList.size() > Constant.ZERO) {
//					for (Role role : roleList) {
//						RoleResponsePayload roleResponse = new RoleResponsePayload();
//						List<RoleModuleMappingResponse> moduleResponseList = new ArrayList<>();
//						List<RoleModuleMapping> roleModuleMappingList = role.getRoleModuleMappingList();
//						for (RoleModuleMapping roleModuleMapping : roleModuleMappingList) {
//							RoleModuleMappingResponse roleModuleMappingResponse = new RoleModuleMappingResponse();
//							BeanUtils.copyProperties(roleModuleMapping, roleModuleMappingResponse);
//							moduleResponseList.add(roleModuleMappingResponse);
//						}
//						BeanUtils.copyProperties(role, roleResponse);
//						roleResponse.setRoleModuleMappingResponseList(moduleResponseList);
//						roleResponse.setEntryDate(DateUtil.convertDateToStringDateTime(role.getEntryDate()));
//						roleResponse.setUpdationDate(DateUtil.convertDateToStringDateTime(role.getUpdationDate()));
//						roleResponseList.add(roleResponse);
//					}
//					roleResponsePage.setRoleList(roleResponseList);
//
//					map.put(Constant.RESPONSE_CODE, Constant.OK);
//					map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
//					map.put(Constant.DATA, roleResponsePage);
//					log.info("Record found! status - {}", roleResponsePage);
//					return map;
//				} else {
//					map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
//					map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
//					map.put(Constant.DATA, roleResponseList);
//					log.info("Record not found! status - {}", roleResponseList);
//					return map;
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
				log.info("Page size and Page index can't be null! status - {}", pageIndex, pageSize);
				return map;
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

	@Override
	public Map<String, Object> blockRole(RoleBlockRequestPayload roleBlockRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		try {
			Optional<Role> role = Optional.empty();
			Role oldRole = new Role();
			role = roleRepository.findById(roleBlockRequestPayload.getId());
			if (role.isPresent()) {
				oldRole = role.get();
				log.info("Record found! status - {}", Constant.OK);

				/* ------------------- Here unblock the role ----------------- */
				if (roleBlockRequestPayload.getStatus().equals(Constant.ONE)) {
					if (oldRole.getStatus().equals(Constant.ONE)) {
						map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
						map.put(Constant.MESSAGE, Constant.ALREADY_UNBLOCKED_MESSAGE);
						log.info("Already unblocked! status - {}", Constant.CONFLICT);
						return map;
					} else {
						oldRole.setStatus(roleBlockRequestPayload.getStatus());
						oldRole.setUpdationDate(new Date());
						oldRole = roleRepository.save(oldRole);
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.UNBLOCKED_SUCCESS_MESSAGE);
						log.info("unblocked successfully! status - {}", Constant.OK);
					}
				}
				/* ------------------- Here block the role ----------------- */
				else if (roleBlockRequestPayload.getStatus().equals(Constant.TWO)) {
					if (oldRole.getStatus().equals(Constant.ZERO)) {
						map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
						map.put(Constant.MESSAGE, Constant.ALREADY_DELETED_MESSAGE);
						log.info("Already deleted!! status - {}", Constant.CONFLICT);
						return map;
					} else if (oldRole.getStatus().equals(Constant.TWO)) {
						map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
						map.put(Constant.MESSAGE, Constant.ALREADY_BLOCKED_MESSAGE);
						log.info("Already blocked! status - {}", Constant.CONFLICT);
						return map;
					} else {
						/* --- To check given role is assigned to the admin or not --- */
						Integer count = adminRepository.findByRoleId(roleBlockRequestPayload.getId()).size();
						if (count > 0) {
							map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
							map.put(Constant.MESSAGE, Constant.ROLE_CAN_NOT_BLOCK_MESSAGE);
							log.info("This role assigned someone, you can't block! status - {}", Constant.BAD_REQUEST);
							return map;
						} else {
							oldRole.setStatus(roleBlockRequestPayload.getStatus());
							oldRole.setUpdationDate(new Date());
							oldRole = roleRepository.save(oldRole);
							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.BLOCKED_SUCCESS_MESSAGE);
							log.info("Blocked successfully! status - {}", Constant.OK);
						}
					}

				} else {
					map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					map.put(Constant.MESSAGE, Constant.STATUS_INVALID_MESSAGE);
					log.info("Status value invalid! status - {}", Constant.BAD_REQUEST);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
				map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
				log.info("Record not found! status - {}", Constant.NOT_FOUND);
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

	@Override
	public List<Role> getAllRoleDetails(String searchText) {
		List<Role> rolelist = new ArrayList<>();
		if (searchText != "" && searchText != null) {
			rolelist = roleRepository.findBySearchText(searchText);
		} else {
			rolelist = roleRepository.findAllRole();
		}
		return rolelist;
	}

}
