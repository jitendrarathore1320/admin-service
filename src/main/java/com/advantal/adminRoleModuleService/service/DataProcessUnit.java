package com.advantal.adminRoleModuleService.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advantal.adminRoleModuleService.models.PushNotification;
import com.advantal.adminRoleModuleService.models.User;
import com.advantal.adminRoleModuleService.repository.PushNotificationRepository;
import com.advantal.adminRoleModuleService.repository.UserRepository;
import com.advantal.adminRoleModuleService.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataProcessUnit {

	@Autowired
	PushNotificationRepository pushNotificationRepository;

	@Autowired
	UserRepository userRepository;

	public void USMarketOpen() {
		PushNotification pushNotification = pushNotificationRepository.findByNotificationType("US_MARKET_OPEN");
		if (pushNotification != null) {
			List<User> userList = userRepository.findAllUsers();
			if (userList != null && !userList.isEmpty()) {
				for (User user : userList) {
					sendMarketNotification(pushNotification, user.getDeviceToken());
				}
				log.info(Constant.SEND_NOTIFICATION_SUCCESSFULLY + " status - {} " + Constant.OK);
			} else {
				log.info(Constant.DATA_NOT_FOUND + " status - {} " + Constant.NOT_FOUND);
			}
		}
	}

	public void USMarketClose() {
//		log.info("notification added sucessfully !!" + notification);
		PushNotification pushNotification = pushNotificationRepository.findByNotificationType("US_MARKET_CLOSE");
		if (pushNotification != null) {
			List<User> userList = userRepository.findAllUsers();
			if (userList != null && !userList.isEmpty()) {
				for (User user : userList) {
					sendMarketNotification(pushNotification, user.getDeviceToken());
				}
				log.info(Constant.SEND_NOTIFICATION_SUCCESSFULLY + " status - {} " + Constant.OK);
			} else {
				log.info(Constant.DATA_NOT_FOUND + " status - {} " + Constant.NOT_FOUND);
			}
		}
	}

	public void SaudiMarketOpen() {
//		log.info("notification added sucessfully !!" + notification);
		PushNotification pushNotification = pushNotificationRepository.findByNotificationType("SAUDI_MARKET_OPEN");
		if (pushNotification != null) {
			List<User> userList = userRepository.findAllUsers();
			if (userList != null && !userList.isEmpty()) {
				for (User user : userList) {
					sendMarketNotification(pushNotification, user.getDeviceToken());
				}
				log.info(Constant.SEND_NOTIFICATION_SUCCESSFULLY + " status - {} " + Constant.OK);
			} else {
				log.info(Constant.DATA_NOT_FOUND + " status - {} " + Constant.NOT_FOUND);
			}
		}
	}

	public void SaudiMarketClose() {
//		log.info("notification added sucessfully !!" + notification);
		PushNotification pushNotification = pushNotificationRepository.findByNotificationType("SAUDI_MARKET_CLOSE");
		if (pushNotification != null) {
			List<User> userList = userRepository.findAllUsers();
			if (userList != null && !userList.isEmpty()) {
				for (User user : userList) {
					sendMarketNotification(pushNotification, user.getDeviceToken());
				}
				log.info(Constant.SEND_NOTIFICATION_SUCCESSFULLY + " status - {} " + Constant.OK);
			} else {
				log.info(Constant.DATA_NOT_FOUND + " status - {} " + Constant.NOT_FOUND);
			}
		}
	}

	private void sendMarketNotification(PushNotification pushNotification, String deviceToken) {
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
			info.put("title", pushNotification.getMessageTittle()); // Notification title
			info.put("body", pushNotification.getMessageBody());
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
