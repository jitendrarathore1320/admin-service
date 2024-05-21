package com.advantal.adminRoleModuleService.service;

import java.util.Map;

import com.advantal.adminRoleModuleService.requestpayload.PushNotificationRequestPayload;

public interface PushNotificationService {

	Map<Object, Object> addNotification(PushNotificationRequestPayload noteFile);

}
