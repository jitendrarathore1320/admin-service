package com.advantal.adminRoleModuleService.serviceimpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

import com.advantal.adminRoleModuleService.models.PushNotification;
import com.advantal.adminRoleModuleService.models.User;
import com.advantal.adminRoleModuleService.repository.PushNotificationRepository;
import com.advantal.adminRoleModuleService.repository.UserRepository;
import com.advantal.adminRoleModuleService.requestpayload.PushNotificationRequestPayload;
import com.advantal.adminRoleModuleService.service.PushNotificationService;
import com.advantal.adminRoleModuleService.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PushNotificationServiceImpl implements PushNotificationService {

	@Autowired
	PushNotificationRepository pushNotificationRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public Map<Object, Object> addNotification(PushNotificationRequestPayload noteFile) {
		Map<Object, Object> map = new HashMap<>();
		PushNotification notification = new PushNotification();
		try {
			if (noteFile != null) {
				BeanUtils.copyProperties(noteFile, notification);
				notification.setCreationDate(new Date());
				pushNotificationRepository.save(notification);
				log.info("notification added sucessfully !!" + notification);
				List<User> userList = userRepository.findAllUsers();
				if (userList != null && !userList.isEmpty()) {
					for (User user : userList) {
//						if (user.getDeviceType().equalsIgnoreCase(Constant.ANDROID)) {
//							// Sending push notification to Android devices using FCM
////							sendFCMPushNotification(noteFile, user.getDeviceToken(), user.getDeviceType());
//						} else if (user.getDeviceType().equalsIgnoreCase(Constant.IOS)) {
//							// Sending push notification to iOS devices using APNs
////							sendAPNsPushNotification(noteFile, user.getDeviceToken(), user.getDeviceType());
//						}
						sendNotification(noteFile, user.getDeviceToken());
					}
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.SEND_NOTIFICATION_SUCCESSFULLY);
					log.info(Constant.SEND_NOTIFICATION_SUCCESSFULLY + " status - {} " + Constant.OK);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND);
					map.put(Constant.DATA, userList);
					log.info(Constant.DATA_NOT_FOUND + " status - {} " + Constant.NOT_FOUND);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
				log.info(Constant.BAD_REQUEST_MESSAGE + " status - {} " + Constant.BAD_REQUEST);
			}
		} catch (DataAccessResourceFailureException e) {
			log.error("Database connection error: " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
		} catch (Exception e) {
			log.error("Exception: " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
		}
		return map;
	}

	private void sendNotification(PushNotificationRequestPayload notificationRequestPayload, String deviceToken) {
//		String result = "";
		try {
			URL url = new URL("https://fcm.googleapis.com/fcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "key=" + Constant.FIREBASE_KEY);
			conn.setRequestProperty("Content-Type", "application/json");
			JSONObject json = new JSONObject();
			json.put("to", deviceToken.trim());
			JSONObject info = new JSONObject();
			info.put("title", notificationRequestPayload.getMessageTittle()); // Notification title
			info.put("body", notificationRequestPayload.getMessageBody());
			json.put("notification", info);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(json.toString());
			wr.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
//			result = "succcess";
		} catch (Exception e) {
			e.printStackTrace();
//			result = "failure";
		}
	}

}
