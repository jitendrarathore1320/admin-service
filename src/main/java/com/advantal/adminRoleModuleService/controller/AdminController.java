package com.advantal.adminRoleModuleService.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.advantal.adminRoleModuleService.requestpayload.AdminBlockRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.AdminHistoryPaginationPayload;
import com.advantal.adminRoleModuleService.requestpayload.AdminLoginRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.LoginRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.PasswordPolicyRequestPayload;
import com.advantal.adminRoleModuleService.service.AdminService;
import com.advantal.adminRoleModuleService.utils.Constant;

//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping(value = "/add_admin", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public Map<String, Object> addAdmin(@RequestPart(required = true) String adminRequestPayload,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		Map<String, Object> map = new HashMap<>();
		if (!adminRequestPayload.isBlank()) {
			return adminService.addAdmin(adminRequestPayload, file);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}

	}

	@PostMapping("/admin_login")
//	@ApiOperation(value = "Admin login !!")
	public Map<String, Object> adminLogin(@RequestBody @Valid AdminLoginRequestPayload adminLoginRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		if (adminLoginRequestPayload != null) {
			return adminService.adminLogin(adminLoginRequestPayload);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

	@PostMapping("/change_password")
//	@ApiOperation(value = "To change password !!")
	public Map<String, Object> changePassword(@RequestParam("id") Long id,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("password") String password) {
		return adminService.changePassword(id, oldPassword, password);
	}

	@PostMapping("/get_all_admin")
//	@ApiOperation(value = "Get admin list by search or direct !!")
	public Map<String, Object> getAllAdmin(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
			@RequestParam(required = false) String keyWord) {
		Map<String, Object> map = new HashMap<>();
		if (pageIndex != null && pageSize != null) {
			return adminService.getAllAdmin(pageIndex, pageSize, keyWord);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

	@PostMapping("/block_admin")
//	@ApiOperation(value = "To block/unblock admin !! send status value, 2 for block and 1 for unblock admin")
	public Map<String, Object> blockAdmin(@RequestBody AdminBlockRequestPayload adminBlockRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		if (adminBlockRequestPayload != null) {
			return adminService.blockAdmin(adminBlockRequestPayload);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

	@PostMapping("/delete_admin")
//	@ApiOperation(value = "Delete admin !!")
	public Map<String, Object> deleteAdmin(@RequestParam Long id) {
		return adminService.deleteAdmin(id);
	}

	@PostMapping(value = "/upload_language_file", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
//	@ApiOperation(value = "Upload language file !!")
	public Map<String, Object> uploadLanguageFile(@RequestPart(value = "file", required = true) MultipartFile file) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (file != null && !file.isEmpty()) {
			return adminService.uploadLanguageFile(file);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

	@PostMapping("/forget_Password")
//	@ApiOperation(value = "Forget password !!")
	public Map<String, Object> forgetPassword(@RequestParam String email) {
		return adminService.forgetPassword(email);
	}

	@PostMapping("/reset_password")
//	@ApiOperation(value = "Reset password !!")
	public Map<String, Object> resetPassword(@RequestParam(value = "id") Long id,
			@RequestParam(value = "password") String password) {
		return adminService.resetPassword(id, password);
	}

	@GetMapping("/get_json_data")
//	@ApiOperation(value = "excel !!")
	Map<String, Object> getExcel() {
		return adminService.excelToJson();
	}

	@PostMapping("/otp")
//	@ApiOperation(value = "Send OTP for login in 1- step !!")
	public Map<String, Object> sendOtp(@RequestBody @Valid AdminLoginRequestPayload adminLoginRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		if (adminLoginRequestPayload != null) {
			return adminService.sendOtp(adminLoginRequestPayload);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

	@PostMapping("/login")
//	@ApiOperation(value = "Verify OTP for login in 2- step !!")
	public Map<String, Object> login(@RequestBody @Valid LoginRequestPayload loginRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		if (loginRequestPayload != null) {
			return adminService.login(loginRequestPayload);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

	/* get all admin count */
	@GetMapping("/admin_count")
//	@ApiOperation(value = "excel !!")
	Map<String, Object> getAdmin() {
		return adminService.getAdmin();
	}

	@GetMapping("/profile")
	public Map<String, Object> getAdminProfile(@RequestParam Long id) {
		return adminService.getAdminProfile(id);
	}

	@GetMapping(value = "/admin_graph")
	public Map<String, Object> getUserGraph() {
		return adminService.getAdminGraph();
	}

	@PostMapping("/password_policy")
	public Map<String, Object> saveAmwalPasswordDocs(
			@RequestBody PasswordPolicyRequestPayload passwordPolicyRequestPayload) {
		return adminService.saveAmwalPasswordDocs(passwordPolicyRequestPayload);
	}

	@GetMapping(value = "/get_password_policy")
	public Map<String, Object> getPasswordPolicy() {
		return adminService.getPasswordPolicy();
	}
	
	@PostMapping(value = "/get_admin_history")
	public Map<String, Object> getAdminHistory(@RequestBody @Valid AdminHistoryPaginationPayload adminHistoryPaginationPayload) {
		return adminService.getAdminHistory(adminHistoryPaginationPayload);
	}
}