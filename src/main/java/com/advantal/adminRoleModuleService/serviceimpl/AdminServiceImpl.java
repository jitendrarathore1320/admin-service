package com.advantal.adminRoleModuleService.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.advantal.adminRoleModuleService.models.Admin;
import com.advantal.adminRoleModuleService.models.AdminHistory;
import com.advantal.adminRoleModuleService.models.Otp;
import com.advantal.adminRoleModuleService.models.PasswordPolicy;
import com.advantal.adminRoleModuleService.models.Role;
import com.advantal.adminRoleModuleService.models.RoleModuleMapping;
import com.advantal.adminRoleModuleService.repository.AdminLogsRepository;
import com.advantal.adminRoleModuleService.repository.AdminRepository;
import com.advantal.adminRoleModuleService.repository.OtpRepository;
import com.advantal.adminRoleModuleService.repository.PasswordPolicyRepository;
import com.advantal.adminRoleModuleService.repository.RoleRepository;
import com.advantal.adminRoleModuleService.requestpayload.AdminBlockRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.AdminHistoryPaginationPayload;
import com.advantal.adminRoleModuleService.requestpayload.AdminLoginRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.AdminRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.CountResponse;
import com.advantal.adminRoleModuleService.requestpayload.LoginRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.PasswordPolicyRequestPayload;
import com.advantal.adminRoleModuleService.responsepayload.AdminGraphResponse;
import com.advantal.adminRoleModuleService.responsepayload.AdminHistoryResponse;
import com.advantal.adminRoleModuleService.responsepayload.AdminResponsePage;
import com.advantal.adminRoleModuleService.responsepayload.AdminResponsePayload;
import com.advantal.adminRoleModuleService.responsepayload.MonthObject;
import com.advantal.adminRoleModuleService.responsepayload.RoleModuleMappingResponse;
import com.advantal.adminRoleModuleService.responsepayload.RoleResponse;
import com.advantal.adminRoleModuleService.responsepayload.RoleResponsePayload;
import com.advantal.adminRoleModuleService.service.AdminService;
import com.advantal.adminRoleModuleService.utils.Constant;
import com.advantal.adminRoleModuleService.utils.DateUtil;
import com.advantal.adminRoleModuleService.utils.FileUploader;
import com.advantal.adminRoleModuleService.utils.HtmlTemplate;
import com.advantal.adminRoleModuleService.utils.NullCheckUtil;
import com.advantal.adminRoleModuleService.utils.SendMail;
import com.advantal.adminRoleModuleService.utils.UtilityMethods;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private CheckExist checkExist;

	@Autowired
	private PasswordPolicyRepository passwordPolicyRepository;

	@Autowired
	private AdminLogsRepository adminLogsRepository;

	@Autowired
	private NullCheckUtil nullCheckUtil;

	@Autowired
	private UtilityMethods utilityMethods;

	@Value("${spring.filedir}")
	private String uploadDir;

	@Value("${spring.serverfilepath}")
	private String serverFilePath;

	@Value("${spring.baseurl}")
	private String baseUrl;

	@Value("${spring.imagedir}")
	private String imageDir;

	private Logger logger = LogManager.getLogger(AdminServiceImpl.class);

	@Override
	public Map<String, Object> addAdmin(String adminRequestPayload, MultipartFile file) {
		Map<String, Object> map = new HashMap<>();
		Admin admin = new Admin();
		Role role = null;
		String imagePath = null;
		try {
			AdminHistory adminHistory = new AdminHistory();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			AdminRequestPayload adminRequest = objectMapper.readValue(adminRequestPayload, AdminRequestPayload.class);
			Map<String, Object> map2 = nullCheckUtil.checkValidation(adminRequest);
			AdminRequestPayload adminRequestPayload2 = objectMapper.convertValue(map2.get("data"),
					AdminRequestPayload.class);
			if (map2.get("responseCode").equals("200")) {
				if (adminRequestPayload2 != null && adminRequestPayload2.getId() != null) {
					if (adminRequestPayload2.getId() != 0) {
						/* ----------- Here update admin details ----------- */
						admin = adminRepository.findByIdAndStatus(adminRequestPayload2.getId(), Constant.ONE);
						if (admin != null) {
							BeanUtils.copyProperties(admin, adminHistory);
							;
							adminHistory.setLastUpdationDate(new Date());
							adminLogsRepository.save(adminHistory);
							log.info("Admin history data updated successfully !! - status { } " + Constant.OK);

							log.info("Admin found! status - {}", admin);
							if (adminRequestPayload2.getRoleId() != null) {
								role = roleRepository.findByIdAndStatus(adminRequestPayload2.getRoleId(), Constant.ONE);
								if (role != null) {
									log.info("Role found! status - {}", admin);
									admin.setRole(role);
									admin.setRoleId(role.getId());
								} else {
									map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
									map.put(Constant.MESSAGE, Constant.ROLE_NOT_FOUND_MESSAGE);
									log.info("Given role not found into the database! status - {}", Constant.NOT_FOUND);
									return map;
								}
							} else {
								map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
								map.put(Constant.MESSAGE, Constant.ADMIN_ID_NOT_EMPTY);
								log.info("Role id can't be null or empty !!" + adminRequest.getId());
							}

							if (checkExist.isExistMobileAndId(adminRequestPayload2.getMobile(),
									adminRequestPayload2.getId())) {
								admin.setMobile(adminRequestPayload2.getMobile());
							} else if (!checkExist.isExistMobile(adminRequestPayload2.getMobile())) {
								admin.setMobile(adminRequestPayload2.getMobile());
							} else {
								admin.setMobile(adminRequestPayload2.getMobile());
							}

							if (file != null) {
								imagePath = baseUrl + FileUploader.uploadLanguageFile(file, imageDir);
								log.info("Image update successfully! status - {}", imagePath);
							}

							admin.setAddress(adminRequestPayload2.getAddress().isBlank() ? admin.getAddress()
									: adminRequestPayload2.getAddress());
							admin.setCity(adminRequestPayload2.getCity().isBlank() ? admin.getCity()
									: adminRequestPayload2.getCity());
							admin.setCountry(adminRequestPayload2.getCountry().isBlank() ? admin.getCountry()
									: adminRequestPayload2.getCountry());
							admin.setPinCode(adminRequestPayload2.getPinCode().isBlank() ? admin.getPinCode()
									: adminRequestPayload2.getPinCode());
//							admin.setImageUrl(imagePath);
							if (file != null) {
								admin.setImageUrl(imagePath);
							} else {
								admin.setImageUrl(admin.getImageUrl());
							}
							admin.setEmail(adminRequestPayload2.getEmail());
							admin.setName(adminRequestPayload2.getName());
							admin.setUpdationDate(new Date());
//							admin.setPassword(adminRequest.getPassword());
							admin.setStatus(Constant.ONE);
							admin = adminRepository.save(admin);

							AdminResponsePayload adminResponsePayload = new AdminResponsePayload();
							BeanUtils.copyProperties(admin, adminResponsePayload);

							RoleResponsePayload roleResponsePayload = new RoleResponsePayload();
							BeanUtils.copyProperties(admin.getRole(), roleResponsePayload);
							roleResponsePayload
									.setEntryDate(DateUtil.convertDateToStringDateTime(admin.getRole().getEntryDate()));
							roleResponsePayload.setUpdationDate(
									DateUtil.convertDateToStringDateTime(admin.getRole().getUpdationDate()));

							List<RoleModuleMappingResponse> roleModuleMappingResponseList = new ArrayList<>();
							for (RoleModuleMapping roleModuleMapping : admin.getRole().getRoleModuleMappingList()) {
								RoleModuleMappingResponse roleModuleMapResponse = new RoleModuleMappingResponse();
								BeanUtils.copyProperties(roleModuleMapping, roleModuleMapResponse);
								roleModuleMapResponse.setEntryDate(
										DateUtil.convertDateToStringDateTime(roleModuleMapping.getEntryDate()));
								roleModuleMapResponse.setUpdationDate(
										DateUtil.convertDateToStringDateTime(roleModuleMapping.getUpdationDate()));
								roleModuleMappingResponseList.add(roleModuleMapResponse);
							}
							roleResponsePayload.setRoleModuleMappingResponseList(roleModuleMappingResponseList);

							adminResponsePayload.setRoleResponsePayload(roleResponsePayload);
							adminResponsePayload
									.setEntryDate(DateUtil.convertDateToStringDateTime(admin.getEntryDate()));
							adminResponsePayload
									.setUpdationDate(DateUtil.convertDateToStringDateTime(admin.getUpdationDate()));

							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.ADMIN_UPDATED_SUCCESS_MESSAGE);
							map.put(Constant.DATA, adminResponsePayload);
							log.info("Admin updated successfully! status - {}", adminResponsePayload);
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
							map.put(Constant.MESSAGE, Constant.ROLE_NOT_FOUND_MESSAGE);
							log.info("Given role not found into the database! status - {}", Constant.NOT_FOUND);
						}
					} else {
						/* ----------- Here creating admin details ----------- */

						role = roleRepository.findByIdAndStatus(adminRequestPayload2.getRoleId(), Constant.ONE);
						if (role != null) {

							if (checkExist.isExistEmailAndStatus(adminRequestPayload2.getEmail())) {
								map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
								map.put(Constant.MESSAGE, Constant.EMAIL_EXISTS_MESSAGE);
								log.info("Please provide another email, as this email already exist!! status - {}",
										Constant.CONFLICT);
								return map;
							} else {
								admin.setEmail(adminRequestPayload2.getEmail());
							}
							if (checkExist.isExistMobile(adminRequestPayload2.getMobile())) {
								map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
								map.put(Constant.MESSAGE, Constant.MOBILE_EXISTS_MESSAGE);
								log.info("Please provide another mobile, as this mobile already exist!! status - {}",
										Constant.CONFLICT);
								return map;
							} else {
								admin.setMobile(adminRequestPayload2.getMobile());
							}

							if (file != null) {
								imagePath = baseUrl + FileUploader.uploadLanguageFile(file, imageDir);
								log.info("Image update successfully! status - {}", imagePath);
							}
							admin.setAddress(adminRequestPayload2.getAddress());
							admin.setCity(adminRequestPayload2.getCity());
							admin.setCountry(adminRequestPayload2.getCountry());
							admin.setPinCode(adminRequestPayload2.getPinCode());
							admin.setImageUrl(imagePath);
							admin.setName(adminRequestPayload2.getName());
							admin.setEntryDate(new Date());
							admin.setPassword(adminRequestPayload2.getPassword());
							admin.setStatus(Constant.ONE);
							admin.setRoleId(role.getId());
							admin.setRole(role);
							admin = adminRepository.save(admin);

							/* save admin logs */
							BeanUtils.copyProperties(admin, adminHistory);
							adminHistory.setLastUpdationDate(new Date());
							adminLogsRepository.save(adminHistory);
							log.info(" Admin history data saved successfully !! - { } " + Constant.OK);

							AdminResponsePayload adminResponsePayload = new AdminResponsePayload();
							BeanUtils.copyProperties(admin, adminResponsePayload);

							RoleResponsePayload roleResponsePayload = new RoleResponsePayload();
							BeanUtils.copyProperties(admin.getRole(), roleResponsePayload);
							roleResponsePayload
									.setEntryDate(DateUtil.convertDateToStringDateTime(admin.getRole().getEntryDate()));
							roleResponsePayload.setUpdationDate(
									DateUtil.convertDateToStringDateTime(admin.getRole().getUpdationDate()));

							List<RoleModuleMappingResponse> roleModuleMappingResponseList = new ArrayList<>();
							for (RoleModuleMapping roleModuleMapping : admin.getRole().getRoleModuleMappingList()) {
								RoleModuleMappingResponse roleModuleMapResponse = new RoleModuleMappingResponse();
								BeanUtils.copyProperties(roleModuleMapping, roleModuleMapResponse);
								roleModuleMapResponse.setEntryDate(
										DateUtil.convertDateToStringDateTime(roleModuleMapping.getEntryDate()));
								roleModuleMapResponse.setUpdationDate(
										DateUtil.convertDateToStringDateTime(roleModuleMapping.getUpdationDate()));
								roleModuleMappingResponseList.add(roleModuleMapResponse);
							}
							roleResponsePayload.setRoleModuleMappingResponseList(roleModuleMappingResponseList);

							adminResponsePayload.setRoleResponsePayload(roleResponsePayload);
							adminResponsePayload
									.setEntryDate(DateUtil.convertDateToStringDateTime(admin.getEntryDate()));
							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.ADMIN_ADDED_SUCCESS_MESSAGE);
							map.put(Constant.DATA, adminResponsePayload);
							log.info("Admin registered successfully! status - {}", adminResponsePayload);
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
							map.put(Constant.MESSAGE, Constant.ROLE_NOT_FOUND_MESSAGE);
							log.info("Given role not found into the database! status - {}", Constant.NOT_FOUND);
						}
					}

				} else {
					map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					map.put(Constant.MESSAGE, Constant.ADMIN_ID_NOT_EMPTY);
					log.info("Admin id can't be null or empty !!" + adminRequest.getId());
				}
			} else {
				map.put(Constant.RESPONSE_CODE, map2.get("responseCode"));
				map.put(Constant.MESSAGE, map2.get("message"));
			}
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}

		return map;
	}

	@Override
	public Map<String, Object> adminLogin(AdminLoginRequestPayload adminLoginRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		Admin admin = adminRepository.findByEmailAndStatus(adminLoginRequestPayload.getEmail(), Constant.ONE);
		try {
			if (admin != null) {
				log.info("Record found! status - {}", Constant.OK);
				if (admin.getPassword().matches(adminLoginRequestPayload.getPassword())) {

					/* Generating otp */
					Integer otp = UtilityMethods.randomNumber();
					/* Otp sending to the admin mobile */
					Integer sentOtp = UtilityMethods.sendOtp(otp);

					AdminResponsePayload adminResponsePayload = new AdminResponsePayload();
					BeanUtils.copyProperties(admin, adminResponsePayload);

					RoleResponsePayload roleResponsePayload = new RoleResponsePayload();
					BeanUtils.copyProperties(admin.getRole(), roleResponsePayload);
					roleResponsePayload
							.setEntryDate(DateUtil.convertDateToStringDateTime(admin.getRole().getEntryDate()));
					roleResponsePayload
							.setUpdationDate(DateUtil.convertDateToStringDateTime(admin.getRole().getUpdationDate()));

					List<RoleModuleMappingResponse> roleModuleMappingResponseList = new ArrayList<>();
					for (RoleModuleMapping roleModuleMapping : admin.getRole().getRoleModuleMappingList()) {
						RoleModuleMappingResponse roleModuleMapResponse = new RoleModuleMappingResponse();
						BeanUtils.copyProperties(roleModuleMapping, roleModuleMapResponse);
						roleModuleMapResponse
								.setEntryDate(DateUtil.convertDateToStringDateTime(roleModuleMapping.getEntryDate()));
						roleModuleMapResponse.setUpdationDate(
								DateUtil.convertDateToStringDateTime(roleModuleMapping.getUpdationDate()));
						roleModuleMappingResponseList.add(roleModuleMapResponse);
					}
					roleResponsePayload.setRoleModuleMappingResponseList(roleModuleMappingResponseList);

					adminResponsePayload.setRoleResponsePayload(roleResponsePayload);
					adminResponsePayload.setEntryDate(DateUtil.convertDateToStringDateTime(admin.getEntryDate()));
					adminResponsePayload.setUpdationDate(DateUtil.convertDateToStringDateTime(admin.getUpdationDate()));

					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.ADMIN_LOGIN_SUCCESSFULLY);
					map.put(Constant.DATA, adminResponsePayload);
					log.info("Admin login successfully! status - {}", adminResponsePayload);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.NOT_AUTHORIZED);
					map.put(Constant.MESSAGE, Constant.INVALID_CREDENTIAL);
					log.info("Invalid credentials! status - {}", Constant.NOT_AUTHORIZED);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.NOT_AUTHORIZED);
				map.put(Constant.MESSAGE, Constant.INVALID_CREDENTIAL);
				log.info("Invalid credentials! status - {}", Constant.NOT_AUTHORIZED);
			}

		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}
		return map;
	}

	@Override
	public Map<String, Object> changePassword(Long id, String oldPassword, String password) {
		Map<String, Object> map = new HashMap<>();
		try {
			AdminHistory adminHistory = new AdminHistory();
			Admin admin = adminRepository.findByIdAndStatus(id, Constant.ONE);
			if (admin != null && admin.getPassword() != null) {
				log.info("Data found! status - {}", admin);
				/* admin history saved */
				BeanUtils.copyProperties(admin, adminHistory);
				adminHistory.setLastUpdationDate(new Date());
				adminLogsRepository.save(adminHistory);
				log.info("admin history data saved sucessfully! status - {}", Constant.OK);

				if (admin.getPassword().equals(oldPassword)) {
					admin.setPassword(password);
					admin.setUpdationDate(new Date());
					admin.setPasswordUpdationDate(new Date());
					admin = adminRepository.save(admin);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.PASSWORD_UPDATE);
					log.info("password changed sucessfully! status - {}", Constant.OK);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.FORBIDDEN);
					map.put(Constant.MESSAGE, Constant.PASSWORD_NOT_MATCH);
					log.info("old password not match!! status - {}", Constant.BAD_REQUEST);
					return map;
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.FIELD_NOT_EMPTY);
				log.info("email not valid!! status - {}", Constant.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
			return map;
		}
		return map;
	}

	@Override
	public Map<String, Object> blockAdmin(AdminBlockRequestPayload adminBlockRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		try {
			AdminHistory adminHistory = new AdminHistory();
			Optional<Admin> admin = Optional.empty();
			Admin oldAdmin = new Admin();
			admin = adminRepository.findById(adminBlockRequestPayload.getId());
			if (admin.isPresent()) {
				log.info("Record found! status - {}", Constant.OK);
				oldAdmin = admin.get();
				/* admin history saved */
				BeanUtils.copyProperties(oldAdmin, adminHistory);
				adminHistory.setLastUpdationDate(new Date());
				adminLogsRepository.save(adminHistory);
				log.info("Admin history data saved successfully !! status - {}", Constant.OK);

				/* ------------------- Here unblock the admin ----------------- */
				if (adminBlockRequestPayload.getStatus().equals(Constant.ONE)) {
					if (oldAdmin.getStatus().equals(Constant.ONE)) {
						map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
						map.put(Constant.MESSAGE, Constant.ALREADY_UNBLOCKED_MESSAGE);
						log.info("Already unblocked! status - {}", Constant.CONFLICT);
						return map;
					} else {
						oldAdmin.setStatus(adminBlockRequestPayload.getStatus());
						oldAdmin.setUpdationDate(new Date());
						oldAdmin = adminRepository.save(oldAdmin);
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.UNBLOCKED_SUCCESS_MESSAGE);
						log.info("unblocked successfully! status - {}", Constant.OK);
					}
				}

				/* ------------------- Here block the admin ----------------- */
				else if (adminBlockRequestPayload.getStatus().equals(Constant.TWO)) {
					if (oldAdmin.getStatus().equals(Constant.TWO)) {
						map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
						map.put(Constant.MESSAGE, Constant.ALREADY_UNBLOCKED_MESSAGE);
						log.info("Already unblocked! status - {}", Constant.CONFLICT);
						return map;
					} else {
						oldAdmin.setStatus(adminBlockRequestPayload.getStatus());
						oldAdmin.setUpdationDate(new Date());
						oldAdmin = adminRepository.save(oldAdmin);
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.BLOCKED_SUCCESS_MESSAGE);
						log.info("Blocked successfully! status - {}", Constant.OK);
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
	public Map<String, Object> deleteAdmin(Long id) {
		Map<String, Object> map = new HashMap<>();
		try {
			AdminHistory adminHistory = new AdminHistory();
			if (id != null) {
				Optional<Admin> admin = Optional.empty();
				Admin oldAdmin = new Admin();
				admin = adminRepository.findById(id);

				if (!admin.isEmpty()) {
					oldAdmin = admin.get();
					/* admin history saved */
					BeanUtils.copyProperties(oldAdmin, adminHistory);
					adminHistory.setLastUpdationDate(new Date());
					adminLogsRepository.save(adminHistory);
					log.info(" Admin history data saved successfully !! status - { }" + Constant.OK);

					if (oldAdmin.getStatus().equals(Constant.ZERO)) {
						map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
						map.put(Constant.MESSAGE, Constant.ALREADY_DELETED_MESSAGE);
						log.info("Already deleted! status - {}", Constant.CONFLICT);
					} else {
						/* ----- Perform delete operation ----- */
						oldAdmin.setStatus(Constant.ZERO);
						oldAdmin.setUpdationDate(new Date());
						oldAdmin = adminRepository.save(oldAdmin);

						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.DELETED_MESSAGE);
						log.info("Deleted successfully! status - {}", Constant.OK);
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
					map.put(Constant.MESSAGE, Constant.ID_NOT_FOUND_MESSAGE);
					log.info("Given id not found into the database! status - {}", Constant.NOT_FOUND);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.ID_CAN_NOT_NULL_MESSAGE);
				log.info("Id can not null, it should be valid! status - {}", Constant.BAD_REQUEST);
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

	public Map<String, Object> getAllAdmin(Integer pageIndex, Integer pageSize, String keyWord) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<AdminResponsePayload> adminResponsePayloadList = new ArrayList<>();
			AdminResponsePage adminResponsePage = new AdminResponsePage();
			List<Admin> adminlist = new ArrayList<>();
			Page<Admin> page = null;
			if (pageSize != 0) {
				Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());
				if (keyWord != null && !keyWord.isBlank()) {
					page = adminRepository.findAllAdmin(keyWord, pageable);
				} else {
					page = adminRepository.findAllAdmin(pageable);
				}
			} else {
				if (keyWord != null && !keyWord.isBlank()) {
					adminlist = adminRepository.findAllSearchingAdmin(keyWord);
				} else {
					adminlist = adminRepository.findAllAdmins();
				}
				log.info(" Total admins without pagination :- " + adminlist.size());
			}
			if (page != null && page.getContent().size() > Constant.ZERO) {
				adminlist = page.getContent();
			}
			for (Admin admin : adminlist) {
				List<RoleModuleMappingResponse> roleModuleMappingResponseList = new ArrayList<>();
				for (RoleModuleMapping roleModuleMapping : admin.getRole().getRoleModuleMappingList()) {
					RoleModuleMappingResponse roleModuleMapResponse = new RoleModuleMappingResponse();
					BeanUtils.copyProperties(roleModuleMapping, roleModuleMapResponse);
					roleModuleMapResponse
							.setEntryDate(DateUtil.convertDateToStringDateTime(roleModuleMapping.getEntryDate()));
					roleModuleMapResponse
							.setUpdationDate(DateUtil.convertDateToStringDateTime(roleModuleMapping.getUpdationDate()));
					roleModuleMappingResponseList.add(roleModuleMapResponse);
				}
				RoleResponsePayload roleResponsePayload = new RoleResponsePayload();
				BeanUtils.copyProperties(admin.getRole(), roleResponsePayload);
				roleResponsePayload.setRoleModuleMappingResponseList(roleModuleMappingResponseList);
				roleResponsePayload.setEntryDate(DateUtil.convertDateToStringDateTime(admin.getRole().getEntryDate()));
				roleResponsePayload
						.setUpdationDate(DateUtil.convertDateToStringDateTime(admin.getRole().getUpdationDate()));

				AdminResponsePayload adminResponsePayload = new AdminResponsePayload();
				BeanUtils.copyProperties(admin, adminResponsePayload);
				adminResponsePayload.setRoleResponsePayload(roleResponsePayload);
				adminResponsePayload.setEntryDate(DateUtil.convertDateToStringDateTime(admin.getEntryDate()));
				adminResponsePayload.setUpdationDate(DateUtil.convertDateToStringDateTime(admin.getUpdationDate()));
				adminResponsePayloadList.add(adminResponsePayload);
			}
			adminResponsePage.setAdminResponsePayloadList(adminResponsePayloadList);
			adminResponsePage.setPageIndex(page != null ? page.getNumber() : null);
			adminResponsePage.setPageSize(page != null ? page.getSize() : null);
			adminResponsePage.setTotalElement(page != null ? page.getTotalElements() : adminlist.size());
			adminResponsePage.setTotalPages(page != null ? page.getTotalPages() : null);
			adminResponsePage.setIsLastPage(page != null ? page.isLast() : null);
			adminResponsePage.setIsFirstPage(page != null ? page.isFirst() : null);

			map.put(Constant.RESPONSE_CODE, Constant.OK);
			map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
			map.put(Constant.DATA, adminResponsePage);
			log.info("Record found! status - {}", adminResponsePage);
		} catch (

		DataAccessResourceFailureException e) {
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
	public Map<String, Object> uploadLanguageFile(MultipartFile docfile) {
		Map<String, Object> map = new HashMap<>();
		try {
			if (docfile != null && !docfile.isEmpty()) {
				String filename = "";
				Integer count = 0;
				Integer newRowcount = 0;

//				File localFile = new File("D:\\Alok_Projects\\upload\\language_file.xlsx");
				/* reading data from a file in the form of bytes */
//				XSSFWorkbook uploadedWorkbook = new XSSFWorkbook(docfile.getInputStream());
//				XSSFWorkbook uploadedWorkbook = null;//new XSSFWorkbook(docfile.getInputStream());
//				XSSFSheet uploadedSheet = uploadedWorkbook.getSheetAt(0);
//				File file = new File(serverFilePath);
				XSSFWorkbook uploadedWorkbook = null;
				XSSFSheet uploadedSheet = null;
				File file = null;

				String extension = FilenameUtils.getExtension(docfile.getOriginalFilename());
				if (extension.equals("xlsx")) {
					uploadedWorkbook = new XSSFWorkbook(docfile.getInputStream());
					uploadedSheet = uploadedWorkbook.getSheetAt(0);
					file = new File(serverFilePath);
					if (file.exists()) {
						System.out.println("File found: " + file);
						log.info("Uploading file found into the server! status - {}", file);
						try {
							/* reading data from a file in the form of bytes */
//						FileInputStream fis = new FileInputStream(
//								new File("D:\\Alok_Projects\\upload\\language_file.xlsx"));
							FileInputStream fis = new FileInputStream(new File(serverFilePath));

							XSSFWorkbook serverWorkbook = new XSSFWorkbook(fis);

//						XSSFSheet uploadedSheet = uploadedWorkbook.getSheetAt(0);
							XSSFSheet serverSheet = serverWorkbook.getSheetAt(0);

							// Getting the count of existing records
//						int uploadRowCount = uploadedSheet.getLastRowNum();
//						int serverRowCount = serverSheet.getLastRowNum();
//							int uploadRowCount = uploadedSheet.getPhysicalNumberOfRows();
							int serverRowCount = serverSheet.getPhysicalNumberOfRows();
							for (int i = 0; i < uploadedSheet.getPhysicalNumberOfRows(); i++) {
								XSSFRow row = uploadedSheet.getRow(i);
//								int rowCount = row.getRowNum() + 1;
								if (row.getRowNum() + 1 <= serverRowCount) {
									for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
										XSSFCell uploadedCellvalue = uploadedSheet.getRow(i).getCell(j);
										XSSFCell serverCellvalue = serverSheet.getRow(i).getCell(j);

										if (uploadedCellvalue.getCellType().equals(serverCellvalue.getCellType())) {
											if (uploadedCellvalue.getCellType() == CellType.STRING) {
												String uploadValue = uploadedCellvalue.getStringCellValue();
												String serverValue = serverCellvalue.getStringCellValue();
												if (!uploadValue.equals(serverValue) && (j != 3)) {
													count++;
													log.info(count + " : Its not matched => " + " New Value : "
															+ uploadValue + "," + " Old Value : " + serverValue);
												}
											}
											if (uploadedCellvalue.getCellType() == CellType.NUMERIC) {
												Integer uploadValue = (int) uploadedCellvalue.getNumericCellValue();
												Integer serverValue = (int) serverCellvalue.getNumericCellValue();
												if (!uploadValue.equals(serverValue) && (j != 3)) {
													count++;
													log.info(count + " : Its not matched => " + " New Value : "
															+ uploadValue + "," + " Old Value : " + serverValue);
												}
											}
										}
									}
								} else {
									// Adding new row
									for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
										XSSFCell uploadedCellvalue = uploadedSheet.getRow(i).getCell(j);

										if (uploadedCellvalue.getCellType() == CellType.STRING) {
											String uploadValue = uploadedCellvalue.getStringCellValue();
											newRowcount++;
											log.info(newRowcount + " : New data adding in server file! status - {}",
													uploadValue);
										}
										if (uploadedCellvalue.getCellType() == CellType.NUMERIC) {
											Integer uploadValue = (int) uploadedCellvalue.getNumericCellValue();
											newRowcount++;
											log.info(newRowcount + " : New data adding in server file! status - {}",
													uploadValue);
										}
									}
								}
							}
							serverWorkbook.close();

							// -------
							try {
								/* File will be deleted if the both file content not matched */
								if ((count > 0 || newRowcount > 0) || (count > 0 && newRowcount > 0)) {
									file.delete();
									log.info("Old file deleted successfully! status - {}", Constant.OK);
									filename = FileUploader.uploadLanguageFile(docfile, uploadDir);

									/* Creating file directory in local drive */
									File filePath = UtilityMethods.createFileDirectory();

									/*
									 * Write data on new created excel file in local directory
									 */
									FileOutputStream os = new FileOutputStream(filePath);
									uploadedWorkbook.write(os);
									os.close();

									/* Reset flag value */
									Integer replaceCount = 0;
									for (int i = 0; i < uploadedSheet.getPhysicalNumberOfRows(); i++) {
										XSSFRow row = uploadedSheet.getRow(i);

										for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
											XSSFCell uploadedCellvalue = uploadedSheet.getRow(i).getCell(j);
											if (uploadedCellvalue.getCellType()
													.equals(uploadedCellvalue.getCellType())) {
												if (uploadedCellvalue.getCellType() == CellType.NUMERIC) {
													Integer uploadValue = (int) uploadedCellvalue.getNumericCellValue();
													XSSFCell cellValue = row.getCell(j);
													Integer cellVal = (int) cellValue.getNumericCellValue();
													if (j == 3 && cellVal != 0) {
														replaceCount++;
														log.info(replaceCount + " : New Value : " + uploadValue);
														Cell cell = row.getCell(j);
														cell.setBlank();
														cell.setCellValue(cell.toString());
														cell.setCellValue(0);
													}
												}
											}
										}
									}

									/* Crating output stream and writing the updated workbook */
									FileOutputStream osNew = new FileOutputStream(filePath);
									uploadedWorkbook.write(osNew);
									uploadedWorkbook.close();
									osNew.close();
									fis.close();// Close input stream

									map.put(Constant.RESPONSE_CODE, Constant.OK);
									map.put(Constant.MESSAGE, Constant.UPDATED_FILE_UPLOAD_SUCCESS_MESSAGE);
									map.put("File Url", filename);
									log.info("Updated file uploaded sucessfully! status - {}", filename);
								} else {
									map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
									map.put(Constant.MESSAGE, Constant.NO_CHANGES_FOUND_IN_FILE_MESSAGE);
									log.info(
											"No Changes found into your local file! so you can't upload the file! status - {}",
											Constant.OK);
									return map;
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						log.info(file.getName() + " file Not exist into server! so uploading new file! status - {}",
								file);
						filename = FileUploader.uploadLanguageFile(docfile, uploadDir);

						/* Creating file directory in local drive */
						File filePath = UtilityMethods.createFileDirectory();

						/*
						 * Write data on new created excel file in local directory
						 */
						FileOutputStream os = new FileOutputStream(filePath);
						uploadedWorkbook.write(os);
						uploadedWorkbook.close();
						os.close();

						/* Reset flag value */
						Integer replaceCount = 0;
						for (int i = 0; i < uploadedSheet.getPhysicalNumberOfRows(); i++) {
							XSSFRow row = uploadedSheet.getRow(i);

							for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
								XSSFCell uploadedCellvalue = uploadedSheet.getRow(i).getCell(j);
								if (uploadedCellvalue.getCellType().equals(uploadedCellvalue.getCellType())) {
									if (uploadedCellvalue.getCellType() == CellType.NUMERIC) {
										Integer uploadValue = (int) uploadedCellvalue.getNumericCellValue();
										XSSFCell cellValue = row.getCell(j);
										Integer cellVal = (int) cellValue.getNumericCellValue();
										if (j == 3 && cellVal != 0) {
											System.out.println("Its not matched : " + uploadValue);
											replaceCount++;
											System.out.println("Total count : " + replaceCount);
											Cell cell = row.getCell(j);
											cell.setBlank();
											cell.setCellValue(cell.toString());
											cell.setCellValue(0);
										}
									}
								}
							}
						}

						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.FILE_UPLOAD_SUCCESS_MESSAGE);
						map.put("File Url", filename);
						log.info("New file uploaded sucessfully! status - {}", filename);
					}

				} else {
					map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					map.put(Constant.MESSAGE, Constant.NOT_A_EXCEL_FILE_MESSAGE);
					log.info("This file is not an excel file! status - {}", Constant.BAD_REQUEST);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.FILE_UPLOAD_FAILED_MESSAGE);
				log.info("File uploading failed! please try again! status - {}", Constant.OK);
			}
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}
		return map;
	}

	@Override
	public Map<String, Object> forgetPassword(String email) {
		Map<String, Object> responce = new HashMap<>();
		try {
			AdminHistory adminHistory = new AdminHistory();
			Admin admin = adminRepository.findByEmailAndStatus(email, Constant.ONE);
			BeanUtils.copyProperties(admin, adminHistory);
			adminHistory.setLastUpdationDate(new Date());
			adminLogsRepository.save(adminHistory);
			log.info(" Admin history data saved successfully !! - status { } " + Constant.OK);

			if (admin != null) {
				String token = "gdftr56r5r7867gyvgf6rftycgt76ftycghyu";
				Long id = admin.getId();
				String baseUrl = Constant.FORGET_PASSWORD_PAGE_LINK;
				String link = baseUrl + "token=" + token + "&id=" + id;
				String message = HtmlTemplate.forgetPasswordTemplate(email, link);

				SendMail.sendMailTemplate(email, Constant.RESET_PASSWORD, message);
				responce.put(Constant.RESPONSE_CODE, Constant.OK);
				responce.put(Constant.MESSAGE, Constant.EMAIL_SEND_SUCCESSFULLY);
				return responce;
			} else {
				responce.put(Constant.RESPONSE_CODE, Constant.NOT_EXIST);
				responce.put(Constant.MESSAGE, Constant.EMAIL_NOT_FOUND_MESSAGE);
			}
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			responce.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			responce.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
		} catch (Exception e) {

			e.printStackTrace();
			responce.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			responce.put(Constant.MESSAGE, Constant.ERROR_MESSAGE);
			return responce;
		}
		return responce;
	}

	@Override
	public Map<String, Object> resetPassword(Long id, String password) {
		Map<String, Object> map = new HashMap<>();
		try {
			AdminHistory adminHistory = new AdminHistory();
			if (password != null && id != null) {
				Admin admin = adminRepository.findByIdAndStatus(id, Constant.ONE);
				BeanUtils.copyProperties(admin, adminHistory);
				adminHistory.setLastUpdationDate(new Date());
				adminLogsRepository.save(adminHistory);
				log.info(" Admin history data saved successfully !! - status { } " + Constant.OK);

				if (admin != null) {
					admin.setPassword(password);
					admin.setUpdationDate(new Date());
					admin.setPasswordUpdationDate(new Date());
					admin = adminRepository.save(admin);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.PASSWORD_RESET);
					log.info("password reset sucessfully! status - {}", admin);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
					map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
					log.info("Record not found! status - {}", Constant.NOT_FOUND);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.USER_ID_PASSWORD_NOT_EMPTY_MESSAGE);
				log.info("User id and password field should not empty!! status - {}", Constant.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> excelToJson() {
		Map<String, Object> map = new HashMap<>();
		try {
			FileInputStream fis = new FileInputStream(serverFilePath);

			XSSFWorkbook workBook = new XSSFWorkbook(fis);
			XSSFSheet workSheet = workBook.getSheetAt(0);

			List<JSONObject> dataList = new ArrayList<>();
			XSSFRow header = workSheet.getRow(0);

			for (int i = 1; i < workSheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = workSheet.getRow(i);
				JSONObject rowJsonObject = new JSONObject();

				for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
					String columnName = header.getCell(j).toString();
					String columnValue = row.getCell(j).toString();
					rowJsonObject.put(columnName, columnValue);
				}
				dataList.add(rowJsonObject);
			}
			map.put(Constant.MESSAGE, Constant.DATA_FOUND);
			map.put(Constant.DATA, dataList);
			log.info("Data found......", dataList);
			System.out.println("json formet" + dataList);

		} catch (IOException e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> sendOtp(AdminLoginRequestPayload adminLoginRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		Admin admin = adminRepository.findByEmail(adminLoginRequestPayload.getEmail());
		try {
			if (admin != null) {
				if (!admin.getStatus().equals(Constant.ZERO)) {
					if (!admin.getStatus().equals(Constant.TWO)) {
						log.info("Record found! status - {}", Constant.OK);
						if (admin.getPassword().matches(adminLoginRequestPayload.getPassword())) {
							Otp otpObj = new Otp();
							String otp = "";
							otpObj = otpRepository.findByAdminIdFk(admin.getId());
							if (otpObj != null) {
								/* Generating otp */
								otp = utilityMethods.sendOtp(admin);
								log.info("OTP generated successfully! status - {}", Constant.OK);
								/* Otp sending to the admin email */
								String message = HtmlTemplate.htmlSentOtp(String.valueOf(otp));
								SendMail.sendMailTemplate(admin.getEmail(), "OTP", message);
								log.info("OTP sent successfully to your email! status - {}", Constant.OK);
							} else {
								otp = utilityMethods.sendOtp(admin);
								log.info("OTP generated successfully! status - {}", Constant.OK);
								/* Otp sending to the admin email */
								String message = HtmlTemplate.htmlSentOtp(String.valueOf(otp));
								SendMail.sendMailTemplate(admin.getEmail(), "OTP", message);
								log.info("OTP sent successfully to your mobile! status - {}", Constant.OK);
							}
							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.OTP_SENT_MESSAGE);
							map.put(Constant.DATA, otp);//
							log.info(
									"OTP temporarily stored, it will be deleted automatically after time expired! status - {}",
									Constant.OK);
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.NOT_AUTHORIZED);
							map.put(Constant.MESSAGE, Constant.INVALID_CREDENTIAL);
							log.info("Invalid credentials! status - {}", Constant.NOT_AUTHORIZED);
						}
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.NOT_AUTHORIZED);
						map.put(Constant.MESSAGE, Constant.ACCOUNT_BLOCKED_MESSAGE);
						log.info("Your account have blocked! status - {}", Constant.NOT_AUTHORIZED);
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.NOT_AUTHORIZED);
					map.put(Constant.MESSAGE, Constant.ACCOUNT_DELETED_MESSAGE);
					log.info("Your account have deleted! status - {}", Constant.NOT_AUTHORIZED);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.NOT_AUTHORIZED);
				map.put(Constant.MESSAGE, Constant.INVALID_CREDENTIAL);
				log.info("Invalid credentials! status - {}", Constant.NOT_AUTHORIZED);
			}
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}
		return map;
	}

	@Override
	public Map<String, Object> login(LoginRequestPayload loginRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		Admin admin = null;
		try {
			AdminHistory adminHistory = new AdminHistory();
			admin = adminRepository.findByEmailAndStatus(loginRequestPayload.getEmail(), Constant.ONE);
			if (admin != null) {
				log.info("Record found! status - {}", Constant.OK);
				Otp otpObj = new Otp();
				otpObj = otpRepository.findByAdminIdFk(admin.getId());
				if (otpObj != null) {
					log.info("OTP found! status - {}", Constant.OK);
					if (otpObj.getOtp().equals(loginRequestPayload.getOtp())) {
						AdminResponsePayload adminResponsePayload = new AdminResponsePayload();
						BeanUtils.copyProperties(admin, adminResponsePayload);

						RoleResponsePayload roleResponsePayload = new RoleResponsePayload();
						BeanUtils.copyProperties(admin.getRole(), roleResponsePayload);
						roleResponsePayload
								.setEntryDate(DateUtil.convertDateToStringDateTime(admin.getRole().getEntryDate()));
						roleResponsePayload.setUpdationDate(
								DateUtil.convertDateToStringDateTime(admin.getRole().getUpdationDate()));

						List<RoleModuleMappingResponse> roleModuleMappingResponseList = new ArrayList<>();
						for (RoleModuleMapping roleModuleMapping : admin.getRole().getRoleModuleMappingList()) {
							RoleModuleMappingResponse roleModuleMapResponse = new RoleModuleMappingResponse();
							BeanUtils.copyProperties(roleModuleMapping, roleModuleMapResponse);
							roleModuleMapResponse.setEntryDate(
									DateUtil.convertDateToStringDateTime(roleModuleMapping.getEntryDate()));
							roleModuleMapResponse.setUpdationDate(
									DateUtil.convertDateToStringDateTime(roleModuleMapping.getUpdationDate()));
							roleModuleMappingResponseList.add(roleModuleMapResponse);
						}
						roleResponsePayload.setRoleModuleMappingResponseList(roleModuleMappingResponseList);

						adminResponsePayload.setRoleResponsePayload(roleResponsePayload);
						adminResponsePayload.setEntryDate(DateUtil.convertDateToStringDateTime(admin.getEntryDate()));
						adminResponsePayload
								.setUpdationDate(DateUtil.convertDateToStringDateTime(admin.getUpdationDate()));

						Date date = otpObj.getCreationDate();
						long otpTimestampMillis = date.getTime(); // Replace with your OTP timestamp
						long currentTimestampMillis = Instant.now().toEpochMilli();
						/* Check if OTP is within a 15-second window */
						long timeDifferenceSeconds = ChronoUnit.SECONDS.between(
								Instant.ofEpochMilli(otpTimestampMillis), Instant.ofEpochMilli(currentTimestampMillis));
						if (timeDifferenceSeconds <= 59) {
							System.out.println("OTP is valid.");
							log.info("user otp found " + otpObj);
							if (otpObj.getOtp().equals(loginRequestPayload.getOtp())) {
								/* Deleting OTP after otp verified */
								otpRepository.deleteById(otpObj.getId());
								/* save the admin history */
								adminHistory.setEmail(loginRequestPayload.getEmail());
								adminHistory.setLogin(true);
								adminHistory.setLastUpdationDate(new Date());

								log.info("Old OTP deleted successfully! status - {}", Constant.OK);
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.ADMIN_LOGIN_SUCCESSFULLY);
								map.put(Constant.DATA, adminResponsePayload);
								log.info("Admin login successfully! status - {}", adminResponsePayload);
							} else {
								/* save the admin history */
								adminHistory.setEmail(loginRequestPayload.getEmail());
								adminHistory.setLogin(false);
								adminHistory.setLastUpdationDate(new Date());
								map.put(Constant.HTTP_STATUS, Constant.BAD_REQUEST);
								map.put(Constant.MESSAGE, Constant.INCORRECT_OTP);
								log.info(Constant.INCORRECT_OTP + "! status - {}", Constant.BAD_REQUEST);
							}
						} else {
							otpRepository.deleteById(otpObj.getId());
							/* save the admin history */
							adminHistory.setEmail(loginRequestPayload.getEmail());
							adminHistory.setLogin(false);
							adminHistory.setLastUpdationDate(new Date());
							map.put(Constant.HTTP_STATUS, Constant.BAD_REQUEST);
							map.put(Constant.MESSAGE, "OTP has expired !!");
							log.info("OTP has expired ! status - {}", Constant.BAD_REQUEST);
						}
					} else {
						/* save the admin history */
						adminHistory.setEmail(loginRequestPayload.getEmail());
						adminHistory.setLogin(false);
						adminHistory.setLastUpdationDate(new Date());
						map.put(Constant.RESPONSE_CODE, Constant.NOT_AUTHORIZED);
						map.put(Constant.MESSAGE, Constant.INVALID_CREDENTIAL);
						log.info("Invalid credentials! status - {}", Constant.NOT_AUTHORIZED);
					}
				} else {
					/* save the admin history */
					adminHistory.setEmail(loginRequestPayload.getEmail());
					adminHistory.setLogin(false);
					adminHistory.setLastUpdationDate(new Date());
					map.put(Constant.RESPONSE_CODE, Constant.NOT_AUTHORIZED);
					map.put(Constant.MESSAGE, Constant.INVALID_CREDENTIAL);
					log.info("OTP not found! status - {}", Constant.NOT_FOUND);
				}
			} else {
				/* save the admin history */
				adminHistory.setEmail(loginRequestPayload.getEmail());
				adminHistory.setLogin(false);
				adminHistory.setLastUpdationDate(new Date());
				map.put(Constant.RESPONSE_CODE, Constant.NOT_AUTHORIZED);
				map.put(Constant.MESSAGE, Constant.INVALID_CREDENTIAL);
				log.info("Invalid credentials! status - {}", Constant.NOT_AUTHORIZED);
			}
			adminLogsRepository.save(adminHistory);
			log.info("save the admin history data successfully! status - {}", Constant.OK);
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}
		return map;
	}

	@Override
	public Map<String, Object> getAdmin() {
		Long adminCount = 0L, activeCount = 0L, deletedCount = 0L, inactiveCount = 0L, verifiedCount = 0L;
		Map<String, Object> map = new HashMap<>();
		try {
			CountResponse countResponse = new CountResponse();
			/*---------------- get all register admins count ------------*/
			adminCount = adminRepository.findAllAdminsCount();
			log.info("total count :- " + adminCount);
			if (adminCount != null) {
				countResponse.setTotal(adminCount);
				/*--------------- get all active admins count ---------------*/
				activeCount = adminRepository.findAllActiveAdminsCount();
				log.info("Active admin count :- " + activeCount);
				if (activeCount != null) {
					countResponse.setActive(activeCount);
				} else {
					countResponse.setActive(0L);
					log.info("Active admin not found :- " + activeCount);
				}
				/*--------------- get all inactive admins count ---------------*/
				inactiveCount = adminRepository.findAllInactiveAdminsCount();
				log.info("Inactive admin count :- " + inactiveCount);
				if (inactiveCount != null) {
					countResponse.setInactive(inactiveCount);
				} else {
					countResponse.setInactive(0L);
					log.info("Inactive admin not found :- " + inactiveCount);
				}
				/*--------------- get all deleted admins count -----------------*/
				deletedCount = adminRepository.findAllDeletedAdminsCount();
				log.info(" Deleted admin count :- " + deletedCount);
				if (deletedCount != null) {
					countResponse.setDeleted(deletedCount);
				} else {
					countResponse.setDeleted(0L);
					log.info("Deleted admin not found :- " + deletedCount);
				}
				/* set verified admin count */
				countResponse.setNot_verified(verifiedCount);
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.ADMIN_COUNT);
				map.put(Constant.DATA, countResponse);
				log.info("data found successfully !! " + countResponse);
			} else {
				countResponse.setActive(activeCount);
				countResponse.setInactive(inactiveCount);
				countResponse.setTotal(adminCount);
				countResponse.setDeleted(deletedCount);
				countResponse.setNot_verified(verifiedCount);
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.ADMIN_NOT_FOUND);
				map.put(Constant.DATA, countResponse);
				log.info("data not found" + countResponse);
			}

		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}
		return map;
	}

	@Override
	public Map<String, Object> getAdminProfile(Long id) {
		Map<String, Object> map = new HashMap<>();

		try {
			if (id != null) {

				Admin admin = adminRepository.findByIdAndStatus(id, Constant.ONE);
				if (admin != null) {
					// Include the admin data in the response map
					map.put(Constant.DATA, admin); // Add admin data to the map
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.FIND_MESSAGE);
					log.info("Find successfully! status - {}", Constant.OK);
				} else {
					// Handle the case where admin is not found with the given ID and active status
					map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
					map.put(Constant.MESSAGE, Constant.GIVEN_ID_NOT_FOUND_MESSAGE);
					log.info("This given Id is block or deleted in the database or not active! status - {}",
							Constant.NOT_FOUND);
				}
			} else {
				// Handle the case where ID is null
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.ID_CAN_NOT_NULL_MESSAGE);
				log.info("ID cannot be null, it should be valid! status - {}", Constant.BAD_REQUEST);
			}

		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
			log.error("Exception: " + e.getMessage());
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception: " + e.getMessage());
		}

		return map;
	}

	@Override
	public Map<String, Object> getAdminGraph() {
		Map<String, Object> map = new HashMap<>();
		AdminGraphResponse graphResponse = new AdminGraphResponse();
		try {
			List<MonthObject> list = new ArrayList<MonthObject>();
			Long totalCount = 0L;
//			List<String> starting_month = new ArrayList<String>();
//			List<String> ending_month = new ArrayList<String>();
//			List<String> month = new ArrayList<String>();

			String[] starting_month = { "", "", "", "", "", "", "", "", "", "", "", "" };
			String[] ending_month = { "", "", "", "", "", "", "", "", "", "", "", "" };
			String[] month = { "", "", "", "", "", "", "", "", "", "", "", "" };
			LocalDate currentDate = LocalDate.now();
			LocalDate startDate = null;
			startDate = currentDate;
			LocalDate endDate = null;

			// Loop through 12 months
			Year currentYear = Year.now();

			for (int i = 1; i <= 12; i++) {
				int length = 0;
				Month mon = startDate.getMonth();
				if (mon.getValue() == 1) {
					length = Month.JANUARY.maxLength();
				}
				if (mon.getValue() == 2) {
					if (currentYear.isLeap())
						length = Month.FEBRUARY.maxLength();
					else
						length = Month.FEBRUARY.maxLength() - 1;
				}
				if (mon.getValue() == 3) {
					length = Month.MARCH.maxLength();
				}
				if (mon.getValue() == 4) {
					length = Month.APRIL.maxLength();
				}
				if (mon.getValue() == 5) {
					length = Month.MAY.maxLength();
				}
				if (mon.getValue() == 6) {
					length = Month.JUNE.maxLength();
				}
				if (mon.getValue() == 7) {
					length = Month.JULY.maxLength();
				}
				if (mon.getValue() == 8) {
					length = Month.AUGUST.maxLength();
				}
				if (mon.getValue() == 9) {
					length = Month.SEPTEMBER.maxLength();
				}
				if (mon.getValue() == 10) {
					length = Month.OCTOBER.maxLength();
				}
				if (mon.getValue() == 11) {
					length = Month.NOVEMBER.maxLength();
				}
				if (mon.getValue() == 12) {
					length = Month.DECEMBER.maxLength();
				}
				int dt = startDate.getDayOfMonth();
				startDate = startDate.minusDays(dt - 1);
				endDate = startDate.plusDays(length - 1);
				starting_month[mon.getValue() - 1] = startDate.toString();
				ending_month[mon.getValue() - 1] = endDate.toString();
				month[mon.getValue() - 1] = startDate.getMonth().toString();
				startDate = startDate.minusMonths(1);
			}

//			Collections.sort(starting_month);
//			Collections.sort(ending_month);
//			Collections.reverse(month);

			for (int i = 0; i <= starting_month.length - 1; i++) {
				totalCount = adminRepository.findAllAdminCurrentMonth(starting_month[i], ending_month[i]);
				if (totalCount != 0) {
					Long active = adminRepository.findAllCurrentMonthActiveAdminsCount(starting_month[i],
							ending_month[i]);
					MonthObject monthObject = new MonthObject();
					monthObject.setMonth(month[i]);
					monthObject.setCount(totalCount);
					monthObject.setActive(active);
					list.add(monthObject);
				} else {
					MonthObject monthObject = new MonthObject();
					monthObject.setMonth(month[i]);
					monthObject.setCount(totalCount);
					monthObject.setActive(0L);
					list.add(monthObject);
				}
			}
			graphResponse.setLabel("Registed Admins");
			int yearValue = currentYear.getValue();
			graphResponse.setYear(yearValue);
			graphResponse.setMonthList(list);
			map.put(Constant.RESPONSE_CODE, Constant.OK);
			map.put(Constant.MESSAGE, Constant.RECORD_FOUND);
			map.put(Constant.DATA, graphResponse);
			logger.info("data found" + graphResponse);
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.PLEASE_TRY_AGAIN);
			logger.error("Exception:-" + e.getMessage());
		}
		return map;
	}

	@Override
	public List<Admin> getAllAdminDetails(String keyword) {
		List<Admin> adminlist = new ArrayList<>();
//		Page<Admin> page = null;
//		Pageable pageable = PageRequest.of(searchRequestPayload.getPageIndex(), searchRequestPayload.getPageSize());
		if (keyword != "" && keyword != null) {
			adminlist = adminRepository.findAllSearchingAdmin(keyword);
		} else {
			adminlist = adminRepository.findAllAdmins();
		}
//		if (page != null && page.getContent().size() > Constant.ZERO) {
//			adminlist = page.getContent();
//		} else {
//			return adminlist;
//		}
		return adminlist;
	}

	@Override
	public Map<String, Object> getPasswordPolicy() {
		Map<String, Object> map = new HashMap<>();
		try {
			PasswordPolicy passwordPolicy = passwordPolicyRepository.findByTitle(Constant.PASSWORD_POLICY_TITTLE);
			if (passwordPolicy != null) {
				map.put(Constant.HTTP_STATUS, Constant.OK);
				map.put(Constant.MESSAGE, Constant.PASSWORD_POLICY_DATA_FOUND);
				map.put(Constant.DATA, passwordPolicy);
				log.info("Retrieved Password Policy successfully: " + passwordPolicy.getDescription());
			} else {
				map.put(Constant.HTTP_STATUS, Constant.NOT_FOUND);
				map.put(Constant.MESSAGE, Constant.PASSWORD_POLICY_NOT_FOUND);
				log.info("Password Policy not found" + passwordPolicy);
			}
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception: " + e.getMessage());
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.PLEASE_TRY_AGAIN);
			log.error("Error retrieving password policy: " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> saveAmwalPasswordDocs(PasswordPolicyRequestPayload passwordPolicyRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		try {
			if (passwordPolicyRequestPayload != null) {
				if (passwordPolicyRequestPayload.getTitle() != null
						&& !passwordPolicyRequestPayload.getTitle().isEmpty()
						&& passwordPolicyRequestPayload.getDescription() != null
						&& !passwordPolicyRequestPayload.getDescription().isEmpty()
						&& passwordPolicyRequestPayload.getPolicy_expiration_days() != null
						&& !passwordPolicyRequestPayload.getPolicy_expiration_days().isEmpty()) {

					// Retrieve existing password policy
					PasswordPolicy passwordPolicy = passwordPolicyRepository
							.findByTitle(passwordPolicyRequestPayload.getTitle());

					if (passwordPolicy != null) {
						passwordPolicy.setDescription(passwordPolicyRequestPayload.getDescription());
						passwordPolicy
								.setPolicy_expiration_days(passwordPolicyRequestPayload.getPolicy_expiration_days());
						passwordPolicyRepository.save(passwordPolicy);
						map.put(Constant.HTTP_STATUS, Constant.OK);
						map.put(Constant.MESSAGE, Constant.PASSWORD_POLICY_UPDATED);
						log.info("Updated Password Policy successfully: "
								+ passwordPolicyRequestPayload.getDescription());
					} else {
						// Create new password policy
						PasswordPolicy newPolicy = new PasswordPolicy();
						newPolicy.setTitle(passwordPolicyRequestPayload.getTitle());
						newPolicy.setDescription(passwordPolicyRequestPayload.getDescription());
						newPolicy.setPolicy_expiration_days(passwordPolicyRequestPayload.getPolicy_expiration_days());
						passwordPolicyRepository.save(newPolicy);
						map.put(Constant.HTTP_STATUS, Constant.OK);
						map.put(Constant.MESSAGE, Constant.PASSWORD_POLICY_CREATED);
						log.info(
								"Password Policy added successfully: " + passwordPolicyRequestPayload.getDescription());
					}
				} else {
					map.put(Constant.HTTP_STATUS, Constant.BAD_REQUEST);
					map.put(Constant.MESSAGE, Constant.TITLE_DESC_EXPIRATION_DAYS_CANT_BE_NULL);
					log.info("Title, description, and expiration days cannot be null: " + Constant.BAD_REQUEST);
				}
			} else {
				map.put(Constant.HTTP_STATUS, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.PAYLOAD_CANNOT_BE_NULL);
				log.info("Payload cannot be null: " + Constant.BAD_REQUEST);
			}
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception: " + e.getMessage());
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.PLEASE_TRY_AGAIN);
			log.error("Error updating password policy: " + e.getMessage());
		}
		return map;
	}

	/*
	 * @Override public Map<String, Object>
	 * saveAmwalPasswordDocs(PasswordPolicyRequestPayload
	 * passwordPolicyRequestPayload) { Map<String, Object> map = new HashMap<>();
	 * try { if (passwordPolicyRequestPayload != null) { PasswordPolicy
	 * passwordPolicy = passwordPolicyRepository
	 * .findByTitle(passwordPolicyRequestPayload.getTitle()); if (passwordPolicy !=
	 * null) { if
	 * (passwordPolicy.getDescription().equals(passwordPolicyRequestPayload.
	 * getDescription())) { map.put(Constant.HTTP_STATUS, Constant.OK);
	 * map.put(Constant.MESSAGE, Constant.AlREADY_UPDATED_PASSWORD_POLICY);
	 * log.info("Password Policy already updated: " +
	 * passwordPolicyRequestPayload.getDescription()); } else {
	 * passwordPolicy.setDescription(passwordPolicyRequestPayload.getDescription());
	 * passwordPolicyRepository.save(passwordPolicy); map.put(Constant.HTTP_STATUS,
	 * Constant.OK); map.put(Constant.MESSAGE, Constant.PASSWORD_POLICY_UPDATED);
	 * log.info("Updated Password Policy successfully: " +
	 * passwordPolicyRequestPayload.getDescription()); } } else { PasswordPolicy
	 * newPolicy = new PasswordPolicy();
	 * newPolicy.setTitle(passwordPolicyRequestPayload.getTitle());
	 * newPolicy.setDescription(passwordPolicyRequestPayload.getDescription());
	 * passwordPolicyRepository.save(newPolicy); log.info(" new data " + newPolicy);
	 * map.put(Constant.HTTP_STATUS, Constant.OK); map.put(Constant.MESSAGE,
	 * Constant.PASSWORD_POLICY); map.put(Constant.DATA, newPolicy);
	 * log.info("Password Policy added successfully: " +
	 * passwordPolicyRequestPayload.getDescription()); } } else {
	 * map.put(Constant.HTTP_STATUS, Constant.BAD_REQUEST);
	 * map.put(Constant.MESSAGE, Constant.MESSAGE_TYPE_NOT_NULL);
	 * log.info("Title cannot be null: " + Constant.BAD_REQUEST); } } catch
	 * (DataAccessResourceFailureException e) { map.put(Constant.RESPONSE_CODE,
	 * Constant.DB_CONNECTION_ERROR); map.put(Constant.MESSAGE,
	 * Constant.NO_DB_SERVER_CONNECTION); log.error("Exception: " + e.getMessage());
	 * } catch (Exception e) { map.put(Constant.RESPONSE_CODE,
	 * Constant.SERVER_ERROR); map.put(Constant.MESSAGE, Constant.PLEASE_TRY_AGAIN);
	 * log.error("Error updating password policy: " + e.getMessage()); } return map;
	 * }
	 */

	@Override
	public Map<String, Object> getAdminHistory(AdminHistoryPaginationPayload adminHistoryPaginationPayload) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<AdminHistoryResponse> adminHistoryResponseList = new ArrayList<AdminHistoryResponse>();
		List<AdminHistory> adminHistoryList = new ArrayList<AdminHistory>();
		Page<AdminHistory> page = null;
		if (adminHistoryPaginationPayload.getPageSize() > 0) {
			Pageable pageable = PageRequest.of(adminHistoryPaginationPayload.getPageIndex(),
					adminHistoryPaginationPayload.getPageSize());
			if (!adminHistoryPaginationPayload.getKeyWord().isBlank()) {
				page = adminLogsRepository.findAllAdminWithSearching(adminHistoryPaginationPayload.getKeyWord(),
						pageable);
			} else {
				page = adminLogsRepository.findAllAdminWithoutSearching(pageable);
			}
			if (page != null) {
				adminHistoryList = page.getContent();
				for (AdminHistory adminHistory : adminHistoryList) {
					AdminHistoryResponse historyResponse = new AdminHistoryResponse();
					RoleResponse roleResponse = new RoleResponse();
					BeanUtils.copyProperties(adminHistory, historyResponse);
					if(adminHistory.getRole()!=null) {
						roleResponse.setRoleName(!adminHistory.getRole().getRoleName().isBlank()?adminHistory.getRole().getRoleName():"null");
					}else {
						roleResponse.setRoleName(null);
					}
					historyResponse.setRole(roleResponse);
					historyResponse.setEntryDate(DateUtil.convertDateToStringDateTime(adminHistory.getEntryDate()));
					historyResponse
							.setUpdationDate(DateUtil.convertDateToStringDateTime(adminHistory.getUpdationDate()));
					historyResponse.setLastUpdationDate(
							DateUtil.convertDateToStringDateTime(adminHistory.getLastUpdationDate()));
					historyResponse.setPasswordUpdationDate(
							DateUtil.convertDateToStringDateTime(adminHistory.getPasswordUpdationDate()));
					adminHistoryResponseList.add(historyResponse);
				}
				Collections.reverse(adminHistoryResponseList);
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.DATA, adminHistoryResponseList);
				map.put(Constant.MESSAGE, Constant.ADMIN_HISTORY_FOUND_SUCCESSFULY);
				log.info(Constant.ADMIN_HISTORY_FOUND_SUCCESSFULY + Constant.OK);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
				map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
				map.put(Constant.DATA, page);
				log.info("Record not found! status - {}", page);
			}
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.PAGE_SIZE_MESSAGE);
			map.put(Constant.DATA, adminHistoryPaginationPayload);
			log.info(Constant.PAGE_SIZE_MESSAGE + Constant.BAD_REQUEST);
		}
		return map;
	}

}
