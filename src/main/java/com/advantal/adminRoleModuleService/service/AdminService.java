package com.advantal.adminRoleModuleService.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.advantal.adminRoleModuleService.models.Admin;
import com.advantal.adminRoleModuleService.requestpayload.AdminBlockRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.AdminHistoryPaginationPayload;
import com.advantal.adminRoleModuleService.requestpayload.AdminLoginRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.LoginRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.PasswordPolicyRequestPayload;

public interface AdminService {

//	Map<String, Object> addAdmin(AdminRequestPayload adminRequestPayload);

//	Map<String, Object> adminLogin(String email, String password);

	Map<String, Object> changePassword(Long id, String oldPassword, String password);

	Map<String, Object> getAllAdmin(Integer pageIndex, Integer pageSize, String keyWord);

	Map<String, Object> blockAdmin(AdminBlockRequestPayload adminBlockRequestPayload);

	Map<String, Object> deleteAdmin(Long id);

	Map<String, Object> uploadLanguageFile(MultipartFile file);

	Map<String, Object> forgetPassword(String email);

	Map<String, Object> resetPassword(Long id, String password);

	Map<String, Object> adminLogin(AdminLoginRequestPayload adminLoginRequestPayload);

	Map<String, Object> excelToJson();

	Map<String, Object> sendOtp(AdminLoginRequestPayload adminLoginRequestPayload);

	Map<String, Object> login(LoginRequestPayload loginRequestPayload);

	Map<String, Object> getAdmin();

//	Map<String, Object> getAdmin(Long id);

	Map<String, Object> addAdmin(String adminRequestPayload, MultipartFile file);

	Map<String, Object> getAdminProfile(Long id);

	Map<String, Object> getAdminGraph();

	List<Admin> getAllAdminDetails(String keyword);

	Map<String, Object> getPasswordPolicy();

	Map<String, Object> saveAmwalPasswordDocs(PasswordPolicyRequestPayload passwordPolicyRequestPayload);

	Map<String, Object> getAdminHistory(AdminHistoryPaginationPayload adminHistoryPaginationPayload);

}
