package com.advantal.adminRoleModuleService.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advantal.adminRoleModuleService.requestpayload.PushNotificationRequestPayload;
import com.advantal.adminRoleModuleService.service.PushNotificationService;

@RestController
@RequestMapping("/api/notification")
public class PushNotificationController {
	
	@Autowired
	PushNotificationService notificationService;

	@PostMapping("/add_notification")
	public Map<Object, Object> addNotification(@RequestBody PushNotificationRequestPayload noteFile)
			{
		return notificationService.addNotification(noteFile);
	}
}
