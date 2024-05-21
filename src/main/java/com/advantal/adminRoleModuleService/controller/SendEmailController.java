package com.advantal.adminRoleModuleService.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.advantal.adminRoleModuleService.requestpayload.SendEmailRequest;
import com.advantal.adminRoleModuleService.utils.Constant;
import com.advantal.adminRoleModuleService.utils.HtmlTemplate;
import com.advantal.adminRoleModuleService.utils.SendMail;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/email")
@Slf4j
//@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class SendEmailController {

	@Autowired
	private SendMail sendMail;

	@PostMapping(value = "/sendEmail")
	public Map<String, Object> sendEmail(@RequestBody SendEmailRequest sendEmailRequest) {
		log.info("Sending email: {}", sendEmailRequest);
		Map<String, Object> map = new HashMap<>();
		try {
			String messageBody = HtmlTemplate.sendEmailForAdminDashboard(sendEmailRequest.getFromEmail(),
					sendEmailRequest.getToEmail(), sendEmailRequest.getMessage());
			sendMail.sendMailTemplate(sendEmailRequest.getToEmail(), sendEmailRequest.getSubject(), messageBody);
			// Pass the userType to your service method
			map.put(Constant.RESPONSE_CODE, Constant.OK);
			map.put(Constant.MESSAGE, Constant.EMAIL_SEND_SUCCESSFULLY);
			// map.put(Constant.DATA, messageBody);
			return map;
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception occurred: {}", e.getMessage(), e);
			return map;
		}
	}
}